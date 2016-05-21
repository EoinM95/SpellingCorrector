import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;


public class WordObject {
	private final String spelling,phonetic;
	private final int numberOfHomophones;
	private final double filmFrequency,bookFrequency;
	private List<WordObject> homophones;
	public WordObject(String line){
		String[] elements=line.split("\t");
		assert elements.length==5;
		spelling=elements[0].toLowerCase();
		filmFrequency=Double.parseDouble(elements[1]);
		bookFrequency=Double.parseDouble(elements[2]);
		numberOfHomophones=Integer.parseInt(elements[3]);
		phonetic=elements[4];
		homophones=new ArrayList<WordObject>();
	}
	
	public String getSpelling(){
		return spelling;
	}
	
	public String getPhonetic(){
		return phonetic;
	}
	
	public int numberOfHomophones(){
		return numberOfHomophones;
	}
	
	public double filmCorpusFreq(){
		return filmFrequency;
	}
	
	public double bookCorpusFreq(){
		return bookFrequency;
	}
	
	public boolean areHomophones(WordObject other){
		return phonetic.equals(other.phonetic);
	}
	
	public void addHomophones(ArrayList<WordObject> words){
		if(numberOfHomophones>0){
			homophones=words.parallelStream().filter(w->(!w.equals(this)&&w.phonetic.equals(phonetic))).collect(Collectors.toList());
		}
	}
	
	public List<WordObject> getHomophones(){
		return homophones;
	}
}
