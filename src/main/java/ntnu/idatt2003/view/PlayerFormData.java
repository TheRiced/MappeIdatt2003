package ntnu.idatt2003.view;

import ntnu.idatt2003.core.PlayerIcon;

/**
 * Represents the form data entered for a player (name, age, icon) during player setup. Used for
 * collecting input from UI forms before creating player objects.
 *
 * @param name the player's name
 * @param age  the player's age
 * @param icon the player's selected icon
 */
public record PlayerFormData(String name, int age, PlayerIcon icon) {

}
