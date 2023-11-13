package br.com.CycleSurvey.domain.repository;

import br.com.CycleSurvey.domain.entity.Bicicleta;
import br.com.CycleSurvey.domain.entity.Cliente;
import br.com.CycleSurvey.domain.service.ClienteService;
import br.com.CycleSurvey.infra.ConnectionFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class BicicletaRepository implements Repository<Bicicleta, Long> {

    private ConnectionFactory factory;

    private static final AtomicReference<BicicletaRepository> instance = new AtomicReference<>();

    private BicicletaRepository() {
        this.factory = ConnectionFactory.build();
    }

    public static BicicletaRepository build() {
        instance.compareAndSet( null, new BicicletaRepository() );
        return instance.get();
    }

    @Override
    public List<Bicicleta> findAll() {
        ClienteService clienteService = new ClienteService();
        List<Bicicleta> list = new ArrayList<>();
        Connection con = factory.getConnection();
        ResultSet rs = null;
        Statement st = null;
        try {
            String sql = "SELECT * FROM TB_INFO_BIKE";
            st = con.createStatement();

            rs = st.executeQuery( sql );

            if (rs.isBeforeFirst()) {
                while (rs.next()) {
                    Long id = rs.getLong( "id_bike" );
                    String marca = rs.getString( "marca" );
                    String modelo = rs.getString( "modelo" );
                    int anoCompra = rs.getInt( "ano_compra" );
                    double valor = rs.getDouble( "valor" );
                    String nf = rs.getString( "nota_fiscal" );
                    long id_cliente = rs.getLong( "id_pf" );

                    Cliente cliente = null;
                    cliente = clienteService.findById( id_cliente );
                    list.add( new Bicicleta( id, marca, modelo, anoCompra, valor, nf, cliente ) );
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
    public Bicicleta findById(Long id) {
        Bicicleta bicicleta = null;
        var sql = """
                SELECT * 
                FROM tb_info_bike 
                where ID_BIKE = ?""";

        Connection con = factory.getConnection();
        PreparedStatement ps = null;
        ResultSet rs = null;

        ClienteService clienteService = new ClienteService();

        try {
            ps = con.prepareStatement( sql );

            ps.setLong( 1, id );
            rs = ps.executeQuery();
            if (rs.isBeforeFirst()) {
                while (rs.next()) {

                    String marca = rs.getString( "marca" );
                    String modelo = rs.getString( "modelo" );
                    int anoCompra = rs.getInt( "ano_compra" );
                    double valor = rs.getDouble( "valor" );
                    String nf = rs.getString( "nota_fiscal" );

                    long id_cliente = rs.getLong( "id_pf" );
                    Cliente cliente = null;


                    cliente = clienteService.findById( id_cliente );

                    bicicleta = new Bicicleta( id, marca, modelo, anoCompra, valor, nf, cliente );
                }
            } else {
                System.out.println( "Dados não encontrados com o id: " + id );
            }
        } catch (SQLException e) {
            System.err.println( "Não foi possível consultar os dados!\n" + e.getMessage() );
        } finally {
            fecharObjetos( rs, ps, con );
        }
        return bicicleta;
    }

    @Override
    public Bicicleta persiste(Bicicleta bc) {

        var sql = "INSERT INTO tb_info_bike  ( id_bike, marca , modelo, ano_compra, valor, nota_fiscal, ID_PF) VALUES (SEQ_INFO_BIKE.nextval, ?,?,?,?,?, ? )";

        Connection con = factory.getConnection();
        PreparedStatement ps = null;

        try {

            ps = con.prepareStatement( sql, new String[]{"ID_BIKE"} );
            // seta os valores dos parâmetros

            ps.setString( 1, bc.getMarca() );
            ps.setString( 2, bc.getModelo() );
            ps.setInt( 3, bc.getAnoDeCompra() );
            ps.setDouble( 4, bc.getValor() );
            ps.setString( 5, bc.getNf() );
            ps.setLong( 6, bc.getCliente().getId() );


            ps.executeUpdate();

            final ResultSet rs = ps.getGeneratedKeys();

            if (rs.next()) {
                final Long id = rs.getLong( 1 );
                bc.setId( id );
            }

        } catch (SQLException e) {
            System.err.println( "Não foi possível inserir os dados!\n" + e.getMessage() );
        } finally {
            fecharObjetos( null, ps, con );
        }
        return bc;
    }
}
