package BSClient.client.GUI;

import BSClient.client.*;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.io.*;
import java.util.Timer;
import java.util.TimerTask;
import javax.imageio.*;
import javax.swing.*;
import javax.swing.border.*;

public class MainWindow {
    private final JFrame fMain;
    private final JPanel pCenter;
    private final JLabel lStatus;
    private final JButton bPlayAgain;
    private final Component cStatusSpacer;
    private final MenuPanel menuPanel;
    private final GamePanel gamePanel;
    private final Component cBottomSpacer;
    
    private Timer timer = new Timer();
    private BufferedImage imgBackground;
    
    public MainWindow(ClientAction clientAction, ClientReaction clientReaction) {
        clientAction.setMainWindow(this);
        clientReaction.setMainWindow(this);
        
        fMain = new JFrame();
        fMain.setTitle("Battle Ship");
        fMain.setLayout(new BorderLayout());
        fMain.setSize(new Dimension(720, 560));
        fMain.setResizable(false);
        fMain.setLocationRelativeTo(null);
        fMain.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                clientAction.quit();
            }
        });
        
        menuPanel = new MenuPanel(clientAction, clientReaction);
        menuPanel.getPanel().setBorder(new LineBorder(Color.LIGHT_GRAY, 1));
        
        try {
            imgBackground = ImageIO.read(new FileInputStream("C:\\Users\\User\\IdeaProjects\\MyKyrs1\\Client\\img\\backgro.jpg"));
        } catch (IOException e) {
             e.printStackTrace();
        }
        
        pCenter = new JPanel() {
            @Override
            public void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (imgBackground != null) {
                    //g.drawImage(imgBackground, 0, 0, null);
                    g.drawImage(imgBackground, 0, 0, pCenter.getWidth(), pCenter.getHeight(), null);
                }
            }
        };
        
        pCenter.setLayout(new BoxLayout(pCenter, BoxLayout.Y_AXIS));
        pCenter.setBorder(new LineBorder(Color.GRAY, 1));

        lStatus = new JLabel();
        lStatus.setFont(new Font(lStatus.getFont().getName(), Font.BOLD, 20));
        lStatus.setAlignmentX(JLabel.CENTER_ALIGNMENT);

        gamePanel = new GamePanel(clientAction, clientReaction);
        gamePanel.getPanel().setVisible(false);
        
        cBottomSpacer = Box.createVerticalGlue();
        cBottomSpacer.setVisible(false);
        
        bPlayAgain = new JButton(Message.PLAY_AGAIN);
        bPlayAgain.setMaximumSize(new Dimension(120, 20));
        bPlayAgain.setPreferredSize(new Dimension(120, 20));
        bPlayAgain.setVisible(false);
        bPlayAgain.addActionListener(e -> {
            clientAction.playAgain();
        });
        
        cStatusSpacer = Box.createHorizontalStrut(15);
        cStatusSpacer.setVisible(false);
        
        JPanel pMergedStatusAndButton = new JPanel();
        pMergedStatusAndButton.setOpaque(false);
        pMergedStatusAndButton.setLayout(new BoxLayout(pMergedStatusAndButton, BoxLayout.X_AXIS));
        pMergedStatusAndButton.setMaximumSize(new Dimension(600, 22));
        pMergedStatusAndButton.setPreferredSize(new Dimension(600, 22));
        pMergedStatusAndButton.add(Box.createHorizontalGlue());
        pMergedStatusAndButton.add(lStatus);
        pMergedStatusAndButton.add(cStatusSpacer);
        pMergedStatusAndButton.add(bPlayAgain);
        pMergedStatusAndButton.add(Box.createHorizontalGlue());
        
        pCenter.add(Box.createVerticalGlue());
        pCenter.add(pMergedStatusAndButton);
        pCenter.add(Box.createVerticalGlue());
        pCenter.add(gamePanel.getPanel());
        pCenter.add(cBottomSpacer);

        fMain.add(menuPanel.getPanel(), BorderLayout.NORTH);
        fMain.add(pCenter, BorderLayout.CENTER);
        fMain.setVisible(true);
    }
    
    public JFrame getFrame() {
        return fMain;
    }
    
    public void displayStatus(String text, boolean immediately) {
        timer.cancel();
        timer.purge();
        
        if (immediately) {
            lStatus.setText(text);
        } else {
            timer = new Timer();
            try {
                timer.schedule(new TimerTask() {
                   @Override
                    public void run() {
                        lStatus.setText(text);
                    } 
                }, 2000);
            } catch (IllegalStateException ex) {
                ex.printStackTrace();
            }
        }
    }
    
    public void hideGameView() {
        gamePanel.getPanel().setVisible(false);
        cBottomSpacer.setVisible(false);
        fMain.revalidate();
    }
    
    public void showGameView() {
        gamePanel.getPanel().setVisible(true);
        cBottomSpacer.setVisible(true);
        fMain.revalidate();
    }
    
    public void hidePlayAgain() {
        bPlayAgain.setVisible(false);
        cStatusSpacer.setVisible(false);
        fMain.revalidate();
    }
    
    public void showPlayAgain() {
        bPlayAgain.setVisible(true);
        cStatusSpacer.setVisible(true);
        fMain.revalidate();
    }
}