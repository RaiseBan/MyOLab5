
package commands;

import exceptions.WrongArgumentsException;
import support.CollectionControl;
import support.Console;
/**
 * This command groups the elements of the collection by the value of the status field and displays the number of elements in each group.
 */
public class GroupByStatus extends AbstractCommand {
    CollectionControl collectionControl;

    public GroupByStatus(CollectionControl collectionControl) {
        super("group_counting_by_status", "сгруппировать элементы коллекции по назначению поля status, вывести кол-во элементов в каждой группе");
        this.collectionControl = collectionControl;
    }

    @Override
    public void execute(String argument) {
        try {
            if (!argument.isEmpty()) throw new WrongArgumentsException();
            collectionControl.gropByStatus();
        } catch (WrongArgumentsException e) {
            Console.err("Неверное кол-во аргементов");
        }
    }
}