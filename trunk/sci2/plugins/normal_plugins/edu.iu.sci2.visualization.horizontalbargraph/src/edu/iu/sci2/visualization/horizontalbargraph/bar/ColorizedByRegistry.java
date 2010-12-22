package edu.iu.sci2.visualization.horizontalbargraph.bar;

import java.awt.Color;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * ColorizedByRegistry provide algorithm to rank the items of 
 * the category to be colorized. AVAILABLE_COLOR_LIST contains
 * that supported colors. The display ordering of the colors is
 * provided by using LinkedHashMap.
 * @author kongch
 *
 */
public class ColorizedByRegistry {
	public final static Color YELLOW = new Color(1.0f, 0.8f, 0.2f);
	public final static Color LIGHT_BLUE = new Color(0.6f, 0.8f, 1.0f);
	public final static Color LIGHT_BROWN = new Color(0.6f, 0.4f, 0.4f);
	public final static Color GREEN = new Color(0.4f, 0.6f, 0.4f);
	public final static Color PURPLE = new Color(0.6f, 0.4f, 1.0f);
	public final static Color KHAKI = new Color(0.4f, 0.4f, 0);
	public final static Color BLACK = new Color(0, 0, 0);
	public static final Color[] AVAILABLE_COLOR_LIST = {
		YELLOW, LIGHT_BLUE, LIGHT_BROWN, GREEN, PURPLE, KHAKI
	};
	public static final PhraseFrequency EMPTY_PHRASE = new PhraseFrequency("");
	public final static String OTHER_CATEGORY = "Others";

	private Map<String, Color> categoriesToColors;
	
	public ColorizedByRegistry(List<String> stringList) {
		/* LinkedHashMap provide the ordering of the color */
		categoriesToColors = new LinkedHashMap<String, Color>();
		this.rankingAlgorithm(stringList);
	}
	
	static class PhraseFrequency {
		private String phrase;
		private int frequency;
		
		public PhraseFrequency(String phrase) {
			this.phrase = phrase;
			this.frequency = 0;
		}
		
		public void increaseFrequency(String phrase) {
			if(this.phrase.equals(phrase)) {
				this.frequency++;
			}
		}
		
		public int getFrequency() {
			return this.frequency;
		}
		
		public String getPhrase() {
			return this.phrase;
		}
	}
	
	// TODO: If time ever permits, rethink this design a little bit (i.e. using Google Collections.Multiset)
	private void rankingAlgorithm(List<String> stringList) {
		this.categoriesToColors.clear();
		Set<PhraseFrequency> frequencySet = generateFrequency(stringList);
		for(Color color: AVAILABLE_COLOR_LIST){
			
			PhraseFrequency topRankPhrase = getTopRankPhraseFrequency(frequencySet);
			if(topRankPhrase!=EMPTY_PHRASE){
				frequencySet.remove(topRankPhrase);
				this.categoriesToColors.put(topRankPhrase.getPhrase(), color);
			}
			
			if(frequencySet.isEmpty()){
				break;
			}
		}
		
		/* Assign other to black if there is more than 6 items */
		if(!frequencySet.isEmpty())
			this.categoriesToColors.put(OTHER_CATEGORY, BLACK);
	}
	
	private PhraseFrequency getTopRankPhraseFrequency(Set<PhraseFrequency> frequencySet){
		PhraseFrequency topRankPhrase = EMPTY_PHRASE;
		
		for(PhraseFrequency phraseFreq : frequencySet){
			if(topRankPhrase.getFrequency() < phraseFreq.getFrequency())
				topRankPhrase = phraseFreq;
		}
		
		return topRankPhrase;
	}
	
	private Set<PhraseFrequency> generateFrequency(List<String> stringList){
		Set<PhraseFrequency> set = new HashSet<PhraseFrequency>();
		Map<String, PhraseFrequency> map = new HashMap<String, PhraseFrequency>();
		
        for (String str : stringList) {
        	if(str != null) {
	        	PhraseFrequency phraseFreq = map.get(str);
	        	if(phraseFreq == null) {
	        		phraseFreq = new PhraseFrequency(str);
	        		map.put(str, phraseFreq);
	        		set.add(phraseFreq);
	        	}
	        	phraseFreq.increaseFrequency(str);
        	}
        }
        
        return set;
	}
	
	public Set<String> getKeySet(){
		return categoriesToColors.keySet();
	}
	
	public Color getColorOf(String colorizedBy){
		if(categoriesToColors.containsKey(colorizedBy)){
			return categoriesToColors.get(colorizedBy);
		}else{
			return BLACK;
		}
	}
}
