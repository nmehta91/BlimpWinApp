import java.util.Comparator;

public class Variable implements Comparable<Variable>{
	String name;
	String type;
	int position;
	
	Variable() {
		name = "";
		type = "";
		position = -1;
	}
	
	Variable(String name, String type, int position) {
		this.name = name;
		this.type = type;
		this.position = position;
	}

	@Override
	public int compareTo(Variable v2) {
		return this.position - v2.position;
	}
}
