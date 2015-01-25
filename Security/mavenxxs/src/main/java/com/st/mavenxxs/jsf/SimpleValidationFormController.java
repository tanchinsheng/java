package com.st.mavenxxs.jsf;

import java.io.Serializable;
import java.util.Locale;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@ManagedBean(name = "simpleValidationFormController")
@SessionScoped
public class SimpleValidationFormController implements Serializable {

    @NotNull(message = "Please enter your username")
    @Pattern(regexp = "[a-zA-Z0-9]{1,}",
    message = "Only letters and numbers are allowed")
    private String name;
    private String password;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
