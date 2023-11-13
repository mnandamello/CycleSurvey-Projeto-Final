package br.com.CycleSurvey.domain.repository;


import br.com.CycleSurvey.domain.entity.Acessorio;
import br.com.CycleSurvey.domain.entity.Bicicleta;
import br.com.CycleSurvey.domain.service.BicicletaService;
import br.com.CycleSurvey.infra.ConnectionFactory;
import java.sql.Connection;
import java.util.List;


import java.sql.*;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

public class AcessorioRepository implements Repository<Acessorio, Long> {


    private ConnectionFactory factory;

    private static final AtomicReference<AcessorioRepository> instance = new AtomicReference<>();

    private AcessorioRepository() {
        this.factory = ConnectionFactory.build();
    }

    public static AcessorioRepository build() {
        instance.compareAndSet(null, new AcessorioRepository());
        return instance.get();
    }


    @Override
    public List<Acessorio> findAll() {
        List<Acessorio> list = new ArrayList<>();
        Connection con = factory.getConnection();
        ResultSet rs = null;
        Statement st = null;
        try {
            String sql = "SELECT * FROM t_cycleSurvey_acessorio";
            st = con.createStatement();
            rs = st.executeQuery(sql);
            BicicletaService bicicletaService = new BicicletaService();

            if (rs.isBeforeFirst()) {
                while (rs.next()) {
                    Long id = rs.getLong("id_acessorio");
                    String marca = rs.getString("marca_acessorio");
                    String modelo = rs.getString("modelo");
                    String tipo = rs.getString("tipo_acessorio");
                    double valor = rs.getDouble("valor");
                    String nf = rs.getString("nota_fiscal_acessorio");

                    long idBk = rs.getLong("id_bike");
                    Bicicleta bicicleta = null;


                    bicicleta = bicicletaService.findById(idBk);



                    list.add(new Acessorio(id, marca, modelo,valor, tipo, nf,bicicleta));
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
    public Acessorio findById(Long id) {
        Acessorio acessorio = null;
        var sql = "SELECT * FROM t_cycleSurvey_acessorio where ACESSORIO_ID = ?";
        Connection con = factory.getConnection();
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = con.prepareStatement(sql);
            ps.setLong(1, id);
            rs = ps.executeQuery();
            BicicletaService bicicletaService = new BicicletaService();

            if (rs.isBeforeFirst()) {
                while (rs.next()) {

                    String marca = rs.getString("marca_acessorio");
                    String modelo = rs.getString("modelo");
                    String tipo = rs.getString("tipo_acessorio");
                    double valor = rs.getDouble("valor");
                    String nf = rs.getString("nota_fiscal_acessorio");
                    long idBk = rs.getLong("id_bike");
                    Bicicleta bicicleta = null;

                    bicicleta = bicicletaService.findById(idBk);

                    acessorio = new Acessorio(id, marca, modelo,valor, tipo, nf,bicicleta);
                }
            } else {
                System.out.println("Dados não encontrados com o id: " + id);
            }
        } catch (SQLException e) {
            System.err.println("Não foi possível consultar os dados!\n" + e.getMessage());
        } finally {
            fecharObjetos(rs, ps, con);
        }
        return acessorio;
    }


    @Override
    public Acessorio persiste(Acessorio ac) {

        var sql = "INSERT INTO t_cycleSurvey_acessorio (marca_acessorio , modelo, tipo_acessorio, valor,nota_fiscal_acessorio, id_bike)" +
                " VALUES (0, ?,?,?,?,?,?)";


        Connection con = factory.getConnection();
        PreparedStatement ps = null;

        try {

            ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            // seta os valores dos parâmetros
            ps.setString(1, ac.getMarca());
            ps.setString(2, ac.getModelo());
            ps.setString(3, ac.getTipo());
            ps.setDouble(4, ac.getValor());
            ps.setString(5, ac.getNf());
            ps.setLong(6, ac.getBicicleta().getId());


            ps.executeUpdate();

            final ResultSet rs = ps.getGeneratedKeys();

            if (rs.next()) {
                final Long id = rs.getLong(1);
                ac.setId(id);
            }

        } catch (SQLException e) {
            System.err.println("Não foi possível inserir os dados!\n" + e.getMessage());
        } finally {
            fecharObjetos(null, ps, con);
        }
        return ac;
    }
}
