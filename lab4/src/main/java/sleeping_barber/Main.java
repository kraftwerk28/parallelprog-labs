package sleeping_barber;

import java.util.LinkedList;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        final var barbershop = new Barbershop(10);
        final var barber = new Barber(barbershop);
        final var customerProducer = new CustomerProducer(barbershop);

        final var barberThread = new Thread(barber);
        final var customerProducerThread = new Thread(customerProducer);

        customerProducerThread.start();
        barberThread.start();
        customerProducerThread.join();
        barberThread.join();
    }
}
