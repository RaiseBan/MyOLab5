
package support;

import commands.Command;
import data.Person;
import data.Status;
import data.Worker;
import exceptions.InputException;

import javax.xml.stream.XMLStreamException;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
/**
 * The CollectionControl class manages a collection of Worker objects and provides methods for manipulating
 * and accessing the data. It implements various operations such as sorting, filtering, grouping and updating
 * the elements of the collection. It also interacts with the FileControl and CommunicationControl classes to
 * read/write data from/to files and receive input from the user, respectively. The class has a HashMap to store
 * the mapping of available commands, and a LocalDateTime variable to keep track of the time of initialization
 * of the collection. The class has public methods for adding, removing and updating elements of the collection,
 * as well as for displaying information about the collection.
 */
public class CollectionControl {
    private final ArrayList<Worker> workersCollection = new ArrayList<>();
    HashMap<String, Command> BufferOfCommandMap;
    protected static LocalDateTime timeInitialization = null;
    FileControl fileControl;
    CommunicationControl communicationControl;

    /**
     * Constructor for the CollectionControl class. It initializes the fileControl and communicationControl objects
     * and sets the BufferOfCommandMap to null.
     *
     * @param fileControl          the FileControl object to use for reading/writing data from/to files
     * @param communicationControl the CommunicationControl object to use for receiving input from the user
     */

    public CollectionControl(FileControl fileControl, CommunicationControl communicationControl) {
        this.fileControl = fileControl;
        this.communicationControl = communicationControl;
        this.BufferOfCommandMap = null;
    }

    /**
     * Sets the BufferOfCommandMap to the specified HashMap.
     *
     * @param map the HashMap to set the BufferOfCommandMap to
     */

    public void getMappingOfCommands(HashMap<String, Command> map) {
        this.BufferOfCommandMap = map;
    }


    /**
     * Returns the current BufferOfCommandMap HashMap.
     *
     * @return the current BufferOfCommandMap HashMap
     */
    public HashMap<String, Command> sendCommandMap() {
        return this.BufferOfCommandMap;
    }

    /**
     * Clears the workersCollection ArrayList.
     */

    public void clear() {
        workersCollection.clear();
        Console.writeln("Коллекция очистилась...");

    }

    /**
     * Sorts the workersCollection ArrayList in ascending order based on the natural ordering of the elements.
     */
    public void sort() {
        Collections.sort(workersCollection);
    }

    /**
     * Removes all elements from the workersCollection ArrayList that are greater than the specified Worker object
     * based on the natural ordering of the elements.
     *
     * @param enotherWorker the Worker object to compare the elements to
     */
    public void removeGreater(Worker enotherWorker) {
        workersCollection.removeIf(worker -> enotherWorker.compareTo(worker) > 0);
    }

    /**
     * Saves the current workersCollection ArrayList to a file with the specified name.
     *
     * @param file the name of the file to save the collection to
     */
    public void saveCollection(String file) {
        try {
            fileControl.writeToFile(workersCollection, file);
        } catch (IOException | XMLStreamException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Filters workers with a status greater than the given string and returns an ArrayList of the filtered workers.
     *
     * @param line the status to compare against.
     * @return an ArrayList of the filtered workers.
     * @throws IllegalArgumentException if the given string is not a valid status.
     */

    public ArrayList<Worker> filterGreaterThanStatus(String line) throws IllegalArgumentException {
        ArrayList<Worker> workerList = new ArrayList<>();
        try {
            Status person = Status.valueOf(line.toUpperCase());


            for (Worker worker : workersCollection) {
                if (person.ordinal() < worker.getStatus().ordinal()) {
                    workerList.add(worker);
                }
            }
            return workerList;
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException();
        }
    }

    /**
     * Groups the workers in the collection by their status and outputs the number of workers for each status.
     */
    public void gropByStatus() {

        Map<Status, List<Worker>> workersByStatus = workersCollection.stream().collect(Collectors.groupingBy(Worker::getStatus));
        for (Status status : workersByStatus.keySet()) {
            Console.writeln("Кол-во работников со статусом '" + status + "': " + workersByStatus.get(status).size());
        }
    }

    /**
     * Outputs information about the worker collection, including the type, time of initialization, and number of elements.
     */
    public void getInfo() {
        Console.writeln("Тип: Worker" + "\n" + "Время инициализации: " + timeInitialization + "\n" + "количество элементов: " + workersCollection.size());

    }

    /**
     * Sorts the workers in the collection by their person object and outputs the sorted list.
     */
    public void sortPerson() {
        ArrayList<Person> sortedPerson = new ArrayList<>();
        for (Worker worker : workersCollection)
            sortedPerson.add(worker.getPerson());
        Collections.sort(sortedPerson);
        for (Person person : sortedPerson) {
            Console.writeln(person.toString());
        }
    }

    /**
     * Removes the worker from the collection with the given ID.
     *
     * @param id the ID of the worker to remove.
     */
    public void removeElementByID(int id) {
        try {
            workersCollection.remove(id);
        } catch (NoSuchElementException e) {
            Console.err("Элемента с такии id нет в коллекции");
        }
    }

    /**
     * Outputs each worker in the collection using their toString method.
     */

    public void show() {
        for (Worker worker : workersCollection) {
            Console.writeln(worker.toString());
        }
    }

    /**
     * Adds the given worker to the collection.
     *
     * @param worker the worker to add to the collection.
     */
    public void addToCollection(Worker worker) {
        workersCollection.add(worker);
    }

    /**
     * Adds the given worker to the collection if their salary is smaller than the minimum salary in the collection.
     *
     * @param newWorker the worker to add to the collection.
     * @return true if the worker was added, false otherwise.
     */

    public boolean addIfSmallerSalary(Worker newWorker) {
        if (workersCollection.isEmpty() || newWorker.getSalary() < Collections.min(workersCollection, Comparator.comparing(Worker::getSalary)).getSalary()) {
            workersCollection.add(newWorker);
            return true;
        }
        return false;
    }

    /**
     * Updates the worker in the collection with the given ID.
     *
     * @param id the ID of the worker to update.
     */
    public void updateByID(int id) {
        try {
            if (id > workersCollection.size()) throw new InputException();
            workersCollection.set(id - 1, new Worker(id, communicationControl.setName(), communicationControl.setCoordinates(), communicationControl.setSalary(), communicationControl.choosePosition(), communicationControl.chooseStatus(), communicationControl.setPerson()).setID(id));

        } catch (InputException e) {
            Console.err("такого рабочего нет");
        }
    }

    /**
     * Clear all id of elements in collection
     */
    public void updateAllIDs() {
        for (int i = 0; i < workersCollection.size(); i++) {
            workersCollection.get(i).setID(i + 1);
        }
    }

}
