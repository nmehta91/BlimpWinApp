import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

// Singleton class used to store syntax information in me
public class SyntaxModel {
	private static SyntaxModel instance = null;
	public Path dataSetPath;
	public String syntaxFilePath;
	public String outputFilePath;
	public HashMap <String, String> mappings;
	public ArrayList<Variable> variables, modelVariables, identifierVariables, allVariables;
	// identifierVariableLocations specifies the indexes of model.variables which it contains - Used for Data View under Impute
	public Set<Integer> identifierVariablesLocations;
	public ArrayList<String> importFileContents;
	public String importFileContentsInString;
	public HashMap<String, String> mcmcOptions;
	
	public static SyntaxModel getInstance(){
		if(instance == null) {
			instance = new SyntaxModel();
			instance.dataSetPath = null;
			instance.syntaxFilePath = null;
			instance.outputFilePath = null;
			instance.mappings = new HashMap <String, String> ();
			instance.mcmcOptions = new HashMap <String, String> ();
			instance.variables = new ArrayList<Variable> ();
			instance.allVariables = new ArrayList<Variable>();
			instance.modelVariables = new ArrayList<Variable> ();
			instance.identifierVariables = new ArrayList<Variable> ();
			instance.identifierVariablesLocations = new HashSet<Integer>();
		}	
		return instance;
		
	}
	
	public static void clearModel() {
		instance.mappings.clear();
		instance.variables.clear();
		instance.allVariables.clear();
		instance.modelVariables.clear();
		instance.identifierVariables.clear();
		instance.dataSetPath = null;
		instance.importFileContentsInString = "";
		instance.outputFilePath = null;
		instance.syntaxFilePath = null;
	}
}
