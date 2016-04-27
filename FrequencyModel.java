import java.util.Hashtable;
import java.util.Set;


public class FrequencyModel {
	/**
	 * @author Eoin Murphy
	 * Class which represents frequency model/default dictionary,
	 * default value always 1
	 */
	Hashtable<String,Double> hashtable;
	public FrequencyModel(){
		hashtable=new Hashtable<String,Double>();
	}
	
	public Double getFrequency(String key){
		if(!hashtable.containsKey(key)) return (double) 1.0/1000000.0;
		return hashtable.get(key);
	}
	
	public void add(String word,Double frequency){
			hashtable.put(word,frequency);
	}

	public Set<String> keySet() {
		return hashtable.keySet();
	}
	
}
