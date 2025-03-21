import java.util.ArrayList;
public class Player {
    private int number;
    private int allPoints=0;
    public int publicPoints=0;
    public boolean playerPlay;
    public ArrayList<devCard> playerDevCards=new ArrayList<>();
    public ArrayList<Village> villages;
    public ArrayList<City> Cities;
    public ArrayList<Road> roads;
    public ArrayList<Resource> resources=new ArrayList<>();
    private String ip;
    private Board Board;
    private boolean BuildVillage=false;
    private boolean BuildCity=false;
    private boolean BuildRoad=false;
    private boolean diceturned=false;
    public Player(int number, Board h) {
        this.Board=h;
        this.number = number;
        this.playerPlay = false; // Initialize with false or another appropriate value
        this.villages = new ArrayList<>(); // Initialize the ArrayList
        this.Cities=new ArrayList<>();
        this.roads=new ArrayList<>();
    }
    public void setAllPoints(int i){
        allPoints=i;
    }
    public void setBuildVillage(boolean b){
        BuildVillage=b;
    }
    public void setBuildCity(boolean b){
        BuildCity=b;
    }
    public void setBuildRoad(boolean b){
        BuildRoad=b;
    }
    public boolean getBuildVillage(){
        return BuildVillage;
    }
    public boolean getBuildCity(){
        return BuildCity;
    }
    public boolean getBuildRoad(){
        return BuildRoad;
    }
    public boolean getDiceTurned(){
        return diceturned;
    }
    public void setDiceTurned(boolean b){
        diceturned=b;
    }
    public ArrayList<Village> getVillages() {
        return villages;
    }
    public void buyDevCard(){

        ArrayList<Resource> requirements=new ArrayList<>();
        requirements.add(new Resource("wool"));
        requirements.add(new Resource("ore"));
        requirements.add(new Resource("grain"));
        if(resources.containsAll(requirements) && Board.getDevCards().cards.size()>=1){
            resources.removeAll(requirements);
            playerDevCards.add(Board.getDevCards().cards.getFirst());
            Board.removedevCards(0);
            System.out.println("you have bought dev card: "+playerDevCards.getLast().getType());}
        else{
            if(!resources.containsAll(requirements)){
                System.out.println("you haven't the require resource for buy dev card");
                
            }
            else{
                System.out.println("the pil of dev cards are empty");
            }

        }
    }
    public void useDevCard(devCard d){
        if(playerDevCards.contains(d)){
            if(d.getType()=="knight") useKnight(1);
            if(d.getType()=="year of plenty" )useYearPlenty(new Resource("brick"), new Resource("ore"));
            if(d.getType()=="road building") useRoadBuilding(new Road(0,0,0),new Road(1,1,0));
        }
    }
    public String getIp(){return ip;}
    public void setIp(String ip){this.ip=ip;}
    private void useRoadBuilding(Road r1, Road r2){
        if(playerDevCards.contains(new devCard("road building"))&&false==playerDevCards.get(playerDevCards.indexOf(new devCard("road building"))).getUsed()){
            addRoad(r1);
            addRoad(r2);
            playerDevCards.get(playerDevCards.indexOf(new devCard("road building"))).setUsed();
        }
    }
    private void useMonopoly(Resource catchRes){
        if(playerDevCards.contains(new devCard("monopoly"))&&false==playerDevCards.get(playerDevCards.indexOf(new devCard("monopoly"))).getUsed()){
            /*change the init of player from main to board and catch the resource from board*/
            int resourcesCatch=0;
            for(int i=0; i<Board.getPlayers().size(); i++){
                while(Board.getPlayers().get(i).getResources().contains(catchRes)){
                    Board.getPlayers().get(i).getResources().remove(catchRes);
                    resourcesCatch++;
                }for(int d=0; d<resourcesCatch; d++){Board.getPlayers().get(number).addResource(catchRes);}
            }
        }
    }
    private void useYearPlenty(Resource r1, Resource r2){
        if(playerDevCards.contains(new devCard("year of plenty"))&&false==playerDevCards.get(playerDevCards.indexOf(new devCard("year of plenty"))).getUsed()){
            resources.add(r1);
            resources.add(r2);
            playerDevCards.get(playerDevCards.indexOf(new devCard("year of plenty"))).setUsed();
        }
    }
    private void useVictoryPoint(){
        if(playerDevCards.contains(new devCard("victory point"))&&false==playerDevCards.get(playerDevCards.indexOf(new devCard("victory point"))).getUsed()){
            allPoints++;
            playerDevCards.get(playerDevCards.indexOf(new devCard("victory point"))).setUsed();
        }
    }
    private void useKnight(int i){
        if(playerDevCards.contains(new devCard("knight"))&&false==playerDevCards.get(playerDevCards.indexOf(new devCard("knight"))).getUsed()){
            useRobber(i);
            playerDevCards.get(playerDevCards.indexOf(new devCard("knight"))).setUsed();
            System.out.println("you have used a knight");
        }
    }
    public void addResource(Resource resource){
        resources.add(resource);
        System.out.println(resources.size());}
    public void addResources(ArrayList<Resource> r){
        for(int i=0; i<r.size(); i++){
            resources.add(r.get(i));}}
    public ArrayList<Resource> getResources() {return resources;}
    public void removeResource(ArrayList<Resource> r){resources.removeAll(r);}
    public void addVillage(Village v) {
        villages.add(v);
    }
    public ArrayList<Road> getRoads() {
        return roads;
    }
    public void addRoad(Road r) {
        this.roads.add(new Road(r.getIndexHexagon(), r.getv1(), r.getv2()));

        if(Board.getHexagons().get(r.getIndexHexagon()).getVertices().get(r.getv1()).getRoad()==true&&Board.getHexagons().get(r.getIndexHexagon()).getVertices().get(r.getv2()).getRoad()==true){
            System.out.println("true");
        }
    }
    public void useRobber(int indexHexagon){Board.getHexagons().get(indexHexagon).setHasRobber(true);}
    public void addCity(City city) {
            Cities.add(new City(city.index, city.vertex, number));
            for(int i=0; i<villages.size(); i++){
                if(villages.get(i).index==city.index&&villages.get(i).vertex==city.vertex){
                    villages.remove(i);
                }
            }
            allPoints+=2;
    }
    public int getNumber() {
        return number;
    }

    public int getNumberOfVillages() {
        return villages.size();
    }


    public boolean isPlayerPlay() {
        return playerPlay;
    }
    public void setPlayerPlay(boolean playerPlay) {
        this.playerPlay = playerPlay;
    }
    public void Resourceshastype(){
        System.out.println(resources.size());
        for(int i=0; i<resources.size(); i++){
            System.out.println(resources.size()+" "+resources.get(i).getType());
        }
        
    }
    public void addResourcebytype(String type){
        resources.add(new Resource(type));
    }
    public int getAllpoints(){
        return allPoints;
    }
    public String devcradString(){
        String ret="";
        for(int i=0; i<playerDevCards.size(); i++){
            ret+=playerDevCards.get(i).getType();
        }
        return ret;
    }
    public String villagesString(){
        String ret="";
        for(int i=0; i<villages.size(); i++){
            ret+=villages.get(i).index+", vindex"+villages.get(i).vertex+", ";
        }
        return ret;
    }
    public String CitiesString(){
        String ret="";
        for(int i=0; i<Cities.size(); i++){
            ret+=Cities.get(i).index+", vindex"+Cities.get(i).vertex+",";
        }
        return ret;
    }
    public String roadsString(){
        String ret="";
        for(int i=0; i<roads.size(); i++){
            ret+="["+roads.get(i).getIndexHexagon()+", ";
            ret+=roads.get(i).getv1()+", ";
            ret+=roads.get(i).getv2()+"]";
        }
        return ret;
    }
    public String resourcesString(){
        String ret="";
        for(int i=0; i<resources.size(); i++){
            ret+=resources.get(i).getType()+", ";
        }
        return ret;
    }
}