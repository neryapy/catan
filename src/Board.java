import com.sun.net.httpserver.Headers;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
public class Board{
    private List<HexagonResource> hexagons =new ArrayList<>();
    private int indexGener = -1;
    private int[][] boardPattern;
    public ArrayList<Player> players = new ArrayList<Player>();
    private int sumDice=0;

    public List<devCard> pileDevCard=new ArrayList<>();
    private int[][] ConnectedHexagons=new int[][]{{1,3,4},{0,2,4,5},{5,6},{0,4,7,8},{0,1,3,5,8,9},{1,2,4,6,9,10},{2,5,10,11},{3,8,12},{3,4,7,9,12,13},{4,5,8,10,12,13},{5,6,9,11,14,15},{6,10,15},{7,8,13,16},{8,9,12,14,14,17},{9,10,13,15,17,18},{10,11,14,18},{12,13,17},{13,14,16,18},{14,15,17}
    };
    private int[][] ConnectedVertices=new int[][]{//(vertex of one, hexindexconnected, vertex of the connection)
        {0,1,2, 0,4,4, 1,3,5, 1,4,3, 2,3,4, 5,1,4},{0,2,2, 0,5,4, 1,4,5 ,1,5,3, 2,0,0, 2,4,4, 3,0,5, 5,2,3},{0,6,4, 1,5,5, 1,6,3, 2,1,0, 2,5,4, 3,1,5},{0,4,2, 0,8,4, 1,7,5, 1,8,3, 2,7,4, 4,0,2, 5,0,1, 5,4,3},{0,5,2, 0,9,4, 1,8,5, 1,9,3, 2,3,0, 2,8,4, 3,0,1, 3,3,5, 4,0,0, 4,1,2, 5,1,1, 5,5,3},{0,6,2, 0,10,4, 1,9,5, 1,10,3, 2,4,0, 2,8,4, 3,1,1, 3,4,5, 4,1,0, 4,2,2, 5,2,1, 5,6,3},{0,11,4, 1,10,5, 1,11,3, 2,5,0, 2,10,4, 3,2,1, 3,5,5, 4,2,0},{0,8,2, 0,12,4, 1,12,3, 4,3,2, 5,3,1, 5,8,3},{0,9,2, 0,13,4, 1,12,5, 1,13,3, 2,7,0, 2,12,4, 3,3,1, 3,7,5, 4,3,0, 4,4,2, 5,4,1, 5,9,3},{0,10,2, 0,14,4, 1,13,5, 1,14,3, 2,8,0, 2,13,4, 3,4,1, 3,8,5, 4,4,0, 4,5,2, 5,5,1, 5,10,3},{0,11,2, 0,15,4, 1,14,5, 1,15,3, 2,9,0, 2,14,4, 3,5,1, 3,9,5, 4,5,0, 4,6,2, 5,6,1, 5,11,3},{1,15,5, 2,10,0, 2,15,4, 3,6,1, 3,10,5, 4,6,1},{0,13,2, 0,16,4, 1,16,3, 3,7,1, 4,7,0, 4,8,1, 5,8,1, 5,13,3},{0,14,1, 0,17,4, 1,16,5, 1,17,3, 2,12,0, 2,16,4, 3,8,1, 3,12,5, 4,8,0, 4,9,2, 5,9,1, 5,14,3},{0,15,2, 0,18,4, 1,17,5, 1,18,3, 2,13,0, 2,17,4, 3,9,1, 3,13,5, 4,9,0, 4,10,2, 5,10,1, 5,15,3},{1,18,5, 2,14,0, 2,18,4, 3,10,1, 3,14,5, 4,10,0, 4,11,1, 5,11,1},{0,17,2, 3,12,1, 4,12,0, 4,13,2, 5,13,1, 5,17,3},{0,18,2, 2,16,0, 3,13,1, 3,16,5, 4,13,0, 4,14,2, 5,14,1, 5,18,3},{2,17,0, 3,14,1, 3,17,5, 4,14,0, 4,15,2, 5,15,1}
    };
    public Board(int numberOfPlayers) {
        boardPattern = new int[][]{
                {0, 0, 1, 1, 1, 0, 0},
                {0, 1, 1, 1, 1, 0, 0},
                {0, 1, 1, 1, 1, 1, 0},
                {0, 1, 1, 1, 1, 0, 0},
                {0, 0, 1, 1, 1, 0, 0}};

        initializeBoard(numberOfPlayers);
    }
    
    private void initializeBoard(int nop) {
        initPlayer(nop);
        for(int i=0; i<14; i++){pileDevCard.add(new devCard("knight"));}
        for(int i=0; i<5; i++){pileDevCard.add(new devCard("victory point"));}
        for(int i=0; i<2; i++){pileDevCard.add(new devCard("road building"));}
        for(int i=0; i<2; i++){pileDevCard.add(new devCard("year of plenty"));}
        for(int i=0; i<2; i++){pileDevCard.add(new devCard("monopoly"));}
        Collections.shuffle(pileDevCard);
        Map<String, Integer> resourceCounts = new HashMap<>();
        resourceCounts.put("lumber", 4);
        resourceCounts.put("wool", 4);
        resourceCounts.put("Ore", 3);
        resourceCounts.put("grain", 4);
        resourceCounts.put("Brick", 3);
        resourceCounts.put("Desert", 1);
        List<String> resources = new ArrayList<>();
        for (Map.Entry<String, Integer> entry : resourceCounts.entrySet()) {for (int i = 0; i < entry.getValue(); i++) {resources.add(entry.getKey());}}
        List<Integer> numbers = new ArrayList<>(List.of(2, 3, 3, 4, 4, 5, 5, 6, 6, 8, 8, 9, 9, 10, 10, 11, 11, 12));
        Collections.shuffle(numbers);
        Collections.shuffle(resources);
        for (int row = 0; row < boardPattern.length; row++) {for (int col = 0; col < boardPattern[row].length; col++) {if (boardPattern[row][col] == 0) continue;
                String resourceType = getNextResourceType(resources);
                int number = (resourceType.equals("Desert")) ? 0 : getNextNumber(numbers);
                indexGener++;
                
                HexagonResource newHex = new HexagonResource(resourceType, number, indexGener);
                hexagons.add(newHex);
        }}

        for(int hexindex=0; hexindex<ConnectedVertices.length; hexindex++){
            for(int i=0; i<ConnectedVertices[hexindex].length; i++){

            }
        }
        
        // Process the list to group every 3 elements
        List<List<int[]>> groupedVertices = new ArrayList<>();

        for (int[] row : ConnectedVertices) {
            List<int[]> tripletList = new ArrayList<>();
            for (int i = 0; i < row.length; i += 3) {
                if (i + 2 < row.length) {
                    int[] triplet = {row[i], row[i + 1], row[i + 2]};
                    tripletList.add(triplet);
                }
            }
            groupedVertices.add(tripletList);
        }
        
        for (int i = 0; i < groupedVertices.size(); i++) {
            for (int[] triplet : groupedVertices.get(i)) {
                hexagons.get(triplet[1]).getVertices().get(triplet[2]).getCoonectedVertices().add(triplet[0]);
                hexagons.get(triplet[1]).getVertices().get(triplet[2]).getCoonectedHexagons().add(i);
            }
        }

        ArrayList<Resource> r=new ArrayList<>();
        r.add(new Resource("grain"));
        r.add(new Resource("wool"));
        r.add(new Resource("lumber"));
        r.add(new Resource("brick"));
        r.add(new Resource("ore"));
        for(int i=0; i<10; i++) players.get(0).addResources(r);
        for(int i=0; i<hexagons.size(); i++){if(hexagons.get(i).getResourceType().equals("Desert")){useRobber(i);}}
        startGame();
    }
    private String getNextResourceType(List<String> resources) {
        return resources.isEmpty() ? "Unknown" : resources.remove(0);
    }
    private void startGame() {
        players.get(0).setPlayerPlay(true);
        for(int i=0; i<players.size(); i++)System.out.println("player "+i+" play "+players.get(i).isPlayerPlay());
    }
    public List<HexagonResource> getHexagons(){return hexagons;}
    public void exchangeResource(Player p1, Player p2, ArrayList<Resource> r1, ArrayList<Resource> r2){if(p1.getResources().containsAll(r1)&&p2.getResources().containsAll(r2)){
            p1.addResources(r2);
            p1.removeResources(r1);
            p2.addResources(r1);
            p2.removeResources(r2);
        }
        else{
            System.out.println("you canot exchange");
        }
    }
    private void initPlayer(int number){
        for(int i=0; i<number; i++){
            players.add(new Player(i, this));
        }
    }
    private int getNextNumber(List<Integer> numbers) {
        return numbers.isEmpty() ? 0 : numbers.remove(0);
    }
    public ArrayList<Player> getplayers(){return players;}
    public void setPlayersPlay(int numberOfPlayer, boolean play){
        if(numberOfPlayer==players.size()){
            players.get(0).setPlayerPlay(true);
            players.get(players.size()).setPlayerPlay(false);
        }
        players.get(numberOfPlayer).setPlayerPlay(play);
    }
    public void addResourceByplayer(int pi, String typeResouce){
        players.get(pi).addResource(new Resource(typeResouce));
    }
    public Resource useRobber(int hex){
        System.out.println(hexagons.get(hex).HasRobber());
        if(!hexagons.get(hex).HasRobber()){
            for(int i=0; i<hexagons.size(); i++){if(hexagons.get(i).HasRobber()) {hexagons.get(i).setRobber(false);}}
            hexagons.get(hex).setRobber(true);
            for(int i=0; i<players.size(); i++){
                if(i!=playerPlayNum()){
                    for(int k=0; k<hexagons.get(hex).getVertices().size(); k++) if(players.get(i).getVillages().contains(new Village(hex, k, i))){
                        int resSelected=ThreadLocalRandom.current().nextInt(0, players.get(i).getResources().size()+1);
                        Resource temp=players.get(i).getResources().get(resSelected);
                        players.get(i).getResources().remove(resSelected);
                        return temp;
                    }
            }
            }
        }
        for(int i=0; i<hexagons.size(); i++){if(hexagons.get(i).HasRobber()) {System.out.println("hexagon "+i+"have robber");}}
        return new Resource("null");
    }
    
    public String exportGameState(){
        String hexagonsState="{";
        String playerState="{";
        
        for (int i = 0; i < hexagons.size(); i++) {
            hexagonsState += "hexagon:"+i+", "+"number:"+hexagons.get(i).getNumber()+", "+"Resource:"+hexagons.get(i).getResourceType();
            
            // Store village & city states properly
            hexagonsState += ", vertices:";
            for(int k=0; k<hexagons.get(i).getVertices().size(); k++) {
                hexagonsState += "v"+k+":(village:"+hexagons.get(i).vertices.get(k).getVillage()+"|city:"+hexagons.get(i).vertices.get(k).getCity()+"), ";
            }
            hexagonsState = hexagonsState.substring(0, hexagonsState.length() - 1); // Remove last comma
            hexagonsState += "}"; 
            
            hexagonsState += "}";
        }
    
        for(int i=0; i<players.size(); i++){
            playerState += "player:"+i+", allpoints:"+players.get(i).getAllpoints()+", devcards:"+players.get(i).devcradString()+", villages:"+players.get(i).villagesString()+", cities:"+players.get(i).CitiesString()+", roads:"+players.get(i).roadsString()+", resources:"+ players.get(i).resourcesString();
        }
        playerState += "player play:"+playerPlayNum()+", sumDice:"+sumDice+"}";
        
        return hexagonsState + playerState;
    }
    public void extractRoads(String gameState) {
    // Find the roads section using regex
    Pattern roadPattern = Pattern.compile("roads:\\[(.*?)\\]");
    Matcher roadMatcher = roadPattern.matcher(gameState);

    System.out.println("Parsed Roads:");
    while (roadMatcher.find()) {
        String roadData = roadMatcher.group(1); // Get content inside []
        String[] roadEntries = roadData.split("\\], \\["); // Split multiple roads if present

        for (String roadEntry : roadEntries) {
            String[] roadValues = roadEntry.split(",\\s*"); // Split by comma with optional spaces
            if (roadValues.length == 3) {
                try {
                    int hexIndex = Integer.parseInt(roadValues[0].trim());
                    int vertex1 = Integer.parseInt(roadValues[1].trim());
                    int vertex2 = Integer.parseInt(roadValues[2].trim());
                    addRoad(new Road(hexIndex, vertex1, vertex2));
                    System.out.println("Hexagon: " + hexIndex + " | Vertices: " + vertex1 + " - " + vertex2);
                } catch (NumberFormatException e) {
                    System.out.println("Invalid road format: " + roadEntry);
                }
            }
        }
    }
}

    public void updateGameState(String gameState) {
        // Clear the current board state
        hexagons.clear();
    
        // Parse the gameState string
        String[] parts = gameState.split("sumDice:");
        if (parts.length < 2) {
            System.out.println("Invalid game state format");
            return;
        }
    
        // Extract player play index
        int startIndex = gameState.indexOf("player play:") + "player play:".length();
        String playerPlayValue = gameState.substring(startIndex).trim();
        int commaIndex = playerPlayValue.indexOf(",");
        String playerPlayValueWithoutComma = playerPlayValue.substring(0, commaIndex).trim();
        int playerPlayIntValue = Integer.parseInt(playerPlayValueWithoutComma);
        players.get(playerPlayIntValue).setPlayerPlay(true);
        if (playerPlayIntValue == 1) players.getLast().setPlayerPlay(false);
        else players.get(playerPlayIntValue +1).setPlayerPlay(false);
    
        String hexagonsPart = parts[0];
        String sumDicePart = parts[1].replace("}", "").trim(); // Remove trailing '}' and extra spaces
    
        // Parse hexagons
        String[] hexagonEntries = hexagonsPart.split("}");
        String[] vertexStrings;
    
        for (String hexagonEntry : hexagonEntries) {
            if (!hexagonEntry.contains("hexagon")) continue;
    
            // Temporary storage for vertices
            HashMap<Integer, Boolean> villageMap = new HashMap<>();
            HashMap<Integer, Boolean> cityMap = new HashMap<>();
    
            vertexStrings = hexagonEntry.split("vertices:")[1].split(", ");
    
            // Extract hexagon details
            String[] hexagonAttributes = hexagonEntry.split(", ");
            String resourceType = null;
            int number = 0, index = 0;
    
            for (String attribute : hexagonAttributes) {
                if (attribute.startsWith("hexagon:")) {
                    index = Integer.parseInt(attribute.split(":")[1]);
                } else if (attribute.startsWith("Resource:")) {
                    resourceType = attribute.split(":")[1];
                } else if (attribute.startsWith("number:")) {
                    number = Integer.parseInt(attribute.split(":")[1]);
                }
            }
    
            for (String vertexString : vertexStrings) {
                if (vertexString.contains("v")) {
                    int vi = Integer.parseInt(vertexString.split("v")[1].split(":")[0]);
    
                    // If a village exists at this vertex
                    if (vertexString.contains("village:true")) {
                        villageMap.put(vi, true);
                    }
    
                    // If a city exists at this vertex
                    if (vertexString.contains("city:true")) {
                        cityMap.put(vi, true);
                    }
                }
            }
    
            // Recreate the hexagon
            if (resourceType != null) {
                HexagonResource hexagon = new HexagonResource(resourceType, number, index);
    
                // Assign villages and cities to vertices
                for (int i = 0; i < hexagon.getVertices().size(); i++) {
                    if (villageMap.containsKey(i)) {
                        hexagon.getVertices().get(i).setVillage(true);
                    }
                    if (cityMap.containsKey(i)) {
                        players.get(playerPlayIntValue).addCity(new City(hexagon.getIndex(), i, playerPlayIntValue));
                        hexagon.getVertices().get(i).setVillage(false);
                        hexagon.getVertices().get(i).setCity(true);
                    }
                }
    
                hexagons.add(hexagon);
            } // Extract roads from the hexagon entry
        }
    
        // Update sumDice
        try {
            sumDice = Integer.parseInt(sumDicePart);
        } catch (NumberFormatException e) {
            System.out.println("Invalid sumDice value");
        }
        
        extractRoads(gameState);
    }
    
    public void setIps(String[] ips){
        for(int i=0; i<players.size(); i++){
            players.get(i).setIp(ips[i]);
            System.out.println("player "+i+" seted "+players.get(i).getIp());
        }
    }
    public void buyDevCard(int playerIndex){
        players.get(playerIndex).buyDevCard();
    }
    public List<devCard> getPileDevCard(){return pileDevCard;}
    public ArrayList<Player> getPlayers(){
        return players;
    }
    public void addVillageInHexagon(Village v){
        for(int i=0; i<hexagons.get(v.index).vertices.get(v.vertex).getCoonectedHexagons().size(); i++){
            hexagons.get(hexagons.get(v.index).vertices.get(v.vertex).getCoonectedHexagons().get(i)).vertices.get(hexagons.get(v.index).vertices.get(v.vertex).getCoonectedVertices().get(i)).setVillage(true);
        }
        hexagons.get(v.getIndex()).vertices.get(v.getVertex()).setVillage(true);
        //for(int i=0; i<hexagons.size(); i++){for(int k=0; k<hexagons.get(i).vertices.size(); k++){if(hexagons.get(i).getVertices().get(k).getVillage()){System.out.println("village: hex: "+i+" vertex: "+k);}}}
    }
    public void addRoad(Road r){hexagons.get(r.getIndexHexagon()).vertices.get(r.getv2()).setRoad(true);
        hexagons.get(r.getIndexHexagon()).vertices.get(r.getv1()).setRoad(true);
        players.get(playerPlayNum()).addRoad(r);}
    public void addCity(City c){players.get(c.getOwner()).addCity(c);
        hexagons.get(c.index).vertices.get(c.vertex).setCity(true);
        hexagons.get(c.index).vertices.get(c.vertex).setVillage(false);
    }
    public void setSumDice(int x){sumDice=x;}
    public void addResourceByPlayer(int ip, String type){players.get(ip).resources.add(new Resource(type));}
    public int getSumDice(){return sumDice;}
    public int playerPlayNum() {
        for (int i = 0; i < players.size(); i++) {if (players.get(i).isPlayerPlay()) {return i;}}
        return -1;
    }
    public void addResourceByDice(int diceSum){
        for(HexagonResource achex:hexagons){
            for(vertex v:achex.getVertices()){
                if(!achex.HasRobber()){
                    if(v.getVillage()&&achex.getNumber()==diceSum) players.get(playerPlayNum()).addResource(new Resource(achex.getResourceType()));
                    else if(v.getCity()){ 
                        players.get(playerPlayNum()).addResource(new Resource(achex.getResourceType()));
                        players.get(playerPlayNum()).addResource(new Resource(achex.getResourceType()));
                    }
            }
            }
        }
    }
    public Player getPlayerPlay(){return players.get(playerPlayNum());}
}