import java.util.ArrayList;
import java.util.Hashtable;

class Jack {
	Hashtable<String,symbolTable> table;
	ArrayList<String> xmlList;
	String className;
	symbolTable funcTable;
	ArrayList<String> vmList;
	int listNum;
	int ifNum;
	int whileNum;
	Jack(Hashtable<String,symbolTable> table) {
		this.table = table;
	}
	void set(ArrayList<String> xml) {
		xmlList = xml;
		listNum = 0;
		ifNum = 0;
		whileNum = 0;
		vmList = new ArrayList<String>();
		run();
	}
	ArrayList<String> getVm(){
		return vmList;
	}
	String getType() {
		XML xml = new XML(xmlList.get(listNum));
		return xml.type;
	}
	String getNext() {				// get the next xml
		XML xml = null;
		String res = null;
		if (listNum < xmlList.size()) {
			xml = new XML(xmlList.get(listNum));
			if (xml.content.length() == 0)
				res = xml.type;
			else
				res = xml.content;
			++listNum;
		}
		return res;
	}
	String topXML() {				// peek the top XML
		XML xml = new XML(xmlList.get(listNum));
		String res = null;
		if (xml.content.length() == 0)
			res = xml.type;
		else
			res = xml.content;
		return res;
	}
	String Type(String symbol) {
		Element ele = funcTable.get(symbol);
		String res;
		if (ele == null) {
			ele = table.get(className).get(symbol);
			res = ele.type;
		}
		else {
			res = ele.type;
		}
		return res;
	}
	String address(String symbol) {
		Element ele = funcTable.get(symbol);
		String res;
		if (ele == null) {
			ele = table.get(className).get(symbol);
			if (ele == null) {
				res = null;
			}
			else if (ele.kind.equals("local")) {
				res = "this " + ele.num;
			}
			else 
				res = ele.kind + " " + ele.num;
		}
		else {
			res = ele.kind + " " + ele.num;
		}
		return res;
	}
	void run(){
		String str;
		listNum = listNum + 2;		// <class> class
		className = getNext();
		str = getNext();
		while(str != null) {
			if (str.equals("subroutineDec")) {
				subroutineDec();
			}
			str = getNext();
		}
	}
	void writefunctionHead(String funcName) {
		vmList.add("function "+funcName + " " + funcTable.getLol()+"\n");
		if (funcTable.kind.equals("method")) {
			vmList.add("push argument 0\n");
			vmList.add("pop pointer 0\n");
		}
		else if (funcTable.kind.equals("constructor")) {
			int classParament = table.get(className).getLol();
			vmList.add("push constant "+classParament+"\n");
			vmList.add("call Memory.alloc 1\n");
			vmList.add("pop pointer 0\n");
		}
	}
	void statements() {
		++listNum;		// <statements>
		String kind = getNext();
		boolean notStop = true;
		while(notStop) {
			switch(kind) {
			case "letStatement": letStatement();break;
			case "ifStatement": ifStatement();break;
			case "whileStatement": whileStatement();break;
			case "doStatement": doStatement();break;
			case "returnStatement": returnStatement();break;
			default: notStop = false;	// </statements>
			}
			if(notStop)
				kind = getNext();
		}
	}
	void subroutineDec() {
		listNum = listNum + 2;	// ignore type & return of a function
		String funcName = className + "." + getNext();
		funcTable = table.get(funcName);
		// add the function line
		writefunctionHead(funcName);
		while (getNext().equals("statements") == false) {}
		// move to statements
		--listNum;
		statements();
	}
	void writeString(String txt) {
		vmList.add("push constant "+txt.length() + "\n");
		vmList.add("call String.new 1\n");
		int t = 0;
		for (int i = 0; i < txt.length(); ++i) {
			t = txt.charAt(i);
			vmList.add("push constant " + t + "\n");
			vmList.add("call String.appendChar 2\n");
		}
	}
	boolean writeKeyword(String txt) {
		boolean res = true;
		if (txt.equals("true")) {
			vmList.add("push constant 0\n");
			vmList.add("not\n");
		}
		else if(txt.equals("false") || txt.equals("null")) {
			vmList.add("push constant 0\n");
		}
		else if (txt.equals("this")) {
			vmList.add("push pointer 0\n");
		}
		else 
			res = false;
		return res;
	}
	void array(String base,boolean lr) {		// the array is to store sth or 
		String ad = address(base);			// get the value
		vmList.add("push "+ad+"\n");
		expression();
		vmList.add("add\n");
		if (lr) {			// get the value
			vmList.add("pop pointer 1\n");
			vmList.add("push that 0\n");
		}
		else {		// store the address at temp 1
			vmList.add("pop temp 1\n");
		}
		++listNum;		// ]
	}
	void term () {
		++listNum;		// <term>
		String type = getType();
		String word = getNext();
		if (type.equals("stringConstant")) {
			writeString(word);
		}
		else if(type.equals("integerConstant")) {
			vmList.add("push constant "+word+"\n");
		}
		else if(writeKeyword(word) == false) {	//if it is keyword, do it
			if (word.equals("-")) {
				term();
				vmList.add("neg\n");
			}
			else if (word.equals("~")) {
				term();
				vmList.add("not\n");
			}
			else if (word.equals("(")) {
				expression();
				++listNum;   // )
			}
			else {			// identifier
				String nextw = getNext();
				if (nextw.equals("/term")) {
					String ad = address(word);
					vmList.add("push "+ad+"\n");
					--listNum;
				}
				else if(nextw.equals("[")) {
					array(word,true);
				}
				// subroutineCall
				else {
					--listNum;
					subroutineCall(word);
				}
			}
		}
		++listNum;		// </term>
	}
	void subroutineCall(String word) {
		String nextw = getNext();
		if(nextw.equals(".")) {
			String varName = address(word);
			int n = 0;
			if (varName == null) {
				String func = word + "." + getNext();
				++listNum;	// (
				n = expressionList();
				vmList.add("call "+ func + " "+n+"\n");
				++listNum;	// )
			}
			else {
				String func = Type(word) + "." + getNext();
				++listNum;	// (
				vmList.add("push "+varName+"\n");		// push this
				n = expressionList() + 1;
				++listNum;
				vmList.add("call "+func+ " "+n+"\n");
			}
		}
		// subroutineName (expressionList)
		else {
			int n = 0;
			String func = className + "." + word;
			symbolTable s = table.get(func);
			if (s == null) {
				// error handle
			}
			else {
				// method
				if (s.kind.equals("method")) {
					vmList.add("push pointer 0\n");	// this
					n = expressionList() + 1;
					vmList.add("call " + func + " "+n+"\n");
					++listNum;	// )
				}
				else {
					n = expressionList();
					++listNum;			// )
					vmList.add("call "+func+ " "+n+"\n");
				}
			}
		}
	}
	int expressionList() {
		++listNum;		//<expressionList>
		int n = 0;
		String word = getNext();
		if (word.equals("/expressionList") == false) {
			--listNum;
			expression();
			n = 1;
			word = getNext();
			while(word.equals(",")) {
				++n;
				expression();
				word = getNext();
			}
		}
		// the final word is </expressionList>
		return n;
	}
	String op() {
		String optxt = getNext();
		String res;
		switch(optxt) {
		case "+": res = "add\n";break;
		case "-": res = "sub\n";break;
		case "*": res = "call Math.multiply 2\n";break;
		case "/": res = "call Math.divide 2\n";break;
		case "&amp;": res = "and\n";break;
		case "|": res = "or\n";break;
		case "&lt;": res = "lt\n";break;
		case "&gt;": res = "gt\n";break;
		default: res = "eq\n";			// = 
		}
		return res;
	}
	void expression() {
		++listNum;		// <expression>
		term();	
		String optxt;
		while(topXML().equals("/expression") == false) {
			optxt = op();
			term();
			vmList.add(optxt);
		}
		++listNum;		// </expression>
	}
	void letStatement() {
		boolean notArray = true;
		++listNum;		// let
		String varName = getNext();		// varName
		if (getNext().equals("[")) {		// array
			array(varName,false);
			notArray = false;
			++listNum;			// = 
		}
		//	else getNext() will get = 
		expression();
		if(notArray) {
			String ad = address(varName);
			vmList.add("pop "+ad+"\n");
		}
		else {
			vmList.add("push temp 1\n");
			vmList.add("pop pointer 1\n");
			vmList.add("pop that 0\n");
		}
		listNum = listNum + 2;	// ; </letStatement>
	}
	void ifStatement() {
		listNum = listNum + 2;		// if (
		expression();
		++listNum;						// )
		vmList.add("not\n");
		String L1 = "IF_FALSE"+ifNum;
		String L2 = "IF_CONTINUE"+ifNum;
		++ifNum;
		vmList.add("if-goto "+L1+"\n");
		++listNum;					// {
		statements();
		++listNum;					// }
		vmList.add("goto "+L2+"\n");
		vmList.add("label "+L1+"\n");
		
		String elsePart= getNext();
		if (elsePart.equals("else")) {
			++listNum;			// {
			statements();
			listNum = listNum + 2;	// } </ifStatement>
		}
		vmList.add("label "+L2+"\n");
	}
	void whileStatement() {
		listNum = listNum + 2;		//while (
		String L1 = "WHILE_EXP"+ whileNum;
		String L2 = "WHILE_END"+whileNum;
		++whileNum;
		vmList.add("label "+L1+"\n");
		expression();
		vmList.add("not\n");
		vmList.add("if-goto "+L2+"\n");
		listNum = listNum+2;			//) {
		statements();
		vmList.add("goto "+L1+"\n");
		vmList.add("label "+L2+"\n");
		
		listNum = listNum + 2;		// } </whileStatement>		
	}
	void doStatement() {
		++listNum;		// do
		String word = getNext();
		subroutineCall(word);
		vmList.add("pop temp 0\n");		// meanless return
		listNum = listNum + 2;		// ; </doStatement>
	}
	void returnStatement() {
		++listNum;		// return
		if(topXML().equals(";")) {
			vmList.add("push constant 0\n");
		}
		else {
			expression();
		}
		vmList.add("return\n");
		listNum = listNum + 2;		// ; </returnStatement>
	}
}
