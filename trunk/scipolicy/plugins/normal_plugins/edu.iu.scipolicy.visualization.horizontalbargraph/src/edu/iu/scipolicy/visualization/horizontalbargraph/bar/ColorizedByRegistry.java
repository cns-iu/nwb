package edu.iu.scipolicy.visualization.horizontalbargraph.bar;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ColorizedByRegistry {
	public final static Color YELLOW = new Color(0xFFCC33);
	public final static Color LIGHT_BLUE = new Color(0x99CCFF);
	public final static Color LIGHT_BROWN = new Color(0x996666);
	public final static Color GREEN = new Color(0x669966);
	public final static Color PURPLE = new Color(0x9966FF);
	public final static Color KHAKI = new Color(0x666600);
	public final static Color BLACK = new Color(0x000000);
	public static final Color[] AVAILABLE_COLOR_LIST = {YELLOW, LIGHT_BLUE, LIGHT_BROWN, GREEN, PURPLE, KHAKI};
	public static final PhraseFrequency EMPTY_PHRASE = new PhraseFrequency("");

	private Map<String, Color> categoriesToColors;
	
	static class PhraseFrequency{
		private String phrase;
		private int frequency;
		
		public PhraseFrequency(String phrase){
			this.phrase = phrase;
			this.frequency = 0;
		}
		
		public void increaseFrequency(String phrase){
			if(this.phrase.equals(phrase)) {
				this.frequency++;
			}
		}
		
		public int getFrequency(){
			return this.frequency;
		}
		
		public String getPhrase(){
			return this.phrase;
		}
	}
	
	public ColorizedByRegistry(List<String> stringList){
		this.rankingAlgorithm(stringList);
	}
	
	// TODO: If time ever permits, rethink this design a little bit (i.e. using Google Collections.Multiset)
	private void rankingAlgorithm(List<String> stringList){
		this.categoriesToColors = new HashMap<String, Color>();
		Set<PhraseFrequency> frequencySet = generateFrequency(stringList);
		for(Color color: AVAILABLE_COLOR_LIST){
			
			this.categoriesToColors.put(getAndRemoveTopRank(frequencySet), color);
			if(frequencySet.isEmpty()){
				break;
			}
		}
	}
	
	private String getAndRemoveTopRank(Set<PhraseFrequency> frequencySet){
		PhraseFrequency topRankPhrase = EMPTY_PHRASE;
		
		for(PhraseFrequency phraseFreq : frequencySet){
			if(topRankPhrase.getFrequency() < phraseFreq.getFrequency())
				topRankPhrase = phraseFreq;
		}
		
		if(topRankPhrase!=EMPTY_PHRASE)
			frequencySet.remove(topRankPhrase);
		
		return topRankPhrase.getPhrase();
	}
	
	private Set<PhraseFrequency> generateFrequency(List<String> stringList){
		Set<PhraseFrequency> set = new HashSet<PhraseFrequency>();
		Map<String, PhraseFrequency> map = new HashMap<String, PhraseFrequency>();
		
        for (String str : stringList) {
        	PhraseFrequency phraseFreq = map.get(str);
        	if(phraseFreq == null){
        		phraseFreq = new PhraseFrequency(str);
        		map.put(str, phraseFreq);
        		set.add(phraseFreq);
        	}
        	phraseFreq.increaseFrequency(str);

        }
        
        return set;
	}
	
	public Color getColorOf(String colorizedBy){
		if(categoriesToColors.containsKey(colorizedBy)){
			return categoriesToColors.get(colorizedBy);
		}else{
			return BLACK;
		}
	}
}
