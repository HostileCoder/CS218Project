package CS218Project;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.SortedMap;
import java.util.TreeMap;

/*
 * Title: CloudSim Toolkit Description: CloudSim (Cloud Simulation) Toolkit for Modeling and
 * Simulation of Clouds Licence: GPL - http://www.gnu.org/copyleft/gpl.html
 * 
 * Copyright (c) 2009-2012, The University of Melbourne, Australia
 */


import org.cloudbus.cloudsim.CloudletScheduler;
import org.cloudbus.cloudsim.Vm;



/**
 * Represents a Virtual Machine (VM) that runs inside a Host, sharing a hostList with other VMs. It processes
 * cloudlets. This processing happens according to a policy, defined by the CloudletScheduler. Each
 * VM has a owner, which can submit cloudlets to the VM to execute them.
 * 
 * @author Rodrigo N. Calheiros
 * @author Anton Beloglazov
 * @since CloudSim Toolkit 1.0
 */
public class myVm extends Vm {

	private VRAM2 vram;
	private Ratio ratio=new Ratio(Math.pow(10, -3));
	private History history = new History(0,0,0,ratio.getRatio());
	private ArrayList<UE_Context> CacheState = new ArrayList<UE_Context>();
	//SortedMap<UE_Context, String> CacheState = new TreeMap<UE_Context, String>( new CUEC());
	
	private int reqRam=0;
	private int spareRam=0;
	private int userSize=0;
	private int ramSpace=0;
	private int numAccess=0;
	private double CPUload=0;
	
	/**
	 * Creates a new Vm object.
	 * 
	 * @param id unique ID of the VM
	 * @param userId ID of the VM's owner
	 * @param mips the mips
	 * @param numberOfPes amount of CPUs
	 * @param ram amount of ram
	 * @param bw amount of bandwidth
	 * @param size The size the VM image size (the amount of storage it will use, at least initially).
	 * @param vmm virtual machine monitor
	 * @param cloudletScheduler cloudletScheduler policy for cloudlets scheduling
         * 
	 * @pre id >= 0
	 * @pre userId >= 0
	 * @pre size > 0
	 * @pre ram > 0
	 * @pre bw > 0
	 * @pre cpus > 0
	 * @pre priority >= 0
	 * @pre cloudletScheduler != null
	 * @post $none
	 */
	public myVm(
			int id,
			int userId,
			double mips,
			int numberOfPes,
			int ram,
			long bw,
			long size,
			String vmm,
			CloudletScheduler cloudletScheduler){
		super(id,userId, mips, ram, ram, size, size, vmm, cloudletScheduler);
		setVram(new VRAM2(ram,id));
		setId(id);
		setUserId(userId);
		setUid(getUid(userId, id));
		setMips(mips);
		setNumberOfPes(numberOfPes);
		setRam(ram);
		setBw(bw);
		setSize(size);
		setVmm(vmm);
		setCloudletScheduler(cloudletScheduler);

		setInMigration(false);
		setBeingInstantiated(true);

		setCurrentAllocatedBw(0);
		setCurrentAllocatedMips(null);
		setCurrentAllocatedRam(0);
		setCurrentAllocatedSize(0);
		

	}


	public VRAM2 getVram() {
		return vram;
	}


	public void setVram(VRAM2 vram) {
		this.vram = vram;
	}
	


	public History getHistory() {
		return history;
	}


	public void setHistory(History history) {
		this.history = history;
	}


	public ArrayList<UE_Context> getCacheState() {
		return CacheState;
	}


	public void setCacheState(ArrayList<UE_Context> cacheState) {
		CacheState = cacheState;
	}


	public int getReqRam() {
		return reqRam;
	}


	public void setReqRam(int reqRam) {
		this.reqRam = reqRam;
	}


	public int getUserSize() {
		return userSize;
	}


	public void setUserSize(int userSize) {
		this.userSize = userSize;
	}
	
	public double getRamToUserRatio(){
		return (double)getRam()/(double)userSize;
	}


	public int getSpareRam() {
		return spareRam;
	}


	public void setSpareRam(int spareRam) {
		this.spareRam = spareRam;
	}


	public int getRamSpace() {
		ramSpace=vram.getFreeSpace();
		return ramSpace;
	}

	public int getNumAccess() {
		return numAccess;
	}

	public int incNumAccess() {
		return numAccess=1+numAccess;
	}
	
	public double getCPUload() {
		return CPUload;
	}

	public void addCPUload(int c) {
		if(c==0){
			CPUload = CPUload + 1.0;
		}else if(c==1){
			CPUload = CPUload + .82;
		}else if(c==2){
			CPUload = CPUload + 1.24;
		}else if(c==3){
			CPUload = CPUload + .26;
		}else if(c==4){
			CPUload = CPUload + 2.31;
		}		
	}
	
	
	public static void sortRamSpace(ArrayList<myVm> x)
	{
		Collections.sort(x,new Comparator<myVm>()
		{
			@Override
			public int compare(myVm o1, myVm o2) {
				int x= o1.getRamSpace()-o2.getRamSpace();
					if(x>0){
						return -1;
					}else if(x<0){
						return 1;
					}
					return 0;
				}
			
		});
	}
	
	public static void sortAccess(ArrayList<myVm> x)
	{
		Collections.sort(x,new Comparator<myVm>()
		{
			@Override
			public int compare(myVm o1, myVm o2) {
				int x= o1.getNumAccess()-o2.getNumAccess();
					if(x>0){
						return 1;
					}else if(x<0){
						return -1;
					}
					return 0;
				}
			
		});
	}

	
	public static void sortCPU(ArrayList<myVm> x)
	{
		Collections.sort(x,new Comparator<myVm>()
		{
			@Override
			public int compare(myVm o1, myVm o2) {
				double x= o1.getCPUload()-o2.getCPUload();
					if(x>0){
						return 1;
					}else if(x<0){
						return -1;
					}
					return 0;
				}
			
		});
	}
	
	
	public static void sortUEC(ArrayList<myVm> x)
	{
		Collections.sort(x,new Comparator<myVm>()
		{
			@Override
			public int compare(myVm o1, myVm o2) {
				double x= o1.getCacheState().get(0).getRatio()-o2.getCacheState().get(0).getRatio();
					if(x>0){
						return 1;
					}else if(x<0){
						return -1;
					}
					return 0;
				}
			
		});
	}


	
	
}
