public class Main {


    public static void main(String[] args) {
        int x = 200;
        int y = 10;
        int z = 200;
        Park testPark = new Park(x, y, z);
        testPark.start();
        ParkWindow pw = new ParkWindow("", x, z);
    }

}
