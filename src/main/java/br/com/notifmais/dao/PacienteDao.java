package br.com.notifmais.dao;

import br.com.notifmais.exception.EntidadeNaoEncontradaException;
import br.com.notifmais.model.Paciente;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
public class PacienteDao {

    @Inject
    private DataSource dataSource;

    public void cadastrar(Paciente paciente) throws SQLException {
        try (Connection conexao = dataSource.getConnection()) {

            PreparedStatement stmt = conexao.prepareStatement("insert into t_paciente (id_paciente, nm_paciente, " +
                    "nm_email) values (sq_t_paciente.nextval, ?, ?)", new String[] {"id_paciente"});

            stmt.setString(1, paciente.getNome());
            stmt.setString(2, paciente.getEmail());

            stmt.executeUpdate();

            ResultSet resultSet = stmt.getGeneratedKeys();
            if (resultSet.next()){
                paciente.setId(resultSet.getInt(1));
            }

        }
    }

    public List<Paciente> listar() throws SQLException {
        try (Connection conexao = dataSource.getConnection()){
            PreparedStatement stmt = conexao.prepareStatement("select * from t_paciente");
            ResultSet rs = stmt.executeQuery();
            List<Paciente> lista = new ArrayList<>();
            while (rs.next()){
                Paciente paciente = parsePaciente(rs);
                lista.add(paciente);
            }
            return lista;
        }
    }

    public Paciente buscar(int id) throws SQLException, EntidadeNaoEncontradaException {
        try (Connection conexao = dataSource.getConnection()) {
            PreparedStatement stmt = conexao.prepareStatement("select * from t_paciente where id_paciente = ?");
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (!rs.next()) {
                throw new EntidadeNaoEncontradaException("Paciente não encontrado");
            }
            return parsePaciente(rs);
        }
    }

    public void atualizar(Paciente paciente) throws SQLException, EntidadeNaoEncontradaException {
        try (Connection conexao = dataSource.getConnection()) {
            PreparedStatement stmt = conexao.prepareStatement("update t_paciente set nm_paciente = ?, nm_email = ? where id_paciente = ?");
            stmt.setString(1, paciente.getNome());
            stmt.setString(2, paciente.getEmail());
            stmt.setInt(3, paciente.getId());
            if (stmt.executeUpdate() == 0) {
                throw new EntidadeNaoEncontradaException("Paciente não existe para ser atualizado");
            }
        }
    }

    public void apagar(int id) throws SQLException, EntidadeNaoEncontradaException {
        try (Connection conexao = dataSource.getConnection()) {
            PreparedStatement stmt = conexao.prepareStatement("delete from t_paciente where id_paciente = ?");
            stmt.setInt(1, id);
            if (stmt.executeUpdate() == 0) {
                throw new EntidadeNaoEncontradaException("Paciente não existe para ser removido");
            }
        }
    }

    private Paciente parsePaciente(ResultSet rs) throws SQLException {
        int id = rs.getInt("id_paciente");
        String nome = rs.getString("nm_paciente");
        String email = rs.getString("nm_email");
        return new Paciente(id, nome, email);
    }

}
