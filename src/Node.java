
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

}
