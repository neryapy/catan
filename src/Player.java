import java.util.ArrayList;

public class Player {
    private int number;
    private int numberOfCities;
    private boolean playerPlay;
    private int devCardKnight = 0;
    private int RoadBuilding = 0;
    private int YearPlenty = 0;
    private int Monopoly = 0;
    private int VictoryPoint = 0;
    private ArrayList<Village> villages;

    public Player(int number) {
        this.number = number;
        this.numberOfCities = 0; // Initialize with 0 or another appropriate value
        this.playerPlay = false; // Initialize with false or another appropriate value
        this.devCardKnight = 0;
        this.RoadBuilding = 0;
        this.YearPlenty = 0;
        this.Monopoly = 0;
        this.VictoryPoint = 0;
        this.villages = new ArrayList<>(); // Initialize the ArrayList
    }

    public void addVillage(Village v) {
        this.villages.add(new Village(v.index, v.vertex, number));
        Board.hexagons.get(v.index).getVertex(v.vertex).setVillage(true);
        System.out.println("index of village "+Board.hexagons.get(v.index).getVertex(v.vertex).getIndex());
        if(Board.hexagons.get(v.index).getVertex(v.vertex).hasVillage()==true){
            System.out.println("true");
        }
    }

    public int getNumber() {
        return number;
    }

    public int getNumberOfVillages() {
        return villages.size();
    }

    public int getNumberOfCities() {
        return numberOfCities;
    }

    public boolean isPlayerPlay() {
        return playerPlay;
    }

    public void setNumberOfCities(int numberOfCities) {
        this.numberOfCities = numberOfCities;
    }

    public void setPlayerPlay(boolean playerPlay) {
        this.playerPlay = playerPlay;
    }
}
