package Vista;

import Controlador.ControladorBotones;
import Modelo.TextoBotones;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;

public class Dashboard extends javax.swing.JPanel {

    private String correoUsuario;
    private final ControladorBotones controladorBotones;

    public Dashboard(String correoUsuario) {
        initComponents();
        this.correoUsuario = correoUsuario;
        btnDashboard.setOpaque(false);
        btnDashboard.setContentAreaFilled(false);
        btnDashboard.setBorderPainted(false);

        btnCuenta.setOpaque(false);
        btnCuenta.setContentAreaFilled(false);
        btnCuenta.setBorderPainted(false);

        btnCertificado.setOpaque(false);
        btnCertificado.setContentAreaFilled(false);
        btnCertificado.setBorderPainted(false);

        btnJuegos.setOpaque(false);
        btnJuegos.setContentAreaFilled(false);
        btnJuegos.setBorderPainted(false);

        btnSalir.setOpaque(false);
        btnSalir.setContentAreaFilled(false);
        btnSalir.setBorderPainted(false);

        Map<String, JButton> botonesMap = new HashMap<>();
        botonesMap.put("btnDashboard", btnDashboard);
        botonesMap.put("btnCuenta", btnCuenta);
        botonesMap.put("btnCertificado", btnCertificado);
        botonesMap.put("btnJuegos", btnJuegos);
        botonesMap.put("btnSalir", btnSalir);

        TextoBotones modeloTextos = new TextoBotones();
        controladorBotones = new ControladorBotones(botonesMap, modeloTextos);
        controladorBotones.iniciar();

        Timer timer = new Timer(10000, e -> controladorBotones.actualizarTextos());
        timer.start();
    }

    public String getCorreoUsuario() {
        return correoUsuario;
    }

    public JLabel getLblLogo() {
        return LblLogo;
    }

    public JLabel getLblNombreProyecto() {
        return LblNombreProyecto;
    }

    public JLabel getLblPanelSuperior() {
        return LblPanelSuperior;
    }

    public JLabel getLblRol() {
        return LblRol;
    }

    public JLabel getLblimagenPrincipal() {
        return LblimagenPrincipal;
    }

    public JPanel getPanelVistas() {
        return PanelVistas;
    }

    public JButton getBtnCertificado() {
        return btnCertificado;
    }

    public JButton getBtnCuenta() {
        return btnCuenta;
    }

    public JButton getBtnDashboard() {
        return btnDashboard;
    }

    public JButton getBtnJuegos() {
        return btnJuegos;
    }

    public JButton getBtnSalir() {
        return btnSalir;
    }

    public JPanel getjPanel1() {
        return jPanel1;
    }

    public JLabel getLblNombre() {
        return lblNombre;
    }

    public void mostrarVista(JPanel nuevaVista) {
        PanelVistas.removeAll();
        PanelVistas.setLayout(new java.awt.BorderLayout());
        PanelVistas.add(nuevaVista, java.awt.BorderLayout.CENTER);
        PanelVistas.revalidate();
        PanelVistas.repaint();
    }

    public void setNombreUsuario(String nombre) {
        lblNombre.setText(nombre);
    }

    public void setRolUsuario(String rol) {
        LblRol.setText(rol);
    }

    public void actualizarSaludo(String saludo) {
        LblSaludo.setText(saludo);
    }

    public void setCorreoUsuario(String correo) {
        this.correoUsuario = correo;
    }

    public JLabel getLblSaludo() {
        return LblSaludo;
    }

    public void actualizarImagenPerfil(String ruta) {
        try {
            BufferedImage img;
            if (ruta.startsWith("http")) {
                img = ImageIO.read(new URL(ruta));
            } else {
                img = ImageIO.read(new File(ruta));
            }

            int size = 120;
            BufferedImage circleBuffer = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2 = circleBuffer.createGraphics();
            Ellipse2D.Double circle = new Ellipse2D.Double(0, 0, size, size);
            g2.setClip(circle);
            g2.drawImage(img, 0, 0, size, size, null);
            g2.dispose();

            LblimagenPrincipal.setIcon(new ImageIcon(circleBuffer));
            LblimagenPrincipal.revalidate();
            LblimagenPrincipal.repaint();

        } catch (Exception ex) {
            System.err.println("Error actualizando imagen en Dashboard: " + ex.getMessage());
        }
    }

    public void mostrarMensaje(String mensaje) {
        javax.swing.JOptionPane.showMessageDialog(this, mensaje);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        PanelVistas = new javax.swing.JPanel();
        LblSaludo = new javax.swing.JLabel();
        LblimagenPrincipal = new javax.swing.JLabel();
        lblNombre = new javax.swing.JLabel();
        LblRol = new javax.swing.JLabel();
        btnDashboard = new javax.swing.JButton();
        btnCuenta = new javax.swing.JButton();
        btnCertificado = new javax.swing.JButton();
        btnJuegos = new javax.swing.JButton();
        LblNombreProyecto = new javax.swing.JLabel();
        LblLogo = new javax.swing.JLabel();
        btnSalir = new javax.swing.JButton();
        LblPanelSuperior = new javax.swing.JLabel();

        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        PanelVistas.setBackground(new java.awt.Color(255, 255, 255));
        PanelVistas.setPreferredSize(new java.awt.Dimension(1190, 660));
        PanelVistas.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        jPanel1.add(PanelVistas, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 110, 1190, 660));

        LblSaludo.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        LblSaludo.setForeground(new java.awt.Color(255, 255, 255));
        jPanel1.add(LblSaludo, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 24, 380, 60));

        LblimagenPrincipal.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/Users.png"))); // NOI18N
        jPanel1.add(LblimagenPrincipal, new org.netbeans.lib.awtextra.AbsoluteConstraints(1180, 10, 90, 90));

        lblNombre.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        lblNombre.setForeground(new java.awt.Color(255, 255, 255));
        jPanel1.add(lblNombre, new org.netbeans.lib.awtextra.AbsoluteConstraints(1030, 20, 130, 40));

        LblRol.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        LblRol.setForeground(new java.awt.Color(255, 255, 255));
        jPanel1.add(LblRol, new org.netbeans.lib.awtextra.AbsoluteConstraints(1030, 60, 130, 30));

        btnDashboard.setBackground(new java.awt.Color(102, 102, 102));
        btnDashboard.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        btnDashboard.setForeground(new java.awt.Color(255, 255, 255));
        btnDashboard.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/menu.png"))); // NOI18N
        btnDashboard.setText("  Inicio");
        btnDashboard.setBorder(null);
        btnDashboard.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btnDashboard.setOpaque(true);
        jPanel1.add(btnDashboard, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 220, 160, 42));

        btnCuenta.setBackground(new java.awt.Color(102, 102, 102));
        btnCuenta.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        btnCuenta.setForeground(new java.awt.Color(255, 255, 255));
        btnCuenta.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/icono-usuario.png"))); // NOI18N
        btnCuenta.setText("  Cuenta");
        btnCuenta.setBorder(null);
        btnCuenta.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btnCuenta.setOpaque(true);
        jPanel1.add(btnCuenta, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 280, 160, 42));

        btnCertificado.setBackground(new java.awt.Color(102, 102, 102));
        btnCertificado.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        btnCertificado.setForeground(new java.awt.Color(255, 255, 255));
        btnCertificado.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/certificado.png"))); // NOI18N
        btnCertificado.setText("  Certificado");
        btnCertificado.setBorder(null);
        btnCertificado.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btnCertificado.setOpaque(true);
        jPanel1.add(btnCertificado, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 340, 160, 42));

        btnJuegos.setBackground(new java.awt.Color(102, 102, 102));
        btnJuegos.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        btnJuegos.setForeground(new java.awt.Color(255, 255, 255));
        btnJuegos.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/play.png"))); // NOI18N
        btnJuegos.setText("  Interactivo");
        btnJuegos.setBorder(null);
        btnJuegos.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btnJuegos.setOpaque(true);
        jPanel1.add(btnJuegos, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 400, 160, 42));

        LblNombreProyecto.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        LblNombreProyecto.setForeground(new java.awt.Color(255, 255, 255));
        LblNombreProyecto.setText("INTI");
        jPanel1.add(LblNombreProyecto, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 130, -1, -1));

        LblLogo.setForeground(new java.awt.Color(255, 255, 255));
        LblLogo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/Logo 90 x90.png"))); // NOI18N
        jPanel1.add(LblLogo, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 40, -1, -1));

        btnSalir.setBackground(new java.awt.Color(102, 102, 102));
        btnSalir.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        btnSalir.setForeground(new java.awt.Color(255, 255, 255));
        btnSalir.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/cierre-de-sesión.png"))); // NOI18N
        btnSalir.setText("  Salir");
        btnSalir.setBorder(null);
        btnSalir.setOpaque(true);
        jPanel1.add(btnSalir, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 560, 168, 42));

        LblPanelSuperior.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/fondo dashboard.png"))); // NOI18N
        jPanel1.add(LblPanelSuperior, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1370, 770));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
    }// </editor-fold>

    // Variables declaration - do not modify
    public javax.swing.JLabel LblLogo;
    public javax.swing.JLabel LblNombreProyecto;
    public javax.swing.JLabel LblPanelSuperior;
    public javax.swing.JLabel LblRol;
    public javax.swing.JLabel LblSaludo;
    public javax.swing.JLabel LblimagenPrincipal;
    public javax.swing.JPanel PanelVistas;
    public javax.swing.JButton btnCertificado;
    public javax.swing.JButton btnCuenta;
    public javax.swing.JButton btnDashboard;
    public javax.swing.JButton btnJuegos;
    public javax.swing.JButton btnSalir;
    public javax.swing.JPanel jPanel1;
    public javax.swing.JLabel lblNombre;
    // End of variables declaration
}
