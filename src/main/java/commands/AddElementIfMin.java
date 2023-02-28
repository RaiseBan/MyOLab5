
package commands;

import data.Worker;
import exceptions.InputException;
import exceptions.WrongArgumentsException;
import support.CollectionControl;
import support.CommunicationControl;
import support.Console;
/**
 * The {@code AddElementIfMin} class represents a command that adds a new worker element to the collection if its salary
 * is less than the salary of all the elements in the collection.
 * The command requires the communicationControl and collectionControl instances to communicate with the user
 * and modify the collection.
 * Extends {@code AbstractCommand} abstract class.
 */
public class AddElementIfMin extends AbstractCommand {

    CollectionControl collectionControl;

    CommunicationControl communicationControl;


    public AddElementIfMin(CollectionControl collectionControl, CommunicationControl communicationControl) {
        super("add_if_min", "Добавить новым элемент в коллекцю, если меньше" +
                "минимального в коллекции");
        this.collectionControl = collectionControl;
        this.communicationControl = communicationControl;
    }

    /**
     * Executes the command to add a new worker element to the collection if its salary is less than the salary
     * of all the elements in the collection.
     *
     * @param argument the arguments passed to the command, not used in this case.
     */
    public void execute(String argument) {
        try {
            if (!argument.isEmpty()) throw new WrongArgumentsException();
            Console.writeln("создайте новый элемент перед тем как сравнивать:");
            Worker newWorker = new Worker(communicationControl.setName(),
                    communicationControl.setCoordinates(),
                    communicationControl.setSalary(), communicationControl.choosePosition(),
                    communicationControl.chooseStatus(), communicationControl.setPerson());
            if (!collectionControl.addIfSmallerSalary(newWorker)) newWorker = null;
        } catch (WrongArgumentsException e) {
            Console.err("Превышенно кол-во аргементов");
        } catch (InputException e) {
            Console.err("Некорректный данные в скрипте!");
        }
    }
}
