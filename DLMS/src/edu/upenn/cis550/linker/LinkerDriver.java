package edu.upenn.cis550.linker;

import java.io.File;

import edu.upenn.cis550.storage.StorageAPI;
import edu.upenn.cis550.utils.Constants;

public class LinkerDriver {

	public static void main(String[] args) {
		long startTime = System.currentTimeMillis();
		Linker linker = new Linker();
		
		File storageDir = new File(Constants.PATH_STORAGE);
		StorageAPI store = new StorageAPI(storageDir);
		
//		linker.threadedLinker(1);
		linker.linker(store,1);
		
		store.closeDB();
		
		long endTime   = System.currentTimeMillis();
		long totalTime = endTime - startTime;
		System.out.println(totalTime);
	}
}
