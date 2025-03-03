import java.util.*;
public class Board{
    private List<HexagonResource> hexagons;
    private int indexGener = -1;
    private int[][] boardPattern;
    private devCards devCards;
    public ArrayList<Player> players = new ArrayList<Player>();
    private int sumDice=0;
    public Board(int numberOfPlayers) {
        boardPattern = new int[][]{
                {0, 0, 1, 1, 1, 0, 0},
                {0, 1, 1, 1, 1, 0, 0},
                {0, 1, 1, 1, 1, 1, 0},
                {0, 1, 1, 1, 1, 0, 0},
                {0, 0, 1, 1, 1, 0, 0}};
        hexagons = new ArrayList<>();

        initializeBoard(numberOfPlayers);
    }
    
    private void initializeBoard(int nop) {
        initPlayer(nop);
        devCards=new devCards();
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
        //printSortedHexagons();
        
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
            p1.removeResource(r1);
            p2.addResources(r1);
            p2.removeResource(r2);
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
    private void printSortedHexagons() {
        hexagons.stream()
                .sorted(Comparator.comparingInt(HexagonResource::getIndex))
                .forEach(hexagon -> System.out.println("Hexagon " + hexagon.getIndex() + ": Resource " + hexagon.getResourceType()));
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
        players.get(pi).addResourcebytype(typeResouce);
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
        players.get(playerPlayIntValue - 1).setPlayerPlay(true);
        if (playerPlayIntValue == 1) players.getLast().setPlayerPlay(false);
        else players.get(playerPlayIntValue - 2).setPlayerPlay(false);
    
        String hexagonsPart = parts[0];
        //System.out.println("Hexagons part: " + hexagonsPart);
        String sumDicePart = parts[1].replace("}", "").trim(); // Remove the trailing '}' and extra spaces
        
        // Parse hexagons
        String[] hexagonEntries = hexagonsPart.split("}");
        String[] vertexStrings;
        System.out.println("-----------------------------------------------------------------------------------------------------------------------------------------------");
        //for(String h : hexagonEntries)System.out.println(h);
        Village tempVillage=new Village(-1, -1, -1);
        for (String hexagonEntry : hexagonEntries) {
            if (!hexagonEntry.contains("hexagon")) continue;
    
            // Temporary lists for storing vertex states per hexagon
            List<Boolean> villageStates = new ArrayList<>();
            List<Boolean> cityStates = new ArrayList<>();
            vertexStrings = hexagonEntry.split("vertices:")[1].split(", ");
            //for(String h : vertexStrings)System.out.println(h);
            // Extract hexagon details
            String[] hexagonAttributes = hexagonEntry.split(", ");
            //for(String h : hexagonAttributes)System.out.println(h);
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
            for(int i=0; i<vertexStrings.length; i++){
                //System.out.println("vertexString: "+vertexStrings[i]);
                if(vertexStrings[i].contains("v")){
                    int vi=Integer.parseInt(vertexStrings[i].split("v")[1].split(":")[0]);
                    if(vertexStrings[i].contains("true")) tempVillage=new Village(index, vi, playerPlayIntValue);

                    //System.out.println("village: ih:" + index + " vi:" + vi + " boolean: " + vertexStrings[i].contains("true"));
                    
                }
                if(vertexStrings[i].contains("city")){
                    if(vertexStrings[i].contains("true"))cityStates.add(true);
                    else cityStates.add(false);
                }
            }
            // Recreate the hexagon
            if (resourceType != null) {
                HexagonResource hexagon = new HexagonResource(resourceType, number, index);
    
                // Assign villages and cities to vertices (fixed to only use correct indices)
                for (int i = 0; i < hexagon.getVertices().size(); i++) {
                    if (i < villageStates.size()) {
                        hexagon.getVertices().get(i).setVillage(villageStates.get(i));
                    }
                    if (i < cityStates.size()) {
                        hexagon.getVertices().get(i).setCity(cityStates.get(i));
                    }
                }
                if(hexagon.getIndex()==tempVillage.index) hexagon.vertices.get(tempVillage.getVertex()).setVillage(true); 
                //System.out.println(tempVillage.index+" "+tempVillage.getVertex()+" "+tempVillage.owner);
                hexagons.add(hexagon);
                //if(tempVillage.index!=-1&&tempVillage.getIndex()!=-1&&tempVillage.owner!=-1) hexagons.get(tempVillage.getIndex()).vertices.get(tempVillage.getVertex()).setVillage(true);
                
            }


        }
    
        // Update sumDice
        try {
            sumDice = Integer.parseInt(sumDicePart);
        } catch (NumberFormatException e) {
            System.out.println("Invalid sumDice value");
        }
    }
    
    public void setIps(String[] ips){
        for(int i=0; i<players.size(); i++){
            players.get(i).setIp(ips[i]);
            System.out.println("player "+i+" seted "+players.get(i).getIp());
        }
    }
    public void removedevCards(int i){devCards.removeDevCardByIndex(i);}
    public devCards getDevCards(){return devCards;}
    public ArrayList<Player> getPlayers(){
        return players;
    }
    public void addVillageInHexagon(Village v){hexagons.get(v.getIndex()).vertices.get(v.getVertex()).setVillage(true);}
    public void addCityInHexagon(City c){hexagons.get(c.index).vertices.get(c.vertex).setCity(true);}
    public void addRoad(Road r){hexagons.get(r.getIndexHexagon()).vertices.get(r.getv2()).setRoad(true);
        hexagons.get(r.getIndexHexagon()).vertices.get(r.getv1()).setRoad(true);
        players.get(playerPlayNum()-1).addRoad(r);}
    
    public List<HexagonResource> getHexagonResources(){return hexagons;}
    public void setSumDice(int x){sumDice=x;}
    public void addResourceByPlayer(int ip, String type){players.get(ip).resources.add(new Resource(type));}
    public int getSumDice(){return sumDice;}
    public int playerPlayNum() {
        for (int i = 0; i < players.size(); i++) {if (players.get(i).isPlayerPlay()) {return i+1;}}
        return -1;
    }
}