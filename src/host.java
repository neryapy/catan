import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.nio.ByteBuffer;
public class host {
    private Board b;
    private Ui ui;
    private volatile boolean running = true;

    public host(String[] hostAddr, int pt) {
        String[] hostAddresses = hostAddr;
        int port = pt;
        b = new Board(hostAddresses.length);
        b.setIps(hostAddresses);
        ui = new Ui(b);
        ui.setOwnIp(hostAddresses[0]);

        new Thread(() -> {
            try (DatagramSocket socket = new DatagramSocket()) {
                while (running) {
                    for (String host : hostAddresses) {
                        try {
                            InetAddress address = InetAddress.getByName(host);
                            String gameState = b.exportGameState();
                            byte[] gameStateBytes = gameState.getBytes();
                            byte[] sizeHeader = ByteBuffer.allocate(4).putInt(gameStateBytes.length).array();

                            // Send size header
                            DatagramPacket sizePacket = new DatagramPacket(sizeHeader, sizeHeader.length, address, port);
                            socket.send(sizePacket);

                            // Send actual data
                            DatagramPacket dataPacket = new DatagramPacket(gameStateBytes, gameStateBytes.length, address, port);
                            socket.send(dataPacket);

                            System.out.println("Sent data size: " + gameStateBytes.length);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    try {
                        Thread.sleep(100);
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

    public void stop() {
        running = false;
    }
}
