package br.com.fgr.walletlistview.utils;

import java.util.concurrent.atomic.AtomicInteger;

public class GeneratorId {

    private static final AtomicInteger sNextGeneratedId = new AtomicInteger(1);

    public static int generate() {

        for (; ; ) {

            final int result = sNextGeneratedId.get();

            int newValue = result + 1;
            if (newValue > 0x00FFFFFF) newValue = 1;

            if (sNextGeneratedId.compareAndSet(result, newValue))
                return result;

        }

    }

}