import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

public class Part2 {
	static List<String> attributes = new ArrayList<String>();
	public static void main(String[] args) {

		File trainingFile = new File(args[0]);
		File testFile = new File(args[1]);

		
		List<String> categoryNames = new ArrayList<String>();

		categoryNames.add("live");
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
			din.nextLine();
			din.nextLine();

			String ln;
			while (din.hasNext()) {
				Scanner line = new Scanner(din.nextLine());
				instances.add(new Instance(categoryNames.indexOf(line.next()), line));
			}
			System.out.println("Read " + instances.size() + " instances");

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		List<Instance> instances2 = new ArrayList<Instance>();
		try {
			Scanner din = new Scanner(testFile);
			din.nextLine();
			din.nextLine();
			String ln;
			while (din.hasNext()) {
				Scanner line = new Scanner(din.nextLine());
				Instance i = new Instance(categoryNames.indexOf(line.next()), line);
				instances2.add(i);
				//System.out.println(i.toString(categoryNames));
			}

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		Node root = buildTree(instances, attributes);
		int live = 0;
		int die = 0;
		int liveWrong = 0;
		int dieWrong = 0;
		
		
		for (Instance in : instances2) {
			Node tempRoot = root;
			while (true) {
				
				if (tempRoot instanceof LeafNode) {
					if (in.getCategory() == 0 && tempRoot.className.equals("ALIVE")) {
						live++;
						break;
					}
					
					if (in.getCategory() == 0 && tempRoot.className.equals("DEAD")) {
						liveWrong++;
						break;
					}
					
					if (in.getCategory() == 1 && tempRoot.className.equals("ALIVE")) {
						dieWrong++;
						break;
					}
					
					if (in.getCategory() == 1 && tempRoot.className.equals("DEAD")) {
						die++;
						break;
					}
				}else {
					String att = tempRoot.bestAtt;
					int index = Part2.getBrandNewAtt().indexOf(att);
					if(in.getAtt(index) ==true) {
						tempRoot = tempRoot.left;
					}else {
						tempRoot = tempRoot.right;
					}
				}
			}
		}
		System.out.println("Alive correct: " + live + " Dead correect: " + die + " Alive wrong: " + liveWrong + " Dead wrong: " + dieWrong);
	}

	public static Node buildTree(List<Instance> instances, List<String> attributes) {

		int alive = 0;
		int dead = 0;
		for (Instance in : instances) {
			if (in.getCategory() == 0) {
				alive++;
			} else {
				dead++;
			}
		}
		System.out.println("Alive: " + alive + "Dead: " + dead + "attributes: " + attributes.size());

		if (instances.isEmpty()) {
			System.out.println("here");
			double probability = 0;
			if (Math.min(alive, dead) == 0) {

			} else {
				probability = Math.min(alive, dead) / Math.max(alive, dead);
			}
			if (alive > dead) {
				// System.out.println("here");
				return new LeafNode("ALIVE", probability);
			} else {
				// System.out.println("here");
				return new LeafNode("DEAD", probability);
			}
		}

		if (alive == 0 || dead == 0) {
			if (alive == 0) {
				System.out.println("here");
				return new LeafNode("ALIVE", 1);
			}
			if (dead == 0) {
				//System.out.println("here");
				return new LeafNode("DEAD", 1);
			}
		}

		if (attributes.isEmpty()) {
			double probability = Math.min(alive, dead) / Math.max(alive, dead);
			if (alive > dead) {
				return new LeafNode("ALIVE", probability);
			} else {
				return new LeafNode("DEAD", probability);
			}
		}

		else {
			// double bestPurity = 10000;
			int bestAtt = 0;
			List<Instance> bestInstaTrue = null;
			List<Instance> bestInstaFalse = null;
			System.out.println("here");

			Node left = null;
			Node right = null;

			for (int pos = 0; pos < attributes.size(); pos++) {
				List<Instance> t = new ArrayList<Instance>(); // instances for which the attribute is true
				List<Instance> f = new ArrayList<Instance>(); // instances for which the attribute is false

				double bestAvgImpurity = 10000;

				for (Instance inst : instances) { // if attribute in instance is true
					if (inst.getAtt(pos) == true) {
						t.add(inst);
					} else { // if attribute in instance is false
						f.add(inst);
					}
				}
				double weightedAvgImpurity = weightedAvgImpurity(t, f, instances);

				if (weightedAvgImpurity < bestAvgImpurity) {
					bestAtt = pos;
					bestInstaTrue = t;
					bestInstaFalse = f;
				}
				

			}
			List<String> newAttributes = attributes;
			newAttributes.remove(bestAtt);
			left = buildTree(bestInstaTrue, newAttributes);
			right = buildTree(bestInstaFalse, newAttributes);
			
						
			return new Node(left, right, Part2.getBrandNewAtt().get(bestAtt));
		}

	}
	
	/*
	 * This method is used to compute the weighted average purity when given the true set, false
	 * set and the list of instances
	 */

	public static double weightedAvgImpurity(List<Instance> t, List<Instance> f, List<Instance> instances) {

		int liveTrue = 0;
		int dieTrue = 0;
		int liveFalse = 0;
		int dieFalse = 0;

		for (Instance i : t) {
			if (i.getCategory() == 0) {
				liveTrue++;
			} else {
				dieTrue++;
			}
		}

		for (Instance i : f) {
			if (i.getCategory() == 0) {
				liveFalse++;
			} else {
				dieFalse++;
			}
		}
		
		double trueImpurity = 0;
		double falseImpurity = 0;
		
		if(t.size() == 0) {
			trueImpurity = 0;
		}else {
			trueImpurity = (liveTrue / t.size()) * (dieTrue / t.size());
		}
		if(f.size() == 0) {
			falseImpurity = 0;
		}else {
			falseImpurity = (liveFalse / f.size()) * (dieFalse / f.size());
		}

		double trueBranch = trueImpurity * (liveTrue + dieTrue / instances.size());
		double falseBranch = falseImpurity * (liveFalse + dieFalse / instances.size());

		double weightedAvgImpurity = trueBranch + falseBranch;

		return weightedAvgImpurity;

	}
	
	public static List<String> getBrandNewAtt(){
		ArrayList<String> s = new ArrayList<String>();
		
		s.add("AGE");
		s.add("FEMALE");
		s.add("STEROID");
		s.add("ANTIVIRALS");
		s.add("FATIGUE");
		s.add("MALAISE");
		s.add("ANOREXIA");
		s.add("BIGLIVER");
		s.add("FIRMLIVER");
		s.add("SPLEENPALPABLE");
		s.add("SPIDERS");
		s.add("ASCITES");
		s.add("VARICES");
		s.add("BILIRUBIN");
		s.add("SGOT");
		s.add("HISTOLOGY");
		
		return s;
	}

}
