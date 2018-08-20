/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spacesurvival;

import engine.console.ConsoleJPanel;
import java.awt.Color;
import engine.sound.AdvancedSoundClip;
import engine.sound.PlayerStream;
import engine.sound.Sound;
import engine.sound.SoundClip;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import spacesurvival.gui.GameScreen;
import spacesurvival.gui.layers.Background;
import spacesurvival.gui.layers.BottomBar;
import spacesurvival.gui.layers.DayEndPopupOverlay;
import spacesurvival.gui.layers.EventPopupOverlay;
import spacesurvival.gui.layers.ReportPage;
import spacesurvival.gui.layers.TextCutscene;
import spacesurvival.gui.layers.TopBar;

/**
 *
 * @author bowen
 */
public enum GameDisplay {
    INSTANCE;
    
    
    private final ScheduledExecutorService ex = Executors.newSingleThreadScheduledExecutor();
    
    public final Color mainColor = new Color(120, 146, 190);
    public final GameScreen gameScreen = new GameScreen();
    
    public final Background BACKGROUNDLAYER = new Background(mainColor);
    public final TopBar TOPBAR = new TopBar();
    public final BottomBar BOTTOMBAR = new BottomBar(mainColor);
    public final ReportPage REPORTPAGE = new ReportPage(mainColor);
    public final TextCutscene TEXTCUTSCENE = new TextCutscene(mainColor);
    public final EventPopupOverlay EVENTPOPUP = new EventPopupOverlay(mainColor);
    
    public final DayEndPopupOverlay DAYENDPOPUP = new DayEndPopupOverlay(mainColor);
    
    
    private GameDisplay() {
        
        gameScreen.addComponent(-1000, BACKGROUNDLAYER);
        
        gameScreen.addComponent(10, TOPBAR);
        gameScreen.addComponent(11, BOTTOMBAR);
        gameScreen.addComponent(20, REPORTPAGE);
        
        gameScreen.addComponent(100, TEXTCUTSCENE);
        gameScreen.addComponent(900, DAYENDPOPUP);
        gameScreen.addComponent(1000, EVENTPOPUP);
        
        ex.scheduleWithFixedDelay(() -> {
            TEXTCUTSCENE.tickHorizontalAnimation();
        }, 0, 8, TimeUnit.MILLISECONDS);
        ex.scheduleWithFixedDelay(() -> {
            REPORTPAGE.tickVerticalAnimation();
        }, 0, 5, TimeUnit.MILLISECONDS);
        ex.scheduleWithFixedDelay(() -> {
            BOTTOMBAR.tickHorizontalAnimation();
        }, 0, 200, TimeUnit.MILLISECONDS);
    }
    
}
