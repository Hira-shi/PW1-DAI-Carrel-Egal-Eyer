/**
 * @file TransferCommand.java
 * @brief Définition de la commande d'export des métadonnées.
 */
package com.dai;

import picocli.CommandLine;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;

/**
 * Commande CLI qui extrait les métadonnées d'un fichier source et les exporte
 * dans un fichier CSV, via Java I/O.
 */
@CommandLine.Command(name = "transferMetadata", description = "Exporte les métadonnées d'un fichier source vers un fichier CSV.")
public class TransferCommand implements Runnable {

    /** Champs exportés dans le CSV, dans l'ordre des colonnes. */
    private static final List<String> EXPORTED_FIELDS =
            List.of(MetaDTO.TITLE, MetaDTO.AUTHOR, MetaDTO.DATE, MetaDTO.ALBUM);

    /**
     * @brief Le chemin du fichier source, récupéré depuis le premier argument CLI.
     */
    @CommandLine.Parameters(index = "0", description = "Le fichier source à lire.")
    String sourcePath;

    /**
     * @brief Le chemin du fichier CSV de destination, récupéré depuis le second argument CLI.
     */
    @CommandLine.Parameters(index = "1", description = "Le fichier CSV de destination.")
    String destPath;

    /**
     * @brief Point d'entrée de la commande d'export.
     *
     * Lit le fichier source, extrait le DTO, puis écrit les métadonnées dans le CSV.
     */
    @Override
    public void run() {
        Path source = Path.of(sourcePath);
        Path output = Path.of(destPath);

        if (!Files.exists(source)) {
            System.err.println("Erreur : le fichier source n'existe pas : " + source);
            return;
        }

        try {
            MetaDTO meta;
            try (InputStream in = new BufferedInputStream(Files.newInputStream(source))) {
                meta = new InterfaceReader().extractMetadata(in);
            }

            if (meta.isEmpty()) {
                System.err.println("Avertissement : aucune métadonnée reconnue dans " + source);
            }

            boolean needsHeader = !Files.exists(output);
            try (Writer out = Files.newBufferedWriter(output, StandardCharsets.UTF_8,
                    StandardOpenOption.CREATE, StandardOpenOption.APPEND)) {
                new FileWriter().write(out, meta, needsHeader);
            }

            printExportSummary(source, output, meta);
        } catch (IOException e) {
            System.err.println("Erreur : " + e.getMessage());
        }
    }

    /**
     * @brief Affiche le détail des métadonnées effectivement exportées.
     *
     * @param source le fichier source
     * @param output le fichier CSV de sortie
     * @param meta les métadonnées extraites
     */
    private static void printExportSummary(Path source, Path output, MetaDTO meta) {
        List<String> lines = new ArrayList<>();
        for (String field : EXPORTED_FIELDS) {
            String value = meta.get(field);
            if (value != null && !value.isEmpty()) {
                lines.add("  " + field + " = " + value);
            }
        }

        if (lines.isEmpty()) {
            System.out.println("0 métadonnée exportée de " + source + " vers " + output);
            return;
        }

        System.out.println("Métadonnées exportées de " + source + " vers " + output + " :");
        for (String line : lines) {
            System.out.println(line);
        }
    }
}