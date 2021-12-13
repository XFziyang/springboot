package com.demo.mybatisplus.controller;

import com.demo.mybatisplus.service.Db1UserServiceImpl;
import com.demo.mybatisplus.service.Db2UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class UserController {

    @Autowired
    private Db1UserServiceImpl db1UserService;
    @Autowired
    private Db2UserServiceImpl db2UserService;

    @RequestMapping("/db1")
    private Object db1UserList() {
        return db1UserService.getAll();
    }

    @RequestMapping("/db2")
    private Object db2UserList() {
        return db2UserService.getAll();
    }
}
