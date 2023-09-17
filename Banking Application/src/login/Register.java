package login;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import database.Data;

public class Register {
    //declare fields
    private String email, password;

    //overiding constructor
    public Register() {
        this.email = "";
        this.password = "";
    }

    //overloading constructor
    public Register(String email, String password) {
        this.email = email;
        this.password = password;
    }

    //create new user
    public void createUser(String email, String firstName, String lastName, String password) {
        Data data = new Data();
        data.createUser(email, formatName(firstName), formatName(lastName), hash(password), new Login().currentDate());
    }

    //returns boolean for if password is strong enough
    public boolean invalidPassword(String password) {
        boolean hasLower = false, hasUpper = false, hasDigit = false, hasSymbol = false;

        for (char c : password.toCharArray()) {
            if (Character.isLowerCase(c))
                hasLower = true;
            else if (Character.isUpperCase(c))
                hasUpper = true;
            else if (Character.isDigit(c))
                hasDigit = true;
            else
                hasSymbol = true;
        }

        return !(password.length() >= 8 && hasLower && hasUpper && hasDigit && hasSymbol);

    }

    //prints all requirements left for password
    public void passwordRequirements(String password) {
        boolean hasLower = false, hasUpper = false, hasDigit = false, hasSymbol = false;

        for (char c : password.toCharArray()) {
            if (Character.isLowerCase(c))
                hasLower = true;
            else if (Character.isUpperCase(c))
                hasUpper = true;
            else if (Character.isDigit(c))
                hasDigit = true;
            else
                hasSymbol = true;
        }

        if (password.length() < 8)
            System.out.println("must be at least 8 characters");
        if (!hasLower)
            System.out.println("must include lower case");
        if (!hasUpper)
            System.out.println("must include upper case");
        if (!hasDigit)
            System.out.println("must include a number");
        if (!hasSymbol)
            System.out.println("must include a symbol");
        System.out.println();
    }

    //returns boolean for if name is just alphabetic unless other than names with a dash (John-Doe)
    public boolean invalidName(String name) {
        boolean invalid = false;
        for (char i : name.toCharArray()) {
            if (!(Character.isAlphabetic(i) || i == '-'))
                invalid = true;
        }

        return invalid;
    }

    //Format name joHn dOe --> John Doe or john-doe --> John-Doe
    public String formatName(String name) {
        String[] splitName = name.split("-");
        for (int i = 0; i < splitName.length; i++) {
            splitName[i] = splitName[i].substring(0, 1).toUpperCase() + splitName[i].substring(1).toLowerCase();
        }
        
        return String.join("-", splitName);
    }

    //generates SHA-265 hash
    public String hash(String password) {
        //declare variables and objects
        MessageDigest md;
        StringBuilder sb;
        byte[] hashBytes;
        String hash;

        try {
            //create MessageDigest object with SHA-256 algorithm
            md = MessageDigest.getInstance("SHA-256");
            //makes the hash of the byte array for password
            hashBytes = md.digest(password.getBytes());
            //create StringBuilder object to construct a string
            sb = new StringBuilder();

            //format each byte
            for (byte b : hashBytes)
                sb.append(String.format("%02x", b));
            //makes hash
            hash = sb.toString();

        } catch (NoSuchAlgorithmException e) {
            hash = "";
        }

        //return hashed value
        return hash;
    }

}
