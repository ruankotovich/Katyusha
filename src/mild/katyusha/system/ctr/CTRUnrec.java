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
import java.util.ArrayList;
import javax.swing.JOptionPane;
import mild.katyusha.system.datatype.Irreconhecido;

/**
 *
 * @author Dmitry
 */
public class CTRUnrec {

    public static void addToUnrec(String comando, ArrayList<Irreconhecido> unrecs) {
        Irreconhecido irreconhecido = new Irreconhecido(comando, 1);
        int indexList = irreconhecido.indexOnList(unrecs);
        if (indexList >= 0) {
            Irreconhecido found = unrecs.get(indexList);
            unrecs.remove(indexList);
            found.setRepeticao(found.getRepeticao() + 1);
            unrecs.add(indexList, found);
        } else {
            unrecs.add(irreconhecido);
        }
        System.err.println("[Updating Unrecognizeds...]" + unrecs);
        CTRUnrec.printUnrecs(unrecs);
    }

    public static void removeUnrec(Irreconhecido as, ArrayList<Irreconhecido> unrecs) {
        int indexList = as.indexOnList(unrecs);
        System.out.println("[Removing] " + as);
        if (indexList >= 0) {
            unrecs.remove(indexList);
        }
        CTRUnrec.printUnrecs(unrecs);
    }

    public static void printUnrecs(ArrayList<Irreconhecido> unrecs) {
        String data = "unrecognized";
        try {
            Writer writer = new OutputStreamWriter(new FileOutputStream(data + ".k"), "UTF-8");

            PrintWriter printWriter = new PrintWriter(writer);
            unrecs.stream().forEach((as) -> {
                printWriter.println(as);
            });
            printWriter.close();

        } catch (FileNotFoundException ex) {
            JOptionPane.showMessageDialog(null, "Unacessed Area!", "Error in Area", JOptionPane.ERROR_MESSAGE);
        } catch (UnsupportedEncodingException ex) {
            JOptionPane.showMessageDialog(null, "Unacessed Area!", "Error in Area", JOptionPane.ERROR_MESSAGE);
        }
    }
}
