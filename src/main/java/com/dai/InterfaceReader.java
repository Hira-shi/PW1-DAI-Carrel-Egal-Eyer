/**
 * @file InterfaceReader.java
 * @brief Classe responsable de la lecture et de l'extraction des métadonnées.
 */
package com.dai;

import java.io.IOException;
import org.apache.tika.Tika;
import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.Parser;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
import java.io.InputStream;


/**
 * @class InterfaceReader
 * @brief Lit un fichier pour détecter son format et extraire son contenu.
 */
public class InterfaceReader {

    private final Tika tika = new Tika();
    private final Parser parser = new AutoDetectParser();

    /**
     * @brief Extrait les métadonnées structurées d'un flux vers un {@link MetaDTO}.
     *
     * @param in le flux du fichier source (déjà ouvert)
     * @return le DTO des métadonnées extraites (peut être vide)
     * @throws IOException si le flux ne peut pas être lu ou analysé
     */
    public MetaDTO extractMetadata(InputStream in) throws IOException {
        Metadata metadata = new Metadata();
        try {
            parser.parse(in, new DefaultHandler(), metadata, new ParseContext());
        } catch (SAXException | TikaException e) {
            throw new IOException("Échec de l'extraction des métadonnées : " + e.getMessage(), e);
        }
        return MetadataMapper.toDTO(metadata);
    }
}
