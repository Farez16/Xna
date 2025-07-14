package Controlador;

import Conexion.Conexion;
import Modelo.Usuario;
import Vista.RegistrarAdmin;
import java.sql.Connection;
import java.sql.SQLException;
import javax.swing.JOptionPane;

public class ControladorRegistrarAdmin {
    private final RegistrarAdmin vista;
    private final Usuario modelo;
    private final Connection conn;


    public ControladorRegistrarAdmin(RegistrarAdmin vista) {
        this.vista = vista;
        this.conn = Conexion.conectar();
        this.modelo = new Usuario(conn);
        
        vista.getBtnRegistrarAd().addActionListener(e -> registrarAdmin());
    }

    private void registrarAdmin() {
        String correo = vista.getTxtCorreo().getText();
        String contrasena = new String(vista.getjPassword().getPassword());

        if (correo.isEmpty() || contrasena.isEmpty()) {
            JOptionPane.showMessageDialog(vista, "Por favor, complete todos los campos.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            if (modelo.registrarAdministrador(correo, contrasena)) {
                JOptionPane.showMessageDialog(vista, "Administrador registrado con éxito.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                vista.getTxtCorreo().setText("");
                vista.getjPassword().setText("");
                vista.getTxtNombre().setText("");
            } else {
                JOptionPane.showMessageDialog(vista, "No se pudo registrar al administrador.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(vista, "Error al registrar: " + e.getMessage(), "Error de Base de Datos", JOptionPane.ERROR_MESSAGE);
        }
    }
}
