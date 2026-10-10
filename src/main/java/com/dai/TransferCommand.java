/**
 * @file TransferCommand.java
 * @brief Définition de la commande de transfert des métadonnées.
 */
package com.dai;

import picocli.CommandLine;

/**
 * @class TransferCommand
 * @brief Commande CLI pour transférer les métadonnées entre deux fichiers.
 * <p>
 * Extrait les métadonnées du fichier source via l'InterfaceReader,
 * puis les écrit dans le fichier de destination en mode best-effort.
 */
@CommandLine.Command(name = "transferMetadata", description = "Transfère les métadonnées.")
public class TransferCommand implements Runnable {

    /**
     * @brief Le chemin du fichier source, récupéré depuis le premier argument CLI.
     */
    @CommandLine.Parameters(index = "0", description = "Le fichier source à lire.")
    String sourcePath;

    /**
     * @brief Le chemin du fichier de destination, récupéré depuis le second argument CLI.
     */
    @CommandLine.Parameters(index = "1", description = "Le fichier de destination.")
    String destPath;

    /**
     * @brief Point d'entrée de la commande de transfert.
     *
     * Exécute la lecture puis l'écriture optimisée des métadonnées.
     */
    @Override
    public void run() {
        // TODO: Implémenter la logique de transfert

        System.out.println("Transfert de " + sourcePath + " vers " + destPath);
    }
}
