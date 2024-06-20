package org.example.eiscuno.model.machine;

import org.example.eiscuno.model.game.GameUno;

/**
 * This class represents a thread responsible for refilling the deck of cards in a Uno game.
 * It extends the {@link Thread} class and continually checks if the deck is empty,
 * refilling it as necessary.
 */
public class ThreadRefillDeck extends Thread {
    private GameUno gameUno;

    /**
     * Constructs a new ThreadRefillDeck.
     *
     * @param gameUno the Uno game instance
     */
    public ThreadRefillDeck(GameUno gameUno) {
        this.gameUno = gameUno;
    }

    /**
     * Runs the thread, continually checking if the deck is empty and refilling it if necessary.
     * <p>
     * The process repeats indefinitely:
     * <ul>
     *     <li>Checks if the deck is empty.</li>
     *     <li>If empty, refills the deck of cards.</li>
     *     <li>Waits for 2 seconds before checking again.</li>
     * </ul>
     * If interrupted during sleep, the thread throws a {@link RuntimeException}.
     */
    @Override
    public void run() {
        while (true) {
            if (gameUno.getDeck().isEmpty()) {
                gameUno.refillDeckOfCards();
            }
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}