package com.dai;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.StringWriter;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Utilisation de l'IA pour les corrections
 *
 * Tests de FileWriter entièrement en mémoire : on écrit dans un StringWriter
 * et on compare le texte produit, sans aucun fichier sur le disque.
 */
public class FileWriterTest {
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


    private final char separator = ',';
    // Toujours "\n", quel que soit l'OS, pour que le CSV soit identique partout
    private final String newLine = "\n";

    private final String csv1 = title +
            separator + author +
            separator + date +
            separator + album;

    private final String csv2 = title2 +
            separator + author2 +
            separator + date2 +
            separator + album2;

    private final String csv3 = title3 +
            separator + author3 +
            separator + date3 +
            separator + album3;

    private final String csv4 = title4 +
            separator + author4 +
            separator + date4 +
            separator + album4;

    private final FileWriter writer = new FileWriter();

    private MetaDTO createMeta(String title, String author, String date, String album) {
        MetaDTO meta = new MetaDTO("audio/mpeg");
        meta.set(MetaDTO.TITLE, title);
        meta.set(MetaDTO.AUTHOR, author);
        meta.set(MetaDTO.DATE, date);
        meta.set(MetaDTO.ALBUM, album);
        return meta;
    }

//    Cas de base
    @Test
    public void writesHeader() throws IOException {
        StringWriter out = new StringWriter();

        writer.writeHeader(out);

        assertEquals(HEADER + newLine, out.toString());
    }

    @Test
    public void writesOneLine() throws IOException {
        StringWriter out = new StringWriter();

        writer.write(out, createMeta(title, author, date, album));

        assertEquals(csv1 + newLine, out.toString());
    }

    @Test
    public void writesHeaderThenLinesInOrder() throws IOException {
        StringWriter out = new StringWriter();

        writer.writeHeader(out);
        writer.write(out, createMeta(title, author, date, album));
        writer.write(out, createMeta(title2, author2, date2, album2));
        writer.write(out, createMeta(title3, author3, date3, album3));
        writer.write(out, createMeta(title4, author4, date4, album4));

        String expected = HEADER + newLine +
                csv1 + newLine +
                csv2 + newLine +
                csv3 + newLine +
                csv4 + newLine;
        assertEquals(expected, out.toString());
    }

    @Test
    public void writesFieldsInHeaderOrder() throws IOException {
        StringWriter out = new StringWriter();
        MetaDTO meta = new MetaDTO();
        meta.set(MetaDTO.ALBUM, album);
        meta.set(MetaDTO.DATE, date);
        meta.set(MetaDTO.AUTHOR, author);
        meta.set(MetaDTO.TITLE, title);

        writer.write(out, meta);

        assertEquals(csv1 + newLine, out.toString());
    }

//    Cas limites
    @Test
    public void writesEmptyValueForMissingField() throws IOException {
        StringWriter out = new StringWriter();
        MetaDTO meta = new MetaDTO();
        meta.set(MetaDTO.TITLE, title);
        meta.set(MetaDTO.AUTHOR, author);
        meta.set(MetaDTO.ALBUM, album);

        writer.write(out, meta);

        // la date manque : case vide entre l'auteur et l'album
        String expected = title + separator + author + separator + separator + album;
        assertEquals(expected + newLine, out.toString());
    }

    @Test
    public void writesEmptyColumnsForEmptyMeta() throws IOException {
        StringWriter out = new StringWriter();

        writer.write(out, new MetaDTO());

        String expected = "" + separator + separator + separator;
        assertEquals(expected + newLine, out.toString());
    }

    @Test
    public void quotesValueContainingComma() throws IOException {
        StringWriter out = new StringWriter();

        writer.write(out, createMeta(title, author, date, album));

        // title5 contient une virgule : il doit être entouré de guillemets
        String expected = "\"" + title + "\"" +
                separator + author +
                separator + date +
                separator + album;
        assertEquals(expected + newLine, out.toString());
    }

//    Cas d'erreur
    @Test
    public void throwsWhenWriterIsNull() {
        assertThrows(NullPointerException.class, () -> writer.write(null, createMeta(title, author, date, album)));
    }

    @Test
    public void throwsWhenMetaIsNull() {
        StringWriter out = new StringWriter();

        assertThrows(NullPointerException.class, () -> writer.write(out, null));
    }
}
