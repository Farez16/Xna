package Controlador;

import Conexion.Conexion;
import Modelo.EmailSender;
import Modelo.Usuario;
import Modelo.UsuarioPerfil;
import Vista.Cuenta;
import Vista.Dashboard;
import Vista.DashboardAdmin;
import java.awt.image.BufferedImage;
import java.io.File;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import javax.imageio.ImageIO;
import javax.swing.JFileChooser;

public class ControladorCuenta {

    private final Cuenta vista;
    private final Connection conn;
    private final Usuario modeloUsuario;
    private UsuarioPerfil usuario;
    private final String correoUsuario;
    private final Dashboard dashboard;
    private final DashboardAdmin dashboardAdmin;
    private final EmailSender emailSender;

    public ControladorCuenta(Cuenta vista, String correo, Dashboard dashboard) {
        this.vista = vista;
        this.conn = Conexion.conectar();
        this.modeloUsuario = new Usuario(conn);
        this.correoUsuario = correo;
        this.dashboard = dashboard;
        this.dashboardAdmin = null;
        this.emailSender = new EmailSender();
        cargarDatosUsuario(correo);
        inicializarEventos();
    }

    public ControladorCuenta(Cuenta vista, String correo, DashboardAdmin dashboardAdmin) {
        this.vista = vista;
        this.conn = Conexion.conectar();
        this.modeloUsuario = new Usuario(conn);
        this.correoUsuario = correo;
        this.dashboardAdmin = dashboardAdmin;
        this.dashboard = null;
        this.emailSender = new EmailSender();
        cargarDatosUsuario(correo);
        inicializarEventos();
    }

    private void cargarDatosUsuario(String correo) {
        try {
            PreparedStatement ps = conn.prepareStatement(
                    "SELECT correo, ruta_imagen, fecha_registro, ultima_sesion "
                            + "FROM usuarios WHERE correo = ?"
            );
            ps.setString(1, correo);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                usuario = new UsuarioPerfil(
                        rs.getString("correo"),
                        rs.getString("ruta_imagen"),
                        rs.getTimestamp("fecha_registro"),
                        rs.getTimestamp("ultima_sesion")
                );

                vista.setUsuario(usuario.getCorreo());
                vista.setContrasena("********");
                DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
                vista.setFechaHora(dtf.format(LocalDateTime.now()));
                Timestamp registro = usuario.getFechaRegistro();
                vista.setFechaRegistro(new SimpleDateFormat("dd/MM/yyyy").format(registro));
                Timestamp ultimoAcceso = usuario.getUltimaSesion();
                if (ultimoAcceso != null) {
                    LocalDateTime ldt = ultimoAcceso.toInstant()
                            .atZone(ZoneId.systemDefault())
                            .toLocalDateTime();
                    vista.setUltimoAcceso(" | Último acceso: " + dtf.format(ldt));
                }

                vista.mostrarImagen(usuario.getRutaImagen());
            }
        } catch (Exception e) {
            vista.mostrarMensaje("Error al cargar datos: " + e.getMessage());
        }
    }

    private void inicializarEventos() {
        vista.onSubirImagen(e -> mostrarMenuSubirImagen());
        vista.onCambiarContrasena(e -> cambiarContraseña());
    }

    private void mostrarMenuSubirImagen() {
        int eleccion = vista.mostrarMenuSubirImagen();
        if (eleccion == 0) {
            seleccionarImagenArchivo();
        } else if (eleccion == 1) {
            ingresarURLImagen();
        }
    }

    private void seleccionarImagenArchivo() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setAcceptAllFileFilterUsed(false);
        fileChooser.addChoosableFileFilter(new javax.swing.filechooser.FileNameExtensionFilter(
                "Imágenes (*.jpg, *.jpeg, *.png)", "jpg", "jpeg", "png"));

        int resultado = fileChooser.showOpenDialog(vista);
        if (resultado == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            actualizarImagen(file.getAbsolutePath());
        }
    }

    private void ingresarURLImagen() {
        String url = vista.ingresarURLImagen();
        if (url != null && !url.trim().isEmpty()) {
            actualizarImagen(url);
        }
    }

    private void actualizarImagen(String nuevaRuta) {
        try {
            modeloUsuario.actualizarImagenUsuario(correoUsuario, nuevaRuta);
            usuario.setRutaImagen(nuevaRuta);
            vista.mostrarImagen(nuevaRuta);

            if (dashboard != null) {
                dashboard.actualizarImagenPerfil(nuevaRuta);
            }
            if (dashboardAdmin != null) {
                dashboardAdmin.actualizarImagenPerfil(nuevaRuta);
            }

            vista.mostrarMensaje("¡Imagen actualizada correctamente en todos los paneles!");
        } catch (SQLException e) {
            vista.mostrarMensaje("Error al actualizar imagen: " + e.getMessage());
        }
    }

    private void cambiarContraseña() {
        String nueva = vista.ingresarNuevaContrasena();
        if (nueva != null) {
            if (nueva.length() >= 6) {
                try {
                    String actual = modeloUsuario.getContrasenaActual(correoUsuario);
                    if (nueva.equals(actual)) {
                        vista.mostrarMensaje("La nueva contraseña no puede ser igual a la actual");
                        return;
                    }

                    PreparedStatement ps = conn.prepareStatement(
                            "UPDATE usuarios SET contrasena = ? WHERE correo = ?"
                    );
                    ps.setString(1, nueva);
                    ps.setString(2, correoUsuario);

                    int resultado = ps.executeUpdate();
                    if (resultado > 0) {
                        String asunto = "Contraseña actualizada";
                        String mensaje = "Su nueva contraseña es: " + nueva;
                        emailSender.enviarCorreo(correoUsuario, asunto, mensaje);

                        vista.mostrarMensaje("¡Contraseña actualizada y enviada a su correo!");
                        vista.setContrasena("********");
                    } else {
                        vista.mostrarMensaje("No se pudo actualizar la contraseña");
                    }
                } catch (Exception e) {
                    vista.mostrarMensaje("Error al cambiar contraseña: " + e.getMessage());
                }
            } else {
                vista.mostrarMensaje("La contraseña debe tener al menos 6 caracteres");
            }
        }
    }
}
