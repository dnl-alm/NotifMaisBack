package br.com.notifmais.resource;

import br.com.notifmais.dao.ConsultaDao;
import br.com.notifmais.dao.LembreteDao;
import br.com.notifmais.dto.lembrete.CadastroLembreteDto;
import br.com.notifmais.dto.lembrete.DetalhesLembreteDto;
import br.com.notifmais.exception.EntidadeNaoEncontradaException;
import br.com.notifmais.model.Consulta;
import br.com.notifmais.model.Lembrete;
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

@Path("/lembretes")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class LembreteResource {

    @Inject
    private ModelMapper modelMapper;

    @Inject
    private LembreteDao lembreteDao;

    @Inject
    private ConsultaDao consultaDao;

    @GET
    public List<DetalhesLembreteDto> listarPorPaciente(@QueryParam("pacienteId") int pacienteId) throws SQLException {
        return lembreteDao.listarPorPaciente(pacienteId).stream()
                .map(l -> modelMapper.map(l, DetalhesLembreteDto.class))
                .toList();
    }

    @GET
    @Path("/{id}")
    public Response buscar(@PathParam("id") int id) throws SQLException, EntidadeNaoEncontradaException {
        Lembrete lembrete = lembreteDao.buscar(id);
        DetalhesLembreteDto dto = modelMapper.map(lembrete, DetalhesLembreteDto.class);
        return Response.ok(dto).build();
    }

}

