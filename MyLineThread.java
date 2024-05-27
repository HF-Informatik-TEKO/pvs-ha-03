import java.io.*;

public class MyLineThread extends Thread {
    
    private MyLineSemaphore sem;
    private File read;
    private File write;

    public MyLineThread(MyLineSemaphore sem, File read, File write) {
        this.sem = sem;
        this.read = read;
        this.write = write;
    }

    @Override
    public void run() {
        try (BufferedReader br = new BufferedReader(new FileReader(read))) {
            String line;
            while ((line = br.readLine()) != null) {
                // System.out.println(line);
                var words = line.split(" ");
                appendToFile(words);
            }
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    private void appendToFile(String[] line) {
        sem.passeren(sem.getPlaces());
        try (var bw = new BufferedWriter(new FileWriter(write, true))) {
            for (var s : line) {
                if (s.isBlank()) {
                    continue;
                }
                System.out.println("wrote: " + s);
                bw.write(s);
                bw.newLine();
            }
        } catch (Exception e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
        finally {
            sem.vrijgave(sem.getPlaces());
        }
    }
}
