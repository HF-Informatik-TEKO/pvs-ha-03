public class MyAdaptiveThread extends Thread {

    private String name;
    private int number;
    private MyAdaptiveSemaphore semaphore;

    public MyAdaptiveThread(
        String name,
        int number,
        MyAdaptiveSemaphore semaphore
        ) 
    {
        this.name = name;
        this.number = number;
        this.semaphore = semaphore;
    }
    
    @Override
    public void run() {
        System.out.println(name + ": try to run");
        semaphore.passeren(number);
        System.out.println(name + ": is running");
        try { Thread.sleep(1_000); } catch (InterruptedException e) { }
        semaphore.vrijgave(number);
        System.out.println(name + ": has finished");
    }
}
