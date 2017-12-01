import java.util.LinkedList;
import java.util.Queue;

/**
 * n producers and n consumers share a non-thread-safe queue
 *
 */
public class ProducerConsumer{
  private static int NUM_MESSAGES = 3;
  private static int NUM_PRODUCERS = 5;
  private static int NUM_CONSUMERS = 5;
  // NUM_MESSAGES must equal NUM_CONSUMERS so that the buffer
  // should be empty in the end

  public static void main(String[] args) throws InterruptedException{
    // create a buffer and share it among all producers and consumers
    Queue<String> buffer = new LinkedList<String>();

    // create and start the producer threads
    Thread[] producers = new Thread[NUM_PRODUCERS];
    for(int i=0; i<NUM_PRODUCERS; i++){
      producers[i] = new Producer("producer"+i, buffer);
      producers[i].start();
    }

    // create and start the consumer threads
    Thread[] consumers = new Thread[NUM_CONSUMERS];
    for(int i=0; i<NUM_CONSUMERS; i++){
      consumers[i] = new Consumer("consumer"+i, buffer);
      consumers[i].start();
    }

    // make this main thread to wait for the producer
    // threads to finish (join the main thread)
    for(int i=0; i<NUM_PRODUCERS; i++){
      producers[i].join();
    }

    // make this main thread to wait for the consumer
    // threads to finish (join the main thread)
    for(int i=0; i<NUM_CONSUMERS; i++){
      consumers[i].join();
    }

    // print what's left in buffer
    System.err.println("messages left in buffer:");
    while(!buffer.isEmpty()){
      System.err.println(buffer.remove());
    }
  }

  public static class Producer extends Thread{
    Queue<String> buffer;

    public Producer(String name, Queue<String> newBuffer){
      super(name);
      buffer = newBuffer;
    }

    public void run(){
      // each thread attempts to create NUM_MESSAGES messages
      // and put them in the shared buffer (queue)
      for (int i=0; i<NUM_MESSAGES; i++){
        String message = "message "+i+" from thread "+getName();
        buffer.add(message);
        System.err.println("sent "+message);
      }
    }
  }

  public static class Consumer extends Thread{
    Queue<String> buffer;

    public Consumer(String name, Queue<String> newBuffer){
      super(name);
      buffer = newBuffer;
    }

    public void run(){
      int count = 0;
      // each consumer thread attempts to retrieve NUM_MESSAGES
      // messages from the shared buffer (queue)
      while (count < NUM_MESSAGES){
        String message;
        if(!buffer.isEmpty()){
          message = buffer.remove();
          System.err.println(getName()+" received "+message);
          count++;
        }
      }
    }
  }
}
