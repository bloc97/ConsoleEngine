/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spacesurvival.characterpanels;

import java.awt.Color;
import spacesurvival.GamePanel;
import static spacesurvival.GamePanel.dayEndOverlay;
import spacesurvival.console.CharacterImage;
import spacesurvival.console.CharacterPanel;
import spacesurvival.logic.Event;
import spacesurvival.logic.EventChoice;

/**
 *
 * @author bowen
 */
public class TextCutscene extends CharacterPanel {
    
    
    public final static String[] cutscene = new String[] {
        "As humanity mastered space travel, it did not lose time to spread itself throughout the galaxy. The once idle universe is soon filled with spaceships of all shapes and sizes cruising between systems, transporting goods and people.",
        "Aboard the gigabus SP4-C3, sailing for Sector 6UD-7H3M3, passengers are eagerly waiting for their arrival at home, where their family are preparing to welcome them at their long anticipated return.",
        "Meanwhile, the captain of the ship, an opportunistic hoarder, has been grumbling about food variety to his second-in-command.",
        "\"Hans!\"",
        "\"Hans, why do I have to eat this crap every day?!\"",
        "\"My captain, with all due respect, you were the one who ordered the deconstruction of the kitchen facility. You insisted to carry extra luxury cargo despite the fact that we ran out of space... Beside, food is not permitted on the bri-\"",
        "\"Silence, fool! Go fetch me some chicken nuggets, right now!\"",
        
        "",
        
        "\"Yes my captain, Right away!\"",
        "* Objective: find some chicken nuggets for the captain. *",
        "You took too long to find the chicken nuggets - of course: there weren't any on the ship to begin with - and the captain went on a rampage.",
        "As the captain let out his anger, he slammed his soylent drink on the dashboard. The force sends the content of the bottle all over the electronics, causing a short circuit. Out of control, the SP4-C3 heads to a nearby planet at full throttle…",
        
        "My captain, you WILL cease your tantrum and act responsibly for once! There is NO chicken nugget on this ship, so forget about it!",
        "HOW DARE YOU!",
        "The captain forcefully slams his soylent drink on the dashboard. The force sends the content of the bottle all over the electronics, causing a short circuit. Out of control, the SP4-C3 heads to a nearby planet at full throttle...",
        
        
        "...",
        "Something explodes near your head and you lose consciousness.",
        "...",
        "...",
        "With the head still hurting, you open your eyes and slowly get up. The captain is lying in front of you, his face distorted by pain. He calls for your help to carry him outside. As you comply, you notice that he is severely injured.",
        "Gathering your strength, you pull the captain and yourself out of the wreck. There were already other survivors, and as soon as they see you two, they all come to gather around, innocently seeking leadership and guidance from the cause of this disaster. However, the captain was in no shape to take the situation in hand. He looks at you in the eyes.",
        
        "\"Hans. I know you are someone responsible. You make smart decisions, you are always eager to help others. I shall trust you with my passengers, and my luxury cargos of course. People are going to count on you... And I'm still counting on you for my chicken nuggets.\"",
            
        "* Objective: find some chicken nuggets for the captain *",
        
        
    };
    
    private int currentLine = 0;
    private int currentStop = 0;
    private boolean chickenNuggets = true;
    
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
    
    
    
    public TextCutscene(int width, int height, Color mainColor) {
        super(0, 0, width, height);
        genImage();
    }

    @Override
    public void onScreenDimensionChange(int newWidth, int newHeight, int oldWidth, int oldHeight) {
        this.setCharacterImage(new CharacterImage(newWidth, newHeight));
        genImage();
    }
    
    
    public void genImage() {
        getCharacterImage().fillBackgroundColor(0xFF302040);
        getCharacterImage().fillRectangle(0, getHeight() / 2, getWidth(), getHeight() - (getHeight() / 2), ' ');
        getCharacterImage().drawRectangle(0, getHeight() / 2, getWidth(), getHeight() - (getHeight() / 2));
        getCharacterImage().fillForegroundColorRectangle(0, getHeight() / 2, getWidth(), getHeight() - (getHeight() / 2), 0xFFEEEEEE);
        
        getCharacterImage().drawString(cutscenePerson[currentLine], 2, getHeight() / 2, 0xFFEEEEEE);
        getCharacterImage().drawStringSpaceWrapPadStopAt(cutscene[currentLine], 3, getHeight() / 2 + 2, 3, 3, 0xFFEEEEEE, currentStop);
    }
    
    public void addStop() {
        currentStop++;
        genImage();
    }
    
    
    public void nextLine() {
        if (currentStop <= cutscene[currentLine].length() + 10) {
            currentStop = cutscene[currentLine].length() + 10;
            genImage();
            return;
        }
        
        if (currentLine == 6) {
            GamePanel.eventPopup.show(new Event("Chicken Nuggets", "Look for chicken nuggets for our good captain?", -1, (t) -> {
            }, new EventChoice("Of course.", -1, (t) -> {
                currentLine = 8;
            }), new EventChoice("No.", -1, (t) -> {
                currentLine = 12;
                chickenNuggets = false;
            })));
        }
        
        if (currentLine == 10) {
            currentLine = 13;
        }
        
        if (currentLine == 22 && !chickenNuggets) {
            currentLine = 23;
        }
        
        if (currentLine >= cutscene.length - 1) {
            currentLine = cutscene.length - 1;
            dayEndOverlay.nextDay();
            hide();
            return;
        }
        currentLine++;
        currentStop = 0;
        genImage();
    }
    
    public void hide() {
        setX(-1000000);
    }

    @Override
    public void onMousePressed(int x, int y, boolean isLeftClick) {
        nextLine();
    }
    
    
    
    
}