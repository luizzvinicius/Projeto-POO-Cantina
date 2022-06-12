import java.awt.Container;
import java.awt.Dimension;
import javax.swing.JDialog;

public abstract class TelaBase {
  protected final JDialog dialog;
  protected final Container container;

  protected TelaBase(JDialog dialog) {
    this.dialog = dialog;
    this.container = this.dialog.getContentPane();
  }

  protected void mostrar(int width) {
    this.dialog.setMinimumSize(new Dimension(width, this.container.getMinimumSize().height));
    this.dialog.pack();
    this.dialog.setLocationRelativeTo(this.dialog.getOwner());
    this.dialog.setVisible(true);
  }
}
