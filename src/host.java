import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class host {
    private Board b;
    private Ui ui;
    private volatile boolean running = true; // Flag to control the loop

    public host(String[] hostAddr, int pt) {
        String[] hostAddresses = hostAddr;
        int port = pt;
        b = new Board(3);
        ui = new Ui(b);
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
