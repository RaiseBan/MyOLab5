
package commands;

import data.Worker;
import exceptions.InputException;
import exceptions.WrongArgumentsException;
import support.CollectionControl;
import support.CommunicationControl;
import support.Console;

/**
 * The RemoveGreater class represents a command to remove all elements from the collection
 * that are greater than the specified element.
 */
public class RemoveGreater extends AbstractCommand {
    CollectionControl collectionControl;
    CommunicationControl communicationControl;

    /**
     * Constructs a new RemoveGreater command with the specified CollectionControl and
     * CommunicationControl.
     *
     * @param collectionControl    The CollectionControl instance to use for command execution.
     * @param communicationControl The CommunicationControl instance to use for user interaction.
     */
    public RemoveGreater(CollectionControl collectionControl, CommunicationControl communicationControl) {
        super("remove_greater", "Удалить из коллекции все элементы, превышающие заданный");
        this.collectionControl = collectionControl;
        this.communicationControl = communicationControl;
    }

    /**
     * Executes the RemoveGreater command.
     *
     * @param argument The command argument (not used).
     */

    @Override
    public void execute(String argument) {
        try {
            if (!argument.isEmpty()) throw new WrongArgumentsException();
            collectionControl.removeGreater(new Worker(communicationControl.setName(),
                    communicationControl.setCoordinates(), communicationControl.setSalary(),
                    communicationControl.choosePosition(), communicationControl.chooseStatus(),
                    communicationControl.setPerson()));
            collectionControl.updateAllIDs();
        } catch (WrongArgumentsException e) {
            Console.err(e.getMessage());
        } catch (InputException e) {
            Console.err("Некорректный данные в скрипте!");
        }

    }
}
