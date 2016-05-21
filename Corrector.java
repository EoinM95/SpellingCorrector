import java.util.ArrayList;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

/**
 * Une structure qui nous permet de suggérer des corrections pour des mots mal écrit
 * @author Eoin Murphy
 *
 */
public class Corrector {
	
	public static final int MAX_EDIT_DISTANCE=4;
	//private String lexFile;
	private ArrayList<WordObject> words;
	private FrequencyModel model;
	/**
	 * 
	 * @param lexFile, le fichier où le lexique est stocké
	 */
	public Corrector(String lexFile){
		ArrayList<String> lines=FileIO.readFromFile(lexFile);
		words=new ArrayList<WordObject>();
		for(String line:lines){
			words.add(new WordObject(line));
		}
		//setHomophones();
		trainModel();
	}
	/**
	 * Chercher pour des homophones pour chaque mot
	 */
	private void setHomophones() {
		words.parallelStream().filter(w->w.numberOfHomophones()>0).forEach(w->w.addHomophones(words));
		/*for(WordObject word:words)
			if(word.numberOfHomophones()>0)
				word.addHomophones(words);*/
	}

	/**
	 * Créer un nouveau FrequencyModel
	 * Ajouter chaque mot et sa fréquence
	 */
	private void trainModel(){
		model=new FrequencyModel();
		for(WordObject word:words){
			model.add(word.getSpelling(),word.bookCorpusFreq());
		}
	}
	
	/**
	 * 
	 * @param word, le mot à corriger
	 * @return un mot bien écrit qui est le plus 
	 * 		probable étant donné le mot mal écrit
	 */
	public String correct(String word){
		ArrayList<Set<String>> corrections;
		int wordLength=word.length();
		/*
		 * Tester le cout de faire la génération
		 */
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
	
	public String[] correctList(String word, int k){
		ArrayList<Set<String>> corrections;
		int wordLength=word.length();
		String[] returnList=new String[k];
		/*
		 * Tester le cout de faire la génération
		 */
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
		int correctionsFound=0;
		while(correctionsFound<k)
			for(Set<String> editSet:corrections){
				editSet.retainAll(model.keySet());
				if(!editSet.isEmpty()){
					String correction=keyMax(editSet, model);
					returnList[correctionsFound++]=correction;
					editSet.remove(correction);
				}
			}
		return returnList;
	}
	
	private String keyMax(Set<String> keys,FrequencyModel model){
		String maxString="";
		double maxVal=0;
		for(String key:keys){
			double freq=model.getFrequency(key);
			if(freq>maxVal){
				maxVal=model.getFrequency(key);
				maxString=key;
			}
		}
		return maxString;
	}
	
	public static void main(String[] args) {
		Scanner input=new Scanner(System.in);
		boolean finished=false;
		Corrector c=new Corrector("Lexique.txt");
		/*c.setHomophones();
		for(WordObject word:c.words){
			System.out.println(word);
			for(WordObject homophone:word.getHomophones()){
				System.out.println(" "+homophone);
			}
		}*/
		while(!finished){
			System.out.println("Enter a word to test: ");
			System.out.println(c.correct(input.next()));
		}
		input.close();
	}

	
	
}
