
package support;

import data.*;
import exceptions.InputException;
import exceptions.WrongArgumentsException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static support.CommunicationControl.containsOnlyDigitsOrLetters;
/**
 * The ParserXml class is responsible for parsing worker data from an XML file.
 * It uses the javax.xml.parsers library to read the XML file and extract the
 * necessary data, and creates Worker objects with the extracted data.
 */
public class ParserXml {
    String file;

    /**
     * Constructs a new ParserXml object with the given file path.
     *
     * @param file the file path of the XML file to be parsed.
     */
    public ParserXml(String file) {
        this.file = file;
    }

    /**
     * Parses worker data from the XML file specified in the file field.
     * Returns a list of Worker objects with the parsed data.
     *
     * @return a list of Worker objects with the parsed data, or null if there is an error parsing the XML file.
     */
    public List<Worker> parseWorkersFromXML() {
        File xmlFile = new File(this.file);
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder;

        try {
            dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(xmlFile);
            doc.getDocumentElement().normalize();
            NodeList nodeList = doc.getElementsByTagName("worker");
            List<Worker> workerList = new ArrayList<>();
            for (int i = 0; i < nodeList.getLength(); i++) {
                workerList.add(getWorker(nodeList.item(i)));
            }
            return workerList;
        } catch (Exception e) {
            Console.err("Неверные данные в xml файле!");
            return null;
        }

    }

    /**
     * Extracts worker data from the specified XML node and creates a new Worker object with the data.
     *
     * @param node the XML node containing the worker data.
     * @return a new Worker object with the extracted data.
     * @throws InputException if the extracted data is invalid.
     */
    private Worker getWorker(Node node) throws InputException {
        if (node.getNodeType() == Node.ELEMENT_NODE) {
            Element element = (Element) node;
            String name = getTagValue("name", element);
            if (!containsOnlyDigitsOrLetters(name, false)) throw new InputException();
            Coordinates coordinates = getCoordinates(element);
            Double salary = Double.parseDouble(getTagValue("salary", element));
            if (salary <= 0) throw new InputException();
            Position position = Position.valueOf(getTagValue("position", element).toUpperCase());
            Status status = Status.valueOf(getTagValue("status", element).toUpperCase());
            Person person = getPerson(element);
            return new Worker(name, coordinates, salary, position, status, person);
        }
        return null;


    }

    /**
     * Extracts Coordinates data from the specified XML element and creates a new Coordinates object with the data.
     *
     * @param element the XML element containing the Coordinates data.
     * @return a new Coordinates object with the extracted data.
     * @throws InputException if the extracted data is invalid.
     */
    private Coordinates getCoordinates(Element element) throws InputException {
        Element coordinatesElement = (Element) element.getElementsByTagName("coordinates").item(0);
        int x = Integer.parseInt(getTagValue("x", coordinatesElement));
        if (x > 468) throw new InputException();
        int y = Integer.parseInt(getTagValue("y", coordinatesElement));
        if (y <= -922) throw new InputException();
        return new Coordinates(x, y);
    }

    /**
     * Returns a new Person object parsed from the input Element.
     *
     * @param element the input Element to be parsed.
     * @return a new Person object.
     * @throws InputException if the height is greater than 350 or the passport ID is not exactly 6 characters long.
     */
    private Person getPerson(Element element) throws InputException {

        Element personElement = (Element) element.getElementsByTagName("person").item(0);
        LocalDateTime birthday = getLocalDateTime(getTagValue("birthday", personElement));
        long height = Long.parseLong(getTagValue("height", personElement));
        if (height > 350) throw new InputException();
        String passportID = getTagValue("passportID", personElement);
        if (passportID.length() != 6) throw new InputException();
        Location location = getLocation(personElement);
        return new Person(birthday, height, passportID, location);
    }

    /**
     * Returns a new Location object parsed from the input Element.
     *
     * @param element the input Element to be parsed.
     * @return a new Location object.
     */
    private Location getLocation(Element element) {
        Element locationElement = (Element) element.getElementsByTagName("location").item(0);
        String name = getTagValue("name", locationElement);
        float x = Float.parseFloat(getTagValue("x", locationElement));
        long y = Long.parseLong(getTagValue("y", locationElement));
        int z = Integer.parseInt(getTagValue("z", locationElement));
        return new Location(x, y, z, name);
    }

    /**
     * Returns the value of a specified tag from the input Element.
     *
     * @param tag     the name of the tag.
     * @param element the input Element to search for the tag in.
     * @return the value of the specified tag.
     */
    private String getTagValue(String tag, Element element) {
        NodeList nodeList = element.getElementsByTagName(tag).item(0).getChildNodes();
        Node node = (Node) nodeList.item(0);
        return node.getNodeValue();
    }

    /**
     * Returns a LocalDateTime object parsed from the input date string.
     *
     * @param dateStr the input date string to be parsed.
     * @return a LocalDateTime object.
     */
    public LocalDateTime getLocalDateTime(String dateStr) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd", new Locale("ru", "Ru"));
            LocalDateTime bd = LocalDate.parse(dateStr, formatter).atStartOfDay();
            if (bd.isAfter(LocalDate.now().atStartOfDay())) throw new WrongArgumentsException();
            return bd;
        } catch (WrongArgumentsException e) {
            Console.err(e.getMessage());
            return null;
        } catch (DateTimeParseException e) {
            Console.err("Неверный формат даты: " + dateStr);
            return null;
        }
    }
}
