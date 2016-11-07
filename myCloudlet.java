package CS218Project;

import org.cloudbus.cloudsim.Cloudlet;
import org.cloudbus.cloudsim.File;
import org.cloudbus.cloudsim.UtilizationModel;

public class myCloudlet extends Cloudlet {
	
	private UE_Context ue = null;
	
	

	public myCloudlet(int cloudletId, long cloudletLength, int pesNumber, long cloudletFileSize,
			long cloudletOutputSize, UtilizationModel utilizationModelCpu, UtilizationModel utilizationModelRam,
			UtilizationModel utilizationModelBw, UE_Context ue) {
		
		super(cloudletId, cloudletLength, pesNumber, cloudletFileSize, cloudletOutputSize, utilizationModelCpu,
				utilizationModelRam, utilizationModelBw);
		this.setUe(ue);
		
	}



	public UE_Context getUE() {
		return ue;
	}



	public void setUe(UE_Context ue) {
		this.ue = ue;
	}
	

}
