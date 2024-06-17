package org.example.eiscuno.model.observer;

public interface EventListener {
    void updatePlayerTurn(boolean playerHasPlayed);
    void updateCardsMachinePlayer();
    void updateCardsHumanPlayer();
}
