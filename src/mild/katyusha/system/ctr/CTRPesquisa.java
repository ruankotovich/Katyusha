/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mild.katyusha.system.ctr;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;

/**
 *
 * @author Dmitry
 */
public class CTRPesquisa {

    public String pesquisaPalavra(String urlSt) {
        try {
            System.err.println("[Procurando por "+urlSt+"]");
            String toSend = "http://www.dicionarioinformal.com.br/" + urlSt+"/";
            URL url = new URL(toSend);
            BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream(),"ISO-8859-1"));
            String linha = "";
            while ((linha = reader.readLine()) != null) {
                if (linha.contains("<p class=\"textoDefinicao\">") && linha.contains("</p>")) {
                    String saida = linha.replace("<p class=\"textoDefinicao\">", "").replace("</p>", "").replace("", "")
                            .replace("<strong>", "").replace("</strong>", "");
                    reader.close();
                    if(saida.length()<saida.indexOf(".")+1){
                    return saida.substring(11,saida.length());
                    }else{
                    return saida.substring(11,saida.indexOf(".")+1);
                    }
                }
            }
            reader.close();
        } catch (Exception ex) {
            System.out.println("Error " + ex);
        }
        return "$";
    }

}
