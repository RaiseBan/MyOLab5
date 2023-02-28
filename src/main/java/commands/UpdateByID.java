
package commands;

import exceptions.WrongArgumentsException;
import support.CollectionControl;
import support.Console;
/**
 * This command is used to update the value of a collection element whose id matches the specified one.
 */

public class UpdateByID extends AbstractCommand {
    CollectionControl collectionControl;

    public UpdateByID(CollectionControl collectionControl) {
        super("update_by_id", "Обновить значение элемента коллекции, id  которого равен заданному");
        this.collectionControl = collectionControl;
    }

    /**
     * Executes the command with the specified argument
     *
     * @param argument the id of the element to be updated
     */
    @Override
    public void execute(String argument) {
        try {
            if (argument.isEmpty()) throw new WrongArgumentsException();

            int id = Integer.parseInt(argument.trim());
            collectionControl.updateByID(id);
            Console.writeln("Замена успешно завершена!");
        } catch (WrongArgumentsException e) {
            Console.err(e.getMessage());
        } catch (NumberFormatException e) {
            Console.err("неправильный тип данных. Должен быть целочисленным");
        }
    }
}
