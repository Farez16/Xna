package Vista;

import java.awt.Graphics2D;
import java.awt.MediaTracker;
import java.awt.RenderingHints;
import java.awt.event.ActionListener;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class Cuenta extends JPanel {

    public Cuenta() {
        initComponents();
    }

    public void setUsuario(String usuario) {
        if (jTextField1MostrarUsuario != null) {
            jTextField1MostrarUsuario.setText(usuario);
            jTextField1MostrarUsuario.setEditable(false);
        }
    }

    public void setContrasena(String contrasena) {
        if (jTextField1Contraseña1 != null) {
            jTextField1Contraseña1.setText(contrasena);
            jTextField1Contraseña1.setEditable(false);
        }
    }

    public void setFechaHora(String fechaHora) {
        if (jLabel1FechayHora1 != null) {
            jLabel1FechayHora1.setText(fechaHora);
        }
    }

    public void setFechaRegistro(String fechaRegistro) {
        if (jLabel1loginactivity != null) {
            jLabel1loginactivity.setText(fechaRegistro);
        }
    }

    public void setUltimoAcceso(String ultimoAcceso) {
        if (jLabel1loginactivity != null) {
            jLabel1loginactivity.setText(jLabel1loginactivity.getText() + ultimoAcceso);
        }
    }

    public void mostrarImagen(String ruta) {
        try {
            if (Lblimagen == null) {
                return;
            }

            if (ruta == null || ruta.isEmpty()) {
                cargarImagenPorDefecto();
                return;
            }

            BufferedImage img;
            if (ruta.startsWith("http")) {
                img = ImageIO.read(new java.net.URL(ruta));
            } else {
                img = ImageIO.read(new java.io.File(ruta));
            }

            BufferedImage circleBuffer = crearImagenCircular(img);
            Lblimagen.setIcon(new ImageIcon(circleBuffer));

        } catch (Exception ex) {
            System.err.println("Error al mostrar imagen: " + ex.getMessage());
            cargarImagenPorDefecto();
        }
    }

    private BufferedImage crearImagenCircular(BufferedImage img) {
        int size = 120;
        BufferedImage circleBuffer = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = circleBuffer.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        Ellipse2D.Double circle = new Ellipse2D.Double(0, 0, size, size);
        g2.setClip(circle);
        g2.drawImage(img, 0, 0, size, size, null);
        g2.dispose();
        return circleBuffer;
    }

    private void cargarImagenPorDefecto() {
        try {
            if (Lblimagen == null) {
                return;
            }

            ImageIcon defaultIcon = new ImageIcon(getClass().getResource("/Imagenes/Users.png"));
            if (defaultIcon.getImageLoadStatus() != MediaTracker.COMPLETE) {
                return;
            }

            BufferedImage buffered = new BufferedImage(120, 120, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2 = buffered.createGraphics();
            defaultIcon.paintIcon(null, g2, 0, 0);
            g2.dispose();

            BufferedImage circleBuffer = crearImagenCircular(buffered);
            Lblimagen.setIcon(new ImageIcon(circleBuffer));
        } catch (Exception e) {
            System.err.println("Error cargando imagen por defecto: " + e.getMessage());
        }
    }

    public void onSubirImagen(ActionListener listener) {
        jButton1SubirImagen.addActionListener(listener);
    }

    public void onCambiarContrasena(ActionListener listener) {
        jButton1CambiarContraseña.addActionListener(listener);
    }

    public int mostrarMenuSubirImagen() {
        Object[] opciones = {"Subir archivo local", "Ingresar URL de imagen"};
        return JOptionPane.showOptionDialog(
                this,
                "Seleccione cómo desea subir la imagen",
                "Subir imagen de perfil",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                opciones,
                opciones[0]
        );
    }

    public String ingresarURLImagen() {
        return JOptionPane.showInputDialog(this, "Ingrese la URL de la imagen:");
    }

    public String ingresarNuevaContrasena() {
        return JOptionPane.showInputDialog(this, "Ingrese nueva contraseña (mínimo 6 caracteres):");
    }

    public void mostrarMensaje(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        PanelVistas = new javax.swing.JPanel();
        jLabel1Usuario = new javax.swing.JLabel();
        jTextField1MostrarUsuario = new javax.swing.JTextField();
        Lblimagen = new javax.swing.JLabel();
        jButton1SubirImagen = new javax.swing.JButton();
        jButton1CambiarContraseña = new javax.swing.JButton();
        jLabel1loginactivity = new javax.swing.JLabel();
        jLabel1FechayHora1 = new javax.swing.JLabel();
        jLabel1Contra1 = new javax.swing.JLabel();
        jTextField1Contraseña1 = new javax.swing.JTextField();
        jLabelMisCertificados = new javax.swing.JLabel();
        jButton1Miscertificados = new javax.swing.JButton();
        LblimagenFondo = new javax.swing.JLabel();
        LblSaludo = new javax.swing.JLabel();
        LabelPerfildelUsuario = new javax.swing.JLabel();

        jPanel1.setPreferredSize(new java.awt.Dimension(1190, 660));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        PanelVistas.setBackground(new java.awt.Color(255, 255, 255));
        PanelVistas.setPreferredSize(new java.awt.Dimension(1190, 660));
        PanelVistas.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel1Usuario.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel1Usuario.setText("<html>     <span style=\"font-size:16px; font-family:Segoe UI; color:#2a4365;\"><b>Usuario:</b></span> </html> ");
        PanelVistas.add(jLabel1Usuario, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 190, -1, -1));
        PanelVistas.add(jTextField1MostrarUsuario, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 230, 210, -1));

        Lblimagen.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/Users.png"))); // NOI18N
        PanelVistas.add(Lblimagen, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 30, -1, -1));

        jButton1SubirImagen.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jButton1SubirImagen.setText("Subir Imagen");
        jButton1SubirImagen.setBorder(null);
        PanelVistas.add(jButton1SubirImagen, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 150, -1, -1));

        jButton1CambiarContraseña.setText("Cambiar Contraseña");
        jButton1CambiarContraseña.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        PanelVistas.add(jButton1CambiarContraseña, new org.netbeans.lib.awtextra.AbsoluteConstraints(330, 320, -1, -1));

        jLabel1loginactivity.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel1loginactivity.setText("<html>     <span style=\"font-size:14px; font-family:Segoe UI; color:#2a4365;\"><b>Login activity</b></span><br><br>          <span style=\"font-size:13px; font-family:Segoe UI; color:#2a4365;\"><b>First access to site</b></span><br>     <span style=\"font-size:13px; font-family:Segoe UI;\">Monday, 22 May 2023, 9:54 PM <i>(2 years 42 days)</i></span><br><br>          <span style=\"font-size:13px; font-family:Segoe UI; color:#2a4365;\"><b>Last access to site</b></span><br>     <span style=\"font-size:13px; font-family:Segoe UI;\">Thursday, 3 July 2025, 12:11 AM <i>(now)</i></span> </html> ");
        PanelVistas.add(jLabel1loginactivity, new org.netbeans.lib.awtextra.AbsoluteConstraints(640, 450, 510, -1));

        jLabel1FechayHora1.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel1FechayHora1.setText("<html>     <span style=\"font-size:14px; font-family:Segoe UI; color:#2a4365;\">         <b>Fecha y Hora:</b><br>         02 de julio de 2025 - 18:45:30     </span> </html> ");
        PanelVistas.add(jLabel1FechayHora1, new org.netbeans.lib.awtextra.AbsoluteConstraints(760, 20, 380, -1));

        jLabel1Contra1.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel1Contra1.setText("<html>     <span style=\"font-size:16px; font-family:Segoe UI; color:#2a4365;\"><b>Contraseña:</b></span> </html> ");
        PanelVistas.add(jLabel1Contra1, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 280, -1, -1));
        PanelVistas.add(jTextField1Contraseña1, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 320, 210, -1));

        jLabelMisCertificados.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabelMisCertificados.setText("<html>     <span style=\"font-size:16px; font-family:Segoe UI; color:#2a4365;\"><b>Mis Certificados</b></span> </html> ");
        PanelVistas.add(jLabelMisCertificados, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 380, -1, -1));

        jButton1Miscertificados.setBorder(null);
        jButton1Miscertificados.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        PanelVistas.add(jButton1Miscertificados, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 390, 160, 20));

        LblimagenFondo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/lbl.jpg"))); // NOI18N
        LblimagenFondo.setPreferredSize(new java.awt.Dimension(1190, 660));
        PanelVistas.add(LblimagenFondo, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, -1));

        jPanel1.add(PanelVistas, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1190, 660));

        LblSaludo.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        LblSaludo.setForeground(new java.awt.Color(255, 255, 255));
        jPanel1.add(LblSaludo, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 50, 219, 24));

        LabelPerfildelUsuario.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        LabelPerfildelUsuario.setForeground(new java.awt.Color(255, 255, 255));
        LabelPerfildelUsuario.setText("<html>     <span style=\"font-size:16px; font-family:Segoe UI; color:#2a4365;\"><b>PERFIL DEL USUARIO</b></span> </html> ");
        jPanel1.add(LabelPerfildelUsuario, new org.netbeans.lib.awtextra.AbsoluteConstraints(410, 40, -1, 40));

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
    public javax.swing.JLabel LabelPerfildelUsuario;
    public javax.swing.JLabel LblSaludo;
    public javax.swing.JLabel Lblimagen;
    public javax.swing.JLabel LblimagenFondo;
    public javax.swing.JPanel PanelVistas;
    public javax.swing.JButton jButton1CambiarContraseña;
    public javax.swing.JButton jButton1Miscertificados;
    public javax.swing.JButton jButton1SubirImagen;
    public javax.swing.JLabel jLabel1Contra1;
    public javax.swing.JLabel jLabel1FechayHora1;
    public javax.swing.JLabel jLabel1Usuario;
    public javax.swing.JLabel jLabel1loginactivity;
    public javax.swing.JLabel jLabelMisCertificados;
    public javax.swing.JPanel jPanel1;
    public javax.swing.JTextField jTextField1Contraseña1;
    public javax.swing.JTextField jTextField1MostrarUsuario;
    // End of variables declaration
}
