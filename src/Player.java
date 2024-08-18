import java.util.ArrayList;

public class Player {
    private int number;
    private int numberOfCities;
    private boolean playerPlay;
    private int devCardKnight=0;
    private int RoadBuilding=0;
    private int YearPlenty=0;
    private int Monopoly=0;
    private int VictoryPoint=0;
    private ArrayList<Village> villages;
    public Player(int number, ArrayList<Village> villages , int numberOfCities, boolean playerPlay, int devCardKnight,int RoadBuilding, int YearPlenty, int Monopoly, int VictoryPoint) {
        this.number = number;
        this.numberOfCities = numberOfCities;
        this.playerPlay = playerPlay;
        this.devCardKnight=devCardKnight;
        this.RoadBuilding=RoadBuilding;
        this.YearPlenty=YearPlenty;
        this.Monopoly=Monopoly;
        this.VictoryPoint=VictoryPoint;
        this.villages=villages;
    }

    public ArrayList<Village> getVillages() {
        return villages;
    }
    public void addVillage(Village v){
        this.villages.add(new Village(v.index, v.vertex, number));
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
