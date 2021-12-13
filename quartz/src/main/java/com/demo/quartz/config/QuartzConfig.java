package com.demo.quartz.config;

import com.demo.quartz.QuartzScheduler;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

import javax.sql.DataSource;


@Configuration
@Slf4j
public class QuartzConfig {

    @Autowired
    private SpringScheduleJobFactory springScheduleJobFactory;

    @Bean
    public SchedulerFactoryBean schedulerFactoryBean(@Qualifier("quartzDataSource")DataSource dataSource) {
        SchedulerFactoryBean schedulerFactoryBean = new SchedulerFactoryBean();
        schedulerFactoryBean.setDataSource(dataSource);
        schedulerFactoryBean.setAutoStartup(true);
        //延长启动
        schedulerFactoryBean.setStartupDelay(10);
        // 用于quartz集群,QuartzScheduler 启动时更新己存在的Job
        // schedulerFactoryBean.setOverwriteExistingJobs(true);

        //设置加载的配置文件
        schedulerFactoryBean.setConfigLocation(new ClassPathResource("quartz.properties"));
        schedulerFactoryBean.setJobFactory(springScheduleJobFactory);
        log.info("init scheduler success.");
        return schedulerFactoryBean;
    }

    @Bean(initMethod = "start", destroyMethod = "destroy")
    public QuartzScheduler scheduler(@Qualifier("schedulerFactoryBean") SchedulerFactoryBean schedulerFactoryBean) {
        Scheduler scheduler = schedulerFactoryBean.getScheduler();
        QuartzScheduler.setScheduler(scheduler);
        QuartzScheduler quartzScheduler = new QuartzScheduler();
        log.info("Init QuartzScheduler success");
        return quartzScheduler;
    }
}


