package org.example.eiscuno.model.machine;

import javafx.scene.image.ImageView;
import org.example.eiscuno.model.exception.UnoException;
import org.example.eiscuno.model.observer.EventManager;
import org.example.eiscuno.model.card.Card;
import org.example.eiscuno.model.game.GameUno;
import org.example.eiscuno.model.player.Player;

import java.util.ArrayList;
import java.util.Random;

/**
 * This class represents a thread that handles the actions of an AI player in a Uno game.
 * It extends the {@link Thread} class and simulates the moves of the machine player.
 */
public class ThreadPlayMachine extends Thread {
    private EventManager eventManager;
    private GameUno gameUno;
    private Player machinePlayer;
    private ImageView tableImageView;
    private volatile boolean hasPlayerPlayed;

    /**
     * Constructs a new ThreadPlayMachine.
     *
     * @param eventManager    the event manager to handle game events
     * @param gameUno         the Uno game instance
     * @param machinePlayer   the machine player controlled by this thread
     * @param tableImageView  the image view representing the game table
     */
    public ThreadPlayMachine(EventManager eventManager, GameUno gameUno, Player machinePlayer, ImageView tableImageView) {
        this.eventManager = eventManager;
        this.gameUno = gameUno;
        this.machinePlayer = machinePlayer;
        this.tableImageView = tableImageView;
        this.hasPlayerPlayed = false;
    }

    /**
     * Runs the thread, simulating the machine player's turn in the Uno game.
     * The machine player will either take a card or put a card on the table
     * with a probability of 20% to take a card. The thread sleeps for 2 seconds
     * between actions.
     * <p>
     * The process repeats indefinitely:
     * <ul>
     *     <li>Checks if the player has played.</li>
     *     <li>If true, waits for 2 seconds.</li>
     *     <li>Generates a random number to decide whether to take a card or put a card on the table.</li>
     *     <li>Performs the corresponding action (take a card or put a card on the table).</li>
     *     <li>Updates the player's turn status and notifies the event manager.</li>
     * </ul>
     */
    @Override
    public void run() {
        while (true) {
            if (hasPlayerPlayed) {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Random random = new Random();
                double probability = 0.2; // Probability of a 20% chance it occurs.
                boolean takeCard = random.nextDouble() < probability;

                if (takeCard) {
                    machineTakeCard();
                } else {
                    putCardOnTheTable();
                }

                this.machinePlayer.setProtectedByUno(false);
                eventManager.notifyListenersCardsMachinePlayerUpdate();
            }
        }
    }

    /**
     * Simulates the machine player placing a card on the table.
     * <p>
     * The method attempts to place a card from the machine player's hand onto the table:
     * <ul>
     *     <li>Iterates through the machine player's cards.</li>
     *     <li>Tries to play each card.</li>
     *     <li>If a card is successfully played, it removes the card from the player's hand, updates the table's image, and exits the loop.</li>
     *     <li>If no valid card is found to play, the machine player takes a card.</li>
     * </ul>
     * If a card cannot be placed due to an {@link UnoException}, it continues to the next card.
     */
    private void putCardOnTheTable() {
        ArrayList<Card> cardsMachinePlayer = machinePlayer.getCardsPlayer();
        boolean validPlacement = false;

        for (int cardIndex = 0; cardIndex < cardsMachinePlayer.size(); cardIndex++) {
            try {
                Card card = machinePlayer.getCardsPlayer().get(cardIndex);
                gameUno.playCard(card, "MACHINE_PLAYER");
                System.out.println("Machine player placed a card.");
                machinePlayer.removeCard(cardIndex);
                tableImageView.setImage(card.getImage());
                validPlacement = true;
                break;
            } catch (UnoException ignored) {
            }
        }

        if (!validPlacement) {
            machineTakeCard();
        }
    }

    /**
     * Simulates the machine player taking a card from the deck.
     * <p>
     * The method attempts to take a card from the deck until successful:
     * <ul>
     *     <li>Continues trying to take a card while {@code cardTaken} is false.</li>
     *     <li>Logs a message when a card is successfully taken.</li>
     *     <li>Ignores any {@link IllegalStateException} that occurs, indicating the deck is temporarily unavailable.</li>
     * </ul>
     */
    public void machineTakeCard() {
        boolean cardTaken = false;

        while (!cardTaken) {
            try {
                gameUno.takeCardPlayer("MACHINE_PLAYER");
                System.out.println("Machine player took a card.");
                hasPlayerPlayed = false;
                eventManager.notifyListenersPlayerTurnUpdate(hasPlayerPlayed);
                cardTaken = true;
            } catch (IllegalStateException ignored) {
            }
        }
    }

    /**
     * Sets the flag indicating whether the player has played.
     *
     * @param hasPlayerPlayed {@code true} if the player has played, {@code false} otherwise.
     */
    public void setHasPlayerPlayed(boolean hasPlayerPlayed) {
        this.hasPlayerPlayed = hasPlayerPlayed;
    }
}
