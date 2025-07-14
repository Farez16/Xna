package Controlador;

import Modelo.EfectosVisuales;
import Modelo.Saludo;
import javax.swing.JLabel;

public class ControladorSaludo {

    private final JLabel lblSaludo;
    private final JLabel lblNombre;
    private final Saludo modelo;
    private boolean mostrandoBienvenido = true;

    public ControladorSaludo(JLabel lblSaludo, JLabel lblNombre, Saludo modelo) {
        this.lblSaludo = lblSaludo;
        this.lblNombre = lblNombre;
        this.modelo = modelo;
        this.modelo.setMensaje("Bienvenido");
    }

    public void actualizarSaludo() {
        String mensaje = mostrandoBienvenido ? "Alli Shamushka" : "Bienvenido";
        modelo.setMensaje(mensaje);
        mostrandoBienvenido = !mostrandoBienvenido;
        String mensajeCompleto = modelo.getMensaje() + " " + lblNombre.getText();
        EfectosVisuales.aplicarFadeLabel(lblSaludo, mensajeCompleto, 1000);
    }

    public void inicializarVista() {
        String mensajeInicial = modelo.getMensaje() + " " + lblNombre.getText();
        lblSaludo.setText(mensajeInicial);
    }
}