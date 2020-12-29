# Tikv
- java client
    - [https://github.com/tikv/client-java/](https://github.com/tikv/client-java/)

### install
- [https://tikv.org/docs/4.0/tasks/deploy/binary/](https://tikv.org/docs/4.0/tasks/deploy/binary/)
```

nohup ./bin/pd-server --name=pd1 --data-dir=pd1 --client-urls="http://127.0.0.1:2379" --peer-urls="http://127.0.0.1:2380" --initial-cluster="pd1=http://127.0.0.1:2380"  --log-file=pd1.log&
//
nohup ./bin/tikv-server --pd-endpoints="127.0.0.1:2379" --addr="127.0.0.1:20160" --data-dir=tikv1 --log-file=tikv1.log&

nohup ./bin/tikv-server --pd-endpoints="127.0.0.1:2379" --addr="127.0.0.1:20161" --data-dir=tikv2 --log-file=tikv2.log&


nohup ./bin/tikv-server --pd-endpoints="127.0.0.1:2379" --addr="127.0.0.1:20162" --data-dir=tikv3 --log-file=tikv3.log&
```


### 参考
- 压测工具
    - [SpringBoot并发压测工具ContiPerf](https://blog.csdn.net/huang_wei_cai/article/details/105729705)
    - [Java 性能测试框架工具-JunitPerf 快速上手](https://blog.csdn.net/qq_40741855/article/details/105075445)
    - JMH
        - [JMH java基准测试](https://my.oschina.net/u/4332827/blog/4180735)
        - [Java使用JMH进行方法性能优化测试](https://blog.csdn.net/weixin_43767015/article/details/104758415)
        
### 运行
```
1.
 java -cp  .\com.yzd.tikv.learn-1.0-SNAPSHOT.jar com.yzd.tikv.Application
2.
 
```
### 性能测试
```
* 1.
* ENV:
* CentOS Linux 7.5.1804 64bit  CPU:8-core MEM:16G
* Tikv release-version=v4.0.9
*
* 2.
* Benchmark              Mode  Cnt      Score      Error  Units
* TikvClientRunner.run  thrpt    5  15297.121 ± 1516.463  ops/s
*
* 3.
* 结论：目前暂定Tikv的性能不理想
```