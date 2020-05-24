package scheduler.models;

import java.net.URL;
import java.util.Date;

public class Appointment {
    private Date startTime;
    private Date endTime;
    private String customer;
    private String createdBy;
    private String appointmentType;
    private String title;
    private String description;
    private String location;
    private String contact;
    private URL url;

    public Appointment(Date startTime, Date endTime, String customer, String createdBy, String appointmentType) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.customer = customer;
        this.createdBy = createdBy;
        this.appointmentType = appointmentType;
    }


    // getters and setters
    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public String getCustomer() {
        return customer;
    }

    public void setCustomer(String customer) {
        this.customer = customer;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getAppointmentType() {
        return appointmentType;
    }

    public void setAppointmentType(String appointmentType) {
        this.appointmentType = appointmentType;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public URL getUrl() {
        return url;
    }

    public void setUrl(URL url) {
        this.url = url;
    }
}



