package br.com.notifmais.dao;

import br.com.notifmais.exception.EntidadeNaoEncontradaException;
import br.com.notifmais.model.Consulta;
import br.com.notifmais.model.Medico;
import br.com.notifmais.model.Paciente;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
public class ConsultaDao {

    @Inject
    private DataSource dataSource;

    public void cadastrar(Consulta consulta) throws SQLException {
        try (Connection conexao = dataSource.getConnection()) {

            PreparedStatement stmt = conexao.prepareStatement(
                    "INSERT INTO t_consulta (id_consulta, id_paciente, id_medico, dt_hora, tp_consulta, st_confirmacao) " +
                            "VALUES (sq_t_consulta.nextval, ?, ?, ?, ?, ?)",
                    new String[] {"id_consulta"}
            );

            stmt.setInt(1, consulta.getPaciente().getId());
            stmt.setInt(2, consulta.getMedico().getId());
            stmt.setObject(3, consulta.getDataHora());
            stmt.setString(4, consulta.getTipo());
            stmt.setString(5, consulta.getStatusConfirmacao());

            stmt.executeUpdate();

            ResultSet resultSet = stmt.getGeneratedKeys();
            if (resultSet.next()) {
                consulta.setId(resultSet.getInt(1));
            }
        }
    }


    public List<Consulta> listar() throws SQLException {
        try (Connection conexao = dataSource.getConnection()) {
            PreparedStatement stmt = conexao.prepareStatement(
                    "SELECT c.id_consulta, c.id_paciente, c.id_medico, c.dt_hora, c.tp_consulta, c.st_confirmacao, " +
                    "p.nm_paciente, p.nm_email, " +
                    "m.nm_medico, m.nm_especialidade " +
                    "FROM t_consulta c " +
                    "LEFT JOIN t_paciente p ON c.id_paciente = p.id_paciente " +
                    "LEFT JOIN t_medico m ON c.id_medico = m.id_medico"

            );

            ResultSet rs = stmt.executeQuery();
            List<Consulta> lista = new ArrayList<>();
            while (rs.next()) {
                Consulta consulta = parseConsulta(rs);
                lista.add(consulta);
            }
            return lista;
        }
    }

    public Consulta buscar(int id) throws SQLException, EntidadeNaoEncontradaException {
        try (Connection conexao = dataSource.getConnection()) {
            PreparedStatement stmt = conexao.prepareStatement(
                    "SELECT c.id_consulta, c.id_paciente, c.id_medico, c.dt_hora, c.tp_consulta, c.st_confirmacao, " +
                            "p.nm_paciente, p.nm_email, " +
                            "m.nm_medico, m.nm_especialidade " +
                            "FROM t_consulta c " +
                            "JOIN t_paciente p ON c.id_paciente = p.id_paciente " +
                            "JOIN t_medico m ON c.id_medico = m.id_medico " +
                            "WHERE c.id_consulta = ?"
            );
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (!rs.next()) {
                throw new EntidadeNaoEncontradaException("Consulta não encontrada");
            }
            return parseConsulta(rs);
        }
    }

    public void atualizarStatusConfirmacao(Consulta consulta) throws SQLException, EntidadeNaoEncontradaException {
        try (Connection conexao = dataSource.getConnection()) {
            PreparedStatement stmt = conexao.prepareStatement("update t_consulta set st_confirmacao = ? where id_consulta = ?");
            stmt.setString(1, consulta.getStatusConfirmacao());
            stmt.setInt(2, consulta.getId());

            if (stmt.executeUpdate() == 0) {
                throw new EntidadeNaoEncontradaException("Consulta não existe para ser atualizado");
            }
        }
    }

    private Consulta parseConsulta(ResultSet rs) throws SQLException {
        int id = rs.getInt("id_consulta");
        int idPaciente = rs.getInt("id_paciente");
        int idMedico = rs.getInt("id_medico");
        LocalDateTime dataHora = rs.getObject("dt_hora", LocalDateTime.class);
        String tipo = rs.getString("tp_consulta");
        String confirmacao = rs.getString("st_confirmacao");

        String nomePaciente = rs.getString("nm_paciente");
        String emailPaciente = rs.getString("nm_email");
        String nomeMedico = rs.getString("nm_medico");
        String especialidadeMedico = rs.getString("nm_especialidade");

        Paciente paciente = new Paciente(idPaciente, nomePaciente, emailPaciente);
        Medico medico = new Medico(idMedico, nomeMedico, especialidadeMedico);

        return new Consulta(id, dataHora, tipo, confirmacao, paciente, medico);
    }

}
