import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Enumeration;
public class Client {
    private int port;
    private Ui ui = new Ui(new Board(3));
    private volatile boolean RunningTCP = true;
    private String ownIP;

    public Client(int pt, String ipToListenStr, String ownIp) {
        this.ownIP=ownIp;
        port = pt;
        if(ownIp==null){
            this.ownIP=getIPv4Address();
        }
        System.out.println("IPv4 Address: " + ownIp);
        ui.setOwnIp(ownIp);

        new Thread(() -> {
            try (ServerSocket serverSocket = new ServerSocket(port)) {
                System.out.println("TCP listener started on port " + (port));
        
                while (RunningTCP) {
                    try (Socket clientSocket = serverSocket.accept();
                         ObjectInputStream ois = new ObjectInputStream(clientSocket.getInputStream())) {
                        Object obj = ois.readObject();
        
                        if (obj instanceof Board) {
                            Board d=(Board)obj;
                            ui.updateAll(d);
                        }
                    } catch (Exception e) {
                        if (RunningTCP) {
                            System.err.println("Error in TCP listener:");
                            e.printStackTrace();
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
// TCP listener for ArrayList<Resource>
new Thread(() -> {
    try (ServerSocket serverSocket = new ServerSocket(port + 1)) {
        System.out.println("TCP listener started on port " + (port + 1));
        while (RunningTCP) {
            try (Socket clientSocket = serverSocket.accept();
                 ObjectInputStream ois = new ObjectInputStream(clientSocket.getInputStream())) {

                System.out.println("Waiting to receive data...");
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
                if (RunningTCP) {
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
        while (RunningTCP) {
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
}
