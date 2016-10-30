package CS218Project;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

public class Distribution {
	private double lambda;
	private double num;
	private ArrayList<Double> list = new ArrayList<Double>();
	private ArrayList<Double> listFile = new ArrayList<Double>();
	
	public Distribution(double lambda, int num){
		this.setLambda(lambda);
		this.num=num;		
	}
	
	public void fillList(){
		list.clear();
		for(int i=0;i<num;i++){
			double x=Math.round(nextTime(lambda));
			list.add(x);
		}		
	}
	
	public void writeToFile(String FileName){
	    PrintWriter writer;
		try {
			writer = new PrintWriter(FileName+".txt", "UTF-8");
			for(double x:list){
				writer.println(x);
			}
		    writer.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	public void fillListFile(String FileName) {
		listFile.clear();
		String filePath = new File("").getAbsolutePath();
		filePath = filePath+"\\"+FileName;

		BufferedReader input;
		try {
			input = new BufferedReader(new FileReader(filePath));
			String line = "";

			while ((line = input.readLine()) != null) {
					listFile.add(Double.parseDouble(line));
			}		
		
		} catch (NumberFormatException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
	}
	
	
	public double nextTime(double lambda){
		double u = - Math.log(Math.random());
		u = u/lambda;
		return u;
	}


	public double getNum() {
		return num;
	}
	
	public void setNum(int num) {
		this.num=num;
	}


	public double getLambda() {
		return lambda;
	}


	public void setLambda(double lambda) {
		this.lambda = lambda;
	}

	public ArrayList<Double> getListFile() {
		return listFile;
	}

	public void setListFile(ArrayList<Double> listFile) {
		this.listFile = listFile;
	}

	public ArrayList<Double> getList() {
		return list;
	}

	public void setList(ArrayList<Double> list) {
		this.list = list;
	}

	
}


