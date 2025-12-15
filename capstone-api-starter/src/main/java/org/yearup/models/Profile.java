package org.yearup.models;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;

public class Profile
{
    @Positive(message = "userId must be greater than 0")
    private int userId;

    @NotBlank(message = "First name is required")
    private String firstName = "";

    @NotBlank(message = "Last name is required")
    private String lastName = "";

    @NotBlank(message = "phone is required")
    private String phone = "";

    @Email(message = "Email should be valid")
    @NotBlank(message = "Email is required")
    private String email = "";

    @NotBlank(message = "Address is required")
    private String address = "";

    @NotBlank(message = "City is required")
    private String city = "";

    @NotBlank(message = "State is required")
    private String state = "";

    @NotBlank(message = "Zip code is invalid")
    private String zip = "";

    public Profile()
    {
    }

    public Profile(int userId, String firstName, String lastName, String phone, String email, String address, String city, String state, String zip)
    {
        this.userId = userId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phone = phone;
        this.email = email;
        this.address = address;
        this.city = city;
        this.state = state;
        this.zip = zip;
    }

    public int getUserId()
    {
        return userId;
    }

    public void setUserId(int userId)
    {
        this.userId = userId;
    }

    public String getFirstName()
    {
        return firstName;
    }

    public void setFirstName(String firstName)
    {
        this.firstName = firstName;
    }

    public String getLastName()
    {
        return lastName;
    }

    public void setLastName(String lastName)
    {
        this.lastName = lastName;
    }

    public String getPhone()
    {
        return phone;
    }

    public void setPhone(String phone)
    {
        this.phone = phone;
    }

    public String getEmail()
    {
        return email;
    }

    public void setEmail(String email)
    {
        this.email = email;
    }

    public String getAddress()
    {
        return address;
    }

    public void setAddress(String address)
    {
        this.address = address;
    }

    public String getCity()
    {
        return city;
    }

    public void setCity(String city)
    {
        this.city = city;
    }

    public String getState()
    {
        return state;
    }

    public void setState(String state)
    {
        this.state = state;
    }

    public String getZip()
    {
        return zip;
    }

    public void setZip(String zip)
    {
        this.zip = zip;
    }
}
