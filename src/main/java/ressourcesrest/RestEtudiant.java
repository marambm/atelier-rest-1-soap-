package ressourcesrest;

import entities.Etudiant;
import entities.EtudiantList;
import entities.Option;
import metiers.EtudiantBusiness;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;

@Path("etudiants")
public class RestEtudiant {

    public static EtudiantBusiness etB = new EtudiantBusiness();

    // 1) POST /etudiants
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addEtudiant(Etudiant e) {
        if (e == null || e.getOption() == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        // addEtudiant renvoie false si l'option n'existe pas
        if (etB.addEtudiant(e)) {
            return Response.status(Response.Status.OK).build();
        }
        return Response.status(Response.Status.NOT_FOUND).build();
    }

    // 2) GET /etudiants
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllEtudiants() {
        return Response.status(Response.Status.OK).entity(etB.getAllEtudiants()).build();
    }

    // 3) GET /etudiants/I001
    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getEtudiantById(@PathParam("id") String id) {
        Etudiant e = etB.getEtudiantByIdentifiant(id);
        if (e != null) {
            return Response.status(Response.Status.OK).entity(e).build();
        }
        return Response.status(Response.Status.NOT_FOUND).build();
    }

    // 4) DELETE /etudiants/I003
    @DELETE
    @Path("{id}")
    public Response deleteEtudiant(@PathParam("id") String id) {
        if (etB.deleteEtudiant(id)) {
            return Response.status(Response.Status.NO_CONTENT).build();
        }
        return Response.status(Response.Status.NOT_FOUND).build();
    }

    // 5) PUT /etudiants/I001
    @PUT
    @Path("{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateEtudiant(@PathParam("id") String id, Etudiant e) {
        if (etB.updateEtudiant(id, e)) {
            return Response.status(Response.Status.OK).build();
        }
        return Response.status(Response.Status.NOT_FOUND).build();
    }

    // 6) GET /etudiants/option?codeOption=1  (XML)
    @GET
    @Path("option")
    @Produces(MediaType.APPLICATION_XML)
    public Response getEtudiantsByOption(@QueryParam("codeOption") int codeOption) {
        Option option = RestOption.optB.getOptionByCode(codeOption);
        if (option == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        List<Etudiant> result = new ArrayList<>();
        for (Etudiant e : etB.getEtudiantsByOption(option)) {
            Etudiant copy = new Etudiant();   // sans "option", comme le XML du TD
            copy.setIdentifiant(e.getIdentifiant());
            copy.setNom(e.getNom());
            copy.setPrenom(e.getPrenom());
            copy.setAnneeEtude(e.getAnneeEtude());
            copy.setEmail(e.getEmail());
            result.add(copy);
        }
        return Response.status(Response.Status.OK).entity(new EtudiantList(result)).build();
    }
}