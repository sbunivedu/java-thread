public class TwoRunnablesTest {
  public static void main(String[] args){
    new Thread(new SimpleRunnable(), "a").start();
    new Thread(new SimpleRunnable(), "b\t").start();
  }
}

class SimpleRunnable implements Runnable{
  public void run(){
    for (int i=0; i<10; i++){
      System.out.println("from thread "+
        Thread.currentThread().getName()+" i="+i);
      try{
        Thread.sleep((long)(Math.random()*1000));
      }catch(InterruptedException e){}
    }
    System.out.println("Done! "+Thread.currentThread().getName());
  }
}
