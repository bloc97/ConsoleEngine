/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spacesurvival;

import spacesurvival.characterpanels.Background;
import spacesurvival.console.ConsoleFont;
import spacesurvival.console.CharacterImage;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import spacesurvival.characterpanels.BottomBar;
import spacesurvival.characterpanels.BottomBarOverlay;
import spacesurvival.characterpanels.ColonyBuildings;
import spacesurvival.characterpanels.MiddleScrollBar;
import spacesurvival.characterpanels.RightScrollBar;
import spacesurvival.characterpanels.TopBar;
import spacesurvival.characterpanels.TopPopupTest;
import spacesurvival.console.CharacterPanel;
import spacesurvival.console.ConsoleScreen;

/**
 *
 * @author bowen
 */
public class GamePanel extends JPanel {
    
    
    public static int RECOMMENDED_CONSOLE_WIDTH = 100;
    
    private final ConsoleScreen screen;
    private final CharacterPanel panel;
    
    final File[] fonts;
    int fontIndex = 0;
    
    
    public static final String TEST_STRING_CHAR = "█ ▓ ▒▒░░▐▌▌▐ ▀▄ -- __ ── ╔╣ ┌┤";
    public static final String TEST_STRING_TEXT = "A quick brown fox jumps over the lazy test dog";
    
    private final Random random = new Random(42);
    
    public GamePanel() {
        
        File fontFolder = new File("resources/fonts/CGA");
        
        fonts = fontFolder.listFiles((pathname) -> {
            return pathname.getName().toLowerCase().endsWith(".ttf");
        });
        
        panel = new CharacterPanel(1, 2, 10, 10) {
            @Override
            public void onScreenDimensionChange(int newWidth, int newHeight, int oldWidth, int oldHeight) {
                this.setCharacterImage(new CharacterImage(newWidth-2, newHeight-4));
            }
        };
        screen = new ConsoleScreen(80, 50, ConsoleFont.fromFile(fonts[fontIndex]));
        Color mainColor = new Color(120, 146, 190);
        
        screen.addCharacterPanel(-5, new Background(30, 30, mainColor));
        screen.addCharacterPanel(1, new MiddleScrollBar(30, 30, mainColor));
        screen.addCharacterPanel(2, new RightScrollBar(30, 30, mainColor));
        screen.addCharacterPanel(3, new TopBar(30, 30));
        screen.addCharacterPanel(4, new BottomBar(30, 30));
        screen.addCharacterPanel(-1, new ColonyBuildings(30, 30));
        //screen.addCharacterPanel(4, new BottomBarOverlay(10, 10));
        //screen.addCharacterPanel(1, panel);
        
        setBackground(Color.BLACK);
        //setBorder(BorderFactory.createLineBorder(Color.BLACK));
        
        this.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_RIGHT:
                        fontIndex++;
                        if (fontIndex >= fonts.length) {
                            fontIndex = fonts.length - 1;
                        }
                        break;
                    case KeyEvent.VK_LEFT:
                        fontIndex--;
                        if (fontIndex < 0) {
                            fontIndex = 0;
                        }
                        break;
                    default:
                        break;
                }
                screen.setConsoleFont(ConsoleFont.fromFile(fonts[fontIndex]));
                repaint();
            }
        });
        
        ex.scheduleWithFixedDelay(() -> {
            repaint();
            ((BottomBar)screen.getCharacterPanel(4)).tickPos();
        }, 0, 500, TimeUnit.MILLISECONDS);
        
    }

    
    
    @Override
    public Dimension getPreferredSize() {
        return new Dimension(1800,1000);
    }

    ScheduledExecutorService ex = Executors.newSingleThreadScheduledExecutor();
    
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        
        //Detects window scaling
        final AffineTransform t = g2.getTransform();
        final double scaleX = t.getScaleX();
        final double scaleY = t.getScaleY();
        
        final int screenPixelWidth = (int)(getWidth() * scaleX);
        final int screenPixelHeight = (int)(getHeight() * scaleY);
        
        t.scale(1d/scaleX, 1d/scaleY);
        g2.setTransform(t);
        
        
        
        //Sets the RenderingHints
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
        
        int scale = 100;
        
        int consoleWidth = screenPixelWidth / screen.getConsoleFont().getWidth() / scale;
        int consoleHeight = screenPixelHeight / screen.getConsoleFont().getHeight() / scale;
        
        //while (consoleWidth < 60 || consoleHeight < 50) {
        while (consoleWidth < 40 || consoleHeight < 30) {
            scale--;
            if (scale <= 0) {
                scale = 1;
                break;
            }
            consoleWidth = screenPixelWidth / screen.getConsoleFont().getWidth() / scale;
            consoleHeight = screenPixelHeight / screen.getConsoleFont().getHeight()/ scale;
        }
        
        
        final int xPad = (screenPixelWidth - (consoleWidth * scale * screen.getConsoleFont().getWidth())) / 2;
        final int yPad = (screenPixelHeight - (consoleHeight * scale * screen.getConsoleFont().getHeight())) / 2;
        
        if (screen.getWidth() != consoleWidth || screen.getHeight() != consoleHeight) {
            screen.setWidth(consoleWidth);
            screen.setHeight(consoleHeight);
        }
        
        BufferedImage image = screen.getImage();
        g2.drawImage(image, xPad, yPad, image.getWidth() * scale, image.getHeight() * scale, this);
        /*
        for (int j=0; j<panel.getHeight(); j++) {
            for (int i=0; i<panel.getWidth(); i++) {
                panel.getCharacterImage().setForegroundColor(i, j, random.nextInt() | 0xFF000000);
                if (random.nextInt(100) < 50) {
                    panel.getCharacterImage().setChar(i, j, ConsoleFont.cp437ToUnicode(random.nextInt(0xFF)));
                } else {
                    panel.getCharacterImage().setChar(i, j, ' ');
                }
            }
        }
        panel.getCharacterImage().drawStringWrap(TEST_STRING_TEXT, 0, 0);
        
        g2.setColor(Color.WHITE);*/
    }
    
    public int roundRatio(double ratio) {
        if (ratio < 1) {
            return 1;
            //return 1d/((int)(1d/ratio));
        } else {
            return (int) ratio;
        }
        
        
    }
    
}
