/******************************************************************************
 *  Compilation:  javac Biconnected.java
 *  Execution:    java Biconnected V E
 *  Dependencies: Graph.java GraphGenerator.java
 *
 *  Identify articulation points and print them out.
 *  This can be used to decompose a graph into biconnected components.
 *  Runs in O(E + V) time.
 *
 *  http://www.cs.brown.edu/courses/cs016/book/slides/Connectivity2x2.pdf
 *
 ******************************************************************************/
import java.util.Iterator;

public class Biconnected {
    private int[] low;
    private int[] pre;
    private int cnt;
    private boolean[] articulation;
	
	/*
	public findTwoVertices(EdgeWeightedDigraph G){
		for (int v = 0; v < G.V(); v++){
			G.remove(v)
			for(int w = 0; w<G.V(); w++){
				G.remove(w);
				biconnected(G);
			}
		}
		
		
		
	}
	*/
    public Biconnected(EdgeWeightedDigraph G) {
		//lowest vertex you can reach following 0 or more spanning tree edges and at most one back edge
        low = new int[G.V()]; 
        pre = new int[G.V()];
        articulation = new boolean[G.V()];
        for (int v = 0; v < G.V(); v++)
            low[v] = -1;
        for (int v = 0; v < G.V(); v++)
            pre[v] = -1;
        
        for (int v = 0; v < G.V(); v++)
            if (pre[v] == -1)
                dfs(G, v, v);
    }

    private void dfs(EdgeWeightedDigraph G, int u, int v) {
        int children = 0;
        pre[v] = cnt++;
        low[v] = pre[v];
		Iterator<Edge> i = G.adj(v).iterator();
        for (int w = 0; w<G.outdegree(v); w++) {
			Edge e = i.next();
			int currentVertex = e.to();
            if (pre[currentVertex] == -1) {
                children++;
                dfs(G, v, currentVertex);

                // update low number
                low[v] = Math.min(low[v], low[currentVertex]);

                // non-root of DFS is an articulation point if low[w] >= pre[v]
                if (low[currentVertex] >= pre[v] && u != v) 
                    articulation[v] = true;
            }

            // update low number - ignore reverse of edge leading to v
            else if (currentVertex != u)
                low[v] = Math.min(low[v], pre[currentVertex]);
        }

        // root of DFS is an articulation point if it has more than 1 child
        if (u == v && children > 1)
            articulation[v] = true;
    }

    // is vertex v an articulation point?
    public boolean isArticulation(int v) { return articulation[v]; }
	public boolean isVisited(int v) { return pre[v] != -1; }

    /* test client
    public static void main(String[] args) {
        int V = Integer.parseInt(args[0]);
        int E = Integer.parseInt(args[1]);
        Graph G = GraphGenerator.simple(V, E);
        StdOut.println(G);

        Biconnected bic = new Biconnected(G);

        // print out articulation points
        StdOut.println();
        StdOut.println("Articulation points");
        StdOut.println("-------------------");
        for (int v = 0; v < G.V(); v++)
            if (bic.isArticulation(v)) StdOut.println(v);
    }*/


}