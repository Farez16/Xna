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
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
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
        vista.btnMenu1.addActionListener(e -> AbrirVistaInicio());
        vista.btnCrearAdmin.addActionListener(e -> AbrirVistaRegistrar());
        vista.btnCuenta1.addActionListener(e -> abrirCuenta());
        vista.btnSalir1.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(
                    null,
                    "¿Deseas cerrar sesión?",
                    "Confirmar",
                    JOptionPane.YES_NO_OPTION
            );
            if (confirm == JOptionPane.YES_OPTION) {
                limpiarControladorSaludo();
                loginFrame.mostrarPanelEnPanel1(loginFrame.getPanelLoginOriginal());
                loginFrame.limpiarCampos();
            }
        });
    }

    private void mostrarVistaInicial() {
        VistaMostrarAdmin vistaInicial = new VistaMostrarAdmin();
        MostrarDatosAdmin modelo = new MostrarDatosAdmin(conn);
        ControladorMostrar controlador = new ControladorMostrar(vistaInicial, modelo);
        vista.mostrarVista(vistaInicial);
    }

    private void AbrirVistaInicio() {
        VistaMostrarAdmin vistaInicio = new VistaMostrarAdmin();
        MostrarDatosAdmin modelo = new MostrarDatosAdmin(conn);
        ControladorMostrar controlador = new ControladorMostrar(vistaInicio, modelo);
        vista.mostrarVista(vistaInicio);
    }

    private void AbrirVistaRegistrar() {
        RegistrarAdmin va = new RegistrarAdmin();
        ControladorRegistrarAdmin controlador = new ControladorRegistrarAdmin(va);
        vista.mostrarVista(va);
    }

    private void abrirCuenta() {
        try {
            Cuenta cuentaPanel = new Cuenta();
            new ControladorCuenta(cuentaPanel, vista.getCorreoUsuario(), vista);
            vista.mostrarVista(cuentaPanel);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(vista, "Error al abrir la cuenta: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void cargarDatosUsuario(String correo) {
        try {
            String[] datos = usuario.obtenerNombreYRol(correo);
            if (datos[0] != null && datos[1] != null) {
                vista.setNombreUsuario(datos[0]);
                vista.setRolUsuario(datos[1]);

                if (controladorSaludo != null) {
                    controladorSaludo.detener();
                }
                Saludo saludo = new Saludo("Bienvenido, Administrador");
                controladorSaludo = new ControladorSaludo(
                        vista.getLblSaludo(),
                        vista.getLblNombre(),
                        saludo
                );
                controladorSaludo.inicializarVista();
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(vista, "Error al cargar los datos del usuario: " + ex.getMessage(), "Error de Base de Datos", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void limpiarControladorSaludo() {
        if (controladorSaludo != null) {
            controladorSaludo.detener();
            controladorSaludo = null;
        }
    }

    private void cargarImagenUsuario() {
        try {
            String rutaImagen = usuario.obtenerImagenUsuario(vista.getCorreoUsuario());
            if (rutaImagen != null && !rutaImagen.isEmpty()) {
                actualizarImagenPerfil(rutaImagen);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(vista, "Error al cargar la imagen del usuario: " + ex.getMessage(), "Error de Base de Datos", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void actualizarImagenPerfil(String ruta) {
        try {
            BufferedImage img;
            if (ruta.startsWith("http")) {
                img = ImageIO.read(new URL(ruta));
            } else {
                img = ImageIO.read(new File(ruta));
            }

            int size = 100;
            BufferedImage circleBuffer = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2 = circleBuffer.createGraphics();
            Ellipse2D.Double circle = new Ellipse2D.Double(0, 0, size, size);
            g2.setClip(circle);
            g2.drawImage(img, 0, 0, size, size, null);
            g2.dispose();

            vista.LblimagenPrincipal.setIcon(new ImageIcon(circleBuffer));
            vista.LblimagenPrincipal.revalidate();
            vista.LblimagenPrincipal.repaint();

        } catch (Exception ex) {
            System.err.println("Error cargando imagen en Dashboard: " + ex.getMessage());
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
        ControladorBotones controlBotones = new ControladorBotones(botonesAdmin, textos);
        controlBotones.iniciar();
    }
}
