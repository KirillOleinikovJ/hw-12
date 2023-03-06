import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

public class SecondTask {
    public static int lastNumber = 36;
    public static ConcurrentLinkedQueue queue = new ConcurrentLinkedQueue<>();
    public static volatile AtomicInteger number = new AtomicInteger(1);

    public static void main(String[] args) throws InterruptedException {
        ExecutorService executorService = Executors.newFixedThreadPool(3);
        ExecutorService service = Executors.newFixedThreadPool(5);
        SecondTask fizzBuzz = new SecondTask();
        service.submit(fizzBuzz::fizz);
        service.submit(fizzBuzz::buzz);
        service.submit(fizzBuzz::fizzbuzz);
        service.submit(fizzBuzz::number);
        service.submit(() -> out(fizzBuzz));
        Thread.sleep(1500);
        service.shutdown();
    }

    private static void out(SecondTask fizzBuzz) {
        while  (lastNumber > number.get()) {
            if (fizzBuzz.queue.isEmpty()) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
            while (!fizzBuzz.queue.isEmpty()) {
                System.out.println(fizzBuzz.queue.poll());
            }
        }

    }

    public synchronized void fizz() {
        while (lastNumber > number.get()) {
            if (number.get() % 3 == 0 && number.get() % 5 != 0) {
                queue.add("fizz");
                number.incrementAndGet();
                notifyAll();
            } else {
                try {
                    wait();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    public synchronized void buzz() {
        while (lastNumber > number.get()) {
            if (number.get() % 3 != 0 && number.get() % 5 == 0) {
                queue.add("fizz");
                number.incrementAndGet();
                notifyAll();

            } else {
                try {
                    wait();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    public synchronized void fizzbuzz() {
        while (lastNumber > number.get()) {
            if (number.get() % 3 == 0 && number.get() % 5 == 0) {
                queue.add("fizzbuzz");
                number.incrementAndGet();
                notifyAll();

            } else {
                try {
                    wait();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }

        }
    }

    public synchronized void number() {
        while (lastNumber > number.get()) {
            if (number.get() % 3 != 0 && number.get() % 5 != 0) {
                queue.add(String.valueOf(number));
                number.incrementAndGet();
                notifyAll();
            } else {
                try {
                    wait();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
}

