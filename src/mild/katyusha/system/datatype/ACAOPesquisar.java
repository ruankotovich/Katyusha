/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mild.katyusha.system.datatype;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Dmitry
 */
public class ACAOPesquisar extends Acao {

    @Override
    public void aplicar() {
        if (!getCaller().isSad) {
            String objetoDePesquisa = getObjeto();
            String ondePesquisar = getObjeto();
            if (objetoDePesquisa.contains("%")) {
                objetoDePesquisa = getCaller().jInputText.getText().replace(getCognicao().getParametro(), "");
                ondePesquisar = getObjeto().replace("%", objetoDePesquisa);
            }
            String toSend = ondePesquisar.replace(" ", "+");
            try {
                Runtime.getRuntime().exec("cmd /c start " + toSend);
            } catch (IOException ex) {
                Logger.getLogger(ACAOPesquisar.class.getName()).log(Level.SEVERE, null, ex);
            }
            getCaller().jInputText.setText("");
        }
    }

}
