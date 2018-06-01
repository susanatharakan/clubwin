package co.uk.mcb.clubwin.config;

import co.uk.mcb.clubwin.ClubWinItemProcessor;
import co.uk.mcb.clubwin.listener.JobCompletionNotificationListener;
import co.uk.mcb.clubwin.model.ClubWin;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.Database;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;

import javax.sql.DataSource;
import java.util.Properties;

@Configuration
@EnableBatchProcessing
@ComponentScan
@EnableAutoConfiguration
public class BatchConfiguration {

    @Autowired
    public JobBuilderFactory jobBuilderFactory;

    @Autowired
    public StepBuilderFactory stepBuilderFactory;

    @Autowired
    public DataSource dataSource;

    @Value("${batch.file}")
    private String batchFileName;

    @Bean
    public ItemReader<ClubWin> reader(){
        FlatFileItemReader<ClubWin> reader = new FlatFileItemReader<ClubWin>();
        reader.setResource(new ClassPathResource(batchFileName));
        reader.setLineMapper(new DefaultLineMapper<ClubWin>(){{
            setLineTokenizer(new DelimitedLineTokenizer(){{
                setNames(new String[] {"winCode"});
            }});
            setFieldSetMapper(new BeanWrapperFieldSetMapper<ClubWin>() {{
                setTargetType(ClubWin.class);
            }});
        }});
        return reader;
    }



    /**
     * The ClubWinItemProcessor is called after a new line is read and it allows the developer
     * to transform the data read
     * In our example it simply return the original object
     *
     * @return
     */
    @Bean
    public ClubWinItemProcessor processor(){
        return new ClubWinItemProcessor();
    }


    /**
     * Nothing special here a simple JpaItemWriter
     * @return
     */
    @Bean
    public ItemWriter<ClubWin> writer() {
        JpaItemWriter writer = new JpaItemWriter<ClubWin>();
        writer.setEntityManagerFactory(entityManagerFactory().getObject());
        return writer;
    }


    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory() {

        LocalContainerEntityManagerFactoryBean lef = new LocalContainerEntityManagerFactoryBean();
        lef.setPackagesToScan("co.uk.mcb.clubwin");
        lef.setDataSource(dataSource);
        lef.setJpaVendorAdapter(jpaVendorAdapter());
        lef.setJpaProperties(new Properties());
        return lef;
    }


    @Bean
    public JpaVendorAdapter jpaVendorAdapter() {
        HibernateJpaVendorAdapter jpaVendorAdapter = new HibernateJpaVendorAdapter();
        jpaVendorAdapter.setDatabase(Database.MYSQL);
        jpaVendorAdapter.setGenerateDdl(true);
        jpaVendorAdapter.setShowSql(false);

        jpaVendorAdapter.setDatabasePlatform("org.hibernate.dialect.MySQLDialect");
        return jpaVendorAdapter;
    }


    @Bean
    public Step step1(StepBuilderFactory stepBuilderFactory, ItemReader<ClubWin> reader,
                      ItemWriter<ClubWin> writer, ItemProcessor<ClubWin, ClubWin> processor){
        return stepBuilderFactory.get("step1")
                .<ClubWin, ClubWin> chunk(100)
                .reader(reader)
                .processor(processor)
                .writer(writer)
                .build();

    }
/*
    @Bean
    public Job importClubWin(JobBuilderFactory jobs, Step s1) {
        return jobs.get("import")
                .incrementer(new RunIdIncrementer()) // because a spring config bug, this incrementer is not really useful
                .flow(s1)
                .end()
                .build();
    }*/


    @Bean
    public Job importClubWin(JobCompletionNotificationListener listener, Step s1) {
        return jobBuilderFactory.get("import")
                .incrementer(new RunIdIncrementer())
                .listener(listener)// because a spring config bug, this incrementer is not really useful
                .flow(s1)
                .end()
                .build();
    }


}
