package com.dai;

import org.apache.tika.metadata.Metadata;

/**
 * Traduit les métadonnées brutes d'Apache Tika vers le DTO.
 * <p>
 * Tika expose les champs sous plusieurs clés selon le format et le parseur
 * (par exemple {@code title}, {@code dc:title}, {@code xmpDM:artist}...). Cette classe
 * choisit la première clé disponible pour chaque champ normalisé, dans un ordre de priorité.
 */
public final class MetadataMapper {

    // Clés Tika candidates pour le titre, par ordre de priorité.
    private static final String[] TITLE_KEYS = {"title", "dc:title"};

    // Clés Tika candidates pour l'auteur, par ordre de priorité.
    private static final String[] AUTHOR_KEYS = {"xmpDM:artist", "dc:creator", "meta:author", "author", "artist"};

    //Clés Tika candidates pour l'album, par ordre de priorité.
    private static final String[] ALBUM_KEYS = {"xmpDM:album", "album"};

    // Clés Tika candidates pour la date, par ordre de priorité.
    private static final String[] DATE_KEYS = {"xmpDM:releaseDate", "dcterms:created", "date", "meta:creation-date", "creation-date"};

    private MetadataMapper() {
    }

    /**
     * Construit un {@link MetaDTO} à partir des métadonnées extraites par Tika.
     *
     * @param metadata les métadonnées Tika.
     * @return un DTO contenant le format d'origine et les champs reconnus (peut être vide)
     */
    public static MetaDTO toDTO(Metadata metadata) {
        MetaDTO dto = new MetaDTO(metadata.get(Metadata.CONTENT_TYPE));
        putIfPresent(dto, MetaDTO.TITLE, firstPresent(metadata, TITLE_KEYS));
        putIfPresent(dto, MetaDTO.AUTHOR, firstPresent(metadata, AUTHOR_KEYS));
        putIfPresent(dto, MetaDTO.ALBUM, firstPresent(metadata, ALBUM_KEYS));
        putIfPresent(dto, MetaDTO.DATE, firstPresent(metadata, DATE_KEYS));
        return dto;
    }

    /**
     * Enregistre un champ dans le DTO uniquement si la valeur est exploitable.
     *
     * @param dto   le DTO à compléter
     * @param field le nom normalisé du champ
     * @param value la valeur extraite (peut être vide).
     */
    private static void putIfPresent(MetaDTO dto, String field, String value) {
        if (value != null && !value.isBlank()) {
            dto.set(field, value.trim());
        }
    }

    /**
     * Retourne la première valeur non vide parmi une liste de clés Tika candidates.
     *
     * @param metadata les métadonnées Tika.
     * @param keys     les clés candidates, par ordre de priorité.
     * @return la première valeur trouvée, ou null si aucune clé n'est renseignée
     */
    private static String firstPresent(Metadata metadata, String[] keys) {
        for (String key : keys) {
            String value = metadata.get(key);
            if (value != null && !value.isBlank()) {
                return value;
            }
        }
        return null;
    }
}
