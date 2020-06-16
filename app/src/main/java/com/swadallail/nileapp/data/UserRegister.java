package com.swadallail.nileapp.data;

public class UserRegister {
    private String Email ;
    private String Password ;
    private String ConfirmPassword ;

    public UserRegister(String email, String password, String confirmPassword) {
        this.Email = email;
        this.Password = password;
        this.ConfirmPassword = confirmPassword;

    }
}
