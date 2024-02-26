package com.fastcampus.batchcampus.batch.detail;

import com.fastcampus.batchcampus.domain.ApiOrder;
import com.fastcampus.batchcampus.domain.ServicePolicy;
import org.springframework.batch.item.ItemProcessor;

/**
 * state를 보는 역할 -> 실패 했는데 요금을 부과하면 안되기 때문
 */
public class PreSettleDetailProcessor implements ItemProcessor<ApiOrder, Key> {
    @Override
    public Key process(ApiOrder item) throws Exception {
        if(item.getState() == ApiOrder.State.FAIL) {
            return null;
        }

        final Long serviceId = ServicePolicy.findByUrl(item.getUrl()).getId();

        return new Key(
                item.getCustomerId(),
                serviceId
        );
    }
}
