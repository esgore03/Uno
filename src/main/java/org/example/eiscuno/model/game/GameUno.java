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
 * Manages the game logic including players, deck, table, and game events.
 */
public class GameUno implements IGameUno {
    private EventManager eventManager; // Manages game events
    private Player humanPlayer; // Human player in the game
    private Player machinePlayer; // AI player in the game
    private Deck deck; // Deck of Uno cards
    private Table table; // Table where cards are placed during the game
    private String currentTableCardColor; // Current color of the top card on the table

    /**
     * Constructs a new instance of GameUno with the specified parameters.
     *
     * @param eventManager  the event manager for managing game events
     * @param humanPlayer   the human player in the game
     * @param machinePlayer the AI player in the game
     * @param deck          the deck of Uno cards
     * @param table         the table where cards are placed during the game
     */
    public GameUno(EventManager eventManager, Player humanPlayer, Player machinePlayer, Deck deck, Table table) {
        this.eventManager = eventManager;
        this.humanPlayer = humanPlayer;
        this.machinePlayer = machinePlayer;
        this.deck = deck;
        this.table = table;
        currentTableCardColor = null;
    }

    /**
     * Starts the Uno game.
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
     * Distributes cards from the deck to a player.
     *
     * @param playerWhoEats the player who receives the cards (either "MACHINE_PLAYER" or "HUMAN_PLAYER")
     * @param numberOfCards the number of cards to distribute
     */
    public void eatCard(String playerWhoEats, int numberOfCards) {
        if (deck.size() < numberOfCards) {
            refillDeckOfCards();
        }

        if (playerWhoEats.equals("MACHINE_PLAYER")) {
            for (int i = 0; i < numberOfCards; i++) {
                machinePlayer.addCard(this.deck.takeCard());
            }
        } else {
            for (int i = 0; i < numberOfCards; i++) {
                humanPlayer.addCard(this.deck.takeCard());
            }
        }
    }

    /**
     * Plays a card in the game, adding it to the table.
     *
     * @param card the card to be played
     */
    @Override
    public void playCard(Card card, String playerWhoPlays) throws UnoException {
        if (!didHumanWin() && !didMachineWin()) {
            if (table.isEmpty()) {
                this.table.addCardOnTheTable(card);
                applySpecialCardEffect(card, playerWhoPlays);
            } else {
                Card currentCard = this.table.getCurrentCardOnTheTable();
                if (cardCanBePlayed(card, currentCard)) {
                    this.table.addCardOnTheTable(card);
                    applySpecialCardEffect(card, playerWhoPlays);
                } else {
                    throw new UnoException("Can't place this card");
                }
            }
        }
    }

    /**
     * Checks if a card can be played.
     *
     * @param card        the card to be played
     * @param currentCard the current card on the table
     * @return true if the card can be played, false otherwise
     */
    private boolean cardCanBePlayed(Card card, Card currentCard) {
        return card.getColor().equals("NON_COLOR") || currentTableCardColor.equals("NON_COLOR") || card.getValue().equals(currentCard.getValue())
                || card.getColor().equals(currentTableCardColor != null ? currentTableCardColor : currentCard.getColor());
    }

    /**
     * Applies the special effects of playing a card and updates the game state accordingly.
     *
     * @param card           the card being played
     * @param playerWhoPlays the player who is playing the card (either "HUMAN_PLAYER" or "MACHINE_PLAYER")
     * @throws UnoException if an error occurs during card play
     */
    private void applySpecialCardEffect(Card card, String playerWhoPlays) throws UnoException {
        String playerWhoEats;

        if (playerWhoPlays.equals("HUMAN_PLAYER")) {
            playerWhoEats = "MACHINE_PLAYER";

            switch (card.getValue()) {
                case "+2":
                    currentTableCardColor = card.getColor();
                    eatCard(playerWhoEats, 2);
                    eventManager.notifyListenersPlayerTurnUpdate(true);
                    eventManager.notifyListenersCardsMachinePlayerUpdate();
                    break;
                case "+4":
                    currentTableCardColor = card.getColor();
                    eatCard(playerWhoEats, 4);
                    eventManager.notifyListenersPlayerTurnUpdate(true);
                    eventManager.notifyListenersCardsMachinePlayerUpdate();
                    break;
                case "SKIP", "REVERSE":
                    currentTableCardColor = card.getColor();
                    eventManager.notifyListenersPlayerTurnUpdate(false);
                    break;
                case "WILD":
                    eventManager.notifyListenersPlayerTurnUpdate(true);
                    break;
                default:
                    currentTableCardColor = card.getColor();
                    eventManager.notifyListenersPlayerTurnUpdate(true);
                    break;
            }
        } else {
            playerWhoEats = "HUMAN_PLAYER";

            switch (card.getValue()) {
                case "+2":
                    currentTableCardColor = card.getColor();
                    eatCard(playerWhoEats, 2);
                    eventManager.notifyListenersPlayerTurnUpdate(false);
                    eventManager.notifyListenersCardsHumanPlayerUpdate();
                    break;
                case "+4":
                    currentTableCardColor = card.getColor();
                    eatCard(playerWhoEats, 4);
                    eventManager.notifyListenersPlayerTurnUpdate(false);
                    eventManager.notifyListenersCardsHumanPlayerUpdate();
                    break;
                case "SKIP", "REVERSE":
                    currentTableCardColor = card.getColor();
                    eventManager.notifyListenersPlayerTurnUpdate(true);
                    break;
                case "WILD":
                    eventManager.notifyListenersPlayerTurnUpdate(false);
                    break;
                default:
                    currentTableCardColor = card.getColor();
                    eventManager.notifyListenersPlayerTurnUpdate(false);
                    break;
            }
        }
    }

    /**
     * Handles the action when a player shouts "Uno".
     *
     * @param playerWhoSang the identifier of the player who shouted "Uno"
     */
    @Override
    public void haveSungOne(String playerWhoSang) {
        boolean machinePlayerProtectedByUno = this.machinePlayer.isProtectedByUno();
        boolean humanPlayerProtectedByUno = this.humanPlayer.isProtectedByUno();

        if (playerWhoSang.equals("HUMAN_PLAYER")) {
            if (machinePlayer.getCardsPlayer().size() == 1 && !machinePlayerProtectedByUno) {
                machinePlayer.addCard(this.deck.takeCard());
                eventManager.notifyListenersCardsMachinePlayerUpdate();
            } else if (humanPlayer.getCardsPlayer().size() == 1) {
                this.humanPlayer.setProtectedByUno(true);
            } else {
                System.out.println("Can't sing UNO.");
            }
        } else {
            if (!humanPlayerProtectedByUno) {
                humanPlayer.addCard(this.deck.takeCard());
                eventManager.notifyListenersCardsHumanPlayerUpdate();
            }
        }
    }

    /**
     * Takes a card from the deck and adds it to the specified player's hand.
     * Also resets the Uno protection status of the player.
     *
     * @param playerWhoTakes the player who is taking the card ("HUMAN_PLAYER" or "MACHINE_PLAYER")
     */
    public void takeCardPlayer(String playerWhoTakes) {
        if (playerWhoTakes.equals("HUMAN_PLAYER")) {
            this.humanPlayer.addCard(this.deck.takeCard());
            this.humanPlayer.setProtectedByUno(false);
        } else {
            this.machinePlayer.addCard(this.deck.takeCard());
            this.machinePlayer.setProtectedByUno(false);
        }
    }

    /**
     * Retrieves the current visible cards of the human player starting from a specific position.
     *
     * @param posInitCardToShow the starting position of the cards to be shown
     * @return an array of cards that are currently visible to the human player
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
     * Retrieves the current visible cards of the machine player.
     *
     * @return an array of cards that are currently visible to the human player
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
     * Checks if the Human Player won.
     *
     * @return true if he did, false otherwise
     */
    @Override
    public Boolean didHumanWin() {
        return humanPlayer.getCardsPlayer().isEmpty();
    }

    /**
     * Checks if the Machine Player won.
     *
     * @return true if he did, false otherwise
     */
    @Override
    public Boolean didMachineWin() {
        return machinePlayer.getCardsPlayer().isEmpty();
    }

    /**
     * Refills the deck of cards by adding all cards from the table back into the deck.
     * This method is called when the deck runs out of cards during gameplay.
     */
    public void refillDeckOfCards() {
        System.out.println("Deck has been refilled.");
        ArrayList<Card> allCardsInTable = table.getCardsTable();
        ArrayList<Card> allCardsInTableButLastOne = new ArrayList<>(allCardsInTable.subList(0, allCardsInTable.size() - 1));
        deck.refillDeck(allCardsInTableButLastOne);
    }

    /**
     * Gets the deck of cards used in the game.
     *
     * @return the deck of cards
     */
    public Deck getDeck() {
        return deck;
    }

}
