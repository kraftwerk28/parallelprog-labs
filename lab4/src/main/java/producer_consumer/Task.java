package producer_consumer;

public class Task {
    private final int _time;

    public int getExecTime() {
        return _time;
    }

    Task(int time) {
        this._time = time;
    }

    void run() {
        try {
            Thread.sleep(_time);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
