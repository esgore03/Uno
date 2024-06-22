package org.example.eiscuno.model.player;

import org.example.eiscuno.model.card.Card;

import java.util.ArrayList;

/**
 * Represents a player in the Uno game.
 */
public class Player implements IPlayer {
    private ArrayList<Card> cardsPlayer;
    private String typePlayer;
    private volatile boolean isProtectedByUno;

    /**
     * Constructs a new Player object with an empty hand of cards.
     */
    public Player(String typePlayer){
        this.cardsPlayer = new ArrayList<Card>();
        this.typePlayer = typePlayer;
        this.isProtectedByUno = false;
    };

    /**
     * Adds a card to the player's hand.
     *
     * @param card The card to be added to the player's hand.
     */
    @Override
    public void addCard(Card card){
        cardsPlayer.add(card);
    }

    /**
     * Retrieves all cards currently held by the player.
     *
     * @return an ArrayList containing all cards in the player's hand.
     */
    @Override
    public ArrayList<Card> getCardsPlayer() {
        return cardsPlayer;
    }

    /**
     * Removes a card from the player's hand based on its index.
     *
     * @param index the index of the card to remove.
     */
    @Override
    public void removeCard(int index) {
        cardsPlayer.remove(index);
    }

    /**
     * Retrieves a card from the player's hand based on its index.
     *
     * @param index the index of the card to retrieve.
     * @return the card at the specified index in the player's hand.
     */
    @Override
    public Card getCard(int index){
        return cardsPlayer.get(index);
    }

    /**
     * Sets the protected by UNO status for the player.
     *
     * @param protectedByUno true if the player is protected by UNO, false otherwise
     */
    public void setProtectedByUno(boolean protectedByUno) {
        isProtectedByUno = protectedByUno;
    }

    /**
     * Checks if the player is protected by UNO.
     *
     * @return true if the player is protected by UNO, false otherwise
     */
    public boolean isProtectedByUno() {
        return isProtectedByUno;
    }
}