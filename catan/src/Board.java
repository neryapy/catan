import javax.swing.JPanel;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.util.*;
import java.util.List;

public class Board extends JPanel {
    private List<HexagonResource> hexagons;
    private List<Road> roads; // List for roads
    private static List<Village> villages;
    private final int hexSize = 30; // Radius of the hexagon
    private final double verticalSpacing;
    private final double horizontalSpacing;

    public Board() {
        hexagons = new ArrayList<>();
        roads = new ArrayList<>(); // Initialize the roads list
        villages = new ArrayList<>();
        verticalSpacing = Math.sqrt(3) * hexSize; // Vertical spacing between hexagons
        horizontalSpacing = 1.5 * hexSize; // Horizontal spacing between hexagons

        // Create hexagons with rows properly aligned to form a larger hexagon
        int[][] boardPattern = {
                {0, 0, 1, 1, 1, 0, 0},
                {0, 1, 1, 1, 1, 0, 0},
                {0, 1, 1, 1, 1, 1, 0},
                {0, 1, 1, 1, 1, 0, 0},
                {0, 0, 1, 1, 1, 0, 0}
        };

        // Resource counts based on the given pattern
        Map<String, Integer> resourceCounts = new HashMap<>();
        resourceCounts.put("Wood", 4);
        resourceCounts.put("Sheep", 4);
        resourceCounts.put("Ore", 3);
        resourceCounts.put("Wheat", 4);
        resourceCounts.put("Brick", 3);
        resourceCounts.put("Desert", 1);

        // Prepare the list of resources based on the pattern
        List<String> resources = new ArrayList<>();
        for (Map.Entry<String, Integer> entry : resourceCounts.entrySet()) {
            for (int i = 0; i < entry.getValue(); i++) {
                resources.add(entry.getKey());
            }
        }

        // Add numbers for all resources except Desert
        List<Integer> numbers = new ArrayList<>(List.of(2, 3, 3, 4, 4, 5, 5, 6, 6, 8, 8, 9, 9, 10, 10, 11, 11, 12));
        Collections.shuffle(numbers);

        // Shuffle resources to randomize the distribution
        Collections.shuffle(resources);

        // Generate hexagon positions and resources
        for (int row = 0; row < boardPattern.length; row++) {
            for (int col = 0; col < boardPattern[row].length; col++) {
                if (boardPattern[row][col] == 0) continue;

                int x = (int) (1.1 * col * horizontalSpacing);
                int y = (int) (0.85 * row * verticalSpacing);

                // Offset every other row
                if (row % 2 == 1) {
                    x += horizontalSpacing / 2; // Half the horizontal spacing to stagger
                }

                // Assign resource and number
                String resourceType = getNextResourceType(resources);
                int number = (resourceType.equals("Desert")) ? 0 : getNextNumber(numbers);

                // Create hexagon resource with the required size parameter
                hexagons.add(new HexagonResource(x, y, resourceType, number, hexSize));
            }
        }

        printSortedHexagons(); // Print sorted hexagons during initialization
    }


    private String getNextResourceType(List<String> resources) {
        // Get the next resource type in the list
        return resources.isEmpty() ? "Unknown" : resources.remove(0);
    }

    private int getNextNumber(List<Integer> numbers) {
        // Get the next number in the list
        return numbers.isEmpty() ? 0 : numbers.remove(0);
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

        // Set the transformation to center the board
        AffineTransform transform = new AffineTransform();
        transform.translate(offsetX, offsetY);
        g2d.setTransform(transform);

        // Draw the hexagons
        for (HexagonResource hex : hexagons) {
            hex.draw(g2d);
        }

        // Draw the roads
        for (Road road : roads) {
            road.draw(g2d, hexagons);
        }
        for (Village village : villages) {
            village.draw(g2d, hexagons);
        }
    }

    public void addRoad(Road road) {
        roads.add(road);
        repaint();
    }
    public void addVillage(Village village) {
        villages.add(village);
        repaint();
    }
    public void printSortedHexagons() {
        List<HexagonResource> sortedHexagons = new ArrayList<>(hexagons);
        sortedHexagons.sort(Comparator.comparingInt(h -> h.number));
        for (HexagonResource hex : sortedHexagons) {
            System.out.println(hex);
        }
    }
}
