import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;

public class Main {
    public static void main(String[] args) {
        
        
        String[] j = {"25.37.70.3"};
        new host(j, 555);
        //new Client(555, "25.40.223.20","25.37.70.3");
        //new PartyManager();
        //new Ui(new Board(3));
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