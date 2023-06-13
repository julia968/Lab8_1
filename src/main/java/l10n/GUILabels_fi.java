package l10n;

import java.util.ListResourceBundle;

public class GUILabels_fi extends ListResourceBundle {

    private static final Object[][] contents =
    {
            {"barbie", "Barbie"},
            {"name", "Nimi: "},
            {"height", "Pituus: "},
            {"coords", "Koordinaatit: "},
            {"creation_date", "Luomispäivä: "},
            {"eyes_color", "Silmien väri: "},
            {"bd", "Syntymäpäivä: "},
            {"country", "Maa: "},
            {"location", "Sijainti: "},
            {"creator", "Tekijä: "},
            {"u_exit", "Haluatko varmasti poistua?"},
            {"attempt", "Kirjautumisyritys"},
            {"empty", "Tyhjä käyttäjänimi"},
            {"success", "Onnistunut!!!"},
            {"exists", "Käyttäjänimi on jo olemassa"},
            {"invalid", "Virheellinen syöte"},
            {"have_acc", "Onko sinulla jo tili?"},
            {"reg_here", "Rekisteröidy tästä!"},
            {"sign_up", "Rekisteröidy"},
            {"login", "Kirjaudu sisään"},
            {"no_acc", "Ei tiliä?"},
            {"login_here", "Kirjaudu sisään täällä!"},
            {"add_new", "Lisää uusi Barbie"},
            {"separator", ","},
            {"date_format", "dd.MM.yyyy"},
            {"help", """
                    apua käytettävissä olevat komennot käyttäjälle
                    info näytä tietoja kokoelmasta
                    add lisää elementti kokoelmaan
                    update päivitä kokoelman elementin tiedot ID:n perusteella
                    exit poistu ohjelmasta
                    remove_by_id poista elementti kokoelmasta sen ID:n perusteella
                    remove_greater poista kokoelmasta kaikki suuremmat elementit kuin annettu
                    remove_lower poista kokoelmasta kaikki pienemmät elementit kuin annettu
                    clear poista kaikki sinulle kuuluvat käyttäjät
                    max_by_name näytä kokoelmasta kuka tahansa henkilö, jonka nimi on suurin
                    show näytä kaikki henkilöt kokoelmassa
                    execute_script lue ja suorita skripti annetusta tiedostosta
                    """ },
            {"profile", "Profiilisi"},
            {"map", "Kartta"},
            {"table", "Taulukko"},
            {"higher", "Poista ylemmät"},
            {"lower", "Poista alemmat"},
            {"by_id", "Poista ID:n perusteella"},
            {"x_c", "X-koordinaatti:"},
            {"y_c", "Y-koordinaatti:"},
            {"x_l", "X-sijainti:"},
            {"y_l", "Y-sijainti:"},
            {"update_id", "Päivitä ID:n perusteella"},
            {"update", "Päivitä"},
            {"add", "Lisää"},
            {"no", "Ei"},
            {"not_ur", "Ei sinun"},
            {"type", "Kokoelman tyyppi:"},
            {"amount", "Barbiejen määrä:"},
            {"init", "Alustuspäivä:"},
            {"to_add", "Lisättäväksi"},
            {"clear", "Tyhjennä kokoelma"},
            {"edit", "Muokkaa kokoelmaa"},
            {"yes", "Kyllä"},
            {"no", "Ei"},
            {"clear_conf", "Haluatko varmasti tyhjentää kokoelman?"},
            {"longest", "Pisin nimi"},
            {"warn", "Varoitus!"},
            {"bd_empty", "Kokoelma on tyhjä, ei elementtiä maksimilla"},
            {"info", "Kokoelman tiedot"},
            {"label_person_collection", "Henkilökohtainen kokoelma!"},
            {"label_phrase", "Ole yhteydessä kanssamme antamalla henkilötietosi."},
            {"label_welcome", "Tervetuloa!"},
            {"label_login", "Kirjautuminen"},
            {"label_password", "Salasana"},
            {"help_heading", "Ohje"},
            {"language", "Kieli"},
            {"collection_info", "Tietoa keräyksestä"}
    };

    public Object[][] getContents() {
        return contents;
    }

}
