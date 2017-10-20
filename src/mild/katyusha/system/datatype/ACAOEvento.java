/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mild.katyusha.system.datatype;

import javax.swing.ImageIcon;
import mild.katyusha.system.ctr.CTRInteligencia;
import mild.katyusha.system.ctr.CTRUnrec;
import mild.katyusha.system.window.WNDCerebro;

/**
 *
 * @author Dmitry
 */
public class ACAOEvento extends Acao {

    @Override
    public void aplicar() {
        String acao = getObjeto();
        switch (acao) {
            case "fechar":
                System.exit(0);
                break;
            case "editor_cognicao":
                new WNDCerebro(getCaller()).setVisible(true);
                break;
            case "ignore":
                CTRInteligencia ceb = getCaller().cerebro;
                System.out.println(ceb.toAdd);
                if (ceb.toAdd != null) {
                    CTRUnrec.removeUnrec(ceb.toAdd, ceb.unrecs);
                    ceb.toAdd = null;
                }
                break;
            case "neve":
                getCaller().jLbFrente.setIcon(new ImageIcon(this.getClass().getResource("/mild/katyusha/system/gfx/neve.gif")));
                break;
                case "folhas":
                getCaller().jLbFrente.setIcon(new ImageIcon(this.getClass().getResource("/mild/katyusha/system/gfx/folhas.gif")));
                break;

        }
        getCaller().jInputText.setText("");
    }

}
