package com.demo.quartz;

import com.demo.quartz.executor.QuartzJob;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.quartz.impl.matchers.GroupMatcher;

import java.util.*;

@Slf4j
public class QuartzScheduler {

    private static Scheduler scheduler;

    public static void setScheduler(Scheduler scheduler) {
        QuartzScheduler.scheduler = scheduler;
    }

    public void start() { log.info("==============================scheduler started============================");}

    public void destroy() { log.info("==============================scheduler destroyed============================");}


    /**
     * 新增调度任务
     * @param jobId
     * @param group
     * @param cronStr
     * @param jobData
     * @return
     * @throws SchedulerException
     */
    public static boolean addJob(String jobId, String group, String cronStr, Map<String, Object> jobData) throws SchedulerException {
        JobKey jobKey = new JobKey(jobId, group);
        TriggerKey triggerKey = TriggerKey.triggerKey(jobId, group);
        if(scheduler.checkExists(triggerKey)) {
            log.info("trigger already exists, triggerKey: {}", triggerKey);
            return true;
        }
        JobDetail jobDetail = JobBuilder.newJob(QuartzJob.class).withIdentity(jobKey).build();

        if(jobData != null && !jobData.isEmpty()) {
            jobDetail.getJobDataMap().putAll(jobData);
        }
        CronScheduleBuilder cron = CronScheduleBuilder.cronSchedule(cronStr).withMisfireHandlingInstructionDoNothing();
        CronTrigger trigger = TriggerBuilder.newTrigger().withIdentity(triggerKey).startNow().withSchedule(cron).build();
        Date date = scheduler.scheduleJob(jobDetail, trigger);
        log.info("add job success, jobDetail {}, cronTrigger: {}, date:{}", jobDetail, trigger, date);
        return true;
    }

    /**
     * 删除定时调度任务
     * @param jobId
     * @param group
     * @return
     * @throws SchedulerException
     */
    public static boolean removeJob(String jobId, String group) throws SchedulerException {
        TriggerKey triggerKey = TriggerKey.triggerKey(jobId, group);
        if(scheduler.checkExists(triggerKey)) {
            scheduler.unscheduleJob(triggerKey);
            log.info("trigger remove success, triggerKey: {}", triggerKey);
        } else {
            log.info("trigger does not exists, triggerKey: {}", triggerKey);
        }
        return true;
    }

    /**
     * 更新调度任务
     * @param jobId
     * @param group
     * @param cronStr
     * @param jobData
     * @return
     * @throws SchedulerException
     */
    public static boolean updateJob(String jobId, String group, String cronStr, Map<String, Object> jobData) throws SchedulerException {
        TriggerKey triggerKey = TriggerKey.triggerKey(jobId, group);
        if(!scheduler.checkExists(triggerKey)) {
            log.info("trigger does not exists, triggerKey: {}", triggerKey);
            return true;
        }
        CronTrigger oldCronTrigger = (CronTrigger) scheduler.getTrigger(triggerKey);
        String oldCron = oldCronTrigger.getCronExpression();

        // 设置新作业信息
        JobKey jobKey = new JobKey(jobId, group);
        JobDetail jobDetail = scheduler.getJobDetail(jobKey);

        if(jobData != null && !jobData.isEmpty()) {
            jobDetail.getJobDataMap().putAll(jobData);
        }
        scheduler.addJob(jobDetail, true, true);
        if(oldCron.equals(cronStr)) {
            log.info("cron same, oldCron: {}", oldCron);
            return true;
        }

        CronScheduleBuilder cron = CronScheduleBuilder.cronSchedule(cronStr).withMisfireHandlingInstructionDoNothing();
        oldCronTrigger = oldCronTrigger.getTriggerBuilder().withIdentity(triggerKey).withSchedule(cron).build();
        Date date = scheduler.rescheduleJob(triggerKey, oldCronTrigger);
        log.info("update job success, cronTrigger: {}, date:{}", oldCronTrigger, date);
        return true;
    }

    /**
     * 暂停调度
     * @param jobId
     * @param group
     * @return
     * @throws SchedulerException
     */
    public static boolean parseJob(String jobId, String group) throws SchedulerException {
        JobKey jobKey = new JobKey(jobId, group);
        scheduler.pauseJob(jobKey);
        log.info("pause job success, jobKey: {}", jobKey);
        return true;
    }

    /**
     * 恢复调度
     * @param jobId
     * @param group
     * @return
     * @throws SchedulerException
     */
    public static boolean resumeJob(String jobId, String group) throws SchedulerException {
        JobKey jobKey = new JobKey(jobId, group);
        scheduler.resumeJob(jobKey);
        log.info("resume job success, jobKey: {}", jobKey);
        return true;
    }

    /**
     * 立即执行一个任务
     * @param jobId
     * @param group
     * @return
     * @throws SchedulerException
     */
    public static boolean runJobNow(String jobId, String group) throws SchedulerException {
        JobKey jobKey = new JobKey(jobId, group);
        scheduler.triggerJob(jobKey);
        log.info("run job success, jobKey: {}", jobKey);
        return true;
    }

    /**
     * 获取所有计划中的任务
     * @return
     * @throws SchedulerException
     */
    public static List<Map> fetchAllJob() throws SchedulerException {
        List<Map> list = new ArrayList<>();
        GroupMatcher<JobKey> jobKeyGroupMatcher = GroupMatcher.anyJobGroup();
        Set<JobKey> jobKeys = scheduler.getJobKeys(jobKeyGroupMatcher);
        for(JobKey jobKey: jobKeys) {
            List<? extends Trigger> triggers = scheduler.getTriggersOfJob(jobKey);
            for(Trigger trigger : triggers) {
                Map<String, String> map = new HashMap<>();
                map.put("jobName", jobKey.getName());
                map.put("jobGroupName", jobKey.getGroup());
                map.put("triggerKey", trigger.getKey().toString());
                Trigger.TriggerState triggerState = scheduler.getTriggerState(trigger.getKey());
                map.put("jobStatus", triggerState.name());
                if(trigger instanceof CronTrigger) {
                    String expression = ((CronTrigger) trigger).getCronExpression();
                    map.put("jobCron", expression);
                }
                list.add(map);
            }
        }
        return list;
    }

    /**
     * 获取所有正在执行中的任务
     * @return
     * @throws SchedulerException
     */
    public static List<Map> fetchRunJob() throws SchedulerException {
        List<Map> list = new ArrayList<>();
        List<JobExecutionContext> currentlyExecutingJobs = scheduler.getCurrentlyExecutingJobs();
        for(JobExecutionContext currentlyExecutingJob : currentlyExecutingJobs) {
            Map<String, String> map = new HashMap<>();
            JobDetail jobDetail = currentlyExecutingJob.getJobDetail();
            JobKey jobKey = jobDetail.getKey();
            Trigger trigger = currentlyExecutingJob.getTrigger();
            map.put("jobName", jobKey.getName());
            map.put("jobGroupName", jobKey.getGroup());
            map.put("triggerKey", trigger.getKey().toString());
            Trigger.TriggerState triggerState = scheduler.getTriggerState(trigger.getKey());
            map.put("jobStatus", triggerState.name());
            if(trigger instanceof CronTrigger) {
                String expression = ((CronTrigger) trigger).getCronExpression();
                map.put("jobCron", expression);
            }
            list.add(map);
        }
        return list;
    }
}
