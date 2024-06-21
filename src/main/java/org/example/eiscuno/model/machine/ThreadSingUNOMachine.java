package org.example.eiscuno.model.machine;

import org.example.eiscuno.model.card.Card;
import org.example.eiscuno.model.game.GameUno;
import org.example.eiscuno.model.player.Player;

import java.util.ArrayList;

/**
 * This class represents a task that checks if the machine player should sing "UNO".
 * It implements the {@link Runnable} interface and is intended to be executed by a thread.
 */
public class ThreadSingUNOMachine implements Runnable {
    private ArrayList<Card> cardsPlayer;
    private ArrayList<Card> cardsMachine;
    private Player machinePlayer;
    private GameUno gameUno;

    /**
     * Constructs a new ThreadSingUNOMachine.
     *
     * @param cardsPlayer the human player cards
     * @param machinePlayer the machine player
     * @param gameUno     the Uno game instance
     */
    public ThreadSingUNOMachine(ArrayList<Card> cardsPlayer, Player machinePlayer  , GameUno gameUno) {
        this.gameUno = gameUno;
        this.machinePlayer = machinePlayer;
        this.cardsPlayer = cardsPlayer;
        this.cardsMachine = machinePlayer.getCardsPlayer();
    }
    /**
     * Runs the thread, continually checking if the machine player should sing "UNO".
     * <p>
     * The process repeats indefinitely:
     * <ul>
     *     <li>Waits for a random duration between 0 and 5 seconds.</li>
     *     <li>Checks if the machine player has only one card left.</li>
     *     <li>If the machine player has one card, it sings "UNO" and notifies the game instance.</li>
     * </ul>
     */
    @Override
    public void run() {
        while (true) {
            try {
                Thread.sleep((long) (Math.random() * 5000));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            hasOneCardTheMachinePlayer();
            hasOneCardTheHumanPlayer();
        }
    }

    /**
     * Checks if the human player has only one card left.
     * <p>
     * If the human player has one card, it simulates the machine singing "UNO"
     * and notifies the game instance.
     */
    private void hasOneCardTheHumanPlayer() {
        if (cardsPlayer.size() == 1) {
            System.out.println("Machine says: UNO");
            gameUno.haveSungOne("MACHINE_PLAYER");
        }
    }

    /**
     * Checks if the machine player has only one card left.
     * <p>
     * If the machine player has one card, it simulates the machine singing "UNO" to protect itself
     * and notifies the game instance.
     */
    private void hasOneCardTheMachinePlayer() {
        if (cardsMachine.size() == 1) {
            System.out.println("Machine protects itself from UNO");
            this.machinePlayer.setProtectedByUno(true);
        }
    }
}
