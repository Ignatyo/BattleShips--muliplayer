package BSClient.client.GUI;

import BSClient.client.*;
import java.awt.*;
import javax.swing.*;

public class SidePanel {
    private final JPanel panel;
    private final JLabel lHeader;
    public final JLabel lStatus;
    private final BoardPanel boardPanel;
    
    public SidePanel(String header, boolean active, ClientAction clientAction) {
        panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setOpaque(false);
        
        lHeader = new JLabel(header);
        lHeader.setFont(new Font(lHeader.getFont().getName(), Font.BOLD, 14));
        lHeader.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        lStatus = new JLabel();
        lStatus.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        boardPanel = new BoardPanel(active, clientAction);
        boardPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        panel.add(lHeader);
        panel.add(Box.createVerticalStrut(5));
        panel.add(lStatus);
        panel.add(Box.createVerticalStrut(15));
        panel.add(boardPanel);
        
        panel.setVisible(true);
    }
    
    public JPanel getPanel() {
        return panel;
    }
    
    public BoardPanel getBoardPanel() {
        return boardPanel;
    }
    
    public void displayShipsCounter(int shipsCounter) {
        if (shipsCounter == 0) {
            lStatus.setText(Message.ALL_SHIPS_SANK);
        } else if (shipsCounter == 1) {
            lStatus.setText("1" + Message.SHIP_LEFT);
        } else {
            lStatus.setText(Message.SHIPS_LEFT);
        }
    }
}