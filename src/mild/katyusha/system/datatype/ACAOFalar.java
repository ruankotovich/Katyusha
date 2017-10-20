/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mild.katyusha.system.datatype;

import java.awt.Color;
import mild.katyusha.system.build.TRDFala;

/**
 *
 * @author Dmitry
 */
public class ACAOFalar extends Acao {
    
    @Override
    public void aplicar() {
        String comando = getObjeto();
        
        if (comando.equals("%")) {
            comando = getCaller().jInputText.getText().replace(getCognicao().getParametro(), "");
        }
        if (comando.isEmpty()) {
            comando = "Falar o que, exatamente? :/";
        }
        
        getCaller().jInputText.setText("Interpretando...");
        getCaller().jInputText.setBackground(new Color(255, 103, 204));
        getCaller().jInputText.setEditable(false);
        if (!getCaller().isSad || comando.contains(":}")) {
            getCaller().jOutputText.setText("Katyusha : " + comando.replace("\\n", "\n"));
        } else {
            getCaller().jOutputText.setText("Katyusha : ...");
        }
        Thread thread = new Thread(new TRDFala(getCaller()));
        thread.start();
    }
    
}
