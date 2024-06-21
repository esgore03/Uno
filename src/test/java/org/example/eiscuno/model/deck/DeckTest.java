package org.example.eiscuno.model.deck;

import javafx.application.Platform;
import javafx.stage.Stage;
import org.example.eiscuno.model.table.Table;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testfx.framework.junit5.ApplicationTest;

import java.util.concurrent.CountDownLatch;

import static org.junit.jupiter.api.Assertions.*;

/**
 * This class contains unit tests for the Deck class.
 * It verifies the correct behavior of the deck and table functionalities.
 */
class DeckTest extends ApplicationTest{

    private static Deck deck;
    private static Table table;

    /**
     * Initializes the JavaFX runtime.
     * This method is executed once before any of the test methods in this class.
     */
    @BeforeAll
    static void initJFX() throws Exception {
        CountDownLatch latch = new CountDownLatch(1);
        Platform.startup(latch::countDown);
        latch.await();
    }

    /**
     * Sets up the initial conditions before each test.
     * Initializes the deck and table objects.
     */
    @BeforeEach
    void setup() {
        deck = new Deck();
        table = new Table();
    }

    /**
     * Tests that the deck is emptied correctly by drawing all cards.
     * It ensures that after drawing all cards, the deck size is zero.
     */
    @Test
    void deckShouldBeEmpty() {
        var deckSize = deck.size();
        for (int i = 0; i < deckSize; i++) {
            table.addCardOnTheTable(deck.takeCard());
        }

        assertEquals(0, deck.size());

        deck.refillDeck(table.getCardsTable());
    }

    /**
     * Tests that the deck is refilled correctly after being emptied.
     * It ensures that the deck size is restored to 54 cards.
     */
    @Test
    void deckShouldBeRefilled() {
        assertEquals(54, deck.size());
    }
}