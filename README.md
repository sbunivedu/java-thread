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