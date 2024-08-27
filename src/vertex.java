import java.util.HashSet;
import java.util.Set;

public class vertex {
    private int index;
    private boolean village = false;
    private boolean city = false;
    private boolean road = false;
    private Set<Integer> connectedHexagons;

    public vertex(int index) {
        this.index = index;
        this.connectedHexagons = new HashSet<>();
    }
    public int getIndex() {
        return index;
    }

    public boolean hasCity() {
        return city;
    }

    public boolean hasVillage() {
        return village;
    }

    public boolean hasRoad() {
        return road;
    }

    public void setVillage(boolean village) {
        this.village = village;
    }

    public void setCity(boolean city) {
        this.city = city;
    }

    public void setRoad(boolean road) {
        this.road = road;
    }

    public void connectHexagon(int hexIndex) {
        connectedHexagons.add(hexIndex);
    }

    public Set<Integer> getConnectedHexagons() {
        return connectedHexagons;
    }

    public boolean isSharedWithHexagon(int hexIndex) {
        return connectedHexagons.contains(hexIndex);
    }
}
