import javax.swing.JComponent;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
public class TextComp extends JComponent{
    int xPos, yPos, fontSize;
    String display;
    Color mainColor;
    public TextComp(String d, int x, int y){
        xPos = x+20;
        yPos = y-20;
        display = d;
        mainColor = Color.YELLOW;
        fontSize = 80;
    }
    public TextComp(String d, int x, int y, Color c){
        xPos = x+20;
        yPos = y-20;
        display = d;
        mainColor = c;
        fontSize = 50;
    }
    public void paintComponent(Graphics g){
        Graphics2D g2 = (Graphics2D) g;
        g2.setFont(new Font("SansSerif", Font.PLAIN, fontSize));
        g2.setColor(mainColor);
        g2.drawString(display, xPos, yPos);
    }
}