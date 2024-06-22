package org.example.eiscuno.model.machine;

import javafx.application.Platform;
import org.example.eiscuno.controller.GameUnoController;
import org.example.eiscuno.model.game.GameUno;

import java.io.IOException;

/**
 * This class represents a thread that continuously checks the game state to determine if the game has ended.
 * It periodically checks if the human player or the machine player has won and calls the appropriate method
 * on the GameUnoController to handle the win/loss scenario.
 */
public class ThreadEndGame  extends Thread{
    GameUnoController gameUnoController;
    GameUno gameUno;

    /**
     * Constructor for the ThreadEndGame class.
     *
     * @param gameUnoController the controller responsible for managing the game state and UI updates
     * @param gameUno the game logic object
     */
     public ThreadEndGame(GameUnoController gameUnoController, GameUno gameUno){
         this.gameUnoController = gameUnoController;
         this.gameUno = gameUno;
     }

    /**
     * The run method contains the logic to continuously check the game state.
     * It sleeps for 500 milliseconds between each check to avoid excessive CPU usage.
     * If a win condition is detected, it calls the corresponding method on the GameUnoController
     * using Platform.runLater to ensure UI updates are done on the JavaFX application thread.
     */
    @Override
    public void run(){
        while(true){
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            try {
                if (gameUno.didHumanWin()) {
                    Platform.runLater(() -> {
                        try {
                            gameUnoController.win();
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    });
                } else if (gameUno.didMachineWin()) {
                    Platform.runLater(() -> {
                        try {
                            gameUnoController.lose();
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    });
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Sets the GameUnoController.
     *
     * @param gameUnoController the controller responsible for managing the game state and UI updates
     */
    public void setGameUnoController(GameUnoController gameUnoController) {
        this.gameUnoController = gameUnoController;
    }

    /**
     * Sets the GameUno object.
     *
     * @param gameUno the game logic object
     */
    public void setGameUno(GameUno gameUno) {
        this.gameUno = gameUno;
    }
}
