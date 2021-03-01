package BSServer.server.game;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Board {
    private final int size;
    private int remainingShips;
    private final List<Ship> ships = new ArrayList<Ship>();
    private final List<Point> shots = new ArrayList<Point>();
    
    public Board(int size) {
        this.size = size;
    }
    
    public void placeShips(int[][] shipsParams) {
        for (int[] shipParam : shipsParams) {
            int copies = shipParam[0];
            int length = shipParam[1];
            for (int i = 0; i < copies; i++) {
                placeShip(length);
            }
        }
        remainingShips = ships.size();
    }
    
    private void placeShip(int length) {
        int x, y;
        boolean vertical;
        do {
            x = (int) (Math.random() * (size + 1 - length));
            y = (int) (Math.random() * size);
            
            vertical = Math.random() < 0.5d;
            if (vertical) {
                int xT = x;
                x = y;
                y = xT;
            }
        } while (!isFreeSpace(x, y, length, vertical));
        
        ships.add(new Ship(x, y, length, vertical, size));
    }
    
    private boolean isFreeSpace(int x, int y, int length, boolean vertical) {
        return ships.stream().noneMatch(ship -> ship.isInArea(x, y, length, vertical));
    }

    public int[] getShipsPositions() {
        int[] shipsPositions = new int[4 * ships.size()];
        int i = 0;
        for (Ship ship : ships) {
            for (int coord : ship.getPosition()) {
                shipsPositions[i] = coord;
                i++;
            }
        }
        return shipsPositions;
    }
    
    public boolean isValidShot(int x, int y) {
        return x >= 0 && x < size && y >= 0 && y < size ?
               !shots.contains(new Point(x, y))
             : false;
    }
    
    public Ship checkForHit(int x, int y) {
        shots.add(new Point(x, y));
        
        for (Ship ship : ships) {
            if (ship.isInPosition(x, y)) {
                ship.hit();
                
                if (ship.isSunk()) {
                    remainingShips--;
                    feedShots(ship.getArea());   
                }
                return ship;
            }
        }
        return null;
    }
        
    private void feedShots(int[] shipArea) {
        int xA1 = shipArea[0];
        int yA1 = shipArea[1];
        int xA2 = shipArea[2];
        int yA2 = shipArea[3];
                    
        for (int i = xA1; i <= xA2; i++) {
            for (int j = yA1; j <= yA2; j++) {
                Point shot = new Point(i, j);
                if (!shots.contains(shot)) {
                    shots.add(shot);
                }
            }
        }
    }
    
    public int getRemainingShips() {
        return remainingShips;
    }
}