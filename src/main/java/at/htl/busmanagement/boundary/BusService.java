package at.htl.busmanagement.boundary;

import at.htl.busmanagement.entity.Bus;
import at.htl.busmanagement.repository.BusRepository;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.jboss.logging.Logger;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.json.JsonObject;
import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.util.stream.Collectors;

@Path("bus")
@Tag(name = "Bus")
public class BusService {
    @Inject
    Logger LOG;

    @Inject
    BusRepository busRepository;

    @GET
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Operation(
            summary = "Returns all buses",
            description = "onlyTitle -> returns all buses with just titles; driver -> returns all buses with the given driver id"
    )
    public Object getBuses(@QueryParam("onlyTitle") boolean onlyTitle, @QueryParam("driver") Long driver) {
        if (onlyTitle) {
            LOG.info("Retrieve all buses sorted");
            return busRepository.getAll().stream().map(Bus::getTitle).collect(Collectors.toList());
        } else if(driver != null){
            return busRepository.driverById(driver);
        }
        LOG.info("Retrieve all buses");
        return busRepository.getAll();
    }


    @GET
    @Path("/{id}")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Operation(
            summary = "Returns one bus",
            description = "id -> bus id"
    )
    public Response getBus(@PathParam("id") Long id) {
        try {
            Bus busSchedule = busRepository.find(id);
            LOG.info("Retrieved bus with id " + id);
            return Response.ok(busSchedule).build();
        } catch (IllegalArgumentException exception) {
            LOG.warn(exception.getMessage());
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

    @POST
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Consumes({MediaType.APPLICATION_JSON})
    @Operation(
            summary = "Inserts one bus",
            description = "Inserts one bus, if title is already taken -> seeOther"
    )
    public Response insertBus(Bus bus, @Context UriInfo uriInfo) {
        Bus tempBus = busRepository.alreadyExists(bus);
        if (tempBus != null) {
            LOG.info("Bus already exists");

            UriBuilder uriBuilder = uriInfo
                    .getAbsolutePathBuilder()
                    .path(Long.toString(tempBus.getId()));

            return Response.seeOther(uriBuilder.build()).build();
        }
        busRepository.insert(bus);

        LOG.info("New bus created: " + bus);

        UriBuilder uriBuilder = uriInfo
                .getAbsolutePathBuilder()
                .path(Long.toString(bus.getId()));

        LOG.info(uriInfo
                .getAbsolutePathBuilder()
                .path(Long.toString(bus.getId())));

        return Response.created(uriBuilder.build()).build();
    }

    @POST
    @Path("multiple")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Consumes({MediaType.APPLICATION_JSON})
    @Operation(
            summary = "Inserts multiple buses",
            description = "Inserts multiple buses, if one name is taken the process stops"
    )
    public Response insertBuses(Bus[] buses, @Context UriInfo uriInfo) {
        for (var bus : buses) {
            Bus tempBus = busRepository.alreadyExists(bus);
            if (tempBus != null) {
                LOG.info("Bus with title " + bus.getTitle() + " already exists! No bus has been created");

                return Response
                        .notModified("Bus with title " + bus.getTitle() + " already exists! No bus has been created")
                        .build();
            }
        }

        for (var bus : buses) {
            busRepository.insert(bus);
            LOG.info("New bus created: " + bus);
        }

        return Response.ok().build();
    }

    @DELETE
    @Path("/{id}")
    @Consumes({MediaType.APPLICATION_JSON})
    @Operation(
            summary = "Removes one bus",
            description = "Removes one bus by given id, if id does not exist -> notFound"
    )
    public Response removeBus(@PathParam("id") Long id) {
        try {
            busRepository.remove(id);
            LOG.info("Bus with id " + id + " removed successfully");
            return Response.noContent().build();
        } catch (IllegalArgumentException exception) {
            LOG.warn(exception.getMessage());
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

    @PUT
    @Path("/{id}")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Operation(
            summary = "Updates whole bus",
            description = "Updates bus by the given id"
    )
    public Response updateBus(@Context UriInfo uriInfo, @PathParam("id") Long id, Bus bus) {
        try {
            busRepository.update(id, bus);
            LOG.info("Bus with id " + id + " updated successfully");

            UriBuilder uriBuilder = uriInfo
                    .getAbsolutePathBuilder();

            return Response.seeOther(uriBuilder.build()).build();
        } catch (IllegalArgumentException exception) {
            LOG.warn(exception.getMessage());
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

    @PATCH
    @Path("/{id}")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Transactional
    @Operation(
            summary = "Updates the bus partly",
            description = "Updates bus by the given id"
    )
    public Response patchBus(@PathParam("id") Long id, JsonObject jsonObject, @Context UriInfo uriInfo) {
        try {
            Bus bus = busRepository.find(id);
            bus.setTitle(jsonObject.getString("title"));
            LOG.info("Bus with id " + id + " updated successfully");

            UriBuilder uriBuilder = uriInfo
                    .getAbsolutePathBuilder();

            return Response.seeOther(uriBuilder.build()).build();
        } catch (IllegalArgumentException exception) {
            LOG.warn(exception.getMessage());
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }
}
