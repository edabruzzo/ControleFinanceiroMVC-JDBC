/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bean;

import DAO.PapeisDAO;
import DAO.UsuariosDAO;
import Util.CriptografarSenha;
import java.io.Serializable;
import java.sql.SQLException;
import java.util.List;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import modelo.Papeis;
import modelo.Usuarios;

/**
 *
 * @author Emm
 */

@Named
@SessionScoped
public class UsuariosBean implements Serializable{

    private static final long serialVersionUID = -7444696162507993250L;

    /**
     * Creates a new instance of UsuarioBean
     */
    public UsuariosBean() {
    }
    
    @Inject
    private Usuarios usuario;
    
    private Integer idPapel;
    
    @Inject
    private Papeis papel;
    
    private boolean canEdit = false;

    public boolean isCanEdit() {
        return canEdit;
    }

    public void setCanEdit(boolean canEdit) {
        this.canEdit = canEdit;
    }

    public Papeis getPapel() {
        return papel;
    }

    public void setPapel(Papeis papel) {
        this.papel = papel;
    }

    public Usuarios getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuarios usuario) {
        this.usuario = usuario;
    }

    public Integer getIdPapel() {
        return idPapel;
    }

    public void setIdPapel(Integer idPapel) {
        this.idPapel = idPapel;
    }
    
    
    public List<Papeis> listaPapeis() throws ClassNotFoundException, SQLException{
       
        boolean possuiPrivilegio = false;
        boolean possuiPrivilegioSuper = false;
        LoginBean lf = new LoginBean();
        possuiPrivilegio = lf.verificaPrivilegio();
        possuiPrivilegioSuper = lf.verificaPrivilegioSuperAdmin();
        
         PapeisDAO papelDAO = new PapeisDAO();
        
        if(possuiPrivilegio & !possuiPrivilegioSuper){
       
      
       return papelDAO.findPapelMenosSuper();
        
    }else if(possuiPrivilegioSuper){
        
       return papelDAO.findPapelEntities();
    }else {
        return null;
    }
        
    }
    

    
    public void criarNovoUsuario() throws Exception{
        
         boolean possuiPrivilegio = false;
        LoginBean lf = new LoginBean();
        possuiPrivilegio = lf.verificaPrivilegio();
        
        if(possuiPrivilegio){
            
        PapeisDAO papelDAO = new PapeisDAO();
        this.papel = papelDAO.findPapel(this.idPapel); 
        this.usuario.setPapel(this.papel);
            
         UsuariosDAO usuarioDAO = new UsuariosDAO();
         CriptografarSenha criptoSenha = new CriptografarSenha();
         String senhaCriptografada = criptoSenha.convertStringToMd5(this.usuario.getPassword());
         this.usuario.setPassword(senhaCriptografada);
         if(usuarioDAO.findByLogin(this.usuario.getLogin()) == null){
                 usuarioDAO.criarUsuario(this.usuario);
                 this.usuario =  new Usuarios();
         }else {
                      usuarioDAO.editarUsuario(this.usuario);
                      this.usuario =  new Usuarios();
                      this.canEdit = false;

         }
            
            
            
        }else return;
 
         
    }
    
    
    public List<Usuarios> listaUsuarios() throws ClassNotFoundException, SQLException{
         UsuariosDAO usuarioDAO = new UsuariosDAO();
         return usuarioDAO.consultaUsuarios();
    }
    
    public void deletaUsuario(Usuarios usuario) throws SQLException, ClassNotFoundException {
         UsuariosDAO usuarioDAO = new UsuariosDAO();
         usuarioDAO.removerUsuario(usuario.getIdUsuario());
        
    }
    
    public void editaUsuario(){
        
        boolean possuiPrivilegio = false;
        LoginBean lf = new LoginBean();
        possuiPrivilegio = lf.verificaPrivilegio();
        if(possuiPrivilegio){
            this.canEdit = true;
        }else return;
    
    }
    
    
    public void cancelarEdicao(){
        
           this.canEdit = false;
           this.usuario = new Usuarios();
        
    
    }
        
        
        /*
        http://respostas.guj.com.br/20826-problema-ao-tentar-setpropertyactionlistener-do-jsf-para-editar-datagrid
        
        O que eu vou te falar é muito sério, mais detalhes, só com consultoria Nunca use setPropertyActionListener

<h:commandLink value="Alterar" update=":formCliente" immediate="true">
                        <f:setPropertyActionListener value="#{cliente}"
                            target="#{clienteBean.cliente}" />
                    </h:commandLink>
faça assim

<p:commandLink value="Alterar" update=":formCliente" immediate="true" action="#{clienteBean.cliente(cliente)}"/>
        
no seu objeto faça o método que recebe o objeto cliente*/
     public void salvarUsuarioEditado() throws Exception{
        
        boolean possuiPrivilegio = false;
        LoginBean lf = new LoginBean();
        possuiPrivilegio = lf.verificaPrivilegio();
        this.canEdit = false;
        
        if(possuiPrivilegio & this.usuario != null){
                 
                    criarNovoUsuario();
        
        }else {
            return;
        }
        
        this.usuario = new Usuarios();
        this.canEdit = false;
    }
    
    
    
}
