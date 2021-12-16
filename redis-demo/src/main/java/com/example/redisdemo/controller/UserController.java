package com.example.redisdemo.controller;

import com.example.redisdemo.service.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserServiceImpl userService;

    @RequestMapping("/get")
    private Object getById(@RequestParam("id") int id) {
        return userService.getById(id);
    }

    @RequestMapping("/add")
    private Object add(@RequestParam("name") String name) {
        return userService.save(name);
    }

    @RequestMapping("/delete")
    private String delete(@RequestParam("id") int id) {
        userService.delete(id);
        return "ok";
    }


}
