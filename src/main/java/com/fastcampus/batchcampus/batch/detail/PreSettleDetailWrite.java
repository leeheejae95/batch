package com.fastcampus.batchcampus.batch.detail;

import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Component
public class PreSettleDetailWrite implements ItemWriter<Key>, StepExecutionListener {

    private StepExecution stepExecution; // StepExecution에 저장하기 위해 선언

    // step이 시작하기전에
    @Override
    public void beforeStep(StepExecution stepExecution) {
        this.stepExecution = stepExecution;

        final ConcurrentMap<Key, Long> snapshotMap = new ConcurrentHashMap<>();
        stepExecution.getExecutionContext().put("snapshots", snapshotMap);
    }

    @Override
    public void write(Chunk<? extends Key> chunk) throws Exception {
        final ConcurrentMap<Key, Long> snapshotMap = (ConcurrentMap<Key, Long>) stepExecution.getExecutionContext().get("snapshots");
        chunk.forEach(key -> {
            snapshotMap.compute( // 충돌방지
                    key,
                    (k,v) -> (v == null) ? 1 : v + 1 // 처음 이용할때는 null이라 1로 셋팅 그다음 부터 이용하면 +1해줌
            );
        });
    }
}

// JOB - (Step1, Step2)
// Step1에만 StepExecution snapshots을 넣음
// Step2에서는 Step1에 StepExecution에 접근할 수 없음
// 그래서 Step1에 StepExecution을 Job으로 올려줘야됨
// 그래야 Step2에서도 사용이 가능
// SettleDetailStepConfiguration에 ExecutionContextPromotionListener로 구현