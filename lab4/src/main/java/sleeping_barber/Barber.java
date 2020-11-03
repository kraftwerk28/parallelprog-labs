package sleeping_barber;

public class Barber implements Runnable {
    private final Barbershop barbershop;

    Barber(final Barbershop barbershop) {
        this.barbershop = barbershop;
    }

    @Override
    public void run() {
        while (true) {
            var customer = barbershop.callCustomer();
            System.out.println("Shaving " + customer.name +
                    " for " + customer.serviceTime + "ms.");
            shaveCustomer(customer);
        }
    }

    private void shaveCustomer(final Customer customer) {
        try {
            Thread.sleep(customer.serviceTime);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
