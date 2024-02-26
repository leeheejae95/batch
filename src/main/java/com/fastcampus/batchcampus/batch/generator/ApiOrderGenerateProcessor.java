package com.fastcampus.batchcampus.batch.generator;

import com.fastcampus.batchcampus.domain.ApiOrder;
import com.fastcampus.batchcampus.domain.ServicePolicy;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.LongStream;

@Component
public class ApiOrderGenerateProcessor implements ItemProcessor<Boolean, ApiOrder> {

    private final List<Long> customerId = LongStream.range(0,20).boxed().toList(); // 0~20까지의 list가 만들어짐
    private final List<ServicePolicy> servicePolicies = Arrays.stream(ServicePolicy.values()).toList(); // A~Z까지 들어있음
    private final ThreadLocalRandom random = ThreadLocalRandom.current(); // 랜덤 뽑기
    private final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");

    @Override
    public ApiOrder process(Boolean item) throws Exception {

        final Long randomCustomerId = customerId.get(random.nextInt(customerId.size()));// 랜덤한 customerId획득
        final ServicePolicy randomServicePolicy = servicePolicies.get(random.nextInt(servicePolicies.size()));
        final ApiOrder.State randomState = random.nextInt(5) % 5 == 1 ? ApiOrder.State.FAIL : ApiOrder.State.SUCCESS; // 5번중 1번은 실패 4번은 성공

        // reader가 100번돌면서 랜덤하게 100번데이터를 만들어줌
        return new ApiOrder(
                UUID.randomUUID().toString(),
                randomCustomerId,
                randomServicePolicy.getUrl(),
                randomState,
                LocalDateTime.now().format(dateTimeFormatter)
        );
    }

}
