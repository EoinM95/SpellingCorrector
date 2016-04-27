import java.util.ArrayList;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;


public class Corrector {
	
	public static final int MAX_EDIT_DISTANCE=4;
	
	public static FrequencyModel trainModel(){
		ArrayList<String> lines=FileIO.readFromFile("Lexique.txt");
		ArrayList<WordObject> words=new ArrayList<WordObject>();
		FrequencyModel model=new FrequencyModel();
		for(String line:lines){
			words.add(new WordObject(line));
		}
		for(WordObject word:words){
			model.add(word.getSpelling(),word.bookCorpusFreq());
		}
		return model;
	}
	
	public static String correct(String word,FrequencyModel model){
		ArrayList<Set<String>> corrections;
		int wordLength=word.length();
		int generationCost=wordLength*2+wordLength-1+CorrectionGenerator.ALPHABET.length*wordLength;
		generationCost=(int)(Math.pow(generationCost,MAX_EDIT_DISTANCE));
		if(generationCost<model.keySet().size()*wordLength){
			corrections = CorrectionGenerator.generateCorrections(word);
		}	
		else{
			corrections=new ArrayList<Set<String>>();
			HashSet<String> singleWord=new HashSet<String>();
			singleWord.add(word);
			corrections.add(singleWord);
			for(int i=1;i<=MAX_EDIT_DISTANCE;i++){
				corrections.add(new HashSet<String>());
			}
			for(String lexWord:model.keySet()){
				int editDistance=Distance.levensteinDistance(word,lexWord);
				if(editDistance>0&&editDistance<=MAX_EDIT_DISTANCE){
					corrections.get(editDistance).add(lexWord);
				}
			}
		}
		for(Set<String> editSet:corrections){
			editSet.retainAll(model.keySet());
			if(!editSet.isEmpty())
				return keyMax(editSet,model);
		}
		return word;
	}
	
	public static String keyMax(Set<String> keys,FrequencyModel model){
		String maxString="";
		double maxVal=0;
		for(String key:keys){
			if(model.getFrequency(key)>maxVal){
				maxVal=model.getFrequency(key);
				maxString=key;
			}
		}
		return maxString;
	}
	
	public static void main(String[] args) {
		Scanner input=new Scanner(System.in);
		boolean finished=false;
		FrequencyModel model=trainModel();
		while(!finished){
			System.out.println("Enter a word to test: ");
			System.out.println(correct(input.next(),model));
		}
		input.close();
	}

	
	
}
