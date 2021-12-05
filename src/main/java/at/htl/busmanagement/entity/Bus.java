package at.htl.busmanagement.entity;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

@Entity
@XmlRootElement
public class Bus {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "b_id")
    private Long id;

    @Column(name = "b_title")
    private String title;

    @JoinColumn(name = "b_driver")
    @OneToOne(cascade = CascadeType.ALL)
    private Driver driver;

    public Bus() {
    }

    public Bus(String title, Driver driver) {
        this.title = title;
        this.driver = driver;
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String busTitle) {
        this.title = busTitle;
    }

    public Driver getDriver() {
        return driver;
    }

    public void setDriver(Driver driver) {
        this.driver = driver;
    }

    public JsonObject toJson(){
        JsonObjectBuilder builder = Json.createObjectBuilder();

        builder.add("title", this.getTitle());
        builder.add("driver", this.getDriver().toJson());

        return builder.build();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Bus bus = (Bus) o;
        return Objects.equals(title, bus.title) &&
                Objects.equals(driver, bus.driver);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, driver);
    }

    @Override
    public String toString() {
        return "Bus{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", driver=" + driver +
                '}';
    }
}
