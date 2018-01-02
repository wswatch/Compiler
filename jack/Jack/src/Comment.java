import java.util.ArrayList;
class CommentError extends Exception {
	public CommentError(String s) {
		super(s);
	}
}
class Translate {		// the comment part
	private boolean isInComment;
	private ArrayList<String> tokenOut;
	private ArrayList<String> parseOut;
	public Translate() {
		isInComment = false;
		tokenOut = null;
		parseOut = null;
	}
	public String deleteComment(String str) {		// delete one line
		String newTxt = "";
		boolean notStop = true;
		boolean isStar = false;
		boolean isSlash = false;
		int validStart = 0;
		char ch;
		
		for(int j = 0; j < str.length() && notStop; ++j) {
			ch = str.charAt(j);
			if(isInComment) {		// find 
				if(isStar) {
					if(ch == '/') {
						isInComment = false;
						validStart = j+1;
					}
					else if(ch != '*')
						isStar = false;
				}
				else if(ch == '*'){
					isStar = true;
				}
			}
			else {
				if (isSlash) {
					if (ch == '/') {
						newTxt = newTxt + str.substring(validStart,j-1);
						notStop = false;
					}
					else if(ch == '*') {
						newTxt = newTxt + str.substring(validStart,j-1);
						isInComment = true;
					}
					else
						isSlash = false;	
				}
				else if (ch == '/') {
					isSlash = true;
				}
			}
		}
		if(notStop && (isInComment == false)) {	// if it is not in comment
			if (validStart < str.length()) {
				if(newTxt.length() == 0) {
					newTxt = str.substring(validStart);
				}
				else {
					newTxt = newTxt + " " + str.substring(validStart);
				}
			}
		}
		return newTxt;
	}
	public void run(ArrayList<String> lines) throws CommentError, TokenError, ParseError {
		String newTxt;
		String str;
		ArrayList<Token> tokenList = null;
		isInComment = false;
		int i = 0;
		try {
			Tokenizer tokenizer = new Tokenizer();
			for(i = 0; i < lines.size(); ++i) {			
				newTxt = deleteComment(lines.get(i));
				if(newTxt.length() > 0) {		// add a new line
					tokenizer.translate(newTxt,i+1);
				}
			}
			tokenOut = tokenizer.getOut();	// get tokens
			tokenList = tokenizer.getTokenList();
			Parser parser = new Parser(tokenList);
			parser.run();
			parseOut = parser.output();
			if (isInComment) {
				throw new CommentError("Comment does not meet a end mark");
			}
		}
		catch (TokenError e) {
			throw new TokenError("line "+ i + ": "+e.getMessage());
		}
		catch (CommentError e) {
			throw new CommentError("line "+ i + ": "+e.getMessage());
		}
	}
	public ArrayList<String> getTokens() {
		return tokenOut;
	}
	public ArrayList<String> getParse() {
		return parseOut;
	}
}