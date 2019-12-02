public class TwoRunnablesTest {
  public static void main(String[] args){
    new Thread(new SimpleRunnable(), "a").start();
    new Thread(new SimpleRunnable(), "b\t\t").start();
  }
}

class SimpleRunnable implements Runnable{
  public void run(){
    for (int i=0; i<10; i++){
      System.out.println("from thread "+
        Thread.currentThread().getName()+" i="+i);
    }
    System.out.println("End of "+Thread.currentThread().getName());
  }
}
