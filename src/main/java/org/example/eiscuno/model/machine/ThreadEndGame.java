package org.example.eiscuno.model.machine;

import org.example.eiscuno.controller.GameUnoController;
import org.example.eiscuno.model.game.GameUno;

import java.io.IOException;

public class ThreadEndGame  extends Thread{
    GameUnoController gameUnoController;
    GameUno gameUno;

     public ThreadEndGame(GameUnoController gameUnoController, GameUno gameUno){
         this.gameUnoController = gameUnoController;
         this.gameUno = gameUno;
     }

    @Override
    public void run(){
        while(true){
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            try{
                System.out.println("Humano ganó: " + gameUno.didHumanWin());
                System.out.println(gameUno.didMachineWin());
                if(gameUno.didHumanWin()){
                    System.out.println("Humano ganó: " + gameUno.didHumanWin());
                    gameUnoController.win();
                }
                else if(gameUno.didMachineWin()){
                    gameUnoController.lose();
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
    public void setGameUnoController(GameUnoController gameUnoController) {
        this.gameUnoController = gameUnoController;
    }

    public void setGameUno(GameUno gameUno) {
        this.gameUno = gameUno;
    }
}
