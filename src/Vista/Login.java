package Vista;

import java.awt.BorderLayout;
import java.awt.Color;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

public class Login extends javax.swing.JFrame {

    private String correoUsuario;
    private JPanel panelLoginOriginal;

    public Login() {
        initComponents();
        panelLoginOriginal = panelFormularioLogin;
        setLocationRelativeTo(null);
        TxtContraseña.setEnabled(false);
        campoUsuario();

        TxtUsuario.addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyReleased(java.awt.event.KeyEvent evt) {
                if (!TxtUsuario.getText().trim().isEmpty() && !TxtUsuario.getText().equals(" Usuario")) {
                    TxtContraseña.setEnabled(true);
                    TxtContraseña.setForeground(Color.BLACK);
                }
            }
        });

        SwingUtilities.invokeLater(() -> {
            inf.requestFocusInWindow();
        });
    }

    public String getCorreoUsuario() {
        return correoUsuario;
    }

    public JPanel getPanelLoginOriginal() {
        return panelLoginOriginal;
    }

    public JPanel getPanel1() {
        return Panel1;
    }

    public JPasswordField getTxtContraseña() {
        return TxtContraseña;
    }

    public JTextField getTxtUsuario() {
        return TxtUsuario;
    }

    public JButton getBtnInicar() {
        return btnInicar;
    }

    public JButton getBtnCodigo() {
        return btnCodigo;
    }

    public JLabel getInf() {
        return inf;
    }

    public void mostrarPanelEnPanel1(JPanel panel) {
        Panel1.removeAll();
        Panel1.setLayout(new BorderLayout());
        Panel1.add(panel, BorderLayout.CENTER);
        Panel1.revalidate();
        Panel1.repaint();
    }

    public void limpiarCampos() {
        TxtUsuario.setText("");
        TxtContraseña.setText("");
        TxtContraseña.setEnabled(false);
        TxtUsuario.requestFocus();
    }

    public void mostrarMensaje(String mensaje) {
        javax.swing.JOptionPane.showMessageDialog(this, mensaje);
    }

    public void resaltarBotonCrearCuenta() {
        javax.swing.Timer timer = new javax.swing.Timer(200, null);
        timer.addActionListener(new java.awt.event.ActionListener() {
            int count = 0;
            boolean highlight = false;
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {
                if (count >= 6) {
                    btnCodigo.setBackground(new java.awt.Color(171, 52, 2));
                    timer.stop();
                    return;
                }
                btnCodigo.setBackground(highlight ? Color.WHITE : new java.awt.Color(171, 52, 2));
                highlight = !highlight;
                count++;
            }
        });
        timer.start();
        btnCodigo.requestFocus();
    }

    public void abrirPanelRegistro(String correo) {
        ContrasenaNueva panelCambio = new ContrasenaNueva(correo);
        javax.swing.JDialog dialog = new javax.swing.JDialog(this, "Registrar nueva cuenta", true);
        dialog.setContentPane(panelCambio);
        dialog.pack();
        dialog.setLocationRelativeTo(this);
        new Controlador.ControladorContrasenaNueva(panelCambio, correo, dialog, this);
        dialog.setVisible(true);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">
    private void initComponents() {

        Panel1 = new javax.swing.JPanel();
        panelFormularioLogin = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        TxtUsuario = new javax.swing.JTextField();
        TxtContraseña = new javax.swing.JPasswordField();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        btnCodigo = new javax.swing.JButton();
        inf = new javax.swing.JLabel();
        btnInicar = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        Panel1.setBackground(new java.awt.Color(255, 255, 255));
        Panel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        panelFormularioLogin.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel1.setBackground(new java.awt.Color(50, 10, 10));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        TxtUsuario.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        TxtUsuario.setForeground(new java.awt.Color(102, 102, 102));
        TxtUsuario.setText(" Usuario");
        TxtUsuario.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 1, 1, 1, new java.awt.Color(204, 204, 204)));
        jPanel1.add(TxtUsuario, new org.netbeans.lib.awtextra.AbsoluteConstraints(270, 125, 330, 30));

        TxtContraseña.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        TxtContraseña.setForeground(new java.awt.Color(102, 102, 102));
        TxtContraseña.setText(" Contraseña");
        TxtContraseña.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 1, 1, 1, new java.awt.Color(204, 204, 204)));
        jPanel1.add(TxtContraseña, new org.netbeans.lib.awtextra.AbsoluteConstraints(270, 180, 330, 30));

        jLabel3.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel3.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        jPanel1.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 240, 230, 40));

        jLabel4.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        jPanel1.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 300, 230, 40));

        btnCodigo.setBackground(new java.awt.Color(171, 52, 2));
        btnCodigo.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        btnCodigo.setForeground(new java.awt.Color(255, 255, 255));
        btnCodigo.setText("CREAR CUENTA");
        jPanel1.add(btnCodigo, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 300, 230, 40));

        inf.setBackground(new java.awt.Color(255, 255, 255));
        inf.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        inf.setForeground(java.awt.Color.white);
        inf.setText("Para ingresar, deberá validar su cuenta o crear una nueva si aún no tiene una.");
        jPanel1.add(inf, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 400, 490, 20));

        btnInicar.setBackground(new java.awt.Color(171, 52, 2));
        btnInicar.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        btnInicar.setForeground(new java.awt.Color(255, 255, 255));
        btnInicar.setText("INICIAR SESION");
        jPanel1.add(btnInicar, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 240, 230, 40));

        jLabel2.setBackground(new java.awt.Color(255, 255, 255));
        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 50)); // NOI18N
        jLabel2.setForeground(java.awt.Color.white);
        jLabel2.setText("I N T I");
        jPanel1.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 40, 150, 40));

        jLabel5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/Logo 90 x90.png"))); // NOI18N
        jPanel1.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(480, 20, -1, 90));

        panelFormularioLogin.add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 150, 870, 470));

        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/fondo l,d.png"))); // NOI18N
        panelFormularioLogin.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1370, 770));

        Panel1.add(panelFormularioLogin, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1370, 770));

        getContentPane().add(Panel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1370, 770));

        pack();
    }// </editor-fold>

    private void campoUsuario() {
        TxtUsuario.addFocusListener(new java.awt.event.FocusAdapter() {
            @Override
            public void focusGained(java.awt.event.FocusEvent evt) {
                if (TxtUsuario.getText().equals(" Usuario")) {
                    TxtUsuario.setText("");
                    TxtUsuario.setForeground(Color.BLACK);
                }
            }

            @Override
            public void focusLost(java.awt.event.FocusEvent evt) {
                if (TxtUsuario.getText().isEmpty()) {
                    TxtUsuario.setText(" Usuario");
                    TxtUsuario.setForeground(new Color(187, 187, 187));
                }
            }
        });
    }

    // Variables declaration - do not modify
    public javax.swing.JPanel Panel1;
    public javax.swing.JPasswordField TxtContraseña;
    public javax.swing.JTextField TxtUsuario;
    public javax.swing.JButton btnCodigo;
    public javax.swing.JButton btnInicar;
    public javax.swing.JLabel inf;
    public javax.swing.JLabel jLabel1;
    public javax.swing.JLabel jLabel2;
    public javax.swing.JLabel jLabel3;
    public javax.swing.JLabel jLabel4;
    public javax.swing.JLabel jLabel5;
    public javax.swing.JPanel jPanel1;
    public javax.swing.JPanel panelFormularioLogin;
    // End of variables declaration
}
