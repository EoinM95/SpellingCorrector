import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class CorrectionEvaluator {

	private static final String OUTPUT_FILE="correction_log.txt";
	
	public static void main(String[] args){
		File output=new File(OUTPUT_FILE);
		try {
			output.createNewFile();
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
		Corrector c=new Corrector(file);
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
		String lineFormat="(?<correction>.*) *: *(?<error>.*)";
		Pattern linePattern=Pattern.compile(lineFormat);
		Matcher m;
		StringBuilder logData=new StringBuilder();
		double success=0;
		for(String line:testData){
			m=linePattern.matcher(line);
			if(m.matches()){
				String error=m.group("error").toLowerCase();
				String correctReal=m.group("correction").toLowerCase();
				String correctionAttempt=c.correct(error);
				logData.append("Mistake: "+error+" Real correction: "+correctReal
						+" Suggested correction: "+correctionAttempt);
				logData.append(System.getProperty("line.separator"));
				if(correctReal.equals(correctionAttempt)){
					success++;
				}
			}
		}
		String successRating="Success rating: "+((success/(double)testData.size()*100)+"%");
		logData.append(successRating);
		System.out.println(successRating);
		if(!FileIO.write(logData.toString(),OUTPUT_FILE)){
			System.out.println("Un erreur d'IO s'est passé en sauvgardant le fichier");
		}
		s.close();
	}
}
