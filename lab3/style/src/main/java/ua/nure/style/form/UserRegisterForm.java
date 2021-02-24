package ua.nure.style.form;

import lombok.Data;

@Data
public class UserRegisterForm {
    private String fName;
    private String lName;
    private String email;
    private String passwordHash;
}