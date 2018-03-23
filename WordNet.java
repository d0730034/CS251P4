import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;


public final class WordNet {
	private SAP sap;
	private Map<String, List<Integer>> word_ids;
	private Map<Integer, String> id_words;
	
	// constructor takes the name of the two input files
    public WordNet(String synsets, String hypernyms) {
    	word_ids = new HashMap<String, List<Integer>>();
    	id_words = new HashMap<Integer, String>();
    	Scanner synsets_file = null;
    	Scanner hypernyms_file = null;
    	try {
    		synsets_file = new Scanner(new BufferedReader(new FileReader(synsets)));
    		int V = 0;
    		while (synsets_file.hasNext()) {
    			V++;
    			String line = synsets_file.nextLine();
    			String[] items = line.split(",");
    			int id = Integer.parseInt(items[0]);
    			String[] words = items[1].split(" ");
    			for (int i = 0; i < words.length; i++) {
    				if (word_ids.containsKey(words[i])) {
    					word_ids.get(words[i]).add(id);
    				}
    				else {
    					List<Integer> list = new ArrayList<Integer>();
    					list.add(id);
    					word_ids.put(words[i], list);
    				}
    				id_words.put(id, items[1]);
    			}
    			
    		}
    		Digraph G = new Digraph(V + 1);
    		hypernyms_file = new Scanner(new BufferedReader(new FileReader(hypernyms)));
    		while (hypernyms_file.hasNext()) {
    			String line = hypernyms_file.nextLine();
    			String[] items = line.split(",");
    			int v = Integer.parseInt(items[0]);
    			for (int i = 1; i < items.length; i++) {
    				int w = Integer.parseInt(items[i]);
    				G.addEdge(v, w);
    			}
    		}
    		sap = new SAP(G);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    // is the word a WordNet noun? This can be used to search for existing
    // nouns at the beginning of the printSap method
    public boolean isNoun(String word) {
    	if (word_ids.containsKey(word)) {
    		return true;
    	}
    	else {
    		return false;
    	}
    }
    
    // print the synset (second field of synsets.txt) that is the common ancestor
    // of nounA and nounB in a shortest ancestral path as well as the length of the path,
    // following this format: "sap<space>=<space><number>,<space>ancestor<space>=<space><synsettext>"
    // If no such path exists the sap should contain -1 and ancestor should say "null"
    // This method should use the previously defined SAP datatype
    public void printSap(String nounA, String nounB) {
    	if (!isNoun(nounA) || !isNoun(nounB)) {
    		System.out.println("sap = -1, ancestor = null");
    		return;
    	}
    	List<Integer> vs = word_ids.get(nounA);
    	List<Integer> ws = word_ids.get(nounB);
    	int len = Integer.MAX_VALUE;
    	int ancestor = -1;
    	for (int v : vs) {
    		for (int w : ws) {
    			int l = sap.length(v, w);
    			if (l == -1) {
    				continue;
    			}
    			if (l < len) {
    				len = l;
    				ancestor = sap.ancestor(v, w);
    			}
    		}
    	}
    	
    	if (len == Integer.MAX_VALUE) {
    		System.out.println("sap = -1, ancestor = null");
    		return;
    	}
    	System.out.println("sap = " + len + ", ancestor = " + id_words.get(ancestor));
    }
    
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String synsets = args[0];
		String hypernyms = args[1];
		String input = args[2];
		WordNet wn = new WordNet(synsets, hypernyms);
		Scanner input_file = null;
		try {
			input_file = new Scanner(new BufferedReader(new FileReader(input)));
    		while (input_file.hasNext()) {
    			String[] items = input_file.nextLine().split(" ");
    			wn.printSap(items[0], items[1]);
    		}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
