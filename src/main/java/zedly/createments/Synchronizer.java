package zedly.createments;

import java.util.concurrent.*;

public class Synchronizer implements Runnable {

    public ConcurrentLinkedQueue<Runnable> queue;

    public Synchronizer() {
        queue = new ConcurrentLinkedQueue<>();
    }

    public void run() {
        if (!queue.isEmpty()) {
            Runnable com = queue.remove();
            com.run();
        }
    }
}