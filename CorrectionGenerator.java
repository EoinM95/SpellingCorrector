import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;


public class CorrectionGenerator {
	
	public static final char[] ALPHABET="abcdefghijklmnopqrstuvwxyzàáèéçùâêîôûëïüÿ".toCharArray();
	
	public static ArrayList<Set<String>> generateCorrections(String word){
		ArrayList<Set<String>> corrections=new ArrayList<Set<String>>();
		HashSet<String> editDistanceOne=editDistanceOne(word);
		corrections.add(editDistanceOne);
		HashSet<String> previousSet=editDistanceOne;
		for(int i=1;i<Corrector.MAX_EDIT_DISTANCE;i++){
			HashSet<String> nextSet=new HashSet<String>();
			for(String correction:previousSet){
				nextSet.addAll(editDistanceOne(correction));
			}
			corrections.add(nextSet);
			previousSet=nextSet;
		
		}
		return corrections;
	}
	
	private static HashSet<String> editDistanceOne(String word){
		HashSet<String> corrections=new HashSet<String>();
		ArrayList<StringTuple> splits=new ArrayList<StringTuple>();
		for(int i=0;i<word.length();i++){
			splits.add(new StringTuple(word.substring(0,i),word.substring(i,word.length())));
		}
		corrections.addAll(deletions(splits));
		corrections.addAll(transpositions(splits));
		corrections.addAll(insertions(splits));
		corrections.addAll(replacements(splits));
		return corrections;
	}
	
	private static HashSet<String> deletions(ArrayList<StringTuple> splits){
		HashSet<String> deletions=new HashSet<String>();
		for(StringTuple tuple:splits){
			String a=tuple.a;
			String b=tuple.b;
			deletions.add(a+b.substring(1,b.length()));
			
		}
		return deletions;
	}
	private static HashSet<String> transpositions(ArrayList<StringTuple> splits){
		HashSet<String> transpositions=new HashSet<String>();
		for(StringTuple tuple:splits){
			String a=tuple.a;
			String b=tuple.b;
			if(b.length()>1)
				transpositions.add(a+b.charAt(1)+b.charAt(0)+b.substring(2,b.length()));
			
		}
		return transpositions;
	}
	private static HashSet<String> insertions(ArrayList<StringTuple> splits){
		HashSet<String> insertions=new HashSet<String>();
		for(char c:ALPHABET)
			for(StringTuple tuple:splits){
				String a=tuple.a;
				String b=tuple.b;
				insertions.add(a+c+b);

			}
		return insertions;
	}
	private static HashSet<String> replacements(ArrayList<StringTuple> splits){
		HashSet<String> replacements=new HashSet<String>();
		for(char c:ALPHABET)
			for(StringTuple tuple:splits){
				String a=tuple.a;
				String b=tuple.b;
				replacements.add(a+c+b.substring(1,b.length()));

			}
		return replacements;
	}
	
}
