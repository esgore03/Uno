package org.example.eiscuno.model.game;

import javafx.stage.Stage;
import org.example.eiscuno.controller.GameUnoController;
import org.example.eiscuno.model.deck.Deck;
import org.example.eiscuno.model.observer.EventManager;
import org.example.eiscuno.model.player.Player;
import org.example.eiscuno.model.table.Table;
import org.junit.jupiter.api.Test;
import org.testfx.framework.junit5.ApplicationTest;

import static org.junit.jupiter.api.Assertions.*;

public class GameUnoTest extends ApplicationTest{
    @Override
        public void start(Stage stage){
    }

    @Test
    void wildCardShouldLeaveTheColorOfTheCardBefore(){
        var humanPlayer = new Player("HUMAN_PLAYER");
        var machinePlayer = new Player("MACHINE_PLAYER");
        var deck = new Deck();
        var table = new Table();
        var eventManager = new EventManager();
        var gameUnoController = new GameUnoController();
        var gameUno = new GameUno(gameUnoController, eventManager, humanPlayer, machinePlayer, deck, table);

        boolean isRedCardPut = false;
        while (!isRedCardPut){
            var card = deck.takeCard();
            if(card.getColor().equals("RED")){
                table.setStartCard(card);
                isRedCardPut = true;
            }
        }

        boolean isWildCardPut = false;
        while (!isWildCardPut){
            var card = deck.takeCard();
            if(card.getValue().equals("WILD")){
                table.addCardOnTheTable(card);
                isWildCardPut = true;
            }
        }

        assertEquals("RED", gameUno.getCurrentTableCardColor());
    }
}