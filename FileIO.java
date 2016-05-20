import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;


public class FileIO {
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
			while( ( line = reader.readLine() ) != null ) {
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
	
	public static boolean write(String data, String filename){
		return write(data,filename,true);
	}
	
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
