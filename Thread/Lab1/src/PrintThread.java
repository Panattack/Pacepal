//THIS SHOULD BE IN a file PrintThread.java
class PrintThread extends Thread {
     
    private int sleepTime;
 
    // PrintThread constructor assigns name to thread
    // by calling Thread constructor
    public PrintThread(String name) {
        super(name);
        // sleep between 0 and 5 seconds
        sleepTime = (int) (Math.random() * 5000);
        System.err.println("Name: " + getName() +
                ".  sleep: " + sleepTime);
    }
 
    // execute the thread
    @Override
    public void run() {
    // put thread to sleep for a random interval
        try {
            System.err.println(getName() + " going to sleep");
            Thread.sleep(sleepTime);
 
        } catch (InterruptedException exception) {
            System.err.println(exception.toString());
 
        }
    // print thread name
        System.err.println(getName() + " done sleeping");
    }
}
