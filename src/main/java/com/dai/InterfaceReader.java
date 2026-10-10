/**
 * @file InterfaceReader.java
 * @brief Classe responsable de la lecture et de l'extraction des métadonnées.
 */
package com.dai;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Map;
import java.util.HashMap;
import java.nio.file.Files;
import org.apache.tika.Tika;
import java.io.InputStream;


/**
 * @class InterfaceReader
 * @brief Lit un fichier pour détecter son format et extraire son contenu.
 */
public class InterfaceReader {

    private final Tika tika = new Tika(); // final empêche de réattribuer l'objet.
    /**
    * @brief Détecte le type MIME d'un fichier et prépare l'extraction de son contenu.
            * <p>
            * Cette méthode vérifie en premier l'existence du fichier cible, puis ouvre un flux
            * d'entrée Java I/O (InputStream) pour identifier son format à l'aide d'Apache Tika.
            * Le flux est automatiquement fermé à la fin de l'analyse (try-with-resources).
            *
            * @param file Le chemin (Path) vers le fichier source à analyser.
            * @return Une Map contenant le type MIME détecté comme clé et le contenu extrait comme valeur (vide pour l'instant).
            *
            * @throws IOException Si le fichier spécifié n'existe pas ou en cas d'erreur lors de l'ouverture du flux I/O.
            */
    public Map<String, String> read(Path file) throws IOException {
        if (!Files.exists(file)) {
            throw new IOException("Le fichier n'existe pas : " + file.toString());
        }

        Map<String, String> result = new HashMap<>();
        String mimeType = "application/octet-stream";

        // Reconnaissance du format et géré via Java I/O
        try (InputStream is = Files.newInputStream(file)) {
            mimeType = tika.detect(is);
        } catch (Exception e) {
            // Par défaut : On garde application/octet-stream si échec
        }

        result.put(mimeType, ""); // TODO : Contenu vide pour le moment
        return result;
    }
}
