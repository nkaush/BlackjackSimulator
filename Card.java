import javax.swing.JComponent;
import java.awt.geom.Rectangle2D;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JComponent;
import javax.swing.AbstractAction;
import javax.swing.Timer;
public class Card extends JComponent{       
    public static final String CLUB = "\u2663";
    public static final String SPADE = "\u2660";
    public static final String HEART = "\u2665";
    public static final String DIAMOND = "\u2666";
    public static final String ACE = "A";
    public static final String JACK = "J";
    public static final String QUEEN = "Q";
    public static final String KING = "K";
    
    private final int width = 75;
    private final int height = 105;
    
    static Timer moveTimer;
    
    boolean faceup = false;
    double x, y;
    int value;
    String display, suit;
    Color mainColor;
    Rectangle2D rect; 
    public Card(String s, int v, double x_val, double y_val){
        suit = s;
        value = v;
        display = Integer.toString(value);
        x = x_val;
        y = y_val;       
        if(suit.equals(CLUB) || suit.equals(SPADE)) mainColor = Color.BLACK;
        else mainColor = Color.RED;
    }
    public Card(String s, String d, double x_val, double y_val){
        suit = s;
        display = d;
        if(d.equals(ACE)) value = 11;
        else value = 10;
        x = x_val;
        y = y_val;
        rect = new Rectangle2D.Double(x, y, width, height);
        if(suit.equals(CLUB) || suit.equals(SPADE)) mainColor = Color.BLACK;
        else mainColor = Color.RED;
    }    
    public void paintComponent(Graphics g){       
        rect = new Rectangle2D.Double(x, y, width, height);
        Graphics2D g2 = (Graphics2D) g;
        g2.setColor(Color.BLACK);
        g2.draw(rect);
        g2.setColor(Color.WHITE);
        g2.fill(rect);
        if(faceup){ 
            g2.setFont(new Font("SansSerif", Font.PLAIN, 40)); 
            g2.setColor(mainColor);
            g2.drawString(display, (int)x+12, (int)y+68);
            g2.setFont(new Font("SansSerif", Font.PLAIN, 25)); 
            g2.drawString(suit, (int)x, (int)y+25);
            g2.drawString(suit, (int)x+50, (int)y+100);
        }
        else{           
            g2.setColor(Color.BLUE);
            g2.fill(new Rectangle2D.Double(x+5, y+5, width-10, height-10));
        }
    }
    public void flip(){
        faceup = !faceup;
        repaint();
    }
    public boolean isAce(){
        return display.equals(ACE);
    }
    public void changeAce(){
        if(isAce()) value = 1;
    }
    public void resetAce(){
        if(isAce()) value = 11;
    }
    public void goTo(double newx, double newy, int time){
        new DealAnimation(newx, newy, time);
    }
    public int getValue(){
        return value;
    }
    public String getDisplay(){
        return display;
    }
    class DealAnimation{
        public DealAnimation(double newx, double newy, int time){
            double xchange = (newx - x)/100;
            double ychange = (newy - y)/100;
            ActionListener listener = new AbstractAction(){
                public void actionPerformed(ActionEvent e){                   
                    if(Math.abs(newx - x) > 3 || Math.abs(newy - y) > 3){
                        x += xchange;
                        y += ychange;
                        repaint();
                    }
                    else{
                        x = newx;
                        y = newy;
                        repaint();
                        ((Timer)e.getSource()).stop();
                    }
                }
            };
            moveTimer = new Timer(time, listener);
            moveTimer.setCoalesce(false);
            moveTimer.setInitialDelay(0);
            moveTimer.setRepeats(true);
            moveTimer.start();           
        }
    }
}