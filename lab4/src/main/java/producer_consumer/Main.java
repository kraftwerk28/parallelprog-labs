package producer_consumer;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        int maxBufSize;
        try {
            maxBufSize = Integer.parseUnsignedInt(args[0]);
        } catch (Exception e) {
            maxBufSize = 10;
        }

        final var bucket = new TaskQueue(maxBufSize);

        final var producer = new Producer(bucket);
        final var consumer = new Consumer(bucket);

        final var producerThread = new Thread(producer);
        final var consumerThread = new Thread(consumer);
        consumerThread.start();
        producerThread.start();

        while (true) {
            Thread.sleep(5000);
            bucket.report();
        }
    }
}
