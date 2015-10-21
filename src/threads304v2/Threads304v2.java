/*
 * This is exercise 30.4 on page 1135
 * Synchronized Threads
 * Launch 100 threads
 * Each thread adds 1 to the variable sum that is initially zero
 * Define an Integer wrapper to hold sum
 * Run with and without sync.
 */
package threads304v2;

/**
 *
 * @author dcros
 */

import java.util.concurrent.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Threads304v2 {
    // Account is an inner class
    
    // We now create a new static object that is used by all
    // the threads
    private static Total total = new Total();
    
    public static void main(String[] args) {
        int nThread = 100;
        // Create an executor and a cached thread pool
        // Executor executes tasks in a thread pool
        // We have set 100 threads 
        // ExecutiveService manages threads
        // Executors executes threads
        
        ExecutorService executor = Executors.newFixedThreadPool(nThread);
        
        //Create and launch 100 theads
        for(int i=0; i <nThread; i++ ){
            // Now we execute new threads
            executor.execute(new AddOneTask());
        }   // end of for loop
        
        // Now we stop anymore threads starting but let the current
        // threads complete
        executor.shutdown();
            
        // Now we wait until all the tasks are finished
        // This is never true unless executor.shutdown has been called
        // and all thread terminated
        while (!executor.isTerminated()) {
            // Apparently do nothing until all the threads are finished
        }
            
        System.out.println("What is the total?" + total.getTotal());
           
    } // end of main
    
    // A thread for adding one to the total
    // This thread is spauned 100 times
    // It has to be runnable
    
    // A thread can enter a synchronized method ONLY if the thread
    // can get a key to that object's lock.
    // Every Java object has a lock with ONE key - but most
    // of the time they are unlocked and no one cares
    // So when the thread find a method that is synchronized then
    // it looks for the object that is specified in the synchronized
    // expression and only preceeds if it can grab it.
    // Obviously no other thread can then grab it
    // so they wait until the key is returned when the method is complete

    private static class AddOneTask implements Runnable{
        // Runnable is an interface, and so its run method is abstract
        // eg you have to override the run method
        // It only has a run method
        // A Java interface helps implement ploymorphism
        // Because AddOneTask implements Runnable then thread knows what
        // to do with it
        @Override
        public void run(){
            // Grabs the key for the total object and then executes the add
            // does not return it until this method completes
            // remember this method has the delay in  it
            // synchronized (total){
            //    total.add(1);
            //}
            
            total.add(1);   // we remove the sync
            
        }   // end of the override  
    }   // end of AddOneTask
    
    // Now define an inner class for Total
    private static class Total {
        // We create a lock
        // It is reentrant - this means that you can interrupt the 
        // reentrant code and then re-enter it again at a later date
        // and get the same answer
        // A reentrant lock is mutually exclusive
        private static Lock lock = new ReentrantLock();
        
        private int totalAmount = 0;
        
        public int getTotal(){
            return totalAmount;
        } // end of getTotal
        
        public void add(int amount){
            // We acquire a lock - we wait if we can not get it
            lock.lock();

            // We put everything inside a try-catch so we ensure lock release

            try  {
                int newTotalAmount = totalAmount + amount;
            // Now add a delay to illustrate the data corruption problem
            // 5 is 5 milliseconds
                Thread.sleep(5);
                
                totalAmount = newTotalAmount;
            }
            catch (InterruptedException ex){
            // Do  nothing
            }   // end of catch
            finally {
                // release the lock whatever happens!
                lock.unlock();
            }
            
        }   // end of the add class    
    }   // end of the Total class 
}      // end of class Threads304v2
