package Controlador;

import Modelo.Modelo_Actividades;
import Modelo.Modelo_Progreso_Usuario;
import Modelo.Usuario;
import Vista.Vista_Actividad1U1;
import Vista.Vista_Actividad2U1;
import Vista.Vista_Unidad1;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.SQLException;
import javax.swing.JOptionPane;

public class Controlador_Actividades {

    private final javax.swing.JPanel vista;
    private final ControladorDashboard controladorDashboard;
    private final Connection conn;
    private final String correo;
    private final int idActividad;
    private Modelo_Actividades actividad;
    private final Controlador_Unidad1 controladorUnidad1;
    private final Usuario usuario;

    public Controlador_Actividades(javax.swing.JPanel vista, ControladorDashboard controladorDashboard,
            Connection conn, String correo, int idActividad, Controlador_Unidad1 controladorUnidad1) {
        this.vista = vista;
        this.controladorDashboard = controladorDashboard;
        this.conn = conn;
        this.usuario = new Usuario(conn);

        if (correo == null || correo.trim().isEmpty()) {
            throw new IllegalArgumentException("El correo no puede ser nulo o vacío");
        }
        this.correo = correo.trim();

        this.idActividad = idActividad;
        this.controladorUnidad1 = controladorUnidad1;

        agregarEventoCompletar();
    }

    public Controlador_Actividades(javax.swing.JPanel vista, ControladorDashboard controladorDashboard,
            Connection conn, String correo, int idActividad) {
        this(vista, controladorDashboard, conn, correo, idActividad, null);
    }

    public void cargarActividad() {
        actividad = Modelo_Actividades.obtenerPorId(conn, idActividad);

        if (actividad == null) {
            System.out.println("No se encontró la actividad con ID " + idActividad);
            return;
        }

        if (vista instanceof Vista_Actividad1U1 act1) {
            act1.jTextAreaPregunta.setText(actividad.getPregunta());
            act1.jRadioButtonOpcion1.setText(actividad.getOpcionA());
            act1.jRadioButtonOpcion2.setText(actividad.getOpcionB());
            act1.jRadioButtonOpcion3.setText(actividad.getOpcionC());

            act1.jLabelMensajeRespuesta.setText("");
            act1.buttonGroupOpciones.clearSelection();

            for (ActionListener al : act1.jButtonResponder.getActionListeners()) {
                act1.jButtonResponder.removeActionListener(al);
            }
            act1.jButtonCOMPLETOACTV1.setEnabled(false);

            act1.jButtonResponder.addActionListener(e -> validarRespuesta(act1));
        }
    }

    private void validarRespuesta(Vista_Actividad1U1 act1) {
        String respuestaSeleccionada = null;

        if (act1.jRadioButtonOpcion1.isSelected()) {
            respuestaSeleccionada = "A";
        } else if (act1.jRadioButtonOpcion2.isSelected()) {
            respuestaSeleccionada = "B";
        } else if (act1.jRadioButtonOpcion3.isSelected()) {
            respuestaSeleccionada = "C";
        }

        if (respuestaSeleccionada == null) {
            act1.jLabelMensajeRespuesta.setText("Por favor, selecciona una respuesta.");
            return;
        }

        if (respuestaSeleccionada.equalsIgnoreCase(actividad.getRespuestaCorrecta() + "")) {
            act1.jLabelMensajeRespuesta.setText("¡Correcto!");
            act1.jButtonCOMPLETOACTV1.setEnabled(true);
        } else {
            act1.jLabelMensajeRespuesta.setText("Incorrecto. Inténtalo de nuevo.");
            act1.jButtonCOMPLETOACTV1.setEnabled(false);
        }
    }

    private void agregarEventoCompletar() {
        try {
            int idUsuario = usuario.obtenerIdPorCorreo(correo);

            if (vista instanceof Vista_Actividad1U1 act1) {
                act1.jButtonCOMPLETOACTV1.addActionListener(e -> completarActividad(idUsuario));
            }
            if (vista instanceof Vista_Actividad2U1 act2) {
                act2.jButtonCOMPLETOACTV2.addActionListener(e -> completarActividad(idUsuario));
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(vista, "Error al obtener el ID del usuario: " + ex.getMessage(), "Error de Base de Datos", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void completarActividad(int idUsuario) {
        Modelo_Progreso_Usuario progreso = ControladorProgresoUsuario.obtenerProgreso(idUsuario, actividad.getIdUnidad());
        boolean actualizado = ControladorProgresoUsuario.actualizarActividad(progreso, idActividad);
        if (actualizado) {
            System.out.println("Actividad " + idActividad + " completada");
        }

        if (controladorUnidad1 != null) {
            controladorUnidad1.actualizarVista();
            
            Vista_Unidad1 vistaUnidad1 = new Vista_Unidad1();
            new Controlador_Unidad1(vistaUnidad1, conn, controladorDashboard, correo,
                    controladorUnidad1.getControladorUnidades());
            controladorDashboard.getVista().mostrarVista(vistaUnidad1);
        } else {
            Vista_Unidad1 vistaUnidad1 = new Vista_Unidad1();
            controladorDashboard.getVista().mostrarVista(vistaUnidad1);
        }
    }
}