
public class Variable {
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
}
