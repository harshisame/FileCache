import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.concurrent.locks.ReentrantLock;

public class FileCacheLRU extends FileCache{

	static HashMap<String, Date> map;
	static ReentrantLock aLock;
	private Object minKey;
	private int key=0;
	private Date leastKey;
	
	public FileCacheLRU(){
		file = this;
		aLock = new ReentrantLock();
		map = new HashMap<String, Date>();
	}
	
	public String fetch(String targetFile){
		aLock.lock();
		try{
			if(map.containsKey(targetFile)){
				System.out.println("\n File already in cache: "+targetFile+"\t Key: "+key);
				return targetFile ;
				
			}
		else
			System.out.println("\nFile is Cached: "+targetFile);
			return cacheFile(targetFile);
		}finally{

			aLock.unlock();
		}
		
	}	

	@SuppressWarnings({ "finally", "unchecked" })
	public String cacheFile(String targetFile) {
		aLock.lock();
		try{
			if(map.size() == cache){
				replace(targetFile);
				//return targetFile;
			}
			
			else{
				
				map.put(targetFile, new Date());
				//System.out.println("\nFile is Cached: "+targetFile+"\tKey:"+(int) map.get(targetFile));
				
			}
		}finally{
			aLock.unlock();
			return targetFile;
			
		}
	}	
	
	@SuppressWarnings("unchecked")
	public String replace(String targetFile){
		
		aLock.lock();
		try{
		
			System.out.println("\nReplacement policy used : LRU");
			if(map.size()==cache){
				leastKey = Collections.min(map.values());
				for(String leastUse : map.keySet()){
					if(leastKey==map.get(leastUse)){
						map.remove(leastUse);
						map.put(targetFile, new Date());
						System.out.println("\n***LRU***File is Cached: "+targetFile);
					}
				}
			}
			return null;
			
		}finally{

			aLock.unlock();
		}	
	}	
}
