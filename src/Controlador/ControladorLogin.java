package Controlador;

import Conexion.Conexion;
import Modelo.EmailSender;
import Modelo.Usuario;
import Vista.Dashboard;
import Vista.DashboardAdmin;
import Vista.Login;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.SQLException;

public class ControladorLogin implements ActionListener {

    private final Login vista;
    private final Usuario usuario;
    private final Connection conn;
    private final EmailSender emailSender;

    public ControladorLogin(Login vista) {
        this.vista = vista;
        this.conn = Conexion.conectar();
        this.usuario = new Usuario(conn);
        this.emailSender = new EmailSender();
        this.vista.getBtnInicar().addActionListener(this);
        this.vista.getBtnCodigo().addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == vista.getBtnInicar()) {
            iniciarSesion();
        } else if (e.getSource() == vista.getBtnCodigo()) {
            abrirPanelRegistro();
        }
    }

    private void iniciarSesion() {
        String correo = vista.getTxtUsuario().getText().trim();
        String contrasena = new String(vista.getTxtContraseña().getPassword());

        if (correo.isEmpty() || contrasena.isEmpty() || correo.equals("Usuario") || contrasena.equals("Contraseña")) {
            vista.mostrarMensaje("Por favor, complete todos los campos");
            return;
        }

        try {
            if (usuario.estaRegistrado(correo)) {
                if (usuario.verificarContrasena(correo, contrasena)) {
                    vista.mostrarMensaje("Inicio de sesión exitoso");
                    emailSender.enviarConfirmacion(correo);
                    abrirDashboard(correo);
                } else {
                    vista.mostrarMensaje("Correo o contraseña incorrecta");
                }
            } else {
                vista.mostrarMensaje("No existe una cuenta con ese correo. Por favor, crea una cuenta.");
                vista.resaltarBotonCrearCuenta();
            }
        } catch (SQLException ex) {
            vista.mostrarMensaje("Error al conectar con la base de datos: " + ex.getMessage());
        }
    }

    private void abrirPanelRegistro() {
        String correo = vista.getTxtUsuario().getText().trim();
        vista.abrirPanelRegistro(correo);
    }

    private void abrirDashboard(String correo) {
        try {
            int rol = usuario.obtenerRolUsuario(correo);
            if (rol == 2) {
                vista.mostrarPanelEnPanel1(new DashboardAdmin(correo));
            } else {
                vista.mostrarPanelEnPanel1(new Dashboard(correo));
            }
        } catch (SQLException ex) {
            vista.mostrarMensaje("Error al obtener el rol del usuario: " + ex.getMessage());
        }
    }
}
