import java.lang.reflect.Array;
import java.util.ArrayList;

public class Player {
    private int number;
    private int allPoints=0;
    public int publicPoints=0;
    private boolean playerPlay;
    private ArrayList<devCard> playerDevCards=new ArrayList<>();
    private ArrayList<Resource> Resources =new ArrayList<>();
    private ArrayList<Village> villages;
    private ArrayList<City> citys;
    private ArrayList<Road> roads;
    private ArrayList<Resource> resources=new ArrayList<>();
    public Player(int number) {
        this.number = number;
        this.playerPlay = false; // Initialize with false or another appropriate value
        this.villages = new ArrayList<>(); // Initialize the ArrayList
        this.citys=new ArrayList<>();
        this.roads=new ArrayList<>();
    }
    public void buyDevCard(){
        ArrayList<Resource> requirements=new ArrayList<>(3);
        requirements.add(new Resource("wool"));
        requirements.add(new Resource("ore"));
        requirements.add(new Resource("grain"));
        if(resources.containsAll(requirements)&&Board.devCards.cards.size()>=1){
            resources.removeAll(requirements);
            playerDevCards.add(Board.devCards.cards.getFirst());
            Board.devCards.cards.removeFirst();
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
            if(d.getType()=="knight")useKnight(1);
            if(d.getType()=="year of plenty")useYearPlenty(new Resource("brick"), new Resource("ore"));
            if(d.getType()=="road building")useRoadBuilding(new Road(0,0),new Road(1,1));
        }
    }
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
            for(int i=0; i<Board.players.size(); i++){
                while(Board.players.get(i).getResources().contains(catchRes)){
                    Board.players.get(i).getResources().remove(catchRes);
                    resourcesCatch++;
                }for(int d=0; d<resourcesCatch; d++){Board.players.get(number).addResource(catchRes);}
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
    public void addResource(Resource resource){Resources.add(resource);}
    public void addResources(ArrayList<Resource> r){
        for(int i=0; i<r.size(); i++){
            resources.add(r.get(i));}}
    public ArrayList<Resource> getResources() {return Resources;}
    public void removeResource(ArrayList<Resource> r){Resources.removeAll(r);}
    public void addVillage(Village v) {
        ArrayList<Resource> villagResouces=new ArrayList();
        villagResouces.add(new Resource("grain"));
        villagResouces.add(new Resource("lumber"));
        villagResouces.add(new Resource("brick"));
        villagResouces.add(new Resource("wool"));
        if(resources.containsAll(villagResouces)){
            this.villages.add(new Village(v.index, v.vertex, number));
            Board.hexagons.get(v.index).getVertex(v.vertex).setVillage(true);}
        else{
            System.out.println("you dont have the resouces"+resources.size());
            for(int i=0; i<resources.size(); i++){
                System.out.println(resources.get(i).getType());
            }
        }
    }

    public void addRoad(Road r) {
        this.roads.add(new Road(r.getIndexLine(), number));
        Board.hexagons.get(r.getIndexLine()).getLines().get(r.getIndexLine()).setRoad(true);
        if(Board.hexagons.get(r.getIndexLine()).getLines().get(r.getIndexLine()).hasRoad()==true){
            System.out.println("true");
        }
    }
    public void useRobber(int indexHexagon){Board.hexagons.get(indexHexagon).setHasRobber(true);}
    public void addCity(City city) {
        if(Board.hexagons.get(city.index).getVertex(city.vertex).getVillage()==true&&this.villages.get(city.index).owner==number) {
            this.citys.add(new City(city.index, city.vertex, number));
            Board.hexagons.get(city.index).getVertex(city.vertex).setCity(true);
            System.out.println("index of village " + Board.hexagons.get(city.index).getVertex(city.vertex).getIndex());
        }
        else{
            System.out.println("you cannot add city because have no village");
        }
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
}