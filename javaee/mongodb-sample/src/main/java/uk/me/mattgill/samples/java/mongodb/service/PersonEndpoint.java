package uk.me.mattgill.samples.java.mongodb.service;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.annotation.Resource;
import javax.enterprise.context.RequestScoped;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.HeuristicMixedException;
import javax.transaction.HeuristicRollbackException;
import javax.transaction.NotSupportedException;
import javax.transaction.RollbackException;
import javax.transaction.SystemException;
import javax.transaction.UserTransaction;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import uk.me.mattgill.samples.java.mongodb.entity.Person;

@Path("/person")
@RequestScoped
public class PersonEndpoint {

    @Resource
    private UserTransaction utx;

    @PersistenceContext(unitName = "mongoPU")
    private EntityManager mongoPU;

    @Path("/add")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Person addPerson(Person person) {
        startTransaction();
        mongoPU.persist(person);
        endTransaction();
        return person;
    }
    
    @Path("/all")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Person> listPeople() {
        List<Person> people = mongoPU.createQuery("SELECT p from Person p", Person.class).getResultList();
        return people;
    }
    
    @Path("/clear")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Person> clearPeople() {
        List<Person> people = listPeople();
        startTransaction();
        for(Person p: people) {
            mongoPU.remove(mongoPU.merge(p));
        } 
        endTransaction();
        return people;
    }
    
    public void startTransaction() {
        try {
            utx.begin();
        } catch (NotSupportedException ex) {
            Logger.getLogger(PersonEndpoint.class.getName()).log(Level.SEVERE, "Nested transactions not supported.", ex);
        } catch (SystemException ex) {
            Logger.getLogger(PersonEndpoint.class.getName()).log(Level.SEVERE, "Unexpected error in starting transaction.", ex);
        }
    }
    
    public void endTransaction() {
        try {
            utx.commit();
        } catch (RollbackException ex) {
            Logger.getLogger(PersonEndpoint.class.getName()).log(Level.SEVERE, "Transaction was rolled back.", ex);
        } catch (HeuristicMixedException | HeuristicRollbackException ex) {
            Logger.getLogger(PersonEndpoint.class.getName()).log(Level.SEVERE, "Heuristic transaction committed.", ex);
        } catch (SecurityException ex) {
            Logger.getLogger(PersonEndpoint.class.getName()).log(Level.SEVERE, "Thread is not allowed to commit transaction.", ex);
        } catch (IllegalStateException ex) {
            Logger.getLogger(PersonEndpoint.class.getName()).log(Level.SEVERE, "Thread is not associated with a transaction.", ex);
        } catch (SystemException ex) {
            Logger.getLogger(PersonEndpoint.class.getName()).log(Level.SEVERE, "Unexpected error in committing transaction.", ex);
        }
    }
}
