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
  public void windowActivated(WindowEvent e) {
    
  }

  @Override
  public void windowClosed(WindowEvent e) {
    
  }

  @Override
  public void windowClosing(WindowEvent e) {
  }

  @Override
  public void windowDeactivated(WindowEvent e) {
    this.container.removeAll();
    this.container.setLayout(this.layoutManagerOriginal);
  }

  @Override
  public void windowDeiconified(WindowEvent e) {
  }

  @Override
  public void windowIconified(WindowEvent e) {
  }

  @Override
  public void windowOpened(WindowEvent e) {
  }
}
