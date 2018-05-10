package app.state;

import java.awt.*;

public abstract class State {

    private static State currentState = null;

    public static void setState(State state){
        currentState = state;
    }

    public static State getState(){
        return currentState;
    }

    //CLASS
    public abstract void update();

    public abstract void processInput();

    public abstract void draw(Graphics g);
}
