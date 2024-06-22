package org.example.eiscuno.model.observer;

import javafx.application.Platform;
import org.example.eiscuno.controller.GameUnoController;

/**
 * This class implements the EventListener interface to observe game events in the Uno game.
 * It updates the corresponding methods in GameUnoController using JavaFX Platform for UI updates.
 */
public class GameUnoControllerObserver implements EventListener {
    private GameUnoController gameUnoController;

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

    /**
     * Sets the GameUnoController instance for this observer.
     *
     * @param gameUnoController the GameUnoController instance to set
     */
    public void setGameUnoController(GameUnoController gameUnoController) {
        this.gameUnoController = gameUnoController;
    }
}
