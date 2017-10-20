/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mild.katyusha.system.build;

import mild.katyusha.system.window.WNDBundle;
import mild.katyusha.system.window.WNDOpener;

/**
 *
 * @author Dmitry
 */
public class TRDBundle implements Runnable {

    private final WNDBundle caller;
    private final int size;

    public TRDBundle(WNDBundle calling) {
        this.caller = calling;
        this.size = this.caller.getSize().height;
    }

    @Override
    public void run() {
        if (caller.isActivated) {
            deactivate();
        } else {
            activate();
        }
    }

    private void deactivate() {
        caller.setActivated(false);
        while (caller.getLocation().getY() >= 0 - this.size) {
            caller.setLocation(caller.getX(), caller.getY() - 1);
            caller.repaint();
        }
        new WNDOpener(caller, this.caller.isSad).setVisible(true);
    }

    private void activate() {
        caller.setActivated(true);
        while (caller.getLocation().getY() < 0) {
            caller.setLocation(caller.getX(), caller.getY() + 1);
            caller.repaint();
        }

    }

}
