package com.hawk.sdufeforumpro.base.utils;

import com.google.common.util.concurrent.ThreadFactoryBuilder;

import java.util.concurrent.*;

public class ThreadPoolUtils {

    private static ThreadFactory smsSendFactory = new ThreadFactoryBuilder()
            .setNameFormat("sms-send-pool-%d").build();

    private static ExecutorService smsSendPool = new ThreadPoolExecutor(5, 20,
            0L, TimeUnit.MILLISECONDS,
            new LinkedBlockingQueue<>(1024), smsSendFactory, new ThreadPoolExecutor.AbortPolicy());

    public static ExecutorService getSmsSendPool() {
        return smsSendPool;
    }
}
