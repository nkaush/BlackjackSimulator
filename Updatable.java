import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.BasicStroke;
import java.awt.geom.Rectangle2D;
public class Updatable extends Button{
    boolean gap, onAlt;
    int value;
    double alt_x, alt_width;
    Rectangle2D alt_rect;
    public Updatable(double x_val, double y_val, double w, double h, String t, int val, boolean g){
        super(x_val, y_val, w, h, t);
        gap = g;
        value = val;
        alt_x = x_val;
        alt_width = width;
        alt_rect = new Rectangle2D.Double(alt_x, y, alt_width, height);
    }
    public Updatable(double x_val, double y_val, double w, double h, String t, int val, boolean g, double aX, double aW){
        super(x_val, y_val, w, h, t);
        gap = g;
        value = val;
        alt_x = aX;
        alt_width = aW;
        alt_rect = new Rectangle2D.Double(alt_x, y, alt_width, height);
    }
    public void paintComponent(Graphics g){       
        if(active){
            Graphics2D g2 = (Graphics2D) g;
            g2.setColor(color);
            if(!onAlt) g2.fill(rect);
            else g2.fill(alt_rect);
            g2.setStroke(new BasicStroke(2));
            g2.setColor(Color.BLACK);
            if(!onAlt) g2.draw(rect); 
            else g2.draw(alt_rect);
            if(gap){
                g2.drawString(type, (int)x+5, (int)y+20);
                g2.drawString("$" + Integer.toString(value), (int)x+110, (int)y+20);
            }
            else g2.drawString(type + "$" + value, (int)x+5, (int)y+20);
        }
    }
    public void updateValue(int newVal){
        value = newVal;
        repaint();
    }
    public void setAlternateRect(){
        onAlt = !onAlt;
        repaint();
    }
}