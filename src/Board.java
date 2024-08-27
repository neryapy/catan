import org.json.JSONObject;
import org.json.JSONArray;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.*;
public class Board {
    public static List<HexagonResource> hexagons;
    private List<Road> roads;
    static ArrayList<Village> villages = new ArrayList<Village>();
    private static Map<Integer, Set<Integer>> cities;
    private final int hexSize = 30;
    private int indexGener = 0;
    public static int mode; // Default mode
    public static int ownerAbstarct;
    private int[][] boardPattern;

    public Board() {
        boardPattern = new int[][]{
                {0, 0, 1, 1, 1, 0, 0},
                {0, 1, 1, 1, 1, 0, 0},
                {0, 1, 1, 1, 1, 1, 0},
                {0, 1, 1, 1, 1, 0, 0},
                {0, 0, 1, 1, 1, 0, 0}};
        hexagons = new ArrayList<>();
        roads = new ArrayList<>();
        cities = new HashMap<>();

        initializeBoard();
    }

    private void initializeBoard() {
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
                String resourceType = getNextResourceType(resources);
                int number = (resourceType.equals("Desert")) ? 0 : getNextNumber(numbers);
                indexGener++;
                HexagonResource newHex = new HexagonResource(resourceType, number, indexGener);
                hexagons.add(newHex);
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
        System.out.println("k" + ownerAbstarct);
        for (Village village : villages) {
            if (village.getIndex() == index && village.getVertex() == vertex) {
                System.out.println("This location is already occupied.");
                return;
            }
        }
        villages.add(new Village(index, vertex, ownerAbstarct));
    }

    public void addCity(int index, int vertex) {
        cities.computeIfAbsent(index, k -> new HashSet<>()).add(vertex);
    }

    public static void exportGameState() {
        List<HexagonResource> hexa = hexagons;
        JSONArray hexagonArray = new JSONArray();

        for (HexagonResource hex : hexa) {
            JSONObject hexObject = new JSONObject();
            hexObject.put("resource", hex.getResourceType());
            hexObject.put("number", hex.getNumber());
            hexObject.put("index", hex.getIndex());

            hexagonArray.put(hexObject);
        }

        JSONObject gameState = new JSONObject();
        gameState.put("hexagons", hexagonArray);

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
