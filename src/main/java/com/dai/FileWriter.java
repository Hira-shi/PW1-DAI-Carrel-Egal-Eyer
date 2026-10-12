package com.dai;

import java.io.IOException;
import java.io.Writer;

/**
 * Formate et écrit les métadonnées extraites dans un flux de sortie.
 */
public class FileWriter {

    private static final String HEADER = "title,author,date,album";

    /**
     * Écrit un objet MetaDTO sous forme de ligne CSV dans un Writer.
     *
     * @param out Le flux de sortie dans lequel écrire.
     * @param meta L'objet contenant les métadonnées à écrire.
     * @param writesHeaderWhenRequested Si true, ajoute l'en-tête CSV avant les données.
     * @throws IOException En cas d'erreur d'écriture dans le flux.
     * @throws NullPointerException Si le Writer ou le MetaDTO est null.
     */
    public void write(Writer out, MetaDTO meta, boolean writesHeaderWhenRequested) throws IOException {
        if (out == null || meta == null) {
            throw new NullPointerException("Le writer et les métadonnées ne peuvent pas être null.");
        }

        if (writesHeaderWhenRequested) {
            out.write(HEADER + "\n");
        }

        out.write(formatCsvField(meta.get(MetaDTO.TITLE)) + ","
                + formatCsvField(meta.get(MetaDTO.AUTHOR)) + ","
                + formatCsvField(meta.get(MetaDTO.DATE)) + ","
                + formatCsvField(meta.get(MetaDTO.ALBUM)) + "\n");
    }

    /**
     * Nettoie un champ pour l'insertion dans un CSV.
     * Prépare un champ pour le CSV : case vide si absent, guillemets si la valeur
     * contient le séparateur ou un guillemet (guillemets internes échappés).
     *
     * @param value La valeur brute à formater.
     * @return La valeur formatée pour le CSV.
     */
    private static String formatCsvField(String value) {
        if (value == null) {
            return "";
        }
        if (value.indexOf(',') >= 0 || value.indexOf('"') >= 0) {
            return '"' + value.replace("\"", "\"\"") + '"';
        }
        return value;
    }
}