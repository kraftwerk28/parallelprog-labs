package producer_consumer;

import java.util.LinkedList;
import java.util.List;

public class TaskQueue {
    private final int maxBufSize;
    private final List<Task> queue = new LinkedList<>();

    public TaskQueue(int maxBufSize) {
        this.maxBufSize = maxBufSize;
    }
    public synchronized void addTask(Task task) {
        while (queue.size() >= maxBufSize) {
            try {
                wait();
            } catch(InterruptedException e) {
                e.printStackTrace();
            }
        }
        queue.add(task);
        notify();
    }
    public synchronized Task takeTask() {
        while (queue.size() == 0) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return queue.remove(0);
    }
    public void report() {
        System.out.println("Tasks in queue: " + queue.size() + ".");
    }
}
