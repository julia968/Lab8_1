package Utils.abobas;

import database.CollectionOfPerson;
import Classes.Person;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.LinkedList;

public class AbobaWriter {
    public void write(LinkedList<Person> people) {
        try {
            people.addAll(CollectionOfPerson.getPeople());
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance(); //получили билдер документов
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();//получли билдер, который парсит XML, создает структуру Document в виде иерархического дерева
            Document document = documentBuilder.newDocument();

            Element root = document.createElement("set");
            document.appendChild(root);

            for (int i = 0; i < people.size(); i++) {
                Element details = document.createElement("person");
                root.appendChild(details);

                Element id = document.createElement("id");
                id.appendChild(document.createTextNode(String.valueOf(people.get(i).getId())));
                details.appendChild(id);

                Element name = document.createElement("name");
                name.appendChild(document.createTextNode((people.get(i).getName())));
                details.appendChild(name);

                Element height = document.createElement("height");
                height.appendChild(document.createTextNode(Double.toString(people.get(i).getHeight())));
                details.appendChild(height);

                Element eyeColor = document.createElement("eyeColor");
                eyeColor.appendChild(document.createTextNode(String.valueOf(people.get(i).getEyeColor())));
                details.appendChild(eyeColor);

                Element nationality = document.createElement("nationality");
                nationality.appendChild(document.createTextNode(String.valueOf(people.get(i).getCountry())));
                details.appendChild(nationality);

                Element coordinates = document.createElement("coordinates");
                coordinates.appendChild(document.createTextNode(String.valueOf(people.get(i).getCoordinates())));
                details.appendChild(coordinates);

                Element location = document.createElement("location");
                location.appendChild(document.createTextNode(String.valueOf(people.get(i).getLocation())));
                details.appendChild(location);

                Element creationDate = document.createElement("creationDate");
                creationDate.appendChild(document.createTextNode(String.valueOf(people.get(i).getCreationDate())));
                details.appendChild(creationDate);


            }

            TransformerFactory tranFactory = TransformerFactory.newInstance();
            Transformer aTransformer = tranFactory.newTransformer();

            // format the XML nicely
            aTransformer.setOutputProperty(OutputKeys.ENCODING, "ISO-8859-1");

            aTransformer.setOutputProperty(
                    "{http://xml.apache.org/xslt}indent-amount", "5");
            aTransformer.setOutputProperty(OutputKeys.INDENT, "yes");



            DOMSource source = new DOMSource(document);
            try {
                BufferedOutputStream fos = new BufferedOutputStream(new FileOutputStream("aboba.xml"));
                StreamResult result = new StreamResult(fos);
                aTransformer.transform(source, result);

            } catch (IOException e) {

                e.printStackTrace();
            }


        } catch (ParserConfigurationException | TransformerException e) {
            throw new RuntimeException(e);
        }
    }

}