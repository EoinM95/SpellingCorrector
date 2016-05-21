import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Classe qui contient des methodes pour lire/écrire d'un fichier
 * @author Eoin Murphy
 *
 */
public class FileIO {
	/**
	 * 
	 * @param filename, le fichier d'où on va lire
	 * @return Un ArrayList<String> de chaque ligne du fichier
	 */
	public static ArrayList<String> readFromFile(String filename){
		BufferedReader reader=null;
		try {
			reader = new BufferedReader(new FileReader (filename));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		String line = null;
		ArrayList<String> returnList = new ArrayList<String>();		

		try {
			if(reader==null) return null;
			while((line = reader.readLine())!= null){
				returnList.add(line);
			}
		} catch(IOException e){
			e.printStackTrace();
		}
		finally {
			if(reader!=null)
				try {
					reader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
		}
		return returnList;
	}
	
	/**
	 * Ecrire dans un fichier (Cette fonctionne va écraser tous ce qui était dans le fichier avant)
	 * @param data, les donnés à stocker
	 * @param filename, le fichier où il faut les stocker 
	 * @return true, ssi il n'y avait aucune erreur en les enregistrant 
	 */
	public static boolean write(String data, String filename){
		return write(data,filename,true);
	}
	
	/**
	 * Ecrire dans un fichier, on peut spécifier si on ajoute/écrase 
	 * @param data
	 * @param filename
	 * @param overwrite, true=écraser;false=ajouter à la fin
	 * @return
	 */
	public static boolean write(String data, String filename, boolean overwrite){
		try {
			BufferedWriter w=new BufferedWriter(new FileWriter(filename,!overwrite));
			w.write(data);
			w.close();
			return true;
		} catch (IOException e) {
			return false;
		}
	}
}
