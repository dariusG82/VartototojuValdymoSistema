package com.cao.dariusg.users;

import com.cao.dariusg.users.Role;

public class User {
    private final String username;
    private final String password;
    private final Role role;
    private final String name;
    private final String surname;
    private final int age;

    public User(String username, String password, Role role, String name, String surname, int age) {
        this.username = username;
        this.password = password;
        this.role = role;
        this.name = name;
        this.surname = surname;
        this.age = age;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public Role getRole() {
        return role;
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public int getAge() {
        return age;
    }
}
