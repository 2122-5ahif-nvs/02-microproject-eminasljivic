package at.htl.busmanagement.repository;

import at.htl.busmanagement.entity.Bus;
import io.quarkus.hibernate.orm.panache.PanacheRepository;

import javax.enterprise.context.ApplicationScoped;
import javax.persistence.NoResultException;
import javax.transaction.Transactional;
import java.util.List;

@ApplicationScoped
public class BusRepository implements PanacheRepository<Bus> {

    public List<Bus> getAll() {
        return listAll();
    }

    public Bus find(Long id) {
        Bus retrievedBus = findById(id);
        if (retrievedBus == null) {
            throw new IllegalArgumentException(String.format("No bus with the id %d exists.", id));
        }
        return retrievedBus;
    }

    public Bus alreadyExists(Bus bus) {
        try {
            return find("title", bus.getTitle()).singleResult();
        } catch (NoResultException exception) {
            return null;
        }
    }

    @Transactional
    public void insert(Bus bus) {
        if (alreadyExists(bus) != null) {
            throw new IllegalArgumentException(String.format("A bus with the title %s already exists!", bus.getTitle()));
        }
        persist(bus);
    }

    @Transactional
    public void remove(Long id) {
        find(id); //So it throws an exception when no bus with the id exists
        deleteById(id);
    }

    public Object driverById(Long id){
        return getEntityManager()
                .createQuery("SELECT d, b.title FROM Bus b Join Driver d on b.driver.id = d.id where d.id = :driverId")
                .setParameter("driverId", id)
                .getResultList();
    }

    @Transactional
    public void update(Long id, Bus bus) {
        Bus toBeUpdatedBus = find(id);

        toBeUpdatedBus.setDriver(bus.getDriver());
        toBeUpdatedBus.setTitle(bus.getTitle());
    }
}
