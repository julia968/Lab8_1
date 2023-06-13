package Utils;

import Classes.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Scanner;

public class PersonBuilder {
    public static Person build() {
        Person person = new Person();
        Scanner scanner = new Scanner(System.in);
        System.out.println("Введите имя человека, которого хотите добавить в коллекцию: ");
        boolean noOkay = true;
        while (noOkay) {
            String userName = scanner.nextLine().trim();
            if (!userName.isEmpty()) {
                person.setName(userName);
                noOkay = false;
            } else {
                System.out.println("Произошла ошибка, повторите попытку ввода имени!");
            }
        }
        System.out.println("Введите рост: ");
        noOkay = true;
        while (noOkay) {
            String userHeight = scanner.nextLine().trim();
            try {
                person.setHeight(Long.parseLong(userHeight));
                noOkay = false;
            } catch (NumberFormatException numberFormatException) {
                System.out.println("Произошла ошибка, повторите попытку ввода роста!");
            }
        }
        System.out.println("Введите цвет глаз человека {доступные цвета: " + Arrays.toString(EyeColor.values()) + "}");
        noOkay = true;
        while (noOkay) {
            String userEyeColor = scanner.nextLine().trim();
            if (userEyeColor.equals("GREEN")) {
                person.setEyeColor(EyeColor.valueOf(userEyeColor));
                noOkay = false;
            } else if (userEyeColor.equals("BLACK")) {
                person.setEyeColor(EyeColor.valueOf(userEyeColor));
                noOkay = false;
            } else if (userEyeColor.equals("YELLOW")) {
                person.setEyeColor(EyeColor.valueOf(userEyeColor));
                noOkay = false;
            } else {
                System.out.println("Нужно выбрать цвет глаз из списка!");
            }
        }

        System.out.println("Введите национальность человека {доступные национальности: " + Arrays.toString(Country.values()) + "}");
        noOkay = true;
        while (noOkay) {
            String userNationality = scanner.nextLine().trim();
            if (userNationality.equals("USA")) {
                person.setNationality(Country.valueOf(userNationality));
                noOkay = false;
            } else if (userNationality.equals("UNITED_KINGDOM")) {
                person.setNationality(Country.valueOf(userNationality));
                noOkay = false;
            } else if (userNationality.equals("GERMANY")) {
                person.setNationality(Country.valueOf(userNationality));
                noOkay = false;
            } else if (userNationality.equals("THAILAND")) {
                person.setNationality(Country.valueOf(userNationality));
                noOkay = false;
            } else {
                System.out.println("Нужно выбрать национальность из списка!");
            }
        }
        boolean shouldContinue = true;
        Long longResult = null;
        while (shouldContinue) {
            System.out.println("Введите координату по X: ");
            try {
                longResult = Long.parseLong(scanner.next());
                shouldContinue = false;
            } catch (NumberFormatException e) {
                shouldContinue = true;
            }
        }
        String userX = longResult.toString();

        boolean shouldContinue1 = true;
        Integer intResult = null;
        while (shouldContinue1) {
            System.out.println("Введите координату по Y: ");
            try {
                intResult = Integer.parseInt(scanner.next());
                shouldContinue1 = false;
            } catch (NumberFormatException e) {
                shouldContinue1 = true;
            }
        }
        String userY = intResult.toString();
        person.setCoordinates(new Coordinates(Long.parseLong(userX), Integer.parseInt(userY)));
        System.out.println("Введите локацию по X: ");
        String userLocationX = scanner.next();
        System.out.println("Введите локацию по Y: ");
        String userLocationY = scanner.next();
        person.setLocation(new Location(Long.parseLong(userLocationX), Float.valueOf(userLocationY)));

        System.out.println("Введите дату дня рождения(пример ввода: 01-01-2001)");
        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
        Date res = null;
        try {
            Date date = format.parse(scanner.next());
            res = date;
        } catch (ParseException e) {
        }
        System.out.println(res);
        person.setBirthday(res);
        return person;
    }
}
