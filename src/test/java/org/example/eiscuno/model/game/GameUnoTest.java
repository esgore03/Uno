package org.example.eiscuno.model.game;

import javafx.stage.Stage;
import org.example.eiscuno.controller.GameUnoController;
import org.example.eiscuno.model.card.Card;
import org.example.eiscuno.model.deck.Deck;
import org.example.eiscuno.model.exception.UnoException;
import org.example.eiscuno.model.observer.EventManager;
import org.example.eiscuno.model.player.Player;
import org.example.eiscuno.model.table.Table;
import org.junit.jupiter.api.Test;
import org.testfx.framework.junit5.ApplicationTest;

import static org.junit.jupiter.api.Assertions.*;

public class GameUnoTest extends ApplicationTest implements IGameUno{
    @Override
        public void start(Stage stage){
    }

    @Test
    void deckShouldBeRefilled(){
        var humanPlayer = new Player("HUMAN_PLAYER");
        var machinePlayer = new Player("MACHINE_PLAYER");
        var deck = new Deck();
        var table = new Table();
        var eventManager = new EventManager();
        var gameUnoController = new GameUnoController();
        var gameUno = new GameUno(gameUnoController, eventManager, humanPlayer, machinePlayer, deck, table);
        for(int i = 0; i < deck.size(); i++){
            table.addCardOnTheTable(deck.takeCard());
            System.out.println(deck.size());
        }

        assertEquals(0, deck.size());

        deck.refillDeck(table.getCardsTable());

        assertEquals(10, deck.size());
    }

    @Override
    public void startGame() {
    }

    @Override
    public void playCard(Card card, String playerWhoPlays) throws UnoException {
    }

    @Override
    public void haveSungOne(String playerWhoSang) {
    }

    @Override
    public Card[] getCurrentVisibleCardsHumanPlayer(int posInitCardToShow) {
        return new Card[0];
    }

    @Override
    public Card[] getCurrentVisibleCardsMachinePlayer() {
        return new Card[0];
    }

    @Override
    public Boolean didHumanWin() {
        return null;
    }

    @Override
    public Boolean didMachineWin() {
        return null;
    }
}