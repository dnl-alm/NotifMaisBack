package br.com.notifmais.resource;

import br.com.notifmais.dao.MedicoDao;
import br.com.notifmais.dto.medico.DetalhesMedicoDto;
import br.com.notifmais.exception.EntidadeNaoEncontradaException;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.modelmapper.ModelMapper;

import java.sql.SQLException;
import java.util.List;

@Path("/medicos")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class MedicoResource {

    @Inject
    private ModelMapper modelMapper;

    @Inject
    private MedicoDao medicoDao;

    @GET
    @Path("/{id}")
    public Response buscar(@PathParam("id") int id) throws SQLException, EntidadeNaoEncontradaException {
        DetalhesMedicoDto dto = modelMapper
                .map(medicoDao.buscar(id), DetalhesMedicoDto.class);
        return Response.ok(dto).build();
    }

    @GET
    public List<DetalhesMedicoDto> listar() throws SQLException {
        return medicoDao.listar().stream().map(
                p -> modelMapper.map(p, DetalhesMedicoDto.class)
        ).toList();
    }

}
