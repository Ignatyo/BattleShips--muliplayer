package BSClient.client;

import BSClient.general.*;
import java.nio.*;

public class ServerConnection extends Connection {
    private final ClientReaction clientReaction;
    private final ClientGame clientGame;
    
    public ServerConnection(ClientAction clientAction, ClientReaction clientReaction, ClientGame clientGame) {
        clientAction.setServerConnection(this);
        clientReaction.setServerConnection(this);
        this.clientReaction = clientReaction;
        this.clientGame = clientGame;
    }
    
    @Override
    public void disconnect() {
        super.disconnect();
        clientReaction.disconnected();
    }
    
    @Override
    protected void receiveMessage(int msgType, ByteBuffer data) {
        switch (msgType) {
            case MessageType.HELLO: {
                clientGame.reset();
                clientReaction.hello();
                break;
            }
            case MessageType.BYE: {
                clientReaction.bye();
                break;
            }
            case MessageType.SERVER_FULL: {
                clientReaction.serverFull();
                break;
            }
            default: {
                if (clientGame != null) {
                    clientGame.parseMessage(this, msgType, data);
                }
            }
        }
    }
}