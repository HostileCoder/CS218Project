package CS218Project;


import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.cloudbus.cloudsim.Log;



class AdaptorAlgorithmEXO implements Runnable{
	//private History history = null;
	private int eventNum=0;
	private int eventCap=5;
	private int groupSize=0;
	private ArrayList <History> records = new ArrayList<History>();
	private Ratio ratio=null;
	private TreeMap<Double, Group> groups = new TreeMap<Double, Group>();
	private Map<Double,valueSet> candidateValue = new  TreeMap<Double,valueSet>();
	private double preBHR=0;
	private double preBIR=0;
	
	public AdaptorAlgorithmEXO(int groupSize, ArrayList <History> records, Ratio ratio){
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

		double a=0.0;
		for(int i=0;i<1000;i++){
			valueSet v=new valueSet(a);
			candidateValue.put( a, v );			
			a=0.003+a;
			a=Math.round(a*1000.0)/1000.0;
			group(v);
		}
		

	}

	
	
	@Override
	public void run() {

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
								
				valueSet x=null;	
				x=candidateValue.get(history.getVarRatio());
				x.setValueBHR(history.getBHR());
				x.setValueBIR(history.getBIR());
				
				if(eventNum==eventCap){
					//Log.printLine("Adopt	Retrieved"+ x);
					eventNum=0;			
					double h=( history.getBHR() + 2*preBHR )/3; 
					double i=( history.getBIR() + 2*preBIR )/3;
					x.setValueBHR(h);
					x.setValueBIR(i);
					preBHR=h;
				    preBIR=i;
					
					//System.out.println("Adopt	modified"+ x);
					for(Group g: groups.values()){
						if(g.getValueSetList().containsKey(x.getValueRatio())){
							valueSet v = g.getValueSetList().remove(x.getValueRatio());
							g.findRep();
							System.out.println("Adopt	:"+v.getValueRatio()+" remove from "+g.getName());	
						}
					}			
					group(x);	
					
					//group with largest representative value (BHR)								
					Group selected = null;						
					double max=0.0;
					for(Group g: groups.values()){
						//System.out.println(g);
						if(g.getRep()>=max){
							max=g.getRep();
							selected = g;
						}
					}

					
					//ratio with smallest BIR in the group
					double newRatio;
					if(selected.valueSetWithSmallestBIR()==null){
						newRatio=ratio.getRatio();
					}else
						newRatio=selected.valueSetWithSmallestBIR().getValueRatio();
						
					System.out.println("");	
					System.out.println("Adopt	group selected:"+selected);	
					System.out.println("Adopt	New Ratio:"+newRatio);
					System.out.println("Adopt	History inserted: "+history);		
					System.out.println(" ");
					
//					System.out.println("Adopt	Selected Group Members ");
//					for(valueSet v:selected.getValueSetList().values()){
//						System.out.println("     	"+v);
//					}
					
					
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
			return;
		}
		
		for(Group g : groups.values()){
			if(x-g.getName()<0.1){
				g.addvalue(v);
				Log.printLine("insert at group "+g.getName());
				return;
			}
		}
		
	}
	
	
//	Group selected = null;				
//	ArrayList<Group> glist = new ArrayList<Group>();
//	for(Group g: groups.values()){
//		glist.add(g);
//	}					
//	Collections.sort(glist, Group.compareByRep);
//	selected = glist.get(0);

}