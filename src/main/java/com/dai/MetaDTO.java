package com.dai;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * La javadoc est faite par l'ia
 *
 * <p>Représentation neutre (indépendante du format) de toutes les métadonnées d'un fichier,
 * sous forme de paires nom &rarr; valeur.</p>
 *
 * <p>Les readers traduisent les clés natives de leur format (ID3 "TIT2", Vorbis "TITLE",
 * EXIF "ImageDescription"...) vers les noms normalisés ci-dessous, et les writers font
 * la traduction inverse. C'est ce qui permet le mapping best-effort entre formats :
 * un writer qui ne connaît pas un champ le signale au lieu de l'écrire.</p>
 *
 * <p>Exemple :</p>
 * <pre>{@code
 * MetaDTO meta = new MetaDTO("image/jpeg");
 * meta.set(MetaDTO.AUTHOR, "Alice");
 * meta.set(MetaDTO.DATE, "2026-10-09");
 * }</pre>
 */
public class MetaDTO {

    /** Nom normalisé du titre du fichier (titre d'un document, d'un morceau...). */
    public static final String TITLE = "title";

    /** Nom normalisé de l'auteur du fichier. */
    public static final String AUTHOR = "author";

    /** Nom normalisé de la date. La valeur doit être au format ISO-8601 (ex. "2026-10-09"). */
    public static final String DATE = "date";

    /** album */
    public static final String ALBUM = "album";

    private final String sourceFormat;
    // LinkedHashMap pour garder l'ordre d'insertion des champs et permettre une recherche efficace
    // Surtout que les champs vont de pair -> author : "Le nom de l'auteur"
    private final Map<String, String> fields = new LinkedHashMap<>();

    /**
     * Crée un DTO vide dont le format d'origine est inconnu.
     */
    public MetaDTO() {
        this(null);
    }

    /**
     * Crée un DTO vide pour un fichier du format donné.
     *
     * @param sourceFormat type MIME du fichier d'origine (ex. "audio/mpeg"), ou {@code null} si inconnu
     */
    public MetaDTO(String sourceFormat) {
        this.sourceFormat = sourceFormat;
    }

    /**
     * Retourne le format du fichier d'où proviennent les métadonnées.
     *
     * @return le type MIME du fichier d'origine, ou {@code null} s'il est inconnu
     */
    public String getSourceFormat() {
        return sourceFormat;
    }

    /**
     * Ajoute un champ, ou remplace sa valeur s'il existe déjà.
     *
     * @param name  nom normalisé du champ (de préférence une des constantes de cette classe)
     * @param value valeur du champ
     */
    public void set(String name, String value) {
        fields.put(name, value);
    }

    /**
     * Retourne la valeur d'un champ.
     *
     * @param name nom normalisé du champ
     * @return la valeur du champ, ou {@code null} si le champ n'existe pas
     */
    public String get(String name) {
        return fields.get(name);
    }

    /**
     * Indique si un champ est présent.
     *
     * @param name nom normalisé du champ
     * @return {@code true} si le champ existe, {@code false} sinon
     */
    public boolean has(String name) {
        return fields.containsKey(name);
    }

    /**
     * Supprime un champ. Ne fait rien si le champ n'existe pas.
     *
     * @param name nom normalisé du champ à supprimer
     */
    public void remove(String name) {
        fields.remove(name);
    }

    /**
     * Retourne tous les champs, dans l'ordre où ils ont été ajoutés.
     *
     * @return une vue non modifiable des champs (nom &rarr; valeur)
     */
    public Map<String, String> getFields() {
        return Collections.unmodifiableMap(fields);
    }

    /**
     * Retourne le nombre de champs.
     *
     * @return le nombre de champs présents
     */
    public int size() {
        return fields.size();
    }

    /**
     * Indique si le DTO ne contient aucun champ.
     *
     * @return {@code true} s'il n'y a aucun champ, {@code false} sinon
     */
    public boolean isEmpty() {
        return fields.isEmpty();
    }

    /**
     * Retourne une représentation lisible du DTO, utile pour le débogage.
     *
     * @return le format d'origine suivi de la liste des champs
     */
    @Override
    public String toString() {
        return "MetaDTO[" + sourceFormat + "] " + fields;
    }
}
