import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ConfigWindow extends JFrame implements ActionListener {

    JFormattedTextField xValue;
    JTextField yValue;
    JTextField zValue;


    public static void main(String[] args) {
        new ConfigWindow();
    }

    public ConfigWindow(){
        super();
        setSize(300,200);
        setTitle("Parkdimensionen");
        setLayout(new GridLayout(4,1));
        xValue = new JFormattedTextField("200");
        yValue = new JTextField("10");
        zValue = new JTextField("200");
        JButton confirm = new JButton("Erstelle Park");
        confirm.addActionListener(this);
        add(new JLabel("x-Wert:"));
        add(xValue);
        add(new JLabel("y-Wert:"));
        add(yValue);
        add(new JLabel("z-Wert:"));
        add(zValue);
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
            Park testPark = new Park(x, y , z);
            SoundUnit[][][] park = testPark.getPark();
            ParkThread pt1 = new ParkThread(new ThreadVolume(0, x/2, 0, y, 0, z), park);
            ParkThread pt2 = new ParkThread(new ThreadVolume(x/2+1, x, 0, y, 0, z), park);
            pt1.start();
            pt2.start();
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
