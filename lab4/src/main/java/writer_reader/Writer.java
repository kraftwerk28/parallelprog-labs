package writer_reader;

import java.nio.charset.StandardCharsets;
import java.util.Random;

public class Writer implements Runnable {
    private final Book book;
    private static final Random rng = new Random();

    public Writer(final Book book) {
        this.book = book;
    }

    @Override
    public void run() {
        while (true) {
            var page = new Book.Page(rng.nextInt(1000) + 1000);
            book.publishPage(page);
        }
    }
}
