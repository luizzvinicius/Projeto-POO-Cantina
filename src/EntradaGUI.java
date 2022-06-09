import javax.swing.JOptionPane;

public class EntradaGUI extends Entrada {
    private Tela tela;

    public EntradaGUI(Tela tela) {
        this.tela = tela;
    }

    public String lerString(String msg) {
        var entrada = "";
        var initialType = JOptionPane.QUESTION_MESSAGE;

        do {
            entrada = JOptionPane.showInputDialog(this.tela, msg, "Entrada", initialType);
            initialType = JOptionPane.ERROR_MESSAGE;
        } while (entrada.isEmpty() || !Character.isLetter(entrada.charAt(0)));

        return entrada;
    }

    public int lerInt(String msg) {
        var entrada = -1;
        var initialType = JOptionPane.QUESTION_MESSAGE;

        do {
            try {
                entrada = Integer.parseUnsignedInt(JOptionPane.showInputDialog(this.tela, msg, "Entrada", initialType));
            } catch (NumberFormatException e) {
                initialType = JOptionPane.ERROR_MESSAGE;
            }
        } while (entrada == -1);

        return entrada;
    }

    public int lerIntValidar(String msg, int min, int max) {
        var entrada = -1;
        var initialType = JOptionPane.QUESTION_MESSAGE;

        do {
            try {
                entrada = Integer.parseUnsignedInt(JOptionPane.showInputDialog(this.tela, msg, "Entrada", initialType));
            } catch (NumberFormatException e) {
                initialType = JOptionPane.ERROR_MESSAGE;
            }
        } while (entrada < min || entrada > max);

        return entrada;
    }

    public int lerIndice(String msg, int max) {
        return lerIntValidar(msg, 1, max) - 1;
    }

    public double lerDoubleValidar(String msg) {
        var entrada = -1d;
        var initialType = JOptionPane.QUESTION_MESSAGE;

        do {
            try {
                entrada = Double.parseDouble(JOptionPane.showInputDialog(this.tela, msg, "Entrada", initialType));
            } catch (NumberFormatException e) {
                initialType = JOptionPane.ERROR_MESSAGE;
            }
        } while (entrada < 0);

        return entrada;
    }
}