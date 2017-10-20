/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mild.katyusha.system.build;

import java.awt.Color;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import mild.katyusha.system.window.WNDBundle;

/**
 *
 * @author Dmitry
 */
public class TRDFala implements Runnable {

    WNDBundle caller;
    public static final String[] T_1 = {"G", "H", "J", "O"};
    public static final String[] T_2 = {"A", "E", "K", "L", "R", "S", "W"};
    public static final String[] T_3 = {"B", "C", "F", "D", "M", "N", "P", "Q", "T", "V", "Z"};
    public static final String[] T_4 = {"I", "U", "X", "Y"};

    private boolean isIn(String[] lista, String par) {
        boolean retorno = false;

        for (String lista1 : lista) {
            if (lista1.equals(par)) {
                retorno = true;
                break;
            }
        }

        return retorno;
    }

    public TRDFala(WNDBundle calling) {
        this.caller = calling;
    }

    @Override
    public void run() {
        try {
            fala(caller.jOutputText.getText());

        } catch (InterruptedException ex) {
            Logger.getLogger(TRDFala.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void fala(String text) throws InterruptedException {
        char[] texto = text.toUpperCase().replace("KATYUSHA : ", "").toCharArray();
        int as = 70;
        if (text.contains("Vyhodila, pesnyu zavodila")) {
            as = 135;
        }

        for (int i = 0; i < texto.length; i++) {
            caller.jLbKatyusha.setIcon(new ImageIcon(this.getClass().getResource("/mild/katyusha/system/gfx/t_" + verificar(texto[i]) + ".png")));
            Thread.sleep(as);
        }

        if (!caller.isSad) {
            if (text.contains(":D") || text.contains("=D") || text.contains("=)")) {
                caller.jLbKatyusha.setIcon(new ImageIcon(this.getClass().getResource("/mild/katyusha/system/gfx/hp_2.png")));
                caller.isSad = false;
            } else if (text.contains(">:(")) {
                caller.jLbKatyusha.setIcon(new ImageIcon(this.getClass().getResource("/mild/katyusha/system/gfx/sd_2.png")));
                caller.isSad = true;
                if (caller.isActivated) {
                    caller.thread = new Thread(new TRDBundle(caller));
                    caller.thread.start();
                }
            } else if (text.contains(":/") || text.contains(":(") || text.contains("=(")) {
                caller.jLbKatyusha.setIcon(new ImageIcon(this.getClass().getResource("/mild/katyusha/system/gfx/sd_1.png")));
            } else {
                caller.jLbKatyusha.setIcon(new ImageIcon(this.getClass().getResource("/mild/katyusha/system/gfx/hp_1.png")));
            }
        } else {
            if (text.contains(":}")) {
                caller.jLbKatyusha.setIcon(new ImageIcon(this.getClass().getResource("/mild/katyusha/system/gfx/hp_2.png")));
                caller.isSad = false;
            } else {
                caller.jLbKatyusha.setIcon(new ImageIcon(this.getClass().getResource("/mild/katyusha/system/gfx/sd_2.png")));
            }
        }
        caller.jInputText.setEditable(true);
        caller.jInputText.setText("");
        caller.jInputText.setBackground(new Color(255, 155, 155));

    }

    private String verificar(char texto) {
        String comparar = String.valueOf(texto);
        if (isIn(T_1, comparar)) {
            return "1";
        } else if (isIn(T_2, comparar)) {
            return "2";
        } else if (isIn(T_3, comparar)) {
            return "3";
        } else if (isIn(T_4, comparar)) {
            return "4";
        } else {
            return "5";
        }
    }

}
