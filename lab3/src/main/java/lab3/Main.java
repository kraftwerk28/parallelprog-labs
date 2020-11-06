package lab3;

import java.util.Random;

public class Main {
    public static void main(String[] args) {
        final var inList = randList(100000000);

        System.out.println(Lab3.countElems(inList,
                (elem) -> elem > 25 && elem <= 75));
        System.out.println(Lab3.findMinMax(inList));
        System.out.println(Lab3.xorList(inList));
    }

    static int[] randList(final int length) {
        final var rng = new Random();
        final var result = new int[length];
        for (int i = 0; i < length; i++) {
            result[i] = rng.nextInt(length);
        }
        return result;
    }
}
