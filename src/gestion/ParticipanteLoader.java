package gestion;

import modelo.Equipo;
import modelo.Jugador;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Clase utilitaria para cargar participantes desde archivos .txt.
 *
 * Formato esperado para equipos:
 *   Primera línea: nombre del equipo
 *   Siguientes:    NombreJugador, contacto (contacto opcional)
 *
 * Formato esperado para jugadores individuales:
 *   Cada línea: NombreJugador, contacto (contacto opcional)
 */
public class ParticipanteLoader {

    /**
     * Constructor por defecto
     */
    private ParticipanteLoader() {}

    /**
     * Lee un archivo .txt y devuelve un Equipo con sus jugadores.
     * @param archivo Archivo de texto ingresado
     * @return Equipo que está en el archivo de texto
     * @throws IOException Se lanza cuando el archivo está vacio
     */
    public static Equipo cargarEquipo(File archivo) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(archivo))) {
            String primeraLinea = reader.readLine();
            if (primeraLinea == null || primeraLinea.isBlank()) {
                throw new IOException("El archivo está vacío o no tiene nombre de equipo.");
            }

            Equipo equipo = new Equipo(primeraLinea.trim());
            String linea;
            while ((linea = reader.readLine()) != null) {
                if (linea.isBlank()) continue;
                String[] partes = linea.split(",", 2);
                equipo.addjugador(new Jugador(
                        partes[0].trim(),
                        partes.length > 1 ? partes[1].trim() : ""
                ));
            }
            return equipo;
        }
    }

    /**
     * Lee un archivo .txt y devuelve una lista de jugadores individuales.
     * @param archivo Archivo de texto ingresado
     * @return Lista de jugadores que está en el archivo de texto
     * @throws IOException Se lanza cuando el archivo está vacío
     */
    public static List<Jugador> cargarJugadores(File archivo) throws IOException {
        List<Jugador> jugadores = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(archivo))) {
            String linea;
            while ((linea = reader.readLine()) != null) {
                if (linea.isBlank()) continue;
                String[] partes = linea.split(",", 2);
                jugadores.add(new Jugador(
                        partes[0].trim(),
                        partes.length > 1 ? partes[1].trim() : ""
                ));
            }
        }
        if (jugadores.isEmpty()) {
            throw new IOException("El archivo no contiene jugadores.");
        }
        return jugadores;
    }
}
