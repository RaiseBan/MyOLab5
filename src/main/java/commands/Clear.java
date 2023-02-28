
package commands;

import data.Worker;
import exceptions.WrongArgumentsException;
import support.CollectionControl;
import support.CommunicationControl;
import support.Console;
/**
 * The Clear class represents a command that clears the collection.
 */
public class Clear extends AbstractCommand {
    private CollectionControl collectionControl;
    private CommunicationControl communicationControl;

    /**
     * Constructs a Clear object with the given CollectionControl and CommunicationControl objects.
     *
     * @param collectionControl    the CollectionControl object that manages the collection
     * @param communicationControl the CommunicationControl object that manages the communication with the user
     */
    public Clear(CollectionControl collectionControl, CommunicationControl communicationControl) {
        super("clear", "очистить коллекцию");
        this.collectionControl = collectionControl;
        this.communicationControl = communicationControl;


    }

    /**
     * Executes the command to clear the collection.
     *
     * @param argument the arguments for the command (not used in this command)
     */
    @Override
    public void execute(String argument) {
        try {
            if (!argument.isEmpty()) throw new WrongArgumentsException();
            Console.writeln("Вы уверены, что хотите отчистить коллекцию? ");
            if (communicationControl.confirm()) {
                collectionControl.clear();
                Worker.idCounter = 0;
            }
        } catch (WrongArgumentsException e) {
            Console.err("Превышенно кол-во аргументов");
        }
    }
}
