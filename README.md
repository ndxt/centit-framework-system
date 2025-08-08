# 系统框架的数据库持久化实现

这是开发单应用系统的基础。传统政企业务系统都有个后台的系统维护，维护的内容就是我们框架的基本要素。工程中的模块有：

1. framework-persistence-core 模块，定义了持久化对象和持久化接口。框架的开发规范中并没有要求持久化类（Dao）的接口和实现分离，由于框架部分希望能够灵活的绑定不同的持久化模块所以抽象出一个接口。
2. framework-persistence-\* 三个相同功能模块，分别用jdbc、hibernate、mybatis实现了基本元素的持久化工作。因为单应用系统的业务部分和系统管理部分一般在一个工程中，这样做多个实现是为了给开发人员不同的选择。
3. framework-system-module 模块，实现了系统维护逻辑操作。
4. framework-system-web 模块，实现了系统维护的http接口，供前段调用。
5. framework-system-view-easyui 模块，为系统维护的前端页面。
6. framework-system-config 为系统模块相关的bean配置。
7. framework-system-demo 是一个可以运行的示例。

如果我们需要开发一个单应用系统[framework-system-demo](./framework-system-demo)是一个最好不过的开发起点。开发人员可以复制这个项目的源代码作为自己项目的基础工程，记得修改java的包名。
11

