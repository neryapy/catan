import java.util.Objects;
public class devCard {
    private String type;
    private boolean used=false;
    public devCard(String type) {this.type = type;}
    public String getType() {return type;}@Override
    public boolean equals(Object o) {if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        devCard devCard = (devCard) o;
        return Objects.equals(type, devCard.type);}@Override
    public int hashCode() {return Objects.hash(type);}
    public void setUsed(){used=true;}
    public boolean getUsed() {
        return used;
    }
}