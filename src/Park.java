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
    static int currentY;
    //0: north, 1: east, 2: south, 3: west
    static int lineOfSight;
    private final int birdRange = 100;
    private final int insectRange = 50;

    public Park(int x, int y, int z){
        this.x = x;
        this.y = y;
        this.z = z;
        park = new SoundUnit[x][y][z];
        currentX = x/2;
        currentZ = z/2;
        currentY = y>=5 ? 5 : y/2;
        lineOfSight = 0;
        initializePark();
        printPark();
    }

    //generate critters in the park
    private void initializePark(){
        for (int j = 0; j<x; j++){
            for (int i = 0; i < z; i++) {
                for (int k = 0; k < y; k++) {
                    int random = (int) (Math.random() * 100);
                    //insect
                    if (random <= 10) {
                        park[j][k][i] = new SoundUnit(1);
                    }
                    //bird
                    else if (random >= 99) {
                        park[j][k][i] = new SoundUnit(2);
                    }
                    //empty
                    else {
                        park[j][k][i] = new SoundUnit(0);
                    }
                    //single bird test
                    /*if(j == x/2 && i == z/2 && k == 2) park[j][k][i] = new SoundUnit(2);
                    else {
                        park[j][k][i] = new SoundUnit(0);
                    }*/
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
        float volume = 0;
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
    private float getSoundPanning(int vectorX, int vectorY, int vectorZ){
        float pan = 0;
        int tempX = 0;
        int tempZ = 0;
        double scalarProduct;
        double amount1, amount2, angle;
        switch (lineOfSight) {
            case 0 -> {
                tempX = -1;
                tempZ = 0;
            }
            case 2 -> tempX = 1;
            case 1 -> {
                tempX = 0;
                tempZ = 1;
            }
            case 3 -> tempZ = -1;
        }
        //calculate angle of incoming sound vector in relation to lineOfSight
        scalarProduct = vectorX * tempX + vectorZ * tempZ;
        amount1 = Math.sqrt((Math.pow(vectorX,2)+Math.pow(vectorY, 2)+Math.pow(vectorZ, 2)));
        amount2 = Math.sqrt((Math.pow(tempX,2)+Math.pow(0, 2)+Math.pow(tempZ, 2)));
        angle = Math.toDegrees(Math.acos(scalarProduct/(amount1*amount2)));
        System.out.println(vectorX+ ", "+ vectorZ+ ", "+angle);
        switch (lineOfSight){
            case 1:
                pan = convertAngleToPan(angle);
                break;
        }
        pan = convertAngleToPan(angle);
        return pan;
    }

    //play bird sound with given modifiers
    private void playBirdSound(int vectorX, int vectorY, int vectorZ){
        System.out.println("Zwitscher");
        double vectorLength = Math.sqrt((Math.pow(vectorX,2)+Math.pow(vectorY, 2)+Math.pow(vectorZ, 2)));
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

                //setting the sound's volume
                FloatControl volume = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
                float v = getSoundVolume(vectorLength, 2);
                volume.setValue(v);
                //System.out.println("Volume: "+Math.pow(10f, volume.getValue() / 20f));

                //setting the sound's pan (left-right positioning)
                FloatControl pan = (FloatControl) clip.getControl(FloatControl.Type.PAN);
                float f = getSoundPanning(vectorX, vectorY, vectorZ);
                pan.setValue(f);

                 //TO-DO: random pitch shifting
                clip.loop(0);
            } catch (LineUnavailableException | UnsupportedAudioFileException | IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void playInsectSound(int vectorX, int vectorY, int vectorZ){
        System.out.println("Zirp");
        double vectorLength = Math.sqrt((Math.pow(vectorX,2)+Math.pow(vectorY, 2)+Math.pow(vectorZ, 2)));
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

                //setting the sound's volume
                FloatControl volume = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
                float v = getSoundVolume(vectorLength, 1);
                volume.setValue(v);
                //System.out.println("Volume: "+Math.pow(10f, volume.getValue() / 20f));

                //setting the sound's pan (left-right positioning)
                FloatControl pan = (FloatControl) clip.getControl(FloatControl.Type.PAN);
                float f = getSoundPanning(vectorX, vectorY, vectorZ);
                pan.setValue(f);

                //TO-DO: random pitch shifting
                clip.loop(0);
            } catch (LineUnavailableException | UnsupportedAudioFileException | IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void calculateDistance(){
        int c = 0;
        for (int i = 0; i < x; i++) {
            for (int j = 0; j < y; j++) {
                for (int k = 0; k < z; k++) {
                    //entry is bird or insect
                    if (park[i][j][k].type() == 1 || park[i][j][k].type() == 2){
                        int vectorX = currentX-i;
                        int vectorY = currentY -j;
                        int vectorZ = currentZ-k;
                        System.out.println("Entfernung zum Standpunkt: "+vectorX + ", "+ vectorZ+ ",h: "+vectorY);
                        c++;
                        if(park[i][j][k].type() == 1){
                            int threshold = 4;
                            int random = (int) (Math.random() * threshold);
                            System.out.println("Random " + random);
                            if(random >= threshold-1) {
                                playInsectSound(vectorX, vectorY, vectorZ);
                            }
                        }
                        else {
                            int threshold = 4;
                            int random = (int) (Math.random() * threshold);
                            System.out.println("Random " + random);
                            if(random >= threshold-1) {
                                playBirdSound(vectorX, vectorY, vectorZ);
                            }
                        }
                    }
                }
            }
        }
        System.out.println("Critters: "+ c);
    }

    public void run() {
        while(!Thread.interrupted()){
            System.out.println("hi");
            calculateDistance();
            /*try {
                sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }*/
        }
    }

}
