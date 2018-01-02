import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
class FileException extends Exception{		// throw error
	public FileException(String s) {
		super(s);
	}
}
class RW_File {
	private ArrayList<File> fileList = null;
	private int fplace = 0;		
	private boolean ForD = false;
	private String destination = null;
	public RW_File(String path) throws FileException {
		fileList = new ArrayList<File>();		// initialize
		fplace = 0;
		File file = new File(path);	// read path
		if(file.exists()) {
			if(file.isDirectory()) {
				ForD = true;			// get the information that it is a directory
				File[] flist = file.listFiles();
				for(int i = 0; i < flist.length; ++i) {
					if(isVmFile(flist[i])) {	// check if a file is a vm file.
						fileList.add(flist[i]);
					}
					else {
						System.out.println(flist[i].getName()+" is not a vm file, it will be ignored");
					}
				}
			}
			else {
				ForD = false;		// is a file
				if (isVmFile(file)){ 
					fileList.add(file);
				}
				else {
					throw new FileException(file.getName()+" is not a vm file!");
				}
			}
		}
		else {
			throw new FileException("Fail to open File "+path+".");
		}
		destination=AsmFile(file);		// get the address of the asm file.
	}
	public boolean isDirectory() {		// return the file
		return ForD;
	}
	private boolean isVmFile(File file) {		// check if a file is a vm file
		String name = file.getName();
		boolean res = false;
		if (file.isFile()) {
			if (name.substring(name.length()-3,name.length()).equals(".vm") ) {
				res = true;
			}
		}
		return res;
	}
	public ArrayList<String> Read() throws FileException{
		if(fplace == fileList.size()) {		// read all files
			return null;
		}
		else {			// read file
			File file = fileList.get(fplace);
			ArrayList<String> sarray = ReadFile(file);
			return sarray;
		}
	}
	public ArrayList<String> ReadFile(File file) throws FileException{
		try {
			FileReader fileReader = new FileReader(file);
			BufferedReader buff = new BufferedReader(fileReader);
			ArrayList<String> sarray = new ArrayList<String>();
			String line;
		
			while( (line = buff.readLine())!= null) {
				sarray.add(line);
			}
			return sarray;
		}
		catch (IOException e) {
			throw new FileException("Fail to read fiile"+file.getName());
		}
	}
	private String AsmFile(File file) {		// create the destination file	
		String path = file.getAbsolutePath();
		if(ForD) {
			String asmPath = file.getAbsolutePath();
			String name = file.getName()+".asm";
			Path p = Paths.get(asmPath,name);
			path = p.toString();
		}
		else {
			path = path.substring(0,path.length()-3)+".asm";
		}
		return path;
	}
	// build the destination file and the lead part
	public void create(String leadcode) throws IOException {
		File targetfile = new File(destination); 
		boolean mk = targetfile.createNewFile();
		if (mk) {
			System.out.println("Create Destination asm file");
		}
		else {
			System.out.println("There exists the destination asm file");
		}
		PrintWriter writer = new PrintWriter(targetfile);
		if(ForD) {			// if it is a directory, then add the initial part
			writer.print(leadcode);
		}
		else {				// if it is a file, just let it be empty
			writer.print("");
		}
		writer.close();
	}
	public void Write(ArrayList<String> text) throws FileException {
		if(fplace == fileList.size()) {		// write all files
			return;
		}
		else {			// write file
			String data = "";
			for(int i = 0; i < text.size(); ++i) {		// move arraylist into a single string
//				data = data + text.get(i)+System.getProperty("line.separator");
				data = data + text.get(i);
			}
			File file = fileList.get(fplace);
			WriteFile(data,destination,file.getName());
		}
	}
	public void WriteFile(String text, String path, String name)throws FileException{	// write text to path
		try {
			FileWriter fw = new FileWriter(path,true);	// add content to 
			BufferedWriter buff = new BufferedWriter(fw);
			PrintWriter out = new PrintWriter(buff);
			out.write(text);
			out.close();
		}
		catch(IOException e) {
			throw new FileException("Fail to write " + name); 
		}		
	}
	public void nextline() {		// move to next line
		fplace = fplace + 1;
	}
	public String getName() {
		File file = fileList.get(fplace);
		String name = file.getName();
		return name.substring(0,name.length()-3);
	}
}
