public class Player {
    private int number;
    private int numberOfVillages;
    private int numberOfCities;
    private boolean playerPlay;
    private int devCardKnight=0;
    private int RoadBuilding=0;
    private int YearPlenty=0;
    private int Monopoly=0;
    private int VictoryPoint=0;
    public Player(int number, int numberOfVillages, int numberOfCities, boolean playerPlay, int devCardKnight,int RoadBuilding, int YearPlenty, int Monopoly, int VictoryPoint) {
        this.number = number;
        this.numberOfVillages = numberOfVillages;
        this.numberOfCities = numberOfCities;
        this.playerPlay = playerPlay;
        this.devCardKnight=devCardKnight;
        this.RoadBuilding=RoadBuilding;
        this.YearPlenty=YearPlenty;
        this.Monopoly=Monopoly;
        this.VictoryPoint=VictoryPoint;
    }

    public int getNumber() {
        return number;
    }

    public int getNumberOfVillages() {
        return numberOfVillages;
    }

    public int getNumberOfCities() {
        return numberOfCities;
    }

    public boolean isPlayerPlay() {
        return playerPlay;
    }

    // Setters
    public void setNumberOfVillages(int numberOfVillages) {
        this.numberOfVillages = numberOfVillages;
    }

    public void setNumberOfCities(int numberOfCities) {
        this.numberOfCities = numberOfCities;
    }

    public void setPlayerPlay(boolean playerPlay) {
        this.playerPlay = playerPlay;
    }
}
