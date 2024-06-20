package org.example.eiscuno.model.game;

import org.example.eiscuno.model.card.Card;
import org.example.eiscuno.model.deck.Deck;
import org.example.eiscuno.model.exception.UnoException;
import org.example.eiscuno.model.observer.EventManager;
import org.example.eiscuno.model.player.Player;
import org.example.eiscuno.model.table.Table;

import java.util.ArrayList;

public class GameUno implements IGameUno {
    private EventManager eventManager;
    private Player humanPlayer;
    private Player machinePlayer;
    private Deck deck;
    private Table table;
    private String currentColor;

    public GameUno(EventManager eventManager, Player humanPlayer, Player machinePlayer, Deck deck, Table table) {
        this.eventManager = eventManager;
        this.humanPlayer = humanPlayer;
        this.machinePlayer = machinePlayer;
        this.deck = deck;
        this.table = table;
        this.currentColor = null; // Inicializar currentColor a null
    }

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

    @Override
    public void eatCard(Player player, int numberOfCards) {
        for (int i = 0; i < numberOfCards; i++) {
            player.addCard(this.deck.takeCard());
        }
    }

    @Override
    public void playCard(Card card, String playerWhoPlays) throws UnoException {
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

    private boolean cardCanBePlayed(Card card, Card currentCard) {
        return card.getColor().equals("NON_COLOR") || card.getValue().equals(currentCard.getValue())
                || card.getColor().equals(currentColor != null ? currentColor : currentCard.getColor());
    }

    private void applySpecialCardEffect(Card card, String playerWhoPlays) throws UnoException {
        Player currentPlayer = playerWhoPlays.equals("HUMAN_PLAYER") ? humanPlayer : machinePlayer;
        Player nextPlayer = playerWhoPlays.equals("HUMAN_PLAYER") ? machinePlayer : humanPlayer;

        switch (card.getValue()) {
            case "+2":
                eatCard(nextPlayer, 2);
                break;
            case "+4":
                eatCard(nextPlayer, 4);
                break;
            case "SKIP":
                // No action needed, next player loses turn
                break;
            case "REVERSE":
                // No action needed for 2-player game
                break;
            default:
                break;
        }
        if (card.isWildCard()) {
            // Reset current color as the wild card color will be chosen
            setCurrentColor(null);
        } else {
            setCurrentColor(card.getColor());
        }
    }

    @Override
    public void haveSungOne(String playerWhoSang) {
        if (playerWhoSang.equals("HUMAN_PLAYER")) {
            if (machinePlayer.getCardsPlayer().size() == 1) {
                machinePlayer.addCard(this.deck.takeCard());
                eventManager.notifyListenersCardsMachinePlayerUpdate();
            } else {
                System.out.println("Can't sing UNO.");
            }
        } else {
            humanPlayer.addCard(this.deck.takeCard());
            eventManager.notifyListenersCardsHumanPlayerUpdate();
        }
    }

    public void takeCard(String playerWhoTakes) {
        if (playerWhoTakes.equals("HUMAN_PLAYER")) {
            humanPlayer.addCard(this.deck.takeCard());
        } else {
            machinePlayer.addCard(this.deck.takeCard());
        }
    }

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

    @Override
    public Boolean isGameOver() {
        return humanPlayer.getCardsPlayer().isEmpty() || machinePlayer.getCardsPlayer().isEmpty();
    }

    public void refillDeckOfCards() {
        System.out.println("Deck has been refilled.");
        ArrayList<Card> allCardsInTable = table.getCardsTable();
        ArrayList<Card> allCardsInTableButLastOne = new ArrayList<>(allCardsInTable.subList(0, allCardsInTable.size() - 1));
        deck.refillDeck(allCardsInTableButLastOne);
    }

    public Deck getDeck() {
        return deck;
    }

    public String getCurrentColor() {
        return currentColor;
    }

    public void setCurrentColor(String color) {
        this.currentColor = color;
    }
}
