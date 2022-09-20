import java.util.concurrent.Semaphore;

public class Main {

    static int x;
    static int y;
    static int z;
    static int bird;
    static int insect;
    static final Semaphore configured = new Semaphore(0);

    public static void main(String[] args) throws InterruptedException {

        ConfigWindow cw = new ConfigWindow();
        configured.acquire();

        Park testPark = new Park(x, y , z, bird, insect);
        SoundUnit[][][] park = testPark.getPark();
        ParkThread pt1 = new ParkThread(new ThreadVolume(0, x/2, 0, y/2, 0, z/2), park);
        ParkThread pt2 = new ParkThread(new ThreadVolume(x/2, x, 0, y/2, 0, z/2), park);
        ParkThread pt3 = new ParkThread(new ThreadVolume(0, x/2, 0, y/2, z/2, z), park);
        ParkThread pt4 = new ParkThread(new ThreadVolume(x/2, x, 0, y/2, z/2, z), park);
        ParkThread pt5 = new ParkThread(new ThreadVolume(0, x/2, y/2, y, 0, z/2), park);
        ParkThread pt6 = new ParkThread(new ThreadVolume(x/2, x, y/2, y, 0, z/2), park);
        ParkThread pt7 = new ParkThread(new ThreadVolume(0, x/2, y/2, y, z/2, z), park);
        ParkThread pt8 = new ParkThread(new ThreadVolume(x/2, x, y/2, y, z/2, z), park);
        pt1.start();
        pt2.start();
        pt3.start();
        pt4.start();
        pt5.start();
        pt6.start();
        pt7.start();
        pt8.start();
        //testPark.start();
        ParkWindow pw = new ParkWindow("", x, z);
        /*
            Park testPark = new Park(x, y, z);
            testPark.start();
            ParkWindow pw = new ParkWindow("", x, z);
        */
    }

}
