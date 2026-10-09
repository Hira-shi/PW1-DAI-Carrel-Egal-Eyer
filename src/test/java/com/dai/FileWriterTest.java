package com.dai;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

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

    @TempDir
    Path tempDir;

    private final FileWriter writer = new FileWriter();

    private MetaDTO createMeta(String title, String author, String date, String album) {
        MetaDTO meta = new MetaDTO("audio/mpeg");
        meta.set(MetaDTO.TITLE, title);
        meta.set(MetaDTO.AUTHOR, author);
        meta.set(MetaDTO.DATE, date);
        meta.set(MetaDTO.ALBUM, album);
        return meta;
    }

//    Création et ajout
    @Test
    public void createsCsvWithHeaderWhenFileDoesNotExist() throws IOException {
        Path file = tempDir.resolve("meta.csv");
        assertFalse(Files.exists(file));

        writer.write(file, createMeta(title, author, date, album));


        List<String> lines = Files.readAllLines(file);
        assertEquals(List.of(HEADER, csv1), lines);
    }

    @Test
    public void appendsNewLineForEachWrite() throws IOException {
        Path file = tempDir.resolve("meta.csv");

        writer.write(file, createMeta(title, author, date, album));
        writer.write(file, createMeta(title2, author2, date2, album2));

        List<String> lines = Files.readAllLines(file);
        assertEquals(List.of(
                HEADER,
                csv1,
                csv2
        ), lines);
    }

    @Test
    public void keepsExistingLinesAndDoesNotRewriteHeader() throws IOException {
        Path file = tempDir.resolve("meta.csv");
        Files.writeString(file, HEADER + "\n" + csv3 + "\n");

        writer.write(file, createMeta(title4, author4, date4, album4));

        List<String> lines = Files.readAllLines(file);
        assertEquals(List.of(
                HEADER,
                csv3,
                csv4
        ), lines);
    }

//    Contenu de la ligne
    @Test
    public void writesEmptyValueForMissingField() throws IOException {
        Path file = tempDir.resolve("meta.csv");
        MetaDTO meta = new MetaDTO();
        meta.set(MetaDTO.TITLE, title);

        writer.write(file, meta);

        String expected = title + separator + separator + separator;
        assertEquals(expected, Files.readAllLines(file).get(1));
    }

    @Test
    public void writesFieldsInHeaderOrder() throws IOException {
        Path file = tempDir.resolve("meta.csv");
        MetaDTO meta = new MetaDTO();
        meta.set(MetaDTO.ALBUM, album);
        meta.set(MetaDTO.TITLE, title);
        meta.set(MetaDTO.AUTHOR, author);

        writer.write(file, meta);

        // la date manque : case vide entre l'auteur et l'album
        String expected = title + separator + author + separator + separator + album;
        assertEquals(expected, Files.readAllLines(file).get(1));
    }

    @Test
    public void quotesValueContainingComma() throws IOException {
        Path file = tempDir.resolve("meta.csv");

        writer.write(file, createMeta(title, author, date, album));

        // title5 contient une virgule : il doit être entouré de guillemets
        String expected = "\"" + title + "\"" +
                separator + author +
                separator + separator +
                separator + album;
        assertEquals(expected, Files.readAllLines(file).get(1));
    }


    @Test
    public void releasesFileHandleAfterWriting() throws IOException {
        Path file = tempDir.resolve("meta.csv");

        writer.write(file, createMeta(title, author, date, album));

        // échoue sous Windows si le flux n'a pas été fermé
        Files.delete(file);
        assertFalse(Files.exists(file));
    }

//    Cas d'erreur
    @Test
    public void throwsWhenParentDirectoryDoesNotExist() {
        Path file = tempDir.resolve("missing").resolve("meta.csv");

        assertThrows(IOException.class, () -> writer.write(file, createMeta(title, author, date, album)));
    }

    @Test
    public void throwsWhenPathIsDirectory() {
        assertThrows(IOException.class, () -> writer.write(tempDir, createMeta(title, author, date, album)));
    }

    @Test
    public void throwsWhenPathIsNull() {
        assertThrows(NullPointerException.class, () -> writer.write(null, createMeta(title, author, date, album)));
    }

    @Test
    public void throwsWhenMetaIsNull() {
        Path file = tempDir.resolve("meta.csv");

        assertThrows(NullPointerException.class, () -> writer.write(file, null));
    }
}
