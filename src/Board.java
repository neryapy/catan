import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.List;
import javax.imageio.ImageIO;
import javax.swing.JPanel;
import java.awt.geom.AffineTransform;

public class Board extends JPanel implements MouseListener {
    private List<HexagonResource> hexagons;
    private List<Road> roads;
    private static Map<Integer, Set<Integer>> villages;
    private static Map<Integer, Set<Integer>> cities;
    private final int hexSize = 30;
    private final double verticalSpacing;
    private final double horizontalSpacing;
    private BufferedImage portOreImage;
    private int indexGener = 0;
    private int mode = 3; // Default mode
    private static int offsetX;
    private static int offsetY;

    public Board() {
        hexagons = new ArrayList<>();
        roads = new ArrayList<>();
        villages = new HashMap<>();
        cities = new HashMap<>();
        verticalSpacing = Math.sqrt(3) * hexSize;
        horizontalSpacing = 1.5 * hexSize;

        int[][] boardPattern = {
                {0, 0, 1, 1, 1, 0, 0},
                {0, 1, 1, 1, 1, 0, 0},
                {0, 1, 1, 1, 1, 1, 0},
                {0, 1, 1, 1, 1, 0, 0},
                {0, 0, 1, 1, 1, 0, 0}
        };

        Map<String, Integer> resourceCounts = new HashMap<>();
        resourceCounts.put("Wood", 4);
        resourceCounts.put("Sheep", 4);
        resourceCounts.put("Ore", 3);
        resourceCounts.put("Wheat", 4);
        resourceCounts.put("Brick", 3);
        resourceCounts.put("Desert", 1);

        List<String> resources = new ArrayList<>();
        for (Map.Entry<String, Integer> entry : resourceCounts.entrySet()) {
            for (int i = 0; i < entry.getValue(); i++) {
                resources.add(entry.getKey());
            }
        }

        List<Integer> numbers = new ArrayList<>(List.of(2, 3, 3, 4, 4, 5, 5, 6, 6, 8, 8, 9, 9, 10, 10, 11, 11, 12));
        Collections.shuffle(numbers);
        Collections.shuffle(resources);

        for (int row = 0; row < boardPattern.length; row++) {
            for (int col = 0; col < boardPattern[row].length; col++) {
                if (boardPattern[row][col] == 0) continue;

                int x = (int) (1.1 * col * horizontalSpacing);
                int y = (int) (0.85 * row * verticalSpacing);

                if (row % 2 == 1) {
                    x += horizontalSpacing / 2;
                }

                String resourceType = getNextResourceType(resources);
                int number = (resourceType.equals("Desert")) ? 0 : getNextNumber(numbers);

                indexGener++;
                hexagons.add(new HexagonResource(x, y, resourceType, number, hexSize, indexGener));
            }
        }

        printSortedHexagons();
        addMouseListener(this);
    }

    private String getNextResourceType(List<String> resources) {
        return resources.isEmpty() ? "Unknown" : resources.remove(0);
    }

    private int getNextNumber(List<Integer> numbers) {
        return numbers.isEmpty() ? 0 : numbers.remove(0);
    }

    private void drawPort(Graphics2D g2d, int x, int y, String path) {
        try {
            portOreImage = ImageIO.read(new File(path));
        } catch (IOException e) {
            e.printStackTrace();
        }
        int imageWidth = portOreImage.getWidth();
        int imageHeight = portOreImage.getHeight();
        g2d.drawImage(portOreImage, x, y, null);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2d = (Graphics2D) g;

        // Calculate the bounding box for the board
        int maxRowSize = 7; // Maximum number of hexagons in any row
        int boardWidth = (int) (horizontalSpacing * (maxRowSize - 1) + hexSize * 2); // Adjust for widest row
        int boardHeight = (int) (verticalSpacing * (maxRowSize - 1) + hexSize * 2); // Adjust for total height

        // Calculate the offset to center the board
        int panelWidth = getWidth();
        int panelHeight = getHeight();
        int offsetX = (panelWidth - boardWidth) / 2;
        int offsetY = (panelHeight - boardHeight) / 2;

        // Draw the background color
        g2d.setColor(new Color(13, 154, 214)); // Color #0D9AD6
        g2d.fillRect(0, 0, panelWidth, panelHeight);

        g2d.setColor(new Color(227, 118, 25)); // Red
        g2d.drawString("2:1 brick", offsetX + 65, offsetY - 55);
        drawPort(g2d, offsetX + 65, offsetY - 55, "C:/prog/workspace java/catan/src/brick_port.png");

        g2d.setColor(new Color(255, 255, 255)); // Red
        drawPort(g2d, offsetX + 145, offsetY - 45, "C:/prog/workspace java/catan/src/ore_port.png");
        g2d.drawString("3:1 everything", offsetX + 145, offsetY - 55);

        g2d.setColor(new Color(156, 141, 118));
        drawPort(g2d, offsetX + 220, offsetY, "C:/prog/workspace java/catan/src/ore_port.png");
        g2d.drawString("2:1 ore", offsetX + 245, offsetY);

        g2d.setColor(new Color(161, 102, 47));
        drawPort(g2d, offsetX + 270, offsetY + 75, "C:/prog/workspace java/catan/src/wood_port.png");
        g2d.drawString("2:1 wood", offsetX + 295, offsetY + 95);

        g2d.setColor(new Color(105, 150, 0));
        drawPort(g2d, offsetX + 220, offsetY + 145, "C:/prog/workspace java/catan/src/sheep_port.png");
        g2d.drawString("2:1 sheep", offsetX + 270, offsetY + 170);

        g2d.setColor(new Color(255, 255, 255));
        drawPort(g2d, offsetX + 145, offsetY + 190, "C:/prog/workspace java/catan/src/sheep_port.png");
        g2d.drawString("3:1 everything", offsetX + 145, offsetY + 240);

        g2d.setColor(new Color(255, 215, 0));
        g2d.drawString("2:1 wheel", offsetX + 70, offsetY + 240);
        drawPort(g2d, offsetX + 75, offsetY + 195, "C:/prog/workspace java/catan/src/wheel_port.png");

        g2d.setColor(new Color(255, 255, 255, 255));
        drawPort(g2d, offsetX + 28, offsetY + 115, "C:/prog/workspace java/catan/src/everything_port.png");
        g2d.drawString("3:1 everything", offsetX - 50, offsetY + 130);

        g2d.setColor(new Color(255, 255, 255, 255));
        drawPort(g2d, offsetX + 28, offsetY + 30, "C:/prog/workspace java/catan/src/everything_port.png");
        g2d.drawString("3:1 everything", offsetX - 50, offsetY + 45);

        AffineTransform transform = new AffineTransform();
        transform.translate(offsetX, offsetY);
        g2d.setTransform(transform);

        // Draw the hexagons
        for (HexagonResource hex : hexagons) {
            hex.draw(g2d);
        }
        // Draw the cities
        for (Map.Entry<Integer, Set<Integer>> entry : cities.entrySet()) {
            int index = entry.getKey();
            Set<Integer> vertices = entry.getValue();
            for (int vertex : vertices) {
                new City(index, vertex).draw(g2d, hexagons);
            }
        }

        // Draw the roads
        for (Road road : roads) {
            road.draw(g2d, hexagons);
        }

        // Draw the villages
        for (Map.Entry<Integer, Set<Integer>> entry : villages.entrySet()) {
            int index = entry.getKey();
            Set<Integer> vertices = entry.getValue();
            for (int vertex : vertices) {
                new Village(index, vertex).draw(g2d, hexagons);
            }
        }
    }

    private void drawPorts(Graphics2D g2d, int offsetX, int offsetY) {
        g2d.setColor(new Color(227, 118, 25));
        g2d.drawString("2:1 brick", offsetX + 65, offsetY - 55);
        drawPort(g2d, offsetX + 65, offsetY - 55, "C:/prog/workspace java/catan/src/brick_port.png");

        g2d.setColor(new Color(255, 255, 255));
        drawPort(g2d, offsetX + 145, offsetY - 45, "C:/prog/workspace java/catan/src/ore_port.png");
        g2d.drawString("3:1 everything", offsetX + 145, offsetY - 55);

        g2d.setColor(new Color(156, 141, 118));
        drawPort(g2d, offsetX + 220, offsetY, "C:/prog/workspace java/catan/src/ore_port.png");
        g2d.drawString("2:1 ore", offsetX + 245, offsetY);

        g2d.setColor(new Color(161, 102, 47));
        drawPort(g2d, offsetX + 270, offsetY + 75, "C:/prog/workspace java/catan/src/wood_port.png");
        g2d.drawString("2:1 wood", offsetX + 295, offsetY + 95);

        g2d.setColor(new Color(105, 150, 0));
        drawPort(g2d, offsetX + 220, offsetY + 145, "C:/prog/workspace java/catan/src/sheep_port.png");
        g2d.drawString("2:1 sheep", offsetX + 270, offsetY + 170);

        g2d.setColor(new Color(255, 255, 255));
        drawPort(g2d, offsetX + 145, offsetY + 190, "C:/prog/workspace java/catan/src/sheep_port.png");
        g2d.drawString("3:1 everything", offsetX + 145, offsetY + 240);

        g2d.setColor(new Color(255, 215, 0));
        g2d.drawString("2:1 wheel", offsetX + 70, offsetY + 240);
        drawPort(g2d, offsetX + 75, offsetY + 195, "C:/prog/workspace java/catan/src/wheel_port.png");

        g2d.setColor(new Color(255, 255, 255, 255));
        drawPort(g2d, offsetX + 28, offsetY + 115, "C:/prog/workspace java/catan/src/everything_port.png");
        g2d.drawString("3:1 everything", offsetX - 50, offsetY + 130);

        g2d.setColor(new Color(255, 255, 255, 255));
        drawPort(g2d, offsetX + 28, offsetY + 30, "C:/prog/workspace java/catan/src/everything_port.png");
        g2d.drawString("3:1 everything", offsetX - 50, offsetY + 45);
    }
    @Override
    public void mouseClicked(MouseEvent e) {
        int x = e.getX();
        int y = e.getY();
        int panelWidth = getWidth();
        int panelHeight = getHeight();
        int maxRowSize = 7;
        int boardWidth = (int) (horizontalSpacing * (maxRowSize - 1) + hexSize * 2);
        int boardHeight = (int) (verticalSpacing * (maxRowSize - 1) + hexSize * 2);
        int offsetX = (panelWidth - boardWidth) / 2;
        int offsetY = (panelHeight - boardHeight) / 2;

        int relativeX = x - offsetX;
        int relativeY = y - offsetY;

        // Debug prints
        System.out.println("Clicked at absolute coordinates: (" + x + ", " + y + ")");
        System.out.println("Clicked at relative coordinates: (" + relativeX + ", " + relativeY + ")");
        if(mode==1){
            if(23<=relativeY && relativeY<=31&&relativeX<=100&&relativeX>=92){addVillage(0,1);
            System.out.println(villages);}
            if(7<=relativeY && relativeY<=15&&relativeX<=72&&relativeX>=66){addVillage(0,2);}
            if(-22<=relativeY && relativeY<=-14&&relativeX<=72&&relativeX>=66){addVillage(0,3);}
            if(-37<=relativeY && relativeY<=-29&&relativeX<=100&&relativeX>=92){addVillage(0,4);}

            if(23<=relativeY && relativeY<=31&&relativeX<=147&&relativeX>=141){addVillage(1,1);}
            if(-22<=relativeY && relativeY<=-14&&relativeX<=126&&relativeX>=116){addVillage(1,3);}
            if(-37<=relativeY && relativeY<=-29&&relativeX<=147&&relativeX>=141){addVillage(1,4);}

            if(7<=relativeY && relativeY<=15&&relativeX<=222&&relativeX>=216){addVillage(2,0);}
            if(23<=relativeY && relativeY<=31&&relativeX<=197&&relativeX>=191){addVillage(2,1);}
            if(-22<=relativeY && relativeY<=-14&&relativeX<=169&&relativeX>=161){addVillage(2,3);}
            if(-37<=relativeY && relativeY<=-29&&relativeX<=197&&relativeX>=191){addVillage(2,4);}
            if(-22<=relativeY && relativeY<=-14&&relativeX<=222&&relativeX>=216){addVillage(2,5);}

            if(50<=relativeY && relativeY<=58&&relativeX<=97&&relativeX>=89){addVillage(3,0);}
            if(23<=relativeY && relativeY<=31&&relativeX<=170&&relativeX>=160){addVillage(3,1);}
            if(66<=relativeY && relativeY<=72&&relativeX<=72&&relativeX>64){addVillage(3,1);}
            if(52<=relativeY && relativeY<=60&&relativeX<=46&&relativeX>=38){addVillage(3,2);}
            if(21<=relativeY && relativeY<=28&&relativeX<=46&&relativeX>=38){addVillage(3,3);}

            if(52<=relativeY && relativeY<=60&&relativeX<=149&&relativeX>=139){addVillage(4,0);}
            if(67<=relativeY && relativeY<=75&&relativeX<=122&&relativeX>=114){addVillage(4,1);}
            if(7<=relativeY && relativeY<=15&&relativeX<=123&&relativeX>=117){addVillage(4,4);}

            if(52<=relativeY && relativeY<=60&&relativeX<=196&&relativeX>=188){addVillage(5,0);}
            if(67<=relativeY && relativeY<=75&&relativeX<=171&&relativeX>=163){addVillage(5,1);}
            if(7<=relativeY && relativeY<=15&&relativeX<=173&&relativeX>=160){addVillage(5,4);}

            if(52<=relativeY && relativeY<=60&&relativeX<=246&&relativeX>=238){addVillage(6,0);}
            if(67<=relativeY && relativeY<=75&&relativeX<=221&&relativeX>=213){addVillage(6,1);}
            if(21<=relativeY && relativeY<=29&&relativeX<=246&&relativeX>=238){addVillage(6,5);}

            if(96<=relativeY && relativeY<=104&&relativeX<=75&&relativeX>=67){addVillage(7,0);}
            if(111<=relativeY && relativeY<=119&&relativeX<=50&&relativeX>=42){addVillage(7,1);}
            if(96<=relativeY && relativeY<=104&&relativeX<=24&&relativeX>=16){addVillage(7,2);}
            if(66<=relativeY && relativeY<=74&&relativeX<=24&&relativeX>=16){addVillage(7,3);}

            if(96<=relativeY && relativeY<=104&&relativeX<=125&&relativeX>=117){addVillage(8,0);}
            if(113<=relativeY && relativeY<=121&&relativeX<=101&&relativeX>=94){addVillage(8,1);}

            if(96<=relativeY && relativeY<=104&&relativeX<=172&&relativeX>=166){addVillage(9,0);}
            if(111<=relativeY && relativeY<=119&&relativeX<=149&&relativeX>=141){addVillage(9,1);}

            if(96<=relativeY && relativeY<=104&&relativeX<=224&&relativeX>=216){addVillage(10,0);}
            if(111<=relativeY && relativeY<=119&&relativeX<=199&&relativeX>=191){addVillage(10,1);}

            if(96<=relativeY && relativeY<=104&&relativeX<=273&&relativeX>=265){addVillage(11,0);}
            if(111<=relativeY && relativeY<=119&&relativeX<=248&&relativeX>=240){addVillage(11,1);}
            if(65<=relativeY && relativeY<=73&&relativeX<=273&&relativeX>=265){addVillage(11,5);}

            if(140<=relativeY && relativeY<=148&&relativeX<=97&&relativeX>=89){addVillage(12,0);}
            if(155<=relativeY && relativeY<=163&&relativeX<=72&&relativeX>=64){addVillage(12,1);}
            if(140<=relativeY && relativeY<=148&&relativeX<=46&&relativeX>=38){addVillage(12,2);}

            if(140<=relativeY && relativeY<=148&&relativeX<=147&&relativeX>=139){addVillage(13,0);}
            if(155<=relativeY && relativeY<=163&&relativeX<=122&&relativeX>=114){addVillage(13,1);}

            if(140<=relativeY && relativeY<=148&&relativeX<=196&&relativeX>=188){addVillage(14,0);}
            if(155<=relativeY && relativeY<=163&&relativeX<=176&&relativeX>=168){addVillage(14,1);}

            if(140<=relativeY && relativeY<=148&&relativeX<=246&&relativeX>=238){addVillage(15,0);}
            if(155<=relativeY && relativeY<=163&&relativeX<=226&&relativeX>=218){addVillage(15,1);}

            if(184<=relativeY && relativeY<=192&&relativeX<=125&&relativeX>=117){addVillage(16,0);}
            if(199<=relativeY && relativeY<=207&&relativeX<=98&&relativeX>=92){addVillage(16,1);}
            if(184<=relativeY && relativeY<=192&&relativeX<=74&&relativeX>=66){addVillage(16,2);}

            if(184<=relativeY && relativeY<=192&&relativeX<=174&&relativeX>=166){addVillage(17,0);}
            if(199<=relativeY && relativeY<=207&&relativeX<=149&&relativeX>=141){addVillage(17,1);}

            if(184<=relativeY && relativeY<=192&&relativeX<=224&&relativeX>=216){addVillage(18,0);}
            if(199<=relativeY && relativeY<=207&&relativeX<=199&&relativeX>=191){addVillage(18,1);}

        }
        if (mode==2){
            if(23<=relativeY && relativeY<=31&&relativeX<=100&&relativeX>=92){addCity(0,1);}
            if(7<=relativeY && relativeY<=15&&relativeX<=72&&relativeX>=66){addCity(0,2);}
            if(-22<=relativeY && relativeY<=-14&&relativeX<=72&&relativeX>=66){addCity(0,3);}
            if(-37<=relativeY && relativeY<=-29&&relativeX<=100&&relativeX>=92){addCity(0,4);}

            if(23<=relativeY && relativeY<=31&&relativeX<=147&&relativeX>=141){addCity(1,1);}
            if(-22<=relativeY && relativeY<=-14&&relativeX<=126&&relativeX>=116){addCity(1,3);}
            if(-37<=relativeY && relativeY<=-29&&relativeX<=147&&relativeX>=141){addCity(1,4);}

            if(7<=relativeY && relativeY<=15&&relativeX<=222&&relativeX>=216){addCity(2,0);}
            if(23<=relativeY && relativeY<=31&&relativeX<=197&&relativeX>=191){addCity(2,1);}
            if(-22<=relativeY && relativeY<=-14&&relativeX<=169&&relativeX>=161){addCity(2,3);}
            if(-37<=relativeY && relativeY<=-29&&relativeX<=197&&relativeX>=191){addCity(2,4);}
            if(-22<=relativeY && relativeY<=-14&&relativeX<=222&&relativeX>=216){addCity(2,5);}

            if(50<=relativeY && relativeY<=58&&relativeX<=97&&relativeX>=89){addCity(3,0);}
            if(23<=relativeY && relativeY<=31&&relativeX<=170&&relativeX>=160){addCity(3,1);}
            if(66<=relativeY && relativeY<=72&&relativeX<=72&&relativeX>64){addCity(3,1);}
            if(52<=relativeY && relativeY<=60&&relativeX<=46&&relativeX>=38){addCity(3,2);}
            if(21<=relativeY && relativeY<=28&&relativeX<=46&&relativeX>=38){addCity(3,3);}

            if(52<=relativeY && relativeY<=60&&relativeX<=149&&relativeX>=139){addCity(4,0);}
            if(67<=relativeY && relativeY<=75&&relativeX<=122&&relativeX>=114){addCity(4,1);}
            if(7<=relativeY && relativeY<=15&&relativeX<=123&&relativeX>=117){addCity(4,4);}

            if(52<=relativeY && relativeY<=60&&relativeX<=196&&relativeX>=188){addCity(5,0);}
            if(67<=relativeY && relativeY<=75&&relativeX<=171&&relativeX>=163){addCity(5,1);}
            if(7<=relativeY && relativeY<=15&&relativeX<=173&&relativeX>=160){addCity(5,4);}

            if(52<=relativeY && relativeY<=60&&relativeX<=246&&relativeX>=238){addCity(6,0);}
            if(67<=relativeY && relativeY<=75&&relativeX<=221&&relativeX>=213){addCity(6,1);}
            if(21<=relativeY && relativeY<=29&&relativeX<=246&&relativeX>=238){addCity(6,5);}

            if(96<=relativeY && relativeY<=104&&relativeX<=75&&relativeX>=67){addCity(7,0);}
            if(111<=relativeY && relativeY<=119&&relativeX<=50&&relativeX>=42){addCity(7,1);}
            if(96<=relativeY && relativeY<=104&&relativeX<=24&&relativeX>=16){addCity(7,2);}
            if(66<=relativeY && relativeY<=74&&relativeX<=24&&relativeX>=16){addCity(7,3);}

            if(96<=relativeY && relativeY<=104&&relativeX<=125&&relativeX>=117){addCity(8,0);}
            if(113<=relativeY && relativeY<=121&&relativeX<=101&&relativeX>=94){addCity(8,1);}

            if(96<=relativeY && relativeY<=104&&relativeX<=172&&relativeX>=166){addCity(9,0);}
            if(111<=relativeY && relativeY<=119&&relativeX<=149&&relativeX>=141){addCity(9,1);}

            if(96<=relativeY && relativeY<=104&&relativeX<=224&&relativeX>=216){addCity(10,0);}
            if(111<=relativeY && relativeY<=119&&relativeX<=199&&relativeX>=191){addCity(10,1);}

            if(96<=relativeY && relativeY<=104&&relativeX<=273&&relativeX>=265){addCity(11,0);}
            if(111<=relativeY && relativeY<=119&&relativeX<=248&&relativeX>=240){addCity(11,1);}
            if(65<=relativeY && relativeY<=73&&relativeX<=273&&relativeX>=265){addCity(11,5);}

            if(140<=relativeY && relativeY<=148&&relativeX<=97&&relativeX>=89){addCity(12,0);}
            if(155<=relativeY && relativeY<=163&&relativeX<=72&&relativeX>=64){addCity(12,1);}
            if(140<=relativeY && relativeY<=148&&relativeX<=46&&relativeX>=38){addCity(12,2);}

            if(140<=relativeY && relativeY<=148&&relativeX<=147&&relativeX>=139){addCity(13,0);}
            if(155<=relativeY && relativeY<=163&&relativeX<=122&&relativeX>=114){addCity(13,1);}

            if(140<=relativeY && relativeY<=148&&relativeX<=196&&relativeX>=188){addCity(14,0);}
            if(155<=relativeY && relativeY<=163&&relativeX<=176&&relativeX>=168){addCity(14,1);}

            if(140<=relativeY && relativeY<=148&&relativeX<=246&&relativeX>=238){addCity(15,0);}
            if(155<=relativeY && relativeY<=163&&relativeX<=226&&relativeX>=218){addCity(15,1);}

            if(184<=relativeY && relativeY<=192&&relativeX<=125&&relativeX>=117){addCity(16,0);}
            if(199<=relativeY && relativeY<=207&&relativeX<=98&&relativeX>=92){addCity(16,1);}
            if(184<=relativeY && relativeY<=192&&relativeX<=74&&relativeX>=66){addCity(16,2);}

            if(184<=relativeY && relativeY<=192&&relativeX<=174&&relativeX>=166){addCity(17,0);}
            if(199<=relativeY && relativeY<=207&&relativeX<=149&&relativeX>=141){addCity(17,1);}

            if(184<=relativeY && relativeY<=192&&relativeX<=224&&relativeX>=216){addCity(18,0);}
            if(199<=relativeY && relativeY<=207&&relativeX<=199&&relativeX>=191){addCity(18,1);}

        }
        if (mode == 3) {
            if (17 <= relativeY && relativeY <= 22 && relativeX >= 78 && relativeX <= 82) {
                addRoad(new Road(0, 1, 2));
                System.out.println("Road added: 0, 1, 2");
            }
            if (-5 <= relativeY && relativeY <= -3 && relativeX >= 72 && relativeX <= 76) {
                addRoad(new Road(0, 2, 3));
                System.out.println("Road added: 0, 2, 3");
            }
            if (-27 <= relativeY && relativeY <= -22 && relativeX >= 83 && relativeX <= 88) {
                addRoad(new Road(0, 3, 4));
                System.out.println("Road added: 0, 3, 4");
            }
            if (17 <= relativeY && relativeY <= 21 && relativeX >= 103 && relativeX <= 108) {
                addRoad(new Road(0, 0, 1));
                System.out.println("Road added: 0, 0, 1");
            }
            if (-10 <= relativeY && relativeY <= -5 && relativeX >= 119 && relativeX <= 126) {
                addRoad(new Road(0, 6, 5));
                System.out.println("Road added: 0, 6, 5");
            }
            if (-26 <= relativeY && relativeY <= -22 && relativeX >= 105 && relativeX <= 110) {
                addRoad(new Road(0, 4, 5));
                System.out.println("Road added: 0, 4, 5");
            }
            if (-27 <= relativeY && relativeY <= -20 && relativeX >= 134 && relativeX <= 140) {
                addRoad(new Road(1, 3, 4));
                System.out.println("Road added: 1, 3, 4");
            }
            if (17 <= relativeY && relativeY <= 22 && relativeX >= 131 && relativeX <= 136) {
                addRoad(new Road(1, 2, 1));
                System.out.println("Road added: 1, 2, 1");
            }
            if (18 <= relativeY && relativeY <= 23 && relativeX >= 152 && relativeX <= 158) {
                addRoad(new Road(1, 0, 1));
                System.out.println("Road added: 1, 0, 1");
            }
            if (-28 <= relativeY && relativeY <= -23 && relativeX >= 154 && relativeX <= 160) {
                addRoad(new Road(1, 4, 5));
                System.out.println("Road added: 1, 4, 5");
            }
            if (-6 <= relativeY && relativeY <= 1 && relativeX >= 169 && relativeX <= 175) {
                addRoad(new Road(1, 0, 5));
                System.out.println("Road added: 1, 0, 5");
            }
            if (76 <= relativeY && relativeY <= 82 && relativeX >= 64 && relativeX <= 72) {
                addRoad(new Road(3, 0, 1));
                System.out.println("Road added: 3, 0, 1");
            }
            if (62 <= relativeY && relativeY <= 70 && relativeX >= 38 && relativeX <= 46) {
                addRoad(new Road(3, 0, 2));
                System.out.println("Road added: 3, 0, 2");
            }
            if (31 <= relativeY && relativeY <= 38 && relativeX >= 38 && relativeX <= 46) {
                addRoad(new Road(3, 0, 3));
                System.out.println("Road added: 3, 0, 3");
            }
            if (-26 <= relativeY && relativeY <= -20 && relativeX >= 182 && relativeX <= 190) {
                addRoad(new Road(2, 3, 4));
                System.out.println("Road added: 2, 3, 4");
            }
            if (-26 <= relativeY && relativeY <= -20 && relativeX >= 205 && relativeX <= 212) {
                addRoad(new Road(2, 4, 5));
                System.out.println("Road added: 2, 4, 5");
            }
            if (-8 <= relativeY && relativeY <= 2 && relativeX >= 222 && relativeX <= 228) {
                addRoad(new Road(2, 5, 6));
                System.out.println("Road added: 2, 5, 6");
            }
            if (17 <= relativeY && relativeY <= 25 && relativeX >= 178 && relativeX <= 185) {
                addRoad(new Road(2, 2, 1));
                System.out.println("Road added: 2, 2, 1");
            }
            if (17 <= relativeY && relativeY <= 25 && relativeX >= 204 && relativeX <= 212) {
                addRoad(new Road(2, 6, 1));
                System.out.println("Road added: 2, 6, 1");
            }
            if (17 <= relativeY && relativeY <= 25 && relativeX >= 55 && relativeX <= 62) {
                addRoad(new Road(3, 3, 4));
                System.out.println("Road added: 3, 3, 4");
            }
            if (38 <= relativeY && relativeY <= 44 && relativeX >= 92 && relativeX <= 97) {
                addRoad(new Road(3, 5, 0));
                System.out.println("Road added: 3, 5, 0");
            }
            if (38 <= relativeY && relativeY <= 44 && relativeX >= 42 && relativeX <= 47) {
                addRoad(new Road(3, 3, 2));
                System.out.println("Road added: 3, 3, 2");
            }
            if (60 <= relativeY && relativeY <= 64 && relativeX >= 55 && relativeX <= 60) {
                addRoad(new Road(3, 1, 2));
                System.out.println("Road added: 3, 1, 2");
            }

            if (62 <= relativeY && relativeY <= 67 && relativeX >= 82 && relativeX <= 87) {
                addRoad(new Road(3, 1, 0));
                System.out.println("Road added: 3, 1, 0");
            }
            if (38 <= relativeY && relativeY <= 44 && relativeX >= 142 && relativeX <= 147) {
                addRoad(new Road(4, 5, 6));
                System.out.println("Road added: 4, 5, 6");
            }
            if (62 <= relativeY && relativeY <= 67 && relativeX >= 132 && relativeX <= 137) {
                addRoad(new Road(4, 1, 0));
                System.out.println("Road added: 4, 1, 0");
            }
            if (62 <= relativeY && relativeY <= 67 && relativeX >= 105 && relativeX <= 110) {
                addRoad(new Road(4, 2, 1));
                System.out.println("Road added: 4, 2, 1");
            }
            if (62 <= relativeY && relativeY <= 67 && relativeX >= 157 && relativeX <= 163) {
                addRoad(new Road(5, 1, 2));
                System.out.println("Road added: 5, 1, 2");
            }
            if (38 <= relativeY && relativeY <= 44 && relativeX >= 188 && relativeX <= 196) {
                addRoad(new Road(5, 5, 0));
                System.out.println("Road added: 5, 5, 0");
            }
            if (64 <= relativeY && relativeY <= 70 && relativeX >= 178 && relativeX <= 184) {
                addRoad(new Road(5, 0, 1));
                System.out.println("Road added: 5, 0, 1");
            }
            if (18 <= relativeY && relativeY <= 25 && relativeX >= 228 && relativeX <= 235) {
                addRoad(new Road(6, 4, 5));
                System.out.println("Road added: 6, 4, 5");
            }
            if (38 <= relativeY && relativeY <= 45 && relativeX >= 243 && relativeX <= 248) {
                addRoad(new Road(6, 0, 5));
                System.out.println("Road added: 6, 0, 5");
            }
            if (61 <= relativeY && relativeY <= 67 && relativeX >= 206 && relativeX <= 211) {
                addRoad(new Road(6, 1, 2));
                System.out.println("Road added: 6, 1, 2");
            }
            if (61 <= relativeY && relativeY <= 67 && relativeX >= 229 && relativeX <= 235) {
                addRoad(new Road(6, 0, 1));
                System.out.println("Road added: 6, 0, 1");
            }
            if (86 <= relativeY && relativeY <= 91 && relativeX >= 67 && relativeX <= 75) {
                addRoad(new Road(7, 5, 0));
                System.out.println("Road added: 7, 5, 0");
            }
            if (64 <= relativeY && relativeY <= 69 && relativeX >= 30 && relativeX <= 38) {
                addRoad(new Road(7, 3, 4));
                System.out.println("Road added: 7, 3, 4");
            }
            if (106 <= relativeY && relativeY <= 114 && relativeX >= 16 && relativeX <= 24) {
                addRoad(new Road(7, 2, 3));
                System.out.println("Road added: 7, 2, 3");
            }
            if (76 <= relativeY && relativeY <= 84 && relativeX >= 16 && relativeX <= 24) {
                addRoad(new Road(7, 0, 3));
                System.out.println("Road added: 7, 0, 3");
            }
            if (108 <= relativeY && relativeY <= 113 && relativeX >= 55 && relativeX <= 60) {
                addRoad(new Road(7, 0, 1));
                System.out.println("Road added: 7, 0, 1");
            }
            if (82 <= relativeY && relativeY <= 90 && relativeX >= 72 && relativeX <= 78) {
                addRoad(new Road(7, 0, 5));
                System.out.println("Road added: 7, 0, 5");
            }
            if (105 <= relativeY && relativeY <= 111 && relativeX >= 28 && relativeX <= 36) {
                addRoad(new Road(7, 1, 2));
                System.out.println("Road added: 7, 1, 2");
            }
            if (84 <= relativeY && relativeY <= 90 && relativeX >= 21 && relativeX <= 26) {
                addRoad(new Road(7, 2, 3));
                System.out.println("Road added: 7, 2, 3");
            }
            if (84 <= relativeY && relativeY <= 90 && relativeX >= 120 && relativeX <= 126) {
                addRoad(new Road(8, 0, 5));
                System.out.println("Road added: 8, 0, 5");
            }
            if (107 <= relativeY && relativeY <= 113 && relativeX >= 82 && relativeX <= 88) {
                addRoad(new Road(8, 1, 2));
                System.out.println("Road added: 8, 1, 2");
            }
            if (107 <= relativeY && relativeY <= 113 && relativeX >= 107 && relativeX <= 113) {
                addRoad(new Road(8, 0, 1));
                System.out.println("Road added: 8, 0, 1");
            }
            if (85 <= relativeY && relativeY <= 90 && relativeX >= 168 && relativeX <= 175) {
                addRoad(new Road(9, 5, 0));
                System.out.println("Road added: 9, 5, 0");
            }
            if (106 <= relativeY && relativeY <= 111 && relativeX >= 128 && relativeX <= 133) {
                addRoad(new Road(9, 1, 2));
                System.out.println("Road added: 9, 1, 2");
            }
            if (106 <= relativeY && relativeY <= 111 && relativeX >= 155 && relativeX <= 160) {
                addRoad(new Road(9, 0, 1));
                System.out.println("Road added: 9, 0, 1");
            }
            if (80 <= relativeY && relativeY <= 86 && relativeX >= 216 && relativeX <= 224) {
                addRoad(new Road(10, 5, 0));
                System.out.println("Road added: 10, 5, 0");
            }
            if (108 <= relativeY && relativeY <= 113 && relativeX >= 204 && relativeX <= 210) {
                addRoad(new Road(10, 0, 1));
                System.out.println("Road added: 10, 0, 1");
            }

            if (108 <= relativeY && relativeY <= 113 && relativeX >= 180 && relativeX <= 186) {
                addRoad(new Road(10, 1, 2));
                System.out.println("Road added: 10, 1, 2");
            }
            if (60 <= relativeY && relativeY <= 66 && relativeX >= 253 && relativeX <= 261) {
                addRoad(new Road(11, 4, 5));
                System.out.println("Road added: 11, 4, 5");
            }
            if (81 <= relativeY && relativeY <= 87 && relativeX >= 265 && relativeX <= 273) {
                addRoad(new Road(11, 5, 0));
                System.out.println("Road added: 11, 5, 0");
            }
            if (108 <= relativeY && relativeY <= 114 && relativeX >= 255 && relativeX <= 260) {
                addRoad(new Road(11, 0, 1));
                System.out.println("Road added: 11, 0, 1");
            }
            if (107 <= relativeY && relativeY <= 112 && relativeX >= 230 && relativeX <= 236) {
                addRoad(new Road(11, 1, 2));
                System.out.println("Road added: 11, 1, 2");
            }
            if (126 <= relativeY && relativeY <= 132 && relativeX >= 93 && relativeX <= 99) {
                addRoad(new Road(12, 5, 0));
                System.out.println("Road added: 12, 5, 0");
            }
            if (153 <= relativeY && relativeY <= 159 && relativeX >= 80 && relativeX <= 86) {
                addRoad(new Road(12, 0, 1));
                System.out.println("Road added: 12, 0, 1");
            }
            if (150 <= relativeY && relativeY <= 156 && relativeX >= 53 && relativeX <= 58) {
                addRoad(new Road(12, 1, 2));
                System.out.println("Road added: 12, 1, 2");
            }
            if (131 <= relativeY && relativeY <= 137 && relativeX >= 41 && relativeX <= 47) {
                addRoad(new Road(12, 2, 3));
                System.out.println("Road added: 12, 2, 3");
            }
            if (131 <= relativeY && relativeY <= 137 && relativeX >= 41 && relativeX <= 47) {
                addRoad(new Road(12, 2, 3));
                System.out.println("Road added: 12, 2, 3");
            }
            if (125 <= relativeY && relativeY <= 131 && relativeX >= 141 && relativeX <= 147) {
                addRoad(new Road(13, 0, 5));
                System.out.println("Road added: 13, 0, 5");
            }
            if (154 <= relativeY && relativeY <= 160 && relativeX >= 130 && relativeX <= 136) {
                addRoad(new Road(13, 0, 1));
                System.out.println("Road added: 13, 0, 1");
            }
            if (149 <= relativeY && relativeY <= 155 && relativeX >= 107 && relativeX <= 113) {
                addRoad(new Road(13, 1, 2));
                System.out.println("Road added: 13, 1, 2");
            }
            if (128 <= relativeY && relativeY <= 134 && relativeX >= 193 && relativeX <= 200) {
                addRoad(new Road(14, 0, 5));
                System.out.println("Road added: 14, 0, 5");
            }
            if (149 <= relativeY && relativeY <= 155 && relativeX >= 180 && relativeX <= 186) {
                addRoad(new Road(14, 0, 1));
                System.out.println("Road added: 14, 0, 1");
            }
            if (149 <= relativeY && relativeY <= 155 && relativeX >= 156 && relativeX <= 161) {
                addRoad(new Road(14, 1, 2));
                System.out.println("Road added: 14, 1, 2");
            }
            if (129 <= relativeY && relativeY <= 135 && relativeX >= 242 && relativeX <= 248) {
                addRoad(new Road(15, 0, 5));
                System.out.println("Road added: 15, 0, 5");
            }
            if (149 <= relativeY && relativeY <= 155 && relativeX >= 231 && relativeX <= 237) {
                addRoad(new Road(15, 0, 1));
                System.out.println("Road added: 15, 0, 1");
            }
            if (149 <= relativeY && relativeY <= 155 && relativeX >= 204 && relativeX <= 210) {
                addRoad(new Road(15, 1, 2));
                System.out.println("Road added: 15, 1, 2");
            }
            if (173 <= relativeY && relativeY <= 179 && relativeX >= 120 && relativeX <= 126) {
                addRoad(new Road(16, 0, 5));
                System.out.println("Road added: 16, 0, 5");
            }
            if (197 <= relativeY && relativeY <= 203 && relativeX >= 107 && relativeX <= 113) {
                addRoad(new Road(16, 1, 0));
                System.out.println("Road added: 16, 1, 0");
            }
            if (197 <= relativeY && relativeY <= 203 && relativeX >= 83 && relativeX <= 89) {
                addRoad(new Road(16, 1, 2));
                System.out.println("Road added: 16, 1, 2");
            }
            if (170 <= relativeY && relativeY <= 175 && relativeX >= 72 && relativeX <= 79) {
                addRoad(new Road(16, 2, 3));
                System.out.println("Road added: 16, 2, 3");
            }

            if (173 <= relativeY && relativeY <= 179 && relativeX >= 170 && relativeX <= 176) {
                addRoad(new Road(17, 0, 5));
                System.out.println("Road added: 17, 0, 5");
            }
            if (197 <= relativeY && relativeY <= 204 && relativeX >= 155 && relativeX <= 161) {
                addRoad(new Road(17, 1, 0));
                System.out.println("Road added: 17, 1, 0");
            }
            if (197 <= relativeY && relativeY <= 203 && relativeX >= 132 && relativeX <= 138) {
                addRoad(new Road(17, 1, 2));
                System.out.println("Road added: 17, 1, 2");
            }

            if (170 <= relativeY && relativeY <= 176 && relativeX >= 221 && relativeX <= 227) {
                addRoad(new Road(18, 0, 5));
                System.out.println("Road added: 18, 0, 5");
            }
            if (197 <= relativeY && relativeY <= 204 && relativeX >= 209 && relativeX <= 215) {
                addRoad(new Road(18, 1, 0));
                System.out.println("Road added: 18, 1, 0");
            }
            if (197 <= relativeY && relativeY <= 203 && relativeX >= 182 && relativeX <= 188) {
                addRoad(new Road(18, 1, 2));
                System.out.println("Road added: 18, 1, 2");
            }
        }

    }
    public void addVillage(int index, int vertex) {
        villages.computeIfAbsent(index, k -> new HashSet<>()).add(vertex);
        repaint();
    }

    public void addCity(int index, int vertex) {
        cities.computeIfAbsent(index, k -> new HashSet<>()).add(vertex);
        repaint();
    }

    @Override
    public void mousePressed(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

    public void addRoad(Road road) {
        roads.add(road);
        repaint();
    }

    private void printSortedHexagons() {
        hexagons.stream()
                .sorted(Comparator.comparingInt(HexagonResource::getIndex))
                .forEach(hexagon -> System.out.println("Hexagon " + hexagon.getIndex() + ": Resource " + hexagon.getResourceType()));
    }
}
