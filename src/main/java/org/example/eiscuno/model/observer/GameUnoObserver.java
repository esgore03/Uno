package org.example.eiscuno.model.observer;

import javafx.application.Platform;
import org.example.eiscuno.controller.GameUnoController;

/**
 * This class implements the EventListener interface to observe game events in the Uno game.
 * It updates the corresponding methods in GameUnoController using JavaFX Platform for UI updates.
 */
public class GameUnoObserver implements EventListener {
    private GameUnoController gameUnoController;

    /**
     * Constructs a new GameUnoObserver with a GameUnoController instance.
     *
     * @param gameUnoController the controller for managing Uno game events
     */
    public GameUnoObserver(GameUnoController gameUnoController) {
        this.gameUnoController = gameUnoController;
    }

    /**
     * Updates the player's turn status in the associated GameUnoController.
     *
     * @param playerHasPlayed {@code true} if the player has played, {@code false} otherwise
     */
    @Override
    public void updatePlayerTurn(boolean playerHasPlayed) {
        gameUnoController.setPlayerHasPlayed(playerHasPlayed);
    }

    /**
     * Updates the machine player's cards in the associated GameUnoController.
     * Uses Platform.runLater() to ensure UI updates are performed on the JavaFX Application Thread.
     */
    @Override
    public void updateCardsMachinePlayer() {
        Platform.runLater(() -> gameUnoController.printCardsMachinePlayer());
    }

    /**
     * Updates the human player's cards in the associated GameUnoController.
     * Uses Platform.runLater() to ensure UI updates are performed on the JavaFX Application Thread.
     */
    @Override
    public void updateCardsHumanPlayer() {
        Platform.runLater(() -> gameUnoController.printCardsHumanPlayer());
    }
}
