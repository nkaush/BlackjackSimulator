import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
public class CardDeck{
    Card[] allCards, arrayOfCards;
    int totalCardSets;
    double deck_x_pos, deck_y_pos;  
    public CardDeck(int tCS, double x, double y){
        totalCardSets = tCS;
        deck_x_pos = x;
        deck_y_pos = y;
        resetDeck();
        arrayOfCards = allCards;
        shuffleDeck();         
    }
    public CardDeck(boolean b, int tCS, double x, double y){
        totalCardSets = tCS;
        deck_x_pos = x;
        deck_y_pos = y;
        devDeck();
        arrayOfCards = allCards;
        shuffleDeck();         
    }
    public Card[] getDeck(){
        return allCards;
    }
    public Card removeTopCard(){        
        if(allCards.length > 0){
            Card topCard = allCards[allCards.length-1];
            allCards = Arrays.copyOf(allCards, allCards.length-1);
            return topCard;
        }
        else{
            return null;
        }
    }
    public void addCardToBottom(Card c){
        Card[] oldDeck = Arrays.copyOf(allCards, allCards.length);
        allCards = new Card[allCards.length+1];
        allCards[0] = c;
        int counter = 1;
        for(Card next : oldDeck){
            allCards[counter] = next;
            counter++;
        }
        c.goTo(deck_x_pos, deck_y_pos, 8);
        c.flip();
    }
    public void resetDeck(){
        allCards = new Card[totalCardSets*52];
        String[] suits = {Card.CLUB, Card.SPADE, Card.HEART, Card.DIAMOND};
        String[] faceCards = {Card.JACK, Card.QUEEN, Card.KING, Card.ACE};
        int counter = 0;
        for(int i = 0; i < totalCardSets; i++){
            for(String s : suits){
                for(int v = 2; v <= 10; v++){
                    allCards[counter] = new Card(s, v, deck_x_pos, deck_y_pos);
                    counter++;
                }
                for(String fC : faceCards){
                    allCards[counter] = new Card(s, fC, deck_x_pos, deck_y_pos);
                    counter++;
                }
            }
        }
    }
    public void devDeck(){
        allCards = new Card[totalCardSets*16];
        String[] suits = {Card.CLUB, Card.SPADE, Card.HEART, Card.DIAMOND};
        String[] faceCards = {Card.JACK, Card.QUEEN, Card.KING, Card.ACE};
        int counter = 0;
        for(int i = 0; i < totalCardSets; i++){
            for(String s : suits){
                for(String fC : faceCards){
                    allCards[counter] = new Card(s, fC, deck_x_pos, deck_y_pos);
                    counter++;
                }
            }
        }
    }
    public void shuffleDeck(){
        ArrayList<Card> oldDeck = new ArrayList<Card>(Arrays.asList(allCards));
        Card[] shuffled = new Card[allCards.length];
        Random r = new Random();
        for(int i = 0; i < shuffled.length; i++){
            Card tempCard = oldDeck.get(r.nextInt(oldDeck.size()));
            oldDeck.remove(tempCard);
            shuffled[i] = tempCard;
        }
        allCards = shuffled;
    }
}