/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mild.katyusha.system.ctr;

import java.awt.Color;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import mild.katyusha.system.datatype.Twig;
import mild.katyusha.system.window.WNDBundle;

/**
 *
 * @author Dmitry
 */
public class CTRRotulo implements MouseListener {

    WNDBundle caller;

    public CTRRotulo(WNDBundle calling) {
        this.caller = calling;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        Twig objeto = ((Twig) e.getSource());
        System.out.println(caller.autoMenu);
        if (!caller.jInputText.getText().contains("Interpretando...")) {
            if (caller.autoMenu) {
                caller.jInputText.setText(objeto.getComando());
                caller.send();
            } else {
                if (e.getButton() != MouseEvent.BUTTON3) {
                    caller.jInputText.setText(objeto.getComando());
                    caller.jInputText.grabFocus();
                } else {
                    caller.jInputText.setText(objeto.getComando());
                    caller.send();
                }
            }
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
        Twig objeto = ((Twig) e.getSource());
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        Twig objeto = ((Twig) e.getSource());
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        Twig objeto = ((Twig) e.getSource());
        JLabel rotulo = caller.jTrotulo;
        if (objeto.getLabel() == null) {
            rotulo.setText("<Indefinido>");
        } else {
            rotulo.setText(objeto.getLabel());
        }
        rotulo.setVisible(true);
        objeto.setBorder(BorderFactory.createLineBorder(Color.yellow, 2));
        objeto.repaint();
    }

    @Override
    public void mouseExited(MouseEvent e) {
        Twig objeto = ((Twig) e.getSource());
        caller.jTrotulo.setVisible(false);
        objeto.setBorder(null);
        objeto.repaint();
    }

}
