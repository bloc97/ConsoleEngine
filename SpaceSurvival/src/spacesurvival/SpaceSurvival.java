/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spacesurvival;

import java.awt.Color;
import java.awt.Frame;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import javax.swing.ImageIcon;
import spacesurvival.gui.GameScreen;
import spacesurvival.engine.console.ConsoleJPanel;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import spacesurvival.engine.console.ConsoleComponent;
import spacesurvival.gui.layers.Background;
import spacesurvival.gui.layers.BottomBar;
import spacesurvival.gui.layers.DayEndPopupOverlay;
import spacesurvival.gui.layers.EventPopupOverlay;
import spacesurvival.gui.layers.ReportPage;
import spacesurvival.gui.layers.TextCutscene;
import spacesurvival.gui.layers.TopBar;
import spacesurvival.logic.Event;

/**
 *
 * @author bowen
 */
public class SpaceSurvival {

    private static final ScheduledExecutorService ex = Executors.newSingleThreadScheduledExecutor();
    
    public static final Color MAINCOLOR = new Color(120, 146, 190);
    
    public static final GameScreen GAMESCREEN = new GameScreen();
    
    public static final Background BACKGROUNDLAYER = new Background(MAINCOLOR);
    public static final TopBar TOPBAR = new TopBar();
    public static final BottomBar BOTTOMBAR = new BottomBar(MAINCOLOR);
    public static final ReportPage REPORTPAGE = new ReportPage(MAINCOLOR);
    public static final TextCutscene TEXTCUTSCENE = new TextCutscene(MAINCOLOR);
    public static final EventPopupOverlay EVENTPOPUP = new EventPopupOverlay(MAINCOLOR);
    
    public static final DayEndPopupOverlay DAYENDPOPUP = new DayEndPopupOverlay(MAINCOLOR);
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            createAndShowGUI();
        });
        
        GAMESCREEN.addComponent(-1000, BACKGROUNDLAYER);
        
        GAMESCREEN.addComponent(10, TOPBAR);
        GAMESCREEN.addComponent(11, BOTTOMBAR);
        GAMESCREEN.addComponent(20, REPORTPAGE);
        
        GAMESCREEN.addComponent(100, TEXTCUTSCENE);
        GAMESCREEN.addComponent(900, DAYENDPOPUP);
        GAMESCREEN.addComponent(1000, EVENTPOPUP);
        
        ex.scheduleWithFixedDelay(() -> {
            TEXTCUTSCENE.tickHorizontalAnimation();
        }, 0, 8, TimeUnit.MILLISECONDS);
        ex.scheduleWithFixedDelay(() -> {
            REPORTPAGE.tickVerticalAnimation();
        }, 0, 5, TimeUnit.MILLISECONDS);
        ex.scheduleWithFixedDelay(() -> {
            BOTTOMBAR.tickHorizontalAnimation();
        }, 0, 200, TimeUnit.MILLISECONDS);
        
        Event.initAllEvents();
    }

    private static void createAndShowGUI() {
        //System.out.println("Created GUI on EDT? "+
        //SwingUtilities.isEventDispatchThread());
        JFrame frame = new JFrame("The Unfortunate Story of Hans");
        JPanel panel = new ConsoleJPanel(frame, GAMESCREEN, 60, 30, 10);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(panel);
        frame.pack();
        frame.setVisible(true);
        frame.setIconImage(new ImageIcon("resources/icon.png").getImage());
        
        
        /*
        panel.setFocusable(true);
        panel.requestFocus();
        panel.requestFocusInWindow();
        panel.setFocusTraversalKeysEnabled(false);*/

    }
}
