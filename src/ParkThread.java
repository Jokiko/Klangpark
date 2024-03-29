import javax.sound.sampled.*;
import java.io.IOException;
import java.net.URL;
import java.util.concurrent.BrokenBarrierException;

public class ParkThread extends Thread{

    private Clip clip;
    private ThreadVolume volume;
    private SoundUnit[][][] park;



    public ParkThread(ThreadVolume tV, SoundUnit[][][] park){
        volume = tV;
        this.park = park;
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
            ratio = (Park.insectRange - amount)/Park.insectRange;
        }
        //bird
        else {
            ratio = (Park.birdRange - amount)/Park.birdRange;
        }
        volume = 20f * (float) Math.log10(ratio);
        return volume;
    }

    //calculate angle of incoming sound vector
    private float getSoundPanning(Vector3D soundVector){
        float pan;
        Vector3D tempVector;
        double scalarProduct;
        double amount1, amount2, angle;
        synchronized(Park.lock) {
            switch (Park.lineOfSight) {
                case 0 -> tempVector = new Vector3D(-1, 0, 0);
                case 2 -> tempVector = new Vector3D(1, 0, 0);
                case 1 -> tempVector = new Vector3D(0, 0, 1);
                case 3 -> tempVector = new Vector3D(0, 0, -1);
                default -> throw new IllegalStateException("Unexpected value: " + Park.lineOfSight);
            }
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
    private void playBirdSound(Vector3D soundVector){
        System.out.println("Zwitscher");
        double vectorLength = Math.sqrt((Math.pow(soundVector.x(),2)+Math.pow(soundVector.y(), 2)+Math.pow(soundVector.z(), 2)));
        System.out.println(vectorLength);
        if(vectorLength <= Park.birdRange) {
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
                float f = getSoundPanning(soundVector);
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

    private void playInsectSound(Vector3D soundVector){
        System.out.println("Zirp");
        double vectorLength = Math.sqrt((Math.pow(soundVector.x(),2)+Math.pow(soundVector.y(), 2)+Math.pow(soundVector.z(), 2)));
        System.out.println(vectorLength);
        if(vectorLength <= Park.insectRange) {
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


    @Override
    public void run() {
        System.out.println("Start: "+volume.xStart()+ ", " + volume.yStart() + ", "+ volume.zStart()+ ", Ende: "+ volume.xEnd() + ", " + volume.yEnd() + ", " + volume.zEnd());

        while(!Thread.interrupted()){
            long timeStart = System.currentTimeMillis();
            for (int i = volume.xStart(); i < volume.xEnd(); i++) {
                for (int j = volume.yStart(); j < volume.yEnd(); j++) {
                    for (int k = volume.zStart(); k < volume.zEnd(); k++) {
                        //entry is bird or insect
                        if (park[i][j][k].type() == 1 || park[i][j][k].type() == 2){
                            Vector3D soundVector;
                            synchronized(Park.lock) {
                                soundVector = new Vector3D(Park.currentX - i, Park.currentY - j, Park.currentZ - k);
                            }
                            //System.out.println("Entfernung zum Standpunkt: "+soundVector.x() + ", "+ soundVector.z()+ ",h: "+soundVector.y());
                            //c++;
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
            try {
                Park.barrier.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (BrokenBarrierException e) {
                e.printStackTrace();
            }
            long timeEnd = System.currentTimeMillis();
            System.out.println("Verlaufszeit der Schleife in " + this.getId() +": "+ (timeEnd - timeStart) + " Millisek.");
        }
    }

}
