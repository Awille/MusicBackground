# MusicBackground
一个小型音乐系统后台

## 开发问题
### 1、非原子操作怎么转化成原子操作
在某些情境之下，对数据库中某个表做某条记录的删除，这时候由于一些外键约束，
需要先删除其他表中与这条记录有关的其他记录。这里其实进行的是非原子操作，
但是在业务形态上要求的是原子操作，这些对数据库操作的非原子操作是可能面临
失败的，这时候就要涉及回退的问题，在最坏的情况下，回退也失败的问题该如何
解决呢？

### 2、触发器问题
非常遗憾，MYSQL中触发器中不能对本表进行 insert ,update ,delete 操作，
以免递归循环触发。

### 3、数据库连接池druid的问题
在写好配置文件以后，用DruidDataSourceFactory.createDataSource发现总是
包classNotFoundException，后来发现是需要把druid的jar包放到tomcat的lib目录之下
，有关这个问题好像在mysql-connector的jar包也要放到tomcat那里。


### 4、REST 模式下的 API 设计
GET: 查询
POST: 增加
PUT:修改
DELETE: 删除

定义业务上的同意请求体
```$java
class request() {
    //请求服务
    String service;
    //请求数据
    Object data;
}
```

#### 4.1 USER 设计
- 查询用户 ：GET方法，根据Account查询
- 更新用户：PUT方法 
service 101 更改用户信息 
service 102 更改密码
service 103 更改头像
- 添加用户： POST
- 删除用户：DELETE