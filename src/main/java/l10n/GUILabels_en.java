package l10n;

import java.util.ListResourceBundle;

public class GUILabels_en extends ListResourceBundle {

    private static final Object[][] contents =
    {
            {"barbie", "Barbie"},
            {"name", "Name: "},
            {"height", "Height: "},
            {"coords", "Coordinates: "},
            {"creation_date", "Creation Date: "},
            {"eyes_color", "Eye Color: "},
            {"bd", "Birthday: "},
            {"country", "Country: "},
            {"location", "Location: "},
            {"creator", "Creator: "},
            {"u_exit", "Are you sure you want to exit?"},
            {"attempt", "Login Attempt"},
            {"empty", "Empty login"},
            {"success", "Success!!!"},
            {"exists", "Login already exists"},
            {"invalid", "Invalid input"},
            {"have_acc", "Already have an account?"},
            {"reg_here", "Register here!"},
            {"sign_up", "Sign up"},
            {"login", "Log in"},
            {"no_acc", "No account?"},
            {"login_here", "Log in here!"},
            {"add_new", "Add New Barbie"},
            {"separator", ","},
            {"date_format", "dd.MM.yyyy"},
            {"help", """
                    help available commands for the user
                    info display information about the collection
                    add add an element to the collection
                    update update data of a collection element by ID
                    exit exit the program
                    remove_by_id remove an element from the collection by its ID
                    remove_greater remove from the collection all elements greater than the specified one
                    remove_lower remove from the collection all elements less than the specified one
                    clear delete all users that belong to you
                    max_by_name display any person from the collection whose name field is maximum
                    show show all people in the collection
                    execute_script read and execute a script from the specified file
                    """ },
            {"profile", "Your Profile"},
            {"map", "Map"},
            {"table", "Table"},
            {"higher", "Remove Higher"},
            {"lower", "Remove Lower"},
            {"by_id", "Remove by ID"},
            {"x_c", "X coordinate:"},
            {"y_c", "Y coordinate:"},
            {"x_l", "X position:"},
            {"y_l", "Y position:"},
            {"update_id", "Update by ID"},
            {"update", "Update"},
            {"add", "Add"},
            {"no", "No"},
            {"not_ur", "Not Yours"},
            {"type", "Collection Type:"},
            {"amount", "Barbie Count:"},
            {"init", "Initialization Date:"},
            {"to_add", "To Add"},
            {"clear", "Clear Collection"},
            {"edit", "Edit Collection"},
            {"yes", "Yes"},
            {"no", "No"},
            {"clear_conf", "Are you sure you want to clear the collection?"},
            {"longest", "Longest Name"},
            {"warn", "Warning!"},
            {"bd_empty", "The collection is empty, no element with the maximum"},
            {"info", "Collection Information"},
            {"label_person_collection", "Personal collection!"},
            {"label_phrase", "Stay in touch with us by providing your personal information."},
            {"label_welcome", "Welcome!"},
            {"label_login", "Login"},
            {"label_password", "Password"},
            {"help_heading", "Help"},
            {"language", "Language"},
            {"collection_info", "Collection info"}
    };

    public Object[][] getContents() {
        return contents;
    }

}
