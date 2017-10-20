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
public class ACAOExecutar extends Acao {

    @Override
    public void aplicar() {
        if (!getCaller().isSad) {
            try {
                String objetoDeExecucao = getObjeto();
                if (objetoDeExecucao.equals("%")) {
                    objetoDeExecucao = getCaller().jInputText.getText().replace(getCognicao().getParametro(), "");
                }
                Runtime.getRuntime().exec("cmd /c start " + objetoDeExecucao);
            } catch (IOException ex) {
                Logger.getLogger(ACAOExecutar.class.getName()).log(Level.SEVERE, null, ex);
            }
            getCaller().jInputText.setText("");
        }
    }

}
