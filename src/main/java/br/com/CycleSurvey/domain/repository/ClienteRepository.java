package br.com.CycleSurvey.domain.repository;

import br.com.CycleSurvey.domain.entity.Cliente;
import br.com.CycleSurvey.domain.service.BicicletaService;
import br.com.CycleSurvey.infra.ConnectionFactory;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class ClienteRepository implements Repository<Cliente, Long> {

    private ConnectionFactory factory;

    private static final AtomicReference<ClienteRepository> instance = new AtomicReference<>();

    private ClienteRepository() {
        this.factory = ConnectionFactory.build();
    }

    public static ClienteRepository build() {
        instance.compareAndSet( null, new ClienteRepository() );
        return instance.get();
    }

    @Override
    public List<Cliente> findAll() {
        List<Cliente> list = new ArrayList<>();
        Connection con = factory.getConnection();
        ResultSet rs = null;
        Statement st = null;
        try {
            String sql = "SELECT *  FROM TB_PESSOA_FISICA";

            st = con.createStatement();
            rs = st.executeQuery( sql );
            BicicletaService bicicletaService = new BicicletaService();
            if (rs.isBeforeFirst()) {
                while (rs.next()) {
                    long id = rs.getLong( "id_pf" );
                    String nome = rs.getString( "nm_completo" );
                    LocalDate nascimento = rs.getDate( "dt_nascimento" ).toLocalDate();
                    String cpf = rs.getString( "cpf" );
                    String cel = rs.getString( "celular" );
                    String cep = rs.getString( "cep" );
                    String cidade = rs.getString( "cidade" );
                    String logradouro = rs.getString( "logradouro" );
                    String nm_log = rs.getString( "num_logradouro" );
                    String estado = rs.getString( "estado" );
                    String complemento = rs.getString( "complemento" );

                    list.add( new Cliente( id, nome, nascimento, cpf, cel, cep, cidade, logradouro, nm_log, estado, complemento ) );
                }
            }
        } catch (SQLException e) {
            System.err.println( "Não foi possível consultar os dados!\n" + e.getMessage() );
        } finally {
            fecharObjetos( rs, st, con );
        }
        return list;
    }


    @Override
    public Cliente findById(Long id) {
        Cliente pessoa = null;
        var sql = "SELECT *  FROM tb_pessoa_fisica  where id_pf = ?";
        Connection con = factory.getConnection();
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = con.prepareStatement( sql );
            ps.setLong( 1, id );
            rs = ps.executeQuery();

            if (rs.isBeforeFirst()) {
                while (rs.next()) {
                    String nome = rs.getString( "nm_completo" );
                    LocalDate nascimento = rs.getDate( "dt_nascimento" ).toLocalDate();
                    String cpf = rs.getString( "cpf" );
                    String cel = rs.getString( "celular" );
                    String cep = rs.getString( "cep" );
                    String cidade = rs.getString( "cidade" );
                    String logradouro = rs.getString( "logradouro" );
                    String nm_log = rs.getString( "num_logradouro" );
                    String estado = rs.getString( "estado" );
                    String complemento = rs.getString( "complemento" );

                    pessoa = new Cliente( id, nome, nascimento, cpf, cel, cep, cidade, logradouro, nm_log, estado, complemento );
                }
            } else {
                System.out.println( "Dados não encontrados com o id: " + id );
            }
        } catch (SQLException e) {
            System.err.println( "Não foi possível consultar os dados!\n" + e.getMessage() );
        } finally {
            fecharObjetos( rs, ps, con );
        }
        return pessoa;
    }


    @Override
    public Cliente persiste(Cliente cl) {


        var sql = "INSERT INTO tb_pessoa_fisica  (id_pf, nm_completo, dt_nascimento , cpf, celular, cep, cidade, logradouro, num_logradouro, estado, complemento) VALUES (SEQ_PESSOA_FISICA.nextval,?, ?, ?, ?, ?, ?, ?, ?, ?, ? )";


        Connection con = factory.getConnection();
        PreparedStatement ps = null;

        try {

            ps = con.prepareStatement( sql, new String[]{"ID_PF"} );

            // seta os valores dos parâmetros
            ps.setString( 1, cl.getNome() );
            ps.setDate( 2, Date.valueOf( cl.getNascimento() ) );
            ps.setString( 3, cl.getCpf() );
            ps.setString( 4, cl.getCelular() );
            ps.setString( 5, cl.getCep() );
            ps.setString( 6, cl.getCidade() );
            ps.setString( 7, cl.getLogradouro() );
            ps.setString( 8, cl.getNumero() );
            ps.setString( 9, cl.getEstado() );
            ps.setString( 10, cl.getComplemento() );

            ps.executeUpdate();

            final ResultSet rs = ps.getGeneratedKeys();

            if (rs.next()) {
                final Long id = rs.getLong( 1 );
                cl.setId( id );
            }

        } catch (SQLException e) {
            System.err.println( "Não foi possível inserir os dados!\n" + e.getMessage() );
        } finally {
            fecharObjetos( null, ps, con );
        }
        return cl;
    }

}