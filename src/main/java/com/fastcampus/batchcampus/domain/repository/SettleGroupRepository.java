package com.fastcampus.batchcampus.domain.repository;

import com.fastcampus.batchcampus.domain.SettleGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface SettleGroupRepository extends JpaRepository<SettleGroup, Long> {

    @Query(
            value = """
                    SELECT new SettleGroup(detail.customer_id, detail.service_id, sum(detail.count), sum(detail.fee))
                    FROM settle_detail detail
                    WHERE detail.target_date between : start and :end
                    AND detail.customer_id = :customerId
                    GROUP BY detail.customer_id, detail.service_id
                    """
    )
    List<SettleGroup> findGroupByCustomerIdAndServiceId(LocalDate start, LocalDate end, Long customerId);

}
