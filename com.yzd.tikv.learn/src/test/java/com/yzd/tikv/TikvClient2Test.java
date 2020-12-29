package com.yzd.tikv;

import lombok.extern.slf4j.Slf4j;
import org.databene.contiperf.PerfTest;
import org.databene.contiperf.junit.ContiPerfRule;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.tikv.common.TiConfiguration;
import org.tikv.common.TiSession;
import org.tikv.kvproto.Kvrpcpb;
import org.tikv.raw.RawKVClient;
import shade.com.google.protobuf.ByteString;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static java.nio.charset.StandardCharsets.UTF_8;

/**
 * @Author: yaozh
 * @Description:
 */
@Slf4j
public class TikvClient2Test {
    private static final String YOUR_PD_ADDRESSES = "127.0.0.1:2379";
    private static final String key = "key:";
    public static final AtomicInteger NUM = new AtomicInteger(0);
    public static RawKVClient kvClient;
    @Rule
    public ContiPerfRule i = new ContiPerfRule();

    @BeforeClass
    public static void init() {
        TiConfiguration conf = TiConfiguration.createRawDefault(YOUR_PD_ADDRESSES);
        TiSession session = TiSession.create(conf);
        kvClient = session.createRawClient();
    }

    @Test
    @PerfTest(threads = 30, warmUp = 5_000, duration = 30_000)
    public void putTest() {
        String value = String.valueOf(NUM.getAndIncrement());
        //log.info(value);
        kvClient.put(ByteString.copyFromUtf8(key + value), ByteString.copyFromUtf8(value));
    }

    @Test
    @PerfTest(threads = 30, warmUp = 5_000, duration = 300_000)
    public void deleteTest() {
        List<Kvrpcpb.KvPair> scan = kvClient.scan(ByteString.copyFromUtf8("key:"), 100);
        if (scan.isEmpty()) {
            return;
        }
        for (Kvrpcpb.KvPair kvPair : scan) {
            if (kvPair == null) {
                continue;
            }
            log.info("Key [{}]", kvPair.getKey().toString(UTF_8));
            kvClient.delete(kvPair.getKey());
        }
    }

    @Test
    public void scanTest() {
        /**
         * 开始：key:100078
         * 结束：key:100079
         */
        List<Kvrpcpb.KvPair> scan = kvClient.scan(ByteString.copyFromUtf8("key:100078"), ByteString.copyFromUtf8("key:100079"), 100);
        if (scan.isEmpty()) {
            return;
        }
        for (Kvrpcpb.KvPair kvPair : scan) {
            if (kvPair == null) {
                continue;
            }
            log.info("Key [{}]", kvPair.getKey().toString(UTF_8));
        }
    }
}
