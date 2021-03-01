package BSClient.client;

import BSClient.general.*;
import BSClient.client.GUI.*;

public class ClientReaction {
    private ServerConnection serverConnection;
    private ClientGame clientGame;
    private MainWindow mainWindow;
    private MenuPanel menuPanel;
    private GamePanel gamePanel;
    
    public void setServerConnection(ServerConnection serverConnection) {
        this.serverConnection = serverConnection;
    }
    
    public void setClientGame(ClientGame clientGame) {
        this.clientGame = clientGame;
    }
    
    public void setMainWindow(MainWindow mainWindow) {
        this.mainWindow = mainWindow;
    }
    
    public void setMenuPanel(MenuPanel menuPanel) {
        this.menuPanel = menuPanel;
    }
    
    public void setGamePanel(GamePanel gamePanel) {
        this.gamePanel = gamePanel;
    }
    
    public void disconnected() {
        menuPanel.unlockMenu();
        mainWindow.displayStatus(Message.DISCONNECTED, false);
        mainWindow.hideGameView();
        mainWindow.hidePlayAgain();
    }
    
    public void hello() {
        mainWindow.displayStatus(Message.WAITING, true);
        mainWindow.hideGameView();
        mainWindow.hidePlayAgain();
    }
    
    public void bye() {
        mainWindow.displayStatus(Message.SERVER_OFF, true);
        mainWindow.hideGameView();
        mainWindow.hidePlayAgain();
    }
    
    public void serverFull() {
        mainWindow.displayStatus(Message.SERVER_FULL, true);
    }
    
    public void configuration(int[] shipsPositions) {
        mainWindow.displayStatus(Message.CONFIGURATION, true);
        gamePanel.getOwnSidePanel().getBoardPanel().clear();
        gamePanel.getEnemySidePanel().getBoardPanel().clear();
        
        if (shipsPositions != null) {
            BoardPanel boardPanel = gamePanel.getOwnSidePanel().getBoardPanel();
            for (int i = 0; i < shipsPositions.length; i += 4) {
                int xP1 = shipsPositions[i];
                int yP1 = shipsPositions[i + 1];
                int xP2 = shipsPositions[i + 2];
                int yP2 = shipsPositions[i + 3];
                
                int prowSprite = BoardSide.PROW_H;
                int boardSprite = BoardSide.BOARD_H;
                
                if (yP1 == yP2) {
                    for (int j = xP1 + 1; j <= xP2; j++) {
                        boardPanel.markShip(j, yP1, boardSprite);
                    }
                } else {
                    prowSprite = BoardSide.PROW_V;
                    boardSprite = BoardSide.BOARD_V;
                    for (int j = yP1 + 1; j <= yP2; j++) {
                        boardPanel.markShip(xP1, j, boardSprite);
                    }
                }
                
                boardPanel.markShip(xP1, yP1, prowSprite);
            }
        
            boardPanel.repaint();
            gamePanel.getOwnSidePanel().displayShipsCounter(clientGame.getOwnShipsCounter());
            gamePanel.getEnemySidePanel().displayShipsCounter(clientGame.getEnemyShipsCounter());
        
            serverConnection.prepareMessage(MessageType.READY);
            serverConnection.send();
        }
    }
    
    public void gameStart() {
        mainWindow.displayStatus(Message.GAME_STARTED, true);
        mainWindow.showGameView();
    }
    
    public void yourTurn() {
        mainWindow.displayStatus(Message.YOUR_TURN, false);
    }
    
    public void enemyTurn() {
        mainWindow.displayStatus(Message.ENEMY_TURN, false);
    }
    
    public void hit(int x, int y) {        
        mainWindow.displayStatus(Message.HIT, true);
        gamePanel.getEnemySidePanel().getBoardPanel().markShip(x, y, BoardSide.REVEAL);
        gamePanel.getEnemySidePanel().getBoardPanel().markShot(x, y);
        gamePanel.getEnemySidePanel().getBoardPanel().repaint();
    }
    
    public void enemyHit(int x, int y) {
        mainWindow.displayStatus(Message.ENEMY_HIT, true);
        gamePanel.getOwnSidePanel().getBoardPanel().markShot(x, y);
        gamePanel.getOwnSidePanel().getBoardPanel().repaint();
    }
    
    public void missed(int x, int y) {
        mainWindow.displayStatus(Message.MISSED, true);
        gamePanel.getEnemySidePanel().getBoardPanel().markShot(x, y);
        gamePanel.getEnemySidePanel().getBoardPanel().repaint();
    }
    
    public void enemyMissed(int x, int y) {
        mainWindow.displayStatus(Message.ENEMY_MISSED, true);
        gamePanel.getOwnSidePanel().getBoardPanel().markShot(x, y);
        gamePanel.getOwnSidePanel().getBoardPanel().repaint();
    }
    
    public void sunk(int xP1, int yP1, int xP2, int yP2, int xO1, int yO1, int xO2, int yO2) {
        mainWindow.displayStatus(Message.SUNK, true);
        BoardPanel boardPanel = gamePanel.getEnemySidePanel().getBoardPanel();

        int prowSprite = BoardSide.PROW_H;
        int boardSprite = BoardSide.BOARD_H;

        if (yP1 == yP2) {
            for (int j = xP1 + 1; j <= xP2; j++) {
                boardPanel.markShip(j, yP1, boardSprite);
            }
        } else {
            prowSprite = BoardSide.PROW_V;
            boardSprite = BoardSide.BOARD_V;
            for (int j = yP1 + 1; j <= yP2; j++) {
                boardPanel.markShip(xP1, j, boardSprite);
            }
        }

        boardPanel.markShip(xP1, yP1, prowSprite);
        for (int i = xO1; i <= xO2; i++) {
            for (int j = yO1; j <= yO2; j++) {
                boardPanel.markShot(i, j);
            }
        }

        boardPanel.repaint();
        gamePanel.getEnemySidePanel().displayShipsCounter(clientGame.getEnemyShipsCounter());
    }
    
    public void enemySunk(int xP1, int yP1, int xP2, int yP2, int xO1, int yO1, int xO2, int yO2) {
        mainWindow.displayStatus(Message.ENEMY_SUNK, true);
        BoardPanel boardPanel = gamePanel.getOwnSidePanel().getBoardPanel();

        int prowSprite = BoardSide.PROW_H;
        int boardSprite = BoardSide.BOARD_H;

        if (yP1 == yP2) {
            for (int j = xP1 + 1; j <= xP2; j++) {
                boardPanel.markShip(j, yP1, boardSprite);
            }
        } else {
            prowSprite = BoardSide.PROW_V;
            boardSprite = BoardSide.BOARD_V;
            for (int j = yP1 + 1; j <= yP2; j++) {
                boardPanel.markShip(xP1, j, boardSprite);
            }
        }

        boardPanel.markShip(xP1, yP1, prowSprite);
        for (int i = xO1; i <= xO2; i++) {
            for (int j = yO1; j <= yO2; j++) {
                boardPanel.markShot(i, j);
            }
        }

        boardPanel.repaint();
        gamePanel.getOwnSidePanel().displayShipsCounter(clientGame.getOwnShipsCounter());
    }
            
    public void youWon() {
        mainWindow.displayStatus(Message.YOU_WON, true);
        mainWindow.showPlayAgain();
    }
    
    public void youLost() {
        mainWindow.displayStatus(Message.YOU_LOST, true);
        mainWindow.showPlayAgain();
    }
    
    public void enemyDisconnected() {
        mainWindow.displayStatus(Message.ENEMY_DISCONNECTED, true);
        mainWindow.showPlayAgain();
    }
}