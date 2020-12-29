package com.yzd.tikv;

import lombok.extern.slf4j.Slf4j;
import org.databene.contiperf.PerfTest;
import org.databene.contiperf.junit.ContiPerfRule;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.tikv.common.TiConfiguration;
import org.tikv.common.TiSession;
import org.tikv.raw.RawKVClient;
import shade.com.google.protobuf.ByteString;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @Author: yaozh
 * @Description:
 */
@Slf4j
public class MultiTikvClient2Test {
    private static final String YOUR_PD_ADDRESSES = "127.0.0.1:2379";
    private static final String key = "key:";
    public static final AtomicInteger NUM = new AtomicInteger(0);
    public static RawKVClient[] rawKVClients = new RawKVClient[10];
    @Rule
    public ContiPerfRule i = new ContiPerfRule();

    @BeforeClass
    public static void init() {
        TiConfiguration conf = TiConfiguration.createRawDefault(YOUR_PD_ADDRESSES);
        TiSession session = TiSession.create(conf);
        for (int i = 0; i < 10; i++) {
            rawKVClients[i] = session.createRawClient();
        }
    }

    @Test
    @PerfTest(threads = 30, warmUp = 5_000, duration = 60_000)
    public void putTest() {
        int i = NUM.getAndIncrement();
        String value = String.valueOf(i);
        //log.info(value);
        rawKVClients[i % 10].put(ByteString.copyFromUtf8(key + value), ByteString.copyFromUtf8(value));
    }

}
