package CS218Project;


import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.cloudbus.cloudsim.Log;



class AdaptorAlgorithm implements Runnable{
	//private History history = null;
	private int eventNum=0;
	private int eventCap=3;
	private int groupSize=0;
	private ArrayList <History> records = new ArrayList<History>();
	private Ratio ratio=null;
	
	private TreeMap<Double, Group> groups = new TreeMap<Double, Group>();
	private Map<Double,valueSet> candidateValue = new  TreeMap<Double,valueSet>();
	
	public AdaptorAlgorithm(int groupSize, ArrayList <History> records, Ratio ratio){
		this.groupSize = groupSize;
		this.records = records;
		this.ratio=ratio;
		double threshold = 1.0/(double) groupSize;
		double name=0.0;
		
		for(int i=0;i<groupSize;i++){
			Group g= new Group(name);
			groups.put(name, g);
			name=name+threshold;
			name=Math.round(name*10.0)/10.0;
		}

		//ratio
		for(int i=1;i<100;i++){
			candidateValue.put( (double)i, new valueSet((double)i)  );
			
			double k = Math.round((double)1/i*1000.0)/1000.0;
			candidateValue.put( k, new valueSet(k)  );
		}
		
		for(valueSet e:candidateValue.values() ){
			group(e);
		}	
		
	}

	
	
	@Override
	public void run() {

		double preBHR=0;
		double preBIR=0;
		while(true)
		{	
			try {
				Thread.sleep(1);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			
			while(! records.isEmpty()){
				History history = records.remove(0);
				
				eventNum++;				
				valueSet x=candidateValue.get(history.getVarRatio());
				x.setValueBHR(history.getBHR());
				x.setValueBIR(history.getBIR());
				
				if(eventNum==eventCap){
					//Log.printLine("Adopt	Retrieved"+ x);
					eventNum=0;			
					double h=( 2*history.getBHR() + preBHR )/3; 
					double i=( 2*history.getBIR() + preBIR )/3;
					x.setValueBHR(h);
					x.setValueBIR(i);
					preBHR=h;
				    preBIR=i;
					
				    Log.printLine("Adopt	modified"+ x);
					for(Group g: groups.values()){
						if(g.getValueSetList().containsKey(x.getValueRatio())){
							g.getValueSetList().remove(x.getValueRatio());
							g.findRep();
						}
					}			
					group(x);	
					
					//group with largest representative value (BHR)			
					Group selected = null;				
					ArrayList<Group> glist = new ArrayList<Group>();
					for(Group g: groups.values()){
						glist.add(g);
						//System.out.println(g.getName()+" "+g.getRep());
					}					
					Collections.sort(glist, Group.compareByRep);
					selected = glist.get(0);
					
					
					
					//ratio with smallest BIR in the group
					double newRatio;
					if(selected.valueSetWithSmallestBIR()==null){
						newRatio=ratio.getRatio();
					}else
						newRatio=selected.valueSetWithSmallestBIR().getValueRatio();
						
//					Log.printLine("");	
//					Log.printLine("Adopt	group selected:"+selected);	
//					Log.printLine("Adopt	New Ratio:"+newRatio);
//					Log.printLine("Adopt	History inserted: "+history);		
//					Log.printLine("Adopt	Selected Group Members ");
					for(valueSet v:selected.getValueSetList().values()){
						//Log.printLine("     	"+v);
					}
					//Log.printLine(" ");
					
					ratio.setRatio(newRatio);	
					
				
				}
			}
		}
	}
	
	
	public void group(valueSet v){
		double x = v.getValueBHR();
		if(x<=0.1){
			Group g = groups.get(0.0);
			g.addvalue(v);
			Log.printLine("insert at group 0.0");
		}else if(x>0.1 && x<=0.2){
			Group g = groups.get(0.1);
			g.addvalue(v);
			Log.printLine("insert at group 0.1");
		}else if(x>0.2 && x<=0.3){
			Group g = groups.get(0.2);
			g.addvalue(v);
			Log.printLine("insert at group 0.2");
		}else if(x>0.3 && x<=0.4){
			Group g = groups.get(0.3);
			g.addvalue(v);
			Log.printLine("insert at group 0.3");
		}else if(x>0.4 && x<=0.5){
			Group g = groups.get(0.4);
			g.addvalue(v);
			Log.printLine("insert at group 0.4");
		}else if(x>0.5 && x<=0.6){
			Group g = groups.get(0.5);
			g.addvalue(v);
			Log.printLine("insert at group 0.5");
		}else if(x>0.6 && x<=0.7){
			Group g = groups.get(0.6);
			g.addvalue(v);
			Log.printLine("insert at group 0.6");
		}else if(x>0.7 && x<=0.8){
			Group g = groups.get(0.7);
			g.addvalue(v);
			Log.printLine("insert at group 0.7");
		}else if(x>0.8 && x<=0.9){
			Group g = groups.get(0.8);
			g.addvalue(v);
			Log.printLine("insert at group 0.8");
		}else if(x>0.9){
			Group g = groups.get(0.9);
			g.addvalue(v);
			Log.printLine("insert at group 0.9");
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



//	double max=0.0;
//	for(Group g: groups.values()){
//		if(g.getRep()>=max){
//			max=g.getRep();
//			selected = g;
//		}
//	}
}