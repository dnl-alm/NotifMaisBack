package br.com.notifmais.resource;

import br.com.notifmais.dao.PacienteDao;
import br.com.notifmais.dto.paciente.CadastroPacienteDto;
import br.com.notifmais.dto.paciente.DetalhesPacienteDto;
import br.com.notifmais.exception.EntidadeNaoEncontradaException;
import br.com.notifmais.model.Paciente;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;
import org.modelmapper.ModelMapper;

import java.net.URI;
import java.sql.SQLException;
import java.util.List;

@Path("/pacientes")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class PacienteResource {

    @Inject
    private PacienteDao pacienteDao;

    @Inject
    private ModelMapper modelMapper;

    @GET
    @Path("/{id}")
    public Response buscar(@PathParam("id") int id) throws SQLException, EntidadeNaoEncontradaException {
        DetalhesPacienteDto dto = modelMapper
                .map(pacienteDao.buscar(id), DetalhesPacienteDto.class);
        return Response.ok(dto).build();
    }

    @GET
    public List<DetalhesPacienteDto> listar() throws SQLException {
        return pacienteDao.listar().stream().map(
                p -> modelMapper.map(p, DetalhesPacienteDto.class)
        ).toList();
    }

    @POST
    public Response create(@Valid CadastroPacienteDto dto, @Context UriInfo uriInfo) throws SQLException {
        Paciente paciente = modelMapper.map(dto, Paciente.class);

        pacienteDao.cadastrar(paciente);

        //Constroi uma URL de retorno, para acessar o recurso criado
        URI uri = uriInfo.getAbsolutePathBuilder()
                .path(String.valueOf(paciente.getId())).build();

        return Response.created(uri).entity(modelMapper.map(paciente, DetalhesPacienteDto.class)).build(); //HTTP STATUS CODE 201 (Created)
    }

}
