package uk.me.mattgill.samples.jpa.sample.service;

import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import uk.me.mattgill.samples.jpa.sample.persistence.entity.Item;
import uk.me.mattgill.samples.jpa.sample.persistence.entity.ItemManager;

@Path("/item")
@RequestScoped
public class ItemResource {

    @Inject
    private ItemManager manager;
    
    @GET
    @Path("/all")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Item> getItems() {
        return manager.getAllItems();
    }
    
    @GET
    @Path("/clear")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Item> clearItems() {
        return manager.clearItems();
    }
    
    @POST
    @Path("/add")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Item insertItem(Item item) {
        return manager.insertItem(item);
    }
    
}
