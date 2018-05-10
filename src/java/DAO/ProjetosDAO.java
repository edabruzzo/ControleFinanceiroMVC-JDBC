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
import modelo.Locais;
import java.util.ArrayList;
import java.util.List;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import modelo.Projetos;

/**
 *
 * @author Emm
 */
@RequestScoped
public class ProjetosDAO implements Serializable {

    private static final long serialVersionUID = -6130058936322421816L;
    
   @Inject 
   LocaisDAO localDAO;
    
    @Inject
    BancoDados fabrica;
    
    
    
    public void create(Projetos projeto) throws SQLException, ClassNotFoundException {
       
        ArrayList<String> listaSQLs = new ArrayList();
        
        String sql1 = "INSERT INTO tb_projeto (ATIVO, NOME, PRIORIDADE, STATUS_PROJETO) "
                + "VALUES ("+projeto.isAtivo()+", '"+projeto.getNome()+"'"
                + ", '"+projeto.getStatus()+"');";
                listaSQLs.add(sql1);
                
         List<Locais> listaLocais = projeto.getLocais();
         
         for(Locais local : listaLocais){
             
             String sql2 = "INSERT INTO tb_projeto_tb_local (Projeto_ID_PROJETO, locais_ID_LOCAL) "
                     + "VALUES ("+projeto.getId_projeto()+", "+local.getId_local()+");";
             listaSQLs.add(sql2);
                          
         }
        
        fabrica.executaBatchUpdate(listaSQLs);
        
        
    }
        
        
     
    public void edit(Projetos projeto) throws SQLException, ClassNotFoundException  {
        
        
        ArrayList<String> listaSQLs = new ArrayList();
        
        String sql1 = "UPDATE tb_projeto SET ATIVO ="+projeto.isAtivo()+", "
                + "NOME = '"+projeto.getNome()
                + "',  PRIORIDADE = '"+projeto.getPrioridade()
                + "', STATUS_PROJETO = '"+projeto.getStatus()+"' "
                + "WHERE id_projeto = "+projeto.getId_projeto()+";";
        
        listaSQLs.add(sql1);

        List<Locais> listaLocaisAtuais = projeto.getLocais();

        String sql2 = "DELETE FROM tb_projeto_tb_local  "
                + "WHERE Projeto_ID_PROJETO  = "+projeto.getId_projeto()+";";
        listaSQLs.add(sql2);
        
        for(Locais local : listaLocaisAtuais){
            
            String sql3 = "INSERT INTO tb_projeto_tb_local(Projeto_ID_PROJETO, locais_ID_LOCAL) "
                    + " VALUES("+projeto.getId_projeto()+", "+local.getId_local()+");";
            listaSQLs.add(sql3);
            
        }

        fabrica.executaBatchUpdate(listaSQLs);

    }


    public void destroy(int id) throws SQLException, ClassNotFoundException {
        
        ArrayList<String> listaSQLs = new ArrayList();
        String sql = "DELETE FROM tb_local WHERE PROJETO_ID_PROJETO = "+id+";";
        listaSQLs.add(sql);
        
        String sql2 = "DELETE FROM tb_projeto_tb_local WHERE PROJETO_ID_PROJETO = "+id+";";
        listaSQLs.add(sql2);
        
        String sql3 = "DELETE FROM tb_projeto WHERE ID_PROJETO = "+id+";";
        listaSQLs.add(sql3);
        
        fabrica.executaBatchUpdate(listaSQLs);
        

    }

    public List<Projetos> findProjetoEntities() throws ClassNotFoundException, SQLException {

       
        String sql = "SELECT * FROM tb_projeto;";
        ResultSet rs = fabrica.executaQueryResultSet(sql);
        return this.extraiListaProjetosResultSet(rs);
        

    }
        
        
    public Projetos findProjeto(int id) throws ClassNotFoundException, SQLException  {

        String sql = "SELECT * FROM tb_projeto WHERE id_projeto = "+id+";";
        ResultSet rs = fabrica.executaQueryResultSet(sql);
        Projetos projeto = this.extraiProjetoResultSet(rs);
        rs.close();
        
        return projeto;

    }


       public List<Projetos> extraiListaProjetosResultSet(ResultSet rs) throws ClassNotFoundException, SQLException{

           Projetos projeto = new Projetos();
           List<Projetos> listaProjetos = new ArrayList();

           while(rs.next()){
               
           projeto = this.extraiProjetoResultSet(rs);
           listaProjetos.add(projeto);
               
            }
           
           return listaProjetos;
       }
    
    
    
        public Projetos extraiProjetoResultSet(ResultSet rs) throws ClassNotFoundException, SQLException{
            
            Projetos projeto = new Projetos();
            
            projeto.setId_projeto(rs.getInt("ID_PROJETO"));
            projeto.setNome(rs.getString("NOME"));
            projeto.setAtivo(rs.getBoolean("ATIVO"));
            projeto.setPrioridade(rs.getString("PRIORIDADE"));
            projeto.setStatus(rs.getString("STATUS_PROJETO"));
                        
            projeto.setLocais(localDAO.findLocalByProjeto(projeto.getId_projeto()));
            
            return projeto;
        }



    
}
