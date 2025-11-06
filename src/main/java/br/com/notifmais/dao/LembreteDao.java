package br.com.notifmais.dao;

import br.com.notifmais.exception.EntidadeNaoEncontradaException;
import br.com.notifmais.model.Consulta;
import br.com.notifmais.model.Lembrete;
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
public class LembreteDao {

    @Inject
    private DataSource dataSource;

    public void cadastrar(Lembrete lembrete) throws SQLException {
        try (Connection conexao = dataSource.getConnection()) {
            PreparedStatement stmt = conexao.prepareStatement(
                    "INSERT INTO t_lembrete (id_lembrete, id_consulta) " +
                            "VALUES (sq_t_lembrete.nextval, ?)",
                    new String[]{"id_lembrete"}
            );

            stmt.setInt(1, lembrete.getConsulta().getId());
            stmt.executeUpdate();

            ResultSet resultSet = stmt.getGeneratedKeys();
            if (resultSet.next()) {
                lembrete.setId(resultSet.getInt(1));
            }
        }
    }

    public List<Lembrete> listarPorPaciente(int idPaciente) throws SQLException {
        try (Connection conexao = dataSource.getConnection()) {
            PreparedStatement stmt = conexao.prepareStatement(
                    "SELECT l.id_lembrete, " +
                            "c.id_consulta, c.dt_hora, c.tp_consulta, c.st_confirmacao, " +
                            "p.id_paciente, p.nm_paciente, p.nm_email, " +
                            "m.id_medico, m.nm_medico, m.nm_especialidade " +
                            "FROM t_lembrete l " +
                            "JOIN t_consulta c ON l.id_consulta = c.id_consulta " +
                            "JOIN t_paciente p ON c.id_paciente = p.id_paciente " +
                            "JOIN t_medico m ON c.id_medico = m.id_medico " +
                            "WHERE p.id_paciente = ?"
            );

            stmt.setInt(1, idPaciente);
            ResultSet rs = stmt.executeQuery();

            List<Lembrete> lista = new ArrayList<>();
            while (rs.next()) {
                lista.add(parseLembrete(rs));
            }
            return lista;
        }
    }

    public Lembrete buscar(int id) throws SQLException, EntidadeNaoEncontradaException {
        try (Connection conexao = dataSource.getConnection()) {
            PreparedStatement stmt = conexao.prepareStatement(
                    "SELECT l.id_lembrete, " +
                            "c.id_consulta, c.dt_hora, c.tp_consulta, c.st_confirmacao, " +
                            "p.id_paciente, p.nm_paciente, p.nm_email, " +
                            "m.id_medico, m.nm_medico, m.nm_especialidade " +
                            "FROM t_lembrete l " +
                            "JOIN t_consulta c ON l.id_consulta = c.id_consulta " +
                            "JOIN t_paciente p ON c.id_paciente = p.id_paciente " +
                            "JOIN t_medico m ON c.id_medico = m.id_medico " +
                            "WHERE l.id_lembrete = ?"
            );

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (!rs.next()) {
                throw new EntidadeNaoEncontradaException("Lembrete não encontrado");
            }

            return parseLembrete(rs);
        }
    }

    private Lembrete parseLembrete(ResultSet rs) throws SQLException {
        int idLembrete = rs.getInt("id_lembrete");

        int idConsulta = rs.getInt("id_consulta");
        LocalDateTime dataHora = rs.getObject("dt_hora", LocalDateTime.class);
        String tipo = rs.getString("tp_consulta");
        String statusConfirmacao = rs.getString("st_confirmacao");

        int idPaciente = rs.getInt("id_paciente");
        String nomePaciente = rs.getString("nm_paciente");
        String emailPaciente = rs.getString("nm_email");

        int idMedico = rs.getInt("id_medico");
        String nomeMedico = rs.getString("nm_medico");
        String especialidadeMedico = rs.getString("nm_especialidade");

        Paciente paciente = new Paciente(idPaciente, nomePaciente, emailPaciente);
        Medico medico = new Medico(idMedico, nomeMedico, especialidadeMedico);
        Consulta consulta = new Consulta(idConsulta, dataHora, tipo, statusConfirmacao, paciente, medico);

        return new Lembrete(idLembrete, consulta);
    }
}

