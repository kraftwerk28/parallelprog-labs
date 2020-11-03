package sleeping_barber;

import java.util.Random;

public class Customer implements Runnable {
    private static final Random rng = new Random();
    private final Barbershop barbershop;
    final int serviceTime;
    final String name;

    public Customer(final int serviceTime,
                    final String name,
                    Barbershop barbershop) {
        this.barbershop = barbershop;
        this.serviceTime = serviceTime;
        this.name = name;
    }

    public static Customer randomCustomer(final Barbershop barbershop) {
        final String name = allNames[rng.nextInt(allNames.length)];
        final int serviceTime = rng.nextInt(1000) + 1000;
        return new Customer(serviceTime, name, barbershop);
    }

    @Override
    public synchronized void run() {
        barbershop.trySeat(this);
        System.out.println("Customer " + name + " is notifying barber.");
    }

    static String[] allNames = {
            "Lillie-Mai Hays",
            "Vikki Ellison",
            "Rhona Munoz",
            "Misha Choi",
            "Aria Leblanc",
            "Dania Milner",
            "Monty Mathis",
            "Hussein Yoder",
            "Caoimhe Cowan",
            "Mehmet Bowden",
            "Dollie Hagan",
            "Yvie Power",
            "Nazifa Oneil",
            "Safia Curtis",
            "Chenai Ballard",
            "Alessia Henry",
            "Lucy Oconnell",
            "Avani Landry",
            "Lily-Ann Turner",
            "Bryson Rees",
            "Falak Mcclain",
            "Baran Lamb",
            "Dawson Rodriguez",
            "Arwen Weeks",
            "Uma Dalby",
            "Zoe Pittman",
            "Anabella Davis",
            "Tamera Ellis",
            "Ethel Burnett",
            "Clement Mathews",
            "Daisy-Mae Dotson",
            "Steve Stanton",
            "Ben Clemons",
            "Lyla-Rose Strickland",
            "Ruby Davenport",
            "Milla Howarth",
            "Ella-Mae Berger",
            "Mysha Daniel",
            "Cali Hart",
            "Hira Arnold",
            "Caolan Amos",
            "Rhianna Mcbride",
            "Marnie Gregory",
            "Chante Armstrong",
            "Zackary Payne",
            "Azaan Hogg",
            "Cynthia Dupont",
            "Tj Head",
            "Jaspal Donovan",
            "Franklin Davila",
    };
}