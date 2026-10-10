/**
 * @file RemoveCommand.java
 * @brief Définition de la commande de suppression des métadonnées.
 */
package com.dai;

import picocli.CommandLine;

/**
 * @class RemoveCommand
 * @brief Commande CLI pour nettoyer un fichier de ses métadonnées.
 * <p>
 * Implémente l'interface Runnable pour Picocli afin d'exécuter la logique
 * lorsqu'elle est appelée via le terminal.
 */
@CommandLine.Command(name = "removeMetadata", description = "Supprime les métadonnées.")
public class RemoveCommand implements Runnable {

    /**
     * @brief Le chemin du fichier cible, récupéré depuis le premier argument CLI.
     */
    @CommandLine.Parameters(index = "0", description = "Le fichier à nettoyer.")
    String filePath;

    /**
     * @brief Point d'entrée de la commande de suppression.
     *
     * Exécute la logique de nettoyage des métadonnées via RandomAccessFile.
     */
    @Override
    public void run() {
        // TODO: Implémenter la logique de suppression

        System.out.println("Suppression pour le fichier " + filePath);
    }
}
