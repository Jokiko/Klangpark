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
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            System.out.println(xValue.getText() + ", " + yValue.getText() + ", " + zValue.getText());
            Park testPark = new Park(Integer.parseInt(xValue.getText()), Integer.parseInt(yValue.getText()), Integer.parseInt(zValue.getText()));
            testPark.start();
            ParkWindow pw = new ParkWindow("", Integer.parseInt(xValue.getText()), Integer.parseInt(zValue.getText()));
            this.dispose();
        }
        catch(NumberFormatException | NegativeArraySizeException ex){
            System.out.println("");
        }
    }
}
