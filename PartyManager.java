import java.awt.*;
import java.util.ArrayList;
import javax.swing.*;

public class PartyManager {
    private JFrame frame = new JFrame("Party Manager");
    private ArrayList<String> clientAddress = new ArrayList<>();
    private int clientPort;

    public PartyManager() {
        frame.setSize(400, 600);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setVisible(true);

        JPanel choicePanel = new JPanel();
        choicePanel.setLayout(null);
        choicePanel.setSize(400, 300);

        JButton hostButton = createStyledButton("Host the game");
        hostButton.addActionListener(e -> showHostPanel());
        hostButton.setSize(160, 40);
        hostButton.setLocation(20, 100);

        JButton joinButton = createStyledButton("Join the game");
        joinButton.addActionListener(e -> showClientPanel());
        joinButton.setSize(160, 40);
        joinButton.setLocation(200, 100);

        choicePanel.add(hostButton);
        choicePanel.add(joinButton);

        frame.add(choicePanel);
    }

    private void showHostPanel() {
        JPanel hostPanel = new JPanel();
        hostPanel.setLayout(new BoxLayout(hostPanel, BoxLayout.Y_AXIS));
        hostPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel clientContainer = new JPanel();
        clientContainer.setLayout(new BoxLayout(clientContainer, BoxLayout.Y_AXIS));
        JScrollPane scrollPane = new JScrollPane(clientContainer);
        scrollPane.setPreferredSize(new Dimension(350, 150));

        JButton addClientButton = createStyledButton("Add Client");
        addClientButton.addActionListener(e -> {
            addClientFields(clientContainer);
            clientContainer.revalidate();
            clientContainer.repaint();
        });

        JButton hostGameButton = createStyledButton("Host Game");
        hostGameButton.addActionListener(e -> {
            for (int i = 0; i < clientAddress.size(); i++) {
                System.out.println(clientAddress.size() + " | ip: " + clientAddress.get(i) + ":" + clientPort);
            }
            new host(clientAddress.toArray(new String[0]), clientPort);
            System.out.println("Game hosted.");
        });

        hostPanel.add(addClientButton);
        hostPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        hostPanel.add(scrollPane);
        hostPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        hostPanel.add(hostGameButton);

        frame.getContentPane().removeAll();
        frame.add(hostPanel);
        frame.revalidate();
        frame.repaint();
    }

    private void showClientPanel() {
        JPanel clientPanel = new JPanel();
        clientPanel.setLayout(new BoxLayout(clientPanel, BoxLayout.Y_AXIS));
        clientPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel ipLabel = new JLabel("Enter IP to connect:");
        JTextField ipConnect = new JTextField();
        ipConnect.setMaximumSize(new Dimension(300, 30));

        JTextField ownIp = new JTextField();
        ownIp.setMaximumSize(new Dimension(300, 30));

        JLabel portLabel = new JLabel("Enter Port:");
        JTextField portField = new JTextField();
        portField.setMaximumSize(new Dimension(300, 30));

        JButton connectButton = createStyledButton("Connect");
        connectButton.addActionListener(e -> {
            clientAddress.clear();
            clientAddress.add(ipConnect.getText());
            clientPort = Integer.parseInt(portField.getText());
            new Client(clientPort, ipConnect.getText(), ownIp.getText());
            System.out.println("Connected to IP: " + clientAddress.get(0) + ", Port: " + clientPort);
        });

        clientPanel.add(ipLabel);
        clientPanel.add(ipConnect);
        clientPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        clientPanel.add(new JLabel("enter the IP of interface you want to use:"));
        clientPanel.add(ownIp);
        clientPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        clientPanel.add(portLabel);
        clientPanel.add(portField);
        clientPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        clientPanel.add(connectButton);

        frame.getContentPane().removeAll();
        frame.add(clientPanel);
        frame.revalidate();
        frame.repaint();
    }

    private void addClientFields(JPanel clientContainer) {
        JPanel clientPanel = new JPanel();
        clientPanel.setLayout(new BoxLayout(clientPanel, BoxLayout.Y_AXIS));
        clientPanel.setBorder(BorderFactory.createTitledBorder("Client"));

        JLabel ipLabel = new JLabel("Enter IP:");
        JTextField ipField = new JTextField();
        ipField.setMaximumSize(new Dimension(300, 30));

        JLabel portLabel = new JLabel("Enter Port:");
        JTextField portField = new JTextField();
        portField.setMaximumSize(new Dimension(300, 30));

        JButton finishButton = createStyledButton("Finish");
        finishButton.addActionListener(e -> {
            clientAddress.add(ipField.getText());
            clientPort = Integer.parseInt(portField.getText());
            System.out.println("Client added.");
            finishButton.setEnabled(false);
        });

        clientPanel.add(ipLabel);
        clientPanel.add(ipField);
        clientPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        clientPanel.add(portLabel);
        clientPanel.add(portField);
        clientPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        clientPanel.add(finishButton);

        clientContainer.add(clientPanel);
    }

    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setPreferredSize(new Dimension(120, 45));
        button.setForeground(Color.WHITE);
        button.setBackground(new Color(45, 145, 255));
        button.setFocusPainted(false);
        button.setFont(new Font("Arial", Font.BOLD, 16));
        button.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
        button.setAlignmentX(Component.CENTER_ALIGNMENT);

        button.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(35, 125, 235), 2),
                BorderFactory.createEmptyBorder(10, 20, 10, 20)
        ));

        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(35, 125, 235));
                button.setForeground(new Color(220, 220, 255));
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(45, 145, 255));
                button.setForeground(Color.WHITE);
            }
        });

        return button;
    }
}
