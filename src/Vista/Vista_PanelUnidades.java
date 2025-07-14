package Vista;

import javax.swing.JLabel;
import javax.swing.JOptionPane;

public class Vista_PanelUnidades extends javax.swing.JPanel {

    public Vista_PanelUnidades() {
        initComponents();
    }

    public void actualizarProgresoGeneral(int progreso) {
        if (jProgressBarGeneral != null) {
            jProgressBarGeneral.setValue(progreso);
            jProgressBarGeneral.setString(progreso + "%");
            jProgressBarGeneral.setStringPainted(true);
        }
        if (jLabelProgresoGeneral != null) {
            jLabelProgresoGeneral.setText(progreso + "%");
        }
    }

    public void mostrarError(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Error", JOptionPane.ERROR_MESSAGE);
    }

    public void mostrarMensaje(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Aviso", JOptionPane.INFORMATION_MESSAGE);
    }

    public void mostrarMensajeBienvenida(String nombreUnidad) {
        String mensaje = "¡Bienvenido a la unidad: " + nombreUnidad + "!\n\n" +
                "Aquí aprenderás nuevas palabras y conceptos.\n" +
                "¡Diviértete aprendiendo!";
        JOptionPane.showMessageDialog(this, mensaje, "Bienvenido", JOptionPane.INFORMATION_MESSAGE);
    }

    public void mostrarUnidadEnDesarrollo(int idUnidad, String nombreUnidad) {
        String mensaje = "La unidad " + idUnidad + ": " + nombreUnidad +
                " está en desarrollo.\n\n" +
                "Pronto estará disponible con contenido nuevo y emocionante.\n" +
                "¡Mantente atento a las actualizaciones!";
        JOptionPane.showMessageDialog(this, mensaje, "Unidad en Desarrollo", JOptionPane.INFORMATION_MESSAGE);
    }

    public JLabel getjLabelUNIDAD1() {
        return jLabelUNIDAD1;
    }

    public JLabel getjLabelUNIDAD2() {
        return jLabelUNIDAD2;
    }

    public JLabel getjLabelUNIDAD3() {
        return jLabelUNIDAD3;
    }

    public JLabel getjLabelUNIDAD4() {
        return jLabelUNIDAD4;
    }

    public JLabel getjLabelEstadoU1() {
        return jLabelEstadoU1;
    }

    public JLabel getjLabelEstadoU2() {
        return jLabelEstadoU2;
    }

    public JLabel getjLabelEstadoU3() {
        return jLabelEstadoU3;
    }

    public JLabel getjLabelEstadoU4() {
        return jLabelEstadoU4;
    }

    public JLabel getjLabelProgresoU1() {
        return jLabelProgresoU1;
    }

    public JLabel getjLabelProgresoU2() {
        return jLabelProgresoU2;
    }

    public JLabel getjLabelProgresoU3() {
        return jLabelProgresoU3;
    }

    public JLabel getjLabelProgresoU4() {
        return jLabelProgresoU4;
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">
    private void initComponents() {

        PanelVistas = new javax.swing.JPanel();
        jLabelUNIDAD4 = new javax.swing.JLabel();
        jLabelUNIDAD3 = new javax.swing.JLabel();
        jLabelUNIDAD2 = new javax.swing.JLabel();
        jLabelUNIDAD1 = new javax.swing.JLabel();
        jLabelProgresoGeneral = new javax.swing.JLabel();
        jProgressBarGeneral = new javax.swing.JProgressBar();
        jLabelProgresoU1 = new javax.swing.JLabel();
        jLabelProgresoU2 = new javax.swing.JLabel();
        jLabelProgresoU3 = new javax.swing.JLabel();
        jLabelProgresoU4 = new javax.swing.JLabel();
        jLabelEstadoU1 = new javax.swing.JLabel();
        jLabelEstadoU2 = new javax.swing.JLabel();
        jLabelEstadoU3 = new javax.swing.JLabel();
        jLabelEstadoU4 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();

        setMaximumSize(new java.awt.Dimension(1190, 660));
        setMinimumSize(new java.awt.Dimension(1190, 660));

        PanelVistas.setBackground(new java.awt.Color(255, 255, 255));
        PanelVistas.setMaximumSize(new java.awt.Dimension(1190, 660));
        PanelVistas.setRequestFocusEnabled(false);
        PanelVistas.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabelUNIDAD4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/unidad4_bounce_suave.gif"))); // NOI18N
        PanelVistas.add(jLabelUNIDAD4, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 470, -1, 180));

        jLabelUNIDAD3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/unidad3_bounce_suave.gif"))); // NOI18N
        PanelVistas.add(jLabelUNIDAD3, new org.netbeans.lib.awtextra.AbsoluteConstraints(750, 330, -1, 170));

        jLabelUNIDAD2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/unidad2_bounce_suave.gif"))); // NOI18N
        PanelVistas.add(jLabelUNIDAD2, new org.netbeans.lib.awtextra.AbsoluteConstraints(630, 80, -1, 170));

        jLabelUNIDAD1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/unidad1_bounce_suave.gif"))); // NOI18N
        PanelVistas.add(jLabelUNIDAD1, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 110, 150, 160));
        PanelVistas.add(jLabelProgresoGeneral, new org.netbeans.lib.awtextra.AbsoluteConstraints(810, 110, 100, 70));
        PanelVistas.add(jProgressBarGeneral, new org.netbeans.lib.awtextra.AbsoluteConstraints(950, 60, 190, 30));

        jLabelProgresoU1.setText("jLabel1");
        PanelVistas.add(jLabelProgresoU1, new org.netbeans.lib.awtextra.AbsoluteConstraints(400, 50, 130, 90));

        jLabelProgresoU2.setText("jLabel1");
        PanelVistas.add(jLabelProgresoU2, new org.netbeans.lib.awtextra.AbsoluteConstraints(400, 140, 130, 90));

        jLabelProgresoU3.setText("jLabel1");
        PanelVistas.add(jLabelProgresoU3, new org.netbeans.lib.awtextra.AbsoluteConstraints(390, 220, 110, 90));

        jLabelProgresoU4.setText("jLabel1");
        PanelVistas.add(jLabelProgresoU4, new org.netbeans.lib.awtextra.AbsoluteConstraints(400, 310, 90, 90));

        jLabelEstadoU1.setText("jLabel1");
        PanelVistas.add(jLabelEstadoU1, new org.netbeans.lib.awtextra.AbsoluteConstraints(990, 200, 120, 90));

        jLabelEstadoU2.setText("jLabel1");
        PanelVistas.add(jLabelEstadoU2, new org.netbeans.lib.awtextra.AbsoluteConstraints(990, 310, 100, 70));

        jLabelEstadoU3.setText("jLabel1");
        PanelVistas.add(jLabelEstadoU3, new org.netbeans.lib.awtextra.AbsoluteConstraints(990, 400, 110, 70));

        jLabelEstadoU4.setText("jLabel1");
        PanelVistas.add(jLabelEstadoU4, new org.netbeans.lib.awtextra.AbsoluteConstraints(990, 480, 130, 80));

        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/fondo_mapa_1190x660.png"))); // NOI18N
        PanelVistas.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1190, 660));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 1190, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(layout.createSequentialGroup()
                                        .addGap(0, 0, Short.MAX_VALUE)
                                        .addComponent(PanelVistas, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(0, 0, Short.MAX_VALUE)))
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 660, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(layout.createSequentialGroup()
                                        .addGap(0, 0, Short.MAX_VALUE)
                                        .addComponent(PanelVistas, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(0, 0, Short.MAX_VALUE)))
        );
    }// </editor-fold>

    // Variables declaration - do not modify
    private javax.swing.JPanel PanelVistas;
    public javax.swing.JLabel jLabel2;
    public javax.swing.JLabel jLabelEstadoU1;
    public javax.swing.JLabel jLabelEstadoU2;
    public javax.swing.JLabel jLabelEstadoU3;
    public javax.swing.JLabel jLabelEstadoU4;
    public javax.swing.JLabel jLabelProgresoGeneral;
    public javax.swing.JLabel jLabelProgresoU1;
    public javax.swing.JLabel jLabelProgresoU2;
    public javax.swing.JLabel jLabelProgresoU3;
    public javax.swing.JLabel jLabelProgresoU4;
    public javax.swing.JLabel jLabelUNIDAD1;
    public javax.swing.JLabel jLabelUNIDAD2;
    public javax.swing.JLabel jLabelUNIDAD3;
    public javax.swing.JLabel jLabelUNIDAD4;
    public javax.swing.JProgressBar jProgressBarGeneral;
    // End of variables declaration
}
