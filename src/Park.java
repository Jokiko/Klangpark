import javax.sound.sampled.*;
import javax.sound.midi.*;
import java.io.IOException;
import java.net.URL;

public class Park extends Thread{

    int x;
    int y;
    int z;
    SoundUnit[][][] park;
    public Clip clip;
    static int currentX;
    static int currentZ;
    //0: north, 1: east, 2: south, 3: west
    static int lineOfSight;

    public Park(int x, int y, int z){
        this.x = x;
        this.y = y;
        this.z = z;
        park = new SoundUnit[x][y][z];
        currentX = x/2;
        currentZ = z/2;
        lineOfSight = 0;
        initializePark();
        printPark();
    }


    private void initializePark(){
        for (int j = 0; j<x; j++){
            for (int i = 0; i < z; i++) {
                for (int k = 0; k < y; k++) {
                    int random = (int) (Math.random() * 100);
                    //insect
                    if (random <= 10) {
                        park[j][0][i] = new SoundUnit(1);
                    }
                    //bird
                    else if (random >= 95) {
                        park[j][0][i] = new SoundUnit(2);
                    }
                    //empty
                    else {
                        park[j][0][i] = new SoundUnit(0);
                    }
                }
            }
        }
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

    private void playBirdSound(){
        System.out.println("Zwitscher");
        try {
            URL url = Main.class.getResource("/sounds/cardinal.wav");
            assert url != null;
            AudioInputStream ais = AudioSystem.getAudioInputStream(url);
            clip = AudioSystem.getClip();
            clip.open(ais);
            clip.start();
        } catch (LineUnavailableException | UnsupportedAudioFileException | IOException e) {
            e.printStackTrace();
        }
    }

    private void calculateDistance(){
        int c = 0;
        for (int i = 0; i < x; i++) {
            //for (int j = 0; j < y; j++) {
                for (int k = 0; k < z; k++) {
                    //entry is bird or insect
                    if (park[i][0][k].type() == 1 || park[i][0][k].type() == 2){
                        int vectorX = currentX-i;
                        int vectorY = 0;
                        int vectorZ = currentZ-k;
                        System.out.println("Entfernung zum Standpunkt: "+vectorX + ", "+ vectorZ);
                        c++;
                        if(park[i][0][k].type() == 1){
                            //placeholder
                        }
                        else {
                            playBirdSound();
                        }
                    }
                }
           // }
        }
        System.out.println("Critters: "+ c);
    }

    public void run() {
        while(!Thread.interrupted()){
            System.out.println("hi");
            calculateDistance();
            try {
                sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}
