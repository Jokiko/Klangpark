import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.*;

//source: https://stackoverflow.com/questions/10876491/how-to-use-keylistener
public class ParkWindow extends JFrame implements KeyListener {

    JLabel position;
    JLabel direction;
    int x;
    int z;

    public ParkWindow (String s, int x, int z){
        super(s);
        this.x = x;
        this.z = z;
        this.setTitle("Klangpark");
        JPanel p = new JPanel(new GridLayout(2, 3));
        position = new JLabel("Position: "+Park.currentX+ ", "+Park.currentZ);
        direction = new JLabel("Blickrichtung: Norden");
        position.setHorizontalAlignment(JLabel.CENTER);
        direction.setHorizontalAlignment(JLabel.CENTER);
        p.add(position);
        p.add(direction);
        add(p);
        addKeyListener(this);
        setSize(300, 100);
        setVisible(true);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        //check in which direction to go and whether the park's boundaries have already been reached
        switch (e.getKeyCode()) {
            case KeyEvent.VK_W, KeyEvent.VK_UP -> {
                synchronized(Park.lock) {
                    if (Park.currentZ < z) {
                        Park.currentZ++;
                        position.setText("Position: " + Park.currentX + ", " + Park.currentZ);
                    }
                }
                System.out.println("Standpunkt: " + Park.currentX + "/" + Park.currentZ);
            }
            case KeyEvent.VK_A, KeyEvent.VK_LEFT -> {
                synchronized (Park.lock) {
                    if (Park.currentX > 0) {
                        Park.currentX--;
                        position.setText("Position: " + Park.currentX + ", " + Park.currentZ);
                    }
                }
                System.out.println("Standpunkt: " + Park.currentX + "/" + Park.currentZ);
            }
            case KeyEvent.VK_S, KeyEvent.VK_DOWN -> {
                synchronized (Park.lock) {
                    if (Park.currentZ > 0) {
                        Park.currentZ--;
                        position.setText("Position: " + Park.currentX + ", " + Park.currentZ);
                    }
                }
                System.out.println("Standpunkt: " + Park.currentX + "/" + Park.currentZ);
            }
            case KeyEvent.VK_D, KeyEvent.VK_RIGHT -> {
                synchronized (Park.lock) {
                    if (Park.currentX < x) {
                        Park.currentX++;
                        position.setText("Position: " + Park.currentX + ", " + Park.currentZ);
                    }
                }
                System.out.println("Standpunkt: " + Park.currentX + "/" + Park.currentZ);
            }
            //turn clockwise
            case KeyEvent.VK_E -> {
                synchronized(Park.lock) {
                    switch (Park.lineOfSight) {
                        case 0 -> {
                            Park.lineOfSight = 1;
                            direction.setText("Blickrichtung: Osten");
                        }
                        case 1 -> {
                            Park.lineOfSight = 2;
                            direction.setText("Blickrichtung: Süden");
                        }
                        case 2 -> {
                            Park.lineOfSight = 3;
                            direction.setText("Blickrichtung: Westen");
                        }
                        case 3 -> {
                            Park.lineOfSight = 0;
                            direction.setText("Blickrichtung: Norden");
                        }
                    }
                }
            }
            //turn counter-clockwise
            case KeyEvent.VK_Q -> {
                synchronized(Park.lock) {
                    switch (Park.lineOfSight) {
                        case 0 -> {
                            Park.lineOfSight = 3;
                            direction.setText("Blickrichtung: Westen");
                        }
                        case 1 -> {
                            Park.lineOfSight = 0;
                            direction.setText("Blickrichtung: Norden");
                        }
                        case 2 -> {
                            Park.lineOfSight = 1;
                            direction.setText("Blickrichtung: Osten");
                        }
                        case 3 -> {
                            Park.lineOfSight = 2;
                            direction.setText("Blickrichtung: Süden");
                        }
                    }
                }
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

    public static void main(String[] args) {
    }

}
