package sleeping_barber;

import java.util.LinkedList;

public class WaitingRoom {
    private final LinkedList<Customer> customers = new LinkedList<>();
    private final int maxSeats;

    public WaitingRoom(final int maxSeats) {
        this.maxSeats = maxSeats;
    }
}
