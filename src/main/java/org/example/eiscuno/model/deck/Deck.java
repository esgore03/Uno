package org.example.eiscuno.model.deck;

import org.example.eiscuno.model.unoenum.EISCUnoEnum;
import org.example.eiscuno.model.card.Card;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Stack;

/**
 * Represents a deck of Uno cards.
 */
public class Deck {
    private Stack<Card> deckOfCards;

    /**
     * Constructs a new deck of Uno cards and initializes it.
     */
    public Deck() {
        deckOfCards = new Stack<>();
        initializeDeck();
    }

    /**
     * Initializes the deck with cards based on the EISCUnoEnum values.
     */
    private void initializeDeck() {
        for (EISCUnoEnum cardEnum : EISCUnoEnum.values()) {
            if (cardEnum.name().startsWith("GREEN_") ||
                    cardEnum.name().startsWith("YELLOW_") ||
                    cardEnum.name().startsWith("BLUE_") ||
                    cardEnum.name().startsWith("RED_") ||
                    cardEnum.name().startsWith("SKIP_") ||
                    cardEnum.name().startsWith("REVERSE_") ||
                    cardEnum.name().startsWith("TWO_WILD_DRAW_") ||
                    cardEnum.name().equals("FOUR_WILD_DRAW") ||
                    cardEnum.name().equals("WILD")) {
                Card card = new Card(cardEnum.getFilePath(), getCardValue(cardEnum.name()), getCardColor(cardEnum.name()));
                deckOfCards.push(card);
                // Print each card
                System.out.println(card.getValue() + " " + card.getColor());
            }
        }
        Collections.shuffle(deckOfCards);
    }

    /**
     * Extracts the value from a card's name.
     * <p>
     * This method checks the card's name and returns the corresponding value.
     * It supports numerical values (0-9), special action cards such as "REVERSE",
     * "+2", "+4", "WILD", and "SKIP".
     * </p>
     *
     * @param name the name of the card
     * @return the value of the card as a string, or null if the name does not match any known card values
     */
    private String getCardValue(String name) {
        if (name.endsWith("0")) {
            return "0";
        } else if (name.endsWith("1")) {
            return "1";
        } else if (name.endsWith("2")) {
            return "2";
        } else if (name.endsWith("3")) {
            return "3";
        } else if (name.endsWith("4")) {
            return "4";
        } else if (name.endsWith("5")) {
            return "5";
        } else if (name.endsWith("6")) {
            return "6";
        } else if (name.endsWith("7")) {
            return "7";
        } else if (name.endsWith("8")) {
            return "8";
        } else if (name.endsWith("9")) {
            return "9";
        } else if (name.contains("REVERSE")) {
            return "REVERSE";
        } else if (name.contains("TWO_WILD_DRAW")) {
            return "+2";
        } else if (name.equals("FOUR_WILD_DRAW")) {
            return "+4";
        } else if (name.equals("WILD")) {
            return "WILD";
        } else if (name.contains("SKIP")) {
            return "SKIP";
        } else {
            return null;
        }
    }

    /**
     * Extracts the color from a card's name.
     *
     * @param name the name of the card
     * @return the color of the card as a string ("GREEN", "YELLOW", "BLUE", "RED") if the name contains any one of these colors, otherwise returns "NON_COLOR"
     */
    private String getCardColor(String name){
        if(name.contains("GREEN")){
            return "GREEN";
        } else if(name.contains("YELLOW")){
            return "YELLOW";
        } else if(name.contains("BLUE")){
            return "BLUE";
        } else if(name.contains("RED")){
            return "RED";
        } else {
            return "NON_COLOR";
        }
    }

    /**
     * Takes a card from the top of the deck.
     *
     * @return the card from the top of the deck
     * @throws IllegalStateException if the deck is empty
     */
    public Card takeCard() {
        if (deckOfCards.isEmpty()) {
            throw new IllegalStateException("Deck is empty. Wait for it to be refilled.");
        }
        return deckOfCards.pop();
    }

    /**
     * Returns the number of cards in the deck.
     *
     * @return the number of cards in the deck
     */
    public int size(){
        return deckOfCards.size();
    }

    /**
     * Checks if the deck is empty.
     *
     * @return true if the deck is empty, false otherwise.
     */
    public boolean isEmpty() {
        return deckOfCards.isEmpty();
    }

    /**
     * Refills the deck on the board with the given cards and shuffles the deck.
     *
     * @param allCardsInTableButLastOne the list of cards to refill the deck, excluding the last card on the table.
     */
    public void refillDeck(ArrayList<Card> allCardsInTableButLastOne) {
        for (Card card : allCardsInTableButLastOne) {
            deckOfCards.push(card);
        }
        Collections.shuffle(deckOfCards);
    }
}
