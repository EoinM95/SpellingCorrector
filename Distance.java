
public class Distance {

	/**
	 * Une fonctionne qui calcul la distance de Levenstein entre deux chaines
	 * Adapté de l'implementation décit ici: 
	 * 		https://en.wikipedia.org/wiki/Levenshtein_distance#Computing_Levenshtein_distance
	 * @param a
	 * @param b
	 * @return 
	 */
	public static int levensteinDistance(String a, String b){
		int len0 = a.length() + 1;                                                     
	    int len1 = b.length() + 1;                                                     
	                                                                                         
	    int[] cost = new int[len0];                                                     
	    int[] newCost = new int[len0];                                                  
	                                                                                                                     
	    for (int i = 0; i < len0; i++)
	    	cost[i] = i;                                     
	                                                                                                                        
	    for (int j = 1; j < len1; j++) {                                                                             
	        newCost[0] = j;                                
	        for(int i = 1; i < len0; i++) {               
	            int match = (a.charAt(i - 1) == b.charAt(j - 1)) ? 0 : 1;                               
	            int costReplace = cost[i - 1] + match;                                 
	            int costInsert  = cost[i] + 1;                                         
	            int costDelete  = newCost[i - 1] + 1;                                                   
	            newCost[i] = Math.min(Math.min(costInsert, costDelete), costReplace);
	        }                                                 
	        int[] swap = cost; 
	        cost = newCost; 
	        newCost = swap;                          
	    }        
	    return cost[len0 - 1]; 
	}
	
	public static void main(String[] args){
		System.out.println(levensteinDistance("cataclysm","cat"));
	}
}
