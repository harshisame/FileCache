import java.util.Collections;
import java.util.HashMap;
import java.util.concurrent.locks.ReentrantLock;

public class FileCacheLFU extends FileCache{

	static HashMap<String, Integer> map;
	static ReentrantLock aLock;
	private Object minKey;
	private int key=0;
	
	public FileCacheLFU(){
		file = this;
		aLock = new ReentrantLock();
		map = new HashMap<String, Integer>();
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
				
				if(map.get(targetFile)!=null)
				{
					key =(int) map.get(targetFile);
				
				}
				else
				{
					key=0;
				}
				
				map.put(targetFile, key+1);
				System.out.println("\nFile is Cached: "+targetFile+"\tKey:"+(int) map.get(targetFile));
				
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
		
			System.out.println("\nReplacement policy used : LFU");
			if(map.size()==cache){
				minKey = Collections.min(map.values());
				for(String leastFrequency : map.keySet()){
					if(minKey==map.get(leastFrequency)){
						map.remove(leastFrequency);
						map.put(targetFile, 1);
						System.out.println("\n***LFU***File is Cached: "+targetFile);
					}
				}
			}
			return null;
			
		}finally{

			aLock.unlock();
		}	
	}	
}
