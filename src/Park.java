import javax.sound.sampled.*;
import java.io.IOException;
import java.net.URL;
import java.util.Random;
import java.util.concurrent.CyclicBarrier;

public class Park extends Thread{

    int x;
    int y;
    int z;
    SoundUnit[][][] park;
    public Clip clip;
    public static CyclicBarrier barrier = new CyclicBarrier(8);
    static final Object lock = new Object();
    static int currentX;
    static int currentZ;
    static int currentY;
    //0: north, 1: east, 2: south, 3: west
    static int lineOfSight;
    static final int birdRange = 100;
    static final int insectRange = 50;
    static int numberOfBirds;
    static int numberOfInsects;


    public Park(int x, int y, int z, int bird, int insect){
        this.x = x;
        this.y = y;
        this.z = z;
        numberOfBirds = bird;
        numberOfInsects = insect;
        park = new SoundUnit[x][y][z];
        currentX = x/2;
        currentZ = z/2;
        currentY = y>=5 ? 5 : y/2;
        lineOfSight = 0;
        initializePark();
        //printPark();
    }

    //generate critters in the park
    private void initializePark(){
        System.out.println("Birds: "+ numberOfBirds+", Insects: "+numberOfInsects);
        //initialize empty park
        for (int j = 0; j<x; j++) {
            for (int i = 0; i < z; i++) {
                for (int k = 0; k < y; k++) {
                    park[j][k][i] = new SoundUnit(0);
                }
            }
        }
        int[][][]  tempPark = new int[x][y][z];
        Random rand = new Random();
        //set random pattern of birds
        for (int i = 0; i < numberOfBirds; i++) {
            boolean occupied = true;
            do {
                int birdX = rand.nextInt(x);
                int birdY = rand.nextInt(y);
                int birdZ = rand.nextInt(z);
                if (tempPark[birdX][birdY][birdZ] == 0){
                    park[birdX][birdY][birdZ] = new SoundUnit(2);
                    tempPark[birdX][birdY][birdZ] = 1;
                    occupied = false;
                }
            }
            while (occupied);
        }
        //set random pattern of insects
        for (int i = 0; i < numberOfInsects; i++) {
            boolean occupied = true;
            do {
                int insX = rand.nextInt(x);
                int insY = rand.nextInt(y);
                int insZ = rand.nextInt(z);
                if (tempPark[insX][insY][insZ] == 0){
                    park[insX][insY][insZ] = new SoundUnit(1);
                    tempPark[insX][insY][insZ] = 1;
                    occupied = false;
                }
            }
            while (occupied);
        }

        //single bird test
        //park[x/2][2][z/2] = new SoundUnit(2);

        //single bird & insect test
        //park[x/2][2][z/2] = new SoundUnit(2);
        // park[x/4][2][z/4] = new SoundUnit(1);
    }

    private void printPark(){
        for (int j = 0; j < x; j++) {
            for (int i = 0; i < z; i++) {
                //System.out.print(park[i][j]);
                System.out.format("%s  ", park[j][0][i].toString());
            }
            System.out.println();
        }
    }

    public SoundUnit[][][] getPark(){
        return park;
    }

}
