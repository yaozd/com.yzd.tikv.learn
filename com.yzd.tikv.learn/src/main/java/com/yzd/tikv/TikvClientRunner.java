package com.yzd.tikv;

import lombok.extern.slf4j.Slf4j;
import org.openjdk.jmh.annotations.*;
import org.tikv.common.TiConfiguration;
import org.tikv.common.TiSession;
import org.tikv.raw.RawKVClient;
import shade.com.google.protobuf.ByteString;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Scope.Thread	默认状态。实例将分配给运行给定测试的每个线程。
 * Scope.Benchmark	运行相同测试的所有线程将共享实例。可以用来测试状态对象的多线程性能(或者仅标记该范围的基准)。
 *
 * @Author: yaozh
 * @Description:
 */
@Slf4j
@State(Scope.Benchmark)
public class TikvClientRunner {
    private static final String YOUR_PD_ADDRESSES = "127.0.0.1:2379";
    private static final String key = "key:";
    private static final AtomicInteger num = new AtomicInteger();
    private Integer seed = 0;
    private RawKVClient rawClient;

    /**
     * 初始化参数
     */
    @Setup
    public void init() {
        TiConfiguration conf = TiConfiguration.createRawDefault(YOUR_PD_ADDRESSES);
        conf.setKvClientConcurrency(500);
        TiSession session = TiSession.create(conf);
        rawClient = session.createRawClient();
        log.info("BENCHMARK_INIT!");
    }

    /**
     * TearDown marks the fixture method to be run after the benchmark.
     */
    @TearDown
    public void end() {
        rawClient.close();
        log.info("BENCHMARK_COMPLETED!");
    }

    @Benchmark
    public void run() {
        //String value = String.valueOf(num.getAndIncrement());
        //String value = String.valueOf(seed++);
        //ByteString.copyFromUtf8(key + value);
        //ByteString.copyFromUtf8(value);
        //log.info(value);
        //========================================
        String value = String.valueOf(num.getAndIncrement());
        //String value = String.valueOf(seed++);
        rawClient.put(ByteString.copyFromUtf8(key + value), ByteString.copyFromUtf8(value));
    }
}
