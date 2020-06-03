package scheduler.models;

import java.net.URL;
import java.sql.Timestamp;
import java.time.LocalDateTime;

public class Appointment {
    private int id;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String customerName;
    private int customerId;
    private int createdById;
    private String createdBy;
    private String appointmentType;
    private String title;
    private String description;
    private String location;
    private String contact;
    private URL url;
    private Customer customer;


    public Appointment() {

    }

    public Appointment(LocalDateTime startTime, LocalDateTime endTime, int customerId, int createdById, String appointmentType, String title, Customer customer) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.customerId = customerId;
        this.createdById = createdById;
        this.appointmentType = appointmentType;
        this.title = title;
        this.customer = customer;
    }



    // getters and setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public int getCreatedById() {
        return createdById;
    }

    public void setCreatedById(int createdById) {
        this.createdById = createdById;
    }
    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public Timestamp getStartTimeStamp() {
        return Timestamp.valueOf(startTime);
    }


    public LocalDateTime getEndTime() {
        return endTime;
    }

    public Timestamp getEndTimeStamp() {
        return Timestamp.valueOf(endTime);
    }


    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customer) {
        this.customerId = customer;
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

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }
}



