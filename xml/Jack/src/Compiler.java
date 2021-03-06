
import java.util.ArrayList;

public class Compiler {
	public static void main(String[] args) {
		try {
			ArrayList<String> list = null;
			ArrayList<String> list1 = null;
			ArrayList<String> list2 = null;
			if(args.length == 1) {
				Translate compiler = new Translate();
				RW_File rwfile = new RW_File(args[0]);
				list = rwfile.Read();
				while(list != null) {
					compiler.run(list);
					list1 = compiler.getTokens();
					rwfile.Write(list1,false);
					list2 = compiler.getParse();
					rwfile.Write(list2,true);
					rwfile.nextline();
					list = rwfile.Read();
				}
			}
			else {
				System.out.println("You can only input one file or directory at a time");
			}
			System.out.println("Finish");
		}
		catch (FileException e) {
			System.out.println(e.getMessage());
		}
		catch (TokenError e) {
			System.out.println(e.getMessage());
		} 
		catch (CommentError e) {
			System.out.println(e.getMessage());
		}
		catch (ParseError e) {
			System.out.println(e.getMessage());
		}
	}
	public void test(String[] args) {
		try {
			ArrayList<String> list = null;
			ArrayList<String> list1 = null;
			ArrayList<String> list2 = null;
			if(args.length == 0) {
				String s = "/Users/shuaiwang/Desktop/10/Square";
				RW_File rwfile = new RW_File(s);
				Translate compiler = new Translate();
				list = rwfile.Read();
				while(list != null) {
					compiler.run(list);
					list1 = compiler.getTokens();
					rwfile.Write(list1,false);
					list2 = compiler.getParse();
					rwfile.Write(list2,true);
					rwfile.nextline();
					list = rwfile.Read();
				}
				System.out.println("Finish");
			}
			else {
				System.out.println("You can only input one file or directory at a time");
			}
		}
		catch (FileException e) {
			System.out.println(e.getMessage());
		}
		catch (TokenError e) {
			System.out.println(e.getMessage());
		}
		catch (CommentError e) {
			System.out.println(e.getMessage());
		} 
		catch (ParseError e) {
			System.out.println(e.getMessage());
		}
	}
}
