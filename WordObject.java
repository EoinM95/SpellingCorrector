
public class WordObject {
	private final String spelling,phonetic;
	private final int numberOfHomophones;
	private final double filmFrequency,bookFrequency;
	
	public WordObject(String line){
		String[] elements=line.split("\t");
		assert elements.length==5;
		spelling=elements[0];
		filmFrequency=Double.parseDouble(elements[1]);
		bookFrequency=Double.parseDouble(elements[2]);
		numberOfHomophones=Integer.parseInt(elements[3]);
		phonetic=elements[4];
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
}
