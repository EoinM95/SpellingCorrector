import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Classe qui sert à tester le fonctionnement de Corrector
 * On lit un fichier avec des mots mal écrit (qui doit etre dans le format 
 * 		(?<correction>.*) *: *(?<error>.*) à chaque ligne )
 * On compare la suggestion de Corrector avec le mot original
 * On enregistre les résultats
 * @author Eoin Murphy
 *
 */
public class CorrectionEvaluator {
	private static final String CORRECTED_FILE="correction_log.txt";
	private static final String FAILED_FILE="failed_log.txt";
	private static final int NUMBER_OF_THREADS=4;
	private Object correctedLock=new Object();
	private Object failedLock=new Object();
	private static final String lineFormat="(?<correction>.*) *: *(?<error>.*)";
	private StringBuilder correctData=new StringBuilder();
	private StringBuilder failedData=new StringBuilder();
	private static final Pattern linePattern=Pattern.compile(lineFormat);
	private Corrector corrector;
	private double success;
	private ArrayList<String> testData;
	private class SplitThread implements Runnable{
		private volatile List<String> data;
		public SplitThread(List<String> d){
			data=d;
		}
		
		@Override
		public void run() {
			for(String line:data){
				Matcher m=linePattern.matcher(line);
				if(m.matches()){
					testAndStore(m.group("error").toLowerCase(),m.group("correction").toLowerCase());
				}
			}
		}
		
	}
	
	public CorrectionEvaluator(Corrector c, String filename){
		corrector=c;
		try {
			new File(CORRECTED_FILE).createNewFile();
			new File(FAILED_FILE).createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
		success=0;
		testData=FileIO.readFromFile(filename);
	}
	
	public void runTest(){
		System.out.println("On a bien lu le contenu du fichier");
		System.out.println("On commence à tester...");
		Thread[] threads=new Thread[NUMBER_OF_THREADS];
		int dataSize=testData.size();
		for(int i=0;i<NUMBER_OF_THREADS;i++){
			threads[i]=new Thread(new SplitThread(testData.subList(i*(dataSize/NUMBER_OF_THREADS),(i+1)*(dataSize/NUMBER_OF_THREADS))));
			threads[i].start();
		}
		try {
			for(int i=0;i<NUMBER_OF_THREADS;i++){
				threads[i].join();
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		String successRating="Success rating: "+((success/(double)testData.size()*100)+"%");
		correctData.append(successRating);
		System.out.println(successRating);
		if(!FileIO.write(correctData.toString(),CORRECTED_FILE)
				||!FileIO.write(failedData.toString(),FAILED_FILE)){
			System.out.println("Un erreur d'IO s'est passé en sauvgardant les fichiers");
		}

	}
	
	private boolean testAndStore(String error,String correctReal){
		String attempt=corrector.correct(error);
		String logMessage="Mistake: "+error+" Real correction: "+correctReal
				+" Suggested correction: "+attempt+System.getProperty("line.separator");
		if(correctReal.equals(attempt)){
			synchronized(correctedLock){
				correctData.append(logMessage);
				success++;
			}
			return true;
		}
		synchronized(failedLock){
			failedData.append(logMessage);
		}
		return false;
	}
}
