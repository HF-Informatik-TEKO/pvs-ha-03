public class MyThread extends Thread {

    private String name;
    private int number;
    private MySemaphore semaphore;

    public MyThread(
        String name,
        int number,
        MySemaphore semaphore
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
