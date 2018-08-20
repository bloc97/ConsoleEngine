/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spacesurvival.logic;

import java.util.Random;

/**
 *
 * @author bowen
 */
public class News {
    
    private final static String weatherVeryCold = "Freezing temperatures, frostbite is likely to occur.";
    private final static String weatherCold = "Cold temperatures, a space heater is your friend.";
    private final static String weatherCool = "Temperature is cool, bring a jacket to work.";
    private final static String weatherOk = "Very comfortable temperature, ideal for outdoor activities.";
    private final static String weatherHot = "Moderate heat, beware of heat exaustion.";
    private final static String weatherVeryHot = "Extreme heat, heat exaustion can occur in less than 3 minutes if exposed.";
    
    
    private final static String weatherClear = "Clear weather, low chance of precipitations.";
    private final static String weatherPrecip = "Light precipitations.";
    private final static String weatherStorm = "Heavy precipitations with occasionnal gusts of wind.";
    private final static String weatherHeavyStorm = "Torrential downpour, it is highly advised to stay indoors.";
    
    
    
    private final static String forecastUnavailable = "- Tomorrow's forecast is unavailable.";
    private final static String forecastClear = "According to weather forecasts, tommorrow will be a clear day.";
    private final static String forecastPrecip = "Chance of precipitations tomorrow.";
    private final static String forecastFog = "High chance of fog in the next days.";
    
    private final static String newsUnavailable = "- No signal on the galactic news channel.";
    private final static String spacePiracy = "Reports of space piracy on the rise, all civilian ships should stay vigilant. We advise you to report any suspicious activities to the local authorities.";
    private final static String shipsMissing = "Reports of a few ships gone missing in the home sector, ransom letters were found in the vicinity. The authorities are currenty investigating the situation.";
    private final static String planetAlign = "The planets in the home sector will align and form a straight line, those lucky enough to live there will be able to witness this event first-hand. Planetarium exhibits will be available throughout the galaxy.";
    private final static String cargoMissing = "A cargo ship full of luxury cargo was found to be missing when it did not arrive at its destination. The client commented that the captain of that ship must have kept the cargo to himself. The captain was known to be ill-mannered. Investigators are currently on this case.";
    
    
    
    static Random random = new Random(42);
    
    public static String generateNews(Colony colony) {
        
        String news = "";
        
        if (colony.getDay() == 5) {
            news += weatherStorm + " " + weatherCold;
        } else {
            switch (random.nextInt(5)) {
                case 0:
                    news += weatherClear + " " + weatherOk;
                    break;
                case 1:
                    news += weatherPrecip + " " + weatherCool;
                    break;
                case 2:
                    news += weatherPrecip + " " + weatherHot;
                    break;
                case 3:
                    news += weatherClear + " " + weatherHot;
                    break;
                case 4:
                    news += weatherClear + " " + weatherCool;
                    break;
            }
        }
        
        switch (colony.getDay()) {
            case 3:
                news += "           " + spacePiracy;
                break;
            case 5:
                news += "           " + shipsMissing;
                break;
            case 9:
                news += "           " + planetAlign;
                break;
            case 11:
                news += "           " + cargoMissing;
                break;
            default :
                news += "           " + newsUnavailable;
            
        }
        
        
        news += "      " + forecastUnavailable;
        
        news += "                            ";
        
        return news;
    }
    
    
}
