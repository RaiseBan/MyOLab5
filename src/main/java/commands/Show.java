package commands;

import exceptions.WrongArgumentsException;
import support.CollectionControl;
import support.Console;

/**
 * Command
 */

public class Show extends AbstractCommand {
    CollectionControl collectionControl;

    public Show(CollectionControl collectionControl) {
        super("show", "вывести в стандартный поток вывода все элементы коллекции в строковом представлению");
        this.collectionControl = collectionControl;
    }

    /**
     * Executes the command.
     *
     * @param argument a string argument for the command
     */
    @Override
    public void execute(String argument) {
        try {
            if (!argument.isEmpty()) throw new WrongArgumentsException();
            this.collectionControl.show();
        } catch (WrongArgumentsException e) {
            Console.err(e.getMessage());
        }

    }
}
