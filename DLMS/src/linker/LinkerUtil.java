package linker;

import java.util.HashSet;

public class LinkerUtil {
	static synchronized void addToDB(long threadID, int nodeID, HashSet<Integer> linkedNodes) {
		System.out.println("THREAD ("+threadID+") : "+nodeID+","+linkedNodes.size());
	}
}
