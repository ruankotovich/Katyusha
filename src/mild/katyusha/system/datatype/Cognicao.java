/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mild.katyusha.system.datatype;

import java.util.List;

/**
 *
 * @author Dmitry
 */
public class Cognicao {

    private String parametro;
    private boolean caseSensitive;
    private List<Acao> acoes;

    @Override
    public String toString() {
        return parametro + " - " + acoes;
    }

    public String getParametro() {
        return parametro;
    }

    public boolean isCaseSensitive() {
        return caseSensitive;
    }

    public void setCaseSensitive(boolean caseSensitive) {
        this.caseSensitive = caseSensitive;
    }

    
    
    public void setParametro(String parametro) {
        this.parametro = parametro;
    }

    public List<Acao> getAcoes() {
        return acoes;
    }

    public void setAcoes(List<Acao> acoes) {
        this.acoes = acoes;
    }

}
