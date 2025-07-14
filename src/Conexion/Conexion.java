package Conexion;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class Conexion {

    private static final Properties properties = new Properties();

    static {
        try (InputStream input = Conexion.class.getClassLoader().getResourceAsStream("db.properties")) {
            if (input == null) {
                System.out.println("No se pudo encontrar el archivo db.properties");
            }
            properties.load(input);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private final String url = properties.getProperty("db.url");
    private final String usuario = properties.getProperty("db.user");
    private final String contraseña = properties.getProperty("db.password");

    public Connection getConexion() {
        Connection conexion = null;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conexion = DriverManager.getConnection(url, usuario, contraseña);
            System.out.println("Conexion exitosa");
        } catch (ClassNotFoundException | SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
        return conexion;
    }

    // MÉTODO ESTÁTICO para obtener conexión directamente
    public static Connection conectar() {
        return new Conexion().getConexion();
    }
}

