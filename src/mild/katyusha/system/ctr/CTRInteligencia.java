/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mild.katyusha.system.ctr;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import mild.katyusha.system.datatype.ACAOAudio;
import mild.katyusha.system.datatype.ACAOEvento;
import mild.katyusha.system.datatype.ACAOExecutar;
import mild.katyusha.system.datatype.ACAOFalar;
import mild.katyusha.system.datatype.ACAOPesquisar;
import mild.katyusha.system.datatype.Acao;
import mild.katyusha.system.datatype.Cognicao;
import mild.katyusha.system.datatype.Irreconhecido;
import mild.katyusha.system.datatype.Twig;
import mild.katyusha.system.window.WNDBundle;

/**
 *
 * @author Dmitry
 */
public class CTRInteligencia {

    public ArrayList<Cognicao> contidos = new ArrayList<>();
    public ArrayList<Cognicao> exatamente = new ArrayList<>();
    public ArrayList<Irreconhecido> unrecs = new ArrayList<>();
    public Irreconhecido toAdd;
    public String pergunta = "!";

    WNDBundle caller;

    public static void addTwigs(ArrayList<Twig> twigs) {

        try {
            Reader reader = new InputStreamReader(new FileInputStream("twigs.k"), "UTF-8");
            BufferedReader bufferedReader = new BufferedReader(reader);
            while (bufferedReader.ready()) {
                String linhaAtual = bufferedReader.readLine();
                if (linhaAtual.contains("twig<-") && linhaAtual.contains("->") && !(linhaAtual.contains("!{") && linhaAtual.contains("}!"))) {
                    linhaAtual = linhaAtual.replace("twig<-", "").replace("->", "");
                    String parametros[] = linhaAtual.split("<#>");
                    if (parametros.length >= 4) {
                        int pegar = Integer.parseInt(parametros[0].replace("<#>", ""));
                        if (pegar <= 6) {
                            String comando = parametros[2].replace("<#>", "");
                            String label = parametros[3].replace("<#>", "");
                            String icone = parametros[1].replace("<#>", "");
                            (twigs.get(pegar)).setIcon(new ImageIcon(icone));
                            (twigs.get(pegar)).setComando(comando);
                            (twigs.get(pegar)).setLabel(label);
                        }
                    }
                }
            }
        } catch (FileNotFoundException ex) {
            String[] a = {"FileNotFoundException - twigs.k", "Veja se o arquivo esta na pasta do programa", ex.getMessage()};
            printTrace(a);
        } catch (IOException ex) {
            String[] a = {"IOException - twigs.k", "Provavelmente o arquivo está em uso ou oculto", ex.getMessage()};
            printTrace(a);
        } catch (Exception ex) {
            String[] a = {"FormatException - twigs.k", "Há algo errado com seu código", ex.getMessage()};
            printTrace(a);
        }

    }

    public static void printTrace(String str[]) {
        String data = "StackTrace_" + new SimpleDateFormat("ddMMyyyy_HHmmss").format(new Date());
        try {
            Writer writer = new OutputStreamWriter(new FileOutputStream(data + ".klog"), "UTF-8");
            PrintWriter printWriter = new PrintWriter(writer);
            for (String str1 : str) {
                printWriter.println(str1);
            }
            printWriter.close();
        } catch (FileNotFoundException ex) {
            JOptionPane.showMessageDialog(null, "Unacessed Area!", "Error in Area", JOptionPane.ERROR_MESSAGE);
        } catch (UnsupportedEncodingException ex) {
            JOptionPane.showMessageDialog(null, "Unacessed Area!", "Error in Area", JOptionPane.ERROR_MESSAGE);
        }
    }

    public CTRInteligencia(WNDBundle calling) {
        this.caller = calling;
        reiniciaCognicao();
    }

    private void reiniciaCognicao() {
        caller.jInputText.setEnabled(false);
        caller.kLoad.setVisible(true);
        new Thread(() -> {
            caller.jLoadBar.setValue(23);
            this.contidos = new ArrayList<>();
            this.exatamente = new ArrayList<>();
            this.unrecs = new ArrayList<>();
            this.toAdd = null;
            this.lerControle("actions");
            caller.jLoadBar.setValue(33);
            this.lerControle("secondary_actions");
            caller.jLoadBar.setValue(73);
            this.lerIrreconhecidos();
            caller.jLoadBar.setValue(91);
            caller.jLoadBar.setValue(99);
            caller.jInputText.setEnabled(true);
            caller.kLoad.setVisible(false);
            caller.setIsReady(true);
            caller.setVisible(true);
            caller.pointOnTextField();
            caller.repaint();
        }).start();

    }

    private void lerIrreconhecidos() {
        Reader reader = null;
        System.err.println(" [Executando Módulo de Irreconhecimentos]");
        try {
            reader = new InputStreamReader(new FileInputStream("unrecognized.k"), "UTF-8");
            BufferedReader bufferedReader = new BufferedReader(reader);
            while (bufferedReader.ready()) {
                String atual[] = bufferedReader.readLine().split("<#>");
                Irreconhecido irreconhecido = new Irreconhecido(atual[0].replace("<#>", "").replace("unrec<-", ""), Integer.parseInt(atual[1].replace("<#>", "").replace("->", "")));
                unrecs.add(irreconhecido);
                System.err.println("  |_Entidade : " + irreconhecido.getSentenca() + " | Repetições : " + irreconhecido.getRepeticao());
            }
            bufferedReader.close();
        } catch (FileNotFoundException | UnsupportedEncodingException ex) {
            String[] a = {"FileNotFoundException - unrecognized.k", "Veja se o arquivo esta na pasta do programa", ex.getMessage()};
            printTrace(a);
        } catch (IOException ex) {
            String[] a = {"IOException - unrecognized.k", "Provavelmente o arquivo está em uso ou oculto", ex.getMessage()};
            printTrace(a);
        }
    }

    private String trocarAcentos(String texto) {
        String retorno = texto;
        try {
            Reader reader = new InputStreamReader(new FileInputStream("determinations.k"), "UTF-8");
            BufferedReader bufferedReader = new BufferedReader(reader);
            while (bufferedReader.ready()) {
                String linha = bufferedReader.readLine();
                if (!(linha.contains("!{") && linha.contains("}!")) || !(linha.replace(" ", "").isEmpty())) {
                    if (linha.contains("alias<-") && linha.contains("->")) {
                        String quebrar[] = (linha.replace("alias<-", "").replace("->", "")).split(" ");
                        if (quebrar[0] != null && quebrar[1] != null) {
                            retorno = retorno.replace(quebrar[0], quebrar[1]);
                        }
                    } else if (linha.contains("bind<-") && linha.contains("->")) {
                        String quebrar[] = (linha.replace("bind<-", "").replace("->", "")).split("<#>");
                        if (quebrar[0] != null && quebrar[1] != null) {
                            retorno = retorno.replace(quebrar[0], quebrar[1].replace("<#>", ""));
                        }
                    }
                }
            }
            bufferedReader.close();
        } catch (FileNotFoundException | UnsupportedEncodingException ex) {
            String[] a = {"FileNotFoundException - determinations.k", "Veja se o arquivo esta na pasta do programa", ex.getMessage()};
            printTrace(a);
        } catch (IOException ex) {
            String[] a = {"IOException - determinations.k", "Provavelmente o arquivo está em uso ou oculto", ex.getMessage()};
            printTrace(a);
        }
        return retorno;

    }

    private void lerControle(String arquivo) {
        try {
            Reader reader = new InputStreamReader(new FileInputStream(arquivo + ".k"), "UTF-8");
            try (BufferedReader bufferedReader = new BufferedReader(reader)) {
                System.err.println("Carregando interpretações...\n");
                while (bufferedReader.ready()) {
                    setCognicao(bufferedReader.readLine());
                }
                bufferedReader.close();
            }
            System.err.println("\nInterpretações carragadas!");
        } catch (FileNotFoundException ex) {
            String[] a = {"FileNotFoundException - actions.k", "Veja se o arquivo esta na pasta do programa", ex.getMessage()};
            printTrace(a);
        } catch (IOException ex) {
            String[] a = {"IOException - actions.k", "Provavelmente o arquivo está em uso ou oculto", ex.getMessage()};
            printTrace(a);
        }
    }

    private void setCognicao(String evento) {
        if (!(evento.contains("!{") && evento.contains("}!")) || !(evento.replace(" ", "").isEmpty())) {
            String[] verificador = trocarAcentos(evento).split("<#>");
            if (verificador[0] != null) {

                if (verificador[0].contains("{!contem->}")) {
                    Cognicao cognicao = new Cognicao();
                    cognicao.setCaseSensitive(true);
                    cognicao.setParametro(verificador[0].replace("{!contem->}", ""));
                    System.err.println("\n+['EVENTO DE PERSISTÊNCIA Binded'] > lendo interpretação... | ativada quando contem '" + cognicao.getParametro() + "'");
                    cognicao.setAcoes(getAcoes(cognicao, verificador));
                    contidos.add(cognicao);

                } else if (verificador[0].contains("{?contem->}")) {
                    Cognicao cognicao = new Cognicao();
                    cognicao.setCaseSensitive(false);
                    cognicao.setParametro(verificador[0].replace("{?contem->}", ""));
                    System.err.println("\n+['EVENTO DE PERSISTÊNCIA Optional'] > lendo interpretação... | ativada quando contem '" + cognicao.getParametro() + "'");
                    cognicao.setAcoes(getAcoes(cognicao, verificador));
                    contidos.add(cognicao);
                } else if (verificador[0].contains("{!exatamente->}")) {
                    Cognicao cognicao = new Cognicao();
                    cognicao.setCaseSensitive(true);
                    cognicao.setParametro(verificador[0].replace("{!exatamente->}", ""));
                    System.err.println("\n+['EVENTO DE PRECISÃO Binded'] > lendo interpretação... | ativada quando exatamente '" + cognicao.getParametro() + "'");
                    cognicao.setAcoes(getAcoes(cognicao, verificador));
                    exatamente.add(cognicao);
                } else if (verificador[0].contains("{?exatamente->}")) {
                    Cognicao cognicao = new Cognicao();
                    cognicao.setCaseSensitive(false);
                    cognicao.setParametro(verificador[0].replace("{?exatamente->}", ""));
                    System.err.println("\n+['EVENTO DE PRECISÃO Option'] > lendo interpretação... | ativada quando exatamente '" + cognicao.getParametro() + "'");
                    cognicao.setAcoes(getAcoes(cognicao, verificador));
                    exatamente.add(cognicao);
                } else if (verificador[0].contains("$_SCONTROL=")) {
                    String scontrol = verificador[0].replace("$_SCONTROL=", "");
                    System.out.println("[$_SCONTROL=" + scontrol + "]");
                    this.pergunta = scontrol + " ";
                }
            }
        }
    }

    private List<Acao> getAcoes(Cognicao cognicao, String str[]) {
        try {
            ArrayList<Acao> acoes = new ArrayList<>();
            if (str[1] != null) {
                String[] comandos = str[1].replace("<#>", "").split("<A>");

                for (String comando : comandos) {
                    if (comando != null) {
                        String comandoAtual = comando.replace("<A>", "");
                        if (comandoAtual.contains("{fale->}")) {
                            ACAOFalar acaoFalar = new ACAOFalar();
                            acaoFalar.setObjeto(comandoAtual.replace("{fale->}", ""));
                            acaoFalar.setCaller(caller);
                            acaoFalar.setCognicao(cognicao);
                            acoes.add(acaoFalar);
                            System.err.println(" |_ Falar : " + acaoFalar);
                        } else if (comandoAtual.contains("{execute->}")) {
                            ACAOExecutar acaoExecutar = new ACAOExecutar();
                            acaoExecutar.setObjeto(comandoAtual.replace("{execute->}", ""));
                            acaoExecutar.setCaller(caller);
                            acaoExecutar.setCognicao(cognicao);
                            acoes.add(acaoExecutar);
                            System.err.println(" |_ Executar : " + acaoExecutar);
                        } else if (comandoAtual.contains("{pesquise->}")) {
                            ACAOPesquisar acaoPesquisar = new ACAOPesquisar();
                            acaoPesquisar.setObjeto(comandoAtual.replace("{pesquise->}", ""));
                            acaoPesquisar.setCaller(caller);
                            acaoPesquisar.setCognicao(cognicao);
                            acoes.add(acaoPesquisar);
                            System.err.println(" |_ Pesquisar : " + acaoPesquisar);
                        } else if (comandoAtual.contains("{audio->}")) {
                            ACAOAudio acaoAudio = new ACAOAudio();
                            acaoAudio.setObjeto(comandoAtual.replace("{audio->}", ""));
                            acaoAudio.setCaller(caller);
                            acaoAudio.setCognicao(cognicao);
                            acoes.add(acaoAudio);
                            System.err.println(" |_ Audio : " + acaoAudio);
                        } else if (comandoAtual.contains("{sistema->}")) {
                            ACAOEvento acaoEvento = new ACAOEvento();
                            acaoEvento.setObjeto(comandoAtual.replace("{sistema->}", ""));
                            acaoEvento.setCognicao(cognicao);
                            acaoEvento.setCaller(caller);
                            acoes.add(acaoEvento);
                            System.err.println(" |_ Evento de Sistema : " + acaoEvento);
                        }
                    }
                }
                return acoes;
            } else {
                return null;
            }
        } catch (Exception ex) {
            String[] a = {"CodificationException - actions.k", "Alguma linha do codigo está errada!", ex.getMessage()};
            printTrace(a);
            return null;
        }

    }

    private boolean interpretaExatamente(String str) {
        boolean executou = false;
        for (Cognicao c : exatamente) {
            if (c.isCaseSensitive()) {
                if (c.getParametro().equals(str)) {
                    System.out.println(" + Ação exata ");
                    executarCognicao(c);
                    executou = true;
                    toAdd = null;
                    break;
                }
            } else {
                if (c.getParametro().toUpperCase().equals(str.toUpperCase())) {
                    System.out.println(" + Ação exata ");
                    executarCognicao(c);
                    executou = true;
                    toAdd = null;
                    break;
                }
            }
        }
        return executou;
    }

    private boolean interpretarPertinencia(String str) {
        boolean executou = false;
        for (Cognicao c : contidos) {
            if (c.isCaseSensitive()) {
                if (str.contains(c.getParametro())) {
                    System.out.println(" + Ação pertinente ");
                    executarCognicao(c);
                    toAdd = null;
                    executou = true;
                    break;
                }
            } else {
                if (str.toLowerCase().contains(c.getParametro().toLowerCase())) {
                    System.out.println(" + Ação pertinente ");
                    executarCognicao(c);
                    executou = true;
                    toAdd = null;
                    break;
                }
            }
        }
        return executou;
    }

    public void executarCognicao(Cognicao cog) {
        for (Acao ac : cog.getAcoes()) {
            System.err.println(" [Executando interpretação] \n" + ac);
            ac.aplicar();
        }
    }

    public void falarAlgo(String falar) {
        List<Acao> action = new ArrayList<>();
        ACAOFalar acaoFalar = new ACAOFalar();
        acaoFalar.setCaller(caller);
        acaoFalar.setObjeto(falar);
        action.add(acaoFalar);
        Cognicao cognicao = new Cognicao();
        cognicao.setAcoes(action);
        executarCognicao(cognicao);
    }

    public void enviarComando(String comando) {
        String com = "";
        try {
            if (toAdd != null) {
                com = new CTRAprendizado().aprenderCognicao(comando, toAdd.getSentenca());
            } else {
                com = null;
            }
        } catch (Exception ex) {
            com = null;
        }

        if (comando.replace(" ", "").isEmpty()) {
            falarAlgo("Fale comigo :/");
        } else {
            if (com != null) {
                new CTRAprendizado().printRec(com);
                CTRUnrec.removeUnrec(toAdd, unrecs);
                falarAlgo("Aaaaaah, okay! :D");
                reiniciaCognicao();
                try {
                    this.finalize();
                } catch (Throwable ex) {
                    Logger.getLogger(CTRInteligencia.class.getName()).log(Level.SEVERE, null, ex);
                }
            } else {
                if (!interpretaExatamente(comando)) {
                    if (!interpretarPertinencia(comando)) {
                        String pesquisa = "#";
                        if (comando.length() > pergunta.length()) {
                            if (comando.substring(0, pergunta.length()).contains(pergunta)) {
                                pesquisa = new CTRPesquisa().pesquisaPalavra(comando.replace(pergunta, "").replace("?", ""));
                            }
                        }
                        System.out.println(pesquisa);

                        if (pesquisa.equals("#")) {
                            Irreconhecido irreconhecido = new Irreconhecido(comando, 1);
                            int index = irreconhecido.indexOnList(unrecs);

                            if (index >= 0) {
                                Irreconhecido atual = unrecs.get(index);
                                switch (atual.getRepeticao()) {
                                    case 1:
                                        falarAlgo("Ainda não entendi, o que significa? :/");
                                        toAdd = atual;
                                        break;
                                    case 2:
                                        falarAlgo("Continuo sem entender isso, o que significa? :/");
                                        toAdd = atual;
                                        break;
                                    case 3:
                                        falarAlgo("Você já me falou isso e eu não entendi, o que significa? :/");
                                        toAdd = atual;
                                        break;
                                    default:
                                        String ints = "";
                                        for (int i = 0; i < atual.getRepeticao() - 4; i++) {
                                            ints = ints.concat("?");
                                        }
                                        falarAlgo("Não entendi, o que significa?" + ints + " :/");
                                        toAdd = atual;
                                        break;
                                }
                            } else {
                                falarAlgo("Não entendi :/");
                                toAdd = null;
                            }
                            CTRUnrec.addToUnrec(irreconhecido.getSentenca(), unrecs);
                        } else if (pesquisa.equals("$")) {
                            falarAlgo("Não sei o que é :/");
                        } else {
                            falarAlgo(pesquisa);
                        }
                    }
                }
            }
        }
    }

}
