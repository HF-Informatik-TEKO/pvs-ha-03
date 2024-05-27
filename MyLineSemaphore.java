public class MyLineSemaphore {
    private int places = 0;

    public MyLineSemaphore() {}

    public MyLineSemaphore(int places) {
        this.places = places;
    }
    
    /**
     * passing (enter)
     */
    public synchronized void passeren(int numberOfPlaces) {
        numberOfPlaces = numberOfPlaces < 1 ? 1 : numberOfPlaces;
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
        numberOfPlaces = numberOfPlaces < 1 ? 1 : numberOfPlaces;
        places += numberOfPlaces;
        notifyAll();
    }

    public void setPlaces(int places) {
        this.places = places;
    }

    public int getPlaces(){
        return places;
    }
}
