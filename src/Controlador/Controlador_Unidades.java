package Controlador;

import Conexion.Conexion;
import Modelo.Modelo_Unidades;
import Vista.Dashboard;
import Vista.Vista_PanelUnidades;
import Vista.Vista_Unidad1;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.util.List;
import javax.swing.JLabel;

public class Controlador_Unidades {

    private static final int TOTAL_LECCIONES_POR_UNIDAD = 5;
    private static final int TOTAL_ACTIVIDADES_POR_UNIDAD = 3;
    private static final int TOTAL_UNIDADES = 4;

    private final Vista_PanelUnidades vista;
    private final ControladorDashboard controladorDashboard;
    private final Dashboard dashboard;
    private final String correo;
    private boolean[] unidadesDisponibles;

    public Controlador_Unidades(Vista_PanelUnidades vista, Dashboard dashboard,
                               ControladorDashboard controladorDashboard, String correo) {

        if (vista == null) {
            throw new IllegalArgumentException("La vista no puede ser null");
        }
        if (controladorDashboard == null) {
            throw new IllegalArgumentException("El controlador dashboard no puede ser null");
        }
        if (correo == null || correo.trim().isEmpty()) {
            throw new IllegalArgumentException("El correo no puede ser null o vacío");
        }

        this.vista = vista;
        this.dashboard = dashboard;
        this.controladorDashboard = controladorDashboard;
        this.correo = correo;
        this.unidadesDisponibles = new boolean[TOTAL_UNIDADES + 1];

        inicializar();
    }

    private void inicializar() {
        try {
            cargarUnidades();
            agregarEventos();
        } catch (Exception e) {
            vista.mostrarError("Error al inicializar el panel de unidades");
        }
    }

    private void cargarUnidades() {
        try {
            unidadesDisponibles = Modelo_Unidades.obtenerDisponibilidadUnidades(correo);
            List<Modelo_Unidades> unidades = Modelo_Unidades.obtenerUnidadesConProgreso(correo);

            for (Modelo_Unidades unidad : unidades) {
                actualizarVistaUnidad(unidad);
            }

            int progresoGeneral = obtenerProgresoGeneral();
            actualizarProgresoGeneral(progresoGeneral);

        } catch (Exception e) {
            vista.mostrarError("Error al cargar las unidades. Por favor, intenta de nuevo.");
        }
    }

    private void actualizarVistaUnidad(Modelo_Unidades unidad) {
        int idUnidad = unidad.getIdUnidad();
        if (idUnidad < 1 || idUnidad > TOTAL_UNIDADES) {
            return;
        }

        boolean disponible = unidadesDisponibles[idUnidad];
        JLabel labelUnidad = obtenerLabelUnidad(idUnidad);
        JLabel labelEstado = obtenerLabelEstado(idUnidad);
        JLabel labelProgreso = obtenerLabelProgreso(idUnidad);

        if (labelUnidad != null && labelEstado != null) {
            unidad.setDisponible(disponible);
            String colorEstado = unidad.getColorEstado();
            String estadoTexto = unidad.getEstadoTexto();

            labelUnidad.setForeground(Color.decode(colorEstado));
            labelEstado.setText(estadoTexto);
            labelEstado.setForeground(Color.decode(colorEstado));

            int progresoUnidad = unidad.calcularProgresoTotal(
                    TOTAL_LECCIONES_POR_UNIDAD, TOTAL_ACTIVIDADES_POR_UNIDAD);

            if (labelProgreso != null) {
                labelProgreso.setText(progresoUnidad + "%");
                labelProgreso.setForeground(Color.decode(colorEstado));
            }

            if (disponible) {
                labelUnidad.setCursor(new Cursor(Cursor.HAND_CURSOR));
                labelUnidad.setEnabled(true);
            } else {
                labelUnidad.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                labelUnidad.setEnabled(false);
            }
        }
    }

    private void actualizarProgresoGeneral(int progresoGeneral) {
        vista.actualizarProgresoGeneral(progresoGeneral);
    }

    private JLabel obtenerLabelUnidad(int idUnidad) {
        return switch (idUnidad) {
            case 1 -> vista.getjLabelUNIDAD1();
            case 2 -> vista.getjLabelUNIDAD2();
            case 3 -> vista.getjLabelUNIDAD3();
            case 4 -> vista.getjLabelUNIDAD4();
            default -> null;
        };
    }

    private JLabel obtenerLabelEstado(int idUnidad) {
        return switch (idUnidad) {
            case 1 -> vista.getjLabelEstadoU1();
            case 2 -> vista.getjLabelEstadoU2();
            case 3 -> vista.getjLabelEstadoU3();
            case 4 -> vista.getjLabelEstadoU4();
            default -> null;
        };
    }

    private JLabel obtenerLabelProgreso(int idUnidad) {
        return switch (idUnidad) {
            case 1 -> vista.getjLabelProgresoU1();
            case 2 -> vista.getjLabelProgresoU2();
            case 3 -> vista.getjLabelProgresoU3();
            case 4 -> vista.getjLabelProgresoU4();
            default -> null;
        };
    }

    private void agregarEventos() {
        configurarEventosUnidad(vista.getjLabelUNIDAD1(), 1, "Saludos y Presentaciones");
        configurarEventosUnidad(vista.getjLabelUNIDAD2(), 2, "Familia y Hogar");
        configurarEventosUnidad(vista.getjLabelUNIDAD3(), 3, "Naturaleza y Animales");
        configurarEventosUnidad(vista.getjLabelUNIDAD4(), 4, "Números y Colores");
    }

    private void configurarEventosUnidad(JLabel labelUnidad, int idUnidad, String nombreUnidad) {
        if (labelUnidad == null) return;

        labelUnidad.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                abrirUnidad(idUnidad, nombreUnidad);
            }
        });
    }

    private void abrirUnidad(int idUnidad, String nombreUnidad) {
        try {
            if (idUnidad >= unidadesDisponibles.length || !unidadesDisponibles[idUnidad]) {
                vista.mostrarMensaje("Esta unidad no está disponible aún.\nCompleta la unidad anterior para desbloquearla.");
                return;
            }

            Modelo_Unidades.registrarVisualizacionUnidad(idUnidad, correo);
            vista.mostrarMensajeBienvenida(nombreUnidad);

            switch (idUnidad) {
                case 1 -> abrirUnidad1();
                case 2, 3, 4 -> vista.mostrarUnidadEnDesarrollo(idUnidad, nombreUnidad);
                default -> System.err.println("ID de unidad no válido: " + idUnidad);
            }

        } catch (Exception e) {
            vista.mostrarError("Error al abrir la unidad. Por favor, intenta de nuevo.");
        }
    }

    private void abrirUnidad1() {
        try {
            Vista_Unidad1 unidad1 = new Vista_Unidad1();
            Connection conn = (Connection) controladorDashboard.getConnection();
            new Controlador_Unidad1(unidad1, conn,
                    controladorDashboard, correo, this);
            dashboard.mostrarVista(unidad1);
        } catch (Exception e) {
            vista.mostrarError("Error al abrir la unidad 1. Por favor, intenta de nuevo.");
        }
    }

    private int obtenerProgresoGeneral() {
        try {
            return Modelo_Unidades.obtenerProgresoGeneral(correo);
        } catch (Exception e) {
            return 0;
        }
    }

    public void actualizarVista() {
        try {
            cargarUnidades();
        } catch (Exception e) {
            vista.mostrarError("Error al actualizar la vista de unidades");
        }
    }

    public void actualizarProgresoLecciones(int idUnidad, int leccionesCompletadas) {
        try {
            if (Modelo_Unidades.actualizarProgresoLecciones(idUnidad, correo, leccionesCompletadas)) {
                actualizarVista();
            }
        } catch (Exception e) {
            System.err.println("Error al actualizar progreso de lecciones: " + e.getMessage());
        }
    }

    public void actualizarProgresoActividades(int idUnidad, int actividadesCompletadas) {
        try {
            if (Modelo_Unidades.actualizarProgresoActividades(idUnidad, correo, actividadesCompletadas)) {
                actualizarVista();
            }
        } catch (Exception e) {
            System.err.println("Error al actualizar progreso de actividades: " + e.getMessage());
        }
    }

    public void registrarEvaluacionAprobada(int idUnidad, int calificacion) {
        try {
            if (Modelo_Unidades.registrarEvaluacionAprobada(idUnidad, correo, calificacion)) {
                actualizarVista();
                vista.mostrarMensaje("¡Felicitaciones! Has completado la unidad " + idUnidad
                        + " con una calificación de " + calificacion + "%");
            }
        } catch (Exception e) {
            System.err.println("Error al registrar evaluación aprobada: " + e.getMessage());
        }
    }
}