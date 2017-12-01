# Java Thread Examples

The following examples demonstrate how to use Java threads to express parallel
tasks and two ways to make shared/concurrent data structures thread-safe.

## Two threads
This example uses a user-defined thread class - ```SimpleThread```, which
extends the ```Thread``` class. Two instances of the ```SimpleThread``` class
are used to execute two independent tasks (the same code). Each task prints
the current value of ```i``` in a loop.

* Does the standard output look different every time you run the program?
* Explain why the output from the two threads are interleaved.
* Does each thread prints its own lines in the correct order? Why?

## Two runnables
This example is the same as the previous example except that it defines an
independent task using the Runnable interface and then creates Java threads
out of ```SimpleRunnable``` objects.

## Counter
This example creates two threads, which share a Counter object. One thread
increments the count by calling the ```increment()``` method on the shared
object and the other thread decrement the count by calling the
```decrement()```. Note that when the ```join()``` method is invoked on a
thread object it will cause the calling thread to wait for this thread to
finish (or join). In the example it is necessary for the main thread to wait
for the two "Updator" threads to finish in order to print the final value in
the counter object.

* Does the output look correct? Why?
* Explain what may have caused the issue.

## Producer Consumer
This example allows a number of threads to communicate via a shared queue
(linked list) illustrating the classic “Producer Consumer Problem”. There are
two types of threads: producer thread and consumer thread. A producer thread
produces messages and push them into the queue. A consumer thread fetches
messsages by popping the queue. A queue interface is used as the abstraction of
the shared message buffer. Such a queue can be implemented using a circular
array, a linked list, or other data structures.

Note that a consumer thread only attemps to remove a message from the queue if
the queue is not empty because otherwise a NoSuchElementException will be
thrown by the queue. If a consumer thread sees an empty queue, it simply retries
later (busy wait). In the end, when all threads finishes, the main thread print
the messages left in the queue.

If the program works correctly there should be NO leftover messages in the queue.
Run this program to see what you can get.

* How many total messages are sent and received in the program?
* Run this program at least 10 times. Does the output seem correct?

If you see any leftover messages in the queue? If you don’t, try doubling the
number of producers and the number of consumers (lines 10 and 11) to increase
the contention level. Any leftover message in the queue shows the program is
incorrect. Sometimes you will see ```NoSuchElementException``` or
```NullPointerException``` in the output. What do you think could cause
such exceptions?

Hints: a consumer thread can only remove message successfully when the queue is
not empty (line 55); is it possible for two consumer threads to see the same
last item in the queue (see the queue is not empty) and proceeds to try to
remove the same element? Can both threads remove a message successfully?

## Producer Consumer with Monitor
The previous program has race conditions because of the concurrent access to
a shared queue from multiple threads. The ```LinkedList``` API specification
warns us about it: ["Note that this implementation is not synchronized."](
http://docs.oracle.com/javase/7/docs/api/java/util/LinkedList.html)

Additionally, consumers wait on the empty queue in a tight loop is not
desirable as it wastes CPU time. In this example we will use Java monitors to
synchronize the access to the shared buffer from multiple concurrent threads.
Recall that a monitor in Java is an object with ```synchronized``` methods.
A monitor ensures only one thread can by executing/in any of the synchronized
methods at any given time. We can use a monitor to achieve mutual exclusion
because each thread trying to execute synchronized code must compete for the
*lock* associated with the monitor.

After a thread obtains a lock it may not be able to proceed, e.g. a consumer
cannot consume till there are things to beconsumed. This is a synchronization
problem. While in synchronized code a thread can call ```wait()```, which
releases the lock and cause the thread to sleep on the monitor
(object with synchronized code). To wake a sleeping thread on a monitor, another
thread needs to call ```notify()``` in synchronized code of the same monitor.
While ```notify()``` awakens one sleeping thread, ```notifyAll()``` awakens all
sleeping threads on the monitor. Either way the newly awakened threads will
compete for the lock when they attempt to execute synchronized code.

A new ```SafeBuffer``` class is used to wrap around and protect the
```LinkedList``` object that implements the shared buffer.
By adding the “synchronized” keyword in the signatures of both ```add()```
and ```remove()``` methods, we make each ```SafeBuffer``` object a monitor.

Run this program several times and increase the number of producers and
consumers if you want.

* Do you see any leftover messages in the end?

The wait() method causes the call thread to sleep on the monitor object, which
has the synchronized method. Before the thread goes to sleep it release the
lock to the monitor and once the thread is awakened it will compete for the
lock because it will attempt to execute the next instruction
(after the wait() call), which is defined in a synchronized method.

* Why do you think the wait() method is called in a while loop?

Note that the produce will call ```notifyAll()``` after adding a message to
wake up all waiting consumers. What if two consumers try to consume the only
message in the queue? Please use some concrete scenarios to prove the
correctness of this solution.
