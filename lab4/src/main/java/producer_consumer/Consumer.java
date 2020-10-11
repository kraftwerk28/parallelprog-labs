package producer_consumer;

public class Consumer implements Runnable {
    private final TaskQueue bucket;

    public Consumer(TaskQueue bucket) {
        this.bucket = bucket;
    }

    @Override
    public void run() {
        while (true) {
            var task = bucket.takeTask();
            System.out.println("Running task " + task.getExecTime() + ".");
            task.run();
        }
    }
}
