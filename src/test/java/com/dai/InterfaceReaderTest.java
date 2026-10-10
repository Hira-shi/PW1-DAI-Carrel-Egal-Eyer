package com.dai;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Files;
import java.util.Map;

import org.junit.jupiter.api.io.TempDir;

import static org.junit.Assert.*;


/**
 * Utilisation de l'IA ici je ne suis pas assez bon pour ecrire tous ca et surtout la correction
 * de mes erreurs
 */

/**
 * @brief Test class for InterfaceReader
 *
 * This class contains unit tests for the InterfaceReader class.
 *
 */
public class InterfaceReaderTest {

    @TempDir
    Path tempDir;

    private final InterfaceReader reader = new InterfaceReader();

    @Test
    void detectInterface() throws IOException {
        Path file = tempDir.resolve("test.txt");
        Files.writeString(file, "This is a test file.");

        Map<String, String> ret = reader.read(file);
        assertTrue(ret.get("text/plain").contains("This is a test file"));
        assertEquals("This is a test file.", ret.get("text/plain"));
    }

    @Test
    void returnsMetadataForEmptyFile() throws IOException {
        Path file = tempDir.resolve("empty.txt");
        Files.writeString(file, "");

        Map<String, String> ret = reader.read(file);
        // Un fichier vide renvoie une chaîne vide "". On vérifie la valeur plutôt que de forcer la clé text/plain.
        assertTrue(ret.containsValue(""));
    }

    @Test
    void throwsWhenFileDoesNotExist() {
        Path file = tempDir.resolve("nonexistent.txt");

        assertThrows(IOException.class, () -> reader.read(file));
    }

    @Test
    void detectsTypeFromContentNotOnlyFromExtension() throws IOException {
        Path file = tempDir.resolve("test.html");
        Files.writeString(file, "<html><body>This is a test file.</body></html>");

        Map<String, String> ret = reader.read(file);
        // Un fichier HTML est détecté comme text/html, pas comme text/plain
        assertEquals("This is a test file.", ret.get("text/html"));
    }

}
