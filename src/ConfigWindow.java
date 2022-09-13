import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ConfigWindow extends JFrame implements ActionListener {

    JFormattedTextField xValue;
    JTextField yValue;
    JTextField zValue;
    JTextField birdValue;
    JTextField insectValue;


    public static void main(String[] args) {
        new ConfigWindow();
    }

    public ConfigWindow(){
        super();
        setSize(300,200);
        setTitle("Parkdimensionen");
        setLayout(new GridLayout(6,1));
        xValue = new JFormattedTextField("200");
        yValue = new JTextField("10");
        zValue = new JTextField("200");
        birdValue = new JTextField("10");
        insectValue = new JTextField("20");
        JButton confirm = new JButton("Erstelle Park");
        confirm.addActionListener(this);
        add(new JLabel("x-Wert:"));
        add(xValue);
        add(new JLabel("y-Wert:"));
        add(yValue);
        add(new JLabel("z-Wert:"));
        add(zValue);
        add(new JLabel("Anzahl Vögel:"));
        add(birdValue);
        add(new JLabel("Anzahl Grillen:"));
        add(insectValue);
        add(confirm);
        setVisible(true);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            System.out.println(xValue.getText() + ", " + yValue.getText() + ", " + zValue.getText());
            int x = Integer.parseInt(xValue.getText());
            int y = Integer.parseInt(yValue.getText());
            int z = Integer.parseInt(zValue.getText());
            int bird = Integer.parseInt(birdValue.getText());
            int insect = Integer.parseInt(insectValue.getText());
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
            this.dispose();
            int cores = Runtime.getRuntime().availableProcessors();
            System.out.println("Kerne: "+cores);
        }
        catch(NumberFormatException | NegativeArraySizeException ex){
            System.out.println("");
        }
    }
}
