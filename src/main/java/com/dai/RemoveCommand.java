/**
 * @file RemoveCommand.java
 * @brief Définition de la commande de gestion de l'historique CSV.
 */
package com.dai;

import picocli.CommandLine;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

/**
 * @class RemoveCommand
 * @brief Commande CLI pour nettoyer ou supprimer le fichier CSV d'historique.
 * <p>
 * Implémente l'interface Runnable pour Picocli afin d'exécuter la logique
 * de suppression ou de réinitialisation lorsqu'elle est appelée via le terminal.
 * </p>
 */
@CommandLine.Command(name = "removeMetadata", description = "Nettoie ou supprime l'historique des métadonnées (CSV).")
public class RemoveCommand implements Runnable {


    /**
     * @brief Le chemin du fichier cible, récupéré depuis le premier argument CLI.
     */
    @CommandLine.Parameters(index = "0", description = "Le fichier CSV à nettoyer.")
    String filePath;

    /**
     * @brief Option (flag) pour supprimer physiquement le fichier CSV du disque.
     */
    @CommandLine.Option(names = {"-d", "--delete"}, description = "Supprime complètement le fichier CSV du disque.")
    boolean delete;

    /**
     * @brief Option (flag) pour vider le contenu du fichier tout en conservant l'en-tête.
     *
     */
    @CommandLine.Option(names = {"-c", "--clear"}, description = "Vide le contenu du fichier (conserve uniquement l'en-tête).")
    boolean clear;

    /**
     * @brief Point d'entrée de la commande de suppression.
     * <p>
     * Exécute la logique de suppression totale ou de vidage du fichier selon l'option choisie.
     * </p>
     */
    @Override
    public void run() {
        Path path = Path.of(filePath);

        if (!Files.exists(path)) {
            // System.err.printf écrit dans la sortie d'erreur suggérée par IntelliJ
            System.err.printf("Le fichier n'existe pas : %s%n", filePath);
            return;
        }

        if (delete && clear) {
            System.err.println("Erreur : Veuillez choisir soit --delete, soit --clear, pas les deux en même temps.");
            return;
        }

        if (!delete && !clear) {
            System.err.println("Erreur : Spécifiez une action avec --delete ou --clear.");
            return;
        }

        try {
            if (delete) {
                Files.deleteIfExists(path);
                // Utilisation de printf suggérée par IntelliJ
                System.out.printf("Suppression totale réussie : %s%n", filePath);
            } else {
                //'clear' est forcément vrai grâce aux vérifications précédentes
                //  Ecrase le fichier et remet l'entête
                String header = "title,author,date,album\n";
                Files.writeString(path, header, StandardOpenOption.TRUNCATE_EXISTING);
                System.out.printf("Le fichier a été vidé avec succès : %s%n", filePath);
            }
        } catch (IOException e) {
            System.out.printf("Erreur d'I/O lors de l'opération : %s%n", e.getMessage());
        }
    }
}
