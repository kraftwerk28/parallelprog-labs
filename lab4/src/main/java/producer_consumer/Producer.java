package producer_consumer;

import java.util.Random;

public class Producer implements Runnable {
    private final TaskQueue bucket;

    public Producer(TaskQueue bucket) {
        this.bucket = bucket;
    }

    @Override
    public void run() {
        final var rng = new Random();
        while (true) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            final var taskLen = rng.nextInt(1000) + 1000;
            final var task = new Task(taskLen);
            System.out.println("Created task " + task.getExecTime() + ".");
            bucket.addTask(task);
        }
    }
}
