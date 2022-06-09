import java.io.OutputStream;
import javax.swing.JTextArea;

public class TesteOutputStream extends OutputStream {
  private final JTextArea textArea;

  public TesteOutputStream(JTextArea textArea) {
    this.textArea = textArea;
  }

  @Override
  public void write(int byte_) {
    this.textArea.setText(this.textArea.getText() + (char) (byte_ & 0xFF));
  }
}