package lab3;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Predicate;
import java.util.stream.IntStream;

public class Lab3 {
    public static int countElems(final int[] list, Predicate<Integer> predicate) {
        final var result = new AtomicInteger(0);
        IntStream.of(list).parallel().forEach((elem) -> {
            int current;
            do {
                current = result.get();
            } while (!result.compareAndSet(current, current + 1));
        });
        return result.get();
    }

    public static class ListMinMax {
        public final int minValue;
        public final int minIndex;
        public final int maxValue;
        public final int maxIndex;

        ListMinMax(
                final int minValue,
                final int maxValue,
                final AtomicInteger minIndex,
                final AtomicInteger maxIndex
        ) {
            this.minValue = minValue;
            this.maxValue = maxValue;
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
        final var maxIndex = new AtomicInteger();
        final var minIndex = new AtomicInteger();
        IntStream.range(0, list.length).parallel().forEach((index) -> {
            final var elem = list[index];
            int prevMaxIndex;
            int prevMinIndex;
            do {
                prevMaxIndex = maxIndex.get();
            } while (list[prevMaxIndex] < elem &&
                    !maxIndex.compareAndSet(prevMaxIndex, index));
            do {
                prevMinIndex = minIndex.get();
            } while (list[prevMinIndex] > elem &&
                    !minIndex.compareAndSet(prevMinIndex, index));
        });
        return new ListMinMax(list[minIndex.get()], list[maxIndex.get()],
                minIndex, maxIndex);
    }

    public static int xorList(final int[] list) {
        final var result = new AtomicInteger(list[0]);
        IntStream.of(list).parallel().forEach((elem) -> {
            int currentHash;
            do {
                currentHash = result.get();
            } while (!result.compareAndSet(currentHash,
                    currentHash ^ elem));
        });
        return result.get();
    }
}
