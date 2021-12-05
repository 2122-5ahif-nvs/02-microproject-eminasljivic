package at.htl.busmanagement.entity;

import at.htl.busmanagement.control.LocalDateTimeAdapter;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

@Entity
@XmlRootElement
public class BusSchedule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "bsch_id")
    private Long id;

    @Column(name = "bsch_arrival_time")
    @XmlJavaTypeAdapter(value = LocalDateTimeAdapter.class)
    private LocalDateTime arrivalTime;

    @Column(name = "bsch_leaving_time")
    @XmlJavaTypeAdapter(value = LocalDateTimeAdapter.class)
    private LocalDateTime leavingTime;

    @JoinColumn(name = "bsch_bus_stop")
    @ManyToOne(cascade = CascadeType.ALL)
    private BusStop busStop;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = "bsch_b",
            joinColumns = @JoinColumn(name = "bsch_bus"),
            inverseJoinColumns = @JoinColumn(name = "b_id"))
    private List<Bus> buses;

    @Column(name = "bsch_ride_type")
    @Enumerated(EnumType.STRING)
    private RideType rideType;

    public BusSchedule() {
        buses = new LinkedList<>();
    }

    public BusSchedule(LocalDateTime arrivalTime, LocalDateTime leavingTime, BusStop busStop, RideType rideType) {
        this();
        this.arrivalTime = arrivalTime;
        this.leavingTime = leavingTime;
        this.busStop = busStop;
        this.rideType = rideType;
    }

    public Long getId() {
        return id;
    }

    public LocalDateTime getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(LocalDateTime arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public LocalDateTime getLeavingTime() {
        return leavingTime;
    }

    public void setLeavingTime(LocalDateTime leavingTime) {
        this.leavingTime = leavingTime;
    }

    public BusStop getBusStop() {
        return busStop;
    }

    public void setBusStop(BusStop busStop) {
        this.busStop = busStop;
    }

    public List<Bus> getBuses() {
        return buses;
    }

    public void setBuses(List<Bus> buses) {
        this.buses = buses;
    }

    public RideType getRideType() {
        return rideType;
    }

    public void setRideType(RideType rideType) {
        this.rideType = rideType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BusSchedule that = (BusSchedule) o;
        return Objects.equals(arrivalTime, that.arrivalTime) &&
                Objects.equals(leavingTime, that.leavingTime) &&
                Objects.equals(busStop, that.busStop) &&
                rideType == that.rideType;
    }

    @Override
    public int hashCode() {
        return Objects.hash(arrivalTime, leavingTime, busStop, rideType);
    }

    @Override
    public String toString() {
        LocalDateTimeAdapter localDateTimeAdapter = new LocalDateTimeAdapter();
        return "BusSchedule{" +
                "id=" + id +
                ", arrivalTime=" + localDateTimeAdapter.marshal(arrivalTime) +
                ", leavingTime=" + localDateTimeAdapter.marshal(leavingTime) +
                ", busStop=" + busStop +
                ", rideType=" + rideType +
                '}';
    }
}
