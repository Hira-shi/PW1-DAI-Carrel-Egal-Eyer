package com.dai;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Files;
import java.util.Map;

import org.apache.tika.Tika;
import org.junit.jupiter.api.io.TempDir;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;


/**
 * Utilisation de l'IA ici je ne suis pas assez bon pour ecrire tous ca
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
}
