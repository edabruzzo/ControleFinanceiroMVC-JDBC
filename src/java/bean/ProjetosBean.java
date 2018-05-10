/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bean;

import DAO.ProjetosDAO;
import java.io.Serializable;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import modelo.Projetos;

/**
 *
 * @author Emm
 */
@Named
@SessionScoped
public class ProjetosBean implements Serializable{

    private static final long serialVersionUID = 8173294256756299119L;

    /**
     * Creates a new instance of ProjetoBean
     */
    public ProjetosBean() {
    }
    @Inject
    private Projetos projeto;

    public Projetos getProjeto() {
        return projeto;
    }

    public void setProjeto(Projetos projeto) {
        this.projeto = projeto;
    }
    
    
    public void criarNovoProjeto() throws SQLException, ClassNotFoundException{
        
        ProjetosDAO projetoDAO = new ProjetosDAO();
        this.projeto.setAtivo(true);
        projetoDAO.create(this.projeto);
        
    }
    
    
    public List<Projetos> listaProjetos() throws ClassNotFoundException, SQLException{
        
    ProjetosDAO projetoDAO = new ProjetosDAO();
    List<Projetos> listaProjetos = new ArrayList();
    
    listaProjetos = projetoDAO.findProjetoEntities();
    return listaProjetos;
        
    }
    
    
    
    
    
    
    
}
