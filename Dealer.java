import javax.swing.JFrame;
public class Dealer extends Person{
    public Dealer(JFrame jF){
        super(490, 30, 475, 15, 135, jF);
    }
    public void flipSecond(){
        hand.get(1).flip();
    }
    public void reset(){
        hand.clear();
        next_card_x_pos = 490;
    }
    public int calculateSoftHand(){
        int total = 0;
        for(Card c : hand){
            if(c.isAce()) total += 1;
            else total += c.getValue();
        }
        return total;
    }
}