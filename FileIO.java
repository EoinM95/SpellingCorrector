import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
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
	
}
