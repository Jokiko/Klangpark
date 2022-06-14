public class Main {


    public static void main(String[] args) {
        int x = 25;
        int y = 5;
        int z = 25;
        Park testPark = new Park(x, y, z);
        testPark.start();
        ParkWindow pw = new ParkWindow("", x, z);
    }

}
