package Command;

import Utils.PersonBuilder;
import Classes.Country;
import Classes.Person;
import Utils.ArgumentListener;
import Utils.LineSplitter;
import database.DataBaseProvider;
import services.CurrentUserManager;
import sql.SQLConnection;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

public class SQLCommandUser {
    private static Map<String, Method> commands = new LinkedHashMap<>();
    private static List<String> commandHistory = new ArrayList<>();
    private ArgumentListener argumentsListener = new ArgumentListener();
    private final SQLConnection sqlConnection = new SQLConnection();
    private final DataBaseProvider source;

    public SQLCommandUser(CurrentUserManager userManager) {
        source = new DataBaseProvider(userManager);
        for (Method method : SQLCommandUser.class.getDeclaredMethods()) {
            if (method.isAnnotationPresent(Command1.class)) {
                Command1 command = method.getAnnotation(Command1.class);
                commands.put(command.name(), method);
            }
        }
    }
    @Command1(name = "help",
            args = "",
            countOfArgs = 0,
            desc = "Доступные пользователю команды",
            aliases = {})
    private void help() {
        StringBuilder sb = new StringBuilder("Список доступных команд: \n");
        for (Method m : this.getClass().getDeclaredMethods()) {
            if (m.isAnnotationPresent(Command1.class)) {
                Command1 com = m.getAnnotation(Command1.class);
                sb.append(com.name()).append(" ")
                        .append(com.args()).append(" - ")
                        .append(com.desc()).append("\n");
            }
        }
        System.out.println(sb);
    }


    @Command1(name = "info",
            args = "",
            countOfArgs = 0,
            desc = "Вывести информацию о коллекции",
            aliases = {})
    private void info() {
        System.out.println("Информация о коллекции: ");
        System.out.println(source.infoOfCollection());
    }

    @Command1(name = "add",
            args = "",
            countOfArgs =  0,
            desc = "Добавить элемент в коллекцию",
            aliases = {})
    private void add() throws IOException {
        if (source.addPersonToDataBase(PersonBuilder.build())){
            System.out.println("Успешно добавлено.");
        } else {
            System.out.println("Не было добавлено.");
        }
    }
    @Command1(name = "update",
            args = "{id}",
            countOfArgs = 1,
            desc = "Обновить данные об элементе коллекции по данному id(введите id)",
            aliases = {})
    private void update(String id) {
        int newId = Integer.parseInt(id);
        if (source.getDataSet().stream()
                .filter(p -> p.getCreator().equals(source.getUserManager().getUserName())).
                map(Person::getId)
                .toList().contains(newId)){

            Person element = source.getDataSet().stream().filter(p -> p.getId().equals(newId)).toList().get(0);
            System.out.println("Введите информацию о человеке: {name[записать буквами] height[>0, записать цифрами] nationality[выбрать: " + Arrays.toString(Country.values()));
            argumentsListener.inputPrimitives(element);
            argumentsListener.inputCoordinates();
            argumentsListener.inputEyeColor(element);

            source.updatePersonInDataBase(element, newId);
            source.updateDataSet();


            System.out.println("Данные о человеке успешно обновлены");
        } else {
            System.out.println("Человек не найден или не пренадлежит вам.");
            source.updateDataSet();
        }
    }


    @Command1(name = "exit",
            args = "",
            countOfArgs = 0,
            desc = "Выход из программы без сохранения",
            aliases = {})
    private void exit() throws IOException {
        System.exit(0);
    }


    @Command1(name = "remove_greater",
            args = "{heightFromConsole}",
            countOfArgs = 1,
            desc = "удалить из коллекции все элементы, превышающие заданный(введите параметр существования заданного человека в коллекции true/false и заданного человека",
            aliases = {})
    private void removerGreater(String heightFromConsole) {
        int before = source.getDataSet().size();
        int heightGreater = Integer.parseInt(heightFromConsole);

        source.getDataSet().stream().filter((p -> p.getHeight() > heightGreater)).toList().forEach(p->source.removePersonById(p.getId()));
        source.updateDataSet();

        int after = source.getDataSet().size();
        int countOfPerson = before - after;
        if (countOfPerson != 0) {
            System.out.println("Количество удалённых людей " + countOfPerson);
        }
    }


    @Command1(name = "remove_lower",
            args = "{heightFromConsole}",
            countOfArgs = 1,
            desc = "удалить из коллекции все элементы, меньшие, чем  заданный",
            aliases = {})
    private void removerLower(String heightFromConsole) {
        int before = source.getDataSet().size();
        int heightGreater = Integer.parseInt(heightFromConsole);

        source.getDataSet().stream().filter((p -> p.getHeight() < heightGreater)).toList().forEach(p->source.removePersonById(p.getId()));
        source.updateDataSet();

        int after = source.getDataSet().size();
        int countOfPerson = before - after;
        if (countOfPerson != 0) {
            System.out.println("Количество удалённых людей " + countOfPerson);
        }
    }

    @Command1(name = "remove_by_id",
            args = "{id}",
            countOfArgs = 1,
            desc = "удалить элемент из коллекции по его id",
            aliases = {})
    private void removeById(String id) {
        boolean personExist = false;
        int newId = Integer.parseInt(id);
        if (source.removePersonById(newId)){
            System.out.println("Человек с введённым id удалён из коллекции");
        } else {
            System.out.println("Такого человека не существует или он вам не пренадлежит.");
        }
    }

    @Command1(name = "clear",
            args = "",
            countOfArgs = 0,
            desc = "удаляет всех пользователей, которые принадлежат вам.",
            aliases = {})
    private void clear() {
        int before = source.getDataSet().size();

        source.getDataSet().stream()
                .filter(p -> p.getCreator().equals(source.getUserManager().getUserName()))
                .forEach(p->source.removePersonById(p.getId()));

        int after = source.getDataSet().size();
        int countOfPerson = before - after;
        System.out.println("Было удалено: " + countOfPerson);
    }

    @Command1(name = "history",
            args = "",
            countOfArgs = 0,
            desc = "Вывести последние 8 команд (без их аргументов)",
            aliases = {})
    private void showHistory() {
        final int countOfWatchableCommands = 8;
        if (commandHistory.size() > countOfWatchableCommands) {
            System.out.println(commandHistory.subList(commandHistory.size() - countOfWatchableCommands, commandHistory.size()));
        }
        if (commandHistory.size() < countOfWatchableCommands) {
            System.out.println("было использовано меньше 8 команд");
        }
        System.out.println(commandHistory);
    }


    @Command1(name = "max_by_name",
            args = "",
            countOfArgs = 0,
            desc = "Вывести любого человека из коллекции, значение поля name которого является максимальным",
            aliases = {})
    private void showMaxByName() {
        Person person = (Person) source.getDataSet().stream().max(new Person.SortByname()).get();
        System.out.println("Данные о человеке с самым длинным именем:\n" + person);
    }

    @Command1(name = "show",
            args = "",
            countOfArgs = 0,
            desc = "Показать всех людей в коллекции",
            aliases = {})
    private void show() {
        source.updateDataSet();
        source.getDataSet().forEach(System.out::println);
    }

    @Command1(name = "execute_script",
            args = "{filename}",
            countOfArgs = 1,
            desc = "Считать и исполнить скрипт из указанного файла",
            aliases = {})
    private void executeScript(String filename) {
        try {
            File starting = new File(System.getProperty("user.dir")); // Получаем текущий каталог пользователя
            File file = new File(starting, filename);
            Scanner sc = new Scanner(file);
            while (sc.hasNext()) {
                String nextLine = sc.nextLine();
                if (!("execute_script " + filename).equals(nextLine)) {
                    ArrayList<String> line = LineSplitter.smartSplit(nextLine);
                    invokeMethod(getCommandName(line), getCommandArguments(line));
                } else {
                    System.out.println("Ошибка выполнения. Скрипт вызывает сам себя.");
                    break;
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("Файла с таким именем в текущей папке нет. Переместите файл и повторите попытку");
        }
    }


    public void commandsReader() {
        while (true) { // цикл завершится только при вызове команды exit
            try {
                ArrayList<String> line = readCommandFromSystemIn();
                invokeMethod(getCommandName(line), getCommandArguments(line));
            } catch (NoSuchElementException e) {
                System.out.println("Введена команда прерывания работы приложения. Работа завершена");
                System.exit(0);
            }
        }
    }

    protected void invokeMethod(String commandName, ArrayList<String> commandArgs) {
        Method method = commands.get(commandName);
        commandHistory.add(commandName);
        try {
            Command1 command = method.getAnnotation(Command1.class);
            if (commandArgs.size() != command.countOfArgs()) {
                System.out.println("Неверное количество аргументов. Необходимо: " + command.countOfArgs());
            } else {
                method.invoke(this, commandArgs.toArray()); //вызываем метод
            }
        } catch (NullPointerException | IllegalAccessException e) {
            System.out.println("Команда некорректна или пуста, попробуйте еще раз");
        } catch (InvocationTargetException e) {
            System.out.println();
        }
    }

    protected static ArrayList<String> readCommandFromSystemIn() {
        Scanner scanner = new Scanner(System.in);
        String line = scanner.nextLine().toLowerCase();
        return LineSplitter.smartSplit(line);
    }

    protected static ArrayList<String> getCommandArguments(ArrayList<String> line) {
        line.remove(0);
        return line;
    }

    protected static String getCommandName(ArrayList<String> line) {
        return line.get(0);
    }
}
