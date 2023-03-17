package commands;

import exceptions.WrongArgumentsException;
import support.CollectionControl;
import support.CommunicationControl;
import support.Console;

/**
 * The RemoveElementByID class represents a command to remove an element from the collection by its ID.
 */
public class RemoveElementByID extends AbstractCommand {
    CollectionControl collectionControl;
    CommunicationControl communicationControl;

    /**
     * Constructs the RemoveElementByID object with the specified CollectionControl and CommunicationControl objects.
     *
     * @param collectionControl    the CollectionControl object to be used
     * @param communicationControl the CommunicationControl object to be used
     */
    public RemoveElementByID(CollectionControl collectionControl, CommunicationControl communicationControl) {
        super("remove_element_by_id", "Remove an element from the collection by its ID");
        this.collectionControl = collectionControl;
        this.communicationControl = communicationControl;
    }

    /**
     * Executes the RemoveElementByID command by invoking the removeElementByID() method on the CollectionControl object.
     *
     * @param argument the argument to be passed to the command
     */
    @Override
    public void execute(String argument) {
        try {
            if (argument.isEmpty()) throw new WrongArgumentsException();
            int id = Integer.parseInt(argument.trim());
            collectionControl.removeElementByID(id);
            collectionControl.updateAllIDs();
            //Console.writeln("Successfully removed element");
            System.out.println("Successfully removed");
        } catch (NumberFormatException e) {
            Console.err("Incorrect ID format");
        } catch (WrongArgumentsException e) {
            Console.err(e.getMessage());
        }

    }
}
