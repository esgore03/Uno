package org.example.eiscuno.model.observer;

import javafx.application.Platform;
import org.example.eiscuno.controller.GameUnoController;

public class GameUnoObserver implements EventListener {
    private GameUnoController gameUnoController;

    public GameUnoObserver(GameUnoController gameUnoController) {
        this.gameUnoController = gameUnoController;
    }

    @Override
    public void updatePlayerTurn(boolean playerHasPlayed) {
        gameUnoController.setPlayerHasPlayed(playerHasPlayed);
    }

    @Override
    public void updateCardsMachinePlayer(){
        Platform.runLater(() -> gameUnoController.printCardsMachinePlayer());
    }

    @Override
    public void updateCardsHumanPlayer(){
        Platform.runLater(() -> gameUnoController.printCardsHumanPlayer());
    }
}
