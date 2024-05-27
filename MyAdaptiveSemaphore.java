public class MyAdaptiveSemaphore {
    private int places = 0;

    public MyAdaptiveSemaphore(int places) {
        this.places = places;
    }
    
    /**
     * passing (enter)
     */
    public synchronized void passeren(int numberOfPlaces) {
        while (places < numberOfPlaces){
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        places -= numberOfPlaces;
    }

    /**
     * release (leave)
     */
    public synchronized void vrijgave(int numberOfPlaces) {
        places += numberOfPlaces;
        notifyAll();
    }
}
