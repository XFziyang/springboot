package com.demo.quartz;

import com.demo.quartz.entity.JobVO;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/quartz")
public class QuartzController {

    @RequestMapping("/add")
    public String add(@RequestBody JobVO jobVO) throws Exception {
        QuartzScheduler.addJob(jobVO.getId(), jobVO.getGroup(), jobVO.getCron(), jobVO.getData());
        return "ok";
    }

}

