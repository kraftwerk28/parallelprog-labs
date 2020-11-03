package lab5;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.IntStream;

public class Main {
    static int LIST_SIZE = 100;

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        final var listft1 = CompletableFuture
                .supplyAsync(() -> randomList(LIST_SIZE))
                .thenApply((list) -> {
                    var avg = listAvg(list);
                    return Arrays.stream(list).filter((item) -> item > avg);
                })
                .thenApply(IntStream::sorted)
                .thenApply(IntStream::toArray)
                .thenApply(Main::list2ll);
        final CompletableFuture<LinkedList<Integer>> listft2 = CompletableFuture
                .supplyAsync(() -> randomList(LIST_SIZE))
                .thenApply((list) -> {
                    var avg = listAvg(list);
                    return Arrays.stream(list).filter((item) -> item < avg);
                })
                .thenApply(IntStream::sorted)
                .thenApply(IntStream::toArray)
                .thenApply(Main::list2ll);

        var finalList = listft1.thenCombine(listft2, (list1, list2) -> {
            var result = new LinkedList<Integer>();
            while (list1.size() > 0 && list2.size() > 0) {
                if (list1.get(0) > list2.get(0))
                    result.add(list2.remove(0));
                else
                    result.add(list1.remove(0));
            }
            result.addAll(list1);
            result.addAll(list2);
            return result;
        }).get();

        System.out.println(finalList);
    }

    static LinkedList<Integer> list2ll(int[] list) {
        LinkedList<Integer> result = new LinkedList<>();
        for (int i : list)
            result.add(i);
        return result;
    }

    static int[] randomList(final int listSize) {
        var rng = new Random();
        var list = new int[listSize];
        for (int i = 0; i < listSize; i++) {
            list[i] = rng.nextInt(listSize);
        }
        return list;
    }

    static float listAvg(int[] list) {
        var sum = Arrays.stream(list).sum();
        return sum / (float) list.length;
    }
}
