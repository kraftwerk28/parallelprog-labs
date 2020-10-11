package writer_reader;

import java.util.stream.Stream;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        final var book = new Book();
        final var writer = new Writer(book);
        final Reader[] readers = Reader.makeReaderPool(5, book);

        final var writerThread = new Thread(writer);
        final var readerThreads = Stream
                .of(readers)
                .map(Thread::new)
                .toArray(Thread[]::new);

        writerThread.start();
        for (var th : readerThreads) th.start();

        writerThread.join();
        for (var th : readerThreads) th.join();
    }
}
