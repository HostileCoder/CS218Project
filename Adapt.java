package CS218Project;

import java.util.ArrayList;

public class Adapt {
	
	private ArrayList<Group> gs=new ArrayList<Group>();
	
	
	public void Grouping(Member m){
		for(Group g:gs){
			if(g.low<m.getBHR() && m.getBHR()<g.high){
				
			}
		}
		
	}

}
