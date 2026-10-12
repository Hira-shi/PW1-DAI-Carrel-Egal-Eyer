# metadata-cli

_DAI - Pratical Work 1 : Java I/O — Groupe 7._

#### Auteurs

* Jérémie Carrel
* Omar Egal Ahmed
* Kaeno Eyer

#### Objectifs

* Développer un outil en ligne de commande (CLI) moderne et portable
* Extraire et agréger les métadonnées de fichiers multimédias hétérogènes dans un fichier CSV
* Manipuler les flux Java I/O
* Garantir la portabilité multiplateforme

#### Dépendances nécessaires au projet :
    
* Picocli (`info.picocli:picocli`) : routage des arguments de la ligne de commande et affichage de l'aide.
* Apache Tika (`org.apache.tika:tika-core` et `tika-parsers-standard-package`) : extraction et détection des métadonnées.
* JUnit 5 (`org.junit.jupiter:junit-jupiter`) : tests unitaires automatisés.

#### Instructions du Travail Pratique

Développer une application en ligne de commande permettant d'extraire, d'archiver et de gérer les métadonnées de fichiers multimédias dans un historique structuré.

* L'application prend en charge différents types de fichiers multimédias (audio MP3/FLAC, vidéo MP4).
* Les métadonnées normalisées (`title`, `author`, `date`, `album`) sont extraites puis agrégées dans un journal CSV lisible.
* L'extraction des flux sources est réalisée au moyen d'un `InterfaceReader` et de la bibliothèque Apache Tika, encapsulés dans un `BufferedInputStream`.
* L'écriture dans le fichier CSV s'effectue via `FileWriter` à l'aide d'un flux de caractères `BufferedWriter` configuré avec l'encodage `StandardCharsets.UTF_8` et les modes `StandardOpenOption.CREATE` et `StandardOpenOption.APPEND`.
* La commande `transferMetadata` permet d'extraire les métadonnées d'un fichier source et de les ajouter à la fin du fichier CSV cible. Si le fichier n'existe pas, il est créé avec son en-tête `title,author,date,album`.
* La commande `removeMetadata` permet de gérer et de nettoyer le fichier CSV cible à l'aide de deux options mutuellement exclusives :
    * `--clear` (`-c`) : vide le contenu du journal tout en conservant la ligne d'en-tête, au moyen de `Files.writeString` et `StandardOpenOption.TRUNCATE_EXISTING`.
    * `--delete` (`-d`) : supprime définitivement le fichier CSV du disque au moyen de `Files.deleteIfExists`.
* Il ne peut pas y avoir d'exécution simultanée des options `--clear` et `--delete`.
* Il doit être possible d'exécuter l'application sur différents systèmes d'exploitation (macOS, Linux, Windows) grâce à l'utilisation explicite du séparateur de ligne `\n`.
* Les caractères spéciaux tels que les virgules et les guillemets doivent être échappés par doublement des guillemets.


#### Compilation et exécution

- Cloner le dépôt :
```bash
git clone https://github.com/Hira-shi/PW1-DAI-Carrel-Egal-Eyer.git
cd PW1-DAI-Carrel-Egal-Eyer
```
- Compiler et packager l'application avec le wrapper Maven :
```bash
./mvnw clean package
```
Le fichier JAR exécutable est produit sous `target/PW1-DAI-Carrrel-Egal-Eyer-1.0-SNAPSHOT.jar`.

#### Guide d'utilisation

- Extraire des métadonnées vers un journal CSV :
```bash
java -jar target/PW1-DAI-Carrrel-Egal-Eyer-1.0-SNAPSHOT.jar transferMetadata demo/sample.mp3 demo/history.csv
```
- Vider l'historique en conservant la structure CSV :
```bash
java -jar target/PW1-DAI-Carrrel-Egal-Eyer-1.0-SNAPSHOT.jar removeMetadata demo/history.csv --clear
```
- Supprimer physiquement le fichier CSV :
```bash
java -jar target/PW1-DAI-Carrrel-Egal-Eyer-1.0-SNAPSHOT.jar removeMetadata demo/history.csv --delete
```

### Tests

- Tests unitaires (JUnit 5 en mémoire via `StringWriter`) :
```bash
./mvnw test
```
- Validation complète automatisée via script :
```bash
chmod +x test.sh
./test.sh
```
_Pour Windows: Utliser Git Bash si vous souhaitez utiliser le script_

#### Utilisation de l'IA

Conformément aux directives du laboratoire concernant les sources et l'utilisation de l'IA, nous avons utilisé des modèles d'intelligence artificielle (Claude, Gemini, etc.) en tant qu'assistants de développement pour les tâches suivantes :
- **Débogage Maven :** Aide à la résolution de l'erreur `SecurityException: Invalid signature file digest` via la configuration du filtre `maven-shade-plugin`.
- **Tests et Automatisation :** Aide à la structuration des tests unitaires (notamment `FileWriterTest.java`) et génération de la base du script Bash (`test.sh`) pour automatiser notre validation.
- **Documentation :** Aide à la mise en forme du présent README, rédaction partielle de la Javadoc et correction de la syntaxe des balises (passage du format C++ Doxygen vers les standards Java).

La logique de l'application (utilisation de l'API Java I/O native `BufferedInputStream`, `BufferedWriter`, `Files.newBufferedWriter`, etc.) a été conçue, écrite et maîtrisée par les membres de l'équipe.
