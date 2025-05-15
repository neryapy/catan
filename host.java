import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Enumeration;
public class host {
    private Board b;
    private Ui ui;
    private volatile boolean running = true;
    private String[] hostAddresses;
    private int port;
    public host(String[] hostAddr, int pt) {
        hostAddresses = new String[hostAddr.length + 1]; // create a new array with extra space
        hostAddresses[0] = "25.40.223.20";//getIPv4Address(); // set the first element to the local IP address
        System.arraycopy(hostAddr, 0, hostAddresses, 1, hostAddr.length); // copy the rest
        port = pt;
        b = new Board(hostAddresses.length);
        b.setHost(hostAddr, pt);
        b.setIps(hostAddresses);
        ui = new Ui(b);
        ui.setHasHost(true);
        ui.setOwnIp(hostAddresses[0]);
        System.out.println("Host IP: " + hostAddresses[0]);
new Thread(() -> {
    try (ServerSocket serverSocket = new ServerSocket(port + 2)) {
        System.out.println("TCP listener started on port " + (port + 2));
        while (running) {
            try (Socket clientSocket = serverSocket.accept();
                ObjectInputStream ois = new ObjectInputStream(clientSocket.getInputStream())) {
                Object obj = ois.readObject();
                System.out.println("Object received!");

                if (obj instanceof ArrayList) {
                    ArrayList<?> list = (ArrayList<?>) obj;
                    if (!list.isEmpty() && list.get(0) instanceof ArrayList) {
                        ArrayList<Resource> getResources = (ArrayList<Resource>) ((ArrayList<?>) list).get(0);
                        ArrayList<Resource> giveResources = (ArrayList<Resource>) ((ArrayList<?>) list).get(1);
                        ui.getProposition(getResources, giveResources);
                    } else {
                        System.err.println("Received data is malformed.");
                    }
                }
            } catch (Exception e) {
                if (running) {
                    System.err.println("Error in TCP listener:");
                    e.printStackTrace();
                }
            }
        }
    } catch (Exception e) {
        e.printStackTrace();
    }
}).start();
new Thread(() -> {
    try (ServerSocket serverSocket = new ServerSocket(port + 3)) {
        System.out.println("Server listening on port " + (port + 3));

        while (running) {
            try {
                Socket clientSocket = serverSocket.accept();
                new Thread(() -> {
                    try (ObjectInputStream ois = new ObjectInputStream(clientSocket.getInputStream())) {
                        ui.getReponseExchange(ois.readBoolean(), clientSocket.getInetAddress().getHostAddress());
                    } catch (IOException e) {
                        e.printStackTrace();
                    } finally {
                        try {
                            clientSocket.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }).start(); // Thread per client
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    } catch (IOException e) {
        e.printStackTrace();
    }
}).start(); // Thread for the whole listener

new Thread(() -> {
    try (ServerSocket serverSocket = new ServerSocket(port)) {
        System.out.println("TCP listener started on port " + (port));

        while (running) {
            try (Socket clientSocket = serverSocket.accept();
                 ObjectInputStream ois = new ObjectInputStream(clientSocket.getInputStream())) {
                Object obj = ois.readObject();

                if (obj instanceof Board) {
                    Board d=(Board)obj;
                    ui.updateAll(d);
                }
            } catch (Exception e) {
                if (running) {
                    System.err.println("Error in TCP listener:");
                    e.printStackTrace();
                }
            }
        }
    } catch (Exception e) {
        e.printStackTrace();
    }
}).start();
}
        public String getIPv4Address() {
        try {
            Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
            while (interfaces.hasMoreElements()) {
                NetworkInterface networkInterface = interfaces.nextElement();
                Enumeration<InetAddress> addresses = networkInterface.getInetAddresses();

                while (addresses.hasMoreElements()) {
                    InetAddress inetAddress = addresses.nextElement();

                    if (!inetAddress.isLoopbackAddress() && inetAddress.getHostAddress().indexOf(':') == -1) {
                        return inetAddress.getHostAddress(); // Return first found IPv4 address
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "No IPv4 Address Found";
    }

    public void stop() {
        running = false;
    }
}
