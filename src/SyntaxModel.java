import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;

// Singleton class used to store syntax information in me
public class SyntaxModel {
	private static SyntaxModel instance = null;
	public Path dataSetPath;
	public HashMap <String, String> mappings;
	public ArrayList<String[]> variables, modelVariables, identifierVariables;
	
	public ArrayList<String> importFileContents;
	public String importFileContentsInString;
	
	public static SyntaxModel getInstance(){
		if(instance == null) {
			instance = new SyntaxModel();
			instance.mappings = new HashMap <String, String> ();
			instance.variables = new ArrayList<String[]> ();
			instance.modelVariables = new ArrayList<String[]> ();
			instance.identifierVariables = new ArrayList<String[]> ();
		}
		return instance;
		
	}
	
	public void clearModel() {
		mappings.clear();
		variables.clear();
	}
}
