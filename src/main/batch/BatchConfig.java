package com.satvik.stockpdfspringboot.batch;

import jakarta.persistence.EntityManagerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.extensions.excel.poi.PoiItemReader;
import org.springframework.batch.item.ItemStreamReader;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.transaction.PlatformTransactionManager;


@Configuration
public class BatchConfig {

    @Value("${app.links-path}")
    private String linksPath;

    @Autowired
    private JobRepository jobRepository;

    @Autowired
    private PlatformTransactionManager transactionManager;

    @Autowired
    private EntityManagerFactory entityManagerFactory;

    @Bean
    ItemStreamReader<StockLink> reader() {
        PoiItemReader<StockLink> reader = new PoiItemReader<>();
        reader.setResource(new ClassPathResource(linksPath));
        reader.setRowMapper(new RowMapperImpl());
        reader.setLinesToSkip(1);
        return reader;
    }

    @Bean
    public JpaItemWriter<StockLink> writer() {
        JpaItemWriter<StockLink> writer = new JpaItemWriter<>();
        writer.setEntityManagerFactory(entityManagerFactory);
        return writer;
    }


    @Bean
    public Step step1() {
        return new StepBuilder("step1", jobRepository)
                .<StockLink, StockLink>chunk(10,transactionManager)
                .reader(reader())
                .writer(writer())
                .transactionManager(transactionManager)
                .build();
    }

    @Bean
    public Job importStockJob() {
        return new JobBuilder("jobA", jobRepository)
                .start(step1())
                .build();
    }
}
