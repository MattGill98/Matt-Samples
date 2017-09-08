package uk.me.mattgill.samples.jpa.sample.persistence.entity;

import java.util.ArrayList;
import java.util.List;
import javax.enterprise.context.RequestScoped;
import javax.persistence.EntityExistsException;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

@RequestScoped
public class ItemManager {

    @PersistenceContext(unitName = "itemPU")
    private EntityManager em;
    
    @Transactional
    public List<Item> getAllItems() {
        return (List<Item>) em.createNamedQuery("item.getAll").getResultList();
    }
    
    @Transactional
    public List<Item> clearItems() {
        em.createNamedQuery("item.clearAll").executeUpdate();
        return new ArrayList<>();
    }
    
    @Transactional
    public Item insertItem(Item item) {
        Item existingItem = em.find(Item.class, item.getName());
        if (existingItem == null) {
            em.persist(item);
            return item;
        } else {
            existingItem.setCount(existingItem.getCount() + item.getCount());
            em.merge(existingItem);
            return existingItem;
        }
    }
    
}
