package BSClient.general;

import java.nio.*;

public abstract class Game {
    private enum State {
        CONFIGURATION,
        TURN_A,
        TURN_B,
        END
    };
    private State state;
    
    public void start() {
        state = State.CONFIGURATION;
    }  
    
    public void stop() {
        state = State.END;
    }
    
    public void setTurnA() {
        state = State.TURN_A;
    }
    
    public void setTurnB() {
        state = State.TURN_B;
    }
    
    public boolean isTurnA() {
        return state == State.TURN_A;
    }

    public boolean isTurnB() {
        return state == State.TURN_B;
    }

    public boolean isPlayed() {
        return state == State.TURN_A || state == State.TURN_B;
    }

    public boolean isFinished() {
        return state == State.END;
    }

    public void changeTurn() {
        if (isPlayed()) {
            state = state == State.TURN_A ? State.TURN_B : State.TURN_A;
        }
    }
    
    public abstract void parseMessage(Connection conn, int msgType, ByteBuffer data);
}