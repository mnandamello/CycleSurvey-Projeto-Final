package br.com.CycleSurvey.domain.repository;

import br.com.CycleSurvey.domain.entity.Bicicleta;
import br.com.CycleSurvey.domain.entity.Cliente;
import br.com.CycleSurvey.domain.service.ClienteService;
import br.com.CycleSurvey.infra.ConnectionFactory;

import java.sql.*;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

public class BicicletaRepository implements Repository<Bicicleta,Long> {

    private ConnectionFactory factory;

    private static final AtomicReference<BicicletaRepository> instance = new AtomicReference<>();

    private BicicletaRepository() {
        this.factory = ConnectionFactory.build();
    }

    public static BicicletaRepository build() {
        instance.compareAndSet(null, new BicicletaRepository());
        return instance.get();
    }

    @Override
    public List<Bicicleta> findAll() {
        List<Bicicleta> list = new ArrayList<>();
        Connection con = factory.getConnection();
        ResultSet rs = null;
        Statement st = null;
        try {
            String sql = "SELECT * FROM t_cycleSurvey_info_bike";
            st = con.createStatement();
            rs = st.executeQuery(sql);

            ClienteService clienteService = new ClienteService();

            if (rs.isBeforeFirst()) {
                while (rs.next()) {
                    Long id = rs.getLong("id_bike");
                    String marca = rs.getString("marca");
                    String modelo = rs.getString("modelo");
                    int anoCompra = rs.getInt("ano_compra");
                    double valor = rs.getDouble("valor");
                    String nf = rs.getString("nota_fiscal");

                    long id_cliente = rs.getLong("id_pf");
                    Cliente cliente = null;


                    cliente = clienteService.findById(id_cliente);

                    list.add(new Bicicleta(id,marca,modelo,anoCompra,valor,nf, cliente));


                    /**
                     *
                     * SELECT *
                     * FROM TB_BICICLETA b
                     * left join TB_ACESSORIO a on
                     * (b.acessorio_id = a.id_acessorio)
                     * where b.id = ?
                     *
                     * +
                     */

                    /**TB_ACESSORIOS_BICICLETA*/



                }
            }
        } catch (SQLException e) {
            System.err.println("Não foi possível consultar os dados!\n" + e.getMessage());
        } finally {
            fecharObjetos(rs, st, con);
        }
        return list;
    }


    @Override
    public Bicicleta findById(Long id) {
        Bicicleta bicicleta = null;
        var sql = "SELECT * FROM t_cycleSurvey_info_bike where BICICLETA_ID = ?";
        Connection con = factory.getConnection();
        PreparedStatement ps = null;
        ResultSet rs = null;

        ClienteService clienteService = new ClienteService();

        try {
            ps = con.prepareStatement(sql);
            ps.setLong(1, id);
            rs = ps.executeQuery();
            if (rs.isBeforeFirst()) {
                while (rs.next()) {

                    String marca = rs.getString("marca");
                    String modelo = rs.getString("modelo");
                    int anoCompra = rs.getInt("ano_compra");
                    double valor = rs.getDouble("valor");
                    String nf = rs.getString("nota_fiscal");

                    long id_cliente = rs.getLong("id_pf");
                    Cliente cliente = null;


                    cliente = clienteService.findById(id_cliente);

                    bicicleta = new Bicicleta(id,marca,modelo,anoCompra,valor,nf, cliente);
                }
            } else {
                System.out.println("Dados não encontrados com o id: " + id);
            }
        } catch (SQLException e) {
            System.err.println("Não foi possível consultar os dados!\n" + e.getMessage());
        } finally {
            fecharObjetos(rs, ps, con);
        }
        return bicicleta;
    }

    @Override
    public Bicicleta persiste(Bicicleta bc) {

        var sql = "INSERT INTO t_cycleSurvey_info_bike ( id_bike, marca , modelo, ano_compra, valor, nota_fiscal) VALUES (0, ?,?,?,?,?, ?, ? )";

        Connection con = factory.getConnection();
        PreparedStatement ps = null;

        try {

            ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            // seta os valores dos parâmetros
            ps.setLong(1, bc.getId());
            ps.setString(2, bc.getMarca());
            ps.setString(3, bc.getModelo());
            ps.setInt(4, bc.getAnoDeCompra());
            ps.setDouble(5, bc.getValor());
            ps.setString(6, bc.getNf());
            ps.setLong(7, bc.getCliente().getId());


            ps.executeUpdate();

            final ResultSet rs = ps.getGeneratedKeys();

            if (rs.next()) {
                final Long id = rs.getLong(1);
                bc.setId(id);
            }

        } catch (SQLException e) {
            System.err.println("Não foi possível inserir os dados!\n" + e.getMessage());
        } finally {
            fecharObjetos(null, ps, con);
        }
        return bc;
    }
}
