import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

//source: https://stackoverflow.com/questions/10876491/how-to-use-keylistener
public class ParkWindow extends JFrame implements KeyListener {

    JLabel label;
    int x;
    int z;

    public ParkWindow (String s, int x, int z){
        super(s);
        this.x = x;
        this.z = z;
        JPanel p = new JPanel();
        label = new JLabel("Klangpark");
        p.add(label);
        add(p);
        addKeyListener(this);
        setSize(200, 100);
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
                if (Park.currentZ < z) {
                    Park.currentZ++;
                }
                Park.lineOfSight = 0;
                System.out.println("Standpunkt: " + Park.currentX + "/" + Park.currentZ);
            }
            case KeyEvent.VK_A, KeyEvent.VK_LEFT -> {
                if (Park.currentX > 0) {
                    Park.currentX--;
                }
                Park.lineOfSight = 1;
                System.out.println("Standpunkt: " + Park.currentX + "/" + Park.currentZ);
            }
            case KeyEvent.VK_S, KeyEvent.VK_DOWN -> {
                if (Park.currentZ > 0) {
                    Park.currentZ--;
                }
                Park.lineOfSight = 2;
                System.out.println("Standpunkt: " + Park.currentX + "/" + Park.currentZ);
            }
            case KeyEvent.VK_D, KeyEvent.VK_RIGHT -> {
                if (Park.currentX < x) {
                    Park.currentX++;
                }
                Park.lineOfSight = 3;
                System.out.println("Standpunkt: " + Park.currentX + "/" + Park.currentZ);
            }
        }

    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

    public static void main(String[] args) {
    }

}
