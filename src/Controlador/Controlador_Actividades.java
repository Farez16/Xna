package Controlador;

import Modelo.Modelo_Actividades;
import Modelo.Modelo_Progreso_Usuario;
import Modelo.Usuario;
import Vista.Vista_Actividad1U1;
import Vista.Vista_Actividad2U1;
import Vista.Vista_Unidad1;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;

public class Controlador_Actividades {

    private final javax.swing.JPanel vista;
    private final ControladorDashboard controladorDashboard;
    private final Connection conn;
    private final String correo;  // Cambiado de cedula a correo
    private final int idActividad;
    private Modelo_Actividades actividad;
    private final Controlador_Unidad1 controladorUnidad1; // Cambiado a Controlador_Unidad1

    // Constructor actualizado para recibir Controlador_Unidad1
    public Controlador_Actividades(javax.swing.JPanel vista, ControladorDashboard controladorDashboard,
            Connection conn, String correo, int idActividad, Controlador_Unidad1 controladorUnidad1) {
        this.vista = vista;
        this.controladorDashboard = controladorDashboard;
        this.conn = conn;

        if (correo == null || correo.trim().isEmpty()) {
            throw new IllegalArgumentException("El correo no puede ser nulo o vacío");
        }
        this.correo = correo.trim();

        this.idActividad = idActividad;
        this.controladorUnidad1 = controladorUnidad1; // Asignar el controlador

        agregarEventoCompletar();
    }

    // Constructor de compatibilidad (opcional, por si lo necesitas en otro lugar)
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

            // Eliminar listeners previos para evitar que se acumulen
            for (ActionListener al : act1.jButtonResponder.getActionListeners()) {
                act1.jButtonResponder.removeActionListener(al);
            }
            act1.jButtonCOMPLETOACTV1.setEnabled(false); // Solo se activa al responder bien

            // Agregar el listener para validar respuesta al pulsar responder
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

        // Validar si la respuesta es correcta
        if (respuestaSeleccionada.equalsIgnoreCase(actividad.getRespuestaCorrecta() + "")) {
            act1.jLabelMensajeRespuesta.setText("¡Correcto!");
            act1.jButtonCOMPLETOACTV1.setEnabled(true);  // Solo se habilita si responde bien
        } else {
            act1.jLabelMensajeRespuesta.setText("Incorrecto. Inténtalo de nuevo.");
            act1.jButtonCOMPLETOACTV1.setEnabled(false); // Desactiva el botón si responde mal
        }
    }

    private void agregarEventoCompletar() {
        int idUsuario = Usuario.obtenerIdPorCorreo(correo);

        if (vista instanceof Vista_Actividad1U1 act1) {
            act1.jButtonCOMPLETOACTV1.addActionListener(e -> completarActividad(idUsuario));
        }
        if(vista instanceof Vista_Actividad2U1 act2){
            act2.jButtonCOMPLETOACTV2.addActionListener(e -> completarActividad(idUsuario) );
        }
    }

    private void completarActividad(int idUsuario) {
        Modelo_Progreso_Usuario progreso = ControladorProgresoUsuario.obtenerProgreso(idUsuario, actividad.getIdUnidad());
        boolean actualizado = ControladorProgresoUsuario.actualizarActividad(progreso, idActividad);
        if (actualizado) {
            System.out.println("Actividad " + idActividad + " completada");
        }

        // Volver a la vista unidad1 usando el controlador existente
        if (controladorUnidad1 != null) {
            // Actualizar el controlador de unidades para reflejar el progreso
            controladorUnidad1.actualizarVista(); // Necesitarás crear este método
            
            // Volver a la vista de unidad1
            Vista_Unidad1 vistaUnidad1 = new Vista_Unidad1();
            new Controlador_Unidad1(vistaUnidad1, conn, controladorDashboard, correo, 
                                   controladorUnidad1.getControladorUnidades()); // Necesitarás crear este getter
            controladorDashboard.getVista().mostrarVista(vistaUnidad1);
        } else {
            // Fallback si no hay controlador disponible
            Vista_Unidad1 vistaUnidad1 = new Vista_Unidad1();
            // Aquí necesitarías obtener el controlador de unidades de alguna manera
            controladorDashboard.getVista().mostrarVista(vistaUnidad1);
        }
    }
}