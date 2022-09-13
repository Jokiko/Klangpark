import com.adonax.audiocue.AudioCue;

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

    //panning needs to be between -1 (left) and 1 (right)
    private float convertAngleToPan(double angle) {
        //check if current position isn't the same as the SoundUnit's
        if (!Double.isNaN(angle)) {
            return (float) (1 - angle / 90);
        }
        else {
            return 0;
        }
    }

    //calculate volume decibels based on vector amount
    private float getSoundVolume(double amount, int soundType){
        float volume;
        double ratio;
        //insect
        if (soundType == 1){
            ratio = (insectRange - amount)/insectRange;
        }
        //bird https://stackoverflow.com/questions/40514910/set-volume-of-java-clip
        else {
            ratio = (birdRange - amount)/birdRange;
        }
        volume = 20f * (float) Math.log10(ratio);
        return volume;
    }

    //calculate angle of incoming sound vector
    private float getSoundPanning(ThreeDVector soundVector){
        float pan;
        ThreeDVector tempVector;
        double scalarProduct;
        double amount1, amount2, angle;
        switch (lineOfSight) {
            case 0 -> tempVector = new ThreeDVector(-1,0,0);
            case 2 -> tempVector = new ThreeDVector(1,0,0);
            case 1 -> tempVector = new ThreeDVector(0,0,1);
            case 3 -> tempVector = new ThreeDVector(0,0,-1);
            default -> throw new IllegalStateException("Unexpected value: " + lineOfSight);
        }
        //calculate angle of incoming sound vector in relation to lineOfSight
        scalarProduct = soundVector.x() * tempVector.x() + soundVector.z() * tempVector.z();
        amount1 = Math.sqrt((Math.pow(soundVector.x(),2)+Math.pow(soundVector.y(), 2)+Math.pow(soundVector.z(), 2)));
        amount2 = Math.sqrt((Math.pow(tempVector.x(), 2)+Math.pow(0, 2)+Math.pow(tempVector.z(), 2)));
        angle = Math.toDegrees(Math.acos(scalarProduct/(amount1*amount2)));
        System.out.println(soundVector.x()+ ", "+ soundVector.z()+ ", "+angle);
        /*switch (lineOfSight){
            case 1:
                pan = convertAngleToPan(angle);
                break;
        }*/
        pan = convertAngleToPan(angle);
        return pan;
    }

    //play bird sound with given modifiers
    private void playBirdSound(ThreeDVector soundVector){
        System.out.println("Zwitscher");
        double vectorLength = Math.sqrt((Math.pow(soundVector.x(),2)+Math.pow(soundVector.y(), 2)+Math.pow(soundVector.z(), 2)));
        System.out.println(vectorLength);
        if(vectorLength <= birdRange) {
            try {
                int random = (int) (Math.random() * 600);
                Thread.sleep(random);
                URL url = Main.class.getResource("/sounds/cardinal.wav");
                assert url != null;
                AudioInputStream ais = AudioSystem.getAudioInputStream(url);
                clip = AudioSystem.getClip();
                clip.open(ais);

                /*AudioCue myAudioCue;
                myAudioCue = AudioCue.makeStereoCue(url, 4);
                myAudioCue.open();
                int handle = myAudioCue.play();
                float value = getSoundVolume(vectorLength, 1);
                myAudioCue.setVolume(handle, value);
                float value2 = getSoundPanning(soundVector);
                myAudioCue.setPan(handle, value);*/

                //setting the sound's volume
                FloatControl volume = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
                float v = getSoundVolume(vectorLength, 2);
                volume.setValue(v);
                //System.out.println("Volume: "+Math.pow(10f, volume.getValue() / 20f));

                //setting the sound's pan (left-right positioning)
                FloatControl pan = (FloatControl) clip.getControl(FloatControl.Type.PAN);
                float f = getSoundPanning(soundVector);
                pan.setValue(f);

                 //TO-DO: random pitch shifting
                clip.loop(0);
                //myAudioCue.play((double) value, (double) value2, 1.0, 0);
                //myAudioCue.start(handle);
                //myAudioCue.releaseInstance(handle);
            } catch (LineUnavailableException | UnsupportedAudioFileException | IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void playInsectSound(ThreeDVector soundVector){
        System.out.println("Zirp");
        double vectorLength = Math.sqrt((Math.pow(soundVector.x(),2)+Math.pow(soundVector.y(), 2)+Math.pow(soundVector.z(), 2)));
        System.out.println(vectorLength);
        if(vectorLength <= insectRange) {
            try {
                int random = (int) (Math.random() * 600);
                Thread.sleep(random);
                URL url = Main.class.getResource("/sounds/cricket.wav");
                assert url != null;
                AudioInputStream ais = AudioSystem.getAudioInputStream(url);
                clip = AudioSystem.getClip();
                clip.open(ais);

                /*AudioCue myAudioCue;
                myAudioCue = AudioCue.makeStereoCue(url, 4);
                myAudioCue.open();
                int handle = myAudioCue.obtainInstance();
                float value = getSoundVolume(vectorLength, 1);
                myAudioCue.setVolume(handle, value);
                value = getSoundPanning(soundVector);
                myAudioCue.setPan(handle, value);*/

                //setting the sound's volume
                FloatControl volume = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
                float v = getSoundVolume(vectorLength, 1);
                volume.setValue(v);
                //System.out.println("Volume: "+Math.pow(10f, volume.getValue() / 20f));

                //setting the sound's pan (left-right positioning)
                FloatControl pan = (FloatControl) clip.getControl(FloatControl.Type.PAN);
                float f = getSoundPanning(soundVector);
                pan.setValue(f);

                //TO-DO: random pitch shifting
                clip.loop(0);
                //myAudioCue.start(handle);
            } catch (LineUnavailableException | UnsupportedAudioFileException | IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void calculateDistance(){
        int c = 0;
        long timeStart = System.currentTimeMillis();
        for (int i = 0; i < x; i++) {
            for (int j = 0; j < y; j++) {
                for (int k = 0; k < z; k++) {
                    //entry is bird or insect
                    if (park[i][j][k].type() == 1 || park[i][j][k].type() == 2){
                        ThreeDVector soundVector = new ThreeDVector(currentX-i, currentY-j, currentZ-k);
                        //System.out.println("Entfernung zum Standpunkt: "+soundVector.x() + ", "+ soundVector.z()+ ",h: "+soundVector.y());
                        c++;
                        if(park[i][j][k].type() == 1){
                            int threshold = 4;
                            int random = (int) (Math.random() * threshold);
                            //System.out.println("Random " + random);
                            if(random >= threshold-1) {
                                playInsectSound(soundVector);
                            }
                        }
                        else {
                            int threshold = 4;
                            int random = (int) (Math.random() * threshold);
                            //System.out.println("Random " + random);
                            if(random >= threshold-1) {
                                playBirdSound(soundVector);
                            }
                        }
                    }
                }
            }
        }
        long timeEnd = System.currentTimeMillis();
        System.out.println("Verlaufszeit der Schleife: " + (timeEnd - timeStart) + " Millisek.");
        System.out.println("Critters: "+ c);
    }

    public void run() {
        while(!Thread.interrupted()){
            calculateDistance();
            /*try {
                sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }*/
        }
    }

}
