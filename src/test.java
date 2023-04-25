import java.io.IOException;
import java.util.HashMap;

public class test extends Thread{

    // static HashMap<Pair, Integer> map = new HashMap<>();
    // public static void main(String[] args) {
        
    //     Pair<Integer, Integer> o = new Pair<Integer, Integer>(1,2);
    //     map.put(o, 3);
    //     Chunk c = new Chunk(o,0,"0,0,0");
    //     //System.out.println(map.get(new Pair<Integer, Integer>(1,2)));
        
    //     Pair<Integer, Integer> p = o;
    //     System.out.println(map.get(c.getHashKey()));
    // }

    // void put(Pair<Integer, Integer> key) {
    //     map.put(key, 3);
    // }

    public static int num = 0;
    public int id;

    public test(int num)
    {
        this.id = num;
    }

    @Override
    public void run()
    {
        System.out.println(this.id + "   but num is : " + this.num);
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        new test(num++).start();
        new test(num++).start();
        System.out.println(test.num);
        String clearScreen = "\033[H\033[2J";
        System.out.println(clearScreen);
        System.out.println(clearScreen);

        // Check the operating system to determine the appropriate clear command
        // String os = System.getProperty("os.name").toLowerCase();
        // String clearCommand = "";
        // if (os.contains("windows")) {
        //     clearCommand = "cmd";
        // } else if (os.contains("linux") || os.contains("mac")) {
        //     clearCommand = "bash";
        // }
        // // Create a ProcessBuilder instance for the clear command
        // ProcessBuilder processBuilder = new ProcessBuilder(clearCommand);
        // Process process = processBuilder.inheritIO().start();
        // process.waitFor();
    }
}
