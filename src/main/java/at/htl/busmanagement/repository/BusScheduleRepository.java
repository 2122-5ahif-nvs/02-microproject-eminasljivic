package at.htl.busmanagement.repository;

import at.htl.busmanagement.entity.BusSchedule;
import io.quarkus.hibernate.orm.panache.PanacheRepository;

import javax.enterprise.context.ApplicationScoped;
import javax.transaction.Transactional;
import java.util.List;

@ApplicationScoped
public class BusScheduleRepository implements PanacheRepository<BusSchedule> {

    public List<BusSchedule> getAll() {
        return listAll();
    }

    public BusSchedule alreadyExists(BusSchedule busSchedule) {
        return getAll().stream().filter(b -> b.equals(busSchedule)).findFirst().orElse(null);
    }

    public BusSchedule find(Long id) {
        BusSchedule retrievedBusSchedule = findById(id);
        if (retrievedBusSchedule == null) {
            throw new IllegalArgumentException(String.format("No bus schedule with the id %d exists.", id));
        }
        return retrievedBusSchedule;
    }

    @Transactional
    public void insert(BusSchedule busSchedule) {
        if(alreadyExists(busSchedule) != null){
            throw new IllegalArgumentException("A bus schedule with the same data already exists!");
        }
        persist(busSchedule);
    }

    @Transactional
    public void remove(Long id) {
        find(id); //So it throws an exception when no bus schedule with the id exists
        deleteById(id);
    }

    @Transactional
    public void update(BusSchedule busSchedule){
        BusSchedule toBeUpdatedBusSchedule = find(busSchedule.getId());

        toBeUpdatedBusSchedule.setBusStop(busSchedule.getBusStop());
        toBeUpdatedBusSchedule.setArrivalTime(busSchedule.getArrivalTime());
        toBeUpdatedBusSchedule.setLeavingTime(busSchedule.getLeavingTime());
        toBeUpdatedBusSchedule.setRideType(busSchedule.getRideType());
    }
}
