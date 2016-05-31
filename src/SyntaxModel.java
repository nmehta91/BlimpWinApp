import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;

// Singleton class used to store syntax information in me
public class SyntaxModel {
	private static SyntaxModel instance = null;
	public Path dataSetPath;
	public HashMap <String, String> mappings;
	
	public ArrayList<String> importFileContents;
	
	public static SyntaxModel getInstance(){
		if(instance == null) {
			instance = new SyntaxModel();
			instance.mappings = new HashMap <String, String> ();
		}
		return instance;
		
	}
}
