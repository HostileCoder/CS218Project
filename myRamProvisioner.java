package CS218Project;

import java.util.HashMap;
import java.util.Map;

import org.cloudbus.cloudsim.Vm;
import org.cloudbus.cloudsim.provisioners.RamProvisioner;

public class myRamProvisioner extends RamProvisioner{
	
	/** The RAM map, where each key is a VM id and each value
     * is the amount of RAM allocated to that VM. */
	private Map<String, Integer> ramTable;
	
	/**
	 * Instantiates a new ram provisioner simple.
	 * 
	 * @param availableRam The total ram capacity from the host that the provisioner can allocate to VMs. 
	 */
	public myRamProvisioner(int ram) {
		super(ram);
		setRamTable(new HashMap<String, Integer>());
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean allocateRamForVm(Vm vm, int ram) {
		int maxRam = vm.getRam();
                /* If the requested amount of RAM to be allocated to the VM is greater than
                the amount of VM is in fact requiring, allocate only the
                amount defined in the Vm requirements.*/
		if (ram >= maxRam) {
			ram = maxRam;
		}

		deallocateRamForVm(vm);

		if (getAvailableRam() >= ram) {
			setAvailableRam(getAvailableRam() - ram);
			getRamTable().put(vm.getUid(), ram);
			vm.setCurrentAllocatedRam(getAllocatedRamForVm(vm));
			return true;
		}

		vm.setCurrentAllocatedRam(getAllocatedRamForVm(vm));

		return false;
	}

	@Override
	public int getAllocatedRamForVm(Vm vm) {
		if (getRamTable().containsKey(vm.getUid())) {
			return getRamTable().get(vm.getUid());
		}
		return 0;
	}

	@Override
	public void deallocateRamForVm(Vm vm) {
		if (getRamTable().containsKey(vm.getUid())) {
			int amountFreed = getRamTable().remove(vm.getUid());
			setAvailableRam(getAvailableRam() + amountFreed);
			vm.setCurrentAllocatedRam(0);
		}
	}

	@Override
	public void deallocateRamForAllVms() {
		super.deallocateRamForAllVms();
		getRamTable().clear();
	}

	@Override
	public boolean isSuitableForVm(Vm vm, int ram) {
		int allocatedRam = getAllocatedRamForVm(vm);
		boolean result = allocateRamForVm(vm, ram);
		deallocateRamForVm(vm);
		if (allocatedRam > 0) {
			allocateRamForVm(vm, allocatedRam);
		}
		return result;
	}

	/**
	 * Gets the map between VMs and allocated ram.
	 * 
	 * @return the ram map
	 */
	protected Map<String, Integer> getRamTable() {
		return ramTable;
	}

	/**
	 * Sets the map between VMs and allocated ram.
	 * 
	 * @param ramTable the ram map
	 */
	protected void setRamTable(Map<String, Integer> ramTable) {
		this.ramTable = ramTable;
	}


}
