/**
 * @file Main.java
 * @brief Point d'entrée principal de l'application CLI metadata-cli.
 */
package com.dai;

import picocli.CommandLine;

/**
 * @class Main
 * @brief Configuration et lancement de l'interface en ligne de commande.
 * <p>
 * La classe utilise la librairie Picocli pour router les requêtes
 * de l'utilisateur vers les commandes dédiées (TransferCommand et RemoveCommand).
 */
@CommandLine.Command(
        name = "metadata-cli",
        mixinStandardHelpOptions = true,
        subcommands = { TransferCommand.class, RemoveCommand.class })

public class Main {

    /**
     * @brief Méthode principale exécutée au lancement du programme.
     *
     * @param args Les arguments passés en ligne de commande par l'utilisateur.
     */
    public static void main(String args[]) {
        int exitCode = new CommandLine(new Main()).execute(args);
        System.exit(exitCode);
    }
}
