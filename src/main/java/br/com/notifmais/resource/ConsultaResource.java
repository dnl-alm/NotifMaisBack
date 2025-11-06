package br.com.notifmais.resource;

import br.com.notifmais.dao.ConsultaDao;
import br.com.notifmais.dao.MedicoDao;
import br.com.notifmais.dao.PacienteDao;
import br.com.notifmais.dto.consulta.AtualizarStatusConsultaDto;
import br.com.notifmais.dto.consulta.CadastroConsultaDto;
import br.com.notifmais.dto.consulta.DetalhesConsultaDto;
import br.com.notifmais.exception.EntidadeNaoEncontradaException;
import br.com.notifmais.model.Consulta;
import br.com.notifmais.model.Medico;
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

@Path("/consultas")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class ConsultaResource {

    @Inject
    private ModelMapper modelMapper;

    @Inject
    private ConsultaDao consultaDao;

    @Inject
    private PacienteDao pacienteDao;

    @Inject
    private MedicoDao medicoDao;

    @GET
    public List<DetalhesConsultaDto> listar() throws SQLException {
        return consultaDao.listar().stream().map(
                p -> modelMapper.map(p, DetalhesConsultaDto.class)
        ).toList();
    }

    @GET
    @Path("/{id}")
    public Response buscar(@PathParam("id") int id) throws SQLException, EntidadeNaoEncontradaException {
        DetalhesConsultaDto dto = modelMapper
                .map(consultaDao.buscar(id), DetalhesConsultaDto.class);
        return Response.ok(dto).build();
    }

    @POST
    public Response create(@Valid CadastroConsultaDto dto, @Context UriInfo uriInfo) throws SQLException {
        Consulta consulta = modelMapper.map(dto, Consulta.class);

        Paciente p = pacienteDao.buscar(dto.getPaciente());
        consulta.setPaciente(p);

        Medico m = medicoDao.buscar(dto.getMedico());
        consulta.setMedico(m);

        consultaDao.cadastrar(consulta);

        URI uri = uriInfo.getAbsolutePathBuilder()
                .path(String.valueOf(consulta.getId())).build();

        return Response.created(uri).entity(modelMapper.map(consulta, DetalhesConsultaDto.class)).build(); //HTTP STATUS CODE 201 (Created)
    }

    @PUT
    @Path("/{id}")
    public Response atualizarStatusConsulta(@PathParam("id") int id, @Valid AtualizarStatusConsultaDto dto) throws EntidadeNaoEncontradaException, SQLException {
        Consulta consulta = consultaDao.buscar(id);
        consulta.setStatusConfirmacao(dto.getStatusConfirmacao());
        consultaDao.atualizarStatusConfirmacao(consulta);

        return Response.ok(modelMapper.map(consulta, DetalhesConsultaDto.class)).build();

    }

}
