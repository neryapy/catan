import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.nio.ByteBuffer;
import java.util.Enumeration;

public class Client {
    private int port;
    private InetAddress ipToListen;
    private Ui ui = new Ui(new Board(3));
    private volatile boolean running = true;

    public Client(int pt, String ipToListenStr) {
        Board b = new Board(3);
        port = pt;
        try {
            System.out.println("Client is listening on " + ipToListenStr + ":" + port);
            ipToListen = InetAddress.getByName(ipToListenStr);
        } catch (Exception e) {
            System.err.println("Invalid IP address: " + ipToListenStr);
            e.printStackTrace();
            return;
        }
        try {
            Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
            while (interfaces.hasMoreElements()) {
                NetworkInterface networkInterface = interfaces.nextElement();
                Enumeration<InetAddress> addresses = networkInterface.getInetAddresses();
                while (addresses.hasMoreElements()) {
                    InetAddress inetAddress = addresses.nextElement();
                    if (!inetAddress.isLoopbackAddress() && inetAddress.isSiteLocalAddress()) {
                        ui.setOwnIp(inetAddress.getHostAddress());
                    }
                }
            }
            
        } catch (Exception e) {
            
            e.printStackTrace();
            
        }
        new Thread(() -> {
            try (DatagramSocket socket = new DatagramSocket(port, ipToListen)) {
                System.out.println("Client is listening on " + ipToListen.getHostAddress() + ":" + port);

                while (running) {
                    try {
                        // Read size header
                        byte[] sizeHeader = new byte[4];
                        DatagramPacket sizePacket = new DatagramPacket(sizeHeader, sizeHeader.length);
                        socket.receive(sizePacket);
                        int dataSize = ByteBuffer.wrap(sizePacket.getData()).getInt();

                        // Read actual data
                        byte[] dataBuffer = new byte[dataSize];
                        DatagramPacket dataPacket = new DatagramPacket(dataBuffer, dataBuffer.length);
                        socket.receive(dataPacket);

                        String received = new String(dataPacket.getData(), 0, dataPacket.getLength());
                        //System.out.println(received);
                        b.updateGameState(received);
                        ui.updateAll(b);
                    } catch (Exception e) {
                        if (running) {
                            e.printStackTrace();
                        }
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