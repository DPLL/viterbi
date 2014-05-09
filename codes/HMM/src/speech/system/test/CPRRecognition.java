package speech.system.test;

/*
 * 
 * CPRRecognition.java is the right for for testing the speech recognition performance based on the 
 * complete workflow in the ICU. 
 *  
 */

import java.awt.List;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.Hashtable;

 
public class CPRRecognition
{
	// state variable
	static final String ZERO      =  "0";
	static final String ONE       =  "1";
	static final String TWO       =  "2";
	static final String THREE     =  "3";
	static final String FOUR      =  "4";
	static final String FIVE      =  "5";
	static final String SIX       =  "6";
	static final String SEVEN     =  "7";
	static final String EIGHT     =  "8";
	static final String NINE      =  "9";
	static final String TEN       = "10";
	static final String ELEVEN    = "11";
	static final String TWELVE    = "12";
	static final String THIRTEEN  = "13";
	static final String FOURTEEN  = "14";
	static final String FIFTEEN   = "15";
	static final String SIXTEEN   = "16";
	static final String SEVENTEEN = "17";
	static final String EIGHTEEN  = "18";
	
	// keyword variable
	static final String CPR  = "CPR";
	static final String ATTA = "attach";
	//version1
/*	static final String VF   = "fibrillation";
	static final String VT   = "tachycardia";*/
	//version2
	static final String VF   = "VFib";
	static final String VT   = "VTach";
	static final String CLEA = "clear";
	static final String RESU = "resuscitation";
	static final String CAPN = "capnography";
	static final String PEA  = "P.E.A";
	//version1
	//static final String PULS = "pulse";
	//version2
	static final String PRES = "pressure";
	static final String COMP = "compression";
    static final String OXYG = "oxygen";
    static final String DEFI = "defibrillator";
    static final String ASYS = "asystole";
    static final String SHOC = "shock";
    static final String IV   = "intravenous";
    static final String IO   = "intraosseous";
    static final String EPI  = "epinephrine";
    static final String AMIO = "amiodarone";
    static final String RHYT = "rhythm";
    
    // stop words are the most common words, and does not contain any information
    static String[] stopWords = {"doctor", "patient"};
    
    // groundTruth states
    static String[] trueStates = {ZERO, ONE, TWO, THREE, THREE, FOUR, SEVENTEEN, ELEVEN, ELEVEN, 
    		ELEVEN, TWELVE, TWELVE, THIRTEEN, EIGHTEEN, FOURTEEN, FOURTEEN, FOURTEEN, FIFTEEN, 
    		FIFTEEN, TEN, SIXTEEN};
    
/*    // groundTruth word sequence version 1
    static String[] trueWords = {CPR, RHYT, ASYS, RESU, EPI, RHYT, VF, DEFI, CLEA, SHOC,
    		RESU, EPI, RHYT, VF, DEFI, CLEA, SHOC, RESU, AMIO, RHYT, COMP};*/
    
    // groundTruth word sequence version 2
    static String[] trueWords = {RESU, RHYT, ASYS, CPR, EPI, RHYT, VF, DEFI, CLEA, SHOC,
    		CPR, EPI, RHYT, VF, DEFI, CLEA, SHOC, CPR, AMIO, RHYT, COMP};
    
    // vocalPhonemes represent the phonemes of the vocabularySet
	static String[] vocalPhonemes;
	// vocalMapping is the mapping relationship between keyword and its phoneme
	static Hashtable<String, String> vocalMapping;
    
    static final int port = 9998;
 
    public static void main(String[] args) throws IOException, InterruptedException 
    {
    	//version1
/*        String[] vocabularySet = new String[] {CPR, ATTA, VF, VT, 
        		CLEA, RESU, CAPN, PEA, PULS, COMP, OXYG, DEFI, ASYS, SHOC, IV, IO, EPI, AMIO, RHYT};*/
    	
        String[] vocabularySet = new String[] {CPR, ATTA, VF, VT, 
        		CLEA, RESU, CAPN, PEA, PRES, COMP, OXYG, DEFI, ASYS, SHOC, IV, IO, EPI, AMIO, RHYT};
        
/*		// simple workflow	    	
  		String[] states = new String[] {ZERO, ONE, TWO, THREE, FOUR};   
        vocalMapping = new Hashtable<String, String>(); 
        Hashtable<String, Double> start_probability = new Hashtable<String, Double>();
    
        start_probability.put(ZERO, 0.2d);
        start_probability.put(ONE, 0.2d);
        start_probability.put(TWO, 0.2d);
        start_probability.put(THREE, 0.2d);
        start_probability.put(FOUR, 0.2d);

        // transition_probability
        Hashtable<String, Hashtable<String, Double>> transition_probability = 
        		new Hashtable<String, Hashtable<String, Double>>();
        Hashtable<String, Double> t0 = new Hashtable<String, Double>();
        t0.put(ZERO, (1.0/3.0d));
        t0.put(ONE, (1.0/3.0d));
        t0.put(TWO, (1.0/3.0d));
        Hashtable<String, Double> t1 = new Hashtable<String, Double>();
        t1.put(ONE, 0.5d);
        t1.put(TWO, 0.5d);
        Hashtable<String, Double> t2 = new Hashtable<String, Double>();
        t2.put(TWO, (1.0/3.0d));
        t2.put(THREE, (1.0/3.0d));
        t2.put(FOUR, (1.0/3.0d));
        Hashtable<String, Double> t3 = new Hashtable<String, Double>();
        t3.put(THREE, (1.0d));
        transition_probability.put(ZERO, t0);
        transition_probability.put(ONE, t1);
        transition_probability.put(TWO, t2);
        transition_probability.put(THREE, t3);
 
        // emission_probability
        Hashtable<String, Hashtable<String, Double>> emission_probability = 
        		new Hashtable<String, Hashtable<String, Double>>();
        Hashtable<String, Double> e0 = new Hashtable<String, Double>();
        e0.put(CPR,  (1.0/3.0d));
        e0.put(OXYG, (1.0/3.0d));            
        e0.put(MONI, (1.0/3.0d));
        Hashtable<String, Double> e1 = new Hashtable<String, Double>();
        e1.put(RHYT, (1.0/2.0d));            
        e1.put(PULS, (1.0/2.0d)); 
        Hashtable<String, Double> e2 = new Hashtable<String, Double>();
        e2.put(ASYS, (1.0/2.0d));            
        e2.put(PEA,  (1.0/2.0d)); 
        Hashtable<String, Double> e3 = new Hashtable<String, Double>();
        e3.put(RESU,  (1.0/3.0d));            
        e3.put(EPI,  (1.0/3.0d)); 
        e3.put(CAPN, (1.0/3.0d));
        Hashtable<String, Double> e4 = new Hashtable<String, Double>();
        e4.put(RHYT, (1.0/2.0d));            
        e4.put(PULS, (1.0/2.0d)); 
        emission_probability.put(ZERO, e0);
        emission_probability.put(ONE, e1);
        emission_probability.put(TWO, e2);
        emission_probability.put(THREE, e3);
        emission_probability.put(FOUR, e4);*/

    	String[] states = new String[] {ZERO, ONE, TWO, THREE, FOUR, FIVE, SIX, SEVEN, EIGHT, NINE, TEN,
    			ELEVEN, TWELVE, THIRTEEN, FOURTEEN, FIFTEEN, SIXTEEN, SEVENTEEN, EIGHTEEN};   
        vocalMapping = new Hashtable<String, String>(); 
        Hashtable<String, Double> start_probability = new Hashtable<String, Double>();
    
        // start_probability
/*        start_probability.put(ZERO, 1/19.0d);
        start_probability.put(ONE, 1/19.0d);
        start_probability.put(TWO, 1/19.0d);
        start_probability.put(THREE, 1/19.0d);
        start_probability.put(FOUR, 1/19.0d);
        start_probability.put(FIVE, 1/19.0d);
        start_probability.put(SIX, 1/19.0d);
        start_probability.put(SEVEN, 1/19.0d);
        start_probability.put(EIGHT, 1/19.0d);
        start_probability.put(NINE, 1/19.0d);
        start_probability.put(TEN, 1/19.0d);
        start_probability.put(ELEVEN, 1/19.0d);
        start_probability.put(TWELVE, 1/19.0d);
        start_probability.put(THIRTEEN, 1/19.0d);
        start_probability.put(FOURTEEN, 1/19.0d);
        start_probability.put(FIFTEEN, 1/19.0d);
        start_probability.put(SIXTEEN, 1/19.0d);
        start_probability.put(SEVENTEEN, 1/19.0d);
        start_probability.put(EIGHTEEN, 1/19.0d);*/
        
        // start_probability
        start_probability.put(ZERO, 1.0d);
        start_probability.put(ONE, 0d);
        start_probability.put(TWO, 0d);
        start_probability.put(THREE, 0d);
        start_probability.put(FOUR, 0d);
        start_probability.put(FIVE, 0d);
        start_probability.put(SIX, 0d);
        start_probability.put(SEVEN, 0d);
        start_probability.put(EIGHT, 0d);
        start_probability.put(NINE, 0d);
        start_probability.put(TEN, 0d);
        start_probability.put(ELEVEN, 0d);
        start_probability.put(TWELVE, 0d);
        start_probability.put(THIRTEEN, 0d);
        start_probability.put(FOURTEEN, 0d);
        start_probability.put(FIFTEEN, 0d);
        start_probability.put(SIXTEEN, 0d);
        start_probability.put(SEVENTEEN, 0d);
        start_probability.put(EIGHTEEN, 0d);

        // transition_probability
        Hashtable<String, Hashtable<String, Double>> transition_probability = 
        		new Hashtable<String, Hashtable<String, Double>>();
        
        Hashtable<String, Double> t0 = new Hashtable<String, Double>();
        t0.put(ONE, (1.0d));
        
        Hashtable<String, Double> t1 = new Hashtable<String, Double>();
        t1.put(TWO, 1/2.0d);
        t1.put(SEVEN, 1/2.0d);
        
        Hashtable<String, Double> t2 = new Hashtable<String, Double>();
        t2.put(THREE, (1.0d));
        
        Hashtable<String, Double> t3 = new Hashtable<String, Double>();
        t3.put(THREE, (1/2.0d));
        t3.put(FOUR, (1/2.0d));
        
        Hashtable<String, Double> t4 = new Hashtable<String, Double>();
        t4.put(FIVE, (1/2.0d));
        t4.put(SEVENTEEN, (1/2.0d));
        
        Hashtable<String, Double> t5 = new Hashtable<String, Double>();
        t5.put(SIX, (1.0d));
        
        Hashtable<String, Double> t6 = new Hashtable<String, Double>();
        t6.put(ELEVEN, (1/3.0d));
        t6.put(FOURTEEN, (1/3.0d));
        t6.put(SIXTEEN, (1/3.0d));
        
        Hashtable<String, Double> t7 = new Hashtable<String, Double>();
        t7.put(EIGHT, (1.0d));

        Hashtable<String, Double> t8 = new Hashtable<String, Double>();
        t8.put(EIGHT, (1/2.0d));
        t8.put(NINE, (1/2.0d));
        
        Hashtable<String, Double> t9 = new Hashtable<String, Double>();
        t9.put(NINE, (1/2.0d));
        t9.put(TEN, (1/2.0d));
        
        Hashtable<String, Double> t10 = new Hashtable<String, Double>();
        t10.put(ELEVEN, (1/2.0d));
        t10.put(SIXTEEN, (1/2.0d));
        
        Hashtable<String, Double> t11 = new Hashtable<String, Double>();
        t11.put(ELEVEN, (1/2.0d));
        t11.put(TWELVE, (1/2.0d));
        
        Hashtable<String, Double> t12 = new Hashtable<String, Double>();
        t12.put(TWELVE, (1/2.0d));
        t12.put(THIRTEEN, (1/2.0d));
        
        Hashtable<String, Double> t13 = new Hashtable<String, Double>();
        t13.put(SIXTEEN, (1/2.0d));
        t13.put(EIGHTEEN, (1/2.0d));
        
        Hashtable<String, Double> t14 = new Hashtable<String, Double>();
        t14.put(FOURTEEN, (1/2.0d));
        t14.put(FIFTEEN, (1/2.0d));
        
        Hashtable<String, Double> t15 = new Hashtable<String, Double>();
        t15.put(TEN, (1/2.0d));
        t15.put(FIFTEEN, (1/2.0d));
        
        Hashtable<String, Double> t17 = new Hashtable<String, Double>();
        t17.put(ELEVEN, (1/2.0d));
        t17.put(FOURTEEN, (1/2.0d));
        
        Hashtable<String, Double> t18 = new Hashtable<String, Double>();
        t18.put(FOURTEEN, (1.0d));
        
        
        transition_probability.put(ZERO, t0);
        transition_probability.put(ONE, t1);
        transition_probability.put(TWO, t2);
        transition_probability.put(THREE, t3);
        transition_probability.put(FOUR, t4);
        transition_probability.put(FIVE, t5);
        transition_probability.put(SIX, t6);
        transition_probability.put(SEVEN, t7);
        transition_probability.put(EIGHT, t8);
        transition_probability.put(NINE, t9);
        transition_probability.put(TEN, t10);
        transition_probability.put(ELEVEN, t11);
        transition_probability.put(TWELVE, t12);
        transition_probability.put(THIRTEEN, t13);
        transition_probability.put(FOURTEEN, t14);
        transition_probability.put(FIFTEEN, t15);
        transition_probability.put(SEVENTEEN, t17);
        transition_probability.put(EIGHTEEN, t18);
 
        // emission_probability
        Hashtable<String, Hashtable<String, Double>> emission_probability = 
        		new Hashtable<String, Hashtable<String, Double>>();
        Hashtable<String, Double> e0 = new Hashtable<String, Double>();
        //version1
        //e0.put(CPR,  (1.0/3.0d));
        //version2
        e0.put(RESU,  (1.0/3.0d));
        e0.put(OXYG, (1.0/3.0d));            
        e0.put(ATTA, (1.0/3.0d));
        
        Hashtable<String, Double> e7 = new Hashtable<String, Double>();
        e7.put(VF, (1.0/2.0d));            
        e7.put(VT, (1.0/2.0d)); 
        Hashtable<String, Double> e17 = new Hashtable<String, Double>();
        e17.put(VF, (1.0/2.0d));            
        e17.put(VT, (1.0/2.0d)); 
        Hashtable<String, Double> e18 = new Hashtable<String, Double>();
        e18.put(VF, (1.0/2.0d));            
        e18.put(VT, (1.0/2.0d)); 
        
        Hashtable<String, Double> e8 = new Hashtable<String, Double>();
        e8.put(DEFI,  (1.0/3.0d));            
        e8.put(CLEA,  (1.0/3.0d)); 
        e8.put(SHOC, (1.0/3.0d));
        Hashtable<String, Double> e11 = new Hashtable<String, Double>();
        e11.put(DEFI,  (1.0/3.0d));            
        e11.put(CLEA,  (1.0/3.0d)); 
        e11.put(SHOC, (1.0/3.0d));
        Hashtable<String, Double> e14 = new Hashtable<String, Double>();
        e14.put(DEFI,  (1.0/3.0d));            
        e14.put(CLEA,  (1.0/3.0d)); 
        e14.put(SHOC, (1.0/3.0d));
        
        Hashtable<String, Double> e5 = new Hashtable<String, Double>();
        //version1
        //e5.put(RESU, (1.0d));
        //version2
        e5.put(CPR, (1.0d)); 
        
        Hashtable<String, Double> e9 = new Hashtable<String, Double>();
        //version1
        //e9.put(RESU,  (1.0/3.0d));   
        //version2
        e9.put(CPR, (1.0/3.0d)); 
        e9.put(IV,  (1.0/3.0d)); 
        e9.put(IO, (1.0/3.0d));
        
        Hashtable<String, Double> e3 = new Hashtable<String, Double>();
        //version1
        //e3.put(RESU,  (1.0/3.0d));
        //version2
        e3.put(CPR, (1.0/3.0d)); 
        e3.put(EPI,  (1.0/3.0d)); 
        e3.put(CAPN, (1.0/3.0d));
        Hashtable<String, Double> e12 = new Hashtable<String, Double>();
        //version1
        //e12.put(RESU,  (1.0/3.0d));
        //version2
        e12.put(CPR,  (1.0/3.0d));  
        e12.put(EPI,  (1.0/3.0d)); 
        e12.put(CAPN, (1.0/3.0d));
        
        Hashtable<String, Double> e15 = new Hashtable<String, Double>();
        //version1
        //e15.put(RESU, (1.0/2.0d));
        //version2
        e15.put(CPR, (1.0/2.0d));   
        e15.put(AMIO,  (1.0/2.0d)); 
        
        Hashtable<String, Double> e2 = new Hashtable<String, Double>();
        e2.put(ASYS, (1.0/2.0d));            
        e2.put(PEA,  (1.0/2.0d)); 
        
        //version1
/*        Hashtable<String, Double> e1 = new Hashtable<String, Double>();
        e1.put(RHYT, (1.0/2.0d));            
        e1.put(PULS, (1.0/2.0d)); 
        Hashtable<String, Double> e4 = new Hashtable<String, Double>();
        e4.put(RHYT, (1.0/2.0d));            
        e4.put(PULS, (1.0/2.0d)); 
        Hashtable<String, Double> e6 = new Hashtable<String, Double>();
        e6.put(RHYT, (1.0/2.0d));            
        e6.put(PULS, (1.0/2.0d)); 
        Hashtable<String, Double> e10 = new Hashtable<String, Double>();
        e10.put(RHYT, (1.0/2.0d));            
        e10.put(PULS, (1.0/2.0d)); 
        Hashtable<String, Double> e13 = new Hashtable<String, Double>();
        e13.put(RHYT, (1.0/2.0d));            
        e13.put(PULS, (1.0/2.0d)); */
        
        //version2
        Hashtable<String, Double> e1 = new Hashtable<String, Double>();
        e1.put(RHYT, (1.0/2.0d));            
        e1.put(PRES, (1.0/2.0d)); 
        Hashtable<String, Double> e4 = new Hashtable<String, Double>();
        e4.put(RHYT, (1.0/2.0d));            
        e4.put(PRES, (1.0/2.0d)); 
        Hashtable<String, Double> e6 = new Hashtable<String, Double>();
        e6.put(RHYT, (1.0/2.0d));            
        e6.put(PRES, (1.0/2.0d)); 
        Hashtable<String, Double> e10 = new Hashtable<String, Double>();
        e10.put(RHYT, (1.0/2.0d));            
        e10.put(PRES, (1.0/2.0d)); 
        Hashtable<String, Double> e13 = new Hashtable<String, Double>();
        e13.put(RHYT, (1.0/2.0d));            
        e13.put(PRES, (1.0/2.0d)); 
        
        Hashtable<String, Double> e16 = new Hashtable<String, Double>();
        e16.put(COMP, (1.0d)); 

        emission_probability.put(ZERO, e0);
        emission_probability.put(ONE, e1);
        emission_probability.put(TWO, e2);
        emission_probability.put(THREE, e3);
        emission_probability.put(FOUR, e4);
        emission_probability.put(FIVE, e5);
        emission_probability.put(SIX, e6);
        emission_probability.put(SEVEN, e7);
        emission_probability.put(EIGHT, e8);
        emission_probability.put(NINE, e9);
        emission_probability.put(TEN, e10);
        emission_probability.put(ELEVEN, e11);
        emission_probability.put(TWELVE, e12);
        emission_probability.put(THIRTEEN, e13);
        emission_probability.put(FOURTEEN, e14);
        emission_probability.put(FIFTEEN, e15);
        emission_probability.put(SIXTEEN, e16);
        emission_probability.put(SEVENTEEN, e17);
        emission_probability.put(EIGHTEEN, e18);
        
        System.out.println("emission_probability is: " + emission_probability);
        System.out.println("transition_probability is: " + transition_probability);
        
    	vocalPhonemes = phonemeConversion(vocabularySet, true);    

	    	
    	//Notice that receivePacket.getData() is 256(previous value) and receivePacket.getLength() is the actual length
    	
    	
/*    	// 20min verison
 * 		String revStr = "start CPR$what's the rhythm$the patient has " +
    			"a Cistulli$start reset asian 42 minutes$give epinephrine for 3 minutes interval$";*/
    	
    	// path1version1
/*    	String revStr = "Dr$ what's the rhythm$ the patient has a sister$ start 37 Asian for 2 minutes$ epinephrine for " +
    			"a 3 minute interval$ what's the riddle$ can a ship has the relation$ charge the difference in later$ clear " +
    			"the bed$ shock patient$ dodge reset a shin for 2 minutes$ epinephrine .3 minute interval$ what the rhythm$ " +
    			"the patient has a ventricular fibrillation$ charged in Atlanta$ clear the bed$ shop the patient$ destination " +
    			"for 2 minutes$ Amy order and Country minute interval$ what's the rhythm$ pulse pressure$";*/
    	
    	// path1version2
/*    	String revStr = "Star Trek$ what's the rhythm$ the patient has a sister$ start CPR for 2 minutes$ give epinephrine 43 " +
    			"minutes interval$ what's the rhythm$ the patient has to be fed$ Chuck the defibrillator$ clear the bed$ shock " +
    			"a patient$ start CPR for 2 minutes$ give up enough room for few minutes interval$ what's the rhythm$ the patient " +
    			"has the Fed$ charge the defibrillator$ clear the bed$ shock the patient$ start CPR for 2 minutes$ give me older in" +
    			" 43 minutes interval$ what's the rhythm$ good pulse with compression$ ";*/
    	
    	String revStr = "Start music Haitian$ what's the rhythm$ the patient has a sister$ start CPR for 2 minutes$ give epinephrine 40 minutes interval$ what's the rhythm$ the patient has to be fed$ charge defibrillator$ clear the bed$ shock a patient$ start CPR for 2 minutes$ give up in Afrin for 3 minutes interval$ what's the rhythm$ the patient has the Fed$ charge the defibrillator$ clear the bed$ shock a patient$ start CPR for 2 minutes$ give me older in 43 minutes interval$ what's the rhythm$ good pulse with compression$";
    	
    	//String revStr = "Start me sassy tation$ what's the rhythm$ the patient has a system$ start CPR for 2 minutes$ give epinephrine 43 minutes interval$ what's the rhythm$ the patient has to be fit$ charge the defibrillator$ clear the bed$ shock patient$ start CPR for 2 minutes$ give epinephrine for 3 minutes interval$ what's the rhythm$ the patient has the Fed$ charge the defibrillator$ clear the bed$ shock a patient$ start CPR for 2 minutes$ give me a reason for 3 minutes interval$ what's the rhythm$ good pulse with compression$";

    	// get rid of the newline char
        System.out.println("RECEIVED: " + revStr.replace("\n", " "));
        // get rid of stop words
/*        String revStr2 = revStr.replaceAll("patient", "");
        System.out.println("After getting rid of the stop word, RECEIVED: " + revStr2);*/
        
        // manipulate the received string 
        String[] sentenceSeq = revStr.split("\\$ "); 
        System.out.println("split.size: " + sentenceSeq.length);
/*        System.out.println("The received seq after splitting is:");
        for (String str : sentenceSeq) {
            System.out.println(str);
        }*/
        //System.out.println(Arrays.toString(sentenceSeq));
        System.out.println("revStr's accuracy is: " + measureWords(sentenceSeq));
        
        // manipulate the wordSeq to find the nearest match
        String[] wordSeq =  manipulateSentence(sentenceSeq, vocabularySet);
        //System.out.println(Arrays.toString(wordSeq));
        System.out.println("The received seq after manipulation is:");
        for (String str : wordSeq) {
        	System.out.println(str);
        }
        
        System.out.println("After the manipulation, revStr's accuracy is: " + measureWords(wordSeq));
        
        Hashtable<String, Hashtable<String, Double>> confusion_probability =
        		confustionGen(wordSeq, vocabularySet);
        
        forward_viterbi(wordSeq,
        		vocabularySet, states,
                start_probability,
                transition_probability,
                emission_probability,
                confusion_probability);
        
    }
 
        public static void forward_viterbi(String[] actualObs, String[] obs, String[] states,
                        Hashtable<String, Double> start_p,
                        Hashtable<String, Hashtable<String, Double>> trans_p,
                        Hashtable<String, Hashtable<String, Double>> emit_p,
			Hashtable<String, Hashtable<String, Double>> conf_p)
        {

        	int state_num = states.length;
        	//int obs_num = obs.length;
        	int obs_num = actualObs.length;
        	//V[t][i] stores the overall largest probability ending at the state of i at time t
        	double V[][] = new double[obs_num+1][state_num];
        	//B[t][i]  stores the last source state corresponding to the V[t][i]
        	int B[][] = new int[obs_num+1][state_num];
        	//X[t][i] stores the word that has been chosen corresponding to the V[t][i]
        	String X[][] = new String[obs_num+1][state_num];
		
        	for (String state : states)
        	{
        		//V[0][m] = start_p.get(state);
                V[0][Integer.parseInt(state)] = Math.log(start_p.get(state));
        		B[0][Integer.parseInt(state)] = Integer.parseInt(state);
        		X[0][Integer.parseInt(state)] = "@";
        	}

        	// t is the records the current time
        	int t = 0;
            for (String input : actualObs)
            {
            	// input is the current actual observation
            	t++;
            	// i is the current state
            	int i = -1;
                for (String next_state : states)
                {
                	i++;
                    int Smax = -1;
                    //double Pmax = 0;
                    double Pmax = Double.NEGATIVE_INFINITY;
                    String v_word = "";	
                    double v_prob = 1;       
			
                    // x is the current accurate observation (word)	
                    int x = -1;
                    for (String word : obs)		
                    {
                    	x++;
                        if (t == 1) {
                            if (emit_p.get(next_state) == null || emit_p.get(next_state).get(word) == null) {
                                v_prob = Double.NEGATIVE_INFINITY;
                            } else {
                                v_prob = Math.log(start_p.get(next_state)) + Math.log(conf_p.get(input).get(word))
                                    + Math.log(emit_p.get(next_state).get(word));
                            }
                            if (v_prob > Pmax) {
                                Pmax = v_prob;
                                //Smax = i;
                                Smax = Integer.parseInt(next_state);
                                v_word = word;
                            }
                        } else {
                        	// j is the previous state
                        	int j = -1; 
                        	for (String source_state : states)
                        	{
                        		j++;
                        		double p;
                        		if (emit_p.get(next_state) == null || trans_p.get(source_state) == null ||
                        				emit_p.get(next_state).get(word) == null || trans_p.get(source_state).get(next_state) == null)
                        			//p = 0;
                                    p = Double.NEGATIVE_INFINITY;
                        		else
    	                    		//p = emit_p.get(next_state).get(word) * 
    	                    		//		trans_p.get(source_state).get(next_state) * conf_p.get(input).get(word);
     	                    		p = Math.log(emit_p.get(next_state).get(word)) +  
    	                    				Math.log(trans_p.get(source_state).get(next_state)) + Math.log(conf_p.get(input).get(word));
 
                        		//v_prob = V[t-1][j] * p;
                                v_prob= V[t-1][Integer.parseInt(source_state)] + p;
    						
                        		if (v_prob > Pmax)
                        		{
                        			Pmax = v_prob;
                        			//Smax = j;
                        			Smax = Integer.parseInt(source_state);
                        			v_word = word;
                        		}
                        	}
                        }
                    }
/*                  V[t][i] = Pmax;
                    B[t][i] = Smax;
                    X[t][i] = v_word;*/
                    V[t][Integer.parseInt(next_state)] = Pmax;
                    B[t][Integer.parseInt(next_state)] = Smax;
                    X[t][Integer.parseInt(next_state)] = v_word;
                }
            }
 
            /*
            for (int n = 0; n < obs_num+1; n++)
            	for (int z = 0; z < state_num; z++)
            		System.out.println(V[n][z]);
            for (int n = 0; n < obs_num+1; n++)
            	for (int z = 0; z < state_num; z++)
            		System.out.println(B[n][z]);
            for (int n = 0; n < obs_num+1; n++) 
            	for (int z = 0; z < state_num; z++)
            		System.out.println(X[n][z]);
			*/
		
            int Smax = -1;
            //double pMax = 0;
            double pMax = Double.NEGATIVE_INFINITY;
            for (int n = 0; n < state_num; n++ ) 
            {
            	if (V[obs_num][n] >= pMax) {
            		pMax = V[obs_num][n];
            		Smax = n;
            	}
            }

            // If at the current stage, the output of ASR is too far-away from the vocabulary set,
            // it needs special handling.
            int path[] = new int[obs_num + 1];
            String words[] = new String[obs_num + 1];
            
            path[obs_num]  = Smax;
            words[obs_num] = X[obs_num][Smax];
            for (int x = obs_num-1; x >= 0; x--)
            {
            	path[x] = B[x+1][path[x+1]];
            	words[x] = X[x][path[x]]; 
            }

            String[] subWords = Arrays.copyOfRange(words, 1, obs_num+1);
            int[] subStates = Arrays.copyOfRange(path, 1, obs_num+1);
            //System.out.println("subWords is: " + Arrays.toString(subWords));
            //System.out.println("subStates is: " + Arrays.toString(subStates));
            	
            System.out.println("\n*************************************\n");         
            System.out.println(Arrays.toString(actualObs));
            
            for (int x = 0; x < obs_num+1; x++)
            {
            	System.out.println("state: " + path[x] + 
            			", with the word: " + words[x]);
            }
            
            System.out.println("My words measurement is: " + measureWords(subWords));
            System.out.println("My state measurement is: " + measureStates(subStates));
            
            System.out.println("\n*************************************\n");
        }

		// convolution_index calculates the overlapping similarity of two strings
		// Notice that p_src is the string in the vocabulary set, and p_dest is the
		// the actual string that is heard.
		public static double convolution_index(String p_src, String p_dest)
    	{
		    int len_src = p_src.length();
		    int len_dest = p_dest.length();
		    int steps = len_src + len_dest - 2;
		    double retval = 0;
	   
		    for(int i = 0; i < steps; i++) {
		        int cur_conv_index = 0;
		        int start_s = Math.max(0, (len_src - 1 - i));
		        int start_d = Math.max(0, i - len_src + 1);
		        while(start_s < len_src && start_d < len_dest) {
		            //System.out.println(p_src.substring(start_s,start_s+1) + " == " + p_dest.substring(start_d,start_d+1));
		            if(p_src.substring(start_s,start_s+1).equals(p_dest.substring(start_d,start_d+1))) {
		                ++cur_conv_index;
		            }
		            ++start_s;
		            ++start_d;
		        }
		        if(cur_conv_index > retval)
		            retval = cur_conv_index;
		    }
			//System.out.println("The convolution index value between " + p_src + " and " + p_dest + " is " + retval);
		    return retval/len_dest;       
		}


        public static Hashtable<String, Hashtable<String, Double>> 
        	confustionGen(String[] obs, String[] vocabularySet) throws IOException, InterruptedException
        {
        	/*
        	 * convert the word sequence into phonetic sequence by calling eSpeak.
        	 */        	
        	//.out.println(Arrays.toString(obs));
        	//System.out.println(Arrays.toString(vocabularySet));
        	String[] obsPhonemes;
        	
        	obsPhonemes = phonemeConversion(obs, false);       	       	
        	
/*        	for (String temp : obsPhonemes)
        		System.out.println(temp);
        	
        	for (String temp : vocalPhonemes)
        		System.out.println(temp);
        	*/
        	
        	int i = 0;
        	int j;
    		Hashtable<String, Hashtable<String, Double>> confusion_probability = 
    				new Hashtable<String, Hashtable<String, Double>>();
        	for (String obsWord : obs) {
        		Hashtable<String, Double> c = new Hashtable<String, Double>();
        		// get rid of the utility 'phonemes'
				// Notice here that getting rid of space is quite useful, wheraz other utility 
				// phonemes such as # is not so clear.
    			String obsPhoneme = obsPhonemes[i].replaceAll("[',%=_:| ]", "");
        		j = 0;
        		for (String volWord : vocabularySet) {
        			double similarityIndex;
        			// get rid of the utility 'phonemes'
        			String vocalPhoneme = vocalPhonemes[j].replaceAll("[',%=_:| ]", "");
        			int LDistance = computeLevenshteinDistance(obsPhoneme, vocalPhoneme);
        			//int wordLength = obsWord.length();
        			//int wordLength = obsPhonemes[i].length();
        			int wordLength = obsPhoneme.length();
        			//System.out.println("vocal " + vocalPhonemes[j] + 
        			//		" LDistance " + LDistance + " wordLength " + wordLength);
        			//similarityIndex = ( LDistance <= wordLength ? (1 - ((double)LDistance/wordLength)) : 0);
                    similarityIndex = 1 - ((double)LDistance/(Math.max(obsPhoneme.length(), vocalPhoneme.length())));
					// --------------------------------------------------------
					// Modified for adding convolution index
					double conv = convolution_index(vocalPhoneme, obsPhoneme);
					similarityIndex *= conv;
					// --------------------------------------------------------
        			c.put(volWord, similarityIndex);
        			j++;
        		}
        		confusion_probability.put(obsWord, c);
        		i++;
        	}
        	
        	//System.out.println("Hi, I am here");
        	
        	Enumeration<String> names;	
            names = confusion_probability.keys();
            while(names.hasMoreElements()) {
               String str = (String) names.nextElement();
               System.out.println(str + ": " +
            		   confusion_probability.get(str));
            }
        	
			return confusion_probability;
        }
        
        public static String phonemeConversion(String str) throws IOException, InterruptedException
        {
        	Process p;
        	String speakCall = "speak -x ";
      
        	String[] subWords = str.split(" ");
        	StringBuilder builder = new StringBuilder();
        	int j = 0;
        	for (String subWord : subWords) {
        		j++;
        		String command = speakCall + "\"" + subWord + "\"";
        		p = Runtime.getRuntime().exec(command);
            	p.waitFor();
            	BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
            	String line;
            	if ((line = reader.readLine()) != null) {
            		//System.out.println(line);
            		if (j == 1)
            			builder.append(line);
            		else
            			builder.append(" ").append(line);
            	}
        	}
        	
			return builder.toString();	
        }
        
    	public static String[] phonemeConversion(String[] str, boolean isKeyword) throws IOException, InterruptedException
    	{
        	Process p;
        	String speakCall = "speak -x ";
        	String line;
        	int i = 0;
        	
        	// phonemeArr is the returned string array containing the phoneme representations of the str
        	String[] phonemeArr = new String[str.length];
    		
        	// word is each word in the string[] 
        	for (String word : str)
        	{
        		// Probably because of the bug in speak package, it cannot return desired results.
        		// Since word may consist of several other words, e.g., obs[] may contain several words,
        		// we have to split it into array of words.
        		String[] subWords = word.split(" ");
        		StringBuilder out = new StringBuilder();
        		int j = 0;
        		for (String subWord : subWords)
        		{
        			j++;
	        		String command = speakCall + subWord;
	        		p = Runtime.getRuntime().exec(command);
	            	p.waitFor();
	            	
	            	BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
	            	
	            	// return the subString(1) because of the fact that espeak adds a space at the beginning
	            	//line = reader.readLine().substring(1);
	            	
	            	if ((line = reader.readLine()) != null)
	            	{
	            		//System.out.println(line);
	            		if (j == 1)
	            			out.append(line.substring(1));
	            		else
	            			out.append(" ").append(line.substring(1));
	            	}
        		}
            	/*
            	StringBuilder out = new StringBuilder();
                String curr = null, previous = null;
                while ((curr = reader.readLine()) != null)
                {
                    if (!curr.equals(previous)) {
                        previous = curr;
                        out.append(curr).append('\n');
                        //System.out.println(curr);
                        phonemeArr[i] = out;
                    }
                }*/      
        		
        		// updated phonemeArr[]
            	phonemeArr[i] = out.toString();
            	
            	// update the vocalMapping
            	if (isKeyword) {
            		vocalMapping.put(phonemeArr[i], word);
            	}
            	i++;
        	}
        	
			return phonemeArr;	
    	}
    	
        private static int minimum(int a, int b, int c) {
            return Math.min(Math.min(a, b), c);
        }

        public static int computeLevenshteinDistance(String str1,String str2) {
            int[][] distance = new int[str1.length() + 1][str2.length() + 1];

            for (int i = 0; i <= str1.length(); i++)
            	distance[i][0] = i;
            for (int j = 1; j <= str2.length(); j++)
                distance[0][j] = j;

            for (int i = 1; i <= str1.length(); i++)
                for (int j = 1; j <= str2.length(); j++)
                    distance[i][j] = minimum(
                    		distance[i - 1][j] + 1,
                            distance[i][j - 1] + 1,
                            // TODO change the weight of substuting one character of the word to 2
                            //distance[i - 1][j - 1]+ ((str1.charAt(i - 1) == str2.charAt(j - 1)) ? 0 : 2));
                            distance[i - 1][j - 1]+ ((str1.charAt(i - 1) == str2.charAt(j - 1)) ? 0 : 1));

            return distance[str1.length()][str2.length()];    
        }
        
        //
        public static String[] manipulateSentence(String[] sentenceSeq, String[] vocabularySet) throws IOException, InterruptedException 
        {
        	int exactCnt = 0;
        	for (int i = 0; i < sentenceSeq.length; i++) {
        		String match = null;
        		if ((match = findExactMatch(sentenceSeq[i], vocabularySet)) != null) {
        			sentenceSeq[i] = match;
        			exactCnt++;
        		} else {
        			match = findFuzzyMatch(sentenceSeq[i]);
        			sentenceSeq[i] = match;
        		}
        	}
        	System.out.println("exactCnt is: " + exactCnt);
        	return sentenceSeq;
        }
        
        // findExactMatch sees whether any of the keywords match exactly with substring of 
        // the received sentence.  It is based on an assumption that the one sentense will 
        // not have two keywords showing up
        public static String findExactMatch(String sentence, String[] vocabularySet) {
        	for (String keyword : vocabularySet) {
        		if (sentence.toLowerCase().contains(keyword.toLowerCase())) {
        			return keyword;
        		}
        	}      	
        	return null;
        }
        
        public static String findFuzzyMatch(String sentence) throws IOException, InterruptedException  {
        	// convert the sentence into its phoneme representations
        	String sentencePhoneme = phonemeConversion(sentence);  
        	if (sentencePhoneme != null) {
        		// get rid of the special characters
        		sentencePhoneme = sentencePhoneme.replaceAll("[',%=_:| ]", "");
        	}
        	//System.out.println("sentencePhoneme after replacenment is: " + sentencePhoneme);
        	
    		// Notice that p_src is the string in the vocabulary set, and p_dest is the
    		// the actual sentence that is heard.
		    int len_src;
		    int len_dest = sentencePhoneme.length();
		    int steps;
		    
		    double maxval = 0;
		    String retStr = null;
		    
		    /*
		     * for testing purpose
		     */
		    //String[] testStrs = {"a#s'IstoUl", "rI2s,VsIt'eIS@n"};
		    //for (String keywordPhoneme : testStrs) {
        	for (String keywordPhoneme : vocalPhonemes) {
        		// we have modify the keywordPhoneme in order to get rid of the special characters
        		String modifiedKeyWordPhoneme = keywordPhoneme.replaceAll("[',%=_:| ]", "");
			    len_src = modifiedKeyWordPhoneme.length();
			    len_dest = sentencePhoneme.length();
			    steps = len_src + len_dest - 2;
			    // retval is the current largest overlapping value for this particular keyword
			    double retval = 0;
		   
			    for(int i = 0; i < steps; i++) {
			        int cur_conv_index = 0;
			        int start_s = Math.max(0, (len_src - 1 - i));
			        int start_d = Math.max(0, i - len_src + 1);
			        while(start_s < len_src && start_d < len_dest) {
			            //System.out.println(p_src.substring(start_s,start_s+1) + " == " + p_dest.substring(start_d,start_d+1));
			            if(modifiedKeyWordPhoneme.substring(start_s,start_s+1).equals(sentencePhoneme.substring(start_d,start_d+1))) {
			                ++cur_conv_index;
			            }
			            ++start_s;
			            ++start_d;
			        }
			        if(cur_conv_index > retval) {
			            retval = cur_conv_index;
			        }
			    }
			    
			    if (retval > maxval) {
			    	maxval = retval;
			    	retStr = vocalMapping.get(keywordPhoneme);
			    }
			//System.out.println("The convolution index value between " + p_src + " and " + p_dest + " is " + retval);
        	}     	
        	return retStr;
        }
        
        public static double measureWords(String[] wordSeq) 
        {
        	int numStates = wordSeq.length;
        	//System.out.println("wordSeq.length is " + wordSeq.length);
        	//System.out.println("trueWords.length is " + trueWords.length);
        	int count = 0;
        	if (numStates > trueWords.length) {
        		System.out.println("Error! The number of words is larger than possible max value");
        		System.exit(-1);
        	}
        	for (int i = 0; i < numStates; i++) {
        		if (wordSeq[i].contains(trueWords[i])) {
        			count++;
        		}
        	}
        	return ((double)count/numStates);
        }
        
        public static double measureStates(int[] stateSeq) 
        {
        	int numStates = stateSeq.length;
        	int count = 0;
        	if (numStates > trueStates.length) {
        		System.out.println("Error! The number of states is larger than possible max value");
        		System.exit(-1);
        	}
        	for (int i = 0; i < numStates; i++) {
        		if (stateSeq[i] == Integer.parseInt(trueStates[i])) {
        			count++;
        		}
        	}
        	//TODO
        	return ((double)count/numStates);
        }
        
/*        public static String trimStopWord(String str, int level)
        {
        	if (level == -1) {
        		return str;
        	} else {
	        	String retStr = null;
	        	retStr = str.replaceAll(stopWords[level-1], "");
	        	System.out.println(retStr);
	    		return trimStopWord(retStr, level-1);
        	}
        }*/
}


