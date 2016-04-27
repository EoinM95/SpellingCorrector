import java.util.ArrayList;
import java.util.Scanner;
import java.util.Set;


public class Corrector {
	
	public static final int MAX_EDIT_DISTANCE=2;
	
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
		ArrayList<Set<String>> corrections = CorrectionGenerator.generateCorrections(word);
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
