import java.awt.Container;
import java.awt.LayoutManager;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import javax.swing.JDialog;

public class WindowListenerLimpador implements WindowListener {
  private final Container container;
  private final LayoutManager layoutManagerOriginal;

  public WindowListenerLimpador(JDialog dialog) {
    this.container = dialog.getContentPane();
    this.layoutManagerOriginal = this.container.getLayout();
  }

  @Override
  public void windowActivated(WindowEvent event) {

  }

  @Override
  public void windowClosed(WindowEvent event) {

  }

  @Override
  public void windowClosing(WindowEvent event) {
  }

  @Override
  public void windowDeactivated(WindowEvent event) {
    this.container.removeAll();
    this.container.setLayout(this.layoutManagerOriginal);
  }

  @Override
  public void windowDeiconified(WindowEvent event) {
  }

  @Override
  public void windowIconified(WindowEvent event) {
  }

  @Override
  public void windowOpened(WindowEvent event) {
  }
}
