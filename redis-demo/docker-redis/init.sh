#!/bin/bash

# 初始化目录
baseDir="redis-cluster"

mkdir -p ${baseDir}
cd ${baseDir}

for port in `seq 7000 7005`; do
        echo "port=${port}"
        mkdir -p ${port}/conf
        export port=${port}
        envsubst '${port}' < ../redis-cluster.tmpl > ${port}/conf/redis.conf
        mkdir -p ${port}/data
done
