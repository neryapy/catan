import java.util.ArrayList;
import java.util.List;
public class Player {
    private int number;
    private int allPoints=0;
    public int publicPoints=0;
    public boolean playerPlay;
    public List<devCard> playerDevCards;
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
        this.playerDevCards = new ArrayList<>();
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
    public List<devCard> getPlayerDevCards(){return playerDevCards;}
    public void buyDevCard(){

        ArrayList<Resource> requirements=new ArrayList<>();
        requirements.add(new Resource("wool"));
        requirements.add(new Resource("ore"));
        requirements.add(new Resource("grain"));
        if(resources.containsAll(requirements) && Board.getPileDevCard().size()>=1){
            for(Resource r:requirements)resources.remove(r);
            playerDevCards.add(Board.getPileDevCard().getFirst());
            Board.getPileDevCard().remove(0);
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
            if(d.getType()=="knight") useKnight(d.gethexRobber());
            if(d.getType()=="year of plenty" )useYearPlenty(d.getPlentyResources()[0], d.getPlentyResources()[1]);
            if(d.getType()=="monopoly") useMonopoly(d.getResSteal());
            if(d.getType()=="victory point") useVictoryPoint();
        }
    }
    private void useMonopoly(Resource catchRes){
        if(playerDevCards.contains(new devCard("monopoly"))&&false==playerDevCards.get(playerDevCards.indexOf(new devCard("monopoly"))).getUsed()){
            for(int i=0; i<Board.getPlayers().size(); i++){
                if(Board.getPlayers().get(i).getResources().contains(catchRes)){
                    addResource(catchRes);
                    Board.getPlayers().get(i).getResources().remove(catchRes);
                }
            }
            for(int i=0; i<playerDevCards.size(); i++){
                if(playerDevCards.get(i).getType()=="monopoly"&&playerDevCards.get(i).getUsed()==false){
                    playerDevCards.get(i).setUsed();
                    return;
                }}
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
        for(int k=0; k<playerDevCards.size(); k++){
            if(playerDevCards.get(k).getType()=="victory point"&&playerDevCards.get(k).getUsed()==false){
            playerDevCards.get(playerDevCards.indexOf(new devCard("victory point"))).setUsed();
            allPoints++;
            System.out.println("real point: "+allPoints);
            return;
        }
    }
    }
    private void useKnight(int i){
        for(int k=0; k<playerDevCards.size(); k++){
            if(playerDevCards.get(k).getType()=="knight"&&playerDevCards.get(k).getUsed()==false){
            Board.useRobber(i);
            playerDevCards.get(playerDevCards.indexOf(new devCard("knight"))).setUsed();
            System.out.println("you have used a knight in hexagon: "+i);
            return;
        }
    }
    }
    
    public String getIp(){return ip;}
    public void setIp(String ip){this.ip=ip;}
    public void addResource(Resource resource){
        resources.add(resource);
        System.out.println(resources.size());}
    public void addResources(ArrayList<Resource> r){
        for(int i=0; i<r.size(); i++){
            resources.add(r.get(i));}}
    public ArrayList<Resource> getResources() {return resources;}
    public void removeResources(ArrayList<Resource> r){resources.removeAll(r);}
    public void addVillage(Village v) {
        villages.add(v);
    }
    public ArrayList<Road> getRoads() {
        return roads;
    }
    public void useRobber(int i){
        Board.useRobber(i);
    }
    public void addRoad(Road r) {
        this.roads.add(new Road(r.getIndexHexagon(), r.getv1(), r.getv2()));

        if(Board.getHexagons().get(r.getIndexHexagon()).getVertices().get(r.getv1()).getRoad()==true&&Board.getHexagons().get(r.getIndexHexagon()).getVertices().get(r.getv2()).getRoad()==true){
            System.out.println("true");
        }
    }
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