/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mild.katyusha.system.datatype;

import java.util.ArrayList;

/**
 *
 * @author Dmitry
 */
public class Irreconhecido {

    String sentenca;
    int repeticao;

    @Override
    public String toString() {
        return "unrec<-"+ getSentenca()+"<#>"+getRepeticao()+"->";
    }
    
    
    
    public Irreconhecido(String sentenca, int repeticao) {
        this.sentenca = sentenca;
        this.repeticao = repeticao;
    }

    public Irreconhecido() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public String getSentenca() {
        return sentenca;
    }

    public void setSentenca(String sentenca) {
        this.sentenca = sentenca;
    }

    public int getRepeticao() {
        return repeticao;
    }

    public void setRepeticao(int repeticao) {
        this.repeticao = repeticao;
    }

    public boolean igual(String comop) {
        return comop.equals(sentenca);
    }

    public int indexOnList(ArrayList<Irreconhecido> array) {
        int indexOn = -1;
        for (int i = 0; i < array.size(); i++) {
            if (array.get(i).igual(this.sentenca)) {
                indexOn = i;
            }
        }
        return indexOn;
    }
}
