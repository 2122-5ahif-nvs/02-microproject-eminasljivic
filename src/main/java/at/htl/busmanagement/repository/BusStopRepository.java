package at.htl.busmanagement.repository;

import at.htl.busmanagement.entity.BusStop;
import io.quarkus.hibernate.orm.panache.PanacheRepository;

import javax.enterprise.context.ApplicationScoped;
import javax.persistence.NoResultException;
import javax.transaction.Transactional;
import java.util.List;

@ApplicationScoped
public class BusStopRepository implements PanacheRepository<BusStop> {

    public List<BusStop> getAll(){
        return listAll();
    }

    public BusStop alreadyExists(BusStop busStop){
        try {
            return find("name", busStop.getName()).singleResult();
        } catch (NoResultException exception) {
            return null;
        }
    }

    public BusStop find(Long id){
        BusStop retrievedBusStop = findById(id);
        if (retrievedBusStop == null) {
            throw new IllegalArgumentException(String.format("No bus stop with the id %d exists.", id));
        }
        return retrievedBusStop;
    }

    @Transactional
    public void insert(BusStop busStop){
        getAll().forEach(System.out::println);
        if(alreadyExists(busStop) != null){
            throw new IllegalArgumentException(String.format("A bus stop with the name %s already exists!", busStop.getName()));
        }
        persist(busStop);
    }

    @Transactional
    public void remove(Long id){
        find(id); //So it throws an exception when no bus stop with the id exists
        deleteById(id);
    }

    @Transactional
    public void update(BusStop busStop){
        BusStop toBeUpdatedBusStop = find(busStop.getId());

        toBeUpdatedBusStop.setNextBusStop(busStop.getNextBusStop());
        toBeUpdatedBusStop.setName(busStop.getName());
    }
}
