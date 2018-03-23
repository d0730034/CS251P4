import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class SAP {
    private Digraph G;

	// constructor
    public SAP(Digraph G) {
    	this.G = G;
    } // Use the Digraph implementation provided by the book

    // return length of shortest ancestral path of v and w; -1 if no such path
    public int length(int v, int w) {
    	return solve(v, w, true);
	}

    // return a common ancestor of v and w that participates in a shortest
    // ancestral path; -1 if no such path
    public int ancestor(int v, int w) {
    	return solve(v, w, false);
    }

    private int solve(int v, int w, Boolean flag) {
    	if (v == w) { // the same vertex, return itself
    		return flag?0:v;
    	}

    	Map<Integer, Integer> v_ancestors = new HashMap<Integer, Integer>();
    	v_ancestors.put(v, 0);
    	Map<Integer, Integer> w_ancestors = new HashMap<Integer, Integer>();
    	w_ancestors.put(w, 0);
    	List<Integer> list_v = new LinkedList<Integer>();
    	list_v.add(v);
    	List<Integer> list_w = new LinkedList<Integer>();
    	list_w.add(w);
    	for(int layer = 1;;++layer) { // run BFS
    		if(list_v.isEmpty() && list_w.isEmpty()) {
    			break;
    		}

    		int candidate = -1;
	    	int len = G.E() + 1;

    		int size_v = list_v.size();
    		for(int i = 0; i < size_v; ++i) { // check ancestors of this layer
	    		v = list_v.remove(0);
		    	Iterator<Integer> iter_v = G.adj(v).iterator();
		    	while(iter_v.hasNext()) {
		    		  int v_ancestor = iter_v.next();
		    		  if (v_ancestors.containsKey(v_ancestor)) { // skip visited ancestor
		    			  continue;
		    		  }
		    		  if(w_ancestors.containsKey(v_ancestor)) { // find a common ancestor
		    			  int l = w_ancestors.get(v_ancestor) + layer;
		    			  if (l < len) {
		    				  candidate = v_ancestor;
		    				  len = l;
		    			  }
		    		  }
		    		  else {
		    			  v_ancestors.put(v_ancestor, layer); // record which layer the ancestor belongs to
		    			  list_v.add(v_ancestor);
		    		  }
		    	}
    		}

    		int size_w = list_w.size();
    		for(int i = 0; i < size_w; ++i) {
	    		w = list_w.remove(0);
		    	Iterator<Integer> iter_w = G.adj(w).iterator();
		    	while(iter_w.hasNext()) {
		    		  int w_ancestor = iter_w.next();
		    		  if (w_ancestors.containsKey(w_ancestor)) {
		    			  continue;
		    		  }
		    		  if(v_ancestors.containsKey(w_ancestor)) {
		    			  int l = v_ancestors.get(w_ancestor) + layer;
		    			  if (l < len) {
		    				  candidate = w_ancestor;
		    				  len = l;
		    			  }
		    		  }
		    		  else {
		    			  w_ancestors.put(w_ancestor, layer);
		    			  list_w.add(w_ancestor);
		    		  }
		    	}
    		}

    		if (candidate >= 0) {
    			return flag?len:candidate;
    		}
    	}

    	return -1;
    }

    public static void main(String[] args) {
		// TODO Auto-generated method stub
    	Scanner gfile = null;
    	Scanner infile = null;
    	try {
    		gfile = new Scanner(new BufferedReader(new FileReader(args[0])));
    		Digraph G = new Digraph(gfile.nextInt());
    		int V = gfile.nextInt();
    		for (int i = 0; i < V; i++) {
    			int v = gfile.nextInt();
    			int w = gfile.nextInt();
    			G.addEdge(v, w);
    		}
    		SAP sap = new SAP(G);
    		infile = new Scanner(new BufferedReader(new FileReader(args[1])));
    		while (infile.hasNext()) {
    			int v = infile.nextInt();
    			int w = infile.nextInt();
    			System.out.println("sap = " + sap.length(v, w) + ", ancestor = " + sap.ancestor(v, w));
    		}

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
