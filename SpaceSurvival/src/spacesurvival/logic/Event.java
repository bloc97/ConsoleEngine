/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spacesurvival.logic;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 *
 * @author panbe
 */
public class Event {
    
    public static List<Event> allEventsList = new LinkedList<>();

    private String name;
    private String description;
    
    private final Function<Colony, Boolean> checkTrigger;
    private final Consumer<Colony> effects;
    
    private final List<EventChoice> choicesList = new ArrayList<>();
    
    private int color = 0xFFCCCCCC;
    public Event(String name, String description, Function<Colony, Boolean> checkTrigger) {
        this.name = name;
        this.description = description;
        this.checkTrigger = checkTrigger;
        this.effects = (t) -> {
        };
    }
    public Event(String name, String description, int dayTrigger, EventChoice choice, EventChoice... choices) {
        this(name, description, dayTrigger, (c) -> {}, choice, choices);
    }

    public Event(String name, String description, int dayTrigger, Consumer<Colony> effects, EventChoice choice, EventChoice... choices) {
        this(name, description, (c) -> {return c.getDay() >= dayTrigger;}, effects, choice, choices);
    }
    
    public Event(String name, String description, Function<Colony, Boolean> checkTrigger, Consumer<Colony> effects, EventChoice choice, EventChoice... choices) {
        this.name = name;
        this.description = description;
        this.checkTrigger = checkTrigger;
        this.effects = effects;
        choicesList.add(choice);
        for (EventChoice c : choices) {
            choicesList.add(c);
        }
    }
    
    
    public boolean resolveEvent(int choice, Colony colony) {
        EventChoice ec = getAvailableChoices().get(choice);
        
        if (ec.checkAvailable(colony)) {
            ec.applyEffects(colony);
            colony.getListTodayEvent().remove(this);
            colony.getListChoosed().add(ec);
            return true;
        }
        return false;
        
    } //this is the master fonction of choosing a eventchoice and applying the modifiers
    
    
    public void applyEffects(Colony colony) {
        effects.accept(colony);
    }
    
    public boolean checkTrigger(Colony colony) {
        return checkTrigger.apply(colony);
    }
    
    public List<EventChoice> getAvailableChoices() { //First choice should always be available
        List<EventChoice> choices = new LinkedList<>();
        
        if (choicesList.isEmpty()) {
            return choices;
        }
        
        choices.add(choicesList.get(0));
        
        int i = 0;
        for (EventChoice c : choicesList) {
            if (i == 0) {
                i++;
                continue;
            }
            if (c.checkAvailable(Colony.INSTANCE)) {
                choices.add(c);
            }
            i++;
        }
        return choices;
    }
    
    public static void initAllEvents() {
        
        allEventsList.add(new Event(
                "Stranded", 
                "The gigabus has crashed in a crater. Only few of the passengers and crew on board survived the brutal impact. Those who boarded escape pods are now scattered far from the crashed ship.\n\n" +
                "The ship's captain is severely injured, and is in no condition to lead. You are now the acting commander.\n\n" +
                "The communication system can still receive transmissions, but sending out signals is out of question due the extent of the damage. Only the short range S.O.S. signal is working. For the time being, you should start getting used to this planet while you wait for a way to fix the comms system.", 
                1, 
                (c) -> {
                    c.spawnBuilding(Building.crashedShip);
                    c.getBuildingsList().get(0).incrementConstructionState();
                    c.getBuildingsList().get(0).incrementConstructionState();
                    c.getBuildingsList().get(0).incrementConstructionState();
                    c.unlockBuilding(Building.mFactory);
                    c.appendReport("- The ship is broken beyond repair, workers are scrapping the ship for materials.\n");
                    
                    c.addObjective(new Objective("Find chicken nuggets for the captain.", (cl) -> {return false;}));
                },
                new EventChoice("For Aiur!"), 
                new EventChoice("It is my duty."), 
                new EventChoice("Okay...")
        ));
        
        allEventsList.add(new Event(
                "Strange Noises", 
                "As the survivors slept for their first night on this planet, some have reported hearing strange noises coming from outside the crater.\n"
                ,
                2, 
                (c) -> {
                },
                new EventChoice("Reassure them."),
                new EventChoice("Ignore the rumour.")
        ));
        
        allEventsList.add(new Event(
                "Settled", 
                "As the first factory on this planet opened its doors, everyone was cheering. Workers scrapped the crashed ship and built a town hall. \n" +
                "\"Maybe we will not die here after all.\"\n\n" +
                "Building Lost: Crashed Ship\n" +
                "Built: HQ\n" +
                "Unlocked: Parts Factory, Electronics Factory"
                ,
                (c) -> {
                    return c.checkBuildingExists(Building.mFactory) && !c.checkEventChoosedId(3);
                }, 
                (c) -> {
                    c.getBuildingsList().add(0, Building.hq.getCopy());
                    c.getBuildingsList().get(0).incrementConstructionState();
                    c.getBuildingsList().remove(1);
                    c.unlockBuilding(Building.pFactory);
                    c.unlockBuilding(Building.eFactory);
                    c.appendReport("- Crashed Ship was Scrapped, freeing 3 space.\n");
                    c.appendReport("- HQ was Built, taking up 1 space.\n");
                    c.appendReport("- Parts Factory was Unlocked\n");
                    c.appendReport("- Electronics Factory was Unlocked\n");
                },
                new EventChoice("Wonderful.", 4)
        ));
        allEventsList.add(new Event(
                "Settled", 
                "After an excruciating amount of hard work, the crashed ship was scrapped and the workers are now building a new town hall.\n" +
                "\"Maybe we will not die here after all.\"\n\n" +
                "Building Lost: Crashed Ship\n" +
                "Built: HQ\n" +
                "Unlocked: Parts Factory, Electronics Factory"
                ,
                (c) -> {
                    return c.getDay() == 4 && !c.checkEventChoosedId(4) && !c.checkBuildingExists(Building.mFactory);
                }, 
                (c) -> {
                    c.addBuilding(Building.hq.getCopy());
                    c.getBuildingsList().remove(0);
                    c.unlockBuilding(Building.pFactory);
                    c.unlockBuilding(Building.eFactory);
                    c.appendReport("- Crashed Ship was Scrapped, freeing 3 space.\n");
                    c.appendReport("- Parts Factory was Unlocked\n");
                    c.appendReport("- Electronics Factory was Unlocked\n");
                },
                new EventChoice("Wonderful.", 3)
        ));
        
        allEventsList.add(new Event(
                "In the rain", 
                "\"Commander, our people are left soaking outside in the rain! We need housing otherwise our morale will greately suffer.\"\n\n" +
                "Objective Added: Build a shelter.\n" +
                "Unlocked: Shelter"
                ,
                5, 
                (c) -> {
                    c.unlockBuilding(Building.shelter);
                    c.addObjective(new Objective("Build a shelter.", (cl) -> {return cl.checkBuildingExists(Building.shelter) || c.getDay() >= 15;}));
                    c.appendReport("- Shelter was Unlocked\n");
                },
                new EventChoice("Of course.")
        ));
        
        allEventsList.add(new Event(
                "The Natives", 
                "Recent industrial activities has angered the native creatures. A large group is headed towards the settlement. Reports estimates their arrival in 4 days.\n\n" +
                "\"Commander, we should send out an scouting team in order to find out more about the natives, or we could dispach them to clear out space to prepare for the defense.\"",
                7, 
                (c) -> {
                },
                new EventChoice("Scout.", 20),
                new EventChoice("Defend.", 21, (c) -> {
                    c.unlockBuilding(Building.bunker);
                    c.addObjective(new Objective("Have at least one Bunker ready for defense.", (cl) -> {return cl.checkBuildingExists(Building.bunker) || c.getDay() >= 12;}));
                    c.addSpaceToColony(3);
                    c.appendReport("- Bunker was Unlocked\n");
                    c.appendReport("- Three spaces were cleared by the dispached team.\n");
                })
        ));
        
        allEventsList.add(new Event(
                "Scouting Party", 
                "The rushed scouting party returns. They report that the planet they landed on is likely inhospitable for humans. The vegetation is unwelcoming and the native creatures are aggressive. You must strengthen your settlement.\n" +
                "Furthermore, a group of survivors was found and they know how to deal with the aggressive natives.\n" +
                "\"Decoys work well against them\", one of them said. \"However, the decoys will not survive a natives' attack.\"\n\n" +
                "Unlocked: Decoy, Biomass Generator",
                (c) -> {
                    return c.getDay() >= 9 && c.checkEventChoosedId(20);
                }, 
                (c) -> {
                    c.unlockBuilding(Building.decoy);
                    c.unlockBuilding(Building.bGenerator);
                    c.addObjective(new Objective("Prepare for the attack.", (cl) -> {return c.getDay() >= 12;}));
                    c.appendReport("- Decoy was Unlocked\n");
                    c.appendReport("- Biomass Generator was Unlocked\n");
                },
                new EventChoice("Prepare.", 22)
        ));
        
        allEventsList.add(new Event(
                "The Natives II", 
                "The charging natives will arrive tonight.\n\n"
                ,
                11,
                new EventChoice("We are ready.", -1, (c) -> {
                    c.addMonstersForNight(3);
                }),
                new EventChoice("Oops.", -1, (c) -> {
                    c.addMonstersForNight(2);
                })
        ));
        
        allEventsList.add(new Event(
                "Aftermath", 
                "We survived the natives' attack, however it is likely that many more will come in the future.\n" +
                "During the attack, we learned some tricks that could divert the natives' attention towards less useful targets.\n\n" +
                "Unlocked: Decoy, Biomass Generator"
                ,
                (c) -> {
                    return c.getDay() >= 12 && c.checkEventChoosedId(21);
                }, 
                (c) -> {
                    c.unlockBuilding(Building.decoy);
                    c.unlockBuilding(Building.bGenerator);
                    c.appendReport("- Decoy was Unlocked\n");
                    c.appendReport("- Biomass Generator was Unlocked\n");
                    c.addObjective(new Objective("Try to stay alive.", (cl) -> {return false;}));
                },
                new EventChoice("We survived.", 23)
        ));
        
        allEventsList.add(new Event(
                "Aftermath", 
                "We survived the natives' attack, however it is likely that many more will come in the future.\n" +
                "During the attack, we noticed that our defenses were lacking strength.\n\n" +
                "Unlocked: Bunker"
                ,
                (c) -> {
                    return c.getDay() >= 12 && c.checkEventChoosedId(22);
                }, 
                (c) -> {
                    c.unlockBuilding(Building.bunker);
                    c.appendReport("- Bunker was Unlocked\n");
                    c.addObjective(new Objective("Try to stay alive.", (cl) -> {return false;}));
                },
                new EventChoice("We survived.", 23)
        ));
        
        allEventsList.add(new Event(
                "Cramped", 
                "The available space around the crash site is becoming scarce. Building a soil reclamation facility will allow us to change nature at our will for a more appropriate construction site.\n\n" +
                "Objective Added: Build a Soil Reclamation Facility\n" +
                "Unlocked: Soil Reclamation Facility"
                ,
                (c) -> {
                    return c.getDay() >= 14 && c.checkEventChoosedId(23);
                }, 
                (c) -> {
                    c.unlockBuilding(Building.reclamationFacility);
                    c.addObjective(new Objective("Build a Soil Reclamation Facility.", (cl) -> {return cl.checkBuildingExists(Building.reclamationFacility);}));
                    c.appendReport("- Soil Reclamation Facility was Unlocked\n");
                },
                new EventChoice("Yes.")
        ));
        
        
        
        
        allEventsList.add(new Event(
                "Survivors", 
                "We have sighted a group of survivors running towards us from the edge of our crater, with monsters on their back!\n" +
                "Accepting them might expose our settlement to danger.\n\n"
                ,
                19, 
                (c) -> {
                },
                new EventChoice("Let them in.", -1, (c) -> {
                    c.addMonstersForNight(5);
                    c.appendReport("We let the survivors in.\n");
                    c.appendReport("The natives were seen charging towards our direction.\n");
                }),
                new EventChoice("Refuse.", -1, (c) -> {
                    c.addMonstersForNight(2);
                    c.appendReport("The survivors were mauled to death, no one survived.\n");
                    c.appendReport("A small group of natives were seen charging towards our direction.\n");
                }),
                new EventChoice("Use a decoy.", -1, (c) -> {
                    return c.checkBuildingExists(Building.decoy);
                }, (c) -> {
                    for (Building b : c.getBuildingsList()) {
                        if (b.getName().equals(Building.decoy)) {
                            c.getBuildingsList().remove(b);
                            break;
                        }
                    }
                    c.setColonyLostSpace(c.getColonyLostSpace() + 1);
                })
        ));
        
        
        allEventsList.add(new Event(
                "The Others", 
                "\"Commander, after what happened yesterday, I think it is our duty to save the other survivors of the crash, we should build a beacon to signal the rally point.\"\n\n" +
                "Objective Added: Build a beacon to signal the rally point.\n" +
                "Unlocked: Beacon"
                ,
                20, 
                (c) -> {
                    c.unlockBuilding(Building.beacon);
                    c.appendReport("- Beacon was unlocked.");
                    c.addObjective(new Objective("Build a Beacon.", (t) -> {return t.checkBuildingExists(Building.beacon);}));
                },
                new EventChoice("It is our duty."),
                new EventChoice("Sure.")
        ));
        
        allEventsList.add(new Event(
                "The Shining Light", 
                "With everyone gathering around the newly constructed beacon, a shining ray of light plows through the clouds, illuminating the sky far and wide. Those who wish to see their lost loved ones just might have their wishes fulfilled.\n" +
                "With the completion of the beacon, we expect many more survivors to join us in the near future.\n"
                ,
                (c) -> {
                    return c.checkBuildingExists(Building.beacon);
                },
                (c) -> {
                },
                new EventChoice("Today is a great day.")
        ));
        
        
        allEventsList.add(new Event(
                "Housing Crisis", 
                "An angry crowd of people has gathered around the town hall demanding housing for their wives and children.\n" +
                "With so many homeless, we expect many more to join the crowd.\n"
                ,
                (c) -> {
                    return c.getDay() == 15 && !c.checkBuildingExists(Building.shelter);
                },
                (c) -> {
                },
                new EventChoice("Housing for everyone.", 40, (c) -> {
                    c.addObjective(new Objective("Build two Shelters before day 25.", (t) -> {return t.checkBuildingExists(Building.shelter, 2) || t.getDay() >= 25;}));
                }),
                new EventChoice("Disperse the crowd.", 41),
                new EventChoice("Ignore.", 41)
        ));
        
        allEventsList.add(new Event(
                "Housing Crisis Resolved", 
                "After building the promised shelters, confidence towards the leadership was resolved.\n"
                ,
                (c) -> {
                    return (c.checkEventChoosedId(40) && c.checkBuildingExists(Building.shelter, 2) && c.getDay() <= 25) || (c.checkEventChoosedId(42) && c.checkBuildingExists(Building.shelter, 3));
                },
                (c) -> {
                },
                new EventChoice("Great.")
        ));
        
        allEventsList.add(new Event(
                "Housing Crisis II", 
                "Due to unpopular political decisions and dissatisfaction among the citizens, riots are taking place in the streets.\n"
                ,
                (c) -> {
                    return c.getDay() == 25 && (c.checkEventChoosedId(41) || (c.checkEventChoosedId(40) && !c.checkBuildingExists(Building.shelter, 2)));
                },
                (c) -> {
                },
                new EventChoice("Build extra housing.", 42, (c) -> {
                    c.addObjective(new Objective("Have at least three Shelters before day 30.", (t) -> {return t.checkBuildingExists(Building.shelter, 3) || t.getDay() >= 30;}));
                }),
                new EventChoice("We must contain it.", 43, (c) -> {
                    c.unlockBuilding(Building.riotControlCenter);
                    c.appendReport("- Riot Control Center was unlocked.");
                    c.addObjective(new Objective("Build a Riot Control Center.", (t) -> {return t.checkBuildingExists(Building.riotControlCenter) || t.getDay() >= 30;}));
                }),
                new EventChoice("Ignore.", 44)
        ));
        
        allEventsList.add(new Event(
                "Anarchy", 
                "The citizen have had enough of the incompetent leadership and are taking arms to start a revolution!\n" +
                "Anarchy has befallen on the settlement, every corner of the colony was plundered by looters."
                ,
                (c) -> {
                    return c.getDay() == 30 && (c.checkEventChoosedId(44) || (c.checkEventChoosedId(43) && !c.checkBuildingExists(Building.riotControlCenter, 1)) || (c.checkEventChoosedId(42) && !c.checkBuildingExists(Building.shelter, 3)));
                })
        );
        
        allEventsList.add(new Event(
                "Ahoy", 
                "A new transmission was picked up: \"Ahoy, Matey. We arrr a patroll ship and we have come across your S.O.S signal. Please deactivate yourr anti-air defenses to let us land and provide assistance.\"\n"
                ,
                22,
                (c) -> {
                },
                new EventChoice("Agree.", -1, (c) -> {
                    c.appendReport("The \"Patroll ship\" was actually a pirate ship, they plundered one of our factories and dumped all of their radioactive waste in our settlement!\n");
                    c.appendReport("- Two Debris was added due to radioactive waste.");
                    c.setColonyLostSpace(c.getColonyLostSpace() + 2);
                }),
                new EventChoice("\"We are fine.\"", -1, (c) -> {
                    c.appendReport("The strange ship left without saying anything.\n");
                }),
                new EventChoice("Retaliate after landing.", -1, (c) -> {
                    c.appendReport("We succesfully fought off the invaders, but as they were leaving in a hurry, they dumped radioactive waste overboard!\n");
                    c.appendReport("- One Debris was added due to radioactive waste.");
                    c.setColonyLostSpace(c.getColonyLostSpace() + 1);
                })
        ));
        
        allEventsList.add(new Event(
                "Strange Meat", 
                "Newly opened shabby-looking restaurants are serving mysterious, but delicious meat to the public.",
                10, 
                (c) -> {
                },
                new EventChoice("Intriguing.", 9)
        ));
        allEventsList.add(new Event(
                "Strange Meat II", 
                "Your men discovered that the mysterious meat is from one of the native creatures living in the waste. The people consuming it ignore what effect these meat have on their body, but one thing is for sure, the demand for the meat is high.\n\n" +
                "Even the wealthest are ready to give out their land for a piece of the meat.",
                (c) -> {
                    return c.getDay() >= 13 && c.checkEventChoosedId(9);
                }, 
                (c) -> {
                },
                new EventChoice("Ban the meat.", 10),
                new EventChoice("Sell some carcass.", 11)
        ));
        allEventsList.add(new Event(
                "Public Health Issue", 
                "Report of increasing number of citizens falling ill. Symptoms include loss of hair, heavy vomiting and heat exaustion. The dispached public safety team have narrowed it down to the food supply.\n\n" +
                "Land clearing might be slower while citizens are sick at home.",
                (c) -> {
                    return c.getDay() >= 16 && c.checkEventChoosedId(11);
                }, 
                (c) -> {
                },
                new EventChoice("Inquire.", 10),
                new EventChoice("Oh dear.", 10)
        ));
        allEventsList.add(new Event(
                "Strange Meat III", 
                "Extensive research has shown that the consumption of the natives' meat is highly dangerous and toxic to the human body. The symptoms are similar to radioactive poisoning.\n\n" + 
                "The meat was banned and an antidote was quickly developped and distributed to those affected.\n" +
                "There was discontent among those affected.",
                (c) -> {
                    return c.getDay() >= 17 && c.checkEventChoosedId(10);
                }, 
                (c) -> {
                },
                new EventChoice("Good ridance.")
        ));
        /*
                    c.unlockBuilding(Building.beacon);
                    c.addObjective(new Objective("Build a beacon.", (cl) -> {return cl.checkBuildingExists(Building.beacon);}));
        */
            /*
            Event event1welcome = new Event("Welcome","This is LD42");
            EventChoice EV11 = new EventChoice("Ok",11);
            event1welcome.getListChoice().add(EV11);
            masterEventList.add(event1welcome);
            //
            Event event100CivilUnrest = new Event("Civil Unrest", "Due to unpopular political decision and unsatisfaction among the citizen, riot are taking place in the street");
            event100CivilUnrest.setRequiredHapinessLower(15);
            event100CivilUnrest.setRequiredHapinessUpper(35);
            EventChoice EV1001 = new EventChoice("Send the army",1001);
            EventChoice EV1002 = new EventChoice("Do nothing",1002);
            event100CivilUnrest.getListChoice().add(EV1001);
            event100CivilUnrest.getListChoice().add(EV1002);
            masterEventList.add(event100CivilUnrest);
           //
           Event event101Revolt = new Event("Revolt", "The citizen have enough of the incompetent leadership and are taking arms to start a revolution!");
            event101Revolt.setRequiredHapinessLower(0);
            event101Revolt.setRequiredHapinessUpper(5);
            EventChoice EV1011 = new EventChoice("Send the army",1011);
            EventChoice EV1012 = new EventChoice("Do nothing",1012);
            event101Revolt.getListChoice().add(EV1011);
            event101Revolt.getListChoice().add(EV1012);
            masterEventList.add(event101Revolt);
           //
            Event event200StrangeMeat = new Event("Strange Meat","Newly opened shabby looking restaurants are serving delicious mysterious meat to the public.");
            event200StrangeMeat.setEventTriggeringDay(6);
            EventChoice EV2001 = new EventChoice("Investigate",2001);
            event200StrangeMeat.getListChoice().add(EV2001);
            masterEventList.add(event200StrangeMeat);
            //
            Event event201StrangeMeat = new Event("Strange Meat II", "After investigation, your men discovered that the mysterious meat is from the native monster living in the waste on the planet. The scientist ignore what effect the consumming of these meat have on human body, but one thing is sure, the demand for the meat is high.  the most wealthest are ready to giveout their land for a piece of the meat.");
           event201StrangeMeat.setEventTriggeringDay(7);
           event201StrangeMeat.setRequiredPreviousEvent(true);
           event201StrangeMeat.setEventRequiredEventChoice(EV2001);
           EventChoice EV2011 = new EventChoice("Ban the meat",2011);
           EventChoice EV2012 = new EventChoice("Sell some carcass",2012);
           event201StrangeMeat.getListChoice().add(EV2011);
           event201StrangeMeat.getListChoice().add(EV2012);
           masterEventList.add(event201StrangeMeat);
           
           Event event204StrangeMeat = new Event("Strange Meat III","After the ban of the meat,intensive research was made and show that the consumming of the meat is highly toxic to the human body. Symptoms are similar to radioactive poisoning.");
           event204StrangeMeat.setEventTriggeringDay(8);
           event204StrangeMeat.setRequiredPreviousEvent(true);
           event204StrangeMeat.setEventRequiredEventChoice(EV2011);
           EventChoice EV2041 = new EventChoice("Good ridance",2041);
           event204StrangeMeat.getListChoice().add(EV2041);
            masterEventList.add(event204StrangeMeat);
           
           Event event202StrangeMeat = new Event("Public Health Issue","Report of increasing number of citizens falling ill, including symptom are loss of hair, heavy vomiting, heat.");
           event202StrangeMeat.setEventTriggeringDay(9);
           event202StrangeMeat.setRequiredPreviousEvent(true);
           event202StrangeMeat.setEventRequiredEventChoice(EV2012);
           EventChoice EV2021 = new EventChoice("Investigate",2021);
           event202StrangeMeat.getListChoice().add(EV2021);
            masterEventList.add(event202StrangeMeat);
            
            Event event203StrangeMeat = new Event("Strange Meat IV","After full investigation on the previous medical problem amount the citizens, the scientist found out all persons failling ill had consummed the monster meat,  since the last report of the illness, some ill had died.");
           event203StrangeMeat.setEventTriggeringDay(10);
           event203StrangeMeat.setRequiredPreviousEvent(true);
           event203StrangeMeat.setEventRequiredEventChoice(EV2021);
           EventChoice EV2031 = new EventChoice("Ban the meat",2031);
           event203StrangeMeat.getListChoice().add(EV2031);
            masterEventList.add(event203StrangeMeat);
           */
    }//need to called the fonction somewhere


    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }



    public int getColor() {
        return color;
    }
    
    int backgroundColor = 0xFFEEEEEE;
    
    public int getBackgroundColor() {
        return backgroundColor;
    }

    public void setColor(int foregroundColor, int backgroundColor) {
        this.color = foregroundColor;
        this.backgroundColor = backgroundColor;
        useDefaultColor = false;
    }
    
    private boolean useDefaultColor = true;
    
    public boolean useDefaultColor() {
        return useDefaultColor;
    }
    
    
    
}
