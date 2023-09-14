package jm.task.core.jdbc;

import jm.task.core.jdbc.service.UserService;
import jm.task.core.jdbc.service.UserServiceImpl;


public class Main {
    public static void main(String[] args) {

        UserService userService = new UserServiceImpl();

        userService.createUsersTable();

        userService.saveUser("Макс", "Борис", (byte) 20);
        userService.saveUser("Наталья", "Иванова", (byte) 25);
        userService.saveUser("Артем", "Петров", (byte) 31);
        userService.saveUser("Екатерина", "Большова", (byte) 38);


        userService.removeUserById(1);
        userService.getAllUsers();
        userService.cleanUsersTable();
        userService.dropUsersTable();
    }
}
