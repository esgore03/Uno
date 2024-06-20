package org.example.eiscuno.model.game;

import org.example.eiscuno.model.card.Card;
import org.example.eiscuno.model.deck.Deck;
import org.example.eiscuno.model.exception.UnoException;
import org.example.eiscuno.model.observer.EventManager;
import org.example.eiscuno.model.player.Player;
import org.example.eiscuno.model.table.Table;

import java.util.ArrayList;

/**
 * Represents a game of Uno.
 * This class manages the game logic and interactions between players, deck, and the table.
 */
public class GameUno implements IGameUno {
    private EventManager eventManager;
    private Player humanPlayer;
    private Player machinePlayer;
    private Deck deck;
    private Table table;

    /**
     * Constructs a new GameUno instance.
     *
     * @param humanPlayer   The human player participating in the game.
     * @param machinePlayer The machine player participating in the game.
     * @param deck          The deck of cards used in the game.
     * @param table         The table where cards are placed during the game.
     */
    public GameUno(EventManager eventManager, Player humanPlayer, Player machinePlayer, Deck deck, Table table) {
        this.eventManager = eventManager;
        this.humanPlayer = humanPlayer;
        this.machinePlayer = machinePlayer;
        this.deck = deck;
        this.table = table;
    }

    /**
     * Starts the Uno game by distributing cards to players.
     * The human player and the machine player each receive 10 cards from the deck.
     */
    @Override
    public void startGame() {
        for (int i = 0; i < 10; i++) {
            if (i < 5) {
                humanPlayer.addCard(this.deck.takeCard());
            } else {
                machinePlayer.addCard(this.deck.takeCard());
            }
        }
    }

    /**
     * Allows a player to draw a specified number of cards from the deck.
     *
     * @param player        The player who will draw cards.
     * @param numberOfCards The number of cards to draw.
     */
    @Override
    public void eatCard(Player player, int numberOfCards) {
        for (int i = 0; i < numberOfCards; i++) {
            player.addCard(this.deck.takeCard());
        }
    }

    /**
     * Places a card on the table during the game.
     *
     * @param card The card to be placed on the table.
     * @param playerWhoPlays The player who is playing the card.
     *
     */
    @Override
    public void playCard(Card card, String playerWhoPlays) throws UnoException {
        if (table.isEmpty()) {
            this.table.addCardOnTheTable(card);
            applySpecialCardEffect(card, playerWhoPlays);
        }
        else {
            Card currentCard = this.table.getCurrentCardOnTheTable();
            if (cardCanBePlayed(card, currentCard)) {
                this.table.addCardOnTheTable(card);
                applySpecialCardEffect(card, playerWhoPlays);
            } else {
                throw new UnoException("Can't place this card");
            }
        }
    }

    /**
     * Checks if a card can be played.
     *
     * @param card The card to be played.
     * @param currentCard The current card on the table.
     *
     */
    private boolean cardCanBePlayed(Card card, Card currentCard) {
        return card.getColor().equals("NON_COLOR") || card.getValue().equals(currentCard.getValue())
                || card.getColor().equals(currentCard.getColor());
    }

    /**
     * Applies the effect of a special card.
     *
     * @param card The card being played.
     * @param playerWhoPlays The player who played the card.
     * @throws UnoException if an error occurs while applying the card effect.
     */
    private void applySpecialCardEffect(Card card, String playerWhoPlays) throws UnoException {
        Player currentPlayer = playerWhoPlays.equals("HUMAN_PLAYER") ? humanPlayer : machinePlayer;
        Player nextPlayer = playerWhoPlays.equals("HUMAN_PLAYER") ? machinePlayer : humanPlayer;

        switch (card.getValue()) {
            case "+2":
                eatCard(nextPlayer, 2);
                // El siguiente jugador pierde su turno
                break;
            case "+4":
                eatCard(nextPlayer, 4);
                // El siguiente jugador pierde su turno
                // Aquí podrías agregar lógica para cambiar el color si lo deseas
                break;
            case "SKIP":
                // El siguiente jugador pierde su turno
                break;
            case "REVERSE":
                // Invertir el orden del juego
                // Aquí podrías agregar lógica para invertir el orden del juego
                break;
            default:
                break;
        }
    }

    /**
     * Handles the scenario when a player shouts "Uno", forcing the other player to draw a card.
     *
     * @param playerWhoSang The player who shouted "Uno".
     */
    @Override
    public void haveSungOne(String playerWhoSang) {
        if (playerWhoSang.equals("HUMAN_PLAYER")) {
            if(machinePlayer.getCardsPlayer().size() == 1) {
                machinePlayer.addCard(this.deck.takeCard());
                eventManager.notifyListenersCardsMachinePlayerUpdate();
            }
            else{
                System.out.println("Can't sing UNO.");
            }
        } else {
            humanPlayer.addCard(this.deck.takeCard());
            eventManager.notifyListenersCardsHumanPlayerUpdate();
        }
    }

    /**
     * Gives a card to the player who is taking it.
     *
     * @param playerWhoTakes The player who is taking the card.
     */
    public void takeCard(String playerWhoTakes){
        if (playerWhoTakes.equals("HUMAN_PLAYER")) {
            humanPlayer.addCard(this.deck.takeCard());
        }
        else{
            machinePlayer.addCard(this.deck.takeCard());
        }
    }

    /**
     * Retrieves the current visible cards of the human player starting from a specific position.
     *
     * @param posInitCardToShow The initial position of the cards to show.
     * @return An array of cards visible to the human player.
     */
    @Override
    public Card[] getCurrentVisibleCardsHumanPlayer(int posInitCardToShow) {
        int totalCards = this.humanPlayer.getCardsPlayer().size();
        int numVisibleCards = Math.min(4, totalCards - posInitCardToShow);
        Card[] cards = new Card[numVisibleCards];

        for (int i = 0; i < numVisibleCards; i++) {
            cards[i] = this.humanPlayer.getCard(posInitCardToShow + i);
        }

        return cards;
    }

    /**
     * Retrieves the current visible cards of the machine player starting from a specific position.
     *
     * @return An array of cards visible to the human player.
     */
    @Override
    public Card[] getCurrentVisibleCardsMachinePlayer() {
        int totalCards = this.machinePlayer.getCardsPlayer().size();
        int numVisibleCards = Math.min(4, totalCards);
        Card[] cards = new Card[numVisibleCards];

        for (int i = 0; i < numVisibleCards; i++) {
            cards[i] = this.machinePlayer.getCard(i);
        }

        return cards;
    }

    /**
     * Checks if the game is over.
     *
     * @return True if any of the player's have no cards left.
     */
    @Override
    public Boolean isGameOver() {
        return humanPlayer.getCardsPlayer().isEmpty() || machinePlayer.getCardsPlayer().isEmpty();
    }

    /**
     * Refills the deck of cards used for taking cards from the table.
     * <p>
     * This method retrieves all cards from the table except the last one,
     * and then refills the deck with these cards.
     * </p>
     */
    public void refillDeckOfCards() {
        System.out.println("Deck has been refilled.");
        ArrayList<Card> allCardsInTable = table.getCardsTable();
        ArrayList<Card> allCardsInTableButLastOne = new ArrayList<>(allCardsInTable.subList(0, allCardsInTable.size() - 1));
        deck.refillDeck(allCardsInTableButLastOne);
    }

    /**
     * Retrieves the deck of the game.
     *
     * @return An instance of the Deck class representing the current deck of the game.
     */
    public Deck getDeck() {
        return deck;
    }
}
