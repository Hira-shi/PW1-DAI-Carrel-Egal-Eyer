package com.dai;

import picocli.CommandLine;


@CommandLine.Command(name = "metadata-cli", mixinStandardHelpOptions = true,
        subcommands = { Main.Transfer.class, Main.Remove.class })

public class Main {
    public static void main(String args[]) {
        int exitCode = new CommandLine(new Main()).execute(args);
        System.exit(exitCode);
    }

    @CommandLine.Command(name = "transferMetadata", description = "Transfère les métadonnées.")
    static class Transfer implements Runnable {

        // Index 0 : 1er argument après "transferMetaData"
        @CommandLine.Parameters(index = "0", description = "Le fichier source à lire.")
        String sourcePath;

        // Index 1 : 2ème argument après "transferMetaData"
        @CommandLine.Parameters(index = "1", description = "Le fichier de destination.")
        String destPath;

        @Override
        public void run() {
            transferMetadata(sourcePath, destPath);
        }
    }

    @CommandLine.Command(name = "removeMetadata", description = "Supprime les métadonnées.")
    static class Remove implements Runnable {

        // Index 0 : 1er argument après "removeMetadata"
        @CommandLine.Parameters(index = "0", description = "Le fichier à nettoyer.")
        String filePath;

        @Override
        public void run() {
            removeMetadata(filePath);
        }
    }

    // Fonctions temporaires permettant au programme de compiler.
    // À REMPLACER PAR LES MÉTHODES DES CLASSES PRÉVUES À CET EFFET
    public static void transferMetadata(String source, String dest) {};
    public static void removeMetadata(String file) {};
}
