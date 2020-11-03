package sleeping_barber;

import java.util.LinkedList;

public class Barbershop {
    public final LinkedList<Customer> waitingRoom = new LinkedList<>();
    public final int waitingRoomCapacity;

    public Barbershop(final int roomCapacity) {
        waitingRoomCapacity = roomCapacity;
    }

    public boolean isEmpty() {
        System.out.println(waitingRoom.size() + " " + waitingRoom.isEmpty());
        return waitingRoom.isEmpty();
    }

    public boolean isFull() {
        return waitingRoom.size() >= waitingRoomCapacity;
    }

    public final synchronized Customer callCustomer() {
        while (isEmpty()) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        var customer = waitingRoom.remove(0);
        notify();
        return customer;
    }

    public final synchronized void trySeat(final Customer customer) {
        if (isFull()) {
            return;
        }
        waitingRoom.add(customer);
        notify();
    }
}
