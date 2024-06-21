package org.example.eiscuno.model.game;

import org.example.eiscuno.SelectColorDialog;
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
    private String currentTableCardColor;

    public GameUno(EventManager eventManager, Player humanPlayer, Player machinePlayer, Deck deck, Table table) {
        this.eventManager = eventManager;
        this.humanPlayer = humanPlayer;
        this.machinePlayer = machinePlayer;
        this.deck = deck;
        this.table = table;
        currentTableCardColor = null;
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

    public void eatCard(String playerWhoEats, int numberOfCards) {
        if(deck.size() < numberOfCards){
            refillDeckOfCards();
        }

        if(playerWhoEats.equals("MACHINE_PLAYER")){
            for (int i = 0; i < numberOfCards; i++) {
                machinePlayer.addCard(this.deck.takeCard());
            }
        }
        else{
            for (int i = 0; i < numberOfCards; i++) {
                humanPlayer.addCard(this.deck.takeCard());
            }
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
                || card.getColor().equals(currentTableCardColor != null ? currentTableCardColor : currentCard.getColor());
    }

    private void applySpecialCardEffect(Card card, String playerWhoPlays) throws UnoException {
        String playerWhoEats;

        if (playerWhoPlays.equals("HUMAN_PLAYER")){
            playerWhoEats = "MACHINE_PLAYER";

            switch (card.getValue()) {
                case "+2":
                    currentTableCardColor = card.getColor();
                    eatCard(playerWhoEats, 2);
                    eventManager.notifyListenersPlayerTurnUpdate(true);
                    break;
                case "+4":
                    currentTableCardColor = SelectColorDialog.display();
                    eatCard(playerWhoEats, 4);
                    eventManager.notifyListenersPlayerTurnUpdate(true);
                    break;
                case "SKIP", "REVERSE":
                    currentTableCardColor = card.getColor();
                    eventManager.notifyListenersPlayerTurnUpdate(false);
                    break;
                case "WILD":
                    currentTableCardColor = SelectColorDialog.display();
                    eventManager.notifyListenersPlayerTurnUpdate(true);
                    break;
                default:
                    currentTableCardColor = card.getColor();
                    eventManager.notifyListenersPlayerTurnUpdate(true);
                    break;
            }
        }
        else{
            playerWhoEats = "HUMAN_PLAYER";

            switch (card.getValue()) {
                case "+2":
                    currentTableCardColor = card.getColor();
                    eatCard(playerWhoEats, 2);
                    eventManager.notifyListenersPlayerTurnUpdate(false);

                    break;
                case "+4":
                    currentTableCardColor = SelectColorDialog.display();
                    eatCard(playerWhoEats, 4);
                    eventManager.notifyListenersPlayerTurnUpdate(false);
                    break;
                case "SKIP", "REVERSE":
                    currentTableCardColor = card.getColor();
                    eventManager.notifyListenersPlayerTurnUpdate(true);
                    break;
                case "WILD":
                    currentTableCardColor = SelectColorDialog.display();
                    eventManager.notifyListenersPlayerTurnUpdate(false);
                    break;
                default:
                    currentTableCardColor = card.getColor();
                    eventManager.notifyListenersPlayerTurnUpdate(false);
                    break;
            }
        }
    }

    @Override
    public void haveSungOne(String playerWhoSang) {
        boolean machinePlayerProtectedByUno = this.machinePlayer.isProtectedByUno();
        boolean humanPlayerProtectedByUno = this.humanPlayer.isProtectedByUno();

        if (playerWhoSang.equals("HUMAN_PLAYER")) {
            if (machinePlayer.getCardsPlayer().size() == 1 && !machinePlayerProtectedByUno) {
                machinePlayer.addCard(this.deck.takeCard());
                eventManager.notifyListenersCardsMachinePlayerUpdate();
            }
            else if (humanPlayer.getCardsPlayer().size() == 1){
                this.humanPlayer.setProtectedByUno(true);
            }
            else {
                System.out.println("Can't sing UNO.");
            }
        } else {
            if(!humanPlayerProtectedByUno){
                humanPlayer.addCard(this.deck.takeCard());
                eventManager.notifyListenersCardsHumanPlayerUpdate();
            }
        }
    }

    public void takeCardPlayer(String playerWhoTakes) {
        if (playerWhoTakes.equals("HUMAN_PLAYER")) {
            this.humanPlayer.addCard(this.deck.takeCard());
            this.humanPlayer.setProtectedByUno(false);
        } else {
            this.machinePlayer.addCard(this.deck.takeCard());
            this.machinePlayer.setProtectedByUno(false);
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

    public String getCurrentTableCardColor() {
        return currentTableCardColor;
    }

    public void setCurrentTableCardColor(String color) {
        this.currentTableCardColor = color;
    }
}
