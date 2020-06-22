package model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Event implements Serializable {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("account_id")
    @Expose
    private String accountId;
    @SerializedName("category_id")
    @Expose
    private String categoryId;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("code")
    @Expose
    private String code;
    @SerializedName("place")
    @Expose
    private String place;
    @SerializedName("avatar")
    @Expose
    private String avatar;
    @SerializedName("ticket_number")
    @Expose
    private String ticketNumber;
    @SerializedName("start_date")
    @Expose
    private String startDate;
    @SerializedName("end_date")
    @Expose
    private String endDate;
    @SerializedName("expired_date")
    @Expose
    private String expiredDate;
    @SerializedName("short_desc")
    @Expose
    private String shortDesc;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("faculty")
    @Expose
    private String faculty;

    public Event() {
    }

    public Event(String id, String accountId, String categoryId, String title, String code, String place, String avatar, String ticketNumber, String startDate, String endDate, String expiredDate, String shortDesc, String email, String status, String faculty) {
        this.id = id;
        this.accountId = accountId;
        this.categoryId = categoryId;
        this.title = title;
        this.code = code;
        this.place = place;
        this.avatar = avatar;
        this.ticketNumber = ticketNumber;
        this.startDate = startDate;
        this.endDate = endDate;
        this.expiredDate = expiredDate;
        this.shortDesc = shortDesc;
        this.email = email;
        this.status = status;
        this.faculty = faculty;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getTicketNumber() {
        return ticketNumber;
    }

    public void setTicketNumber(String ticketNumber) {
        this.ticketNumber = ticketNumber;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getExpiredDate() {
        return expiredDate;
    }

    public void setExpiredDate(String expiredDate) {
        this.expiredDate = expiredDate;
    }

    public String getShortDesc() {
        return shortDesc;
    }

    public void setShortDesc(String shortDesc) {
        this.shortDesc = shortDesc;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getFaculty() {
        return faculty;
    }

    public void setFaculty(String faculty) {
        this.faculty = faculty;
    }
}