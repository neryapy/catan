import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

public class Client {
    private int port;
    private InetAddress ipToListen;
    private Ui ui = new Ui(new Board(3));
    private volatile boolean running = true; // Flag to control the loop

    public Client(int pt, String ipToListenStr) {
        Board b = new Board(3);
        port = pt;

        try {
            ipToListen = InetAddress.getByName(ipToListenStr);
        } catch (Exception e) {
            System.err.println("Invalid IP address: " + ipToListenStr);
            e.printStackTrace();
            return;
        }

        // Start the listening loop in a separate thread
        new Thread(() -> {
            try (DatagramSocket socket = new DatagramSocket(port, ipToListen)) {
                byte[] buffer = new byte[6394];
                System.out.println("Client is listening on " + ipToListen.getHostAddress() + ":" + port);

                while (running) {
                    try {
                        DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                        socket.receive(packet);

                        String received = new String(packet.getData(), 0, packet.getLength());
                        b.updateGameState(received);
                        ui.updateAll(b);
                        //System.out.println("------------------------------------------------------------------------------------------------------------------");
                    } catch (Exception e) {
                        if (running) { // Ignore exceptions if we're stopping
                            e.printStackTrace();
                        }
                    }
                }
            } catch (SocketException e) {
                System.err.println("Socket could not be created on " + ipToListen.getHostAddress() + ":" + port);
                e.printStackTrace();
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
