package game;

import java.awt.event.KeyEvent;

public class KeyHandler implements java.awt.event.KeyListener {
  public KeyHandler() {}
  
  private boolean[] keys = new boolean['ȍ'];
  
  private int pressTime = 10;
  
  public void keyPressed(KeyEvent e) {
    keys[e.getKeyCode()] = true;
  }
  
  public void keyReleased(KeyEvent e) {
    keys[e.getKeyCode()] = false;
  }
  

  public void keyTyped(KeyEvent e) {}
  
  public boolean[] getKeys()
  {
    return keys;
  }
  
  public void reset() {
    for (int i = 0; i < keys.length; i++) {
      keys[i] = false;
    }
  }
}
