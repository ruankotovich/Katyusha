/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mild.katyusha.system.ctr;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JOptionPane;
import mild.katyusha.system.datatype.Cognicao;

/**
 *
 * @author Dmitry
 */
public class CTRAprendizado {

    private final Map inicioPassagens;

    public CTRAprendizado() {
        inicioPassagens = new HashMap();
        inicioPassagens.put("quando exatamente, significa que", "?exatamente");
        inicioPassagens.put("quando exatamente,", "?exatamente");
        inicioPassagens.put("quando for exato,", "?exatamente");
        inicioPassagens.put("quando for exato, significa que", "?exatamente");
        inicioPassagens.put("sendo exato, significa que", "?exatamente");
        inicioPassagens.put("sendo exato,", "?exatamente");
        inicioPassagens.put("quando exato, significa que", "?exatamente");
        inicioPassagens.put("quando exato,", "?exatamente");
        inicioPassagens.put("quando literalmente for exato,", "!exatamente");
        inicioPassagens.put("quando literalmente for exato, significa que", "!exatamente");
        inicioPassagens.put("sendo literalmente exato, significa que", "!exatamente");
        inicioPassagens.put("sendo literalmente exato,", "!exatamente");
        inicioPassagens.put("quando literalmente exato, significa que", "!exatamente");
        inicioPassagens.put("quando literalmente exato,", "!exatamente");

        inicioPassagens.put("quando contém, significa que", "?contem");
        inicioPassagens.put("quando contém,", "?contem");
        inicioPassagens.put("quando conter, significa que", "?contem");
        inicioPassagens.put("quando conter,", "?contem");
        inicioPassagens.put("contendo, significa que", "?contem");
        inicioPassagens.put("contendo,", "?contem");

        inicioPassagens.put("quando literalmente contém, significa que", "!contem");
        inicioPassagens.put("quando literalmente contém,", "!contem");
        inicioPassagens.put("quando literalmente conter, significa que", "!contem");
        inicioPassagens.put("quando literalmente conter,", "!contem");
        inicioPassagens.put("contendo literalmente, significa que", "!contem");
        inicioPassagens.put("contendo literalmente,", "!contem");
    }

    public void printRec(String rec) {
        String data = "secondary_actions";
        try {
            Writer writer = new OutputStreamWriter(new FileOutputStream(data + ".k", true), "UTF-8");
            try (PrintWriter printWriter = new PrintWriter(writer)) {
                printWriter.println(rec);
                printWriter.close();
            }
        } catch (FileNotFoundException ex) {
            JOptionPane.showMessageDialog(null, "Unacessed Area!", "Error in Area", JOptionPane.ERROR_MESSAGE);
        } catch (UnsupportedEncodingException ex) {
            JOptionPane.showMessageDialog(null, "Unacessed Area!", "Error in Area", JOptionPane.ERROR_MESSAGE);
        }
    }

    public String aprenderCognicao(String passagem, String comando) {
        Cognicao cognicao = new Cognicao();
        String quebraFrase[] = passagem.split(" você deverá ");
        if (quebraFrase.length > 0 && comando.length() > 0) {
            if (inicioPassagens.containsKey(quebraFrase[0])) {
                String initialStat = "{" + inicioPassagens.get(quebraFrase[0]) + "->}" + comando + "<#>";
                String endStat = requerirAcoes(quebraFrase[1]);
                if (endStat.length() > 0) {
                    return initialStat.concat(endStat);
                } else {
                    return null;
                }
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    private String requerirAcoes(String acoes) {
        String retorno = "";
        String separar[] = acoes.split(" e ");
        if (separar.length > 0) {
            for (String sep : separar) {
                if (sep.contains("falar ")) {
                    //System.out.print("<A>{fale->}" + sep.replace("falar ", ""));
                    retorno = retorno.concat("<A>{fale->}" + sep.replace("falar ", ""));
                } else if (sep.contains("executar ")) {
                    //System.out.print("<A>{execute->}" + sep.replace("executar ", ""));
                    retorno = retorno.concat("<A>{execute->}" + sep.replace("executar ", ""));
                } else if (sep.contains("pesquisar ")) {
                    //System.out.print("<A>{pesquise->}" + sep.replace("pesquisar ", ""));
                    retorno = retorno.concat("<A>{pesquise->}" + sep.replace("pesquisar ", ""));
                } else if (sep.contains("reproduzir ")) {
                    //System.out.print("<A>{audio->}" + sep.replace("reproduzir ", ""));
                    retorno = retorno.concat("<A>{audio->}" + sep.replace("reproduzir ", ""));
                }
            }
            System.out.println();
            return retorno;
        } else {
            return null;
        }

    }
}
