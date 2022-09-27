import javax.sound.sampled.*;
import javax.sound.midi.*;
import java.io.IOException;
import java.net.URL;

import static javax.sound.sampled.AudioFormat.Encoding.PCM_SIGNED;
import static javax.sound.sampled.AudioSystem.getAudioInputStream;

//test class to test java sound features
public class SoundTest {

    public static Mixer mixer;
    public static Clip clip;
    public static Clip clip2;
    public static Clip clip3;

    private static AudioFormat getOutFormat(AudioFormat inFormat) {
        double mod = 5;
        float rate = inFormat.getSampleRate();
        System.out.println("Sample Rate: "+rate);
        System.out.println("Sample Rate nachher: "+rate*mod);
        return new AudioFormat(inFormat.getEncoding(), (float) (rate*mod), inFormat.getSampleSizeInBits(),
                inFormat.getChannels(), inFormat.getFrameSize(), inFormat.getFrameRate(),
                inFormat.isBigEndian());
    }

    // Method 1
    // Main driver method
    public static void main(String[] args) throws InterruptedException {

        //receive and print all infromation about the soundsystem
        /*Mixer.Info[] mixInfos = AudioSystem.getMixerInfo();
        for (Mixer.Info mixInfo : mixInfos) {
            System.out.println(mixInfo.getName() + " ---- " + mixInfo.getDescription());
        }
        mixer = AudioSystem.getMixer(mixInfos[0]);*/

        try{


        URL url = Main.class.getResource("/sounds/cardinal.wav");
        AudioInputStream ais = getAudioInputStream(url);
        clip = AudioSystem.getClip();

        URL url2 = Main.class.getResource("/sounds/cricket.wav");
        AudioInputStream ais2 = getAudioInputStream(url2);
        clip2 = AudioSystem.getClip();

            final AudioInputStream in1 = getAudioInputStream(url2);

            final AudioFormat inFormat = getOutFormat(in1.getFormat());
//change the frequency of Audio format
            final AudioInputStream in2 = getAudioInputStream(inFormat, in1);

            clip3 = AudioSystem.getClip();
            clip3.open(in2);

        clip.open(ais);
        clip2.open(ais2);



        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }


        /*clip2.start();
        Thread.sleep(300);
        clip.loop(Clip.LOOP_CONTINUOUSLY);
        Thread.sleep(300);
        clip2.loop(Clip.LOOP_CONTINUOUSLY);
        Thread.sleep(3000);
        clip.close();
        clip2.close();*/
        clip3.start();
        clip3.loop(5);
        Thread.sleep(3000);
        clip2.start();
        clip2.loop(5);
        Thread.sleep(3000);



    }




}
