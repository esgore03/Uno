package org.example.eiscuno.model.observer;

import java.util.ArrayList;
import java.util.List;

/**
 * This class manages the listeners and notifies them of game events in the Uno game.
 */
public class EventManager {
    private List<EventListener> listeners = new ArrayList<>();

    /**
     * Adds an event listener to be notified of game events.
     *
     * @param eventListener the event listener to add
     */
    public void addListener(EventListener eventListener) {
        listeners.add(eventListener);
    }

    /**
     * Removes an event listener from the list of listeners.
     *
     * @param eventListener the event listener to remove
     */
    public void removeListener(EventListener eventListener) {
        listeners.remove(eventListener);
    }

    /**
     * Notifies all registered listeners about a player's turn update.
     *
     * @param playerHasPlayed {@code true} if the player has played, {@code false} otherwise
     */
    public void notifyListenersPlayerTurnUpdate(boolean playerHasPlayed) {
        for (EventListener eventListener : listeners) {
            eventListener.updatePlayerTurn(playerHasPlayed);
        }
    }

    /**
     * Notifies all registered listeners about an update in the human player's cards.
     */
    public void notifyListenersCardsHumanPlayerUpdate() {
        for (EventListener eventListener : listeners) {
            eventListener.updateCardsHumanPlayer();
        }
    }

    /**
     * Notifies all registered listeners about an update in the machine player's cards.
     */
    public void notifyListenersCardsMachinePlayerUpdate() {
        for (EventListener eventListener : listeners) {
            eventListener.updateCardsMachinePlayer();
        }
    }
}

