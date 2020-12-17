### 时间表

2020-12-9，初步搭建客户端和服务端，引入相关依赖，建立连接

2020-12-10 至 2020-12-11，设计通信协议，编写编码器和解码器

2020-12-14，进行客户端界面开发，可以往客户端简单的输入命令

2020-12-15 至 2020-12-22，设计用户类，实现创建用户、登录、查看用户信息、用户移动和地图切换的功能

2020-12-23 至 2020-12-25，实现静态资源加载，能够从excel表中读取对象信息并自动创建

2020-12-26 至 2020-12-31，尝试解决多人并发问题，完善代码



第一周：实现服务器端和客户端的简单交互，设计通信协议，实现客户端交互界面

第二周：设计用户类，实现用户创建、登录、查看和移动的功能

第三周：实现静态资源加载，完善代码，解决并发问题



### 每日总结

2020-12-9 ，初步搭建起客户端和服务端，并能实现简单的交互，并设计了消息体。

遇到的坑：

1.一个类要序列化，里面的对象和枚举也必须序列化



2020-12-10 ，建立心跳检测机制，服务器长时间接收不到客户端的消息就会主动断开连接，初步设计了分发策略，服务器能根据不同的消息类型来进行任务分发。

遇到的坑：

1.关闭channel时要加上同步，否则可能有消息丢失

2.协议体最好用一个module独立管理，否则修改协议设计时容易出错

3.创建新module时要注意git仓库的同步

4.用slf4j框架进行日志记录



2020-12-11 ，为服务器分发机制做了改善，开发了部分客户端交互页面

遇到的坑：

1.手动创建过的对象无法再自动注入



2020-12-14，完成客户端分发机制，解决获取响应信息的问题，完成客户端简单交互页面，服务端实现与数据库连接

遇到的坑：

1.客户端接收响应时，只能通过分发来将响应数据传输给服务类

2.断开连接时要注意将数据保存到数据库



2020-12-15，设计了用户类、地图类，初步实现创建用户、登录、查看用户信息、用户移动和地图切换的功能，同时能与数据库同步



2020-12-16，地图类中维护了在地图的用户，实现查看同一地图内的用户人数功能

遇到的坑：

1.spring管理的类的注入顺序：各成员变量的初始化执行顺序为：“static 成员变量 ”--> “非static成员变量” --> “被@Autowired修饰的构造函数” --> “被@Autowired修饰的成员变量” --> “被@PostConstruct修饰的init()函数”



2020-12-17，实现服务端对所有客户端进行消息通知，初步完成地图人数实时性显示；改善了密码输入错误、重复登录的问题。