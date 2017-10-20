/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mild.katyusha.system.bean;

import java.awt.AlphaComposite;
import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.JLabel;

/**
 *
 * @author Dmitry
 */
public class JTransparentLabel extends JLabel {

    private float alpha = 1.0f;

    public JTransparentLabel(float transparency) {
        if (transparency < 0.0f || transparency > 1.0f) {
            this.alpha = 1.0f;
        } else {
            this.alpha = transparency;
        }
    }

    public float getAlpha() {
        return alpha;
    }

    public void setAlpha(float alpha) {
        this.alpha = alpha;
    }

    @Override
    public void paint(Graphics g) {
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setComposite(AlphaComposite.SrcOver.derive(alpha));
        super.paint(g2d);
        g2d.dispose();
    }
}
