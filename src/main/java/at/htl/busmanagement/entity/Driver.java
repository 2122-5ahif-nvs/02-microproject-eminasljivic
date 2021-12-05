package at.htl.busmanagement.entity;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.json.bind.JsonbBuilder;
import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;
import java.time.LocalDate;
import java.util.Objects;

@Entity
@XmlRootElement
public class Driver {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "d_id")
    private Long id;

    @Column(name = "d_first_name")
    private String firstName;

    @Column(name = "d_last_name")
    private String lastName;

    @Column(name = "d_salary")
    private double salary;

    @Column(name = "d_svnr")
    private String svnr;

    public Driver() {
    }

    public Driver(String firstName, String lastName, double salary, String svnr) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.salary = salary;
        this.svnr = svnr;
    }

    public Long getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public double getSalary() {
        return salary;
    }

    public void setSalary(double salary) {
        this.salary = salary;
    }

    public String getSvnr() {
        return svnr;
    }

    public void setSvnr(String svnr) {
        this.svnr = svnr;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Driver driver = (Driver) o;
        return Double.compare(driver.salary, salary) == 0 &&
                Objects.equals(firstName, driver.firstName) &&
                Objects.equals(lastName, driver.lastName) &&
                Objects.equals(svnr, driver.svnr);
    }

    public JsonObject toJson(){
        JsonObjectBuilder builder = Json.createObjectBuilder();
        builder.add("firstName", this.getFirstName());
        builder.add("lastName", this.getLastName());
        builder.add("svnr", this.getSvnr());
        builder.add("salary", this.getSalary());

        return builder.build();
    }

    @Override
    public int hashCode() {
        return Objects.hash(firstName, lastName, salary, svnr);
    }

    @Override
    public String toString() {
        return "Driver{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", salary=" + salary +
                ", svnr='" + svnr + '\'' +
                '}';
    }
}
