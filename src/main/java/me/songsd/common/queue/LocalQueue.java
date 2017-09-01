package me.songsd.common.queue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by SongSD on 2017/9/1.
 */
public class LocalQueue<T> {

    private Logger logger = LoggerFactory.getLogger(LocalQueue.class);

    private LinkedBlockingQueue<T> queue = new LinkedBlockingQueue<>();

    private int warningLine = 100;
    private int freeTimeSleepDuration = 100;
    private int batchSize = 100;

    public LocalQueue(BatchProcessor<T> batchProcessor) {
        new Thread(() -> {
            while (queue != null) {
                List<T> processEffects = new ArrayList<>();
                int insertSize = queue.drainTo(processEffects, batchSize);
                if (insertSize > 0) {
                    batchProcessor.process(processEffects);
                }
                String loggerInfo = "TQueue size = " + queue.size() +
                        ", this time insert size = " + insertSize;
                if (queue.size() > warningLine) {
                    logger.warn(loggerInfo);
                } else {
                    try {
                        Thread.sleep(freeTimeSleepDuration);
                    } catch (InterruptedException e) {
                        logger.error("InterruptedException occured in consumer of TQueue, e - ", e);
                        Thread.currentThread().interrupt();
                    }
                    logger.info(loggerInfo);
                }
            }
        }).start();
    }

    public LocalQueue(SingleProcessor<T> singleProcessor) {
        new Thread(() -> {
            while (queue != null) {
                T t = queue.poll();
                singleProcessor.process(t);
                String loggerInfo = "TQueue size = " + queue.size();
                if (queue.size() > warningLine) {
                    logger.warn(loggerInfo);
                } else {
                    try {
                        Thread.sleep(freeTimeSleepDuration);
                    } catch (InterruptedException e) {
                        logger.error("InterruptedException occured in consumer of TQueue, e - ", e);
                        Thread.currentThread().interrupt();
                    }
                    logger.info(loggerInfo);
                }
            }
        }).start();
    }

    public interface BatchProcessor<T> {
        void process(List<T> tList);
    }

    public interface SingleProcessor<T> {
        void process(T t);
    }

    public boolean offer(T t) {
        return queue.offer(t);
    }

    public void setWarningLine(int warningLine) {
        this.warningLine = warningLine;
    }

    public void setFreeTimeSleepDuration(int freeTimeSleepDuration) {
        this.freeTimeSleepDuration = freeTimeSleepDuration;
    }

    public void setBatchSize(int batchSize) {
        this.batchSize = batchSize;
    }
}
