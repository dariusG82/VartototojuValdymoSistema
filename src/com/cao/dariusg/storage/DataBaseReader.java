package com.cao.dariusg.storage;

import com.cao.dariusg.users.Role;
import com.cao.dariusg.users.User;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static com.cao.dariusg.users.Role.*;

public class DataBaseReader {

    public final String path;

    public DataBaseReader(String path){
        this.path = path;
    }

    public User getUser(String username, String password){
        List<User> users = getAllUsersFromFile();
        for (User user : users){
            if(user.getUsername().equals(username) && user.getPassword().equals(password)){
                return user;
            }
        }
        return null;
    }

    public User getUserByUserName(String username){
        List<User> users = getAllUsersFromFile();
        for (User user : users){
            if(user.getUsername().equals(username)){
                return user;
            }
        }
        return null;
    }

    public boolean addNewUser(User user){
        List<User> usersList = getAllUsersFromFile();

        // First user automatically becomes ADMIN user
        if(usersList.isEmpty()){
            User newUser = new User(user.getUsername(), user.getPassword(), ADMIN, user.getName(), user.getSurname(), user.getAge());
            usersList.add(newUser);
        } else {
            for(User newUser : usersList){
                if(user.getUsername().equals(newUser.getUsername())){
                    return false;
                }
            }
            usersList.add(user);
        }

        saveNewUsersList(usersList);
        return true;
    }

    public int removeUser(String username){
        User newUser = getUserByUserName(username);
        if(newUser == null){
            return 0;
        } else {
            List<User> users = getAllUsersFromFile();
            if(users.size() == 1){
                return 1;
            }
            users.removeIf(user -> user.getUsername().equals(username));
            saveNewUsersList(users);
            return 2;
        }
    }

    public List<User> getAllUsersFromFile() {
        try {
            Scanner scanner = new Scanner(new File(path));
            ArrayList<User> users = new ArrayList<>();

            while (scanner.hasNextLine()){
                String username = scanner.nextLine();
                String password = scanner.nextLine();
                String roleString = scanner.nextLine();
                Role role;
                if(roleString.equals("ADMIN")){
                    role = ADMIN;
                } else {
                    role = SIMPLE;
                }
                String name = scanner.nextLine();
                String surname = scanner.nextLine();
                String ageString = scanner.nextLine();
                int age;
                try {
                    age = Integer.parseInt(ageString);
                } catch (NumberFormatException e){
                    age = 0;
                }
                scanner.nextLine();

                if(age > 0){
                    users.add(new User(username, password, role, name, surname, age));
                }
            }
            return users;
        } catch (FileNotFoundException e){
            return null;
        }
    }

    public void saveNewUsersList(List<User> usersList){
        try {
            PrintWriter printWriter = new PrintWriter(new FileWriter(path));
            for(User user : usersList){
                printWriter.println(user.getUsername());
                printWriter.println(user.getPassword());
                printWriter.println(user.getRole().toString());
                printWriter.println(user.getName());
                printWriter.println(user.getSurname());
                printWriter.println(user.getAge());
                printWriter.println();
            }
            printWriter.close();
        } catch (IOException e){
            System.out.println("File not found");
        }
    }
}
