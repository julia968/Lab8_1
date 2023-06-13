package Utils;

import Classes.EyeColor;
import Classes.Coordinates;
import Classes.Country;
import Classes.Person;

import java.util.Arrays;
import java.util.Scanner;

public class ArgumentListener {
    public void inputPrimitives(Person person) {
        Scanner scanner = new Scanner(System.in);
        String[] inputArray = scanner.nextLine().split(" ");
        try {
            person.setName(inputArray[0]);
            person.setHeight(Long.parseLong(inputArray[1]));
            person.setNationality(Country.valueOf(inputArray[2]));
        } catch (IllegalArgumentException e) {
            System.out.println("Введены некорректные данные, верный формат: name[записать буквами] height[>0, записать цифрами] nationality[выбрать из предложенных]");
            inputPrimitives(person);
        }
    }

    public Coordinates inputCoordinates() {
        System.out.println("Введите координаты:");
        Coordinates newCoordinates = new Coordinates();
        inputX(newCoordinates);
        inputY(newCoordinates);
        return newCoordinates;
    }

    private void inputX(Coordinates coordinates) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Введите координату x (целое число): ");
        try {
            coordinates.setX(Long.parseLong(scanner.nextLine()));
        } catch (NumberFormatException e) {
            System.out.println("Число имеет неверный формат");
            inputX(coordinates);
        } catch (IllegalArgumentException e) {
            System.out.println("Число не входит в допустимый диапазон");
            inputX(coordinates);
        }
    }

    private void inputY(Coordinates coordinates) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Введите Y(число с плавающей точкой): ");
        try {
            coordinates.setY(Integer.parseInt(scanner.nextLine()));
        } catch (NumberFormatException e) {
            System.out.println("Ошибка ввода");
            inputY(coordinates);
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
            inputY(coordinates);
        }
    }
    public void inputEyeColor(Person person) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Введите цвет глаз, доступные цвета: " + Arrays.toString(EyeColor.values()));
        String inputString = scanner.nextLine().toUpperCase();
        if ("".equals(inputString)) {
            person.setEyeColor(null);
        } else {
            try {
                person.setEyeColor(EyeColor.valueOf(inputString));
            } catch (IllegalArgumentException e) {
                System.out.println("Ошибка ввода, такого цвета не существует");
                inputEyeColor(person);
            }
        }
    }
}
