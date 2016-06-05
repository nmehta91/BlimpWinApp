import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;

// Singleton class used to store syntax information in me
public class SyntaxModel {
	private static SyntaxModel instance = null;
	public Path dataSetPath;
	public HashMap <String, ArrayList<String[]>> mappings;
	public ArrayList<String[]> variables;
	
	public ArrayList<String> importFileContents;
	public String importFileContentsInString;
	
	public static SyntaxModel getInstance(){
		if(instance == null) {
			instance = new SyntaxModel();
			instance.mappings = new HashMap <String, ArrayList<String[]>> ();
			instance.variables = new ArrayList<String[]> ();
		}
		return instance;
		
	}
}
