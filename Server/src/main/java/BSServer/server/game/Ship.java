package BSServer.server.game;

public class Ship {
    private int xP1, yP1, xP2, yP2;
    private int xA1, yA1, xA2, yA2;
    private final int[] positionCoords = new int[4];
    private final int[] areaCoords = new int[4];
    private int remainingFields;
    
    public Ship(int x, int y, int length, boolean vertical, int boardSize) {
        setPosition(x, y, length, vertical);
        setArea(boardSize);
        remainingFields = length;
    }
    
    private void setPosition(int x, int y, int length, boolean vertical) {
        xP1 = x;
        yP1 = y;
        if (vertical) {
            xP2 = x;
            yP2 = y + length - 1;
        } else {
            xP2 = x + length - 1;
            yP2 = y;
        }
        
        positionCoords[0] = xP1;
        positionCoords[1] = yP1;
        positionCoords[2] = xP2;
        positionCoords[3] = yP2;
    }
    
    private void setArea(int boardSize) {
        xA1 = xP1 > 0 ? xP1 - 1 : xP1;
        yA1 = yP1 > 0 ? yP1 - 1 : yP1;
        xA2 = xP2 < boardSize - 1 ? xP2 + 1 : xP2;
        yA2 = yP2 < boardSize - 1 ? yP2 + 1 : yP2;
        
        areaCoords[0] = xA1;
        areaCoords[1] = yA1;
        areaCoords[2] = xA2;
        areaCoords[3] = yA2;
    }
    
    public boolean isInPosition(int x, int y) {
        return x >= xP1 && x <= xP2 && y >= yP1 && y <= yP2;
    }
    
    public boolean isInArea(int x, int y, int length, boolean vertical) {
        return vertical ? x <= xA2 && x >= xA1 && y <= yA2 && y + length - 1 >= yA1 : x <= xA2 && x + length - 1 >= xA1 && y <= yA2 && y >= yA1;
    }
    
    public void hit() {
        remainingFields--;
    }
    
    public boolean isSunk() {
        return remainingFields == 0;
    }
    
    public int[] getPosition() {
        return positionCoords;
    }
    
    public int[] getArea() {
        return areaCoords;
    }
}