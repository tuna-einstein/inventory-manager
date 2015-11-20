package com.usp.inventory.backend;

import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiNamespace;
import com.google.api.server.spi.response.CollectionResponse;
import com.google.api.server.spi.response.NotFoundException;
import com.google.appengine.api.datastore.Cursor;
import com.google.appengine.api.datastore.QueryResultIterator;
import com.googlecode.objectify.ObjectifyService;
import com.googlecode.objectify.cmd.Query;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.annotation.Nullable;
import javax.inject.Named;

import static com.googlecode.objectify.ObjectifyService.ofy;

/**
 * WARNING: This generated code is intended as a sample or starting point for using a
 * Google Cloud Endpoints RESTful API with an Objectify entity. It provides no data access
 * restrictions and no data validation.
 * <p/>
 * DO NOT deploy this code unchanged as part of a real application to real users.
 */
@Api(
        name = "inventoryApi",
        version = "v1",
        resource = "inventory",
        namespace = @ApiNamespace(
                ownerDomain = "backend.inventory.usp.com",
                ownerName = "backend.inventory.usp.com",
                packagePath = ""
        )
)
public class InventoryEndpoint {

    private static final Logger logger = Logger.getLogger(InventoryEndpoint.class.getName());

    private static final int DEFAULT_LIST_LIMIT = 20;

    static {
        // Typically you would register this inside an OfyServive wrapper. See: https://code.google.com/p/objectify-appengine/wiki/BestPractices
        ObjectifyService.register(Inventory.class);
    }

    /**
     * Returns the {@link Inventory} with the corresponding ID.
     *
     * @param id the ID of the entity to be retrieved
     * @return the entity with the corresponding ID
     * @throws NotFoundException if there is no {@code Inventory} with the provided ID.
     */
    @ApiMethod(
            name = "get",
            path = "inventory/{id}",
            httpMethod = ApiMethod.HttpMethod.GET)
    public Inventory get(@Named("id") String id) throws NotFoundException {
        logger.info("Getting Inventory with ID: " + id);
        Inventory inventory = ofy().load().type(Inventory.class).id(id).now();
        if (inventory == null) {
            throw new NotFoundException("Could not find Inventory with ID: " + id);
        }
        return inventory;
    }

    /**
     * Inserts a new {@code Inventory}.
     */
    @ApiMethod(
            name = "insert",
            path = "inventory",
            httpMethod = ApiMethod.HttpMethod.POST)
    public Inventory insert(Inventory inventory) {
        // Typically in a RESTful API a POST does not have a known ID (assuming the ID is used in the resource path).
        // You should validate that inventory.id has not been set. If the ID type is not supported by the
        // Objectify ID generator, e.g. long or String, then you should generate the unique ID yourself prior to saving.
        //
        // If your client provides the ID then you should probably use PUT instead.
        ofy().save().entity(inventory).now();
        logger.info("Created Inventory with ID: " + inventory.getId());

        return ofy().load().entity(inventory).now();
    }

    /**
     * Updates an existing {@code Inventory}.
     *
     * @param id        the ID of the entity to be updated
     * @param inventory the desired state of the entity
     * @return the updated version of the entity
     * @throws NotFoundException if the {@code id} does not correspond to an existing
     *                           {@code Inventory}
     */
    @ApiMethod(
            name = "update",
            path = "inventory/{id}",
            httpMethod = ApiMethod.HttpMethod.PUT)
    public Inventory update(@Named("id") String id, Inventory inventory) throws NotFoundException {
        // TODO: You should validate your ID parameter against your resource's ID here.
        checkExists(id);
        ofy().save().entity(inventory).now();
        logger.info("Updated Inventory: " + inventory);
        return ofy().load().entity(inventory).now();
    }

    /**
     * Deletes the specified {@code Inventory}.
     *
     * @param id the ID of the entity to delete
     * @throws NotFoundException if the {@code id} does not correspond to an existing
     *                           {@code Inventory}
     */
    @ApiMethod(
            name = "remove",
            path = "inventory/{id}",
            httpMethod = ApiMethod.HttpMethod.DELETE)
    public void remove(@Named("id") String id) throws NotFoundException {
        checkExists(id);
        ofy().delete().type(Inventory.class).id(id).now();
        logger.info("Deleted Inventory with ID: " + id);
    }

    /**
     * List all entities.
     *
     * @param cursor used for pagination to determine which page to return
     * @param limit  the maximum number of entries to return
     * @return a response that encapsulates the result list and the next page token/cursor
     */
    @ApiMethod(
            name = "list",
            path = "inventory",
            httpMethod = ApiMethod.HttpMethod.GET)
    public CollectionResponse<Inventory> list(@Nullable @Named("cursor") String cursor, @Nullable @Named("limit") Integer limit) {
        limit = limit == null ? DEFAULT_LIST_LIMIT : limit;
        Query<Inventory> query = ofy().load().type(Inventory.class).limit(limit);
        if (cursor != null) {
            query = query.startAt(Cursor.fromWebSafeString(cursor));
        }
        QueryResultIterator<Inventory> queryIterator = query.iterator();
        List<Inventory> inventoryList = new ArrayList<Inventory>(limit);
        while (queryIterator.hasNext()) {
            inventoryList.add(queryIterator.next());
        }
        return CollectionResponse.<Inventory>builder().setItems(inventoryList).setNextPageToken(queryIterator.getCursor().toWebSafeString()).build();
    }

    private void checkExists(String id) throws NotFoundException {
        try {
            ofy().load().type(Inventory.class).id(id).safe();
        } catch (com.googlecode.objectify.NotFoundException e) {
            throw new NotFoundException("Could not find Inventory with ID: " + id);
        }
    }
}