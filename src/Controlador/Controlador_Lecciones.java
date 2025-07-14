package Controlador;

import Modelo.Modelo_Lecciones;
import Modelo.Modelo_Progreso_Usuario;
import Modelo.Usuario;
import Vista.Vista_LeccionFONOLOGIA;
import Vista.Vista_LeccionPRONOMBRES;
import Vista.Vista_LeccionSALUDOS;
import Vista.Vista_Unidad1;
import java.awt.Image;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

public class Controlador_Lecciones {

    private final JPanel vistaLeccion;
    private final ControladorDashboard controladorDashboard;
    private final Connection conn;
    private final String correo;
    private final int numeroLeccion;
    private int idUsuario;
    private final int ID_UNIDAD = 1;
    private ImageIcon imagen1Original;
    private ImageIcon imagen2Original;
    private final Controlador_Unidades controladorUnidades;
    private final Usuario usuario;

    public static final int LECCION_SALUDOS = 1;
    public static final int LECCION_FONOLOGIA = 2;
    public static final int LECCION_PRONOMBRES = 3;

    public Controlador_Lecciones(JPanel vistaLeccion, ControladorDashboard controladorDashboard,
            Connection conn, String correo, int numeroLeccion) {
        this.vistaLeccion = vistaLeccion;
        this.controladorDashboard = controladorDashboard;
        this.conn = conn;
        this.usuario = new Usuario(conn);

        if (correo == null || correo.trim().isEmpty()) {
            throw new IllegalArgumentException("El correo no puede ser nulo o vacío");
        }
        this.correo = correo.trim();
        this.numeroLeccion = numeroLeccion;

        try {
            this.idUsuario = usuario.obtenerIdPorCorreo(this.correo);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(vistaLeccion, "Error al obtener el ID del usuario: " + ex.getMessage(), "Error de Base de Datos", JOptionPane.ERROR_MESSAGE);
        }

        if (this.idUsuario <= 0) {
            throw new IllegalArgumentException("Usuario no encontrado con el correo: " + this.correo);
        }

        configurarVistaLeccion();
        agregarListeners();
        this.controladorUnidades = null;
    }

    private void configurarVistaLeccion() {
        configurarSegunTipoLeccion();

        if (numeroLeccion == LECCION_SALUDOS) {
            SwingUtilities.invokeLater(() -> {
                Timer timer = new Timer(200, e -> {
                    if (vistaLeccion.isShowing()) {
                        ((Timer) e.getSource()).stop();
                    }
                });
                timer.setRepeats(false);
                timer.start();
            });
        }
    }

    private void configurarSegunTipoLeccion() {
        switch (numeroLeccion) {
            case LECCION_SALUDOS:
                configurarLeccionSaludos();
                configurarVistaLeccionSaludos();
                break;
            case LECCION_FONOLOGIA:
                configurarLeccionFonologia();
                break;
            case LECCION_PRONOMBRES:
                configurarLeccionPronombres();
                break;
        }
    }

    private void configurarLeccionSaludos() {
        if (vistaLeccion instanceof Vista_LeccionSALUDOS) {
            Vista_LeccionSALUDOS vista = (Vista_LeccionSALUDOS) vistaLeccion;
            vista.jButtonCOMPLETOSALUDOS.setText("COMPLETAR LECCIÓN DE SALUDOS");
        }
    }

    private void configurarLeccionFonologia() {
        if (vistaLeccion instanceof Vista_LeccionFONOLOGIA) {
            Vista_LeccionFONOLOGIA vista = (Vista_LeccionFONOLOGIA) vistaLeccion;
            vista.jButtonCOMPLETOFONOLOGIA.setText("COMPLETAR LECCIÓN DE FONOLOGÍA");
        }
    }

    private void configurarLeccionPronombres() {
        if (vistaLeccion instanceof Vista_LeccionPRONOMBRES) {
            Vista_LeccionPRONOMBRES vista = (Vista_LeccionPRONOMBRES) vistaLeccion;
            vista.jButtonCOMPLETOPRONOMBRES.setText("COMPLETAR LECCIÓN DE PRONOMBRES");
        }
    }

    private void agregarListeners() {
        if (vistaLeccion instanceof Vista_LeccionSALUDOS) {
            Vista_LeccionSALUDOS vista = (Vista_LeccionSALUDOS) vistaLeccion;
            vista.jButtonCOMPLETOSALUDOS.addActionListener(e -> completarLeccion());

            vista.jLabelImagen1Saludos.addComponentListener(new ComponentAdapter() {
                @Override
                public void componentResized(ComponentEvent e) {
                    escalarYMostrarImagen(vista.jLabelImagen1Saludos, imagen1Original);
                }
            });

            vista.jLabelImagen2Saludos.addComponentListener(new ComponentAdapter() {
                @Override
                public void componentResized(ComponentEvent e) {
                    escalarYMostrarImagen(vista.jLabelImagen2Saludos, imagen2Original);
                }
            });

        } else if (vistaLeccion instanceof Vista_LeccionFONOLOGIA) {
            Vista_LeccionFONOLOGIA vista = (Vista_LeccionFONOLOGIA) vistaLeccion;
            vista.jButtonCOMPLETOFONOLOGIA.addActionListener(e -> completarLeccion());

        } else if (vistaLeccion instanceof Vista_LeccionPRONOMBRES) {
            Vista_LeccionPRONOMBRES vista = (Vista_LeccionPRONOMBRES) vistaLeccion;
            vista.jButtonCOMPLETOPRONOMBRES.addActionListener(e -> completarLeccion());
        }
    }

    private void completarLeccion() {
        try {
            Modelo_Progreso_Usuario progreso = ControladorProgresoUsuario.obtenerProgreso(idUsuario, ID_UNIDAD);

            if (progreso.getLeccionesCompletadas() >= numeroLeccion) {
                int respuesta = JOptionPane.showConfirmDialog(
                        vistaLeccion,
                        "Ya has completado esta lección. ¿Quieres marcarla como completada nuevamente?",
                        "Lección ya completada",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE
                );

                if (respuesta != JOptionPane.YES_OPTION) {
                    return;
                }
            }

            boolean actualizado = ControladorProgresoUsuario.actualizarLeccion(progreso, numeroLeccion);

            if (actualizado || progreso.getLeccionesCompletadas() >= numeroLeccion) {
                mostrarMensajeExito();
                actualizarInterfaz();
            } else {
                JOptionPane.showMessageDialog(
                        vistaLeccion,
                        "Error al actualizar el progreso",
                        "Error",
                        JOptionPane.ERROR_MESSAGE
                );
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(
                    vistaLeccion,
                    "Error inesperado al completar la lección: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void mostrarMensajeExito() {
        String nombreLeccion = obtenerNombreLeccion();
        JOptionPane.showMessageDialog(
                vistaLeccion,
                "¡Felicitaciones! Has completado la " + nombreLeccion + " exitosamente.",
                "Lección Completada",
                JOptionPane.INFORMATION_MESSAGE
        );
    }

    private String obtenerNombreLeccion() {
        switch (numeroLeccion) {
            case LECCION_SALUDOS:
                return "Lección de Saludos";
            case LECCION_FONOLOGIA:
                return "Lección de Fonología";
            case LECCION_PRONOMBRES:
                return "Lección de Pronombres";
            default:
                return "Lección " + numeroLeccion;
        }
    }

    private void actualizarInterfaz() {
        Vista_Unidad1 vistaUnidad1 = new Vista_Unidad1();
        new Controlador_Unidad1(vistaUnidad1, conn, controladorDashboard, correo, controladorUnidades);
        controladorDashboard.getVista().mostrarVista(vistaUnidad1);
    }

    private void configurarVistaLeccionSaludos() {
        if (vistaLeccion instanceof Vista_LeccionSALUDOS) {
            Vista_LeccionSALUDOS vista = (Vista_LeccionSALUDOS) vistaLeccion;

            List<Modelo_Lecciones> textos = Modelo_Lecciones.obtenerLeccionesPorUnidadYTipo(conn, ID_UNIDAD, "texto");
            List<Modelo_Lecciones> imagenes = Modelo_Lecciones.obtenerLeccionesPorUnidadYTipo(conn, ID_UNIDAD, "imagen");

            if (textos.size() > 0) {
                vista.jTextAreaTexto1Saludos.setText(textos.get(0).getContenido());
            }
            if (textos.size() > 1) {
                vista.jTextAreaTexto2Saludos.setText(textos.get(1).getContenido());
            }

            String rutaImagen1 = "Imagenes/Lecciones/Imagen1Saludos.png";
            java.net.URL url1 = getClass().getClassLoader().getResource(rutaImagen1);
            if (url1 != null) {
                imagen1Original = new ImageIcon(url1);
                escalarYMostrarImagen(vista.jLabelImagen1Saludos, imagen1Original);

                vista.jLabelImagen1Saludos.addComponentListener(new ComponentAdapter() {
                    @Override
                    public void componentResized(ComponentEvent e) {
                        escalarYMostrarImagen(vista.jLabelImagen1Saludos, imagen1Original);
                    }
                });
            }

            String rutaImagen2 = "Imagenes/Lecciones/Imagen2Saludos.png";
            java.net.URL url2 = getClass().getClassLoader().getResource(rutaImagen2);
            if (url2 != null) {
                imagen2Original = new ImageIcon(url2);
                escalarYMostrarImagen(vista.jLabelImagen2Saludos, imagen2Original);

                vista.jLabelImagen2Saludos.addComponentListener(new ComponentAdapter() {
                    @Override
                    public void componentResized(ComponentEvent e) {
                        escalarYMostrarImagen(vista.jLabelImagen2Saludos, imagen2Original);
                    }
                });
            }
        }
    }

    private void escalarYMostrarImagen(JLabel label, ImageIcon imagenOriginal) {
        int ancho = label.getWidth();
        int alto = label.getHeight();

        if (ancho > 0 && alto > 0 && imagenOriginal != null) {
            Image imagenEscalada = imagenOriginal.getImage().getScaledInstance(ancho, alto, Image.SCALE_SMOOTH);
            label.setIcon(new ImageIcon(imagenEscalada));
        }
    }

    public String obtenerEstadisticasProgreso() {
        Modelo_Progreso_Usuario progreso = ControladorProgresoUsuario.obtenerProgreso(idUsuario, ID_UNIDAD);

        int totalLecciones = 3;
        int totalActividades = 2;

        double porcentajeLecciones = (double) progreso.getLeccionesCompletadas() / totalLecciones * 100;
        double porcentajeActividades = (double) progreso.getActividadesCompletadas() / totalActividades * 100;

        return String.format(
                "Lecciones: %d/%d (%.1f%%) | Actividades: %d/%d (%.1f%%) | Evaluación: %s | Calificación: %d",
                progreso.getLeccionesCompletadas(), totalLecciones, porcentajeLecciones,
                progreso.getActividadesCompletadas(), totalActividades, porcentajeActividades,
                progreso.isEvaluacionAprobada() ? "Aprobada" : "Pendiente",
                progreso.getCalificacion()
        );
    }
}
