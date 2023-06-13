package Utils.abobas;

import Classes.Person;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedHashSet;

public class AbobaReader {
    public static LinkedHashSet<Person> read(String fileName) {
        LinkedHashSet<Person> persons = new LinkedHashSet<>();
        try (InputStream inputStream = new FileInputStream(fileName)) {
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance(); //получили билдер документов
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();//получли билдер, который парсит XML, создает структуру Document в виде иерархического дерева
            Document document = documentBuilder.parse(inputStream); // Запарсили XML, создав структуру Document. Теперь у нас есть доступ ко всем элементам, каким нам нужно
            document.getDocumentElement().normalize();
            NodeList personElements = document.getElementsByTagName("person"); // Получение списка всех элементов person внутри корневого элемента
            for (int i = 0; i < personElements.getLength(); i++) { // Перебор всех элементов person

            }
            for (Person person : persons)
                System.out.println(String.format("Информации о людях: имя - %s, рост - %s, id - %s, цвет глаз - %s, национальность - %s", person.getName(), person.getHeight(), person.getId(), person.getEyeColor(), person.getCountry()));
        }

        catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ParserConfigurationException e) {
            throw new RuntimeException(e);
        } catch (SAXException e) {
            throw new RuntimeException(e);
        }

        return persons;
    }
}
