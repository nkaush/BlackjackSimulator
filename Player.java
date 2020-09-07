import java.util.ArrayList;
import java.awt.geom.Rectangle2D;
import javax.swing.JFrame;
public class Player extends Person{
    private static int counter = 0;
    private static int[] card_x_coords = new int[]{30, 490, 950};
    private static int[] box_x_coords = new int[]{15, 475, 935};
    
    public static String BLJK = "Blackjack!";
    public static String BUST = "Bust!";
    public static String PUSH = "Push!";
    public static String WIN = "Win!";
    public static String LOSE = "Lose!";
    
    public static double BLJK_MODIFIER = 2.5;
    public static double BUST_MODIFIER = 0;
    public static double PUSH_MODIFIER = 1;
    public static double WIN_MODIFIER = 2;    
    public static double LOSE_MODIFIER = 0;
    
    ArrayList<Card> secondHand;
    Button hitButton, standButton, doubleButton, splitButton, plus, minus;
    Updatable betBox, nameBox;
    boolean turnCompleted, handSplit, onSecondHand, hasResult;
    int secondHandTotal, second_next_card_x_pos, second_next_card_y_pos, moneyTotal, bet;
    public Player(JFrame jF){
        super(card_x_coords[counter], 470, box_x_coords[counter], 455, Person.BOX_HEIGHT, jF);
        moneyTotal = 200;  
        
        hitButton = new Button(box_x_coords[counter]+5, 700, 100, 30, Button.HIT);
        standButton = new Button(box_x_coords[counter]+5, 735, 100, 30, Button.STAND);
        splitButton = new Button(box_x_coords[counter]+110, 735, 100, 30, Button.SPLIT);
        doubleButton = new Button(box_x_coords[counter]+110, 700, 100, 30, Button.DOUBLE);      
        minus = new Button(box_x_coords[counter]+BOX_WIDTH-155, 455+BOX_HEIGHT-70, 30, 30, Button.MINUS);
        plus = new Button(box_x_coords[counter]+BOX_WIDTH-35, 455+BOX_HEIGHT-70, 30, 30, Button.PLUS);
        nameBox = new Updatable(box_x_coords[counter]+BOX_WIDTH-155, 455+BOX_HEIGHT-35, 150, 30, "Player " + (counter+1) + ": ", moneyTotal, true);
        betBox = new Updatable(box_x_coords[counter]+BOX_WIDTH-120, 455+BOX_HEIGHT-70, 80, 30, "Bet: ", bet, false, box_x_coords[counter]+BOX_WIDTH-155, 150);
        
        jF.add(hitButton); jF.setVisible(true);
        jF.add(standButton); jF.setVisible(true);
        jF.add(splitButton); jF.setVisible(true);
        jF.add(doubleButton); jF.setVisible(true);
        jF.add(plus); jF.setVisible(true);
        jF.add(minus); jF.setVisible(true);
        jF.add(nameBox); jF.setVisible(true);
        jF.add(betBox); jF.setVisible(true);
        
        counter++;
        second_next_card_x_pos = next_card_x_pos;
        second_next_card_y_pos = next_card_y_pos+115;
        secondHand = new ArrayList<Card>();
        if(counter == 3) counter = 0;
    }
    public void reset(){
        hand.clear();
        secondHand.clear();
        next_card_x_pos = card_x_coords[counter];
        second_next_card_x_pos = next_card_x_pos;
        counter++;
        turnCompleted = false;
        handSplit = false;
        onSecondHand = false;
        hasResult = false;
        setAllButtons(true);
        setPlusMinus(true);
        betBox.setAlternateRect();
        if(counter == 3) counter = 0;
    }
    public ArrayList<Card> getSecondHand(){
        return secondHand;
    }
    public void updateButtons(){
        splitButton.setActive(hand.size()==2&&hand.get(0).getDisplay().equals(hand.get(1).getDisplay())&&!handSplit&&!turnCompleted);
        if(hand.size()>=5) hitButton.setActive(false);
        if(onSecondHand && secondHand.size()<=4) hitButton.setActive(true);
        else if(secondHand.size()>4) hitButton.setActive(false);
        if(hand.size()>2) doubleButton.setActive(false);
    }
    public boolean hasBlackjack(){
        if(onSecondHand){
            return calculateHandTotal(secondHand) == 21;
        }
        else{
            return calculateHandTotal(hand) == 21;
        }
    }
    public boolean hasBust(){
        if(onSecondHand){
            if(handContainsAce(secondHand) && calculateHandTotal(secondHand) > 21){
                int index = 0;
                while(index < secondHand.size() && calculateHandTotal(secondHand) > 21){
                    secondHand.get(index).changeAce();
                    index++;
                }
            }
            return calculateHandTotal(secondHand) > 21;
        }
        else{
            if(handContainsAce(hand) && calculateHandTotal(hand) > 21){
                int index = 0;
                while(index < hand.size() && calculateHandTotal(hand) > 21){
                    hand.get(index).changeAce();
                    index++;
                }
            }
            return calculateHandTotal(hand) > 21;
        }
    }
    public void setAllButtons(boolean b){
        hitButton.setActive(b);
        splitButton.setActive(b);
        doubleButton.setActive(b);
        standButton.setActive(b);
    }
    public void setPlusMinus(boolean b){
        plus.setActive(b);
        minus.setActive(b);
    }
    public void alternateBetBox(){
        betBox.setAlternateRect();
    }
    public void increaseBet(){
        bet += 10;
        moneyTotal -= 10;
        betBox.updateValue(bet);
        nameBox.updateValue(moneyTotal);
    }
    public void decreaseBet(){
        bet -= 5;
        moneyTotal += 5;
        betBox.updateValue(bet);
        nameBox.updateValue(moneyTotal);
    }
    public void doubleBet(){
        moneyTotal -= bet;
        bet *= 2;  
        betBox.updateValue(bet);
        nameBox.updateValue(moneyTotal);
    }
    public void addGains(double modifier){
        moneyTotal += (int)((double)bet * modifier);       
        bet = 0;
        nameBox.updateValue(moneyTotal);
        betBox.updateValue(bet);        
    }
    public boolean hitWasClicked(int x, int y){
        return hitButton.wasClicked(x, y) && hitButton.getActive();     
    }
    public boolean standWasClicked(int x, int y){
        return standButton.wasClicked(x, y) && standButton.getActive();     
    }
    public boolean splitWasClicked(int x, int y){
        return splitButton.wasClicked(x, y) && splitButton.getActive();     
    }
    public boolean doubleWasClicked(int x, int y){
        return doubleButton.wasClicked(x, y) && doubleButton.getActive();     
    }
    public boolean plusWasClicked(int x, int y){
        return plus.wasClicked(x, y) && plus.getActive() && moneyTotal > 5;
    }
    public boolean minusWasClicked(int x, int y){
        return minus.wasClicked(x, y) && minus.getActive() && bet > 0;
    }
    public void splitCurrentHand(){
        if(!handSplit){
            handSplit = true;
            Card secondCard = hand.get(1);
            secondHand.add(secondCard);
            hand.remove(1);
            secondCard.goTo(second_next_card_x_pos, second_next_card_y_pos, 5);
            second_next_card_x_pos += 85;
            next_card_x_pos -= 85;
        }
        updateButtons();
    }
    public void addCardSecondHand(Card c){
        secondHand.add(c);
        c.goTo(second_next_card_x_pos, second_next_card_y_pos, 5);
        second_next_card_x_pos += 85;
    }
    public boolean isHandSplit(){
        return handSplit;
    }
    public boolean onSecondHand(){
        return onSecondHand;
    }
    public boolean getHasResult(){
        return hasResult;
    }
    public void setSecondHand(boolean b){
        onSecondHand = b;
    }
    public void setSplitButton(boolean b){
        splitButton.setActive(b);
    }
    public void setHitButton(boolean b){
        hitButton.setActive(b);
    }
    public void setTurnCompleted(boolean b){
        turnCompleted = b;
    }
    public void setHasResult(boolean b){
        hasResult = b;
    }
}