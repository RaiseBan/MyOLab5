package commands;

import exceptions.WrongArgumentsException;
import support.CollectionControl;
import support.Console;

/**
 * Command to print the Person data in ascending order by their name.
 */
public class PrintFieldOfPerson extends AbstractCommand {
    CollectionControl collectionControl;

    /**
     * Constructor to create a new PrintFieldOfPerson object.
     *
     * @param collectionControl the CollectionControl object to be used
     */
    public PrintFieldOfPerson(CollectionControl collectionControl) {
        super("print_field_ascending_person", "выводит данные о человеке в порядке возрастания");
        this.collectionControl = collectionControl;
    }

    /**
     * Executes the PrintFieldOfPerson command to print the Person data in ascending order by name.
     *
     * @param argument the arguments passed to the command
     */
    @Override
    public void execute(String argument) {
        try {
            if (!argument.isEmpty()) throw new WrongArgumentsException();
            collectionControl.sortPerson();
        } catch (WrongArgumentsException e) {
            Console.err(e.getMessage());
        }
    }
}

