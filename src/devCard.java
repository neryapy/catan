import java.util.Objects;
public class devCard {
    private String type;
    private boolean used=false;
    private int hexRobber=-1;
    private Resource ResSteal;
    private Resource[] plentyResources = new Resource[2];
    public devCard(String type) {this.type = type;}
    public String getType() {return type;}@Override

        public int hashCode() {return Objects.hash(type);}
    public void setUsed(){used=true;}
    public int gethexRobber(){return hexRobber;}
    public Resource getResSteal(){return ResSteal;}
    public void setResSteal(Resource res){ResSteal=res;}
    public Resource[] getPlentyResources(){return plentyResources;}
    public void setPlentyResources(Resource res1, Resource res2){
        plentyResources[0]=res1;
        plentyResources[1]=res2;
    }
    public void setKnight(int hex){
        hexRobber=hex;
    }
    public boolean getUsed() {
        return used;
    }
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;  // Same reference
        if (obj == null || getClass() != obj.getClass()) return false;  // Null or wrong class

        devCard card = (devCard) obj;
        return this.type.equals(card.type) && this.used == card.used;
    }

}