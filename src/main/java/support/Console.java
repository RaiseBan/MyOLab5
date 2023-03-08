
package support;

import commands.Command;
import commands.Exit;
import data.Worker;
import exceptions.EmptyInputException;
import exceptions.InputException;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Scanner;
/**
 * A class that represents a console for interacting with the program. It handles user input, executes commands, and
 * outputs messages to the console.
 */
public class Console {
    private final Scanner scanner;
    private FileControl fileControl;
    private CollectionControl collectionControl;
    private final CommandControl commandControl;

    /**
     * Constructs a new Console object.
     *
     * @param scanner           a Scanner object to read user input from the console
     * @param commandControl    a CommandControl object to access and execute commands
     * @param fileControl       a FileControl object to read and write data to files
     * @param collectionControl a CollectionControl object to access and manipulate the collection of workers
     */

    public Console(Scanner scanner, CommandControl commandControl, FileControl fileControl, CollectionControl collectionControl) {
        this.scanner = scanner;
        this.commandControl = commandControl;
        this.fileControl = fileControl;
        this.collectionControl = collectionControl;

    }

    /**
     * Starts an interactive session with the user. Reads user input, executes commands, and outputs messages to the console.
     */
    public void interactive() {
        String[] userCommand;
        List<Worker> setWorkerList = null;
        try {
            setWorkerList = fileControl.readXmlFile();
        }catch (InputException e){
            Console.err("нет доступа к файлу...");
        }

        if (setWorkerList != null) {
            for (Worker worker : setWorkerList) {
                collectionControl.addToCollection(worker);
            }
        }


        while (true) {
            try {
                Console.write(">>");
                userCommand = (scanner.nextLine().trim() + " ").split(" ", 2);
                if (userCommand.length == 0) throw new EmptyInputException();
                chooseCommand(userCommand).execute(userCommand[1]);
            } catch (InputException e) {
                Console.err("Введенной команды не существет. Введите командочку help");
            } catch (EmptyInputException e) {
                Console.err(e.getMessage());
            }catch(NoSuchElementException e){
                Console.err("НЕльзя так");
                System.exit(0);
            }
        }
    }

    /**
     * Searches for and returns the Command object corresponding to the user's input.
     *
     * @param userCommand an array containing the user's input, with the first element being the command name and the second element being its arguments
     * @return the Command object corresponding to the user's input
     * @throws InputException if the command specified by the user is not recognized
     */

    public Command chooseCommand(String[] userCommand) throws InputException {
        for (String key : commandControl.getMapping().keySet()) {
            if (key.equalsIgnoreCase(userCommand[0])) {
                return commandControl.getMapping().get(key);
            }
        }
        throw new InputException();
    }


    /**
     * Outputs the specified object to the console, without adding a newline character.
     *
     * @param outPut the object to output to the console
     */
    public static void write(Object outPut) {
        System.out.print(outPut);
    }

    /**
     * Outputs the specified object to the console, followed by a newline character.
     *
     * @param outPut the object to output to the console
     */
    public static void writeln(Object outPut) {
        System.out.println(outPut);
    }

    /**
     * Outputs the specified object to the console as an error message, preceded by the string "Error: ".
     *
     * @param outPut the object to output to the console as an error message
     */
    public static void err(Object outPut) {
        System.err.println("Error: " + outPut);
    }
}
