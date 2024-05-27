import java.io.File;
import java.util.Base64;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        
        // adaptiveScenario();

        convertScenario();
        Thread.sleep(5_000);
        System.exit(0);
    }

    /**
        1. Erstellen Sie einen Semaphore mit dem Initialwert 5.
        2. Nun soll ein Thread 1 «Platz» reservieren (durchgeführt).
        3. Ein weiterer Thread soll darauf 5 «Plätze» versuchen zu reservieren (blockiert).
        4. Ein dritter Thread soll nun 4 «Plätze» reservieren (durchgeführt).
        5. Nun soll der erste und der dritte Thread wieder ihre «Plätze» abgeben, wodurch der zweite
        Thread erst jetzt ausgeführt wird
     * 
     * @throws InterruptedException
     */
    private static void adaptiveScenario() throws InterruptedException {
        var s = new MyAdaptiveSemaphore(5);

        var t1 = new MyAdaptiveThread("Thread 1", 1, s);
        var t2 = new MyAdaptiveThread("Thread 2", 5, s);
        var t3 = new MyAdaptiveThread("Thread 3", 4, s);

        t1.start();
        Thread.sleep(50);
        t2.start();
        t3.start();
    }

    private static void convertScenario() {
        var lineSemaphore = new MyLineSemaphore();
        var splitLineThread = getLineThread(lineSemaphore);
        var convertTreads = getConvertThreads(lineSemaphore);

        var len = convertTreads.length > 0 ? convertTreads.length : 1;
        lineSemaphore.setPlaces(len);

        splitLineThread.start();
        for (var t : convertTreads) {
            t.start();
        }
    }

    private static Thread getLineThread(MyLineSemaphore lineSemaphore) {
        return new MyLineThread(
            lineSemaphore, 
            new File("text.txt"), 
            new File("result-files/lines.txt")
        );
    }

    private static Thread[] getConvertThreads(MyLineSemaphore lineSemaphore) {
        var t1 = getReverse(lineSemaphore, new File("result-files/reverse.txt"));
        var t2 = getBase64(lineSemaphore, new File("result-files/base64.txt"));
        var t3 = getMd5(lineSemaphore, new File("result-files/md5.txt"));

        return new Thread[] { t1, t2, t3 };
    }

    private static MyConvertThread getReverse(MyLineSemaphore lineSemaphore, File out) {
        return new MyConvertThread(lineSemaphore, new File("result-files/lines.txt"), out) {
            @Override
            protected String convert(String n) {
                var r = "";

                for (int i = n.length() - 1; i >= 0; i--) {
                    r += n.charAt(i);
                }

                return r;
            }
        };
    }

    private static MyConvertThread getBase64(MyLineSemaphore lineSemaphore, File out) {
        return new MyConvertThread(lineSemaphore, new File("result-files/lines.txt"), out) {
            @Override
            protected String convert(String n) {
                return Base64.getEncoder().encodeToString(n.getBytes());
            }
        };
    }
    
    private static MyConvertThread getMd5(MyLineSemaphore lineSemaphore, File out) {
        return new MyConvertThread(lineSemaphore, new File("result-files/lines.txt"), out) {
            @Override
            protected String convert(String n) {
                var md5 = "";
                try {
                    var md = MessageDigest.getInstance("MD5");
                    var inputBytes = n.getBytes();
                    var hashBytes = md.digest(inputBytes);
                    
                    md5 = convertToHex(hashBytes);
                    
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                }

                return md5;
            }

            private String convertToHex(byte[] hashBytes) {
                StringBuilder sb = new StringBuilder();
                for (byte b : hashBytes) {
                    sb.append(String.format("%02x", b));
                }
                String md5Hash = sb.toString();
                return md5Hash;
            }
        };
    }
}