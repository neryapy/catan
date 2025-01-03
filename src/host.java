import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class host {
    private Board b = new Board();
    private Ui ui = new Ui(b);
    private volatile boolean running = true; // Flag to control the loop

    public host(String[] hostAddr, int pt) {
        String[] hostAddresses = hostAddr;
        int port = pt;

        // Start the loop in a separate thread
        new Thread(() -> {
            try (DatagramSocket socket = new DatagramSocket()) {
                while (running) {
                    for (String host : hostAddresses) {
                        try {
                            InetAddress address = InetAddress.getByName(host);
                            byte[] buffer = b.exportGameState().getBytes();
                            DatagramPacket packet = new DatagramPacket(buffer, buffer.length, address, port);
                            socket.send(packet);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    try {
                        Thread.sleep(100); // Sleep for 100 milliseconds
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        break;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    // Stop the loop gracefully
    public void stop() {
        running = false;
    }
}
