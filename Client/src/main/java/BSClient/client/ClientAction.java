package BSClient.client;

import BSClient.general.*;
import BSClient.client.GUI.*;
import java.nio.*;
import javax.swing.*;

public class ClientAction {
    private ServerConnection serverConnection;
    private ClientGame clientGame;
    
    private MainWindow mainWindow;
    private MenuPanel menuPanel;
    
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
    
    public void connectDisconnect() {
        if (!serverConnection.isConnected()) {
            connect();
        } else {
            disconnect();
        }
    }
    
    private void connect() {
        String host = menuPanel.getHost();
        int port = -1;
        
        if (host.isEmpty()) {
            JOptionPane.showMessageDialog(mainWindow.getFrame(), Message.INVALID_HOST);
            return;
        }
        
        try {
            port = Integer.parseInt(menuPanel.getPort());
            port = (port > 0 && port <= 65536) ? port : -1;
        } catch (NumberFormatException ex) {}
        
        if (port == -1) {
            JOptionPane.showMessageDialog(mainWindow.getFrame(), Message.INVALID_PORT);
            return;
        }
        
        if (serverConnection.connect(host, port)) {
            menuPanel.lockMenu();
            mainWindow.displayStatus(Message.CONNECTED, true);
            serverConnection.prepareMessage(MessageType.HELLO);
            serverConnection.send();
        } else {
            mainWindow.displayStatus(Message.NOT_CONNECTED, true);
        }
    }
    
    private void disconnect() {
        serverConnection.prepareMessage(MessageType.BYE);
        serverConnection.send();
        serverConnection.disconnect();
        mainWindow.displayStatus(Message.DISCONNECTED, true);
        mainWindow.hidePlayAgain();
    }
    
    public void shot(int x, int y) {
        if (serverConnection.isConnected() && clientGame.isTurnA()) {
            ByteBuffer data = ByteBuffer.allocate(2 * Integer.BYTES);
            data.putInt(x);
            data.putInt(y);
            serverConnection.prepareMessage(MessageType.SHOT, data);
            serverConnection.send();
        }
    }
    
    public void playAgain() {
        serverConnection.prepareMessage(MessageType.HELLO);
        serverConnection.send();
    }
    
    public void quit() {
        if (serverConnection.isConnected()) {
            disconnect();
        }
        System.exit(0);
    }
}