package com.cao.dariusg;

import com.cao.dariusg.storage.DataBaseReader;
import com.cao.dariusg.users.Role;
import com.cao.dariusg.users.User;

import java.util.List;
import java.util.Scanner;

import static com.cao.dariusg.users.Role.*;

public class Main {

    private static final String path = "src/com/cao/dariusg/storage/users.txt";
    private static final DataBaseReader DATA_BASE_READER = new DataBaseReader(path);

    public static void main(String[] args) {
        printMenu();
    }

    private static void printMenu() {
        Scanner scanner = new Scanner(System.in);
        String input;
        int option;

        System.out.println("Welcome to the system!");
        while (true) {
            System.out.println("Choose option:");
            System.out.println("[1] - Login");
            System.out.println("[2] - Register");
            System.out.println("[3] - EXIT");
            input = scanner.nextLine();
            try {
                option = Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("Wrong input, try again");
                continue;
            }
            switch (option) {
                case 1 -> {
                    User user = getUserByLoginAndPassword(scanner);
                    if (user != null) {
                        switch (user.getRole()) {
                            case ADMIN -> runAdminUserMenu(user, scanner);
                            case SIMPLE -> runSimpleUserMenu(user, scanner);
                        }
                    }
                }
                case 2 -> registerNewAccount(scanner, SIMPLE);
                case 3 -> {
                    return;
                }
                default -> System.out.println("Wrong option, choose again");
            }
        }
    }

    private static User getUserByLoginAndPassword(Scanner scanner) {
        System.out.print("Enter your username: ");
        String username = scanner.nextLine();
        System.out.print("Enter your password: ");
        String password = scanner.nextLine();

        User user = DATA_BASE_READER.getUser(username, password);
        if (user != null) {
            return user;
        } else {
            System.out.println("Wrong username or password");
            return null;
        }
    }

    private static void registerNewAccount(Scanner scanner, Role newRole) {
        System.out.print("Enter your username: ");
        String username = scanner.nextLine();
        System.out.print("Enter your password: ");
        String password = scanner.nextLine();
        System.out.print("Enter your name: ");
        String name = scanner.nextLine();
        System.out.print("Enter your surname: ");
        String surname = scanner.nextLine();
        int age = 0;
        do {
            System.out.print("Enter your age: ");
            String input = scanner.nextLine();
            try {
                age = Integer.parseInt(input);
            } catch (NumberFormatException e) {
                age = -1;
                System.out.println("Wrong input, try again");
            }
            if(age == 0){
                System.out.println("Age cannot be 0, try again");
            }
        } while (age <= 0);

        if (DATA_BASE_READER.addNewUser(new User(username, password, newRole, name, surname, age))) {
            System.out.println("Registration successful \n");
        } else {
            System.out.println("Registration failed, username is already in database, try again \n");
        }
    }

    private static void runSimpleUserMenu(User user, Scanner scanner) {
        while (true) {
            System.out.println("Choose your action: ");
            System.out.println("[1] - Your information");
            System.out.println("[2] - Logout");
            int option;
            String input = scanner.nextLine();
            try {
                option = Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("Wrong input, try again");
                continue;
            }
            switch (option) {
                case 1 -> printUserInformation(user, SIMPLE);
                case 2 -> {
                    return;
                }
                default -> System.out.println("Wrong option, choose again");
            }
        }
    }

    private static void runAdminUserMenu(User user, Scanner scanner) {
        while (true) {
            System.out.println("Choose your action: ");
            System.out.println("[1] - Your information");
            System.out.println("[2] - All users information");
            System.out.println("[3] - Add new user");
            System.out.println("[4] - Remove user");
            System.out.println("[5] - Logout");
            int option;
            String input = scanner.nextLine();
            try {
                option = Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("Wrong input, try again");
                continue;
            }
            switch (option) {
                case 1 -> printUserInformation(user, ADMIN);
                case 2 -> printAllUserInformation();
                case 3 -> {
                    System.out.println("Choose new Account role: ");

                    System.out.println("[1] - ADMIN user");
                    System.out.println("[2] - SIMPLE user");
                    String userString = scanner.nextLine();
                    int newChoice;
                    try {
                        newChoice = Integer.parseInt(userString);
                        switch (newChoice) {
                            case 1 -> registerNewAccount(scanner, ADMIN);
                            case 2 -> registerNewAccount(scanner, SIMPLE);
                            default -> System.out.println("Wrong choice");
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("Wrong input, try again");
                    }
                }
                case 4 -> removeUserByUserName(scanner);
                case 5 -> {
                    return;
                }
                default -> System.out.println("Wrong option, choose again");
            }
        }
    }

    private static void removeUserByUserName(Scanner scanner){
        System.out.print("Enter username of user to remove: ");
        String username = scanner.nextLine();
        int removalResult = DATA_BASE_READER.removeUser(username);
        switch (removalResult){
            case 0 -> System.out.printf("User: %s does not exist\n\n", username);
            case 1 -> System.out.println("Cannot remove last user!\n");
            case 2 -> System.out.printf("User: %s successfully removed\n\n", username);
            default -> System.out.printf("Something is wrong - there cannot be this result: %d\n\n", removalResult);
        }
    }

    private static void printAllUserInformation() {
        List<User> users = DATA_BASE_READER.getAllUsersFromFile();
        for (User user : users) {
            printUserInformation(user, ADMIN);
        }
    }

    private static void printUserInformation(User user, Role role) {
        if (role == SIMPLE) {
            System.out.printf("Username: %s\n Name: %s \nSurname: %s\nAge: %d\n",
                    user.getUsername(), user.getName(), user.getSurname(), user.getAge());
        } else {
            System.out.printf("Role: %s \nUsername: %s \nName: %s \nSurname: %s \nAge: %d \n",
                    user.getRole(), user.getUsername(), user.getName(), user.getSurname(), user.getAge());
        }
        System.out.println("*******************************");
    }
}
