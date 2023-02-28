
package commands;

import data.Worker;
import exceptions.WrongArgumentsException;
import support.CollectionControl;
import support.CommunicationControl;
import support.Console;
/**
 * The {@code FilterGreaterStatus} class represents a command that filters the elements in the collection by the status field
 * and displays those whose value is greater than the specified one.
 */
public class FilterGreaterStatus extends AbstractCommand {
    CollectionControl collectionControl;
    CommunicationControl communicationControl;

    /**
     * Constructs a new {@code FilterGreaterStatus} object with the specified {@code CollectionControl} and {@code CommunicationControl}.
     * @param collectionControl the {@code CollectionControl} object to control the collection
     * @param communicationControl the {@code CommunicationControl} object to interact with user
     */
    public FilterGreaterStatus(CollectionControl collectionControl, CommunicationControl communicationControl) {
        super("filter_greater_than_status", "вывести элементы, значение поля status которых больше заданного");
        this.collectionControl = collectionControl;
        this.communicationControl = communicationControl;


    }

    /**
     * Executes the command to filter the elements in the collection by the status field and displays those whose value is greater than the specified one.
     * @param argument the command argument (not used)
     */
    @Override
    public void execute(String argument) {
        String line;

        try {
            if (!argument.isEmpty()) throw new WrongArgumentsException();
            Console.writeln("Введите значение с которым вы хотите сравнивать");
            line = communicationControl.setEnotherInfo();

            for (Worker worker : collectionControl.filterGreaterThanStatus(line)) {
                Console.writeln(worker.toString());
            }
        } catch (IllegalArgumentException e) {
            Console.err("Не является элементом Status");
        } catch (WrongArgumentsException e) {
            Console.err("Неверное кол-во аргементов...");
        }
    }
}
