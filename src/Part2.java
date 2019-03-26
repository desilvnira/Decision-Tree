import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

public class Part2 {

	public static void main(String[] args) {
		
		File trainingFile = new File(args[0]);
		File testFile = new File(args[1]);
		
		List<String> categoryNames = new ArrayList<String>();
		
		
		categoryNames.add("ALIVE");
		categoryNames.add("AGE");
		categoryNames.add("FEMALE");
		categoryNames.add("STEROID");
		categoryNames.add("ANTIVIRALS");
		categoryNames.add("FATIGUE");
		categoryNames.add("MALAISE");
		categoryNames.add("ANOREXIA");
		categoryNames.add("BIGLIVER");
		categoryNames.add("FIRMLIVER");
		categoryNames.add("SPLEENPALPABLE");
		categoryNames.add("SPIDERS");
		categoryNames.add("ASCITES");
		categoryNames.add("VARICES");
		categoryNames.add("BILIRUBIN");
		categoryNames.add("SGOT");
		categoryNames.add("HISTOLOGY");
		
		List<Instance> instances = new ArrayList<Instance>();
		try {
			Scanner din = new Scanner(trainingFile);
			 
			    String ln;
			    while (din.hasNext()){ 
			      Scanner line = new Scanner(din.nextLine());
			      instances.add(new Instance(categoryNames.indexOf(line.next()),line));
			    }
			    System.out.println("Read " + instances.size()+" instances");
			    
			  
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		buildTree(instances, categoryNames);
	}

	public static Node buildTree(List<Instance> instances, List<String> attributes) {

		int alive = 0;
		int dead = 0;
		
		if (instances.isEmpty()) {
			
			for(Instance in: instances) {
				if(in.getAtt(0)) {
					alive++;
				}
				else {
					dead++;
				}
			}
			double probability = Math.min(alive, dead)/Math.max(alive, dead);
			if(alive > dead) {
				return new Node("ALIVE", probability);
			}else {
				return new Node("DEAD", probability);
			}
		}
		
		if(alive == 0 || dead == 0) {
			if(alive == 0) {
				return new Node("ALIVE", 1);
			}
			if(dead == 0) {
				return new Node("ALIVE", 1);
			}
		}
		
		if(attributes.isEmpty()) {
			double probability = Math.min(alive, dead)/Math.max(alive, dead);
			if(alive > dead) {
				return new Node("ALIVE", probability);
			}else {
				return new Node("DEAD", probability);
			}
		}

		else {
			//double bestPurity = 10000;
			int bestAtt = 0;
			List<Instance> bestInstaTrue = null;
			List<Instance> bestInstaFalse = null;
			
			Node left;
			Node right;
			
			for (int pos = 0; pos < attributes.size(); pos++) {
				List<Instance> t = new ArrayList<Instance>(); // instances for which the attribute is true
				List<Instance> f = new ArrayList<Instance>(); // instances for which the attribute is false
				
				double bestAvgImpurity = 10000;
				
				for (Instance inst : instances) { // if attribute in instance is true
					if (inst.getAtt(pos)) {
						t.add(inst);
					}else {						// if attribute in instance is false
						f.add(inst);
					}
				}
				
				double avgImpurity = (t.size() + f.size())/2; // calculates the avgImpurity of the current 2 sets
				if(avgImpurity < bestAvgImpurity) {
					bestAtt = pos;
					bestInstaTrue = t;
					bestInstaFalse = f;
				}
				List<String> newAttributes = new ArrayList<String>();
				newAttributes.remove(bestAtt);
				left = buildTree(bestInstaTrue, newAttributes);
				right = buildTree(bestInstaFalse, newAttributes);
				
				
			}
			
		}
		
		return null;

	}

}
