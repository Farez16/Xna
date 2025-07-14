package Controlador;

import Conexion.Conexion;
import Modelo.EmailSender;
import Modelo.OTPService;
import Modelo.Usuario;
import Vista.ContrasenaNueva;
import Vista.Dashboard;
import Vista.DashboardAdmin;
import Vista.Login;
import java.awt.Color;
import java.sql.Connection;
import java.sql.SQLException;
import javax.swing.JDialog;
import javax.swing.JOptionPane;

public class ControladorContrasenaNueva {

    private final ContrasenaNueva vista;
    private final JDialog dialog;
    private final Login login;
    private final Usuario usuario;
    private final Connection conn;
    private final EmailSender emailSender;

    public ControladorContrasenaNueva(ContrasenaNueva vista, String correoUsuario, JDialog dialog, Login login) {
        this.vista = vista;
        this.dialog = dialog;
        this.login = login;
        this.conn = Conexion.conectar();
        this.usuario = new Usuario(conn);
        this.emailSender = new EmailSender();

        vista.bloquearCamposRegistro();
        this.vista.getBtnEnviarCodigo().addActionListener(e -> enviarCodigo());
        this.vista.getBtnVerificarCodigo().addActionListener(e -> verificarCodigo());
        this.vista.getBtnGuardarContraseña().addActionListener(e -> guardarContrasena());
        addFocusListeners();
    }

    private void addFocusListeners() {
        vista.getTextFieldIngresarCorreo().addFocusListener(new java.awt.event.FocusAdapter() {
            @Override
            public void focusGained(java.awt.event.FocusEvent evt) {
                if (vista.getTextFieldIngresarCorreo().getText().equals("Ingrese Correo")) {
                    vista.getTextFieldIngresarCorreo().setText("");
                    vista.getTextFieldIngresarCorreo().setForeground(Color.BLACK);
                }
            }

            @Override
            public void focusLost(java.awt.event.FocusEvent evt) {
                if (vista.getTextFieldIngresarCorreo().getText().isEmpty()) {
                    vista.getTextFieldIngresarCorreo().setText("Ingrese Correo");
                    vista.getTextFieldIngresarCorreo().setForeground(new Color(187, 187, 187));
                }
            }
        });
        vista.getTextFieldIngresarCodigoRecibido().addFocusListener(new java.awt.event.FocusAdapter() {
            @Override
            public void focusGained(java.awt.event.FocusEvent evt) {
                if (vista.getTextFieldIngresarCodigoRecibido().getText().equals("Codigo Recibido")) {
                    vista.getTextFieldIngresarCodigoRecibido().setText("");
                    vista.getTextFieldIngresarCodigoRecibido().setForeground(Color.BLACK);
                }
            }

            @Override
            public void focusLost(java.awt.event.FocusEvent evt) {
                if (vista.getTextFieldIngresarCodigoRecibido().getText().isEmpty()) {
                    vista.getTextFieldIngresarCodigoRecibido().setText("Codigo Recibido");
                    vista.getTextFieldIngresarCodigoRecibido().setForeground(new Color(187, 187, 187));
                }
            }
        });
        vista.getTextFieldIngresarNuevaContraseña().addFocusListener(new java.awt.event.FocusAdapter() {
            @Override
            public void focusGained(java.awt.event.FocusEvent evt) {
                if (vista.getTextFieldIngresarNuevaContraseña().getText().equals("Nueva Contraseña")) {
                    vista.getTextFieldIngresarNuevaContraseña().setText("");
                    vista.getTextFieldIngresarNuevaContraseña().setForeground(Color.BLACK);
                }
            }

            @Override
            public void focusLost(java.awt.event.FocusEvent evt) {
                if (vista.getTextFieldIngresarNuevaContraseña().getText().isEmpty()) {
                    vista.getTextFieldIngresarNuevaContraseña().setText("Nueva Contraseña");
                    vista.getTextFieldIngresarNuevaContraseña().setForeground(new Color(187, 187, 187));
                }
            }
        });
        vista.getTextFieldConfirmarContraseña().addFocusListener(new java.awt.event.FocusAdapter() {
            @Override
            public void focusGained(java.awt.event.FocusEvent evt) {
                if (vista.getTextFieldConfirmarContraseña().getText().equals("Confirmar Contraseña")) {
                    vista.getTextFieldConfirmarContraseña().setText("");
                    vista.getTextFieldConfirmarContraseña().setForeground(Color.BLACK);
                }
            }

            @Override
            public void focusLost(java.awt.event.FocusEvent evt) {
                if (vista.getTextFieldConfirmarContraseña().getText().isEmpty()) {
                    vista.getTextFieldConfirmarContraseña().setText("Confirmar Contraseña");
                    vista.getTextFieldConfirmarContraseña().setForeground(new Color(187, 187, 187));
                }
            }
        });
        vista.getTxtNombre().addFocusListener(new java.awt.event.FocusAdapter() {
            @Override
            public void focusGained(java.awt.event.FocusEvent evt) {
                if (vista.getTxtNombre().getText().equals("Nombre de Usuario")) {
                    vista.getTxtNombre().setText("");
                    vista.getTxtNombre().setForeground(Color.BLACK);
                }
            }

            @Override
            public void focusLost(java.awt.event.FocusEvent evt) {
                if (vista.getTxtNombre().getText().isEmpty()) {
                    vista.getTxtNombre().setText("Nombre de Usuario");
                    vista.getTxtNombre().setForeground(new Color(187, 187, 187));
                }
            }
        });
    }

    private void enviarCodigo() {
        String correo = vista.getTextFieldIngresarCorreo().getText().trim().toLowerCase();
        if (correo.isEmpty() || correo.equals("correo")) {
            JOptionPane.showMessageDialog(vista, "Ingrese un correo válido para registrar.");
            return;
        }
        if (OTPService.codigoEnviado(correo)) {
            JOptionPane.showMessageDialog(vista, "Ya se ha enviado un código a este correo. Por favor, revise su correo o espere para solicitar otro.");
            return;
        }
        String codigo = OTPService.generarYEnviarCodigo(correo);
        emailSender.enviarCodigo(correo, codigo);
        JOptionPane.showMessageDialog(vista, "Se ha enviado el código al correo.");
    }

    private void verificarCodigo() {
        String correo = vista.getTextFieldIngresarCorreo().getText().trim().toLowerCase();
        String codigo = vista.getTextFieldIngresarCodigoRecibido().getText().trim().replaceAll("[^0-9]", "");

        if (correo.isEmpty() || correo.equalsIgnoreCase("correo")) {
            JOptionPane.showMessageDialog(vista, "Ingrese el correo.");
            return;
        }
        if (codigo.isEmpty() || codigo.equalsIgnoreCase("codigo recibido")) {
            JOptionPane.showMessageDialog(vista, "Ingrese el código recibido.");
            return;
        }

        if (!OTPService.validarCodigo(correo, codigo)) {
            JOptionPane.showMessageDialog(vista, "El código ingresado no es correcto.", "Código incorrecto", JOptionPane.ERROR_MESSAGE);
            return;
        }
        JOptionPane.showMessageDialog(vista, "Código correcto. Ahora ingrese su nombre y contraseña.");
        vista.desbloquearCamposRegistro();
    }

    private void guardarContrasena() {
        try {
            String correoUsuario = vista.getTextFieldIngresarCorreo().getText().trim();
            String nuevaContrasena = vista.getTextFieldIngresarNuevaContraseña().getText().trim();
            String confirmarContrasena = vista.getTextFieldConfirmarContraseña().getText().trim();
            String nombre = vista.getTxtNombre().getText().trim();

            if (nuevaContrasena.isEmpty() || confirmarContrasena.isEmpty() || nombre.isEmpty()) {
                JOptionPane.showMessageDialog(vista, "Por favor, complete todos los campos.", "Campos vacíos", JOptionPane.WARNING_MESSAGE);
                return;
            }

            if (!nuevaContrasena.equals(confirmarContrasena)) {
                JOptionPane.showMessageDialog(vista, "Las contraseñas no coinciden.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            boolean actualizada = usuario.actualizarContrasena(correoUsuario, nuevaContrasena, nombre);
            if (actualizada) {
                OTPService.borrarCodigo(correoUsuario);

                String asunto = "¡Gracias por registrarte en nuestra app!";
                String mensaje = "Bienvenido/a " + nombre + ",\n\n"
                        + "Tu registro fue exitoso. Puedes iniciar sesión con:\n"
                        + "Usuario: " + nombre + "\n"
                        + "Correo: " + correoUsuario + "\n"
                        + "Contraseña: " + nuevaContrasena + "\n\n"
                        + "¡Gracias por registrarte en nuestra app!";
                emailSender.enviarCorreo(correoUsuario, asunto, mensaje);

                JOptionPane.showMessageDialog(vista, "Registro exitoso. Revisa tu correo y disfruta nuestra app.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                dialog.dispose();

                int rol = usuario.obtenerRolUsuario(correoUsuario);
                if (rol == 2) {
                    DashboardAdmin dashAdmin = new DashboardAdmin(correoUsuario);
                    new ControladorDashboardAdmin(dashAdmin, login);
                    login.mostrarPanelEnPanel1(dashAdmin);
                } else {
                    Dashboard dashPanel = new Dashboard(correoUsuario);
                    new ControladorDashboard(dashPanel, login);
                    login.mostrarPanelEnPanel1(dashPanel);
                }
            } else {
                JOptionPane.showMessageDialog(vista, "No se pudo actualizar la contraseña.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(vista, "Error al guardar la contraseña: " + ex.getMessage(), "Error de Base de Datos", JOptionPane.ERROR_MESSAGE);
        }
    }
}
