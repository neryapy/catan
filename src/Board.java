import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import org.json.JSONObject;
import org.json.JSONArray;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.*;

public class Board {
    private boolean buildMode;
    private static List<HexagonResource> hexagons;
    private List<Road> roads;
    static ArrayList<Village> villages = new ArrayList<Village>();
    private static Map<Integer, Set<Integer>> cities;
    private final int hexSize = 30;
    final double verticalSpacing;
    final double horizontalSpacing;
    private BufferedImage portOreImage;
    private int indexGener = 0;
    public static int mode; // Default mode
    public static int ownerAbstarct;
    private static int offsetX;
    private static int offsetY;
    private int[][] boardPattern;
    private static final int[][] VILLAGE_City_COORDINATES = {{0, 1, 23, 31, 92, 100}, {0, 2, 7, 15, 66, 72}, {0, 3, -22, -14, 66, 72}, {0, 4, -37, -29, 92, 100}, {1, 1, 23, 31, 141, 147}, {1, 3, -22, -14, 116, 126}, {1, 4, -37, -29, 141, 147}, {2, 0, 7, 15, 216, 222}, {2, 1, 23, 31, 191, 197}, {2, 3, -22, -14, 161, 169}, {2, 4, -37, -29, 191, 197}, {2, 5, -22, -14, 216, 222}, {3, 0, 50, 58, 89, 97}, {3, 1, 23, 31, 160, 170}, {3, 1, 66, 72, 64, 72}, {3, 2, 52, 60, 38, 46}, {3, 3, 21, 28, 38, 46}, {4, 0, 52, 60, 139, 149}, {4, 1, 67, 75, 114, 122}, {4, 4, 7, 15, 117, 123}, {5, 0, 52, 60, 188, 196}, {5, 1, 67, 75, 163, 171}, {5, 4, 7, 15, 160, 173}, {6, 0, 52, 60, 238, 246}, {6, 1, 67, 75, 213, 221}, {6, 5, 21, 29, 238, 246}, {7, 0, 96, 104, 67, 75}, {7, 1, 111, 119, 42, 50}, {7, 2, 96, 104, 16, 24}, {7, 3, 66, 74, 16, 24}, {8, 0, 96, 104, 117, 125}, {8, 1, 113, 121, 94, 101}, {9, 0, 96, 104, 166, 172}, {9, 1, 111, 119, 141, 149}, {10, 0, 96, 104, 216, 224}, {10, 1, 111, 119, 191, 199}, {11, 0, 96, 104, 265, 273}, {11, 1, 111, 119, 240, 248}, {11, 5, 65, 73, 265, 273}, {12, 0, 140, 148, 89, 97}, {12, 1, 155, 163, 64, 72}, {12, 2, 140, 148, 38, 46}, {13, 0, 140, 148, 139, 147}, {13, 1, 155, 163, 114, 122}, {14, 0, 140, 148, 188, 196}, {14, 1, 155, 163, 168, 176}, {15, 0, 140, 148, 238, 246}, {15, 1, 155, 163, 218, 226}, {16, 0, 184, 192, 117, 125}, {16, 1, 199, 207, 92, 98}, {16, 2, 184, 192, 66, 74}, {17, 0, 184, 192, 166, 174}, {17, 1, 199, 207, 141, 149}, {18, 0, 184, 192, 216, 224}, {18, 1, 199, 207, 191, 199}};
    int[][] roadCoordinates = {{0, 17, 22, 78, 82, 0, 1, 2}, {0, -5, -3, 72, 76, 0, 2, 3}, {0, -27, -22, 83, 88, 0, 3, 4}, {0, 17, 21, 103, 108, 0, 0, 1}, {0, -10, -5, 119, 126, 0, 6, 5}, {0, -26, -22, 105, 110, 0, 4, 5}, {1, -27, -20, 134, 140, 1, 3, 4}, {1, 17, 22, 131, 136, 1, 2, 1}, {1, 18, 23, 152, 158, 1, 0, 1}, {1, -28, -23, 154, 160, 1, 4, 5}, {1, -6, 1, 169, 175, 1, 0, 5}, {3, 76, 82, 64, 72, 3, 0, 1}, {3, 62, 70, 38, 46, 3, 0, 2}, {3, 31, 38, 38, 46, 3, 0, 3}, {2, -26, -20, 182, 190, 2, 3, 4}, {2, -26, -20, 205, 212, 2, 4, 5}, {2, -8, 2, 222, 228, 2, 5, 6}, {2, 17, 25, 178, 185, 2, 2, 1}, {2, 17, 25, 204, 212, 2, 6, 1}, {3, 17, 25, 55, 62, 3, 3, 4}, {3, 38, 44, 92, 97, 3, 5, 0}, {3, 38, 44, 42, 47, 3, 3, 2}, {3, 60, 64, 55, 60, 3, 1, 2}, {3, 62, 67, 82, 87, 3, 1, 0}, {4, 38, 44, 142, 147, 4, 5, 6}, {4, 62, 67, 132, 137, 4, 1, 0}, {4, 62, 67, 105, 110, 4, 2, 1}, {5, 62, 67, 157, 163, 5, 1, 2}, {5, 38, 44, 188, 196, 5, 5, 0}, {5, 64, 70, 178, 184, 5, 0, 1}, {6, 18, 25, 228, 235, 6, 4, 5}, {6, 38, 45, 243, 248, 6, 0, 5}, {6, 61, 67, 206, 211, 6, 1, 2}, {6, 61, 67, 229, 235, 6, 0, 1}, {7, 86, 91, 67, 75, 7, 5, 0}, {7, 64, 69, 30, 38, 7, 3, 4}, {7, 106, 114, 16, 24, 7, 2, 3}, {7, 76, 84, 16, 24, 7, 0, 3}, {7, 108, 113, 55, 60, 7, 0, 1}, {7, 82, 90, 72, 78, 7, 0, 5}, {7, 105, 111, 28, 36, 7, 1, 2}, {7, 84, 90, 21, 26, 7, 2, 3}, {8, 84, 90, 120, 126, 8, 0, 5}, {8, 107, 113, 82, 88, 8, 1, 2}, {8, 107, 113, 107, 113, 8, 0, 1}, {9, 85, 90, 168, 175, 9, 5, 0}, {9, 106, 111, 128, 133, 9, 1, 2}, {9, 106, 111, 155, 160, 9, 0, 1}, {10, 80, 86, 216, 224, 10, 5, 0}, {10, 108, 113, 204, 210, 10, 0, 1}, {10, 108, 113, 180, 186, 10, 1, 2}, {11, 60, 66, 253, 261, 11, 4, 5}, {11, 81, 87, 265, 273, 11, 5, 0}, {11, 108, 114, 255, 260, 11, 0, 1}, {11, 107, 112, 230, 236, 11, 1, 2}, {12, 126, 132, 93, 99, 12, 5, 0}, {12, 153, 159, 80, 86, 12, 0, 1}, {12, 150, 156, 53, 58, 12, 1, 2}, {12, 131, 137, 41, 47, 12, 2, 3}, {13, 125, 131, 141, 147, 13, 0, 5}, {13, 154, 160, 130, 136, 13, 0, 1}, {13, 149, 155, 107, 113, 13, 1, 2}, {14, 128, 134, 193, 200, 14, 0, 5}, {14, 149, 155, 180, 186, 14, 0, 1}, {14, 149, 155, 156, 161, 14, 1, 2}, {15, 129, 135, 242, 248, 15, 0, 5}, {15, 149, 155, 231, 237, 15, 0, 1}, {15, 149, 155, 204, 210, 15, 1, 2}, {15, 173, 179, 192, 198, 15, 2, 3},};
    public Board() {
        buildMode = true;
        hexagons = new ArrayList<>();
        roads = new ArrayList<>();
        cities = new HashMap<>();
        verticalSpacing = Math.sqrt(3) * hexSize;
        horizontalSpacing = 1.5 * hexSize;

        boardPattern = new int[][]{
                {0, 0, 1, 1, 1, 0, 0},
                {0, 1, 1, 1, 1, 0, 0},
                {0, 1, 1, 1, 1, 1, 0},
                {0, 1, 1, 1, 1, 0, 0},
                {0, 0, 1, 1, 1, 0, 0}};
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
    }
    private String getNextResourceType(List<String> resources) {
        return resources.isEmpty() ? "Unknown" : resources.remove(0);
    }

    private int getNextNumber(List<Integer> numbers) {
        return numbers.isEmpty() ? 0 : numbers.remove(0);
    }
    public void addVillage(int index, int vertex) {
        System.out.println("k"+ownerAbstarct);
        for (Village village : villages) {
            if (village.getIndex() == index && village.getVertex() == vertex) {
                System.out.println("This location is already occupied.");
                return;}}
        villages.add(new Village(index, vertex, ownerAbstarct));}

    public void addCity(int index, int vertex) {
        cities.computeIfAbsent(index, k -> new HashSet<>()).add(vertex);
    }

        public static void exportGameState() {
            List<HexagonResource> hexa = hexagons;
            JSONArray hexagonArray = new JSONArray();

            // Iterate through each HexagonResource and add it to the JSON array
            for (HexagonResource hex : hexa) {
                JSONObject hexObject = new JSONObject();
                hexObject.put("x", hex.getX());
                hexObject.put("y", hex.getY());
                hexObject.put("resource", hex.getResourceType());
                hexObject.put("number", hex.getNumber());
                hexObject.put("size", hex.getSize());
                hexObject.put("index", hex.getIndex());

                hexagonArray.put(hexObject);
            }

            // Create the game state JSON object and add the hexagons array to it
            JSONObject gameState = new JSONObject();
            gameState.put("hexagons", hexagonArray);

            // Export the JSON to a file
            try (FileWriter file = new FileWriter("gameState.json")) {
                file.write(gameState.toString(4)); // Pretty print with indentation
                System.out.println("Successfully exported game state to gameState.json");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    public void addRoad(Road road) {
        roads.add(road);
    }

    private void printSortedHexagons() {
        hexagons.stream()
                .sorted(Comparator.comparingInt(HexagonResource::getIndex))
                .forEach(hexagon -> System.out.println("Hexagon " + hexagon.getIndex() + ": Resource " + hexagon.getResourceType()));
    }
}