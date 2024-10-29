import java.util.HashSet;
import java.util.Set;
public class vertex {
    private int index;
    private boolean village;
    private boolean city;
    private Set<Integer> connectedHexagons;
    private vertex vertexPoint;
    public vertex(int index) {
        this.index = index;
        this.connectedHexagons = new HashSet<>();
        this.village=false;
        this.city=false;
    }
    public int getIndex() {
        return index;
    }

    public boolean getCity() {
        return city;
    }

    public boolean getVillage() {
        return village;
    }
    public void setVillage(boolean village) {
        this.village = village;
    }

    public void setCity(boolean city) {
        this.city = city;
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
