import java.util.ArrayList;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JComponent;
import javax.swing.AbstractAction;
import javax.swing.Timer;
import javax.swing.JFrame;
public class GameLogic extends JComponent{
    Dealer dealer;
    Player[] players;
    Player currentPlayer;    
    CardDeck deck;
    ArrayList<Card> activeCards;    
    ArrayList<TextComp> allTC;  
    TextComp placeBets;
    Polygon poly;    
    Timer startRoundAnimationTimer, hitAnimationTimer, returnToDeckAnimationTimer, dealerHitAnimationTimer;
    Button nextActionButton;   
    JFrame frame;   
    boolean roundPlaying, showArrow, needsReset;
    int[] arrow_x_coords = {237, 697, 1158};
    int currentPlayerIndex, totalRounds;
    public GameLogic(Dealer d, Player[] ps, CardDeck dk, JFrame jF){
        dealer = d;
        players = ps;
        deck = dk;
        activeCards = new ArrayList<Card>();
        allTC = new ArrayList<TextComp>();
        placeBets = new TextComp("Place Your Bets", 25, 80, Color.BLACK);
        nextActionButton = new Button(598, 242.5, 200, 100, Button.START, Color.YELLOW);
        frame = jF;
        frame.add(this); frame.setVisible(true);
        frame.add(nextActionButton); frame.setVisible(true);
        frame.add(placeBets); frame.setVisible(true);
    }
    public void paintComponent(Graphics g){
        Graphics2D g2 = (Graphics2D) g;
        if(roundPlaying && showArrow && currentPlayerIndex < 3){
            int x_val = arrow_x_coords[currentPlayerIndex];
            int y_val = 450;
            poly = new Polygon(new int[]{x_val-20, x_val+20, x_val}, new int[]{y_val-35, y_val-35, y_val}, 3);
            g2.setColor(Color.YELLOW);
            g2.fill(poly);
        }
    }
    private void startRound(){
        activeCards.clear();
        if(!roundPlaying){
            Person[] people = new Person[4];
            people[3] = dealer;
            for(int i = 0; i < players.length; i++){
                players[i].alternateBetBox();
                players[i].setPlusMinus(false);
                people[i] = players[i];
            }         
            roundPlaying = true;  
            frame.remove(placeBets); frame.setVisible(true);
            new StartRoundAnimation(people, 0);            
            new StartRoundAnimation(people, 2000);               
        }
    }
    private boolean buttonClicked(int x, int y){
        return nextActionButton.getActive() && nextActionButton.wasClicked(x, y);
    }
    private void findNextPlayer(){        
        while(currentPlayerIndex < 3 && players[currentPlayerIndex].getHasResult()){
            currentPlayerIndex++;
        }
        if(currentPlayerIndex < 3) currentPlayer = players[currentPlayerIndex];
        else {nextActionButton.setActive(true); repaint();}
        repaint();
    }
    private void dealerActions(){
        dealer.flipSecond();
        if(dealer.calculateHandTotal(dealer.getHand()) <= 21){
            new DealerHitAnimation();
        }
    }
    private Player checkBetIncrease(int x, int y){
        for(Player p : players){
            if(p.plusWasClicked(x, y)) return p;
        }
        return null;
    }
    private Player checkBetDecrease(int x, int y){
        for(Player p : players){
            if(p.minusWasClicked(x, y)) return p;
        }
        return null;
    }
    public void nextAction(int x, int y){
        if(!roundPlaying && checkBetIncrease(x, y) != null){
            checkBetIncrease(x, y).increaseBet();
        }
        else if(!roundPlaying && checkBetDecrease(x, y) != null){
            checkBetDecrease(x, y).decreaseBet();
        }
        else if(needsReset){
            nextActionButton.setActive(false);
            nextActionButton.setType(Button.START); 
            for(Player p : players) p.reset();
            dealer.reset();
            for(TextComp tC : allTC) frame.remove(tC);
            allTC.clear();
            roundPlaying = false;
            needsReset = false;
            currentPlayerIndex = 0;
            findNextPlayer();
            new ReturnToDeckAnimation();
        }
        else if(!roundPlaying && buttonClicked(x, y)){
            startRound();
            nextActionButton.setType(Button.CONTINUE);
            nextActionButton.setActive(false);
        }        
        else if(roundPlaying && currentPlayerIndex > 2 && buttonClicked(x, y)){
            nextActionButton.setType(Button.RESTART);
            nextActionButton.setActive(false);
            dealerActions();
        }
        else if(roundPlaying){
            if(currentPlayerIndex == 3){
                nextActionButton.setActive(true);
                repaint();
            }
            else if(currentPlayerIndex < 3){
                findNextPlayer();
                if(currentPlayer.splitWasClicked(x, y)){
                    currentPlayer.splitCurrentHand();
                }
                if(currentPlayer.hitWasClicked(x, y)){
                    currentPlayer.setSplitButton(true);
                    currentPlayer.updateButtons();
                    new HitAnimation(currentPlayer);
                }
                if(currentPlayer.standWasClicked(x, y)){
                    if(currentPlayer.isHandSplit() && !currentPlayer.onSecondHand()){
                        currentPlayer.setSecondHand(true);
                        currentPlayer.setHitButton(true);
                    }
                    else{
                        currentPlayer.setAllButtons(false);
                        currentPlayer.setTurnCompleted(true);
                        currentPlayerIndex++;
                        findNextPlayer();
                        repaint();
                        if(currentPlayerIndex == 3){
                            nextActionButton.setActive(true); repaint();
                        }                    
                    }
                }
            }
        }
    }
    public boolean timersInAction(){
        return (startRoundAnimationTimer != null && startRoundAnimationTimer.isRunning())       || 
               (hitAnimationTimer != null && hitAnimationTimer.isRunning())                     ||
               (returnToDeckAnimationTimer != null && returnToDeckAnimationTimer.isRunning())   ||
               (dealerHitAnimationTimer != null && dealerHitAnimationTimer.isRunning());
    }
    class StartRoundAnimation{
        public StartRoundAnimation(Person[] people, int initialDelay){
            int counter[] = {0};
            ActionListener listener = new AbstractAction(){
                public void actionPerformed(ActionEvent e){                                      
                    Person p = people[counter[0]];
                    Card flippedCard = deck.removeTopCard();
                    if(counter[0] != 3 || initialDelay == 0) flippedCard.flip();
                    p.addCard(flippedCard);    
                    activeCards.add(flippedCard);
                    counter[0]++;
                    for(Player ps : players){
                        ps.updateButtons();
                    }
                    if(counter[0] > 3){                      
                        ((Timer)e.getSource()).stop();
                        if(initialDelay > 0){
                            showArrow = true;
                            repaint();
                        }    
                        for(Player pr : players){
                            if(pr.hasBlackjack()){
                                TextComp tempTC = new TextComp(Player.BLJK, pr.getBoxX(), pr.getBoxY());
                                pr.addGains(Player.BLJK_MODIFIER);
                                allTC.add(tempTC);
                                frame.add(tempTC);                                  
                                frame.setVisible(true);
                                pr.setHasResult(true);
                                pr.setAllButtons(false);
                            }
                        }
                        findNextPlayer();
                    }
                }
            };
            startRoundAnimationTimer = new Timer(500, listener);
            startRoundAnimationTimer.setCoalesce(false);
            startRoundAnimationTimer.setInitialDelay(initialDelay);
            startRoundAnimationTimer.setRepeats(true);
            startRoundAnimationTimer.start();           
        }
    }
    class HitAnimation{
        public HitAnimation(Player cP){
            ActionListener listener = new AbstractAction(){
                public void actionPerformed(ActionEvent e){                      
                    Card flippedCard = deck.removeTopCard();
                    flippedCard.flip();
                    if(!cP.onSecondHand()){
                        cP.addCard(flippedCard);
                        activeCards.add(flippedCard);
                    }
                    else{
                        cP.addCardSecondHand(flippedCard);
                        activeCards.add(flippedCard);
                    }
                    ((Timer)e.getSource()).stop();
                    cP.updateButtons();
                    if(cP.hasBlackjack() || cP.hasBust() || 
                      (!cP.onSecondHand() && cP.getHand().size() == 5) ||
                      (cP.onSecondHand() && cP.getSecondHand().size() == 5)){
                        TextComp tempTC;
                        if(cP.hasBust()){
                            tempTC = new TextComp(Player.BUST, cP.getBoxX(), cP.getBoxY());
                            cP.addGains(cP.BUST_MODIFIER);                       
                        }
                        else if(cP.hasBlackjack()){
                            tempTC = new TextComp(Player.BLJK, cP.getBoxX(), cP.getBoxY());
                            cP.addGains(cP.BLJK_MODIFIER);
                        }
                        else{
                            tempTC = new TextComp(Player.WIN, cP.getBoxX(), cP.getBoxY());
                            cP.addGains(cP.WIN_MODIFIER);
                        }
                        cP.setAllButtons(false); cP.setTurnCompleted(true); cP.setHasResult(true);
                        currentPlayerIndex++; findNextPlayer();
                        allTC.add(tempTC); frame.add(tempTC); frame.setVisible(true);
                        if(currentPlayerIndex == 3) nextActionButton.setActive(true);    
                    }
                    repaint();                   
                }
            };
            hitAnimationTimer = new Timer(500, listener);
            hitAnimationTimer.setCoalesce(false); hitAnimationTimer.setRepeats(true);
            hitAnimationTimer.start(); 
        }
    }
    class DealerHitAnimation{
        public DealerHitAnimation(){
            ActionListener listener = new AbstractAction(){
                public void actionPerformed(ActionEvent e){     
                    if(!(dealer.calculateSoftHand()>17 || dealer.calculateHandTotal(dealer.getHand())>19)){
                        Card flippedCard = deck.removeTopCard();
                        flippedCard.flip();
                        dealer.addCard(flippedCard);
                        activeCards.add(flippedCard);
                    }
                    if(dealer.calculateSoftHand()>17 || dealer.calculateHandTotal(dealer.getHand())>19){
                        ((Timer)e.getSource()).stop();
                        boolean cont = true;
                        for(Player p : players){
                            cont = cont && p.getHasResult();
                        }
                        if(!cont){
                            for(Player p : players){
                                if(!p.getHasResult()){
                                    int pTotal = p.calculateHandTotalWithBusts(p.getHand());
                                    int dTotal = dealer.calculateHandTotal(dealer.getHand());
                                    TextComp tempTC;
                                    if(dTotal <= 21){ 
                                        p.setAllButtons(false);
                                        p.setTurnCompleted(true);
                                        currentPlayerIndex++;
                                        findNextPlayer();
                                        p.setHasResult(true);
                                        if(pTotal > dTotal){
                                            tempTC = new TextComp(Player.WIN, p.getBoxX(), p.getBoxY());
                                            p.addGains(Player.WIN_MODIFIER);
                                        }
                                        else if(pTotal == dTotal){
                                            tempTC = new TextComp(Player.PUSH, p.getBoxX(), p.getBoxY());
                                            p.addGains(Player.PUSH_MODIFIER);
                                        }
                                        else{
                                            tempTC = new TextComp(Player.LOSE, p.getBoxX(), p.getBoxY());  
                                            p.addGains(Player.LOSE_MODIFIER);
                                        }                                   
                                    }
                                    else{
                                        tempTC = new TextComp(Player.WIN, p.getBoxX(), p.getBoxY());
                                        p.addGains(Player.WIN_MODIFIER);
                                    }
                                    allTC.add(tempTC);
                                    frame.add(tempTC);                                  
                                    frame.setVisible(true);
                                }
                            }
                        }
                        nextActionButton.setActive(true);
                        needsReset = true;                       
                    }
                }
            };
            dealerHitAnimationTimer = new Timer(1000, listener);
            dealerHitAnimationTimer.setInitialDelay(500);
            dealerHitAnimationTimer.setCoalesce(false);
            dealerHitAnimationTimer.setRepeats(true);
            dealerHitAnimationTimer.start(); 
        }
    }
    class ReturnToDeckAnimation{
        public ReturnToDeckAnimation(){
            ActionListener listener = new AbstractAction(){
                public void actionPerformed(ActionEvent e){                   
                    if(activeCards.size() > 0){
                        deck.addCardToBottom(activeCards.get(0));
                        activeCards.get(0).resetAce();
                        activeCards.remove(0);
                    }
                    else{
                        ((Timer)e.getSource()).stop();
                        nextActionButton.setActive(true);                        
                        deck.shuffleDeck();                       
                        frame.add(placeBets); frame.setVisible(true);
                    }
                }
            };
            returnToDeckAnimationTimer = new Timer(100, listener);
            returnToDeckAnimationTimer.setCoalesce(false);
            returnToDeckAnimationTimer.setRepeats(true);
            returnToDeckAnimationTimer.start(); 
        }
    }
}