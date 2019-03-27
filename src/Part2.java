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
		
		List<String> attributes = new ArrayList<String>();
		List<String> categoryNames = new ArrayList<String>();
		
		categoryNames.add("alive");
		categoryNames.add("die");
		
		
		attributes.add("AGE");
		attributes.add("FEMALE");
		attributes.add("STEROID");
		attributes.add("ANTIVIRALS");
		attributes.add("FATIGUE");
		attributes.add("MALAISE");
		attributes.add("ANOREXIA");
		attributes.add("BIGLIVER");
		attributes.add("FIRMLIVER");
		attributes.add("SPLEENPALPABLE");
		attributes.add("SPIDERS");
		attributes.add("ASCITES");
		attributes.add("VARICES");
		attributes.add("BILIRUBIN");
		attributes.add("SGOT");
		attributes.add("HISTOLOGY");
		
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
		
		buildTree(instances, attributes);
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
				return new Node("ALIVE", probability, null, null);
			}else {
				return new Node("DEAD", probability, null, null);
			}
		}
		
		if(alive == 0 || dead == 0) {
			if(alive == 0) {
				return new Node("ALIVE", 1, null, null);
			}
			if(dead == 0) {
				return new Node("ALIVE", 1, null, null);
			}
		}
		
		if(attributes.isEmpty()) {
			double probability = Math.min(alive, dead)/Math.max(alive, dead);
			if(alive > dead) {
				return new Node("ALIVE", probability, null, null);
			}else {
				return new Node("DEAD", probability, null, null);
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
				
				double weightedAvgImpurity = weightedAvgImpurity(t, f, instances);
				
				if(weightedAvgImpurity < bestAvgImpurity) {
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
	
	public static double weightedAvgImpurity(List<Instance> t, List<Instance> f, List<Instance> instances) {
		
		
		int liveTrue = 0;
		int dieTrue = 0;
		int liveFalse = 0;
		int dieFalse = 0;
		
		for(Instance i: t) {
			if(i.getCategory() == 0) {
				liveTrue++;
			}else {
				dieTrue++;
			}
		}
		
		for(Instance i: f) {
			if(i.getCategory() == 0) {
				liveFalse++;
			}else {
				dieFalse++;
			}
		}
		
		double trueImpurity = (liveTrue/t.size()) * (dieTrue/t.size());
		double falseImpurity = (liveFalse/f.size()) * (dieFalse/f.size());
		
		double trueBranch = trueImpurity * (liveTrue + dieTrue/instances.size());
		double falseBranch = falseImpurity * (liveFalse + dieFalse/instances.size());
		
		double weightedAvgImpurity = trueBranch + falseBranch;
		
		return weightedAvgImpurity;
		
	}

}
