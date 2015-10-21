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

public class Threads304v2 {
    // Account is an inner class
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
            
        System.out.println("What is the total?" + total.getBalance());
           
    } // end of main
    
    // A thread for adding one to the total
    // This thread is spauned 100 times
    // It has to be runnable

    private static class AddOneTask implements Runnable{
        // Runnable is an interface, and so its run method is abstract
        // eg you have to override the run method
        // It only has a run method
        // A Java interface helps implement ploymorphism
        // Because AddOneTask implements Runnable then thread knows what
        // to do with it
        @Override
        public void run(){
            total.add(1);
        }   // end of the override  
    }   // end of AddOneTask
    
    // Now define an inner class for Total
    private static class Total {
        private int totalAmount = 0;
        
        public int getTotal(){
            return totalAmount;
        } // end of getTotal
        
        public void add(int amount){
            int newTotalAmount = totalAmount + amount;
        
            // Now add a delay to illustrate the data corruption problem
            try  {
            // 5 is 5 milliseconds
            Thread.sleep(5);
            }
            catch (InterruptedException ex){
            // Do  nothing
            }   // end of catch
            
            totalAmount = newTotalAmount;
        }
        
        
    }   // end of the Total class
    
    
    
}      // end of class Threads304v2
