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

    public static void main(String[] args) {
        new test(num++).start();
        new test(num++).start();
        System.out.println(test.num);
    }
}
