package org.example.eiscuno.model.observer;

import org.example.eiscuno.model.machine.ThreadPlayMachine;

/**
 * Represents an observer for the ThreadPlayMachine class.
 * This observer listens for events related to player turns and updates the ThreadPlayMachine accordingly.
 */
public class ThreadPlayMachineObserver implements EventListener{
    private ThreadPlayMachine threadPlayMachine;

    /**
     * Updates the ThreadPlayMachine when the player turn status changes.
     *
     * @param playerHasPlayed true if the player has played a card, false otherwise
     */
    @Override
    public void updatePlayerTurn(boolean playerHasPlayed) {
        threadPlayMachine.setHasPlayerPlayed(playerHasPlayed);
    }

    /**
     * Updates the observer with new cards for the machine player.
     * This method is not implemented because it's not needed in this observer.
     */
    @Override
    public void updateCardsMachinePlayer() {
    }

    /**
     * Updates the observer with new cards for the human player.
     * This method is not implemented because it's not needed in this observer.
     */
    @Override
    public void updateCardsHumanPlayer() {
    }

    /**
     * Sets the ThreadPlayMachine instance for this observer.
     *
     * @param threadPlayMachine the ThreadPlayMachine instance to set
     */
    public void setThreadPlayMachine(ThreadPlayMachine threadPlayMachine) {
        this.threadPlayMachine = threadPlayMachine;
    }
}
