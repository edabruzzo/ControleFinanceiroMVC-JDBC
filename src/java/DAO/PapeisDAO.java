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
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import modelo.Papeis;

/**
 *
 * @author Emm
 */
@RequestScoped
public class PapeisDAO implements Serializable {

    private static final long serialVersionUID = 618298451758930316L;

   @Inject
    BancoDados fabrica;
  
    


    public List<Papeis> findPapelEntities() throws ClassNotFoundException, SQLException {
        
        String sql = "SELECT * FROM tb_papel;";
        
        ResultSet rs = fabrica.executaQueryResultSet(sql);
        
        return this.extrairListPapeisResultSet(rs);

    }


    
   
      public List<Papeis> findPapelMenosSuper() throws ClassNotFoundException, SQLException{
          
          String sqlString = "SELECT * FROM tb_papel WHERE PRIV_SUPERADMIN IS NOT TRUE;";
         ResultSet rs = fabrica.executaQueryResultSet(sqlString);
          
          return this.extrairListPapeisResultSet(rs);
          
          
          
      }
   
   
    public Papeis findPapel(int id) throws ClassNotFoundException, SQLException {

        String sql = "SELECT * FROM tb_papel WHERE IDPAPEL = "+id+";";
        
       ResultSet rs = fabrica.executaQueryResultSet(sql);
       Papeis papel = this.extraiPapelResultSet(rs);
       rs.close();
       
       return papel;


    }

public List<Papeis> extrairListPapeisResultSet(ResultSet rs) throws SQLException{
    
    List<Papeis> listaPapeis = new ArrayList();
    
    while(rs.next()){
        
        Papeis papel = this.extraiPapelResultSet(rs);
        listaPapeis.add(papel);
        
    }
    
    return listaPapeis;
    
}

     public Papeis extraiPapelResultSet(ResultSet rs) throws SQLException{

         Papeis papel = new Papeis();
         
         papel.setIdPapel(rs.getInt("IDPAPEL"));
         papel.setAtivo(rs.getBoolean("ATIVO"));
         papel.setDescPapel(rs.getString("DESCPAPEL"));
         papel.setPrivAdmin(rs.getBoolean("PRIVADMIN"));
         papel.setPrivSuperAdmin(rs.getBoolean("PRIV_SUPERADMIN"));
         
         return papel;

     }

    Papeis findPapelByUsuario(int idUsuario) throws SQLException, ClassNotFoundException {
        

        String sql = "SELECT * FROM tb_papel_tb_usuario  "
                + "WHERE usuario_IDUSUARIO = "+idUsuario+";";
                
        ResultSet rs = fabrica.executaQueryResultSet(sql);
        Papeis papel = this.extraiPapelResultSet(rs);
        rs.close();
        
        return papel;

    }
    
}
