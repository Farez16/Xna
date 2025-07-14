package Controlador;

import Conexion.Conexion;
import Modelo.Juego;
import Modelo.Saludo;
import Modelo.TextoBotones;
import Modelo.Usuario;
import Vista.Cuenta;
import Vista.Dashboard;
import Vista.Login;
import Vista.VistaJuego;
import Vista.Vista_PanelUnidades;
import java.awt.event.ActionEvent;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JButton;
import javax.swing.JOptionPane;

public class ControladorDashboard {

    private final Dashboard vista;
    private final Login loginFrame;
    private ControladorSaludo controladorSaludo;

    private final Vista_PanelUnidades panelUnidades;
    private final Controlador_Unidades controladorUnidades;
    private final Connection conn;
    private final Usuario usuario;

    public ControladorDashboard(Dashboard vista, Login loginFrame) {
        this.vista = vista;
        this.loginFrame = loginFrame;
        this.conn = Conexion.conectar();
        this.usuario = new Usuario(conn);

        if (this.conn == null) {
            vista.mostrarMensaje("Error: No se pudo establecer conexión con la base de datos");
        }

        agregarEventos();
        configurarBotonesAnimados();

        panelUnidades = new Vista_PanelUnidades();
        controladorUnidades = new Controlador_Unidades(panelUnidades, vista, this, vista.getCorreoUsuario());

        vista.mostrarVista(panelUnidades);

        cargarDatosUsuario(vista.getCorreoUsuario());
        cargarImagenUsuario();
    }

    public Connection getConnection() {
        return conn;
    }

    private void agregarEventos() {
        vista.getBtnJuegos().addActionListener((ActionEvent e) -> {
            abrirVistaJuego();
        });
        vista.getBtnDashboard().addActionListener(e -> abrirPanelUnidades());
        vista.getBtnCuenta().addActionListener(e -> abrirCuenta());
        vista.getBtnSalir().addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(null, "¿Deseas salir?", "Cerrar sesión", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                if (controladorSaludo != null) {
                    controladorSaludo.detener();
                }
                loginFrame.mostrarPanelEnPanel1(loginFrame.getPanelLoginOriginal());
                loginFrame.limpiarCampos();
            }
        });
    }

    private void abrirPanelUnidades() {
        vista.mostrarVista(panelUnidades);
    }

    private void abrirCuenta() {
        try {
            Cuenta cuentaPanel = new Cuenta();
            new ControladorCuenta(cuentaPanel, vista.getCorreoUsuario(), vista);
            vista.mostrarVista(cuentaPanel);
        } catch (Exception e) {
            vista.mostrarMensaje("Error al abrir la vista de cuenta: " + e.getMessage());
        }
    }

    private void abrirVistaJuego() {
        VistaJuego va = new VistaJuego();
        Juego modeloJuego = new Juego(conn);
        ControladorJuego controlador = new ControladorJuego(va, modeloJuego);
        vista.mostrarVista(va);
    }

    public void limpiarControladorSaludo() {
        if (controladorSaludo != null) {
            controladorSaludo.detener();
            controladorSaludo = null;
        }
    }

    private void cargarDatosUsuario(String correo) {
        try {
            String[] datos = usuario.obtenerNombreYRol(correo);
            if (datos[0] != null && datos[1] != null) {
                vista.setNombreUsuario(datos[0]);
                vista.setRolUsuario(datos[1]);
            }
            if (controladorSaludo != null) {
                controladorSaludo.detener();
            }
            Saludo saludo = new Saludo("Bienvenido");
            controladorSaludo = new ControladorSaludo(
                    vista.getLblSaludo(),
                    vista.getLblNombre(),
                    saludo
            );
            controladorSaludo.inicializarVista();
        } catch (SQLException ex) {
            vista.mostrarMensaje("Error al cargar los datos del usuario: " + ex.getMessage());
        }
    }

    private void cargarImagenUsuario() {
        try {
            String rutaImagen = usuario.obtenerImagenUsuario(vista.getCorreoUsuario());
            if (rutaImagen != null && !rutaImagen.isEmpty()) {
                vista.actualizarImagenPerfil(rutaImagen);
            }
        } catch (SQLException ex) {
            vista.mostrarMensaje("Error al cargar la imagen del usuario: " + ex.getMessage());
        }
    }

    private void configurarBotonesAnimados() {
        Map<String, JButton> botonesEstudiante = new HashMap<>();
        botonesEstudiante.put("btnDashboard", vista.getBtnDashboard());
        botonesEstudiante.put("btnCuenta", vista.getBtnCuenta());
        botonesEstudiante.put("btnCertificado", vista.getBtnCertificado());
        botonesEstudiante.put("btnJuegos", vista.getBtnJuegos());
        botonesEstudiante.put("btnSalir", vista.getBtnSalir());

        TextoBotones textos = new TextoBotones();
        new ControladorBotones(botonesEstudiante, textos).iniciar();
    }

    public Vista_PanelUnidades getPanelUnidades() {
        return panelUnidades;
    }

    public Controlador_Unidades getControladorUnidades() {
        return controladorUnidades;
    }

    public Dashboard getVista() {
        return vista;
    }
}
