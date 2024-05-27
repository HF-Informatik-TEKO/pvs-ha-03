import java.io.*;
import java.util.ArrayList;

public abstract class MyConvertThread extends Thread {
    
    private MyLineSemaphore sem;
    private int lineNumber;
    private File read;
    private File write;

    public MyConvertThread(MyLineSemaphore sem, File read, File write) {
        this.sem = sem;
        this.read = read;
        this.write = write;
    }

    @Override
    public void run() {
        while (true) {
            var lines = getUnreadLines();
            var converted = convertAll(lines);
            writeToFile(converted);
            try { Thread.sleep(500); } catch (InterruptedException e) { }
        }
    }

    public ArrayList<String> convertAll(ArrayList<String> line) {
        var list = new ArrayList<String>();

        for (var s : line) {
            var reversed = convert(s);
            list.add(reversed);
        }

        return list;
    }

    protected abstract String convert(String input);

    protected void writeToFile(ArrayList<String> converted) {
        try (var bw = new BufferedWriter(new FileWriter(write, true))) {
            for (var s : converted) {
                if (s.isBlank()) {
                    continue;
                }
                System.out.println("converted: " + s);
                bw.write(s);
                bw.newLine();
            }
        } catch (Exception e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    private ArrayList<String> getUnreadLines() {
        sem.passeren(1);
        var lines = new ArrayList<String>();
        var currentLineNumber = 0;
        try (BufferedReader br = new BufferedReader(new FileReader(read))) {
            String line;
            while ((line = br.readLine()) != null) {
                // System.out.println(line);
                currentLineNumber++;
                if (currentLineNumber <= lineNumber) {
                    continue;
                }
                lines.add(line);
            }
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
        finally {
            lineNumber = currentLineNumber;
            sem.vrijgave(1);
        }

        return lines;
    }
}
