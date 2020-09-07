import javax.swing.JFrame;
import java.awt.event.MouseListener;
import java.awt.event.MouseEvent;
import java.awt.Color;
public class Starter{
    public static void main(String[] args){
        JFrame f = new JFrame("Blackjack");
        int frameWidth = 1395;
        int frameHeight = 800;
        f.setSize(frameWidth, frameHeight);
        f.getContentPane().setBackground(new Color(33, 140, 47));
        f.setVisible(true);        
        CardDeck deck = new CardDeck(4, frameWidth-100, 25);
        GameLogic gL = new GameLogic(new Dealer(f), new Player[]{new Player(f), new Player(f), new Player(f)}, deck, f);       
        for(Card c : deck.getDeck()){
            f.add(c); f.setVisible(true);
        }         
        class myMouseListener implements MouseListener{
            public void mouseClicked(MouseEvent e){}
            public void mousePressed(MouseEvent e){}
            public void mouseEntered(MouseEvent e){}
            public void mouseExited(MouseEvent e){}
            public void mouseReleased(MouseEvent e){                                                          
                if(!gL.timersInAction()){
                    gL.nextAction(e.getX(), e.getY()-20);
                }
            }
        }
        f.addMouseListener(new myMouseListener());
    }
}