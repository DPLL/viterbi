package com.example.voicerecognition2;

import java.util.HashMap;
import java.util.Map;

/** This class contains a simple medical model stored in a hashmap.
 * With the category name as the key, and list of keywords as values. 
 * 
 * @author wang44
 * @date March 1, 2013
 */
public class SimpleMedicalModel {

    public static String [] devices = {
        "IV", "intravenous", 
        "ventilator", 
        "catheter", 
        "mask", 
        "ET tube", 
        "EKG"
    };
    public static String [] drugs = {
        "saline", 
        "levophed", //TODO: train words, may be profile specific (MacSpeech can't understand this one)
        "epinephrine", 
        "oxygen", 
        "bicarbonate",
        "lidocaine",
        "lasix"
	};
    public static String [] physiology = {
        "BP", "blood pressure", 
        "HR", "heart rate", //TODO: abbreviations need special attn (e.g. throw will trigger HR)
        "O2 Sat", "oxygen sat",
        "CO2", "CO 2", "carbon dioxide"
    };
    public static String [] rhythm = {
        "regular sinus rhythm",
        "asytole",
        "flatline", "flat line",
        "PVC",
        "atrial fibrillation"
    };
    public static String [] patient = {
        "awake",
        "pain",
        "breathing",
        "moving"
    };
    public static String [] verbs = {
        "put in",
        "working",
        "connect"
    };
    public static String [] adjectives = {
        "high",
        "low",
        "good",
        "bad",
        "not measured", //TODO: parse NOT + other things
        "no response"
    };
	
    /** Create an return the medical model in a hashmap
     * 
     * @return Map<String, String[]>
     */
    public static Map<String, String []> getModel () {
        Map <String, String[]> model = new HashMap<String, String[]>();
        model.put("devices", devices);
        model.put("drugs", drugs);
        model.put("physiology", physiology);
        model.put("rhythm", rhythm);
        model.put("patient", patient);
        model.put("verbs", verbs);
        model.put("adjectives", adjectives);
        return model;
    }
}
