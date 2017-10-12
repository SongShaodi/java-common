package me.songsd.common.util;

import me.songsd.common.queue.LocalQueue;
import org.junit.Test;

/**
 * Created by SongSD on 2017/9/1.
 */
public class LocalQueueTest {

    @Test
    public void testQueue() throws InterruptedException {
        LocalQueue<Integer> queue = new LocalQueue<>(integers -> {
            try {
                Thread.sleep(30);
                System.out.println(integers.size() + "--" + integers.get(integers.size() - 1));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }, "TestQueue", 10 * 1000);

        for (int i = 0; i < 32; i++) {
            for (int j = 0; j < 1000; j++) {
                queue.offer(i * 1000 + j);
            }
        }

        Thread.sleep(5000);
    }

}
