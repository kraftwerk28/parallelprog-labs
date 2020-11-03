package philosopher;

import java.util.stream.Stream;

public class Main {
    public static final int NPHILOSOPHERS = 5;

    public static void main(String[] args) throws InterruptedException {
        final var forks = Fork.createPool();
        final var philosophers = Philosopher.createPool(forks);

        final Thread[] philosopherThreads = Stream
                .of(philosophers)
                .map(Thread::new)
                .toArray(Thread[]::new);

        for (var th : philosopherThreads) th.start();
        for (var th : philosopherThreads) th.join();
    }
}
