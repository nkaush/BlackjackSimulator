import javax.swing.JComponent;
import java.awt.geom.Rectangle2D;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.BasicStroke;
public class Button extends JComponent{
    public static final String HIT = "Hit";
    public static final String STAND = "Stand";
    public static final String DOUBLE = "Double Down";
    public static final String SPLIT = "Split Hand";
    public static final String START = "DEAL";
    public static final String CONTINUE = "CONTINUE";
    public static final String RESTART = "RESTART";
    public static final String PLUS = "+";
    public static final String MINUS = "-";
    
    Rectangle2D rect;
    double x, y, width, height;
    boolean active;
    String type;
    Color color;
    public Button(double x_val, double y_val, double w, double h, String t){
        x = x_val;
        y = y_val;
        width = w;
        height = h;
        type = t;
        active = true;
        color = Color.WHITE;
        rect = new Rectangle2D.Double(x, y, width, height);
    }
    public Button(double x_val, double y_val, double w, double h, String t, Color c){
        x = x_val;
        y = y_val;
        width = w;
        height = h;
        type = t;
        active = true;
        color = c;
        rect = new Rectangle2D.Double(x, y, width, height);
    }
    public void paintComponent(Graphics g){       
        if(active){
            Graphics2D g2 = (Graphics2D) g;
            g2.setColor(color);
            g2.fill(rect);
            g2.setStroke(new BasicStroke(2));
            g2.setColor(Color.BLACK);
            g2.draw(rect);
            if(type.equals(START)){
                g2.setFont(new Font("SansSerif", Font.PLAIN, 50));
                g2.drawString(type, (int)x+35, (int)y+70);
            }
            else if(type.equals(CONTINUE)){
                g2.setFont(new Font("SansSerif", Font.PLAIN, 32));
                g2.drawString(type, (int)x+18, (int)y+62);
            }
            else if(type.equals(RESTART)){
                g2.setFont(new Font("SansSerif", Font.PLAIN, 38));
                g2.drawString(type, (int)x+18, (int)y+65);
            }
            else if(type.equals(PLUS) || type.equals(MINUS)){
                g2.drawString(type, (int)x+10, (int)y+18);
            }
            else{
                g2.drawString(type, (int)x+5, (int)y+20);
            }   
        }
    }
    public boolean getActive(){
        return active;
    }
    public void setActive(boolean b){
        active = b;
        repaint();
    }
    public void setType(String s){
        type = s;
        repaint();
    }
    public boolean wasClicked(int x, int y){
        return rect.contains(x, y);
    }
}