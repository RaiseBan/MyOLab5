
package support;

import data.Worker;
import exceptions.EmptyInputException;
import exceptions.InputException;
import exceptions.PermissionsDeniedException;
import exceptions.WrongArgumentsException;

import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * The FileControl class provides methods to read and write files related to worker information
 */
public class FileControl {
    private final String[] file;

    /**
     * Constructor for FileControl
     *
     * @param file an array of strings representing the file path
     */

    public FileControl(String[] file) {
        this.file = file;
        CollectionControl.timeInitialization = LocalDateTime.now();
    }

    /**
     * Writes a list of workers to an XML file specified by the given path. The XML file will contain the name, coordinates,
     * salary, position, status, and personal information (birthday, height, passportID, and location) of each worker.
     *
     * @param workers the list of workers to be written to the fil
     * @throws IOException        if an I/O error occurs while writing the file
     * @throws XMLStreamException if an error occurs while writing the XML document
     */
    public void writeToFile(ArrayList<Worker> workers) throws IOException, XMLStreamException {
        try {
            String file = this.file[0].trim();
            XMLOutputFactory factory = XMLOutputFactory.newInstance();
            XMLStreamWriter writer = factory.createXMLStreamWriter(new FileOutputStream(file), "UTF-8");
            writer.writeStartDocument("UTF-8", "1.0");
            writer.writeStartElement("workers");
            for (Worker worker : workers) {
                writer.writeStartElement("worker");
                writer.writeStartElement("name");
                writer.writeCharacters(worker.getName());
                writer.writeEndElement();
                writer.writeStartElement("coordinates");
                writer.writeStartElement("x");
                writer.writeCharacters(String.valueOf(worker.getCoordinates().getX()));
                writer.writeEndElement();
                writer.writeStartElement("y");
                writer.writeCharacters(String.valueOf(worker.getCoordinates().getY()));
                writer.writeEndElement();
                writer.writeEndElement();
                writer.writeStartElement("salary");
                writer.writeCharacters(String.valueOf(worker.getSalary()));
                writer.writeEndElement();
                writer.writeStartElement("position");
                writer.writeCharacters(String.valueOf(worker.getPosition()));
                writer.writeEndElement();
                writer.writeStartElement("status");
                writer.writeCharacters(String.valueOf(worker.getStatus()));
                writer.writeEndElement();
                writer.writeStartElement("person");
                writer.writeStartElement("birthday");
                writer.writeCharacters(String.valueOf(worker.getPerson().getBirthday()).substring(0, String.valueOf(worker.getPerson().getBirthday()).length() - 6));
                writer.writeEndElement();
                writer.writeStartElement("height");
                writer.writeCharacters(String.valueOf(worker.getPerson().getHeight()));
                writer.writeEndElement();
                writer.writeStartElement("passportID");
                writer.writeCharacters(worker.getPerson().getPassportID());
                writer.writeEndElement();
                writer.writeStartElement("location");
                writer.writeStartElement("x");
                writer.writeCharacters(String.valueOf(worker.getPerson().getLocation().getX()));
                writer.writeEndElement();
                writer.writeStartElement("y");
                writer.writeCharacters(String.valueOf(worker.getPerson().getLocation().getY()));
                writer.writeEndElement();
                writer.writeStartElement("z");
                writer.writeCharacters(String.valueOf(worker.getPerson().getLocation().getZ()));
                writer.writeEndElement();
                writer.writeStartElement("name");
                writer.writeCharacters(worker.getPerson().getLocation().getName());
                writer.writeEndElement();
                writer.writeEndElement();
                writer.writeEndElement();
            }
            writer.writeEndElement();
            writer.writeEndDocument();
            writer.flush();
            writer.close();
        } catch (FileNotFoundException e) {
            Console.err("сюда сохранить невозможно!");
        } catch (Exception e){
            Console.err("неверные данные записи");
        }
    }

    /**
     * Reads worker information from an XML file and returns a list of workers
     *
     * @return a List of workers read from the XML file
     */
    public List<Worker> readXmlFile() throws InputException {
        try {
            if (file.length != 1) {
                // Генерируем WrongArgumentsException, если в массиве не ровно один аргумент
                throw new WrongArgumentsException();
            }
            if (!checkFilePermissions(this.file[0])) {
                throw new InputException();
            }

            ParserXml parserXml = new ParserXml(this.file[0]);
            return parserXml.parseWorkersFromXML();


        } catch (WrongArgumentsException e) {
            Console.err("В арументы командной сроки было переданно " + this.file.length +
                    " количество аргументов должно быть 1");
            return null;
        }

    }
    /**
     * Checks the permissions of the file specified by the argument
     *
     * @param arg a string representing the file path to check the permissions of
     */
    public static boolean checkFilePermissions (String arg){
        try {

            // Получаем путь к файлу из первого аргумента
            if (arg.isEmpty()) {
                // Генерируем EmptyInputException, если путь к файлу пустой
                throw new EmptyInputException();
            }
            File file = new File(arg);
            if (!file.exists()) {

                // Генерируем FileNotFoundException, если файл не существует
                throw new FileNotFoundException();
            }
            if (!file.canRead() || !file.canWrite()) {
                // Генерируем PermissionsDeniedException, если файл не имеет необходимых прав доступа
                throw new PermissionsDeniedException();
            }
            return true;
        } catch (EmptyInputException e) {
            Console.err("в аргумент командной строки было передано null");
            return false;
        } catch (FileNotFoundException e) {
            Console.err("Файл не найден, проверьте Path");
            return false;
        } catch (IOException e) {
            Console.err("Права этого файла не позволяют использовать его, попробуйте изменить права");
            return false;
        }

    }


}
