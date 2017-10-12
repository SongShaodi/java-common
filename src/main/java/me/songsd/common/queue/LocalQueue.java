package me.songsd.common.queue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by SongSD on 2017/9/1.
 */
public class LocalQueue<T> {

    private Logger logger = LoggerFactory.getLogger(LocalQueue.class);

    private LinkedBlockingQueue<T> queue = new LinkedBlockingQueue<>();

    private int maxBatchSize = 100;

    public LocalQueue(BatchProcessor<T> batchProcessor, String name, long period) {
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                List<T> processEffects = new ArrayList<>();
                int batchSize = queue.drainTo(processEffects, maxBatchSize);
                if (batchSize > 0) {
                    batchProcessor.process(processEffects);
                }

                String loggerInfo = name + " residual size = " + queue.size() + ", process size = " + batchSize;
                if (queue.size() > 0) {
                    logger.warn(loggerInfo);
                } else {
                    logger.info(loggerInfo);
                }
            }
        }, 0, period);

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            List<T> toProcesses = new ArrayList<>();
            int residualSize = queue.drainTo(toProcesses);
            logger.info("{} in shutdown hook, residualSize = {}", name, residualSize);
            if (residualSize > 0) {
                batchProcessor.process(toProcesses);
            }
        }));
    }

    public interface BatchProcessor<T> {
        void process(List<T> tList);
    }

    public boolean offer(T t) {
        return queue.offer(t);
    }

    public void setMaxBatchSize(int maxBatchSize) {
        this.maxBatchSize = maxBatchSize;
    }
}
