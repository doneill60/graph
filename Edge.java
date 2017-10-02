public class Edge implements Comparable<Edge>{
	private int from;
	private int to;
	private String type;
	private int bandwidth;
	private int flow;
	private int length;
	private double weight;
	private int speed;
	Edge(int from, int to, String type, int bandwidth, int length){
		this.from = from;
		this.to = to;
		this.type = type;
		this.bandwidth = bandwidth;
		this.length = length;
		if(type.equals("copper")){
			speed = 230000000;
		}
		else{
			speed = 200000000;
		}
		this.weight = (double)(length)/(double)(speed);
		this.flow = 0;
	}
	public Edge(Edge e) {
        this.from = e.from;
		this.to = e.to;
		this.type = e.type;
		this.bandwidth = e.bandwidth;
		this.length = e.length;
		this.weight =  e.weight;
		this.flow = 0;
    }
	public int from(){
		return from;
	}
	public int to(){
		return to;
	}
	public void setTo(int w){
		to = w;	
	}
	public void setFrom(int w){
		from = w;	
	}
	public double weight(){
		return weight;
	}
	public String type(){
		return type;
	}
	public int bandwidth(){
		return bandwidth;
	}
	public int getLength(){
		return length;
	}
	public String toString() {
		return String.format("%d to %d at weight %.9f  with flow %d", from, to, weight, flow);
    }
	public int flow() {
        return flow;
    }
	public void addResidualFlowTo(int vertex, int delta) {
        if (!(delta >= 0)) throw new IllegalArgumentException("Delta must be nonnegative");
        if      (vertex == from) flow -= delta;           // backward edge
        else if (vertex == to) flow += delta;           // forward edge
        else throw new IllegalArgumentException("invalid endpoint");
        if (!(flow >= 0))      throw new IllegalArgumentException("Flow is negative");
        if (!(flow <= bandwidth)) throw new IllegalArgumentException("Flow exceeds capacity");
    }
	public int residualCapacityTo(int vertex) {
        if      (vertex == from) return flow;              // backward edge
        else if (vertex == to) return bandwidth - flow;   // forward edge
        else throw new IllegalArgumentException("invalid endpoint");
    }
	public int other(int vertex) {
        if      (vertex == to) return to;
        else if (vertex == from) return from;
        else throw new IllegalArgumentException("invalid endpoint");
    }
	public int compareTo(Edge e){
		if(this.weight<e.weight) return -1;
		else if(this.weight>e.weight) return 1;
		return 0;
	}
}