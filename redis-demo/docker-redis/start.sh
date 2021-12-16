#!/bin/bash
# 启动redis容器

# 配置文件绝对路径
baseDir=/data/zhouziyang/redis/

for port in `seq 7000 7005`; do
        echo "port:${port}"
        docker stop redis-${port}
        docker rm redis-${port}
        docker run -dit -p ${port}:${port} -p 1${port}:1${port} \
        -v ${baseDir}/redis-cluster/${port}/conf/redis.conf:/usr/local/etc/redis/redis.conf \
        -v ${baseDir}/redis-cluster/${port}/data:/data \
        --restart always --name redis-${port} --sysctl net.core.somaxconn=1024 \
        redis redis-server /usr/local/etc/redis/redis.conf
done
