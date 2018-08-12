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
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import javax.swing.JPanel;
import spacesurvival.characterpanels.BottomBar;
import spacesurvival.characterpanels.BuildMenu;
import spacesurvival.characterpanels.ColonyBuildings;
import spacesurvival.characterpanels.DayEndPopupOverlay;
import spacesurvival.characterpanels.EventPopupOverlay;
import spacesurvival.characterpanels.MiddleScrollBar;
import spacesurvival.characterpanels.RightScrollBar;
import spacesurvival.characterpanels.TopBar;
import spacesurvival.characterpanels.TopPopupTest;
import spacesurvival.console.CharacterPanel;
import spacesurvival.console.ConsolePanel;
import spacesurvival.console.ConsoleScreen;
import spacesurvival.logic.Logic;

/**
 *
 * @author bowen
 */
public class GamePanel extends JPanel {
    
    
    private final ConsoleScreen screen;
    
    final File[] fonts;
    int fontIndex = 0;
    
    
    public static final String TEST_STRING_CHAR = "█ ▓ ▒▒░░▐▌▌▐ ▀▄ -- __ ── ╔╣ ┌┤";
    public static final String TEST_STRING_TEXT = "A quick brown fox jumps over the lazy test dog";
    
    private final Random random = new Random(42);
    
    
    public static final Color mainColor = new Color(120, 146, 190);
    public static final BottomBar bottomBar = new BottomBar(30, 30, mainColor);
    public static final DayEndPopupOverlay dayEndOverlay = new DayEndPopupOverlay(30, 30, mainColor);
    public static final ColonyBuildings colonyBuildings = new ColonyBuildings(30, 30, mainColor, dayEndOverlay);
    public static final BuildMenu buildMenu = new BuildMenu(30, 30, mainColor);
    public static final Background background = new Background(30, 30, mainColor);
    public static final TopBar topBar = new TopBar(30, 30);
    
    
    ScheduledExecutorService ex = Executors.newSingleThreadScheduledExecutor();
    
    public GamePanel() {
        /*
        File fontFolder = new File("resources/fonts/VGA");
        fonts = fontFolder.listFiles((pathname) -> {
            return pathname.getName().toLowerCase().endsWith(".ttf");
        });*/
        
        File[] fonts0 = new File("resources/fonts/CGA").listFiles((pathname) -> {
            return pathname.getName().toLowerCase().endsWith(".ttf");
        });
        File[] fonts1 = new File("resources/fonts/VGA").listFiles((pathname) -> {
            return pathname.getName().toLowerCase().endsWith(".ttf");
        });
        
        fonts = new File[fonts0.length + fonts1.length];
        
        for (int i=0; i<fonts0.length; i++) {
            fonts[i] = fonts0[i];
        }
        for (int i=0; i<fonts1.length; i++) {
            fonts[i + fonts0.length] = fonts1[i];
        }
        
        screen = new ConsoleScreen(80, 50, ConsoleFont.fromFile(fonts[fontIndex]));
        
        screen.addCharacterPanel(-5, background);
        //screen.addCharacterPanel(2, new RightScrollBar(30, 30, mainColor));
        screen.addCharacterPanel(3, topBar);
        screen.addCharacterPanel(5, colonyBuildings);
        screen.addCharacterPanel(6, colonyBuildings.getScrollBar());
        
        screen.addCharacterPanel(10, buildMenu);
        screen.addCharacterPanel(11, buildMenu.getScrollBar());
        
        screen.addCharacterPanel(20, bottomBar);
        
        screen.addCharacterPanel(100, dayEndOverlay);
        screen.addCharacterPanel(101, new EventPopupOverlay(30, 30, mainColor));
        //screen.addCharacterPanel(4, new BottomBarOverlay(10, 10));
        //screen.addCharacterPanel(1, panel);
        
        //screen.addCharacterPanel(15, new TopPopupTest(30, 30));
        
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
                
                if (screen.getConsoleFont().getHeight() == 8) {
                    preferredConsoleWidth = 40;
                } else if (screen.getConsoleFont().getHeight() == 14) {
                    preferredConsoleWidth = 70;
                } else {
                    preferredConsoleWidth = 80;
                }
                Background.setFontHeight(screen.getConsoleFont().getHeight());
                BuildMenu.setFontHeight(screen.getConsoleFont().getHeight());
                ColonyBuildings.setFontHeight(screen.getConsoleFont().getHeight());
                EventPopupOverlay.setFontHeight(screen.getConsoleFont().getHeight());
                
                if (focusedPanel != null) {
                    focusedPanel.onKeyPressed(e);
                }
                screen.getCharacterPanels().forEach((panel) -> {
                    panel.onGlobalKeyPressed(e);
                });
                
                repaint();
            }

            @Override
            public void keyReleased(KeyEvent e) {
                if (focusedPanel != null) {
                    focusedPanel.onKeyReleased(e);
                }
                screen.getCharacterPanels().forEach((panel) -> {
                    panel.onGlobalKeyReleased(e);
                });
            }

            @Override
            public void keyTyped(KeyEvent e) {
                if (focusedPanel != null) {
                    focusedPanel.onKeyTyped(e);
                }
                screen.getCharacterPanels().forEach((panel) -> {
                    panel.onGlobalKeyTyped(e);
                });
            }
            
        });
        
        MouseAdapter mouseAdapter = new MouseAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                Point mouseConsolePoint = getMouseConsolePosition(e.getX(), e.getY());
                
                ConsolePanel newFocusedPanel = screen.getFocusedPanel(mouseConsolePoint.x, mouseConsolePoint.y);
                
                if (newFocusedPanel != focusedPanel) {
                    if (focusedPanel != null) {
                        focusedPanel.onMouseExited(mouseConsolePoint.x, mouseConsolePoint.y);
                    }
                    if (newFocusedPanel != null) {
                        newFocusedPanel.onMouseEntered(mouseConsolePoint.x, mouseConsolePoint.y);
                    }
                }
                
                focusedPanel = newFocusedPanel;
                
                if (focusedPanel != null) {
                    focusedPanel.onMouseMoved(mouseConsolePoint.x - focusedPanel.getX(), mouseConsolePoint.y - focusedPanel.getY());
                }
            }

            @Override
            public void mouseDragged(MouseEvent e) {
                Point mouseConsolePoint = getMouseConsolePosition(e.getX(), e.getY());
                
                if (focusedPanel != null) {
                    focusedPanel.onMouseDragged(mouseConsolePoint.x - focusedPanel.getX(), mouseConsolePoint.y - focusedPanel.getY(), e.getButton() == 1);
                }
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                Point mouseConsolePoint = getMouseConsolePosition(e.getX(), e.getY());
                focusedPanel = screen.getFocusedPanel(mouseConsolePoint.x, mouseConsolePoint.y);
                
                if (focusedPanel != null) {
                    focusedPanel.onMouseClicked(mouseConsolePoint.x - focusedPanel.getX(), mouseConsolePoint.y - focusedPanel.getY(), e.getButton() == 1);
                }
            }

            @Override
            public void mousePressed(MouseEvent e) {
                Point mouseConsolePoint = getMouseConsolePosition(e.getX(), e.getY());
                focusedPanel = screen.getFocusedPanel(mouseConsolePoint.x, mouseConsolePoint.y);
                
                if (focusedPanel != null) {
                    focusedPanel.onMousePressed(mouseConsolePoint.x - focusedPanel.getX(), mouseConsolePoint.y - focusedPanel.getY(), e.getButton() == 1);
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                Point mouseConsolePoint = getMouseConsolePosition(e.getX(), e.getY());
                
                if (focusedPanel != null) {
                    focusedPanel.onMouseReleased(mouseConsolePoint.x - focusedPanel.getX(), mouseConsolePoint.y - focusedPanel.getY(), e.getButton() == 1);
                }
            }
            
            @Override
            public void mouseWheelMoved(MouseWheelEvent e) {
                
                if (focusedPanel != null) {
                    focusedPanel.onMouseWheelMoved(e.getWheelRotation() * e.getScrollAmount());
                }
                
                //System.out.println(colonyBuildings.getScroll() + " " + colonyBuildings.getMaxScroll() + " " +  e.getWheelRotation() * e.getScrollAmount());
            }
            
        };
        
        this.addMouseListener(mouseAdapter);
        this.addMouseMotionListener(mouseAdapter);
        this.addMouseWheelListener(mouseAdapter);
        
        ex.scheduleWithFixedDelay(() -> {
            if (focusedPanel != null) {
                focusedPanel.onFocusTick();
            }
            screen.getCharacterPanels().forEach((panel) -> {
                panel.onGlobalTick();
            });
            repaint();
        }, 0, 10, TimeUnit.MILLISECONDS);
        
        ex.scheduleWithFixedDelay(() -> {
            repaint();
            bottomBar.tickPos();
        }, 0, 200, TimeUnit.MILLISECONDS);
        
    }
    
    private Point getMouseConsolePosition(int mouseX, int mouseY) {
        
        final int screenPixelWidth = (int)(getWidth() * scaleX);
        final int screenPixelHeight = (int)(getHeight() * scaleY);

        int customScale = SCALE;

        int consoleWidth = screenPixelWidth / screen.getConsoleFont().getWidth() / customScale;
        int consoleHeight = screenPixelHeight / screen.getConsoleFont().getHeight() / customScale;

        while (consoleWidth < preferredConsoleWidth || consoleHeight < preferredConsoleHeight) {
            customScale--;
            if (customScale <= 0) {
                customScale = 1;
                break;
            }
            consoleWidth = screenPixelWidth / screen.getConsoleFont().getWidth() / customScale;
            consoleHeight = screenPixelHeight / screen.getConsoleFont().getHeight()/ customScale;
        }


        final int xPad = (screenPixelWidth - (consoleWidth * customScale * screen.getConsoleFont().getWidth())) / 2;
        final int yPad = (screenPixelHeight - (consoleHeight * customScale * screen.getConsoleFont().getHeight())) / 2;

        if (screen.getWidth() != consoleWidth || screen.getHeight() != consoleHeight) {
            screen.setWidth(consoleWidth);
            screen.setHeight(consoleHeight);
        }

        int mouseConsoleX = ((int)(mouseX * scaleX) - xPad) / (screen.getConsoleFont().getWidth() * customScale);
        int mouseConsoleY = ((int)(mouseY * scaleY) - yPad) / (screen.getConsoleFont().getHeight() * customScale);
        
        return new Point(mouseConsoleX, mouseConsoleY);
    }
    
    @Override
    public Dimension getPreferredSize() {
        return new Dimension(1280,720);
    }
    
    private final int SCALE = 100;
    
    private int preferredConsoleWidth = 40;
    private int preferredConsoleHeight = 30;
    
    private ConsolePanel focusedPanel;
    
    private double scaleX = 1d;
    private double scaleY = 1d;
    
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        
        //Detects window scaling
        final AffineTransform t = g2.getTransform();
        scaleX = t.getScaleX();
        scaleY = t.getScaleY();
        
        final int screenPixelWidth = (int)(getWidth() * scaleX);
        final int screenPixelHeight = (int)(getHeight() * scaleY);
        
        t.scale(1d/scaleX, 1d/scaleY);
        g2.setTransform(t);
        
        int customScale = SCALE;
        
        //Sets the RenderingHints
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
        
        
        int consoleWidth = screenPixelWidth / screen.getConsoleFont().getWidth() / customScale;
        int consoleHeight = screenPixelHeight / screen.getConsoleFont().getHeight() / customScale;
        
        //while (consoleWidth < 60 || consoleHeight < 50) {
        while (consoleWidth < preferredConsoleWidth || consoleHeight < preferredConsoleHeight) {
            customScale--;
            if (customScale <= 0) {
                customScale = 1;
                break;
            }
            consoleWidth = screenPixelWidth / screen.getConsoleFont().getWidth() / customScale;
            consoleHeight = screenPixelHeight / screen.getConsoleFont().getHeight()/ customScale;
        }
        
        
        final int xPad = (screenPixelWidth - (consoleWidth * customScale * screen.getConsoleFont().getWidth())) / 2;
        final int yPad = (screenPixelHeight - (consoleHeight * customScale * screen.getConsoleFont().getHeight())) / 2;
        
        if (screen.getWidth() != consoleWidth || screen.getHeight() != consoleHeight) {
            screen.setWidth(consoleWidth);
            screen.setHeight(consoleHeight);
        }
        
        //int mouseConsoleX = ((int)(lastMouseX * scaleX) - xPad) / (screen.getConsoleFont().getWidth() * customScale);
        //int mouseConsoleY = ((int)(lastMouseY * scaleY) - yPad) / (screen.getConsoleFont().getHeight() * customScale);
        
        //focusedPanel = screen.getFocusedPanel(mouseConsoleX, mouseConsoleY);
        
        //System.out.println(mouseConsoleX + " " + mouseConsoleY);
        
        BufferedImage image = screen.getImage();
        g2.drawImage(image, xPad, yPad, image.getWidth() * customScale, image.getHeight() * customScale, this);
        
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
