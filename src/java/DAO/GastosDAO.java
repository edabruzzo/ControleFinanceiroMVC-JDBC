/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DAO;

import Util.BancoDados;
import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import modelo.Gastos;
import modelo.Locais;
import modelo.Usuarios;

/**
 *
 * @author Emm
 */
@RequestScoped
public class GastosDAO implements Serializable {

    private static final long serialVersionUID = 8185213676019293546L;

    
    @Inject
    BancoDados fabrica;
    
    @Inject
    UsuariosDAO usuarioDAO;

    @Inject
    LocaisDAO localDAO;

    public boolean criarGasto(Gastos gasto) throws ClassNotFoundException, SQLException {

        String sql1 = "INSERT INTO tb_gasto (DATAGASTO, "
                + "MODALIDADEPAGAMENTO, TIPOGASTO, VALORGASTO, "
                + "USUARIO_IDUSUARIO, LOCAL_ID_LOCAL) "
                + "VALUES ('" + gasto.getDataGasto()
                + "', '" + gasto.getModalidadePagamento()
                + "', " + gasto.getTipoGasto()
                + "', " + gasto.getValorGasto()
                + " , " + gasto.getUsuario().getIdUsuario()
                + " , " + gasto.getLocal().getId_local();

        try {
            fabrica.executaQuerieUpdate(sql1);
        } catch (SQLException ex) {
            Logger.getLogger(GastosDAO.class.getName()).log(Level.SEVERE, null, ex);

            return false;
        }

        String sql2 = "INSERT INTO tb_usuario_tb_gasto (Usuario_IDUSUARIO, gastos_ID_GASTO) "
                + "VALUES (" + gasto.getUsuario().getIdUsuario()
                + " , (SELECT last_insert_id() FROM tb_gasto));";

        fabrica.executaQuerieUpdate(sql2);

        String sql3 = "INSERT INTO tb_usuario_tb_gasto (Usuario_IDUSUARIO, gastos_ID_GASTO) "
                + "VALUES (" + gasto.getUsuario().getIdUsuario()
                + " , (SELECT last_insert_id() FROM tb_gasto));";

        fabrica.executaQuerieUpdate(sql3);

        String sql4 = "INSERT INTO tb_local_tb_gasto (Local_ID_LOCAL, gastos_ID_GASTO) "
                + "VALUES ((SELECT last_insert_id() FROM tb_gasto), " + gasto.getLocal().getId_local()
                + " );";

        fabrica.executaQuerieUpdate(sql4);

        return true;

    }

    public void edit(Gastos gasto) throws SQLException, ClassNotFoundException {

        ArrayList<String> listaSQLs = new ArrayList();

        String sql1 = "UPDATE tb_gasto "
                + " SET DATAGASTO = '" + gasto.getDataGasto()
                + "', MODALIDADEPAGAMENTO = '" + gasto.getModalidadePagamento()
                + "', TIPOGASTO = '" + gasto.getTipoGasto()
                + "', VALORGASTO = " + gasto.getValorGasto()
                + ", USUARIO_IDUSUARIO = " + gasto.getUsuario().getIdUsuario()
                + ", LOCAL_ID_LOCAL = " + gasto.getLocal().getId_local()
                + "WHERE ID_GASTO = " + gasto.getId_gasto() + ";";

        listaSQLs.add(sql1);

        String sql2 = "UPDATE tb_usuario_tb_gasto "
                + " SET USUARIO_IDUSUARIO = " + gasto.getUsuario().getIdUsuario()
                + " WHERE GASTOS_ID_GASTO = " + gasto.getId_gasto() + " ;";

        listaSQLs.add(sql2);

        String sql3 = "UPDATE tb_local_tb_gasto "
                + " SET Local_ID_LOCAL = " + gasto.getLocal().getId_local()
                + " WHERE gastos_ID_GASTO = " + gasto.getId_gasto() + ";";

        listaSQLs.add(sql3);

        fabrica.executaBatchUpdate(listaSQLs);

    }

    public void destroy(int id) throws ClassNotFoundException, SQLException {

        ArrayList<String> listaSQLs = new ArrayList();

        String sql1 = "DELETE FROM tb_usuario_tb_gasto WHERE gastos_ID_GASTO = "
                + id + ";";
        listaSQLs.add(sql1);

        String sql2 = "DELETE FROM tb_local_tb_gasto WHERE gastos_ID_GASTO = "
                + id + ";";

        listaSQLs.add(sql2);

        String sql3 = "DELETE FROM tb_gasto WHERE id_gasto = " + id + ";";

        listaSQLs.add(sql3);

        fabrica.executaBatchUpdate(listaSQLs);

    }

    public List<Gastos> findGastoEntities() throws ClassNotFoundException, SQLException {

        String sql = "SELECT * FROM tb_gasto;";

        ResultSet rs = fabrica.executaQueryResultSet(sql);
 
        return this.extrairListaGastosResultSet(rs);
    }

    public Gastos findGasto(int id) throws ClassNotFoundException, SQLException {

        String sql = "SELECT * FROM tb_gasto WHERE id = " + id + ";";

        ResultSet rs = fabrica.executaQueryResultSet(sql);
        Gastos gasto = extraiGastoResultSet(rs);
        rs.close();

        return gasto;
    }

    public Double calculaGastosByLocalUsuarioLogado(Integer localID, Integer idUsuario) throws ClassNotFoundException, SQLException {

        String sqlString = "SELECT sum(g.VALORGASTO) FROM tb_gasto g "
                + " WHERE g.LOCAL_ID_LOCAL = " + localID + " "
                + " AND USUARIO_IDUSUARIO = " + idUsuario + ";";
        ResultSet rs = fabrica.executaQueryResultSet(sqlString);
        Double resultado = rs.getDouble(1);
        rs.close();
        return resultado;

    }

    public double calculaGastosByLocal(Integer localID) throws ClassNotFoundException, SQLException {

        String sqlString = "SELECT sum(g.VALORGASTO) FROM tb_gasto g "
                + " WHERE g.LOCAL_ID_LOCAL = " + localID + ";";
        ResultSet rs = fabrica.executaQueryResultSet(sqlString);
        Double resultado = rs.getDouble(1);
        rs.close();
        return resultado;

    }

    public List<Gastos> listaGastosByLocal(Integer localID) throws ClassNotFoundException, SQLException {

        String sqlString = "SELECT g.* FROM tb_gasto g "
                + " WHERE g.LOCAL_ID_LOCAL = " + localID + " ORDER BY DATAGASTO DESC;";

        ResultSet rs = fabrica.executaQueryResultSet(sqlString);

        return extrairListaGastosResultSet(rs);

    }

    public List<Gastos> listaGastosByUsuario(Integer idUsuario) throws ClassNotFoundException, SQLException {
        

        String sqlString = "SELECT g.* FROM tb_gasto g "
                + " WHERE g.USUARIO_IDUSUARIO = " + idUsuario + " ORDER BY DATAGASTO DESC;";
        
        ResultSet rs = fabrica.executaQueryResultSet(sqlString);

       return extrairListaGastosResultSet(rs);
       
    }

    public List<Gastos> listaGastosByUsuarioLogado(Integer idUsuario) throws ClassNotFoundException, SQLException {
        
        
        String sqlString = "SELECT g.* FROM tb_gasto g "
                + " WHERE g.USUARIO_IDUSUARIO = " + idUsuario + " ORDER BY DATAGASTO DESC;";
        
       ResultSet rs = fabrica.executaQueryResultSet(sqlString);
        return extrairListaGastosResultSet(rs);

    }

    public double calculaGastosByUsuario(Integer usuarioID) throws ClassNotFoundException, SQLException {
       
        String sqlString =  "SELECT sum(g.VALORGASTO) FROM tb_gasto g "
                + " WHERE g.USUARIO_IDUSUARIO = " + usuarioID + ";";
        
        ResultSet rs = fabrica.executaQueryResultSet(sqlString);
        Double resultado = rs.getDouble(1);
        rs.close();
        return resultado;
    }

   
    public List<Gastos> listaGastosByProjetoUsuarioLogado(Integer projetoID, Integer idUsuario) throws ClassNotFoundException, SQLException {
    
       
        String sqlString = "SELECT g.* FROM tb_gasto g "
                + "INNER JOIN tb_projeto_tb_local pl ON g.LOCAL_ID_LOCAL = pl.locais_ID_LOCAL "
                + "INNER JOIN tb_projeto p ON pl.Projeto_ID_PROJETO = p.ID_PROJETO "
                + " WHERE p.ID_PROJETO = " + projetoID + " "
                + " AND g.USUARIO_IDUSUARIO = " + idUsuario + " ORDER BY DATAGASTO DESC;";
        
        ResultSet rs = fabrica.executaQueryResultSet(sqlString);
        
        return this.extrairListaGastosResultSet(rs);
    }

    public List<Gastos> listaGastosByProjeto(Integer projetoID) throws ClassNotFoundException, SQLException {

        String sqlString = "SELECT g.* FROM tb_gasto g "
                + "INNER JOIN tb_projeto_tb_local pl ON g.LOCAL_ID_LOCAL = pl.locais_ID_LOCAL "
                + "INNER JOIN tb_projeto p ON pl.Projeto_ID_PROJETO = p.ID_PROJETO "
                + " WHERE p.ID_PROJETO = " + projetoID + ";";

        ResultSet rs = fabrica.executaQueryResultSet(sqlString);

        return this.extrairListaGastosResultSet(rs);
    }

    
    public double calculaGastosByProjeto(Integer projetoID) throws ClassNotFoundException, SQLException {

        String sqlString = "SELECT sum(g.VALORGASTO) FROM tb_gasto g "
                + "INNER JOIN tb_projeto_tb_local pl ON g.LOCAL_ID_LOCAL = pl.locais_ID_LOCAL "
                + "INNER JOIN tb_projeto p ON pl.Projeto_ID_PROJETO = p.ID_PROJETO "
                + " WHERE p.ID_PROJETO = " + projetoID + ";";

        ResultSet rs = fabrica.executaQueryResultSet(sqlString);
        Double resultado = rs.getDouble(1);
        rs.close();
        return resultado;

    }

    
    public double calculaGastosByProjetoUsuarioLogado(Integer projetoID, Integer idUsuario) throws ClassNotFoundException, SQLException {
        
        String sqlString = "SELECT sum(g.VALORGASTO) FROM tb_gasto g "
                + "INNER JOIN tb_projeto_tb_local pl ON g.LOCAL_ID_LOCAL = pl.locais_ID_LOCAL "
                + "INNER JOIN tb_projeto p ON pl.Projeto_ID_PROJETO = p.ID_PROJETO "
                + " WHERE p.ID_PROJETO = " + projetoID + " "
                + " AND g.USUARIO_IDUSUARIO = " + idUsuario + ";";
        
        ResultSet rs = fabrica.executaQueryResultSet(sqlString);
        Double resultado = rs.getDouble(1);
        rs.close();
        return resultado;

    }

    public List<Gastos> listaGastosByMesUsuarioLogado(Integer month, Integer year, Integer idUsuario) throws ClassNotFoundException, SQLException {

        String sqlString = "SELECT g.* FROM tb_gasto g "
                + "WHERE USUARIO_IDUSUARIO = " + idUsuario
                + " AND g.DATAGASTO BETWEEN '" + year + "-" + month + "-01' AND '" + year + "-" + month + "-31' "
                + "ORDER BY g.DATAGASTO DESC;";
                
        ResultSet rs = fabrica.executaQueryResultSet(sqlString);
        return this.extrairListaGastosResultSet(rs);


    }

    public List<Gastos> listaGastosByMes(Integer month, Integer year) throws ClassNotFoundException, SQLException {

        String sqlString = "SELECT g.* FROM tb_gasto g "
                + " WHERE g.DATAGASTO BETWEEN '" 
                + year + "-" + month + "-01' AND '" + year + "-" + month + "-31' "
                + " ORDER BY DATAGASTO DESC;";

        ResultSet rs = fabrica.executaQueryResultSet(sqlString);
        return this.extrairListaGastosResultSet(rs);
        

    }

    public double calculaGastosMensais(Integer month, Integer year) throws ClassNotFoundException, SQLException {

        String sqlString = "SELECT sum(g.VALORGASTO) FROM tb_gasto g "
                + " WHERE g.DATAGASTO BETWEEN '" + year + "-" + month + "-01' AND '" + year + "-" + month + "-31';";

       ResultSet rs = fabrica.executaQueryResultSet(sqlString);
       Double resultado = rs.getDouble(1);
       rs.close();
       
       return resultado;

    }

    public double calculaGastosTotais() throws ClassNotFoundException, SQLException {

        String sqlString = "SELECT sum(g.VALORGASTO) FROM tb_gasto g ;";
       ResultSet rs = fabrica.executaQueryResultSet(sqlString);
       Double resultado = rs.getDouble(1);
       rs.close();
       
       return resultado;



    }

    public double calculaGastosTotaisUsuarioLogado(Integer idUsuario) throws ClassNotFoundException, SQLException {

        String sqlString = "SELECT sum(g.VALORGASTO) FROM tb_gasto g WHERE "
                + " USUARIO_IDUSUARIO = " + idUsuario + ";";
        
       ResultSet rs = fabrica.executaQueryResultSet(sqlString);
       Double resultado = rs.getDouble(1);
       rs.close();
       
       return resultado;

    }

    public List<Gastos> listaGastosByLocalUsuarioLogado(Integer localID, Integer idUsuario) throws ClassNotFoundException, SQLException {


        String sqlString = "SELECT g.* FROM tb_gasto g "
                + "WHERE LOCAL_ID_LOCAL = " + localID + " AND USUARIO_IDUSUARIO = "
                + idUsuario + " ORDER BY DATAGASTO DESC;";
       ResultSet rs = fabrica.executaQueryResultSet(sqlString);
       
       return this.extrairListaGastosResultSet(rs);

    }

    public double calculaGastosMensaisUsuarioLogado(Integer month, Integer year, Integer idUsuario) throws SQLException, ClassNotFoundException {

        String sqlString = "SELECT sum(g.VALORGASTO) FROM tb_gasto g WHERE "
                + " DATAGASTO BETWEEN '" + year + "-" + month + "-01' AND '" + year + "-" + month + "-31' "
                + " AND g.USUARIO_IDUSUARIO = " + idUsuario + ";";

        ResultSet rs = fabrica.executaQueryResultSet(sqlString);
        
        Double resultado = rs.getDouble(1);
        rs.close();
        return resultado;
    }
    
    public Gastos extraiGastoResultSet(ResultSet rs) throws SQLException, ClassNotFoundException {

        Gastos gasto = new Gastos();
        Usuarios usuario = new Usuarios();
        Locais local = new Locais();
        gasto.setId_gasto(rs.getInt("id_gasto"));
        gasto.setDataGasto(rs.getDate("DATAGASTO"));
        gasto.setModalidadePagamento(rs.getString("MODALIDADEPAGAMENTO"));
        gasto.setTipoGasto(rs.getString("TIPOGASTO"));
        gasto.setValorGasto(rs.getDouble("VALORGASTO"));

        usuario = usuarioDAO.findUsuario(rs.getInt("USUARIO_IDUSUARIO"));
        local = localDAO.findLocal(rs.getInt("LOCAL_ID_LOCAL"));

        gasto.setUsuario(usuario);
        gasto.setLocal(local);

        return gasto;

    }

    public List<Gastos> extrairListaGastosResultSet(ResultSet rs) throws SQLException, ClassNotFoundException {

        List<Gastos> listaGastos = new ArrayList();

        while (rs.next()) {
            listaGastos.add(extraiGastoResultSet(rs));
        }
        rs.close();

        return listaGastos;

    }

}
