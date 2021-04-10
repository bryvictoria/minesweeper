import java.io.*;
import java.nio.*;
public class FileHandler{
	String content = "";
	File file;
	FileReader fr;
	FileWriter fw;
	BufferedReader br;
	BufferedWriter bw;
	
	public String readFile(String fileName){
		try{
			
			file = new File(fileName);
			fr = new FileReader(file);
			
			br = new BufferedReader(fr);
			
			StringBuilder sb = new StringBuilder();
			String line;

			do{
				line = br.readLine();
				if(line != null){
					sb.append(line);
					sb.append(System.lineSeparator());
				}
			} while (line != null) ;
			br.close();
			content = sb.toString();
		}catch(Exception e){
			
			System.out.println("Error Encountered:"+e.toString());
			e.printStackTrace();
			
		}
		return content;
		
	}
	
	public void writeFile(String fileName,String content){
		try{
			
			file = new File(fileName);
			fw = new FileWriter(file);
			
			bw = new BufferedWriter(fw);
			bw.write(content);
			
			bw.close();
		}catch(Exception e){
			
			System.out.println("Error Encountered:"+e.toString());
			e.printStackTrace();
			
		}
	}
	
}