package CS218Project;

import java.util.ArrayList;

import org.cloudbus.cloudsim.File;
import org.cloudbus.cloudsim.ParameterException;

public class VRAM {
	private int RamID=0;
	private int RamSize=0;
	private int freeSpace=0;
	private int used=0;
	private int index=0;
	public ArrayList<File> ram= new ArrayList<File>();
	
	public VRAM(int size, int RamID){
		this.setRamID(RamID);
		this.setSize(size);
		setFreeSpace(size);		
		for(int i=0;i<size;i++){
			ram.add(null);
		}
		index=0;
	}
	
	public void addSpace(int s){
		for(int i=0;i<s;i++){
			ram.add(null);
		}
		freeSpace=freeSpace+s;
		RamSize=RamSize+s;
	}
	
	public void removeSpace(int s){
		for(int i=0;i<s;i++){
			File f= ram.get(ram.size()-1);
			if(f==null){
				freeSpace--;
			}else{
				used--;
			}
			RamSize--;
			ram.remove(ram.size()-1);
		}
		index = findfirstNull();
	}
	
	public void addFile(File file){		
		if(freeSpace==0){
			return;
		}		
		ram.set(index, file);
		index = findfirstNull();
		used++;
		freeSpace=RamSize-used;
	}
	
	
	public void removeFile(File file){
		if(ram.contains(file)){
			ram.remove(ram.indexOf(file));
			ram.add(null);
		}else{
			return;
		}
		used--;
		index= findfirstNull();
		freeSpace=RamSize-used;
	}
	

	public int getFreeSpace() {
		return freeSpace;
	}

	public void setFreeSpace(int freeSpace) {
		this.freeSpace = freeSpace;
	}

	public int getSize() {
		return RamSize;
	}

	public void setSize(int RamSize) {
		this.RamSize = RamSize;
	}

	public int getUsed() {
		return used;
	}

	public void setUsed(int used) {
		this.used = used;
	}

	public int getRamID() {
		return RamID;
	}

	public void setRamID(int ramID) {
		RamID = ramID;
	}
	
	public String toString(){
		return "ID "+RamID+" RamSize "+RamSize+" freeSpace "+freeSpace+" used "+used+" index "+index;	
	}
	
	public void printContent(){
		int i=0;
		for(File f:ram){
			System.out.println(i+1+" "+f);
			i++;
		}
	}
	
	public int findfirstNull(){
		for(int i=0;i<RamSize;i++){
			if(ram.get(i)==null){
				return i;
			}
		}
		return RamSize;
	}
}
