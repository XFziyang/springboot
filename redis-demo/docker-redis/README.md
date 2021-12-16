redis 容器部署 （伪集群部署）


1. 下载redis镜像
    
    docker pull redis
    
2. 初始化配置文件和data存储目录

    bash init.sh

3. 启动容器  (注意：绝对路径)

    bash start.sh

4. 进入任意容器，创建redis集群

    docker exec -it redis-7000 /bin/bash

    redis-cli --cluster create \
    10.10.20.170:7000 10.10.20.170:7001 10.10.20.170:7002 10.10.20.170:7003 10.10.20.170:7004 10.10.20.170:7005 \
    --cluster-replicas 1


 