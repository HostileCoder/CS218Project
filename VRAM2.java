package CS218Project;

import java.util.ArrayList;

import org.cloudbus.cloudsim.File;

public class VRAM2 {
	private int RamID=0;
	private int RamSize=0;
	private int freeSpace=0;
	private int used=0;
	private int index=0;
	public ArrayList<File> fileList= new ArrayList<File>();
	
	public VRAM2(int size, int RamID){
		this.setRamID(RamID);
		this.setSize(size);
		setFreeSpace(size);		
	}
	
	public void addSpace(int s){
		freeSpace=freeSpace+s;
		RamSize=RamSize+s;
	}
	
	public void removeSpace(int s){		
		if(freeSpace-s<0){
			//future
		}
		freeSpace=freeSpace-s;
		RamSize=RamSize-s;
	}
	
	public void addFile(File file){		
		if(freeSpace==0){
			return;
		}
		if(RamSize-used+file.getSize()<0){
			return;
		}
		fileList.add(file);		
		used=used+file.getSize();
		freeSpace=RamSize-used;
	}
	
	
	public void removeFile(File file){
		if( fileList.contains(file)){
			fileList.remove( fileList.indexOf(file));
		}else{
			return;
		}
		used=used-file.getSize();
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
		return "ID:"+RamID+
				" RamSize:"+RamSize+
				" freeSpace:"+freeSpace+
				" used:"+used;	
	}
	
	public void printContent(){
		int i=0;
		for(File f: fileList){
			System.out.println(i+1+" "+f);
			i++;
		}
	}
	
}
