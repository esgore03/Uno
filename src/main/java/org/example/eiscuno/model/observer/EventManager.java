package org.example.eiscuno.model.observer;
import java.util.ArrayList;
import java.util.List;

public class EventManager {
    private List<EventListener> listeners = new ArrayList<>();

    public void addListener(EventListener eventListener) {
        listeners.add(eventListener);
    }

    public void removeListener(EventListener eventListener) {
        listeners.remove(eventListener);
    }

    public void notifyListenersPlayerTurnUpdate(boolean playerHasPlayed) {
        for (EventListener eventListener : listeners) {
            eventListener.updatePlayerTurn(playerHasPlayed);
        }
    }

    public void notifyListenersCardsHumanPlayerUpdate() {
        for (EventListener eventListener : listeners) {
            eventListener.updateCardsHumanPlayer();
        }
    }

    public void notifyListenersCardsMachinePlayerUpdate() {
        for (EventListener eventListener : listeners) {
            eventListener.updateCardsMachinePlayer();
        }
    }
}

