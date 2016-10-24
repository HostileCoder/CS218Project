package CS218Project;

public class testThread implements Runnable{
	public testThread(){
		new Thread(this, "Answer").start();
	}
	@Override
	public void run() {
		// TODO Auto-generated method stub

		foo();
		
	}
	
	 public synchronized void foo(){
			try {
				wait();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println("hahah");
	 }
	
}