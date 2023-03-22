

package commands;

import exceptions.EmptyInputException;
import exceptions.InputException;
import exceptions.RecursionException;
import exceptions.WrongArgumentsException;
import support.*;
import support.Console;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

/**
 * The ExecuteScript class represents a command to execute a script file.
 */
public class ExecuteScript extends AbstractCommand {
    CollectionControl collectionControl;
    CommunicationControl communicationControl;
    boolean flag = true;
    static Stack<String> stackWithFiles = new Stack<>();
    static Stack<Scanner> stackWithScanners = new Stack<>();

    /**
     * Constructs a new ExecuteScript instance with the specified collection and communication controls.
     *
     * @param collectionControl    the collection control instance
     * @param communicationControl the communication control instance
     */
    public ExecuteScript(CollectionControl collectionControl, CommunicationControl communicationControl) {
        super("execute_script", "выполняет скрипт");
        this.collectionControl = collectionControl;
        this.communicationControl = communicationControl;
    }


    /**
     * Executes the command with the specified argument.
     *
     * @param argument the argument for the command
     */
    @Override
    public void execute(String argument) throws FileNotFoundException {
        argument = argument.trim();
        stackWithFiles.push(argument);


        try (Scanner scanner = new Scanner(new File(argument))) {
            stackWithScanners.push(scanner);
            communicationControl.changeScanner(scanner);
            if (argument.isEmpty()) throw new WrongArgumentsException();
            if (!FileControl.checkFilePermissions(argument)) throw new InputException();
            communicationControl.setUnsetLoop();
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine().trim();
                System.out.println(line);
                String[] args = (line.trim()).split(" ");
                if (!checkRecursion(line)) {
                    throw new RecursionException("Обнаружена рекурсия! Уберите");
                }
                HashMap<String, Command> commandMap = collectionControl.sendCommandMap();

                for (String key : commandMap.keySet()) {
                    if (key.equalsIgnoreCase(args[0].trim())) {
                        String argumentForExecute;
                        if (args.length == 2) {
                            argumentForExecute = args[1];
                        } else {
                            argumentForExecute = "";
                        }
                        if (key.equalsIgnoreCase("execute_script")) {

                            flag = false;
                        }
                        commandMap.get(key).execute(argumentForExecute);
                    }
                }
            }

        } catch (RecursionException e) {
            System.out.println(e.getMessage());
        } catch (InputException e) {
            Console.err("InputException");
        } catch (WrongArgumentsException e) {
            Console.err("Мало аргементов");
        } finally {
            if (flag) {
                communicationControl.setUnsetLoop();
                communicationControl.changeScanner(new Scanner(System.in));
            }else {
                stackWithScanners.pop();
                try {
                    communicationControl.changeScanner(stackWithScanners.peek());
                }catch (EmptyStackException | FileNotFoundException e){
                    communicationControl.changeScanner(new Scanner(System.in));
                }
            }
        }

    }

    public boolean checkRecursion(String currentCommand) {
        try {
            if (Objects.equals(currentCommand.split(" ")[0], "execute_script") && stackWithFiles.contains(currentCommand.split(" ")[1])) {
                return false;

            } else if (Objects.equals(currentCommand.split(" ")[0], "execute_script") && !stackWithFiles.contains(currentCommand.split(" ")[1])) {
                Path path = Paths.get(currentCommand.split(" ")[1]);
                stackWithFiles.push(currentCommand.split(" ")[1]);
                //chosenScanner = new Scanner(path);
            }
        } catch (Exception e) {
            System.out.println("Ты ошибка!");
        }
        return true;
    }
}

