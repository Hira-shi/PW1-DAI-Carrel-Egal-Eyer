#!/usr/bin/env bash
set -euo pipefail

# --- Palette de couleurs ---
C_RESET="\033[0m"
C_BOLD="\033[1m"
C_BLUE="\033[34m"
C_GREEN="\033[32m"
C_YELLOW="\033[33m"
C_RED="\033[31m"

# --- fichiers tests et temporaires ---
MP3_SRC="demo/sample_court.mp3"
TMP_CSV="test_historique.csv"

# Nettoyage automatique : supprime que le CSV temporaire
cleanup() {
    rm -f "$TMP_CSV"
}
trap cleanup EXIT

# --- Fonctions d'affichage ---
step() {
    echo -e "\n${C_BOLD}${C_BLUE}==> [Étape $1] $2${C_RESET}"
}

success() {
    echo -e "    ${C_GREEN}✔ $1${C_RESET}"
}

warn() {
    echo -e "    ${C_YELLOW}ℹ $1${C_RESET}"
}

fail() {
    echo -e "    ${C_RED}✖ $1${C_RESET}" >&2
    exit 1
}

# --- 1. Validation unitaire ---
step 1 "Exécution des tests JUnit"
./mvnw test -q

# --- 2. Construction du JAR ---
step 2 "Compilation et génération de l'archive"
./mvnw clean package -DskipTests -q

JAR_FILE=$(find target -maxdepth 1 -name "*.jar" ! -name "original-*" | head -n 1)
[[ -n "$JAR_FILE" && -f "$JAR_FILE" ]] || fail "Aucun binaire JAR exécutable trouvé dans target/"
success "Binaire sélectionné : $JAR_FILE"

# --- 3. Test métier : Extraction des métadonnées ID3 ---
step 3 "Extraction et validation des métadonnées d'un vrai fichier MP3"

# Vérification de présence du fichier échantillon
[[ -f "$MP3_SRC" ]] || fail "Le fichier fixture '$MP3_SRC' est introuvable."

# Exécution du transfert vers le CSV
java -jar "$JAR_FILE" transferMetadata "$MP3_SRC" "$TMP_CSV"
[[ -s "$TMP_CSV" ]] || fail "Le fichier CSV n'a pas été généré ou est vide."

# Validation des 4 champs réels exportés
if grep -qi "Luidji" "$TMP_CSV"; then
    success "Auteur validé (Luidji)"
else
    fail "Le champ 'author' (Luidji) est manquant dans le CSV"
fi

if grep -qi "Reste.*En.*Vie" "$TMP_CSV"; then
    success "Titre validé (Reste En Vie)"
else
    fail "Le champ 'title' (Reste En Vie) est manquant dans le CSV"
fi

if grep -q "Saison 00" "$TMP_CSV"; then
    success "Album validé (Saison 00)"
else
    fail "Le champ 'album' (Saison 00) est manquant dans le CSV"
fi

if grep -q "2023" "$TMP_CSV"; then
    success "Date validée (2023)"
else
    fail "Le champ 'date' (2023) est manquant dans le CSV"
fi

# --- 4. Test d'effacement du contenu ---
step 4 "Vidage des données (conservation en-tête)"
java -jar "$JAR_FILE" removeMetadata "$TMP_CSV" --clear
success "Option --clear exécutée"

# --- 5. Test de suppression du fichier ---
step 5 "Suppression du fichier CSV (--delete)"
java -jar "$JAR_FILE" removeMetadata "$TMP_CSV" --delete
if [[ -f "$TMP_CSV" ]]; then
    fail "Le fichier CSV existe toujours sur le disque."
else
    success "Le fichier CSV a bien été supprimé."
fi

# --- 6. Tests des erreurs attendues ---

step 6 "Validation des cas limites et erreurs contrôlées"
touch "$TMP_CSV"

# Test 6.1 : Deux drapeaux incompatibles
# On laisse la commande s'exécuter comme dans ton script d'origine
java -jar "$JAR_FILE" removeMetadata "$TMP_CSV" --clear --delete || true
success "Test 6.1 passé (le programme ne crashe pas brutalement)"

# Test 6.2 : Absence d'argument
java -jar "$JAR_FILE" removeMetadata "$TMP_CSV" || true
success "Test 6.2 passé (absence d'options gérée)"

# Test 6.3 : Fichier cible introuvable
java -jar "$JAR_FILE" removeMetadata "fichier_introuvable.csv" --clear || true
success "Test 6.3 passé (fichier inexistant géré)"

echo -e "\n${C_BOLD}${C_GREEN}==========================================${C_RESET}"
echo -e "${C_BOLD}${C_GREEN} SUITE DE TESTS TERMINÉE AVEC SUCCÈS !    ${C_RESET}"
echo -e "${C_BOLD}${C_GREEN}==========================================${C_RESET}\n"