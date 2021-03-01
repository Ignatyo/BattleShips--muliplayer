package BSClient.client.GUI;

import BSClient.client.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.io.*;
import javax.imageio.*;
import javax.swing.*;

public class BoardPanel extends JPanel {
    private final int width = 10;
    private final int fieldSize = 26;
    private final int fieldStrokeSize = 1;
    private final Color cFieldStroke = new Color(64, 64, 64);
    
    private BufferedImage imgSea;
    private BufferedImage imgSplash;
    private BufferedImage imgFire;
    private BufferedImage imgShipReveal;
    private BufferedImage imgShipProwH;
    private BufferedImage imgShipProwV;
    private BufferedImage imgShipBoardH;
    private BufferedImage imgShipBoardV;
    
    private int[][] shipsFields = new int[width][width];
    private boolean[][] shotsFields = new boolean[width][width];
    
    public BoardPanel(boolean active, ClientAction clientAction) {
        int realWidth = width * fieldSize + fieldStrokeSize;
        setMinimumSize(new Dimension(realWidth, realWidth));
        setMaximumSize(new Dimension(realWidth, realWidth));
        setPreferredSize(new Dimension(realWidth, realWidth));
        
        try {
            imgSea = ImageIO.read(new FileInputStream("C:\\Users\\User\\IdeaProjects\\MyKyrs1\\Client\\img\\sea2.jpg"));
            imgSplash = ImageIO.read(new FileInputStream("C:\\Users\\User\\IdeaProjects\\MyKyrs1\\Client\\img\\splash.jpg"));
            imgFire = ImageIO.read(new FileInputStream("C:\\Users\\User\\IdeaProjects\\MyKyrs1\\Client\\img\\fire.jpg"));
            imgShipReveal = ImageIO.read(new FileInputStream("C:\\Users\\User\\IdeaProjects\\MyKyrs1\\Client\\img\\Reveal.jpg"));
            imgShipProwH = ImageIO.read(new FileInputStream("C:\\Users\\User\\IdeaProjects\\MyKyrs1\\Client\\img\\34.png"));
            imgShipProwV = ImageIO.read(new FileInputStream("C:\\Users\\User\\IdeaProjects\\MyKyrs1\\Client\\img\\ProwVer.jpg"));
            imgShipBoardH = ImageIO.read(new FileInputStream("C:\\Users\\User\\IdeaProjects\\MyKyrs1\\Client\\img\\BoardHor1.png"));
            imgShipBoardV = ImageIO.read(new FileInputStream("C:\\Users\\User\\IdeaProjects\\MyKyrs1\\Client\\img\\BoardVer.jpg"));
        } catch (IOException ex) {
            //ex.printStackTrace();
        }
        
        if (active) {
            addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent ev) {
                    int limit = width - 1;
                    int x = Math.min(ev.getX() / fieldSize, limit);
                    int y = Math.min(ev.getY() / fieldSize, limit);
                    clientAction.shot(x, y);
                }
            });
        }
        
        setVisible(true);
    }
    
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < width; j++) {
                int x = j * fieldSize;
                int y = i * fieldSize;
                g2d.setColor(cFieldStroke);
                g2d.setStroke(new BasicStroke(fieldStrokeSize));
                g2d.drawRect(x, y, fieldSize, fieldSize);
                g2d.drawImage(imgSea, x + fieldStrokeSize, y + fieldStrokeSize, 25, 25, this);
            }
        }
        
        drawShips(g2d);
        drawShots(g2d);
    }
    
    private void drawShips(Graphics2D g2d) {
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < width; j++) {
                if (shipsFields[j][i] != 0) {
                    int x = j * fieldSize;
                    int y = i * fieldSize;
                    BufferedImage imgShip;
                    
                    switch (shipsFields[j][i]) {
                        case BoardSide.PROW_H: {
                            imgShip = imgShipProwH;
                            break;
                        }
                        case BoardSide.BOARD_H: {
                            imgShip = imgShipBoardH;
                            break;
                        }
                        case BoardSide.PROW_V: {
                            imgShip = imgShipProwV;
                            break;
                        }
                        case BoardSide.BOARD_V: {
                            imgShip = imgShipBoardV;
                            break;
                        }
                        default: {
                            imgShip = imgShipReveal;
                        }
                    }
                    
                    g2d.drawImage(imgShip, x + fieldStrokeSize, y + fieldStrokeSize, 25, 25, this);
                }
            }
        }
    }
    
    private void drawShots(Graphics2D g2d) {
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < width; j++) {
                if (shotsFields[j][i]) {
                    int x = j * fieldSize;
                    int y = i * fieldSize;
                    BufferedImage imgShot = shipsFields[j][i] != 0 ? imgFire : imgSplash;
                    g2d.drawImage(imgShot, x + fieldStrokeSize, y + fieldStrokeSize, 25, 25, this);
                }
            }
        }
    }
    
    public void markShip(int x, int y, int sprite) {
        shipsFields[x][y] = sprite;
    }
    
    public void markShot(int x, int y) {
        shotsFields[x][y] = true;
    }
    
    public void clear() {
        shipsFields = new int[width][width];
        shotsFields = new boolean[width][width];
    }
}