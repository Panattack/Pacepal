public class ThreadDemo extends Thread {
 
    private Thread thread;
    private String name;
    PrintDemo printDemo;
   
    ThreadDemo( String name,  PrintDemo printDemo){
          this.name = name;
          this.printDemo = printDemo;
    }
   
    @Override
    public void run() {
      /*We use the keyword synchronized over the object printDemo
      * because printDemo is a shared object and we want to access
      * it atomically, ie. one thread only accesses the shared
      * object at a time, thus, avoiding interleaving threads
      * which may lead to inconsistent state of the shared object,
      * data structure etc
      */
      //synchronized(printDemo){
        printDemo.printCount();
      //}
      System.out.println("Thread " + name + " exiting.");
    }
   
    @Override
    public void start ()
    {
      /*Just a showcase that we can also overload the start
      * method, otherwise use 
      * "AThreadObjectYouCreated.start()"
      */
      System.out.println("Starting " + name);
      if (thread == null)
      {
        thread = new Thread (this, name);
        thread.start ();
      }
    }
  }
  