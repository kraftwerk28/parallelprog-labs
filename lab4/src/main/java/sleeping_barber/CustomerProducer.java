package sleeping_barber;

public class CustomerProducer implements Runnable {
    private final Barbershop barbershop;

    public CustomerProducer(final Barbershop barbershop) {
        this.barbershop = barbershop;
    }

    @Override
    public void run() {
        while (true) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            final var customer = Customer.randomCustomer(barbershop);
            new Thread(customer).run();
        }
    }
}
