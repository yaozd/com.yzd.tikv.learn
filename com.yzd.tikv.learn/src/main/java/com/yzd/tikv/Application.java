package com.yzd.tikv;

import lombok.extern.slf4j.Slf4j;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;
import org.openjdk.jmh.runner.options.TimeValue;

/**
 * Benchmark
 * @Author: yaozh
 * @Description:
 */
@Slf4j
public class Application {

    /**
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
     *
     * @param args
     * @throws RunnerException
     */
    public static void main(String[] args) throws RunnerException {
        Options opt = new OptionsBuilder()
                //正则：方法：com.yzd.jutils.benchmark.t1.Helloworld.m
                .include(TikvClientRunner.class.getName())
                //.include("com.yzd.jutils.benchmark.t1.Helloworld.m")
                //.exclude("Pref")
                .mode(Mode.Throughput)
                //预热次数
                .warmupIterations(2)
                .warmupTime(TimeValue.seconds(5))
                //实际每次运行时间
                //.measurementTime(TimeValue.seconds(10))
                .measurementTime(TimeValue.seconds(60))
                //.timeout(TimeValue.NONE)
                .threads(100)
                //实际每次迭代次数
                .measurementIterations(5)
                //forks:进程
                .forks(1)
                .build();

        new org.openjdk.jmh.runner.Runner(opt).run();
    }
}
