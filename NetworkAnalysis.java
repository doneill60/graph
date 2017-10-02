import java.util.Scanner;
import java.util.Iterator;

public class NetworkAnalysis{
	private String rawInput;
	private static final int INFINITY = Integer.MAX_VALUE;
	private int selection;
	private String garbage;	//used to process extra data thats not needed
	public static void main(String[] args){
		NetworkAnalysis na = new NetworkAnalysis();
		na.runUI(args[0]);
	}
	public void runUI(String file){
		In in = new In(file);
        EdgeWeightedDigraph G = new EdgeWeightedDigraph(in);	
		Scanner input = new Scanner(System.in);
		int toVertex;
		int fromVertex;
		loop: while(true){
			System.out.print("Please select an option from the following: \n" 
			+ "1. Find the lowest latency path\n2. Determine if the graph is copper-only connected\n" + 
			"3. Find the max amount of data that can be transferred between two given vertices\n" 
			+"4. Find the average latency spanning tree for the graph\n5. Determine whether "
			+"the graph would remain connected even if any two vertices fail.\n6. Quit\n\n> ");
			rawInput = input.nextLine();
			selection = Integer.parseInt(rawInput);
			//Finds lowest latency path
			if(selection==1){
				System.out.println("Please enter two vertices, separated by a space.");
				fromVertex = input.nextInt();
				toVertex = input.nextInt();
				garbage = input.nextLine();	//collect new line nextInt fails to handle
				DijkstraSP sp = new DijkstraSP(G, fromVertex);
				System.out.println("Path:");
				int min = INFINITY;
				for(Edge e : sp.pathTo(toVertex)){
					min = Math.min(min, e.bandwidth());
					System.out.println("Edge from " + e.from() +" to " + e.to());
				}
				System.out.println("Minimum of maximum bandwidths along path = " + min + " bit/s");								
			}
			//Determines if a graph is connected through copper only connections
			else if(selection == 2){
				//Gets copy of graph that is strictly copper connected
				EdgeWeightedDigraph GCopy = new EdgeWeightedDigraph
				BreadthFirstPaths bfs = new BreadthFirstPaths(GCopy, G);
				//Calls bfs to visit all reachable vertices0);
				for (int v = 0; v < G.V(); v++){
					//If any vertex is unvisitable 
					if (!bfs.isVisited(v)){
						System.out.println("Not connected through copper only");
						continue loop;
					}
				}
				System.out.println("Connected through copper only connections");
			}
			//Finds the max amount of data that can be transferred from one vertex to another
			else if(selection == 3){
				System.out.println("Please enter two vertices, separated by a space.");
				fromVertex = input.nextInt();
				toVertex = input.nextInt();
				garbage = input.nextLine();	//collects new line
				FordFulkerson ff = new FordFulkerson(G, fromVertex, toVertex);
				System.out.println("Maximum Bandwidth " + ff.value() + " bit/s");
				in = new In(file);
				G = new EdgeWeightedDigraph(in);	//reset graph to reset flows
			}
			//Finds the minimum average latency spanning tree
			else if(selection == 4){
				LazyPrimMST lp = new LazyPrimMST(G);
				System.out.println("0");	//path always starts at 0, call to e.to() only print next vertex
				for(Edge e : lp.edges()){
					System.out.println(e.to());
				}
			}
			//Determines whether a graph would remain connected if any two vertices should fail
			else if(selection == 5){
				EdgeWeightedDigraph F;
				Biconnected bic;
				int j;
				System.out.println("Pairs of vertices that could be eliminated to cause the network to fail: ");
				for(int i = 0; i<G.V(); i++){
					F = new EdgeWeightedDigraph(G, i);
					bic = new Biconnected(F);
					for (int v = 0; v < F.V(); v++){
						j=v;
						if(v>=i)j=v+1;
						if (bic.isArticulation(v)) StdOut.println("vertices " + i + " " + j);
					}
					G.fixEdges(G,i);
				}
			}
			else if(selection == 6){
				break;
			}
			
		}
	}
}