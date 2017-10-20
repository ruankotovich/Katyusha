/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mild.katyusha.system.window;

import java.awt.Color;
import java.awt.Component;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.nio.channels.FileChannel;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;
import mild.katyusha.system.ctr.CTRInteligencia;

/**
 *
 * @author Dmitry
 */
public final class WNDBackups extends JDialog {

    /**
     * Creates new form WNDCerebro
     */
    DefaultTableModel modelo;

    private final String primeiraC = "|contem|exatamente|fale|pesquise|audio|execute|sistema|";
    private final String segundaC = "|!|-|A|";
    private final String terceiraC = "|twig|bind|alias|";
    private boolean loading = false;
    WNDBundle caller;

    private WNDBackups() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private void addTexto(File arquivoOb) {
        Reader reader = null;
        jTextPane1.setText("");
        try {
            reader = new InputStreamReader(new FileInputStream(arquivoOb), "UTF-8");
            BufferedReader bufferedReader = new BufferedReader(reader);
            int count = 0;
            while (bufferedReader.ready()) {
                count++;
                String a = bufferedReader.readLine();

                jTextPane1.setText(jTextPane1.getText() + (count > 1 ? "\n" : "") + a);
                jTextPane1.setCaretPosition(jTextPane1.getDocument().getLength());
                jTextPane1.repaint();
            }
        } catch (UnsupportedEncodingException | FileNotFoundException ex) {
            JOptionPane.showMessageDialog(null, "Erro : arquivo não encontrado!", "Erro ao abrir arquivo", JOptionPane.ERROR_MESSAGE);
            String[] a = {"Arquivo não encontrado", "Veja se o arquivo esta na pasta backup", ex.getMessage()};
            CTRInteligencia.printTrace(a);
        } catch (IOException ex) {
            Logger.getLogger(WNDBackups.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                reader.close();
            } catch (IOException ex) {
                Logger.getLogger(WNDBackups.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public static void copyFile(File source, File destination) throws IOException {
        if (destination.exists()) {
            destination.delete();
        }

        FileChannel sourceChannel = null;
        FileChannel destinationChannel = null;

        try {
            sourceChannel = new FileInputStream(source).getChannel();
            destinationChannel = new FileOutputStream(destination).getChannel();
            sourceChannel.transferTo(0, sourceChannel.size(),
                    destinationChannel);
        } finally {
            if (sourceChannel != null && sourceChannel.isOpen()) {
                sourceChannel.close();
            }
            if (destinationChannel != null && destinationChannel.isOpen()) {
                destinationChannel.close();
            }
        }
    }

    public WNDBackups(WNDBundle calling) {
        initComponents();
        this.caller = calling;
        modelo = (DefaultTableModel) jTable1.getModel();
        this.setTitle(jLbTitulo.getText());
        addStyle();
        this.setIconImage(new ImageIcon(this.getClass().getResource("/mild/katyusha/system/gfx/cerebro/icone_cerebro.png")).getImage());
        this.setTitle("Editor de interpretações");
        this.setLocationRelativeTo(null);
        this.dispose();
        this.setUndecorated(true);
        this.setVisible(true);
        listFiles();
        pintarTabela();
        repaint();
    }

    private void pintarTabela() {
        jTextPane1.setSize(jPanel9.getSize());
        jTable1.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {

            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                super.getTableCellRendererComponent(table, value, isSelected,
                        hasFocus, row, column);
                if (String.valueOf(modelo.getValueAt(row, 0)).contains("$backup")) {
                    setBackground(Color.CYAN);
                } else if (String.valueOf(modelo.getValueAt(row, 0)).contains("$presave")) {
                    setBackground(Color.LIGHT_GRAY);
                } else if (String.valueOf(modelo.getValueAt(row, 0)).contains("$cancel")) {
                    setBackground(Color.RED);
                } else {
                    setBackground(Color.PINK);
                }
                return this;
            }
        });
    }

    private void listFiles() {
        for (File arquivo : new File("backup").listFiles()) {
            String nomeArquivo = arquivo.getName();
            modelo.addRow(new Object[]{nomeArquivo});
        }
    }

    public void addStyle() {

        final StyleContext cont = StyleContext.getDefaultStyleContext();
        final AttributeSet rosa = cont.addAttribute(cont.getEmptySet(), StyleConstants.Foreground, new Color(255, 0, 255));
        final AttributeSet verde = cont.addAttribute(cont.getEmptySet(), StyleConstants.Foreground, new Color(40, 255, 60));
        final AttributeSet roxo = cont.addAttribute(cont.getEmptySet(), StyleConstants.Foreground, new Color(128, 0, 128));
        final AttributeSet normal = cont.addAttribute(cont.getEmptySet(), StyleConstants.Foreground, Color.black);
        DefaultStyledDocument doc;
        doc = new DefaultStyledDocument() {
            @Override
            public void insertString(int offset, String str, AttributeSet a) throws BadLocationException {
                super.insertString(offset, str, a);

                String text = getText(0, getLength());
                int antes = ultimoSimbolo(text, offset);
                if (antes < 0) {
                    antes = 0;
                }
                int depois = primeiroSimbolo(text, offset + str.length());
                int palavraEsquerda = antes;
                int palavraDireita = antes;

                while (palavraDireita <= depois) {
                    if (palavraDireita == depois || String.valueOf(text.charAt(palavraDireita)).matches("\\W")) {
                        if (text.substring(palavraEsquerda, palavraDireita).matches("(\\W)*(" + primeiraC + ")")) {
                            setCharacterAttributes(palavraEsquerda, palavraDireita - palavraEsquerda, roxo, false);
                        } else if (text.substring(palavraEsquerda, palavraDireita).matches("(\\W)*(" + segundaC + ")")) {
                            setCharacterAttributes(palavraEsquerda, palavraDireita - palavraEsquerda, verde, false);
                        } else if (text.substring(palavraEsquerda, palavraDireita).matches("(\\W)*(" + terceiraC + ")")) {
                            setCharacterAttributes(palavraEsquerda, palavraDireita - palavraEsquerda, rosa, false);
                        } else {
                            setCharacterAttributes(palavraEsquerda, palavraDireita - palavraEsquerda, normal, false);
                        }
                        palavraEsquerda = palavraDireita;
                    }
                    palavraDireita++;
                }
            }

            @Override
            public void remove(int offs, int len) throws BadLocationException {
                super.remove(offs, len);

                String text = getText(0, getLength());
                int antes = ultimoSimbolo(text, offs);
                if (antes < 0) {
                    antes = 0;
                }
                int depois = primeiroSimbolo(text, offs);

                if (text.substring(antes, depois).matches("(\\W)*(" + primeiraC + ")")) {
                    setCharacterAttributes(antes, depois - antes, roxo, false);
                } else if (text.substring(antes, depois).matches("(\\W)*(" + segundaC + ")")) {
                    setCharacterAttributes(antes, depois - antes, verde, false);
                } else if (text.substring(antes, depois).matches("(\\W)*(" + terceiraC + ")")) {
                    setCharacterAttributes(antes, depois - antes, rosa, false);
                } else {
                    setCharacterAttributes(antes, depois - antes, normal, false);
                }
            }
        };
        jTextPane1.setDocument(doc);
    }

    private int ultimoSimbolo(String text, int index) {
        while (--index >= 0) {
            if (String.valueOf(text.charAt(index)).matches("\\W")) {
                break;
            }
        }
        return index;
    }

    private int primeiroSimbolo(String text, int index) {
        while (index < text.length()) {
            if (String.valueOf(text.charAt(index)).matches("\\W")) {
                break;
            }
            index++;
        }
        return index;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jLbFechar = new javax.swing.JLabel();
        jLbTitulo = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jPanel9 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTextPane1 = new javax.swing.JTextPane();
        jLabel7 = new javax.swing.JLabel();
        jPload = new javax.swing.JPanel();
        jLbLoad = new javax.swing.JLabel();
        jPanel8 = new javax.swing.JPanel();
        jLbSalvar = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jPanel6 = new javax.swing.JPanel();
        jButton8 = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLbDetermination = new javax.swing.JLabel();
        jLbAction = new javax.swing.JLabel();
        jLbTwig = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(102, 0, 0));
        jPanel1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(102, 51, 0)));

        jPanel3.setBackground(new java.awt.Color(255, 51, 51));

        jLbFechar.setBackground(new java.awt.Color(153, 0, 0));
        jLbFechar.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLbFechar.setForeground(new java.awt.Color(255, 255, 255));
        jLbFechar.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLbFechar.setText("X");
        jLbFechar.setOpaque(true);
        jLbFechar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLbFecharMouseClicked(evt);
            }
        });

        jLbTitulo.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLbTitulo.setForeground(new java.awt.Color(255, 255, 255));
        jLbTitulo.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLbTitulo.setText("Seletor de Cognições");

        jLabel20.setIcon(new javax.swing.ImageIcon(getClass().getResource("/mild/katyusha/system/gfx/cerebro/icone_cerebro.png"))); // NOI18N

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addComponent(jLabel20)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLbTitulo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLbFechar, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jLbFechar, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
            .addComponent(jLbTitulo, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jLabel20, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        jPanel2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255), 3));
        jPanel2.setOpaque(false);

        jPanel9.setBackground(new java.awt.Color(255, 51, 102));

        jScrollPane1.setAutoscrolls(true);
        jScrollPane1.setFocusable(false);
        jScrollPane1.setRequestFocusEnabled(false);
        jScrollPane1.setVerifyInputWhenFocusTarget(false);

        jTextPane1.setBorder(null);
        jTextPane1.setFont(new java.awt.Font("Tahoma", 1, 9)); // NOI18N
        jTextPane1.setFocusable(false);
        jTextPane1.setRequestFocusEnabled(false);
        jTextPane1.setSelectedTextColor(new java.awt.Color(0, 0, 0));
        jTextPane1.setSelectionColor(new java.awt.Color(204, 204, 0));
        jTextPane1.setVerifyInputWhenFocusTarget(false);
        jTextPane1.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseDragged(java.awt.event.MouseEvent evt) {
                jTextPane1MouseDragged(evt);
            }
        });
        jTextPane1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextPane1KeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jTextPane1KeyTyped(evt);
            }
        });
        jScrollPane1.setViewportView(jTextPane1);

        jLabel7.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(255, 255, 255));
        jLabel7.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel7.setText("Vizualização");

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1)
                    .addComponent(jLabel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 279, Short.MAX_VALUE)
                .addContainerGap())
        );

        jPload.setBackground(new java.awt.Color(255, 255, 255));
        jPload.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(255, 51, 102), 10, true));
        jPload.setOpaque(false);

        jLbLoad.setBackground(new java.awt.Color(255, 51, 102));
        jLbLoad.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLbLoad.setIcon(new javax.swing.ImageIcon(getClass().getResource("/mild/katyusha/system/gfx/charged.gif"))); // NOI18N
        jLbLoad.setOpaque(true);

        javax.swing.GroupLayout jPloadLayout = new javax.swing.GroupLayout(jPload);
        jPload.setLayout(jPloadLayout);
        jPloadLayout.setHorizontalGroup(
            jPloadLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLbLoad, javax.swing.GroupLayout.DEFAULT_SIZE, 106, Short.MAX_VALUE)
        );
        jPloadLayout.setVerticalGroup(
            jPloadLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLbLoad, javax.swing.GroupLayout.DEFAULT_SIZE, 93, Short.MAX_VALUE)
        );

        jPanel8.setOpaque(false);

        jLbSalvar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/mild/katyusha/system/gfx/salvar.png"))); // NOI18N
        jLbSalvar.setEnabled(false);
        jLbSalvar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLbSalvarMouseClicked(evt);
            }
        });

        jLabel19.setIcon(new javax.swing.ImageIcon(getClass().getResource("/mild/katyusha/system/gfx/cancelar.png"))); // NOI18N
        jLabel19.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel19MouseClicked(evt);
            }
        });

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLbSalvar)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 77, Short.MAX_VALUE)
                .addComponent(jLabel19)
                .addContainerGap())
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLbSalvar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel19, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(7, 7, 7)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(38, 38, 38)
                        .addComponent(jPload, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 47, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(8, 8, 8)
                .addComponent(jPanel9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 9, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addGap(0, 2, Short.MAX_VALUE)
                        .addComponent(jPload, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );

        jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255), 3), "Seletor", javax.swing.border.TitledBorder.LEFT, javax.swing.border.TitledBorder.TOP, new java.awt.Font("Tahoma", 1, 14), new java.awt.Color(255, 255, 255))); // NOI18N
        jPanel4.setOpaque(false);

        jScrollPane2.setBorder(null);

        jTable1.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Nome"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTable1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTable1MouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(jTable1);
        if (jTable1.getColumnModel().getColumnCount() > 0) {
            jTable1.getColumnModel().getColumn(0).setResizable(false);
        }

        jButton1.setBackground(new java.awt.Color(153, 0, 153));
        jButton1.setFont(new java.awt.Font("Tahoma", 1, 10)); // NOI18N
        jButton1.setForeground(new java.awt.Color(255, 255, 255));
        jButton1.setText("Selecionar como actions.k");
        jButton1.setBorderPainted(false);
        jButton1.setRequestFocusEnabled(false);
        jButton1.setVerifyInputWhenFocusTarget(false);
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setBackground(new java.awt.Color(153, 0, 153));
        jButton2.setFont(new java.awt.Font("Tahoma", 1, 10)); // NOI18N
        jButton2.setForeground(new java.awt.Color(255, 255, 255));
        jButton2.setText("Selecionar como determinations.k");
        jButton2.setBorderPainted(false);
        jButton2.setRequestFocusEnabled(false);
        jButton2.setVerifyInputWhenFocusTarget(false);
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jButton3.setBackground(new java.awt.Color(153, 0, 153));
        jButton3.setFont(new java.awt.Font("Tahoma", 1, 10)); // NOI18N
        jButton3.setForeground(new java.awt.Color(255, 255, 255));
        jButton3.setText("Selecionar como twigs.k");
        jButton3.setBorderPainted(false);
        jButton3.setRequestFocusEnabled(false);
        jButton3.setVerifyInputWhenFocusTarget(false);
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(jButton1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButton3, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButton2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 357, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton3)
                .addContainerGap())
        );

        jPanel6.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255), 3), "Configuração Atual", javax.swing.border.TitledBorder.LEFT, javax.swing.border.TitledBorder.TOP, new java.awt.Font("Tahoma", 1, 14), new java.awt.Color(255, 255, 255))); // NOI18N
        jPanel6.setOpaque(false);

        jButton8.setBackground(new java.awt.Color(153, 0, 153));
        jButton8.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jButton8.setForeground(new java.awt.Color(255, 255, 255));
        jButton8.setText("Resetar");
        jButton8.setBorderPainted(false);
        jButton8.setRequestFocusEnabled(false);
        jButton8.setVerifyInputWhenFocusTarget(false);
        jButton8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton8ActionPerformed(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("actions.k : ");

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("determinations.k :");

        jLabel3.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("twigs.k :");

        jLbDetermination.setFont(new java.awt.Font("Tahoma", 1, 10)); // NOI18N
        jLbDetermination.setForeground(new java.awt.Color(255, 255, 102));
        jLbDetermination.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLbDetermination.setText("Não Alterado");

        jLbAction.setFont(new java.awt.Font("Tahoma", 1, 10)); // NOI18N
        jLbAction.setForeground(new java.awt.Color(255, 255, 102));
        jLbAction.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLbAction.setText("Não Alterado");

        jLbTwig.setFont(new java.awt.Font("Tahoma", 1, 10)); // NOI18N
        jLbTwig.setForeground(new java.awt.Color(255, 255, 102));
        jLbTwig.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLbTwig.setText("Não Alterado");

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButton8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLbAction, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLbDetermination, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLbTwig, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel1)
                            .addComponent(jLabel2)
                            .addComponent(jLabel3))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLbAction)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel2)
                .addGap(5, 5, 5)
                .addComponent(jLbDetermination)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLbTwig)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton8, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jLbFecharMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLbFecharMouseClicked
        int saida = JOptionPane.showConfirmDialog(null, "Deseja fechar o seletor?");
        if (saida == JOptionPane.YES_OPTION) {
            if (jLbTitulo.getText().contains("[*]")) {
                int escolha = JOptionPane.showConfirmDialog(null, "Deseja salvar as alterações?\n\nSerá necessário reiniciar a Katyusha");
                if (escolha == JOptionPane.YES_OPTION) {
                    caller.restart();
                }
            }
            this.dispose();
        }
    }//GEN-LAST:event_jLbFecharMouseClicked

    private void jTextPane1KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextPane1KeyTyped

    }//GEN-LAST:event_jTextPane1KeyTyped

    private void jTextPane1KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextPane1KeyReleased

    }//GEN-LAST:event_jTextPane1KeyReleased

    private void jTextPane1MouseDragged(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTextPane1MouseDragged
    }//GEN-LAST:event_jTextPane1MouseDragged

    private void jLbSalvarMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLbSalvarMouseClicked
        if (jLbTitulo.getText().contains("[*]") && jLbSalvar.isEnabled()) {
            if (!jLbAction.getText().equals("Não Alterado")) {
                try {
                    copyFile(new File("backup/".concat(jLbAction.getText())), new File("actions.k"));
                } catch (IOException ex) {
                    String[] a = {"Erro ao copiar arquivo", ex.getMessage()};
                    CTRInteligencia.printTrace(a);
                }
            }
            if (!jLbDetermination.getText().equals("Não Alterado")) {
                try {
                    copyFile(new File("backup/".concat(jLbDetermination.getText())), new File("determinations.k"));
                } catch (IOException ex) {
                    String[] a = {"Erro ao copiar arquivo", ex.getMessage()};
                    CTRInteligencia.printTrace(a);
                }
            }
            if (!jLbTwig.getText().equals("Não Alterado")) {
                try {
                    copyFile(new File("backup/".concat(jLbTwig.getText())), new File("twigs.k"));
                } catch (IOException ex) {
                    String[] a = {"Erro ao copiar arquivo", ex.getMessage()};
                    CTRInteligencia.printTrace(a);
                }
            }
            this.dispose();
            caller.restart();
        }
    }//GEN-LAST:event_jLbSalvarMouseClicked

    private void jLabel19MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel19MouseClicked
        this.dispose();
        System.runFinalization();
        System.gc();
    }//GEN-LAST:event_jLabel19MouseClicked

    private void jTable1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable1MouseClicked
        if (jTable1.getSelectedRow() > -1 && loading == false) {
            loading = true;
            jLbLoad.setIcon(new ImageIcon(this.getClass().getResource("/mild/katyusha/system/gfx/load.gif")));
            jLbLoad.repaint();
            Runnable runnable = () -> {
                addTexto(new File("backup\\" + String.valueOf(modelo.getValueAt(jTable1.getSelectedRow(), 0))));
                jLbLoad.setIcon(new ImageIcon(this.getClass().getResource("/mild/katyusha/system/gfx/charged.gif")));
                jLbLoad.repaint();
                loading = false;
            };
            new Thread(runnable).start();
        }
    }//GEN-LAST:event_jTable1MouseClicked

    private void jButton8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton8ActionPerformed
        jLbAction.setText("Não Alterado");
        jLbTwig.setText("Não Alterado");
        jLbDetermination.setText("Não Alterado");
        jLbTitulo.setText(jLbTitulo.getText().replace("[*]", ""));
        jLbSalvar.setEnabled(false);
    }//GEN-LAST:event_jButton8ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        if (jTable1.getSelectedRow() > -1) {
            jLbAction.setText(String.valueOf(modelo.getValueAt(jTable1.getSelectedRow(), 0)));
            jLbTitulo.setText(jLbTitulo.getText().replace("[*]", "").concat("[*]"));
            jLbSalvar.setEnabled(true);
        }
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        if (jTable1.getSelectedRow() > -1) {
            jLbDetermination.setText(String.valueOf(modelo.getValueAt(jTable1.getSelectedRow(), 0)));
            jLbTitulo.setText(jLbTitulo.getText().replace("[*]", "").concat("[*]"));
            jLbSalvar.setEnabled(true);
        }
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        if (jTable1.getSelectedRow() > -1) {
            jLbTwig.setText(String.valueOf(modelo.getValueAt(jTable1.getSelectedRow(), 0)));
            jLbTitulo.setText(jLbTitulo.getText().replace("[*]", "").concat("[*]"));
            jLbSalvar.setEnabled(true);
        }
    }//GEN-LAST:event_jButton3ActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(WNDBackups.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {

            public void run() {
                new WNDBackups().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton8;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLbAction;
    private javax.swing.JLabel jLbDetermination;
    private javax.swing.JLabel jLbFechar;
    private javax.swing.JLabel jLbLoad;
    private javax.swing.JLabel jLbSalvar;
    private javax.swing.JLabel jLbTitulo;
    private javax.swing.JLabel jLbTwig;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JPanel jPload;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTable jTable1;
    private javax.swing.JTextPane jTextPane1;
    // End of variables declaration//GEN-END:variables
}
