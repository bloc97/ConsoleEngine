/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hans.ui;

import engine.SwingWindow;
import javax.swing.ImageIcon;

/**
 *
 * @author bowen
 */
public class HansGameWindow extends SwingWindow {

    public HansGameWindow() {
        super("The Unfortunate Story of Hans", false);
        setIconImages(new ImageIcon("resources/icon.png").getImage());
    }
    
    
    /*
    private final ScheduledExecutorService ex = Executors.newSingleThreadScheduledExecutor();
    
    public final Color mainColor = new Color(120, 146, 190);
    public final GameConsoleHandler gameScreen = new GameConsoleHandler();
    
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
    }*/
    
}
