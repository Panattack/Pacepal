/////////// This should be PrintDemo.java
  
class PrintDemo {
  
    public void printCount(){
     try {
          for(int i = 5; i > 0; i--) {
             System.out.println("Counter   ---   "  + i );
              int sleepTime = (int) (Math.random() * 500);
             Thread.sleep(sleepTime);
          }
      } catch (Exception e) {
          System.out.println("Thread  interrupted.");
      }
    }
 }
