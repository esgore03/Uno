package org.example.eiscuno.model.machine;

import org.example.eiscuno.model.game.GameUno;

public class ThreadRefillDeck extends Thread{
    private GameUno gameUno;

    public ThreadRefillDeck(GameUno gameUno){
        this.gameUno = gameUno;
    }

    public void run(){
        while(true){
            if(gameUno.getDeck().isEmpty()){
                gameUno.refillDeckOfCards();
            }
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
