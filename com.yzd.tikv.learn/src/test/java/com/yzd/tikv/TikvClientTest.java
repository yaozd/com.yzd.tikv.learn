package com.yzd.tikv;

import com.github.houbb.junitperf.core.annotation.JunitPerfConfig;
import com.github.houbb.junitperf.core.report.impl.ConsoleReporter;
import com.github.houbb.junitperf.core.report.impl.HtmlReporter;
import lombok.extern.slf4j.Slf4j;
import org.junit.After;
import org.junit.Before;
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
public class TikvClientTest {
    private static final String YOUR_PD_ADDRESSES = "127.0.0.1:2379";
    private static final String key = "key:";
    private AtomicInteger num = new AtomicInteger(0);
    public static RawKVClient kvClient;

    @Before
    public void init() {
        TiConfiguration conf = TiConfiguration.createRawDefault(YOUR_PD_ADDRESSES);
        TiSession session = TiSession.create(conf);
        this.kvClient = session.createRawClient();
    }

    @After
    public void end() {
        log.info("Num:[{}]", num.get());
        kvClient.close();
    }

    @Test
    @JunitPerfConfig(threads = 10, warmUp = 10_000, duration = 30_000,
            reporter = {HtmlReporter.class, ConsoleReporter.class})
    public void test() {
        if (kvClient == null) {
            init();
            return;
        }
        String value = String.valueOf(num.getAndIncrement());
        //log.info(value);
        kvClient.put(ByteString.copyFromUtf8(key + value), ByteString.copyFromUtf8(value));
    }
}
