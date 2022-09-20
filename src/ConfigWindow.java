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
        setSize(350,200);
        setTitle("Konfiguration des Klangparks");
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
            if(x*y*z < bird + insect){
                throw new IllegalArgumentException("Nicht mehr Tiere als Platz im Park!");
            }

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
