import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import javax.swing.JDialog;

public class WindowListenerLimpador implements WindowListener {
  private final JDialog dialog;
  
  public WindowListenerLimpador(JDialog dialog) {
    this.dialog = dialog;
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
    this.dialog.getContentPane().removeAll();
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
