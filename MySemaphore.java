import java.util.ArrayList;

public class MySemaphore {
    private int places = 0;

    public MySemaphore(int places) {
        this.places = places;
    }
    
    /**
     * passing (enter)
     */
    public synchronized void passeren(int numberOfPlaces) {
        if (places == 0){
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
