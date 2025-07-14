package Modelo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class Usuario {

    private final Connection conn;
    private static final Map<String, String> imagenesUsuarios = new HashMap<>();

    public Usuario(Connection conn) {
        this.conn = conn;
    }

    public boolean tieneContrasena(String email) throws SQLException {
        String sql = "SELECT contrasena FROM usuarios WHERE correo = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, email);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    String pass = rs.getString("contrasena");
                    return pass != null && !pass.trim().isEmpty();
                }
            }
        }
        return false;
    }

    public boolean actualizarContrasena(String email, String nuevaContrasena, String nombre) throws SQLException {
        String sqlUpdate = "UPDATE usuarios SET contrasena = ?, nombre = ? WHERE correo = ?";
        String sqlInsert = "INSERT INTO usuarios (nombre, correo, contrasena, id_rol) VALUES (?, ?, ?, 1)";

        try (PreparedStatement psUpdate = conn.prepareStatement(sqlUpdate)) {
            psUpdate.setString(1, nuevaContrasena);
            psUpdate.setString(2, nombre);
            psUpdate.setString(3, email);
            int filasAfectadas = psUpdate.executeUpdate();

            if (filasAfectadas > 0) {
                return true;
            }
        }

        try (PreparedStatement psInsert = conn.prepareStatement(sqlInsert)) {
            psInsert.setString(1, nombre);
            psInsert.setString(2, email);
            psInsert.setString(3, nuevaContrasena);
            return psInsert.executeUpdate() > 0;
        }
    }

    public boolean existeUsuario(String email) throws SQLException {
        String sql = "SELECT COUNT(*) FROM usuarios WHERE correo = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, email);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        }
        return false;
    }

    public boolean verificarContrasena(String email, String contrasenaIngresada) throws SQLException {
        String sql = "SELECT contrasena FROM usuarios WHERE correo = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, email);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    String contrasenaGuardada = rs.getString("contrasena");
                    return contrasenaGuardada != null && contrasenaGuardada.equals(contrasenaIngresada);
                }
            }
        }
        return false;
    }

    public void guardarRutaImagen(String correo, String rutaImagen) {
        imagenesUsuarios.put(correo.toLowerCase(), rutaImagen);
    }

    public String obtenerRutaImagen(String correo) {
        return imagenesUsuarios.getOrDefault(correo.toLowerCase(), "src/Imagenes/Users.png");
    }

    public boolean actualizarImagenUsuario(String correo, String ruta) throws SQLException {
        String sql = "UPDATE usuarios SET ruta_imagen = ? WHERE correo = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, ruta);
            stmt.setString(2, correo);
            int updated = stmt.executeUpdate();

            if (updated > 0) {
                imagenesUsuarios.put(correo.toLowerCase(), ruta);
                return true;
            }
            return false;
        }
    }

    public String obtenerImagenUsuario(String correo) throws SQLException {
        if (imagenesUsuarios.containsKey(correo.toLowerCase())) {
            return imagenesUsuarios.get(correo.toLowerCase());
        }

        String sql = "SELECT ruta_imagen FROM usuarios WHERE correo = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, correo);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    String ruta = rs.getString("ruta_imagen");
                    imagenesUsuarios.put(correo.toLowerCase(), ruta);
                    return ruta;
                }
            }
        }
        return null;
    }

    public String obtenerCorreoDesdeUsuario(String usuarioOCorreo) throws SQLException {
        String sql = "SELECT correo FROM usuarios WHERE usuario = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, usuarioOCorreo);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("correo");
                }
            }
        }
        return null;
    }

    public boolean estaRegistrado(String correo) throws SQLException {
        String sql = "SELECT 1 FROM usuarios WHERE correo = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, correo);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    public boolean verificarCodigo(String correo, String codigoIngresado) throws SQLException {
        String sql = "SELECT codigo FROM usuarios WHERE correo = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, correo);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    String codigoGuardado = rs.getString("codigo");
                    return codigoGuardado != null && codigoGuardado.equals(codigoIngresado);
                }
            }
        }
        return false;
    }

    public String[] obtenerNombreYRol(String correo) throws SQLException {
        String[] datos = new String[2];
        String sql = "SELECT u.nombre, r.nombre_rol " +
                "FROM usuarios u " +
                "JOIN roles r ON u.id_rol = r.id_rol " +
                "WHERE u.correo = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, correo);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    datos[0] = rs.getString("nombre");
                    datos[1] = rs.getString("nombre_rol");
                }
            }
        }
        return datos;
    }

    public int obtenerRolUsuario(String correo) throws SQLException {
        String sql = "SELECT id_rol FROM usuarios WHERE correo = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, correo);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? rs.getInt("id_rol") : 1; // Default: estudiante
            }
        }
    }

    public boolean registrarAdministrador(String correo, String contrasena) throws SQLException {
        String sql = "INSERT INTO usuarios (correo, contrasena, id_rol) VALUES (?, ?, 2)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, correo);
            ps.setString(2, contrasena);
            return ps.executeUpdate() > 0;
        }
    }

    public String getContrasenaActual(String correo) throws SQLException {
        String sql = "SELECT contrasena FROM usuarios WHERE correo = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, correo);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("contrasena");
                }
            }
        }
        return null;
    }

    public int obtenerIdPorCorreo(String correo) throws SQLException {
        if (correo == null) {
            throw new IllegalArgumentException("El correo no puede ser nulo");
        }

        int id = -1;
        String sql = "SELECT id_usuario FROM usuarios WHERE correo = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            String correoLimpio = correo.trim();
            ps.setString(1, correoLimpio);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    id = rs.getInt("id_usuario");
                }
            }
        }
        return id;
    }
}
