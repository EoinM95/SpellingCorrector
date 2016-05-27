import java.io.File;
import java.util.Scanner;
public class CorrectorMain {

	private static Scanner input;
	private static Corrector corrector;
	public static void main(String[] args) {
		input=new Scanner(System.in);
		boolean finished=false;
		String file="";
		File f=new File(file);
		while(!f.exists()){
			System.out.println("Entrez le fichier où se trouve le lexique");
			file=input.next();
			f=new File(file);
			if(!f.exists())
				System.out.println("Erreur: fichier pas trouvée");
		}
		corrector=new Corrector(file);
		
		while(!finished){
			System.out.println("Pour tester si le corrector peut corriger un mot (mot à mot) taper: 0");
			System.out.println("Pour corriger tous les mots d'un texte taper: 1");
			System.out.println("Pour tester l'effacité du corretor taper: 2");
			System.out.println("Pour terminé le programme taper: 3");
			int choice;
			try{
				choice=input.nextInt();
			} catch (java.util.InputMismatchException e){
				System.out.println("Votre choix n'était pas reconnu");
				continue;
			}
			switch(choice){
			case 0:
				correctWordByWord();
				break;
			case 1:
				correctText();
				break;
			case 2:
				setUpTest();
				break;
			default:
				finished=true;
				break;
			}
			
		}
		input.close();
		
	}
	
	private static void correctWordByWord(){
		System.out.println("Entez un mot: ");
		String word=input.next();
		String[] correctList=corrector.correctList(word,3);
		for(String correction:correctList)
			System.out.println(correction);
	}
	
	private static void correctText(){
		System.out.println("Entrez le nom du fichier de texte");
		String filename=input.next();
		File file=new File(filename);
		if(!file.exists()){
			System.out.println("Fichier pas trouvé");
			return;
		}
		System.out.println("Entrez un nom du fichier où vous voulez stocké les corrections");
		String outputFile=input.next();
		corrector.correctTextFromFile(filename,outputFile);
	}
	
	private static void setUpTest(){
		String file="";
		File f=new File(file);
		while(!f.exists()){
			System.out.println("Entrez le fichier où se trouve les donnés à tester");
			file=input.next();
			f=new File(file);
			if(!f.exists())
				System.out.println("Erreur: fichier pas trouvée");
		}
		CorrectionEvaluator eval=new CorrectionEvaluator(corrector,file);
		eval.runTest();
	}
}
