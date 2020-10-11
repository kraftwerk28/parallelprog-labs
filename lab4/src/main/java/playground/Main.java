package playground;

import java.util.concurrent.locks.ReentrantLock;

public class Main {
    static class Rec implements Runnable {
        final ReentrantLock lck;
        int i;

        Rec(ReentrantLock lck, int i) {
            this.i = i;
            this.lck = lck;
        }

        @Override
        public void run() {
            try {
                lck.lock();
                System.out.println(Thread.currentThread().getName() + " " + lck.getHoldCount());
                i -= 1;
                if (i > 0) {
                    run();
                }
            } finally {
                lck.unlock();
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        ReentrantLock lck = new ReentrantLock();
        var t = new Thread(() -> {
            lck.lock();
            slp(1000);
            System.out.println("Reader");
        });
        lck.lock();
        t.start();
        t.join();
    }

    private static void slp(final int ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
