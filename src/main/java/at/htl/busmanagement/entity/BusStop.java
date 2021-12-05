package at.htl.busmanagement.entity;

import javax.json.bind.annotation.JsonbTransient;
import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;
import java.util.Objects;

@Entity
@XmlRootElement
public class BusStop {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "bst_id")
    private Long id;

    @Column(name = "bst_name")
    private String name;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "bst_next_bus_stop")
    private BusStop nextBusStop;

    @JsonbTransient
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "busStop")
    private List<BusSchedule> busSchedules;

    public BusStop() {
    }

    public BusStop(String name, BusStop nextBusStop) {
        this.name = name;
        this.nextBusStop = nextBusStop;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BusStop getNextBusStop() {
        return nextBusStop;
    }

    public void setNextBusStop(BusStop nextBusStop) {
        this.nextBusStop = nextBusStop;
    }

    public List<BusSchedule> getBusSchedules() {
        return busSchedules;
    }

    public void setBusSchedules(List<BusSchedule> busSchedules) {
        this.busSchedules = busSchedules;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BusStop busStop = (BusStop) o;
        return Objects.equals(name, busStop.name) &&
                Objects.equals(nextBusStop, busStop.nextBusStop);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, nextBusStop);
    }

    @Override
    public String toString() {
        return "BusStop{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", nextBusStop=" + nextBusStop +
                '}';
    }
}
