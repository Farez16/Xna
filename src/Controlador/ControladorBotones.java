package Controlador;

import Modelo.FadeEfectoBotones;
import Modelo.TextoBotones;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JButton;

public class ControladorBotones {

    private final TextoBotones modelo;
    private final Map<String, Boolean> estadosBotones;
    private final Map<String, JButton> botonesMap;

    public ControladorBotones(Map<String, JButton> botonesMap, TextoBotones modelo) {
        this.botonesMap = botonesMap;
        this.modelo = modelo;
        this.estadosBotones = new HashMap<>();
        botonesMap.keySet().forEach(key -> estadosBotones.put(key, true));
    }

    public void iniciar() {
        actualizarTextosBotones(false);
    }

    public void actualizarTextos() {
        estadosBotones.replaceAll((k, v) -> !v);
        actualizarTextosBotones(true);
    }

    private void actualizarTextosBotones(boolean conAnimacion) {
        if (modelo == null) {
            throw new IllegalStateException("El modelo TextoBotones no puede ser null");
        }

        for (Map.Entry<String, JButton> entry : botonesMap.entrySet()) {
            String nombreBoton = entry.getKey();
            JButton boton = entry.getValue();

            String[] textos = modelo.getTextos(nombreBoton);
            if (textos == null) {
                System.err.println("No hay textos para: " + nombreBoton);
                continue;
            }
            boolean mostrarPrimerTexto = estadosBotones.get(nombreBoton);
            String nuevoTexto = mostrarPrimerTexto ? textos[0] : textos[1];

            if (conAnimacion) {
                FadeEfectoBotones.applyFadeEffect(boton, nuevoTexto);
            } else {
                boton.setText(nuevoTexto);
            }
        }
    }
}
