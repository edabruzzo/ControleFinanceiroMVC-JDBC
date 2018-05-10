/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bean;

import DAO.LocaisDAO;
import DAO.ProjetosDAO;
import java.io.Serializable;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import modelo.Locais;
import modelo.Projetos;

/**
 *
 * @author Emm
 */
@Named
@SessionScoped
public class LocaisBean implements Serializable{

    private static final long serialVersionUID = -716678353134143011L;

    /**
     * Creates a new instance of LocalBean
     */
    public LocaisBean() {
    }
    
    private int projetoID;
    private Projetos projeto = new Projetos();
    private Locais local = new Locais();

    public Locais getLocal() {
        return local;
    }

    public void setLocal(Locais local) {
        this.local = local;
    }

    public Projetos getProjeto() {
        return projeto;
    }

    public void setProjeto(Projetos projeto) {
        this.projeto = projeto;
    }
    

    public int getProjetoID() {
        return projetoID;
    }

    public void setProjetoID(int projetoID) {
        this.projetoID = projetoID;
    }
    
    
    public List<Projetos> listaProjetos() throws ClassNotFoundException, SQLException{
        
        ProjetosDAO projetoDAO = new ProjetosDAO();
         List<Projetos> listaProjetos = new ArrayList();
         listaProjetos = projetoDAO.findProjetoEntities();
         return listaProjetos;
        
    }
    
    public void gravaProjetoNoLocal() throws ClassNotFoundException, SQLException{
         ProjetosDAO projetoDAO = new ProjetosDAO();
         this.projeto = projetoDAO.findProjeto(projetoID);
         this.local.setProjeto(this.projeto);
    }
    
    
    public void criaNovoLocal() throws SQLException, ClassNotFoundException{
        
        LocaisDAO localDAO = new LocaisDAO();
        localDAO.create(this.local);
        
    }
    
    
    public List<Locais> listaLocais() throws SQLException, ClassNotFoundException{
          LocaisDAO localDAO = new LocaisDAO();
        List<Locais> listaLocais = new ArrayList();  
        listaLocais = localDAO.findLocalEntities();
        return listaLocais;
    }
   
    
    
}
