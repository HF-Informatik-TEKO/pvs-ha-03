public class Main {
    public static void main(String[] args) {
        
        var s = new MySemaphore(0);

        var t1 = new MyThread("Thread 1", 1, s);
        var t2 = new MyThread("Thread 1", 5, s);
        var t3 = new MyThread("Thread 1", 4, s);

        t1.run();
        t2.run();
        t3.run();

        s.vrijgave(5);
    }
}