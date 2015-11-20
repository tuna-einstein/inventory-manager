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
        name = "itemApi",
        version = "v1",
        resource = "item",
        namespace = @ApiNamespace(
                ownerDomain = "backend.inventory.usp.com",
                ownerName = "backend.inventory.usp.com",
                packagePath = ""
        )
)
public class ItemEndpoint {

    private static final Logger logger = Logger.getLogger(ItemEndpoint.class.getName());

    private static final int DEFAULT_LIST_LIMIT = 20;

    static {
        // Typically you would register this inside an OfyServive wrapper. See: https://code.google.com/p/objectify-appengine/wiki/BestPractices
        ObjectifyService.register(Item.class);
    }

    /**
     * Returns the {@link Item} with the corresponding ID.
     *
     * @param id the ID of the entity to be retrieved
     * @return the entity with the corresponding ID
     * @throws NotFoundException if there is no {@code Item} with the provided ID.
     */
    @ApiMethod(
            name = "get",
            path = "item/{id}",
            httpMethod = ApiMethod.HttpMethod.GET)
    public Item get(@Named("id") Long id) throws NotFoundException {
        logger.info("Getting Item with ID: " + id);
        Item item = ofy().load().type(Item.class).id(id).now();
        if (item == null) {
            throw new NotFoundException("Could not find Item with ID: " + id);
        }
        return item;
    }

    /**
     * Inserts a new {@code Item}.
     */
    @ApiMethod(
            name = "insert",
            path = "item",
            httpMethod = ApiMethod.HttpMethod.POST)
    public Item insert(Item item) {
        // Typically in a RESTful API a POST does not have a known ID (assuming the ID is used in the resource path).
        // You should validate that item.id has not been set. If the ID type is not supported by the
        // Objectify ID generator, e.g. long or String, then you should generate the unique ID yourself prior to saving.
        //
        // If your client provides the ID then you should probably use PUT instead.
        ofy().save().entity(item).now();
        logger.info("Created Item with ID: " + item.getId());

        return ofy().load().entity(item).now();
    }

    /**
     * Updates an existing {@code Item}.
     *
     * @param id   the ID of the entity to be updated
     * @param item the desired state of the entity
     * @return the updated version of the entity
     * @throws NotFoundException if the {@code id} does not correspond to an existing
     *                           {@code Item}
     */
    @ApiMethod(
            name = "update",
            path = "item/{id}",
            httpMethod = ApiMethod.HttpMethod.PUT)
    public Item update(@Named("id") Long id, Item item) throws NotFoundException {
        // TODO: You should validate your ID parameter against your resource's ID here.
        checkExists(id);
        ofy().save().entity(item).now();
        logger.info("Updated Item: " + item);
        return ofy().load().entity(item).now();
    }

    /**
     * Deletes the specified {@code Item}.
     *
     * @param id the ID of the entity to delete
     * @throws NotFoundException if the {@code id} does not correspond to an existing
     *                           {@code Item}
     */
    @ApiMethod(
            name = "remove",
            path = "item/{id}",
            httpMethod = ApiMethod.HttpMethod.DELETE)
    public void remove(@Named("id") Long id) throws NotFoundException {
        checkExists(id);
        ofy().delete().type(Item.class).id(id).now();
        logger.info("Deleted Item with ID: " + id);
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
            path = "item",
            httpMethod = ApiMethod.HttpMethod.GET)
    public CollectionResponse<Item> list(@Nullable @Named("cursor") String cursor, @Nullable @Named("limit") Integer limit) {
        limit = limit == null ? DEFAULT_LIST_LIMIT : limit;
        Query<Item> query = ofy().load().type(Item.class).limit(limit);
        if (cursor != null) {
            query = query.startAt(Cursor.fromWebSafeString(cursor));
        }
        QueryResultIterator<Item> queryIterator = query.iterator();
        List<Item> itemList = new ArrayList<Item>(limit);
        while (queryIterator.hasNext()) {
            itemList.add(queryIterator.next());
        }
        return CollectionResponse.<Item>builder().setItems(itemList).setNextPageToken(queryIterator.getCursor().toWebSafeString()).build();
    }

    private void checkExists(Long id) throws NotFoundException {
        try {
            ofy().load().type(Item.class).id(id).safe();
        } catch (com.googlecode.objectify.NotFoundException e) {
            throw new NotFoundException("Could not find Item with ID: " + id);
        }
    }
}