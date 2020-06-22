package model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Profile {

    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("faculty")
    @Expose
    private String faculty;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("avatar")
    @Expose
    private String avatar;

    public Profile(String name, String faculty, String email) {
        this.name = name;
        this.faculty = faculty;
        this.email = email;
    }

    public Profile(String name, String faculty, String email, String avatar) {
        this.name = name;
        this.faculty = faculty;
        this.email = email;
        this.avatar = avatar;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFaculty() {
        return faculty;
    }

    public void setFaculty(String faculty) {
        this.faculty = faculty;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }
}
