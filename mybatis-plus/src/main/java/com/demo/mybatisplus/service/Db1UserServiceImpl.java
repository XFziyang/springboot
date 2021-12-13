package com.demo.mybatisplus.service;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.demo.mybatisplus.entity.UserEntity;
import com.demo.mybatisplus.mapper.Db1UserMapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@DS("db1")
public class Db1UserServiceImpl extends ServiceImpl<Db1UserMapper, UserEntity> {

    public List<UserEntity> getAll() {
        return super.list();
    }
}
