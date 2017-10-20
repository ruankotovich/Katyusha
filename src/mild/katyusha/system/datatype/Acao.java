/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mild.katyusha.system.datatype;

import mild.katyusha.system.window.WNDBundle;

/**
 *
 * @author Dmitry
 */
public abstract class Acao {

    private String objeto;
    private WNDBundle caller;
    private Cognicao cognicao;

    public Cognicao getCognicao() {
        return cognicao;
    }

    public void setCognicao(Cognicao chamador) {
        this.cognicao = chamador;
    }

    @Override
    public String toString() {
        return ("  |_Entidade I/O : " + objeto + "\n  |_Caller : " + caller);
    }

    public WNDBundle getCaller() {
        return caller;
    }

    public void setCaller(WNDBundle caller) {
        this.caller = caller;
    }

    public abstract void aplicar();

    public String getObjeto() {
        return objeto;
    }

    public void setObjeto(String objeto) {
        this.objeto = objeto;
    }

}
