package services;





import database.DataBaseProvider;

import java.util.NoSuchElementException;
import java.util.Scanner;

import static services.UserBuilder.getPasswordLogin;
import static services.UserBuilder.getUserName;


public class Authentication {
    private final CurrentUserManager currentUserManager = new CurrentUserManager();
    private final DataBaseProvider source = new DataBaseProvider(new CurrentUserManager());
    private final Scanner reader = new Scanner(System.in);

    public Authentication() {
    }

    private void configUserManager(String username) {
        currentUserManager.setUserName(username);
    }

    public CurrentUserManager start() {
        try {
            System.out.println("Добро пожаловать! Выберите что сделать: 1 - войти в аккаунт, 2 - зарегистрироваться.");
            switch (reader.nextLine()) {
                case "1" -> {
                    return login();
                }
                case "2" -> {
                    return registerUser();
                }
                case "exit" -> {
                    System.exit(0);
                    return null;
                }
                default -> {
                    return start();
                }
            }
        } catch (NoSuchElementException e){
            System.out.println("Завершение программы.");
            System.exit(1);
            return new CurrentUserManager();
        }
    }

    public CurrentUserManager registerUser() {
        String username = getUserName(reader);
        String password = getPasswordLogin(reader);
        if (source.getUserNameSet().contains(username)) {
            System.out.println("Пользователь с данным никнеймом существует. Попробуйте еще раз.");
            registerUser();
        } else {
            source.userRegister(username, password);
            System.out.println("Пользователь успешно зарегестрирован.");
            configUserManager(username);
        }
        return currentUserManager;
    }

    public CurrentUserManager login() {
        String username = getUserName(reader);
        String password = getPasswordLogin(reader);
        if (source.checkUserPassword(username, password)) {
            System.out.println("Успешный вход!");
            configUserManager(username);
        } else {
            System.out.println("Неверный логин или пароль.");
            login();
        }
        return currentUserManager;
    }


    public CurrentUserManager getUserManager() {
        return currentUserManager;
    }
}
