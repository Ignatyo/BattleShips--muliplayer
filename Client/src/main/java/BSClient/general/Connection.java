package BSClient.general;

import java.io.*;
import java.net.*;
import java.nio.*;

public abstract class Connection {
    protected Socket socket;
    private DataInputStream input;
    private DataOutputStream output;
    
    public Connection() {}
    
    public boolean connect(String host, int port) {
        try {
            socket = new Socket(host, port);
            input = new DataInputStream(socket.getInputStream());
            output = new DataOutputStream(socket.getOutputStream());            
            start();
            return true;
        } catch (IOException ex) {
            ex.printStackTrace();
            return false;
        }
    }
    
    public void disconnect() {
        if (socket != null) {
            try {
                socket.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }
    
    private void start() {
        new Thread(() -> {
            receive();
            disconnect();
        }).start();
    }
        
    private void receive() {
        while (input != null) {
            try {
                ByteBuffer data = null;
                int msgType = input.readInt();
                int dataLength = input.readInt();
                if (dataLength > 0) {
                    data = ByteBuffer.allocate(dataLength);
                    input.readFully(data.array());
                }
                receiveMessage(msgType, data);
            } catch (IOException ex) {
                ex.printStackTrace();
                break;
            }
        }
    }
    
    protected abstract void receiveMessage(int msgType, ByteBuffer data);

    public void prepareMessage(int msgType, ByteBuffer data) {
        if (output != null) {
            try {
                output.writeInt(msgType);
                if (data != null) {
                    output.writeInt(data.array().length);
                    output.write(data.array());
                } else {
                    output.writeInt(0);
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }
    
    public void prepareMessage(int msgType) {
        prepareMessage(msgType, null);
    }
    
    public void send() {
        if (output != null) {
            try {
                output.flush();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    public boolean isConnected() {
        return socket != null && !socket.isClosed() && socket.isConnected();
    }
}