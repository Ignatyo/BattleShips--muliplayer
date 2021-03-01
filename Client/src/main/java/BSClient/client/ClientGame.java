package BSClient.client;

import BSClient.general.*;
import java.nio.*;

public class ClientGame extends Game {
    private final ClientReaction clientReaction;
    
    private int ownShipsCounter = 0;
    private int enemyShipsCounter = 0;
    
    public ClientGame(ClientAction clientAction, ClientReaction clientReaction) {
        clientAction.setClientGame(this);
        clientReaction.setClientGame(this);
        this.clientReaction = clientReaction;
    }
    
    public int getOwnShipsCounter() {
        return ownShipsCounter;
    }

    public int getEnemyShipsCounter() {
        return enemyShipsCounter;
    }

    @Override
    public void parseMessage(Connection conn, int msgType, ByteBuffer data) {
        switch (msgType) {
            case MessageType.CONFIGURATION: {
                IntBuffer intData = data.asIntBuffer();
                int[] positions = new int[intData.limit()];
                intData.get(positions);
                ownShipsCounter = positions.length / 4;
                enemyShipsCounter = ownShipsCounter;
                clientReaction.configuration(positions);
                break;
            }
            case MessageType.GAME_START: {
                clientReaction.gameStart();
                break;
            }
            case MessageType.YOUR_TURN: {
                setTurnA();
                clientReaction.yourTurn();
                break;
            }
            case MessageType.ENEMY_TURN: {
                setTurnB();
                clientReaction.enemyTurn();
                break;
            }
            case MessageType.HIT: {
                int x = data.getInt();
                int y = data.getInt();
                clientReaction.hit(x, y);
                break;
            }
            case MessageType.ENEMY_HIT: {
                int x = data.getInt();
                int y = data.getInt();
                clientReaction.enemyHit(x, y);
                break;
            }
            case MessageType.MISSED: {
                int x = data.getInt();
                int y = data.getInt();
                clientReaction.missed(x, y);
                break;
            }
            case MessageType.ENEMY_MISSED: {
                int x = data.getInt();
                int y = data.getInt();
                clientReaction.enemyMissed(x, y);
                break;
            }
            case MessageType.SUNK: {
                enemyShipsCounter--;
                int xP1 = data.getInt();
                int yP1 = data.getInt();
                int xP2 = data.getInt();
                int yP2 = data.getInt();
                int xA1 = data.getInt();
                int yA1 = data.getInt();
                int xA2 = data.getInt();
                int yA2 = data.getInt();
                clientReaction.sunk(xP1, yP1, xP2, yP2, xA1, yA1, xA2, yA2);
                break;
            }
            case MessageType.ENEMY_SUNK: {
                ownShipsCounter--;
                int xP1 = data.getInt();
                int yP1 = data.getInt();
                int xP2 = data.getInt();
                int yP2 = data.getInt();
                int xA1 = data.getInt();
                int yA1 = data.getInt();
                int xA2 = data.getInt();
                int yA2 = data.getInt();
                clientReaction.enemySunk(xP1, yP1, xP2, yP2, xA1, yA1, xA2, yA2);
                break;
            }
            case MessageType.INVALID_SHOT: {
                break;
            }
            case MessageType.YOU_WON: {
                stop();
                clientReaction.youWon();
                break;
            }
            case MessageType.YOU_LOST: {
                stop();
                clientReaction.youLost();
                break;
            }
            case MessageType.ENEMY_LEFT: {
                stop();
                clientReaction.enemyDisconnected();
                break;
            }
        }
    }
    
    public void reset() {
        ownShipsCounter = 0;
        enemyShipsCounter = 0;
    }
}