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
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
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
            JOptionPane.showMessageDialog(vista, "Error: No se pudo establecer conexión con la base de datos", "Error de Conexión", JOptionPane.ERROR_MESSAGE);
        }

        agregarEventos();

        panelUnidades = new Vista_PanelUnidades();
        controladorUnidades = new Controlador_Unidades(panelUnidades, vista, this, vista.getCorreoUsuario());

        vista.mostrarVista(panelUnidades);

        cargarDatosUsuario(vista.getCorreoUsuario());
        cargarImagenUsuario();
        configurarBotonesAnimados();
    }

    public Connection getConnection() {
        return conn;
    }

    private void agregarEventos() {
        vista.btnJuegos.addActionListener((ActionEvent e) -> {
            abrirVistaJuego();
        });
        vista.btnDashboard.addActionListener(e -> abrirPanelUnidades());
        vista.btnCuenta.addActionListener(e -> abrirCuenta());
        vista.btnSalir.addActionListener(e -> {
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
            JOptionPane.showMessageDialog(vista, "Error al abrir la vista de cuenta: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
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
            JOptionPane.showMessageDialog(vista, "Error al cargar los datos del usuario: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void cargarImagenUsuario() {
        try {
            String rutaImagen = usuario.obtenerImagenUsuario(vista.getCorreoUsuario());
            if (rutaImagen != null && !rutaImagen.isEmpty()) {
                actualizarImagenPerfil(rutaImagen);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(vista, "Error al cargar la imagen del usuario: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
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
        Map<String, JButton> botonesEstudiante = new HashMap<>();
        botonesEstudiante.put("btnDashboard", vista.getBtnDashboard());
        botonesEstudiante.put("btnCuenta", vista.getBtnCuenta());
        botonesEstudiante.put("btnCertificado", vista.getBtnCertificado());
        botonesEstudiante.put("btnJuegos", vista.getBtnJuegos());
        botonesEstudiante.put("btnSalir", vista.getBtnSalir());

        TextoBotones textos = new TextoBotones();
        ControladorBotones controlBotones = new ControladorBotones(botonesEstudiante, textos);
        controlBotones.iniciar();
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
