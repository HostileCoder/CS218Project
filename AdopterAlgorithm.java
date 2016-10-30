package CS218Project;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;



class AdopterAlgorithm implements Runnable{
	//private History history = null;
	private int eventNum=0;
	private int eventCap=10;
	private int groupSize=0;
	private double localratio=0.0;
	private double localBHR=0.0;
	private double localBIR=0.0;
	private double localpreBHR=0.0;
	private double localpreBIR=0.0;
	private ArrayList <History> records = new ArrayList<History>();
	private Ratio ratio=null;
	
	private TreeMap<Double, Group> groups = new TreeMap<Double, Group>();
	private Map<Double,valueSet> candidateValue = new  HashMap<Double,valueSet>();
	
	public AdopterAlgorithm(int groupSize, ArrayList <History> records, Ratio ratio){
		this.setGroupSize(groupSize);
		this.records = records;
		this.ratio=ratio;
		double threshold = 1.0/(double) groupSize;
		double name=0.0;
		
		for(int i=0;i<groupSize;i++){
			Group g= new Group(Double.toString(name));
			groups.put(name, g);
			name=name+threshold;
			name=Math.round(name*10.0)/10.0;
		}

		//ratio
		for(int i=1;i<100;i++){
			candidateValue.put( (double)i, new valueSet((double)i)  );
		}
				
		for(valueSet e:candidateValue.values() ){
			group(e);
		}	
	}

	
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		while(true)
			while(! records.isEmpty()){
				body();
			}
	}
	
	
	public void body(){
				
		History history = records.remove(0);
		
		eventNum++;				
		valueSet x=candidateValue.get(history.getVarRatio());
		x.setValueBHR(history.getBHR());
		x.setValueBIR(history.getBIR());
		
		if(eventNum==eventCap){
			eventNum=0;			
			x.setValueBHR(( history.getBHR() + history.getPreBHR() )/2      );
			x.setValueBIR(( history.getBIR() + history.getPreBIR() )/2      );
			
			for(Group g: groups.values()){
				if(g.getValueSetList().containsKey(x.getValueRatio())){
					g.getValueSetList().remove(x.getValueRatio());
				}
			}			
			group(x);	
			
			
			double max=0;
			Group selected = null;
			for(Group g: groups.values()){
				if(g.getRep()>max){
					max=g.getRep();
					selected = g;
				}
			}
															
			ratio.setRatio(selected.getSmallestBIR().getValueRatio());									
		}
					
	}
	
	public void group(valueSet v){
		double x = v.getValueBHR();
		if(x<=0.1){
			Group g = groups.get(0.0);
			g.addvalue(v);
		}else if(x>0.1 && x<=0.2){
			Group g = groups.get(0.1);
			g.addvalue(v);
		}else if(x>0.2 && x<=0.3){
			Group g = groups.get(0.2);
			g.addvalue(v);
		}else if(x>0.3 && x<=0.4){
			Group g = groups.get(0.3);
			g.addvalue(v);
		}else if(x>0.4 && x<=0.5){
			Group g = groups.get(0.4);
			g.addvalue(v);
		}else if(x>0.5 && x<=0.6){
			Group g = groups.get(0.5);
			g.addvalue(v);
		}else if(x>0.6 && x<=0.7){
			Group g = groups.get(0.6);
			g.addvalue(v);
		}else if(x>0.7 && x<=0.8){
			Group g = groups.get(0.7);
			g.addvalue(v);
		}else if(x>0.8 && x<=0.9){
			Group g = groups.get(0.8);
			g.addvalue(v);
		}else if(x>0.9){
			Group g = groups.get(0.9);
			g.addvalue(v);
		}		
	}
	
	public Map<Double, Group> getGroups(){
		return groups;
	}



	public int getGroupSize() {
		return groupSize;
	}



	public void setGroupSize(int groupSize) {
		this.groupSize = groupSize;
	}



	public double getLocalratio() {
		return localratio;
	}



	public void setLocalratio(double localratio) {
		this.localratio = localratio;
	}



	public double getLocalBHR() {
		return localBHR;
	}



	public void setLocalBHR(double localBHR) {
		this.localBHR = localBHR;
	}



	public double getLocalBIR() {
		return localBIR;
	}



	public void setLocalBIR(double localBIR) {
		this.localBIR = localBIR;
	}



	public double getLocalpreBHR() {
		return localpreBHR;
	}



	public void setLocalpreBHR(double localpreBHR) {
		this.localpreBHR = localpreBHR;
	}



	public double getLocalpreBIR() {
		return localpreBIR;
	}



	public void setLocalpreBIR(double localpreBIR) {
		this.localpreBIR = localpreBIR;
	}
	
	

}