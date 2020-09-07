import java.util.ArrayList;
import javax.swing.JComponent;
import java.awt.geom.Rectangle2D;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Font;
import java.awt.Color;
import javax.swing.JFrame;
abstract class Person extends JComponent{
    public static final int BOX_WIDTH = 445;
    public static final int BOX_HEIGHT = 315;
    
    Rectangle2D rect;
    ArrayList<Card> hand; 
    int handTotal, next_card_x_pos, next_card_y_pos, x, y;
    public Person(int card_x, int card_y, int box_x, int box_y, int height, JFrame jF){
        next_card_x_pos = card_x;
        next_card_y_pos = card_y;
        x = box_x;
        y = box_y;
        rect = new Rectangle2D.Double(box_x, box_y, BOX_WIDTH, height);
        hand = new ArrayList<Card>();
        jF.add(this); jF.setVisible(true);
    }
    public void paintComponent(Graphics g){
        Graphics2D g2 = (Graphics2D) g;
        g2.draw(rect);
    }
    public ArrayList<Card> getHand(){
        return hand;
    }
    public void addCard(Card c){
        hand.add(c);
        c.goTo(next_card_x_pos, next_card_y_pos, 5);
        next_card_x_pos += 85;
    }
    public boolean handContainsAce(ArrayList<Card> list){
        for(Card c : list){
            if(c.isAce()) return true;
        }
        return false;
    }
    public int calculateHandTotal(ArrayList<Card> list){
        handTotal = 0;
        for(Card c : list){
            handTotal += c.getValue();
        }
        return handTotal;   
    }
    public int calculateHandTotalWithBusts(ArrayList<Card> list){
        if(handContainsAce(hand) && calculateHandTotal(hand) > 21){
            int index = 0;
            while(index < hand.size() && calculateHandTotal(hand) > 21){
                hand.get(index).changeAce();
                index++;
            }
        }       
        handTotal = 0;
        for(Card c : list){
            handTotal += c.getValue();
        }
        return handTotal;   
    }
    public int getBoxX(){
        return x;
    }
    public int getBoxY(){
        return y;
    }
}