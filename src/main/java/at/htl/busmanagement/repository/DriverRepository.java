package at.htl.busmanagement.repository;

import at.htl.busmanagement.entity.Driver;
import io.quarkus.hibernate.orm.panache.PanacheRepository;

import javax.enterprise.context.ApplicationScoped;
import javax.persistence.NoResultException;
import javax.transaction.Transactional;
import java.util.List;

@ApplicationScoped
public class DriverRepository implements PanacheRepository<Driver> {

    public List<Driver> getAll(){
        return listAll();
    }

    public Driver alreadyExists(Driver driver){
        try {
            return find("svnr", driver.getSvnr()).singleResult();
        } catch (NoResultException exception) {
            return null;
        }
    }

    public Driver find(Long id){
        Driver retrievedDriver = findById(id);
        if (retrievedDriver == null) {
            throw new IllegalArgumentException(String.format("No driver with the id %d exists.", id));
        }
        return retrievedDriver;
    }

    @Transactional
    public void insert(Driver driver){
        if(alreadyExists(driver) != null){
            throw new IllegalArgumentException(String.format("A driver with the SVNR %s already exists!", driver.getSvnr()));
        }
        persist(driver);
    }

    @Transactional
    public void remove(Long id){
        find(id);
        deleteById(id);
    }

    @Transactional
    public void update(Driver driver){
        Driver toBeUpdatedDriver = findById(driver.getId());

        toBeUpdatedDriver.setFirstName(driver.getFirstName());
        toBeUpdatedDriver.setLastName(driver.getLastName());
        toBeUpdatedDriver.setSalary(driver.getSalary());
        toBeUpdatedDriver.setSvnr(driver.getSvnr());
    }
}

