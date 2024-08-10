import java.io.*;
import java.net.*;
import java.util.*;

public class GameServer {
    private static final int PORT = 12345;
    private static Map<String, Room> rooms = new HashMap<>();

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Server started and waiting for clients...");
            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Client connected: " + clientSocket.getInetAddress());
                new ClientHandler(clientSocket).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static synchronized boolean createRoom(String roomName, int capacity) {
        if (rooms.containsKey(roomName)) {
            return false; // Room already exists
        }
        rooms.put(roomName, new Room(capacity));
        return true; // Room created successfully
    }

    static synchronized boolean joinRoom(String roomName, ClientHandler clientHandler) {
        Room room = rooms.get(roomName);
        if (room == null) {
            return false; // Room does not exist
        }
        return room.addClient(clientHandler);
    }

    static synchronized int getPlayerCount(String roomName) {
        Room room = rooms.get(roomName);
        return (room != null) ? room.getPlayerCount() : 0;
    }

    static class Room {
        private final int capacity;
        private final List<ClientHandler> clients = new ArrayList<>();

        Room(int capacity) {
            this.capacity = capacity;
        }

        synchronized boolean addClient(ClientHandler clientHandler) {
            if (clients.size() < capacity) {
                clients.add(clientHandler);
                return true;
            }
            return false; // Room is full
        }

        synchronized int getPlayerCount() {
            return clients.size();
        }

        synchronized int getCapacity() {
            return capacity;
        }
    }

    static class ClientHandler extends Thread {
        private Socket clientSocket;
        private PrintWriter out;
        private BufferedReader in;

        public ClientHandler(Socket socket) {
            this.clientSocket = socket;
        }

        @Override
        public void run() {
            try {
                in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                out = new PrintWriter(clientSocket.getOutputStream(), true);

                out.println("Enter command: (create roomName capacity / join roomName)");
                String inputLine;
                while ((inputLine = in.readLine()) != null) {
                    String[] tokens = inputLine.split(" ");
                    if (tokens.length < 2 || tokens.length > 3) {
                        out.println("Invalid command. Use: create roomName capacity or join roomName");
                        continue;
                    }
                    String command = tokens[0];
                    String roomName = tokens[1];

                    if (command.equalsIgnoreCase("create")) {
                        int capacity = Integer.parseInt(tokens[2]);
                        if (capacity < 3 || capacity > 4) {
                            out.println("Invalid capacity. Choose 3 or 4.");
                        } else {
                            if (createRoom(roomName, capacity)) {
                                out.println("Room " + roomName + " created with capacity " + capacity + ".");
                            } else {
                                out.println("Room " + roomName + " already exists.");
                            }
                        }
                    } else if (command.equalsIgnoreCase("join")) {
                        if (joinRoom(roomName, this)) {
                            out.println("Joined room " + roomName + " successfully.");
                            int playerCount = getPlayerCount(roomName);
                            out.println("Player count: " + playerCount);
                        } else {
                            out.println("Room " + roomName + " is full or does not exist.");
                        }
                    } else {
                        out.println("Invalid command. Use: create roomName capacity or join roomName");
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
