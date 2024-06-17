package org.example.eiscuno.model.observer;

public interface EventListener {
    void updateTurn(boolean playerHasPlayed);
    void updateCards();
}
