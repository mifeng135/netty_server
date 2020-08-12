# 基于netty,nanomsg实现


# 项目依赖
* java 8
* mysql
* redis

# 项目部署
### windows项目部署
* 1.将nanomsg.dll 拷贝到 java jdk下的bin目录
* 2.启动jar包即可

### liunx项目部署
* 将windows下编译好的jar中直接启动即可


# 描述

### login
* 1.login http服务器，使用netty实现。每个gate服务器都连接到登录服务器，定义同步gate连接数用来做负载均衡。 
* 2.login服务器连接redis和mysql，登录时先从redis读取，如果没有在执行sql查询。更新时先更新redis，
* 3.mysql更新时先放到mysql批处理线程中一次性更新，现在暂定30秒执行一次批处理。

### gate
* 1.gate服务器使用netty webosokcet,因为是针对cocos creator 的websocekt所以没有实现自定义协议解析。
* 2.服务器消息转发使用nanomsg 

### 消息顺序处理
为了保证多线程消息处理的顺序先后，消息转发的时候，根据megbean转发过来的id（玩家id）进行取余。保证同一个玩家处理消息都是在同一线程。


# QQ群
### 1084098050













