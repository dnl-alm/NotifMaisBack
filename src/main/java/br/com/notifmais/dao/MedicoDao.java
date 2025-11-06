package br.com.notifmais.dao;

import br.com.notifmais.exception.EntidadeNaoEncontradaException;
import br.com.notifmais.model.Medico;
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
public class MedicoDao {

    @Inject
    private DataSource dataSource;

    public void cadastrar(Medico medico) throws SQLException {
        try (Connection conexao = dataSource.getConnection()) {

            PreparedStatement stmt = conexao.prepareStatement("insert into t_medico (id_medico, nm_medico, " +
                    "nm_especialidade) values (sq_t_medico.nextval, ?, ?)", new String[] {"id_medico"});

            stmt.setString(1, medico.getNome());
            stmt.setString(2, medico.getEspecialidade());

            stmt.executeUpdate();

            ResultSet resultSet = stmt.getGeneratedKeys();
            if (resultSet.next()){
                medico.setId(resultSet.getInt(1));
            }

        }
    }

    public List<Medico> listar() throws SQLException {
        try (Connection conexao = dataSource.getConnection()){
            PreparedStatement stmt = conexao.prepareStatement("select * from t_medico");
            ResultSet rs = stmt.executeQuery();
            List<Medico> lista = new ArrayList<>();
            while (rs.next()){
                Medico medico = parseMedico(rs);
                lista.add(medico);
            }
            return lista;
        }
    }

    public Medico buscar(int id) throws SQLException, EntidadeNaoEncontradaException {
        try (Connection conexao = dataSource.getConnection()) {
            PreparedStatement stmt = conexao.prepareStatement("select * from t_medico where id_medico = ?");
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (!rs.next()) {
                throw new EntidadeNaoEncontradaException("Médico não encontrado");
            }
            return parseMedico(rs);
        }
    }

    public void atualizar(Medico medico) throws SQLException, EntidadeNaoEncontradaException {
        try (Connection conexao = dataSource.getConnection()) {
            PreparedStatement stmt = conexao.prepareStatement("update t_medico set nm_medico = ?, nm_medico = ? where id_medico = ?");
            stmt.setString(1, medico.getNome());
            stmt.setString(2, medico.getEspecialidade());
            stmt.setInt(3, medico.getId());
            if (stmt.executeUpdate() == 0) {
                throw new EntidadeNaoEncontradaException("Médico não existe para ser atualizado");
            }
        }
    }

    public void apagar(int id) throws SQLException, EntidadeNaoEncontradaException {
        try (Connection conexao = dataSource.getConnection()) {
            PreparedStatement stmt = conexao.prepareStatement("delete from t_medico where id_medico = ?");
            stmt.setInt(1, id);
            if (stmt.executeUpdate() == 0) {
                throw new EntidadeNaoEncontradaException("Médico não existe para ser removido");
            }
        }
    }

    private Medico parseMedico(ResultSet rs) throws SQLException {
        int id = rs.getInt("id_medico");
        String nome = rs.getString("nm_medico");
        String especialidade = rs.getString("nm_especialidade");
        return new Medico(id, nome, especialidade);
    }

}
