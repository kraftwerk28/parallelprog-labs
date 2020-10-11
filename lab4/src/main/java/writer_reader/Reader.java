package writer_reader;

public class Reader implements Runnable {
    private final Book book;
    private final String name;
    private int readPages = 0;

    public Reader(final Book book, final String name) {
        this.book = book;
        this.name = name;
    }

    @Override
    public void run() {
        while (true) {
            book.readPage(readPages, this);
            readPages++;
//            System.out.println("Reader " + name +
//                    " already read " + readPages + " pages.");
        }
    }

    final String getName() {
        return name;
    }

    static Reader[] makeReaderPool(final int size, final Book book) {
        final Reader[] pool = new Reader[size];
        for (int i = 0; i < size; i++) {
            final String name = "Reader" + i;
            pool[i] = new Reader(book, name);
        }
        return pool;
    }
}
