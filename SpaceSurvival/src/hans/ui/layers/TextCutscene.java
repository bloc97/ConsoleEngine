/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hans.ui.layers;

import java.awt.Color;
import java.awt.event.KeyEvent;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import hans.ui.HansGameWindow;
import engine.console.ConsoleJPanel;
import console.CharacterImage;
import console.StringWriter;
import hans.ui.HansGameLayer;
import hans.game.Colony;
import hans.game.Event;
import hans.game.EventChoice;

/**
 *
 * @author bowen
 */
public class TextCutscene extends HansGameLayer {
    
    
    public final static String[] cutscene = new String[] {
        "As humanity mastered space travel, it did not lose time to spread itself throughout the galaxy. The once idle universe was soon filled with spaceships of all shapes and sizes cruising between systems, transporting goods and people.",
        "Aboard the gigabus SP4-C3, sailing for Sector 6UD-7H3M3, passengers are eagerly waiting for their arrival at home, where their family are preparing to welcome them at their long anticipated return.",
        "Meanwhile, the captain of the ship, an opportunistic hoarder, has been grumbling about food variety to his second-in-command.",
        "\"Hans!\"",
        "\"Hans, why do I have to eat this crap every day?!\"",
        "\"My captain, with all due respect, you were the one who ordered the deconstruction of the kitchen facilities. You insisted to carry extra luxury cargo despite the fact that we ran out of space... Beside, food is not permitted on the bri-\"",
        "\"Silence, fool! Go fetch me some chicken nuggets, right now!\"",
        
        "",
        
        "\"Yes my captain, Right away!\"",
        "* Objective Added: find some chicken nuggets for the captain. *",
        "You took too long to find the chicken nuggets - of course: as there weren't any on the ship to begin with - the captain went on a rampage.",
        "As the captain let out his anger, he slammed his soylent drink on the dashboard. The force sends the content of the bottle all over the pilot's console, causing a short circuit. Out of control, the SP4-C3 heads to a nearby planet at full throttle...",
        
        "My captain, you WILL cease your tantrum and act responsibly for once! There are NO chicken nuggets on this ship, so forget about it!",
        "HOW DARE YOU!",
        "The captain forcefully slams his soylent drink on the dashboard. The force sends the content of the bottle all over the pilot's console, causing a short circuit. Out of control, the SP4-C3 heads to a nearby planet at full throttle...",
        
        
        "...",
        "Something explodes near your head and you lose consciousness.",
        "...",
        "...",
        "With your head still hurting, you open your eyes and slowly get up. The captain is lying in front of you, his face distorted by his painful expression. He calls for your help to carry him outside. As you comply, you notice that he is severely injured.",
        "Gathering your strength, you pull the captain and yourself out of the wreck. There were already other survivors outside, and as soon as they see you two, they all come to gather around, innocently seeking guidance and wanting to know the cause of this disaster. However, the captain was in no shape to take the situation in hand. He looks at you in the eyes.",
        
        "\"Hans. I know you are someone responsible. You make smart decisions, you are always eager to help others. I shall trust you with my passengers, and of course, my luxury cargo. People are going to count on you... Don't forget, I'm still counting on you for my chicken nuggets.\"",
            
        "* Objective Added: find some chicken nuggets for the captain *",
    };
    
    private int currentLine = 0;
    private int currentStop = 0;
    private boolean chickenNuggets = true;
    private boolean isDone = false;
    
    public final static String[] cutscenePerson = new String[] {
        "",
        "",
        "",
        "Captain",
        "Captain",
        "Hans",
        "Captain",
        
        "",
        
        "Hans",
        "",
        "",
        "",
        
        "Hans",
        "Captain",
        "",
            
            
        "",
        "",
        "",
        "",
        "",
        "",
    
        "Captain",
        
        "",
        
    };
    
    
    public TextCutscene(Color mainColor) {
        super();
    }

    
    
    private void genImage() {
        getCharacterImage().clear();
        getCharacterImage().fillBackgroundColor(0xFF302040);
        getCharacterImage().fillRectangleChar(0, getHeight() / 2, getWidth(), getHeight() - (getHeight() / 2), ' ');
        getCharacterImage().drawRectangle(0, getHeight() / 2, getWidth(), getHeight() - (getHeight() / 2));
        getCharacterImage().fillRectangleForegroundColor(0, getHeight() / 2, getWidth(), getHeight() - (getHeight() / 2), 0xFFEEEEEE);
        
        getCharacterImage().drawString(cutscenePerson[currentLine], 2, getHeight() / 2, 0xFFEEEEEE);
        
        final StringWriter stringWriter = getCharacterImage().createStringWriter();
        
        stringWriter.setBoundary(3, getHeight() / 2 + 2, getWidth() - 6, getHeight() - (getHeight() / 2) - 4);
        stringWriter.setMaxCharsWritten(currentStop);
        stringWriter.writeString(cutscene[currentLine], 0xFFEEEEEE);
        //getCharacterImage().drawStringWrapWordPaddedStopAt(cutscene[currentLine], 3, getHeight() / 2 + 2, 3, 3, 0xFFEEEEEE, currentStop);
        
        
    }
    
    public void tickHorizontalAnimation() {
        currentStop++;
    }
    
    
    public void nextLine() {
        
        if (isDone) {
            return;
        }
        
        if (currentStop < cutscene[currentLine].length() + 10) {
            currentStop = cutscene[currentLine].length() + 10;
            genImage();
            return;
        }
        
        if (currentLine == 6) {
            EventChoice yes = new EventChoice("Of course.", -1, (t) -> {
                currentLine = 8;
                currentStop = 0;
            });
            EventChoice no = new EventChoice("No.", -1, (t) -> {
                currentLine = 12;
                chickenNuggets = false;
                currentStop = 0;
            });
            yes.setColor(0xFF00EE00);
            no.setColor(0xFFEE0000);
            
            Event thisEvent = new Event("Chicken Nuggets", "Look for chicken nuggets for our good captain?", -1, (t) -> {
            }, yes, no);
            thisEvent.setColor(0xFFEEEEEE, 0xFF302040);
            
            HansGameWindow.INSTANCE.EVENTPOPUP.show(thisEvent);
            
            return;
        }
        
        if (currentLine == 11) {
            currentLine = 14;
        }
        
        if (currentLine == 21 && chickenNuggets) {
            currentLine = 22;
        }
        
        if (currentLine >= cutscene.length - 1) {
            currentLine = 0;
            isDone = true;
            
            /** DISABLED TEMPORARILY
            dayEndOverlay.nextDay();
            **/
            Colony.INSTANCE.nextDay();
            HansGameWindow.INSTANCE.REPORTPAGE.maximize();
            
            hide();
            disable();
            return;
        }
        currentLine++;
        currentStop = 0;
    }
    

    @Override
    public boolean onMousePressed(int x, int y, boolean isLeftClick, boolean isEntered, boolean isFocused) {
        if (isEntered) {
            nextLine();
            return true;
        }
        return false;
    }

    @Override
    public boolean onKeyPressed(KeyEvent e, boolean isEntered, boolean isFocused) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_SPACE:
            case KeyEvent.VK_ENTER:
                nextLine();
                break;
            default :
                break;
        }
        return true;
    }

    @Override
    public boolean onPrePaintTick(int mouseX, int mouseY, boolean isEntered, boolean isFocused) {
        genImage();
        return true;
    }

    
    
    
    
    
    
    
}
