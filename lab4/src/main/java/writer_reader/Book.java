package writer_reader;

import java.util.LinkedList;
import java.util.Random;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class Book {
    static class Page {
        final int nchars;

        Page(final int nchars) {
            this.nchars = nchars;
        }
    }

    private final LinkedList<Page> pages = new LinkedList<>();
    private final ReentrantLock lck = new ReentrantLock(true);
    private final Condition readingCondvar = lck.newCondition();
    private final Condition publishCondvar = lck.newCondition();
    private int nreaders = 0;
    private static Random rng = new Random();

    public final void publishPage(final Page page) {
        lck.lock();
        try {
            while (nreaders > 0) {
                System.out.println("Waiting for readers to get out.");
                readingCondvar.await();
            }
            Thread.sleep(1000);
            pages.add(page);
            System.out.println("Writer wrote page with " +
                    page.nchars + " characters.");
            publishCondvar.signalAll();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lck.unlock();
        }
    }

    public void readPage(final int pageIndex, final Reader reader) {
        try {
            lck.lock();
            while (pages.size() <= pageIndex + 1) {
                System.out.println("Reader " + reader.getName() +
                        " is waiting for new pages...");
                publishCondvar.await();
            }
            nreaders++;
            if (nreaders == 1) {
                readingCondvar.signal();
            }
            lck.unlock();

            final var page = pages.get(pageIndex);
            System.out.println("Reader " + reader.getName() +
                    " read page with " + page.nchars + " characters.");
            Thread.sleep(page.nchars + rng.nextInt(1000));

            lck.lock();
            nreaders--;
            if (nreaders == 0) {
                readingCondvar.signal();
            }
            lck.unlock();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
