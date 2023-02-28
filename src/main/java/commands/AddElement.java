
package commands;

import data.Worker;
import exceptions.InputException;
import exceptions.WrongArgumentsException;
import support.CollectionControl;
import support.CommunicationControl;
import support.Console;
/**
 * The {@code AddElement} class represents a command that adds a new worker element to the collection.
 * The command requires instances of {@link CommunicationControl} and {@link CollectionControl} to communicate with the user
 * and modify the collection, respectively.
 * This class extends the {@link AbstractCommand} abstract class.
 */
public class AddElement extends AbstractCommand {

    private CommunicationControl communicationControl;

    private CollectionControl collectionControl;

    /**
     * Constructs an {@code AddElement} object with the specified instances of {@link CommunicationControl} and {@link CollectionControl}.
     *
     * @param communicationControl the {@link CommunicationControl} instance to be used for communication with the user
     * @param collectionControl the {@link CollectionControl} instance to be used for modifying the collection
     */
    public AddElement(CommunicationControl communicationControl, CollectionControl collectionControl) {
        super("addElement", "Добавить элемент в коллекцию");
        this.communicationControl = communicationControl;
        this.collectionControl = collectionControl;
    }

    /**
     * Executes the command by adding a new worker element to the collection.
     *
     * @param argument the command argument
     */
    public void execute(String argument) {
        try {
            if (!argument.isEmpty()) throw new WrongArgumentsException();
            collectionControl.addToCollection(new Worker(communicationControl.setName(),
                    communicationControl.setCoordinates(),
                    communicationControl.setSalary(), communicationControl.choosePosition(),
                    communicationControl.chooseStatus(), communicationControl.setPerson()));
        } catch (WrongArgumentsException e) {
            Console.err(e.getMessage());
        } catch (InputException e) {
            Console.err("Некорректный данные в скрипте!");
        }
    }

}
