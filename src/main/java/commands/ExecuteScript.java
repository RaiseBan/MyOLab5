

package commands;

import exceptions.EmptyInputException;
import exceptions.InputException;
import exceptions.WrongArgumentsException;
import support.*;
import support.Console;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * The ExecuteScript class represents a command to execute a script file.
 */
public class ExecuteScript extends AbstractCommand {
    CollectionControl collectionControl;
    CommunicationControl communicationControl;
    boolean flag = true;
    Stack<InputStream> stack = new Stack<>();
    Stack<String> stackWithFiles = new Stack<>();

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
    public void execute(String argument) {
        argument = argument.trim();
        stackWithFiles.push(argument);
        try {

            if (argument.isEmpty()) throw new WrongArgumentsException();
            if (!FileControl.checkFilePermissions(argument)) throw new InputException();
            List<String> outputCommandsName = new ArrayList<>();
            List<String> outputDataName = new ArrayList<>();

            fileProcessor(argument, outputDataName, outputCommandsName);


            String inputCommand = String.join(System.lineSeparator(), outputCommandsName);
            InputStream fileWithCommands = new ByteArrayInputStream(inputCommand.getBytes(StandardCharsets.UTF_8));
            String inputData = String.join(System.lineSeparator(), outputDataName);
            InputStream fileWithData = new ByteArrayInputStream(inputData.getBytes(StandardCharsets.UTF_8));
            Scanner scanner = new Scanner(fileWithCommands);
            communicationControl.setUnsetLoop();

            try (scanner) {
                // Заменяем стандартный поток ввода на InputStream из файла
                communicationControl.changeScanner(fileWithData);
                stack.push(fileWithData);
                while (scanner.hasNextLine()) {
                    String line = scanner.nextLine().trim();
                    String[] args = (line.trim() + " ").split(" ");
                    HashMap<String, Command> commandMap = collectionControl.sendCommandMap();

                    for (String key : commandMap.keySet()) {
                        if (args.length == 2) {
                            if ((key.equalsIgnoreCase("execute_script")) && (key.equalsIgnoreCase(args[0].trim())) && (stackWithFiles.contains(args[1]))){
                                for (String str :stackWithFiles){
                                    System.out.println(str);
                                }
                                System.out.println(argument);
                                throw new WrongArgumentsException("Нельзя передавать один и тот же файл (возникает рекурсия)");
                            }
                        }
                        if (key.equalsIgnoreCase(args[0].trim())) {
                            String argumentForExecute;
                            if (args.length == 2) {
                                argumentForExecute = args[1];
                            } else {
                                argumentForExecute = "";
                            }
                            if (key.equalsIgnoreCase("execute_script")){
                                flag = false;
                            }
                            commandMap.get(key).execute(argumentForExecute);
                        }
                    }
                }
            }catch (WrongArgumentsException e) {
                Console.err(e.getMessage());
            } finally {
                // Восстанавливаем стандартный поток ввода

                if (flag) {
                    communicationControl.setUnsetLoop();
                    communicationControl.changeScanner(System.in);
                }else {
                    stack.pop();
                    try {
                        communicationControl.changeScanner(stack.peek());
                    }catch (EmptyStackException e){
                        communicationControl.changeScanner(System.in);
                    }
                }
            }

        } catch (WrongArgumentsException e) {
            Console.err("название скрипта не введено");

        } catch (IOException e) {
            Console.err("нет досупа к файлу");
        }
    }

    /**
     * Processes the script file and writes the commands to a file.
     *
     * @param file the script file to process
     */
    private void fileProcessor(String file, List<String> outputDataName, List<String> outputCommandsName) {
        try {
            Scanner scanner = new Scanner(new File(file));
            while (scanner.hasNextLine()) {
                String[] args;
                String line = scanner.nextLine();
                args = (line.trim() + " ").split(" ", 2);
                if (args.length == 0) throw new EmptyInputException();
                if (!collectionControl.sendCommandMap().containsKey(args[0].trim())) {
                    outputDataName.add(line);
                } else {
                    outputCommandsName.add(args[0] + " " + args[1]);
                }
            }

        } catch (IOException e) {
            Console.writeln("Файла не найдено");
            e.printStackTrace();
        }
    }
}
