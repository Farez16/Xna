package Controlador;

import Conexion.Conexion;
import Modelo.MostrarDatosAdmin;
import Modelo.Saludo;
import Modelo.TextoBotones;
import Modelo.Usuario;
import Vista.Cuenta;
import Vista.DashboardAdmin;
import Vista.Login;
import Vista.RegistrarAdmin;
import Vista.VistaMostrarAdmin;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JButton;
import javax.swing.JOptionPane;

public class ControladorDashboardAdmin {

    private final DashboardAdmin vista;
    private final Login loginFrame;
    private ControladorSaludo controladorSaludo;
    private final Usuario usuario;
    private final Connection conn;

    public ControladorDashboardAdmin(DashboardAdmin vista, Login loginFrame) {
        this.vista = vista;
        this.loginFrame = loginFrame;
        this.conn = Conexion.conectar();
        this.usuario = new Usuario(conn);

        agregarEventos();
        cargarDatosUsuario(vista.getCorreoUsuario());
        cargarImagenUsuario();
        configurarBotonesAnimados();
        mostrarVistaInicial();

        vista.getPanelVistas().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        javax.swing.SwingUtilities.invokeLater(this::mostrarVistaInicial);
    }

    private void agregarEventos() {
        vista.getBtnMenu1().addActionListener(e -> AbrirVistaInicio());
        vista.getBtnCrearAdmin().addActionListener(e -> AbrirVistaRegistrar());
        vista.getBtnCuenta1().addActionListener(e -> abrirCuenta());
        vista.getBtnSalir1().addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(
                    null,
                    "¿Deseas cerrar sesión?",
                    "Confirmar",
                    JOptionPane.YES_NO_OPTION
            );
            if (confirm == JOptionPane.YES_OPTION) {
                loginFrame.mostrarPanelEnPanel1(loginFrame.getPanelLoginOriginal());
                loginFrame.limpiarCampos();
            }
        });
    }

    private void mostrarVistaInicial() {
        VistaMostrarAdmin vistaInicial = new VistaMostrarAdmin();
        MostrarDatosAdmin modelo = new MostrarDatosAdmin(conn);
        new ControladorMostrar(vistaInicial, modelo);
        vista.mostrarVista(vistaInicial);
    }

    private void AbrirVistaInicio() {
        VistaMostrarAdmin vistaInicio = new VistaMostrarAdmin();
        MostrarDatosAdmin modelo = new MostrarDatosAdmin(conn);
        new ControladorMostrar(vistaInicio, modelo);
        vista.mostrarVista(vistaInicio);
    }

    private void AbrirVistaRegistrar() {
        RegistrarAdmin va = new RegistrarAdmin();
        new ControladorRegistrarAdmin(va);
        vista.mostrarVista(va);
    }

    private void abrirCuenta() {
        try {
            Cuenta cuentaPanel = new Cuenta();
            new ControladorCuenta(cuentaPanel, vista.getCorreoUsuario(), vista);
            vista.mostrarVista(cuentaPanel);
        } catch (Exception e) {
            vista.mostrarMensaje("Error al abrir la cuenta: " + e.getMessage());
        }
    }

    private void cargarDatosUsuario(String correo) {
        try {
            String[] datos = usuario.obtenerNombreYRol(correo);
            if (datos[0] != null && datos[1] != null) {
                vista.setNombreUsuario(datos[0]);
                vista.setRolUsuario(datos[1]);

                Saludo saludo = new Saludo("Bienvenido, Administrador");
                controladorSaludo = new ControladorSaludo(
                        vista.getLblSaludo(),
                        vista.getLblNombre(),
                        saludo
                );
                controladorSaludo.inicializarVista();
            }
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
        Map<String, JButton> botonesAdmin = new HashMap<>();
        botonesAdmin.put("btnMenu1", vista.getBtnCuenta1());
        botonesAdmin.put("btnCuenta1", vista.getBtnCuenta1());
        botonesAdmin.put("btnGraficos1", vista.getBtnGraficos1());
        botonesAdmin.put("btnJuegos1", vista.getBtnJuegos1());
        botonesAdmin.put("btnSalir1", vista.getBtnSalir1());

        TextoBotones textos = new TextoBotones();
        new ControladorBotones(botonesAdmin, textos).iniciar();
    }
}
