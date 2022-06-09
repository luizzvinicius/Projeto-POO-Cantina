import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class EntradaGUI extends JFrame implements ActionListener {
    private JLabel label;
    private JTextField textField;
    private Container container;

    public EntradaGUI() {
        this.setSize(300, 300);
        this.setTitle("Teste");
        this.container = getContentPane();

        this.label = new JLabel();
        this.textField = new JTextField();

        this.container.add(this.label);
        this.container.add(this.textField);
    }

    public void lerEnter(String msg) {
    }

    public String lerString(String msg) {

    }

    public int lerInt(String msg) {

    }

    public int lerIntValidar(String msg, int min, int max) {

    }

    public int lerIndice(String msg, int max) {
        return lerIntValidar(msg, 1, max) - 1;
    }

    public double lerDoubleValidar(String msg) {

    }

    @Override
    public void actionPerformed(ActionEvent e) {

    }
}