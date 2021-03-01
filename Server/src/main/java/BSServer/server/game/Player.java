package BSServer.server.game;

import BSServer.general.*;

public class Player {
    public final Connection connection;
    public Board board;
    public boolean isReady;
    
    public Player(Connection connection) {
        this.connection = connection;
    }
}