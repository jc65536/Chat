package util;

import java.io.Serializable;

public class User implements Serializable {
    private String username;
    private char[] password;

    public User(String u, char[] p) {
        username = u;
        password = p;
    }

    public String getUsername() {
        return username;
    }

    public char[] getPassword() {
        return password;
    }
}
