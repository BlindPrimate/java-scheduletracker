package scheduler.models;

public class Customer {

    private int id;
    private String name;
    private String address;
    private String phone;
    private int addressId;


    public Customer(String name, String address, String phone) {
        this.name = name;
        this.address = address;
        this.phone = phone;
    }


    // set equality to equal by ID
    @Override
    public boolean equals(Object obj) {
        Customer test = (Customer)obj;
        if (obj == null) return false;
        if (this.id == test.getId()) return true;
        return super.equals(obj);
    }


    // getters and setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getAddressId() {
        return addressId;
    }

    public void setAddressId(int addressId) {
        this.addressId = addressId;
    }
}
