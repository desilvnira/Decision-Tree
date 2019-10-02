
public class LeafNode extends Node{
	
	String className;
	double prob;

	public LeafNode(String className, double prob) {
		super(className, prob);
		this.className = className;
		this.prob = prob;
		// TODO Auto-generated constructor stub
	}
	
	
	public void report(String indent){
		System.out.format("%sClass %s, prob=%4.2f\n",
		indent, className, prob);
		}

}
