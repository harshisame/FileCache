import java.util.HashMap;
import java.util.concurrent.locks.ReentrantLock;

public abstract class FileCache {

	//static public int key;
	static FileCache file;
	static int cache = 2;
	static ReentrantLock aLock;
	
	public FileCache(){
		file = this;
		aLock = new ReentrantLock();
	}
	
	public abstract String fetch(String targetfile);
	public abstract String cacheFile(String targetfile);
	
	
}
