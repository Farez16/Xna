package Controlador;

import Modelo.Juego;
import Vista.VistaJuego;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;

public class ControladorJuego {

    private final VistaJuego vista;
    private final Juego modelo;
    private List<Juego> juegosActuales;
    private final Map<JLabel, String> asociaciones = new HashMap<>();
    private JLabel imagenSeleccionada = null;

    public ControladorJuego(VistaJuego vista, Juego modelo) {
        this.vista = vista;
        this.modelo = modelo;
        cargarElementosJuego();
        agregarEventos();
    }

    private void agregarEventos() {
        JLabel[] labels = {vista.getLblimagen1(), vista.getLblimagen2(), vista.getLblimagen3()};
        JButton[] botones = {vista.getBtn1(), vista.getBtn2(), vista.getBtn3()};

        for (JLabel lbl : labels) {
            lbl.addMouseListener(new java.awt.event.MouseAdapter() {
                @Override
                public void mouseClicked(java.awt.event.MouseEvent evt) {
                    if (asociaciones.containsKey(lbl)) {
                        vista.mostrarMensaje("Esta imagen ya tiene una palabra asignada.");
                        return;
                    }

                    for (JLabel l : labels) {
                        l.setBorder(null);
                    }

                    imagenSeleccionada = lbl;
                    lbl.setBorder(javax.swing.BorderFactory.createLineBorder(java.awt.Color.BLUE, 3));
                    vista.mostrarMensaje("Imagen seleccionada. Ahora elige una palabra.");
                }
            });
        }

        for (JButton btn : botones) {
            btn.addActionListener(e -> {
                if (imagenSeleccionada != null) {
                    asociaciones.put(imagenSeleccionada, btn.getText());

                    btn.setBackground(java.awt.Color.GREEN);
                    btn.setEnabled(false);

                    imagenSeleccionada.setBorder(null);
                    imagenSeleccionada = null;
                } else {
                    vista.mostrarMensaje("Primero selecciona una imagen.");
                }
            });
        }

        vista.getBtnVerificar().addActionListener(e -> verificarResultados());
    }

    public void cargarElementosJuego() {
        asociaciones.clear();
        juegosActuales = modelo.obtenerTodoAleatorio(3);

        JLabel[] labels = {vista.getLblimagen1(), vista.getLblimagen2(), vista.getLblimagen3()};
        JButton[] botones = {vista.getBtn1(), vista.getBtn2(), vista.getBtn3()};

        for (int i = 0; i < juegosActuales.size(); i++) {
            Juego juego = juegosActuales.get(i);
            String rutaCompleta = "/" + juego.getRutaImagen();
            URL urlImagen = getClass().getResource(rutaCompleta);
            if (urlImagen == null) {
                System.out.println("No se encontró la imagen: " + rutaCompleta);
            } else {
                ImageIcon icon = new ImageIcon(urlImagen);
                labels[i].setIcon(icon);
                labels[i].setBorder(null);
                labels[i].putClientProperty("palabra", juego.getPalabra());
            }
        }

        List<String> palabras = new ArrayList<>();
        for (Juego juego : juegosActuales) {
            palabras.add(juego.getPalabra());
        }
        Collections.shuffle(palabras);

        for (int i = 0; i < botones.length; i++) {
            botones[i].setText(palabras.get(i));
            botones[i].setEnabled(true);
            botones[i].setBackground(null);
        }
    }

    private void verificarResultados() {
        if (asociaciones.size() < juegosActuales.size()) {
            vista.mostrarMensaje("Debes asociar todas las imágenes antes de verificar.");
            return;
        }

        int correctos = 0;
        StringBuilder resultado = new StringBuilder();

        for (Map.Entry<JLabel, String> entry : asociaciones.entrySet()) {
            JLabel label = entry.getKey();
            String seleccion = entry.getValue();
            String correcta = (String) label.getClientProperty("palabra");

            if (seleccion.equalsIgnoreCase(correcta)) {
                correctos++;
                resultado.append("✅ Correcto: ").append(correcta).append("\n");
            } else {
                resultado.append("❌ Incorrecto: elegiste ").append(seleccion)
                        .append(", era ").append(correcta).append("\n");
            }
        }

        resultado.append("\nTotal correctos: ").append(correctos).append(" de ").append(juegosActuales.size());

        int opcion = vista.mostrarResultados(resultado.toString());

        if (opcion == 0) {
            cargarElementosJuego();
        } else {
            vista.mostrarMensaje("Gracias por jugar.");
        }
    }
}
