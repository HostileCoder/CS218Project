package CS218Project;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import org.cloudbus.cloudsim.Vm;
import org.cloudbus.cloudsim.provisioners.RamProvisioner;

public class VMLoadBalancer {
	
//	private HashMap<Integer, myVm> Vms = new HashMap<Integer, myVm>();
//	private HashMap<Integer, myVm> newVms = new HashMap<Integer, myVm>();
//	private HashMap<Integer, myVm> deltaVms = new HashMap<Integer, myVm>();
//	private HashMap<Integer, myVm> Normal = new HashMap<Integer, myVm>();
//	private HashMap<Integer, myVm> Warn = new HashMap<Integer, myVm>();
//	private HashMap<Integer, myVm> Critical = new HashMap<Integer, myVm>();
	private RamProvisioner ramProvisioner=null;
	private ArrayList<myVm> Vms = new ArrayList<myVm>();
	private ArrayList<myVm> newVms = new ArrayList<myVm>();
	private ArrayList<myVm> deltaVms = new ArrayList<myVm>();
	private ArrayList<myVm> Normal = new ArrayList<myVm>();
	private ArrayList<myVm> Warn = new ArrayList<myVm>();
	private ArrayList<myVm> Critical = new ArrayList<myVm>();
	private ArrayList<myVm> NormVMallocateList = new ArrayList<myVm>();
	private ArrayList<myVm> WarnVMallocateList = new ArrayList<myVm>();
	private int L=0;
	private int L1=0;
	private int L2=0;
	private int N=0;
	
	public VMLoadBalancer(List<Vm> vmlist, RamProvisioner ramProvisioner){
		this.ramProvisioner=ramProvisioner;
		for(Vm vm:vmlist){
			//Vms.put(vm.getId(),(myVm) vm);
			Vms.add((myVm) vm);
		}
		classify();
	}
	
	public void classify(){
		for(myVm vm:Vms){
			double r=vm.getRamToUserRatio();
			if(r<0.30){
				//Critical.put(vm.getId(), vm);
				Critical.add(vm);
			}else if(0.3>=r && r<0.7){
				//Warn.put(vm.getId(), vm);
				Warn.add(vm);
			}else if(r>=0.7){
				//Normal.put(vm.getId(),  vm);
				Normal.add(vm);
			}		
		}
	
	}
	
	public void balanceVMs(){
		if(!Critical.isEmpty()){
			
			for(myVm vm:Critical){
				L=L+getRequiredRam(vm);
				vm.setReqRam(getRequiredRam(vm));
			}
			sortDemandVMs(Critical);
			
			for(myVm vm:Normal){
				L1=L1+getFreeRamNorm(vm);
				vm.setSpareRam(getFreeRamNorm(vm));
				NormVMallocateList.add(vm);
				if(L1>L)
					break;
			}
			sortSupplyVMs(NormVMallocateList);
			
			if(L1>L){
				newVms= update(Vms);
				return;
			}else{
				NormToWarn();
				//N=L-L1;
			}
			
			for(myVm vm:Warn){
				L2=L2+getFreeRamWarn(vm);
				vm.setSpareRam(getFreeRamWarn(vm));
				WarnVMallocateList.add(vm);
				if(L2>L)
					break;
			}
			sortSupplyVMs(WarnVMallocateList);
			
			if(L2>L){
				newVms= update(Vms);
				return;
			}else{
				classify();
				System.out.println("Low Ram");
			}				
		
		}else if(!Normal.isEmpty()&&!Warn.isEmpty()){
		
			
		}
		
		L=0;
		L1=0;
		L2=0;
		classify();
	}
	
	
	public int getRequiredRam(myVm vm){	
	   double x =vm.getUserSize()*0.3;
	   return (int) Math.ceil(x);	   
	}
	
	public int getFreeRamNorm(myVm vm){
		double x =vm.getUserSize()*0.7;
		int y =vm.getRam()-(int) Math.ceil(x);	
		return y;
	}
	
	public int getFreeRamWarn(myVm vm){
		double x =vm.getUserSize()*0.5;
		int y =vm.getRam()-(int) Math.ceil(x);	
		return y;
	}
	
	public ArrayList<myVm> update(ArrayList<myVm> Vms){
		if(L1>L){
			
			for(myVm vm:NormVMallocateList){
				int free=vm.getSpareRam();
				vm.setSpareRam(0);
				ramProvisioner.allocateRamForVm(vm, vm.getRam()-free);
			}
			int required=0;
			for(myVm vm:Critical){
				required=vm.getReqRam();
				vm.setReqRam(0);
				ramProvisioner.allocateRamForVm(vm, vm.getRam()+required);
			}
			
			
			
		}else if(L2>L){
			
			for(myVm vm:Warn){
				int free=vm.getSpareRam();
				vm.setSpareRam(0);
				ramProvisioner.allocateRamForVm(vm, vm.getRam()-free);
			}
		
			for(myVm vm:Critical){
				int required=vm.getReqRam();
				vm.setReqRam(0);
				ramProvisioner.allocateRamForVm(vm, vm.getRam()+required);
			}
		}
	
		return Vms;	
	}
	
	
	
	public void NormToWarn(){	
		for(myVm vm:Normal){
			//Warn.put(vm.getId(), vm);
			Warn.add(vm);
		}
		Normal.clear();
	}
	
	//H to L
	public void sortSupplyVMs(ArrayList<myVm> x){
			Collections.sort(x,new Comparator<myVm>()
			{
				public int compare(myVm v1, myVm v2)
				{
					int x= v1.getSpareRam()-v2.getSpareRam();
					if(x>0){
						return -1;
					}else if(x<0){
						return 1;
					}
					return 0;
				}
			});
	}
	
	////L to H
	public void sortDemandVMs(ArrayList<myVm> x){
		Collections.sort(x,new Comparator<myVm>()
		{
			public int compare(myVm v1, myVm v2)
			{
				int x= v1.getReqRam()-v2.getReqRam();
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
