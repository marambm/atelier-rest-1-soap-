package ressourcesrest;

import entities.Option;
import metiers.OptionBusiness;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;

@Path("options")
public class RestOption {

    public static OptionBusiness optB = new OptionBusiness();

    // 1) POST /options
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addOption(Option op) {
        if (optB.addOption(op)) {
            return Response.status(Response.Status.OK).build();
        }
        return Response.status(Response.Status.NOT_FOUND).build();
    }

    // 2) GET /options   et   3) GET /options?domaine=Mathématiques
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllOptions(@QueryParam("domaine") String domaine) {
        List<Option> l = new ArrayList<>();
        if (domaine == null || domaine.isEmpty()) {
            l = optB.getListeOptions();
        } else {
            for (Option o : optB.getListeOptions()) {
                if (o.getDomaine() != null && o.getDomaine().equalsIgnoreCase(domaine)) {
                    l.add(o);
                }
            }
        }
        return Response.status(Response.Status.OK).entity(l).build();
    }

    // 4) DELETE /options/2
    @DELETE
    @Path("{code}")
    public Response deleteOption(@PathParam("code") int code) {
        if (optB.deleteOption(code)) {
            return Response.status(Response.Status.NO_CONTENT).build();
        }
        return Response.status(Response.Status.NOT_FOUND).build();
    }

    // 5) PUT /options/1
    @PUT
    @Path("{code}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateOption(@PathParam("code") int code, Option op) {
        if (optB.updateOption(code, op)) {
            return Response.status(Response.Status.OK).build();
        }
        return Response.status(Response.Status.NOT_FOUND).build();
    }

    // 6) GET /options/1
    @GET
    @Path("{code}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getOptionById(@PathParam("code") int code) {
        for (Option o : optB.getListeOptions()) {
            if (o.getCodeOption() == code) {
                return Response.status(Response.Status.OK).entity(o).build();
            }
        }
        return Response.status(Response.Status.NOT_FOUND).build();
    }
}