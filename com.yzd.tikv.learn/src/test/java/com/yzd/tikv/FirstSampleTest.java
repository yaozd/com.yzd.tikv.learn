package com.yzd.tikv;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.tikv.common.TiConfiguration;
import org.tikv.common.TiSession;
import org.tikv.kvproto.Kvrpcpb;
import org.tikv.raw.RawKVClient;
import shade.com.google.protobuf.ByteString;

import java.util.List;

import static java.nio.charset.StandardCharsets.UTF_8;

/**
 * @Author: yaozh
 * @Description:
 */
@Slf4j
public class FirstSampleTest {
    private static final String YOUR_PD_ADDRESSES = "127.0.0.1:2379";
    public static final String key = "key:";

    @Test
    public void demoTest() {
        // You MUST create a raw configuration if you are using RawKVClient.
        TiConfiguration conf = TiConfiguration.createRawDefault(YOUR_PD_ADDRESSES);
        TiSession session = TiSession.create(conf);
        RawKVClient rawClient = session.createRawClient();
        rawClient.put(ByteString.copyFromUtf8(key), ByteString.copyFromUtf8(key));
        ByteString value = rawClient.get(ByteString.copyFromUtf8(key));
        if (value.isEmpty()) {
            log.error("Value is empty!");
            return;
        }
        log.info("Value [{}]", value.toString(UTF_8));
        List<Kvrpcpb.KvPair> scan = rawClient.scan(ByteString.copyFromUtf8(key), 10);
        if (scan.isEmpty()) {
            return;
        }
        for (Kvrpcpb.KvPair kvPair : scan) {
            if (kvPair == null) {
                continue;
            }
            log.info("Key [{}]", kvPair.getKey().toString(UTF_8));
            rawClient.delete(kvPair.getKey());
        }
        log.info("Start put and delete");
        for (int i = 0; i < 10_000; i++) {
            rawClient.put(ByteString.copyFromUtf8(key + i), ByteString.copyFromUtf8(key));
            //rawClient.delete(ByteString.copyFromUtf8(key));
            log.info("Num:[{}]", i);
        }
        log.info("End put and delete");
    }
}
