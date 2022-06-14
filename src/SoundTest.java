import javax.sound.sampled.*;
import javax.sound.midi.*;
import java.io.IOException;
import java.net.URL;

//test class to test java sound features
public class SoundTest {

    public static Mixer mixer;
    public static Clip clip;
    public static Clip clip2;
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
        URL url = Main.class.getResource("cardinal.wav");
        AudioInputStream ais = AudioSystem.getAudioInputStream(url);
        clip = AudioSystem.getClip();

        URL url2 = Main.class.getResource("/sounds/cardinal.wav");
        AudioInputStream ais2 = AudioSystem.getAudioInputStream(url2);
        clip2 = AudioSystem.getClip();

        clip.open(ais);
        clip2.open(ais2);
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }


        clip2.start();
        Thread.sleep(300);
        clip.loop(Clip.LOOP_CONTINUOUSLY);
        Thread.sleep(300);
        clip2.loop(Clip.LOOP_CONTINUOUSLY);
        Thread.sleep(3000);
        clip.close();
        clip2.close();


    }




}
