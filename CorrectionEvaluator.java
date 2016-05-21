import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Classe qui sert à tester le fonctionnement de Corrector
 * On lit le fichier Lexique et crée un objet Corrector
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
	private static Object correctedLock=new Object();
	private static Object failedLock=new Object();
	private static final String lineFormat="(?<correction>.*) *: *(?<error>.*)";
	private static StringBuilder correctData=new StringBuilder();
	private static StringBuilder failedData=new StringBuilder();
	private static final Pattern linePattern=Pattern.compile(lineFormat);
	private static Corrector corrector;
	private static double success=0;
	private static class SplitThread implements Runnable{
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
	
	public static void main(String[] args){
		try {
			new File(CORRECTED_FILE).createNewFile();
			new File(FAILED_FILE).createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
		Scanner s=new Scanner(System.in);
		String file="";
		File f=new File(file);
		while(!f.exists()){
			System.out.println("Entrez le fichier où se trouve le lexique");
			file=s.next();
			f=new File(file);
			if(!f.exists())
				System.out.println("Erreur: fichier pas trouvée");
		}
		corrector=new Corrector(file);
		file="";
		f=new File(file);
		while(!f.exists()){
			System.out.println("Entrez le fichier où se trouve les donnés à tester");
			file=s.next();
			f=new File(file);
			if(!f.exists())
				System.out.println("Erreur: fichier pas trouvée");
		}
		ArrayList<String> testData=FileIO.readFromFile(file);
		System.out.println("On a bien lu le contenu du fichier");
		System.out.println("On commence à tester...");
		Thread a=new Thread(new SplitThread(testData.subList(0,testData.size()/4)));
		Thread b=new Thread(new SplitThread(testData.subList(testData.size()/4,testData.size()/2)));
		Thread c=new Thread(new SplitThread(testData.subList(testData.size()/2,(testData.size()/4)*3)));
		Thread d=new Thread(new SplitThread(testData.subList((testData.size()/4)*3,testData.size())));
		a.start();
		b.start();
		c.start();
		d.start();
		try {
			a.join();
			b.join();
			c.join();
			d.join();
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
		s.close();
	}
	
	private static boolean testAndStore(String error,String correctReal){
		String correctionAttempt;
		correctionAttempt=corrector.correct(error);
		String logMessage="Mistake: "+error+" Real correction: "+correctReal
				+" Suggested correction: "+correctionAttempt+System.getProperty("line.separator");
		if(correctReal.equals(correctionAttempt)){
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
