package linker;

public class LinkerDriver {

	public static void main(String[] args) {
		long startTime = System.currentTimeMillis();
		Linker linker = new Linker();
		
//		linker.threadedLinker(1);
//		linker.linker(1);
		
		long endTime   = System.currentTimeMillis();
		long totalTime = endTime - startTime;
		System.out.println(totalTime);
	}

}
