
public class Node {
	
	String className;
	double prob;
	Node left;
	Node right;
	String bestAtt;
	
	public Node(Node left, Node right, String bestAtt) {
		
		this.left = left;
		this.right = right;
		this.bestAtt = bestAtt;
		
	}

	public Node(String className, double prob) {
		// TODO Auto-generated constructor stub
		this.className = className;
		this.prob = prob;
	}
	
	public void report(String indent){
		System.out.format("%s%s = True:\n",
		indent, bestAtt);
		left.report(indent+"    ");
		System.out.format("%s%s = False:\n",
		indent, bestAtt);
		right.report(indent+"    ");
		}

}
