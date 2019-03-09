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
service 103 更改/上传 头像
- 添加用户： POST
- 删除用户：DELETE
删除用户操作暂时不做，就算要做也只是采取一些特殊方式。
如果正儿八经的进行删除的话，需要遍历整个数据库删除有关此
用户的所有信息，这些信息可能与其他用户或者资源有交叉，
贸然在用户表中删除该用户信息可能对系统的正常运行造成
很大的影响。

#### 4.2 SONG 设计
- 查询歌曲信息 : GET方法 根据songId或者
word关键词 进行查询
word进行模糊查询 两个key不能同时为非null，查询时以非null
的key决定查询方式

- 歌曲上传 ：POST方法
service 201 : 歌曲文件
service 202 : 歌词文件
service 203 : 歌曲基本信息
service 204 : 上传歌曲头像

- PUT DELETE 歌曲信息更新 删除操作不开放

#### 4.3 SONGLIST 设计
- GET
两个参数 account 与 songListId
两个参数不能同时不为null
account 查询用户的歌单列表
songListId用户查询每个具体的歌单信息
- POST
新增歌单
- PUT
service 301 : 更改歌单信息
service 302 : 添加歌曲到歌单
service 303 ：从歌单中删除歌曲
- DELETE
删除歌单


#### 4.4 COMMENT 设计





### 5、tomcat创建文件路径问题
把项目部署到Tomcat上，需要在项目文件夹内创建新的文件夹，这里只能使用绝对路径创建，
使用相对路径无法创建，这是为什么？

Tomcat好像是个多机主实例的web容器，路径必须要唯一

为了解决这个问题，目前把用户上传的图片，歌曲等内容放到 "项目upload文件夹"目录下

### 6、编码问题 中文乱码问题
request.setCharacterEncoding("utf-8");
记得加这句，还有在tomcat的server.xml中添加URIEncoding = urf-8