import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ConfigWindow extends JFrame implements ActionListener {

    JTextField parkUnit;
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
        setSize(350,200);
        setTitle("Konfiguration des Klangparks");
        setLayout(new GridLayout(7,1));
        parkUnit = new JTextField("10");
        xValue = new JFormattedTextField("20");
        yValue = new JTextField("1");
        zValue = new JTextField("20");
        birdValue = new JTextField("5");
        insectValue = new JTextField("10");
        JButton confirm = new JButton("Erstelle Park");
        confirm.addActionListener(this);
        add(new JLabel("Größe einer Parkeinheit:"));
        add(parkUnit);
        add(new JLabel("x-Wert (in Parkeinheiten):"));
        add(xValue);
        add(new JLabel("y-Wert (in Parkeinheiten):"));
        add(yValue);
        add(new JLabel("z-Wert (in Parkeinhieten):"));
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
            int pU = Integer.parseInt(parkUnit.getText());
            int x = Integer.parseInt(xValue.getText());
            int y = Integer.parseInt(yValue.getText());
            int z = Integer.parseInt(zValue.getText());
            int bird = Integer.parseInt(birdValue.getText());
            int insect = Integer.parseInt(insectValue.getText());
            if((x*y*z)*pU < bird + insect){
                throw new IllegalArgumentException("Nicht mehr Tiere als Platz im Park!");
            }

            Main.parkUnit = pU;
            Main.x = x;
            Main.y = y;
            Main.z = z;
            Main.bird = bird;
            Main.insect = insect;
            Main.configured.release();

            this.dispose();
            int cores = Runtime.getRuntime().availableProcessors();
            System.out.println("Kerne: "+cores);
        }
        catch(IllegalArgumentException | NegativeArraySizeException ex){
            System.out.println(ex.getMessage());
        }
    }
}
