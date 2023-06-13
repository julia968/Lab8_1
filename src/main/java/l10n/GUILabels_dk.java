package l10n;

import java.util.ListResourceBundle;

public class GUILabels_dk extends ListResourceBundle {

    private static final Object[][] contents =
    {
            {"barbie", "Barbie"},
            {"name", "Navn: "},
            {"height", "Højde: "},
            {"coords", "Koordinater: "},
            {"creation_date", "Oprettelsesdato: "},
            {"eyes_color", "Øjenfarve: "},
            {"bd", "Fødselsdag: "},
            {"country", "Land: "},
            {"location", "Position: "},
            {"creator", "Skaber: "},
            {"u_exit", "Er du sikker på, at du vil afslutte?"},
            {"attempt", "Login Forsøg"},
            {"empty", "Tom login"},
            {"success", "Success!!!"},
            {"exists", "Login eksisterer allerede"},
            {"invalid", "Ugyldig input"},
            {"have_acc", "Har du allerede en konto?"},
            {"reg_here", "Registrer her!"},
            {"sign_up", "Tilmeld"},
            {"login", "Log ind"},
            {"no_acc", "Ingen konto?"},
            {"login_here", "Log ind her!"},
            {"add_new", "Tilføj Ny Barbie"},
            {"separator", ","},
            {"date_format", "dd.MM.yyyy"},
            {"help", """
                    hjælp tilgængelige kommandoer for brugeren
                    info vis information om samlingen
                    add tilføj et element til samlingen
                    update opdater data om et samlingselement efter ID
                    exit afslut programmet
                    remove_by_id fjern et element fra samlingen efter dets ID
                    remove_greater fjern fra samlingen alle elementer større end det angivne
                    remove_lower fjern fra samlingen alle elementer mindre end det angivne
                    clear slet alle brugere, der tilhører dig
                    max_by_name vis enhver person fra samlingen, hvis navnefelt er maksimalt
                    show vis alle personer i samlingen
                    execute_script læs og udfør et script fra den angivne fil
                    """ },
            {"profile", "Din Profil"},
            {"map", "Kort"},
            {"table", "Tabel"},
            {"higher", "Fjern Højere"},
            {"lower", "Fjern Lavere"},
            {"by_id", "Fjern efter ID"},
            {"x_c", "X-koordinat:"},
            {"y_c", "Y-koordinat:"},
            {"x_l", "X-position:"},
            {"y_l", "Y-position:"},
            {"update_id", "Opdater efter ID"},
            {"update", "Opdater"},
            {"add", "Tilføj"},
            {"no", "Nej"},
            {"not_ur", "Ikke Din"},
            {"type", "Samlingstype:"},
            {"amount", "Antal Barbie:"},
            {"init", "Initialiseringsdato:"},
            {"to_add", "Tilføjelse"},
            {"clear", "Ryd Samling"},
            {"edit", "Rediger Samling"},
            {"yes", "Ja"},
            {"no", "Nej"},
            {"clear_conf", "Er du sikker på, at du vil rydde samlingen?"},
            {"longest", "Længste Navn"},
            {"warn", "Advarsel!"},
            {"bd_empty", "Samlingen er tom, intet element med maksimum"},
            {"info", "Samlingens Information"},
            {"label_person_collection", "Personlig samling!"},
            {"label_phrase", "Hold kontakten med os ved at give dine personlige."},
            {"label_welcome", "Velkommen!"},
            {"label_login", "Log ind"},
            {"label_password", "Adgangskode"},
            {"help_heading", "Hjælp"},
            {"language", "Аkte"},
            {"collection_info", "Tietoa keräyksestä"}
    };

    public Object[][] getContents() {
        return contents;
    }

}
