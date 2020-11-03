package lab3;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Predicate;
import java.util.stream.IntStream;

public class Lab3 {
    public static int countElems(final int[] list, Predicate<Integer> predicate) {
        final var result = new AtomicInteger(0);
        IntStream.of(list).parallel().forEach((elem) -> {
            if (predicate.test(elem)) {
                result.incrementAndGet();
            }
        });
        return result.get();
    }

    public static class ListMinMax {
        public final int minValue;
        public final int minIndex;
        public final int maxValue;
        public final int maxIndex;

        ListMinMax(
                final AtomicInteger minValue,
                final AtomicInteger maxValue,
                final AtomicInteger minIndex,
                final AtomicInteger maxIndex
        ) {
            this.minValue = minValue.get();
            this.maxValue = maxValue.get();
            this.minIndex = minIndex.get();
            this.maxIndex = maxIndex.get();
        }

        @Override
        public String toString() {
            return '{' + " min: " + minValue + " at index " + minIndex +
                    "; max: " + maxValue + " at index " + maxIndex + '}';
        }
    }

    public static ListMinMax findMinMax(final int[] list) {
        final var maxValue = new AtomicInteger(Integer.MIN_VALUE);
        final var minValue = new AtomicInteger(Integer.MAX_VALUE);
        final var maxIndex = new AtomicInteger();
        final var minIndex = new AtomicInteger();
        IntStream.range(0, list.length).parallel().forEach((index) -> {
            final var elem = list[index];
            if (maxValue.get() < elem) {
                maxIndex.set(index);
                maxValue.set(elem);
            }
            if (minValue.get() > elem) {
                minIndex.set(index);
                minValue.set(elem);
            }
        });
        return new ListMinMax(minValue, maxValue, minIndex, maxIndex);
    }

    public static int xorList(final int[] list) {
        final var result = new AtomicInteger(list[0]);
        IntStream.of(list).parallel().forEach((elem) -> {
            result.set(result.get() ^ elem);
        });
        return result.get();
    }
}
