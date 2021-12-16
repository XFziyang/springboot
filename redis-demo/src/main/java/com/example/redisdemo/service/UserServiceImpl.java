package com.example.redisdemo.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.redisdemo.entity.UserEntity;
import com.example.redisdemo.mapper.UserMapper;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;


@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, UserEntity> {

    /**
     *  @Cacheable
     *  value: 缓存区名称
     *  key: 主键
     *  根据主键值从指定缓存区中获取结果
     *      如找到： 返回缓存对象
     *      如未找到： 执行方法，并将返回值放入缓存
     */
    @Cacheable(value = "users", key = "#id")
    public UserEntity getById(int id) {
        return super.getById(id);
    }


    /**
     *  @CachePut
     *  value: 缓存区名称
     *  直接执行方法，并将返回值存入缓存
     */
    @CachePut(value = "users")
    public UserEntity save(String name) {
        UserEntity userEntity = new UserEntity();
        userEntity.setName(name);
        super.save(userEntity);
        return userEntity;
    }


    /**
     * @CacheEvict
     *  value: 缓存区名称
     *  key: 主键
     *  执行方法成功后(无异常抛出)，根据key从缓存中删除对象
     */
    @CacheEvict(value = "users", key = "#id")
    public void delete(int id) {
        super.removeById(id);
    }

}
