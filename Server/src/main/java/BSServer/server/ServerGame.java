package BSServer.server;

import BSServer.general.*;
import BSServer.server.game.*;
import java.nio.*;

public class ServerGame extends Game {
    private final int boardSize = 10;
    private final int[][] shipsParams = new int[][]{
        {1, 4}, 
        {2, 3}, 
        {3, 2}, 
        {4, 1}  
    };
    private final Player playerA;
    private final Player playerB;

    public ServerGame(ClientConnection clientA, ClientConnection clientB) {
        this.playerA = new Player(clientA);
        this.playerB = new Player(clientB);
    }

    @Override
    public void start() {
        super.start();
        Console.print("New game started");
        playerA.board = new Board(boardSize);
        playerB.board = new Board(boardSize);
        playerA.board.placeShips(shipsParams);
        playerB.board.placeShips(shipsParams);
        sendConfiguration(playerA);
        sendConfiguration(playerB);
    }

    @Override
    public void stop() {
        super.stop();
        Console.print("Game stopped");
    }

    private void sendConfiguration(Player player) {
        ByteBuffer data = ByteBuffer.allocate(player.board.getShipsPositions().length * Integer.BYTES);
        for (int val : player.board.getShipsPositions()) {
            data.putInt(val);
        }
        player.connection.prepareMessage(MessageType.CONFIGURATION, data);
        player.connection.send();
    }

    public Player getPlayer(Connection connection) {
        return connection == playerA.connection ? playerA : playerB;
    }

    public Player getSecondPlayer(Connection connection) {
        return connection == playerA.connection ? playerB : playerA;
    }

    @Override
    public void parseMessage(Connection connection, int msgType, ByteBuffer data) {
        if (!isFinished()) {
            Player player = getPlayer(connection);
            Player secondPlayer = getSecondPlayer(connection);
            switch (msgType) {
                case MessageType.BYE: {
                    byeAction(secondPlayer);
                    break;
                }
                case MessageType.READY: {
                    readyAction(player);
                    break;
                }
                case MessageType.SHOT: {
                    shotAction(player, secondPlayer, data);
                    break;
                }
            }
        }
    }

    private void byeAction(Player secondPlayer) {
        stop();
        secondPlayer.connection.prepareMessage(MessageType.ENEMY_LEFT);
        secondPlayer.connection.send();
    }

    private void readyAction(Player player) {
        player.isReady = true;
        
        if (playerA.isReady && playerB.isReady) {
            playerA.connection.prepareMessage(MessageType.GAME_START);
            playerB.connection.prepareMessage(MessageType.GAME_START);
            
            if (Math.random() < 0.5d) {
                setTurnA();
                playerA.connection.prepareMessage(MessageType.YOUR_TURN);
                playerB.connection.prepareMessage(MessageType.ENEMY_TURN);
            } else {
                setTurnB();
                playerB.connection.prepareMessage(MessageType.YOUR_TURN);
                playerA.connection.prepareMessage(MessageType.ENEMY_TURN);
            }
            playerA.connection.send();
            playerB.connection.send();
        }
    }

    private void shotAction(Player player, Player secondPlayer, ByteBuffer data) {
        int x = data.getInt();
        int y = data.getInt();

        if (secondPlayer.board.isValidShot(x, y)) {
            Ship hitShip = secondPlayer.board.checkForHit(x, y);
            
            if (hitShip != null) {
                player.connection.prepareMessage(MessageType.HIT, data);
                secondPlayer.connection.prepareMessage(MessageType.ENEMY_HIT, data);
                
                if (hitShip.isSunk()) {
                    ByteBuffer sunkData = createSunkData(hitShip);
                    player.connection.prepareMessage(MessageType.SUNK, sunkData);
                    secondPlayer.connection.prepareMessage(MessageType.ENEMY_SUNK, sunkData);
                    
                    if (secondPlayer.board.getRemainingShips() == 0) {
                        stop();
                        player.connection.prepareMessage(MessageType.YOU_WON);
                        secondPlayer.connection.prepareMessage(MessageType.YOU_LOST);
                    }
                }
            } else {
                changeTurn();
                player.connection.prepareMessage(MessageType.MISSED, data);
                player.connection.prepareMessage(MessageType.ENEMY_TURN);
                secondPlayer.connection.prepareMessage(MessageType.ENEMY_MISSED, data);
                secondPlayer.connection.prepareMessage(MessageType.YOUR_TURN);
            }
        } else {
            player.connection.prepareMessage(MessageType.INVALID_SHOT);
        }
        player.connection.send();
        secondPlayer.connection.send();
    }
    
    private ByteBuffer createSunkData(Ship sunkShip) {        
        ByteBuffer sunkData = ByteBuffer.allocate(8 * Integer.BYTES);
        for (int val : sunkShip.getPosition()) {
            sunkData.putInt(val);
        }
        for (int val : sunkShip.getArea()) {
            sunkData.putInt(val);
        }
        return sunkData;
    }
}