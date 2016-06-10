import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.locks.ReentrantLock;

public class AcessCounter {

	public static AcessCounter acess;
	static HashMap<String,Integer> map = new HashMap<String,Integer>();
	static ReentrantLock aLock;
	static ArrayList<String> files = new ArrayList<String>();
	private String path;
	private int count=0;
	static FileCacheLRU fileCacheLRU;
	static FileCacheLFU fileCacheLFU;

	public AcessCounter(){
		acess = this;
		aLock = new ReentrantLock();
	}
	
	public void increment(String path){
		aLock.lock();
		try{
			if(map.containsKey(path)){
				count = map.get(path);
				map.put(path, count+1);
			}
		else{
			map.put(path, 1);
			count=1;
		}
		}
		finally{
			System.out.println("\nFile: "+path);

			aLock.unlock();
		}
	}
	
	
	public int getCount(String path) {
		aLock.lock();
		try{
			count = map.get(path);
			System.out.println("\nCounter: "+count);
			return count;
		}finally{
			aLock.unlock();
		}
		
	}
	
	public static void main(String[] args){
		acess = new AcessCounter();
		fileCacheLRU = new FileCacheLRU();
		fileCacheLFU = new FileCacheLFU();
		files.add("a.html");
		files.add("b.html");
		files.add("b.html");
		files.add("d.html");
		files.add("d.html");
		files.add("d.html");
		for(int x =0; x<5; x++){
			for( int i = 0; i < 4; i++){
				new Thread(acess.new RequestHandler(files.get(i))).start();
			}
		}			
	}
	
	public class RequestHandler implements Runnable{

		
		private String file;
		

		RequestHandler(String file){
			this.file = file;
		}
		
		@Override
		public void run() {
			aLock.lock();
			try{
				
				fileCacheLRU.fetch(file);
				
				fileCacheLFU.fetch(file);
				
				acess.increment(file);
				
				acess.getCount(file);
				
				
				Thread.sleep(2);
				
						}catch(InterruptedException e){}
		finally{
			aLock.unlock();
		}
		}

		
		
	}
	
	
}
