import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Set;
class XML {
	String content;
	String type;
	XML(String str) {
		content = "";
		int n = str.length()-2;
		int i = 0;
		while( str.charAt(i) != '<' ) { ++i; }
		++i;
		int j = i+1;
		while (str.charAt(j) != '>') ++j;
		type = str.substring(i,j);
		if(j < n) {
			i = j + 2;
			j = i;
			while (str.charAt(j) != '<') { ++j; }
			content = str.substring(i,j-1);
		}
	}
}
class Element {
	public String type;
	public String kind;
	public int num;
}
class symbolTable {
	symbolTable parent;		// the class parent of a function symbolTable
	String kind;			// it is class/ method, function, constructor
	int argnum;
	int localnum;
	int staticnum;
	Hashtable<String, Element> hash = null;
	symbolTable() {
		parent = null;
		argnum = 0;
		localnum = 0;
		staticnum = 0;
		hash = new Hashtable<String, Element>();
	}
	int getArg() {
		return argnum;
	}
	int getLol() {
		return localnum;
	}
	void show() {
		Set<String> keys = hash.keySet();
		Element e;
		for(String str: keys) {
			e = hash.get(str);
			System.out.println(str+"  "+e.kind + "  "+e.type+"  "+e.num);
		}
	}
	Element get(String name) {
		return hash.get(name);
	}		// add it to the symbolTable
	public void add(String name, String type, String kind) {
		Element e = new Element();
		if (kind.equals("field") || kind.equals("var")) {
			e.kind = "local";
			e.type = type;
			e.num = localnum;
			++localnum;
		}
		else if(type.equals("static")){
			e.kind = kind;
			e.type = type;
			e.num = staticnum;
			++staticnum;
		}
		else {
			e.kind = kind;
			e.type = type;
			e.num = argnum;
			++argnum;
		}
		hash.put(name, e); 
	}
}
class buildTable {
	ArrayList<String> xmlList;
	Hashtable<String, symbolTable> hash = null;
	int lineNum;
	int endNum;
	String className;
	buildTable() {
		hash = new Hashtable<String,symbolTable>();
	}
	void setTable(ArrayList<String> list) {
		xmlList = list;
		lineNum = 0;
		endNum = list.size();
		className = null;
		run();
	}
	Hashtable<String, symbolTable> getTable() {		// return the hash table
		return hash;
	}
	String getNext() {				// get the next xml
		XML xml = null;
		String res = null;
		if (lineNum < endNum) {
			xml = new XML(xmlList.get(lineNum));
			if (xml.content.length() == 0)
				res = xml.type;
			else
				res = xml.content;
			++lineNum;
		}
		return res;
	}
	String topXML() {				// peek the top XML
		XML xml = new XML(xmlList.get(lineNum));
		String res = null;
		if (xml.content.length() == 0)
			res = xml.type;
		else
			res = xml.content;
		return res;
	}
	void classVarDec(symbolTable table) {
		String kind = "";
		String type = "";
		String varName = "";
		getNext();		// <classVarDec>
		kind = getNext();
		type = getNext();
		varName = getNext();
		table.add(varName, type, kind);
		while (getNext().equals(",")) {
			varName = getNext();
			table.add(varName, type, kind);  // varName
		}
		++lineNum;		// </classVarDec>
	}
	void classTable() {
		String name = getNext();		// class's name
		className = name;		// get the class name
		getNext();		// "{"
		symbolTable table = new symbolTable();
		while (topXML().equals("classVarDec")) {
			classVarDec(table); 
		}
//		System.out.println(className);
//		table.show();
		hash.put(name, table);
	}
	void parameterList(symbolTable table) {
		++lineNum;
		String str = getNext();
		String type;
		if (str.equals("/parameterList")) {		// empty
			return;
		}
		type = str;
		str = getNext();
		table.add(str, type, "argument");
		str = getNext();
		while(str.equals(",")) {
			type = getNext();
			str = getNext();
			table.add(str, type, "argument");
			str = getNext();
		}
	}
	void varDec(symbolTable table) {
		getNext();		// var
		String type = getNext();
		String name = getNext();
		table.add(name, type, "var");
		while (getNext().equals(",")) {	// can be ',' or ';'
			name = getNext();
			table.add(name, type, "var");
		}
		getNext();		// /varDec
	}
	void subroutineDec() {
		symbolTable table = new symbolTable();
		table.parent = hash.get(className);
		table.kind = getNext();		// return method,function or construction
		++lineNum;		// return type
		String name = className + "." + getNext();
		++lineNum;		// (
		if (table.kind.equals("method")) {		// method add a element
			table.add("this", className, "argument");
		}
		parameterList(table);
		String str;
		lineNum = lineNum + 3;		// ) <subroutineBody> {
		while (getNext().equals("varDec")) {
			varDec(table);
		}
//		System.out.println(name);
//		table.show();
		hash.put(name, table);
	}
	void run(){
		String str;
		++lineNum;		// <class>
		str = getNext();
		while(str != null) {
			if (str.equals("class")) {		// get the class
				classTable();
			}
			else if (str.equals("subroutineDec")) {
				subroutineDec();
			}
			str = getNext();
		}
	}
}
