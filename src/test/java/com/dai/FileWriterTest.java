package com.dai;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.StringWriter;

import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * <p>Tests unitaires de {@link FileWriter}, qui écrit des {@link MetaDTO} sous forme
 * de lignes CSV ({@code title,author,date,album}).</p>
 *
 * <p>Les tests s'exécutent entièrement en mémoire : le writer écrit dans un
 * {@link StringWriter} et on compare le texte produit avec le texte attendu.
 * Aucun fichier n'est lu ou écrit sur le disque, les tests donnent donc le même
 * résultat sur n'importe quelle machine.</p>
 *
 * <p>Les données de test (titres, auteurs, dates, albums) sont construites dans la
 * classe, et les lignes CSV attendues ({@code csv1} à {@code csv4}) sont assemblées
 * à partir de ces mêmes valeurs.</p>
 */
public class FileWriterTest {
    /** En-tête attendu du CSV, dans l'ordre des colonnes. */
    private static final String HEADER = "title,author,date,album";

    private final String author = "Yoko Suzuki";
    private final String album = "Japanese Piano";
    private final String title = "Merry Christmas Mr.Lawrence";
    private final String date = "2014-04-23";

    private final String author2 = "Ryuichi Sakamoto";
    private final String album2 = "1996";
    private final String title2 = "Merry Christmas Mr.Lawrence";
    private final String date2 = "1996-06-04";

    private final String author3 = "Joe Hisaishi";
    private final String album3 = "Kiki's Delivery Service Soundtrack Music";
    private final String title3 = "A Town with an Ocean View";
    private final String date3 = "1989-09-09";

    private final String author4 = "Yoko Shimomura";
    private final String album4 = "Kingdom Hearts";
    private final String title4 = "Dearly Beloved";
    private final String date4 = "2002-03-22";

    private final String title5 = "Hello, World";


    /** Séparateur de colonnes du CSV. */
    private final char separator = ',';
    // Toujours "\n", quel que soit l'OS, pour que le CSV soit identique partout
    /** Fin de ligne attendue après chaque ligne du CSV. */
    private final String newLine = "\n";

    /** Ligne CSV attendue pour le morceau 1 (sans fin de ligne). */
    private final String csv1 = title +
            separator + author +
            separator + date +
            separator + album;

    /** Ligne CSV attendue pour le morceau 2 (sans fin de ligne). */
    private final String csv2 = title2 +
            separator + author2 +
            separator + date2 +
            separator + album2;

    /** Ligne CSV attendue pour le morceau 3 (sans fin de ligne). */
    private final String csv3 = title3 +
            separator + author3 +
            separator + date3 +
            separator + album3;

    /** Ligne CSV attendue pour le morceau 4 (sans fin de ligne). */
    private final String csv4 = title4 +
            separator + author4 +
            separator + date4 +
            separator + album4;

    /** Instance testée. */
    private final FileWriter writer = new FileWriter();

    /**
     * Crée un DTO avec les quatre champs du CSV remplis.
     *
     * @param title  titre du morceau
     * @param author auteur du morceau
     * @param date   date au format ISO-8601
     * @param album  album du morceau
     * @return un {@link MetaDTO} contenant ces quatre champs
     */
    private MetaDTO createMeta(String title, String author, String date, String album) {
        MetaDTO meta = new MetaDTO("audio/mpeg");
        meta.set(MetaDTO.TITLE, title);
        meta.set(MetaDTO.AUTHOR, author);
        meta.set(MetaDTO.DATE, date);
        meta.set(MetaDTO.ALBUM, album);
        return meta;
    }

    /**
     * Avec {@code withHeader = true}, l'en-tête est écrit avant la ligne du DTO.
     */
    @Test
    public void writesHeaderWhenRequested() throws IOException {
        StringWriter out = new StringWriter();

        writer.write(out, createMeta(title, author, date, album), true);

        assertEquals(HEADER + newLine + csv1 + newLine, out.toString());
    }

    /**
     * Avec {@code withHeader = false}, seule la ligne du DTO est écrite,
     * terminée par une fin de ligne.
     */
    @Test
    public void writesOneLineWithoutHeader() throws IOException {
        StringWriter out = new StringWriter();

        writer.write(out, createMeta(title, author, date, album), false);

        assertEquals(csv1 + newLine, out.toString());
    }

    /**
     * Plusieurs écritures dans le même writer donnent l'en-tête (une seule fois)
     * puis une ligne par DTO, dans l'ordre d'écriture.
     */
    @Test
    public void writesHeaderOnceThenLinesInOrder() throws IOException {
        StringWriter out = new StringWriter();

        writer.write(out, createMeta(title, author, date, album), true);
        writer.write(out, createMeta(title2, author2, date2, album2), false);
        writer.write(out, createMeta(title3, author3, date3, album3), false);
        writer.write(out, createMeta(title4, author4, date4, album4), false);

        String expected = HEADER + newLine +
                csv1 + newLine +
                csv2 + newLine +
                csv3 + newLine +
                csv4 + newLine;
        assertEquals(expected, out.toString());
    }

    /**
     * Les valeurs sont écrites dans l'ordre des colonnes de l'en-tête,
     * même si le DTO a été rempli dans un autre ordre.
     */
    @Test
    public void writesFieldsInHeaderOrder() throws IOException {
        StringWriter out = new StringWriter();
        MetaDTO meta = new MetaDTO();
        meta.set(MetaDTO.ALBUM, album);
        meta.set(MetaDTO.DATE, date);
        meta.set(MetaDTO.AUTHOR, author);
        meta.set(MetaDTO.TITLE, title);

        writer.write(out, meta, false);

        assertEquals(csv1 + newLine, out.toString());
    }

    /**
     * Cas limite : un champ absent du DTO donne une case vide,
     * sans décaler les colonnes suivantes.
     */
    @Test
    public void writesEmptyValueForMissingField() throws IOException {
        StringWriter out = new StringWriter();
        MetaDTO meta = new MetaDTO();
        meta.set(MetaDTO.TITLE, title);
        meta.set(MetaDTO.AUTHOR, author);
        meta.set(MetaDTO.ALBUM, album);

        writer.write(out, meta, false);

        // la date manque : case vide entre l'auteur et l'album
        String expected = title + separator + author + separator + separator + album;
        assertEquals(expected + newLine, out.toString());
    }

    /**
     * Cas limite : un DTO vide donne une ligne de cases vides ({@code ,,,}).
     */
    @Test
    public void writesEmptyColumnsForEmptyMeta() throws IOException {
        StringWriter out = new StringWriter();

        writer.write(out, new MetaDTO(), false);

        String expected = "" + separator + separator + separator;
        assertEquals(expected + newLine, out.toString());
    }

    /**
     * Cas limite : une valeur qui contient une virgule est entourée de guillemets,
     * sinon elle serait lue comme deux colonnes.
     */
    @Test
    public void quotesValueContainingComma() throws IOException {
        StringWriter out = new StringWriter();

        writer.write(out, createMeta(title5, author, date, album), false);

        // title5 contient une virgule : il doit être entouré de guillemets
        String expected = "\"" + title5 + "\"" +
                separator + author +
                separator + date +
                separator + album;
        assertEquals(expected + newLine, out.toString());
    }

    /**
     * Un writer {@code null} est refusé avec une {@link NullPointerException}.
     */
    @Test
    public void throwsWhenWriterIsNull() {
        assertThrows(NullPointerException.class, () -> writer.write(null, createMeta(title, author, date, album), false));
    }

    /**
     * Un DTO {@code null} est refusé avec une {@link NullPointerException}.
     */
    @Test
    public void throwsWhenMetaIsNull() {
        StringWriter out = new StringWriter();

        assertThrows(NullPointerException.class, () -> writer.write(out, null, false));
    }
}
