package philosopher;

import java.util.Random;

public class Philosopher implements Runnable {
    private static Random rng = new Random();
    private Fork leftFork;
    private Fork rightFork;
    public String name;
    public State state;

    public enum State {
        EATING,
        THINKING,
    }

    public Philosopher(final Fork leftFork,
                       final Fork rightFork,
                       final String name) {
        this.leftFork = leftFork;
        this.rightFork = rightFork;
        this.name = name;
        this.state = State.THINKING;
    }

    @Override
    public void run() {
        while (true) {
            eat();
        }
    }

    private void eat() {
        if (leftFork.takeIn()) {
            if (rightFork.takeIn()) {
                final int eatTime = rng.nextInt(1000) + 1000;
                state = State.EATING;
                System.out.println(name + " eats.");
                try {
                    Thread.sleep(eatTime);
                } catch (InterruptedException ex) {
                }
                state = State.THINKING;
                System.out.println(name + " thinks.");
                rightFork.layBack();
                leftFork.layBack();
            } else {
                leftFork.layBack();
            }
        }
    }

    public static Philosopher[] createPool(final Fork[] forks) {
        final Philosopher[] philosophers = new Philosopher[Main.NPHILOSOPHERS];
        for (int i = 0; i < Main.NPHILOSOPHERS; i++) {
            final Fork left = forks[i];
            final Fork right = forks[(i + 1) % Main.NPHILOSOPHERS];
            philosophers[i] = new Philosopher(left, right, allNames[i]);
        }
        return philosophers;
    }

    private static String[] allNames = {"Aristotle", "Plato", "Socrates",
            "Cicero", "Avicenna"};
}