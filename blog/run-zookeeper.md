# zookeeper 部署与运行
zookeeper 可以以单机和集群两种模式运行，也可以以伪集群的模式运行，这里说一下伪集群的搭建与运行。

伪集群的原理就是起多个进程，进程之间互相通信，每个进程使用的配置文件有一点不同。

## 配置文件
在 zookeeper home 的 conf 目录下分别创建 3 个不同的配置文件，分别为 zk1.cfg zk2.cfg zk3.cfg，其中 zk1.cfg 如下所示：

``` shell
# 心跳时间
tickTime=2000
# follower 和 leader 之间初始化连接时允许的心跳时间间隔数
initLimit=10
# follower 和 leader 之间请求和应答允许的心跳时间间隔数
syncLimit=5
# 持久化数据存储路径，如 zk 的编号 myid 就存储在这里，其他两个配置文件分别为 zk2 zk3
dataDir=/tmp/zookeeper/zk1
# 监听客户端连接的端口，其他两个配置文件使用不同的端口
clientPort=2181

server.1=127.0.0.1:2888:3888
server.2=127.0.0.1:2889:3889
server.3=127.0.0.1:2890:3890

```

## 设置 myid

在 `/tmp/zookeeper/zk1`、`/tmp/zookeeper/zk2`、`/tmp/zookeeper/zk3` 目录下分别创建 3 个名为 myid 的文件，文件的内容分别为对应的 id。

## 启动 zookeeper
使用 zookeeper home 下的 bin 中的 zkServer.sh/zkServer.cmd 脚本启动 zookeeper，如在 Linux 环境下启动命令为

``` shell
 % pwd
/home/rojeralone/zookeeper-3.4.10/bin # 当前在 bin 目录下

./zkServer.sh start ../conf/zk1.cfg
./zkServer.sh start ../conf/zk2.cfg
./zkServer.sh start ../conf/zk3.cfg
```

通过 status 参数可以查看当前 zookeeper 的状态：

``` shell
rojeralone@linux ~/zookeeper-3.4.10/bin
 % ./zkServer.sh status ../conf/zk1.cfg
ZooKeeper JMX enabled by default
Using config: ../conf/zk1.cfg
Mode: follower
rojeralone@linux ~/zookeeper-3.4.10/bin
 % ./zkServer.sh status ../conf/zk2.cfg
ZooKeeper JMX enabled by default
Using config: ../conf/zk2.cfg
Mode: leader
rojeralone@linux ~/zookeeper-3.4.10/bin
 % ./zkServer.sh status ../conf/zk3.cfg
ZooKeeper JMX enabled by default
Using config: ../conf/zk3.cfg
Mode: follower
```
可以看到三个结点一主两从。