public class Counter {
  private int c = 0;

  public int value() {
    return c;
  }

  public void increment() {
    c++;
  }

  public void decrement() {
    c--;
  }

  public static void main(String[] args) throws InterruptedException {
    Counter c = new Counter();
    Updator up = new Updator(c, true);
    Updator down = new Updator(c, false);
    up.start();
    down.start();
    up.join();
    down.join();
    System.err.println("count=" + c.value());
  }
}

class Updator extends Thread {
  private Counter counter;
  private boolean up;

  public Updator(Counter c, boolean isUp) {
    counter = c;
    up = isUp;
  }

  public void run() {
    for (int i = 0; i < 800; i++) {
      if (up) {
        counter.increment();
      } else {
        counter.decrement();
      }
/*
      try{
        Thread.sleep((long)(Math.random()));
      }catch(InterruptedException e){}
*/
    }
  }
}
