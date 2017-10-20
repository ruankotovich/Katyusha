/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mild.katyusha.system.datatype;

import mild.katyusha.system.ctr.CTRAudio;

/**
 *
 * @author Dmitry
 */
public class ACAOAudio extends Acao {

    @Override
    public void aplicar() {
        if (!getCaller().isSad) {
            String objeto = getObjeto();
            if (objeto.equals("%")) {
                objeto = getCaller().jInputText.getText().replace(getCognicao().getParametro(), "");
                CTRAudio.playExternal(objeto + ".wav");
            } else if (objeto.contains("?")) {
                objeto = getObjeto().replace("?", "");
                CTRAudio.playExternal(objeto + ".wav");
                System.out.println(objeto);
            } else {
                CTRAudio.play("/mild/katyusha/system/sfx/" + getObjeto() + ".wav");
            }
            getCaller().jInputText.setText("");
        }
    }

}
