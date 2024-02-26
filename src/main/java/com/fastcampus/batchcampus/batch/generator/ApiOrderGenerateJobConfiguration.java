package com.fastcampus.batchcampus.batch.generator;

import com.fastcampus.batchcampus.domain.ApiOrder;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.DefaultJobParametersValidator;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.builder.FlatFileItemWriterBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.PathResource;
import org.springframework.transaction.PlatformTransactionManager;

//@Configuration
@RequiredArgsConstructor
public class ApiOrderGenerateJobConfiguration {

    private final JobRepository jobRepository;
    private final PlatformTransactionManager platformTransactionManager;

    @Bean
    public Job apiOrderGenerateJob(Step step) {
        return new JobBuilder("apiOrderGenerateJob", jobRepository)
                .start(step)
                .incrementer(new RunIdIncrementer())
                .validator(new DefaultJobParametersValidator(new String[]{"targetDate", "totalCount"}, new String[0]))
                .build();
    }

    @Bean
    public Step step(
            ApiOrderGenerateReader apiOrderGenerateReader,
            ApiOrderGenerateProcessor apiOrderGenerateProcessor
    ) {
        return new StepBuilder("apiOrderGenerateStep", jobRepository)
                .<Boolean, ApiOrder>chunk(5000,platformTransactionManager)
                .reader(apiOrderGenerateReader) // totalCount만큼 돌게 설정
                .processor(apiOrderGenerateProcessor) // 랜덤하게 ApiOrder 객체를 만듬
                .writer(apiOrderFlatFileItemWriter(null)) // 해당 내용으로 파일을 떨굼
                .build();
    }

    @Bean
    @StepScope // 언제적 날짜로 받고 싶은지 job parameter로 받고싶음
    public FlatFileItemWriter<ApiOrder> apiOrderFlatFileItemWriter(
            @Value("#{jobParameters['targetDate']}") String targetDate
    ) {
        final String fileName = targetDate + "_api_orders.csv";

        return new FlatFileItemWriterBuilder<ApiOrder>()
                .name("apiOrderFlatFileItemWriter")
                .resource(new PathResource("src/main/resources/datas" + fileName))
                .delimited()
                .names("id", "customerId", "url", "state", "createdAt")
                .headerCallback(writer -> writer.write("id, customerId, url, state, createdAt"))
                .build();
    }
}
