/******************************************************************************
 *  Compilation:  javac EdgeWeightedDigraph.java
 *  Execution:    java EdgeWeightedDigraph digraph.txt
 *  Dependencies: Bag.java Edge.java
 *  Data files:   http://algs4.cs.princeton.edu/44st/tinyEWD.txt
 *                http://algs4.cs.princeton.edu/44st/mediumEWD.txt
 *                http://algs4.cs.princeton.edu/44st/largeEWD.txt
 *
 *  An edge-weighted digraph, implemented using adjacency lists.
 *
 ******************************************************************************/
import java.util.NoSuchElementException;
import java.util.Iterator;
/**
 *  The {@code EdgeWeightedDigraph} class represents a edge-weighted
 *  digraph of vertices named 0 through <em>V</em> - 1, where each
 *  directed edge is of type {@link Edge} and has a real-valued weight.
 *  It supports the following two primary operations: add a directed edge
 *  to the digraph and iterate over all of edges incident from a given vertex.
 *  It also provides
 *  methods for returning the number of vertices <em>V</em> and the number
 *  of edges <em>E</em>. Parallel edges and self-loops are permitted.
 *  <p>
 *  This implementation uses an adjacency-lists representation, which 
 *  is a vertex-indexed array of {@link Bag} objects.
 *  All operations take constant time (in the worst case) except
 *  iterating over the edges incident from a given vertex, which takes
 *  time proportional to the number of such edges.
 *  <p>
 *  For additional documentation,
 *  see <a href="http://algs4.cs.princeton.edu/44sp">Section 4.4</a> of
 *  <i>Algorithms, 4th Edition</i> by Robert Sedgewick and Kevin Wayne.
 *
 *  @author Robert Sedgewick
 *  @author Kevin Wayne
 */
public class EdgeWeightedDigraph {
    private static final String NEWLINE = System.getProperty("line.separator");

    private int V;                // number of vertices in this digraph
    private int E;                      // number of edges in this digraph
    private Bag<Edge>[] adj;    // adj[v] = adjacency list for vertex v
    
    /**
     * Initializes an empty edge-weighted digraph with {@code V} vertices and 0 edges.
     *
     * @param  V the number of vertices
     * @throws IllegalArgumentException if {@code V < 0}
     */
    public EdgeWeightedDigraph(int V) {
        if (V < 0) throw new IllegalArgumentException("Number of vertices in a Digraph must be nonnegative");
        this.V = V;
        this.E = 0;
		@SuppressWarnings({"unchecked", "rawtypes"})
		Bag<Edge>[] temp = (Bag<Edge>[]) new Bag[V];
		adj = temp;
        for (int v = 0; v < V; v++)
            adj[v] = new Bag<Edge>();
    }

    /**  
     * Initializes an edge-weighted digraph from the specified input stream.
     * The format is the number of vertices <em>V</em>,
     * followed by the number of edges <em>E</em>,
     * followed by <em>E</em> pairs of vertices and edge weights,
     * with each entry separated by whitespace.
     *
     * @param  in the input stream
     * @throws IllegalArgumentException if the endpoints of any edge are not in prescribed range
     * @throws IllegalArgumentException if the number of vertices or edges is negative
     */
    public EdgeWeightedDigraph(In in) {
		try {
            this.V = in.readInt();
            if (V < 0) throw new IllegalArgumentException("number of vertices in a Graph must be nonnegative");
			@SuppressWarnings({"unchecked", "rawtypes"})
			Bag<Edge>[] temp = (Bag<Edge>[]) new Bag[V];
            adj = temp;
            for (int v = 0; v < V; v++) {
                adj[v] = new Bag<Edge>();
            }
            while(true){
                int v = in.readInt();
                int w = in.readInt();
				String x = in.readString();
				int y = in.readInt();
				int z = in.readInt();
                validateVertex(v);
                validateVertex(w);
				Edge e = new Edge(v, w, x, y, z);
				Edge f = new Edge(w, v, x, y, z);
                addEdges(e,f); 
				if(!in.hasNextLine())
					break;
            }
        }
        catch (NoSuchElementException e) {
            throw new IllegalArgumentException("invalid input format in Graph constructor", e);
        }
    }
	/*	Constructor that copys an existing graph to a new graph with only copper connections
	*	Parameters: Original graph
	*
	*/
	public EdgeWeightedDigraph(EdgeWeightedDigraph G) {
        this(G.V());
        this.E = G.E();
        for (int v = 0; v < G.V(); v++) {
            for (Edge e : G.adj[v]) {
				if(e.type().equals("copper")) adj[v].add(e);
				else E--;
                
            }
        }
    }
	/*  Deletes one vertex in the graph and assigns new indices in the process
	*	Parameters: Graph and vertex to delete
	*
	*/
	public EdgeWeightedDigraph(EdgeWeightedDigraph G, int w){
		this(G.V()-1);
		int k = 0;
        for (int v = 0; v < G.V(); v++) {
			if(v==w){
				if(w==G.V()-1) break;
				//decrement = true;
				v++;
			}	
			for (Edge e : G.adj[v]) {
				if(e.to() == w){
					e.setFrom(-1);
					continue;
				}
				if(e.to() > w){
					e.setTo(e.to()-1);
				}
				if(e.from() > w){
					e.setFrom(e.from()-1);
				}
				adj[k].add(e);
            }
			k++;
        }
	}
	
	
	/*	Method that helps with the task of finding two vertices that if removed would make the graph unconnected.
	*	Reverses the work done in deleting
	*	Method fixes indices so vertices make sense
	*	Parameters: Graph and vertex removed
	*
	*/
	public void fixEdges(EdgeWeightedDigraph G, int w){
		for (int v = 0; v < V; v++) {
			if(v==w){
				if(w==G.V()-1) break;
				//decrement = true;
				v++;
			}
			for (Edge e : adj[v]) {
				if(e.to() > w){
					e.setTo(e.to()+1);
				}
				if(e.to() == w){
					if(e.from() != -1){
						e.setTo(e.to()+1);
					}
				}
				e.setFrom(v);
			}
		}
	}

    /**
     * Returns the number of vertices in this edge-weighted digraph.
     *
     * @return the number of vertices in this edge-weighted digraph
     */
    public int V() {
        return V;
    }

    /**
     * Returns the number of edges in this edge-weighted digraph.
     *
     * @return the number of edges in this edge-weighted digraph
     */
    public int E() {
        return E;
    }

    // throw an IllegalArgumentException unless {@code 0 <= v < V}
    private void validateVertex(int v) {
        if (v < 0 || v >= V)
            throw new IllegalArgumentException("vertex " + v + " is not between 0 and " + (V-1));
    }
	public void addEdges(Edge e, Edge f) {
        validateVertex(e.from());
        validateVertex(e.to());
        E++;
		//each edge gets added twice in the adjacency list implementation
        adj[e.from()].add(e);
        adj[e.to()].add(f);
    }

    /**
     * Returns the directed edges incident from vertex {@code v}.
     *
     * @param  v the vertex
     * @return the directed edges incident from vertex {@code v} as an Iterable
     * @throws IllegalArgumentException unless {@code 0 <= v < V}
     */
    public Iterable<Edge> adj(int v) {
        validateVertex(v);
        return adj[v];
    }

    /**
     * Returns the number of directed edges incident from vertex {@code v}.
     * This is known as the <em>outdegree</em> of vertex {@code v}.
     *
     * @param  v the vertex
     * @return the outdegree of vertex {@code v}
     * @throws IllegalArgumentException unless {@code 0 <= v < V}
     */
    public int outdegree(int v) {
        validateVertex(v);
        return adj[v].size();
    }

    /**
     * Returns all directed edges in this edge-weighted digraph.
     * To iterate over the edges in this edge-weighted digraph, use foreach notation:
     * {@code for (Edge e : G.edges())}.
     *
     * @return all edges in this edge-weighted digraph, as an iterable
     */
    public Iterable<Edge> edges() {
        Bag<Edge> list = new Bag<Edge>();
        for (int v = 0; v < V; v++) {
            for (Edge e : adj(v)) {
                list.add(e);
            }
        }
        return list;
    } 

    /**
     * Returns a string representation of this edge-weighted digraph.
     *
     * @return the number of vertices <em>V</em>, followed by the number of edges <em>E</em>,
     *         followed by the <em>V</em> adjacency lists of edges
     */
    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append(V + " " + E + NEWLINE);
        for (int v = 0; v < V; v++) {
            s.append(v + ": ");
            for (Edge e : adj[v]) {
                s.append(e + "  ");
            }
            s.append(NEWLINE);
        }
        return s.toString();
    }

    /**
     * Unit tests the {@code EdgeWeightedDigraph} data type.
     *
     * @param args the command-line arguments
     */
    public static void main(String[] args) {
        In in = new In(args[0]);
        EdgeWeightedDigraph G = new EdgeWeightedDigraph(in);
        StdOut.println(G);
    }

}