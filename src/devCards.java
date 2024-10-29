import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class devCards {
    public ArrayList<devCard> cards=new ArrayList<>();
    public devCards(){
        for(int i=0; i<14; i++){this.cards.add(new devCard("knight"));}
        for(int i=0; i<5; i++){this.cards.add(new devCard("victory point"));}
        for(int i=0; i<2; i++){this.cards.add(new devCard("road building"));}
        for(int i=0; i<2; i++){this.cards.add(new devCard("year of plenty"));}
        for(int i=0; i<2; i++){this.cards.add(new devCard("monopoly"));}
        Collections.shuffle(this.cards);
    }
    public void removeDevCard(devCard d){cards.remove(d);}
    public int size(){return cards.size();}
    public devCard randomCard(){return cards.getFirst();}
}
