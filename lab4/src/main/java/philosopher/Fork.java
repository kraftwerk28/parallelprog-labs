package philosopher;

import java.util.concurrent.Semaphore;

public class Fork {
    public final Semaphore sem = new Semaphore(1);
    public int id;

    public Fork(int id) {
        this.id = id;
    }

    public boolean takeIn() {
        return sem.tryAcquire();
    }

    public void layBack() {
        sem.release();
    }

    public static Fork[] createPool() {
        final Fork[] forks = new Fork[Main.NPHILOSOPHERS];
        for (int i = 0; i < Main.NPHILOSOPHERS; i++) {
            forks[i] = new Fork(i);
        }
        return forks;
    }
}