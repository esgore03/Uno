package org.example.eiscuno.model.observer;

/**
 * This interface represents an event listener for the Uno game.
 * It defines methods to handle various game events such as player turns and card updates.
 */
public interface EventListener {
    /**
     * Called to update the listener about the player's turn status.
     *
     * @param playerHasPlayed {@code true} if the player has played, {@code false} otherwise.
     */
    void updatePlayerTurn(boolean playerHasPlayed);

    /**
     * Called to update the listener about the machine player's cards.
     */
    void updateCardsMachinePlayer();

    /**
     * Called to update the listener about the human player's cards.
     */
    void updateCardsHumanPlayer();
}