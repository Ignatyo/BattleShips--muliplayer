package BSServer.server;

import java.io.*;
import java.net.*;
import java.util.*;

public class Server {
    private ServerSocket serverSocket;
    private int clientLimit;
    private List<ClientConnection> clients;
    private static Console console = new Console();
    
    public void start(int port, int clientLimit) {
        try {
            serverSocket = new ServerSocket(port);
            this.clientLimit = clientLimit;
            clients = new ArrayList<ClientConnection>();
            console.print("Server started at " + InetAddress.getLocalHost().getHostAddress() + ":" + port + " -> client limit: " + clientLimit);
            new Thread(() -> acceptConnections()).start();
        } catch (IOException ex) {}
    }
    
    public void stop() {
        clients.forEach(client -> client.farewell());
        try {
            serverSocket.close();
        } catch (IOException ex) {}
    }
    
    private void acceptConnections() {
        while (!serverSocket.isClosed()) {
            try {
                Socket clientSocket = serverSocket.accept();
                console.print("New connection from " + clientSocket.getRemoteSocketAddress().toString());
                new ClientConnection(clientSocket, this);
            } catch (IOException ex) {}
        }
    }
    
    public boolean addClient(ClientConnection client) {
        if (clients.size() < clientLimit) {
            clients.add(client);
            console.print("New client added");
            return true;
        } else {
            console.print("Server is full | client limit: " + clientLimit);
            return false;
        }
    }
    
    public boolean removeClient(ClientConnection client) {
        if (clients.remove(client)) {
            console.print("Client removed");
            return true;
        } else {
            console.print("No such client");
            return false;
        }
    }
    
    public boolean makeGame(ClientConnection clientB) {
        clientB.clearGame();
        ClientConnection clientA = findIdleClient(clientB);
        if (clientA != null) {
            console.print("Found a client to pair with");
            ServerGame serverGame = new ServerGame(clientA, clientB);
            clientA.setGame(serverGame);
            clientB.setGame(serverGame);
            serverGame.start();
            return true;
        }
        return false;
    }
    
    private ClientConnection findIdleClient(ClientConnection clientB) {
        return clients.stream()
                .filter(clientA -> clientA != clientB && !clientA.isInGame())
                .findFirst().orElse(null);
    }
    
    public static void main(String[] args) {
        int port = 8081;
        int clientLimit = 50;
        
        if (args.length >= 1) {
            try {
                port = Integer.parseInt(args[0]);
            } catch (NumberFormatException ex) {}
        }
        
        if (args.length >= 2) {
            try {
                clientLimit = Integer.parseInt(args[1]);
            } catch (NumberFormatException ex) {}
        }
        
        Server server = new Server();
        server.start(port, clientLimit);
        try {
            console.print("Enter 'E' to stop the server");
            InputStreamReader kbd = new InputStreamReader(System.in);
            int c = kbd.read();
            if (c == 'E' || c == 'e') {
                server.stop();
                console.print("Server stopped");
                System.exit(0);
            }
        } catch (IOException ex) {}
    }
}