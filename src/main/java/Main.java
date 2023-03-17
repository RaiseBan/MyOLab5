import commands.*;
import support.*;

import java.util.Scanner;

/**
 * Entry point to the program. Create objects for the program to work
 */

public class Main {
    /**
     * The main method is the entry point of the program. It creates instances of
     * various control classes and passes them to a Console object to handle user
     * input and execute commands.
     *
     * @param args An array of command-line arguments.
     */

    public static void main(String[] args) {
        try (Scanner scanner = new Scanner(System.in)) {
            FileControl fileControl = new FileControl(args);
            CommunicationControl communicationControl = new CommunicationControl(scanner);
            CollectionControl collectionControl = new CollectionControl(fileControl, communicationControl);
            CommandControl commandControl = new CommandControl(new AddElement(communicationControl, collectionControl),
                    new AddElementIfMin(collectionControl, communicationControl),
                    new Clear(collectionControl, communicationControl),
                    new ExecuteScript(collectionControl, communicationControl), new Exit(), new FilterGreaterStatus(collectionControl, communicationControl),
                    new GroupByStatus(collectionControl), new Help(collectionControl), new Info(collectionControl), new PrintFieldOfPerson(collectionControl),
                    new RemoveElementByID(collectionControl, communicationControl), new RemoveGreater(collectionControl, communicationControl), new SaveCollection(fileControl, collectionControl),
                    new Show(collectionControl), new Sort(collectionControl), new UpdateByID(collectionControl), collectionControl);
            Console console = new Console(scanner, commandControl, fileControl, collectionControl);
            console.interactive();
        }

    }

}