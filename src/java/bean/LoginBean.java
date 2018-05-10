

package bean;



import DAO.UsuariosDAO;
import Util.ContextJSF;
import Util.CriptografarSenha;
import Util.BancoDados;
import java.io.Serializable;
import java.sql.SQLException;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import modelo.Usuarios;



/**
 *
 * @author Emm
 */
@Named
@SessionScoped
public class LoginBean implements Serializable{

    private static final long serialVersionUID = -5988096083357420786L;
    


private  boolean permiteAcesso = false;



private static Usuarios usuario = new Usuarios();
  
@Inject
ContextJSF contextoJSF;

@Inject
UsuariosDAO usuarioDAO;

@Inject
BancoDados fabricaConexao;


    public boolean isPermiteAcesso() {
        return permiteAcesso;
    }

    public void setPermiteAcesso(boolean permiteAcesso) {
        this.permiteAcesso = permiteAcesso;
    }

    public Usuarios getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuarios usuario) {
        this.usuario = usuario;
    }
    
    
    
    public void criarInfraestrutura() throws ClassNotFoundException, SQLException{
        
        this.fabricaConexao.criaBaseDados();
        this.fabricaConexao.criaInfraestrutura();
        
    }
    
    
    
        public String validaAcesso() throws ClassNotFoundException, SQLException{
            
           String redireciona = "login?faces-redirect=true";
           
            
            UsuariosDAO usuarioDAO = new UsuariosDAO();
            Usuarios novoUsuario = new Usuarios();
         
            //faz a criptografia da senha entrada pelo usuário antes de 
           //gravar no banco
            CriptografarSenha criptoSenha = new CriptografarSenha();
            String senhaCriptografada = criptoSenha.convertStringToMd5(this.usuario.getPassword());
            this.usuario.setPassword(senhaCriptografada);
            novoUsuario = usuarioDAO.findByLoginSenha(this.usuario.getLogin(), this.usuario.getPassword());
 
            if(novoUsuario == null){
                    
                String mensagem = "O PROCESSO DE LOGIN FALHOU ! USUÁRIO INEXISTENTE OU SENHA INCORRETA !";
                contextoJSF.adicionaMensagem("fatal", mensagem);
                
            }else{
           
              contextoJSF.guardarUsuarioMapaSessao(this.usuario);
              this.usuario = novoUsuario;
              this.permiteAcesso = true;
                redireciona = "/restricted/gastos?faces-redirect=true";
            }   
                
             return redireciona;
            
        }
        
        
         public boolean verificaPapel() throws ClassNotFoundException, SQLException{
            
             boolean permitido = false;
             Usuarios usuarioLogado = contextoJSF.verificarUsuarioLogado();
             
             if (usuarioLogado != null & usuarioLogado.getPapel().isPrivAdmin()){
             permitido = true;
             }else{
                 
                 contextoJSF.adicionaMensagem("erro", "Você não possui privilégio");  
             }
             return permitido;
         }
         
         
         public boolean verificaPrivilegioSuperAdmin() throws ClassNotFoundException, SQLException{
             
             boolean possuiPrivilegioSuperAdmin = false;

            Usuarios usuarioLogado = contextoJSF.verificarUsuarioLogado();

             if (usuarioLogado != null & usuarioLogado.getPapel().isPrivSuperAdmin()){
             possuiPrivilegioSuperAdmin = true;
             }else{
                 
                 contextoJSF.adicionaMensagem("erro", "Você não possui privilégio");  
             }
             return possuiPrivilegioSuperAdmin;
         }
      
         
         
         public String redirecionaUsuários() throws ClassNotFoundException, SQLException{
             String redireciona = null;
             boolean permitidoRedirecionamento = verificaPapel();
             if(permitidoRedirecionamento){
                 redireciona = "/restricted/usuarios?faces-redirect=true";
             }
             return redireciona;
         }
         
         
         
             public String redirecionaGráficos() throws ClassNotFoundException, SQLException{
             String redireciona = null;
             boolean permitidoRedirecionamento = verificaPapel();
             if(permitidoRedirecionamento){
                 redireciona = "/restricted/graficos?faces-redirect=true";
             }
             return redireciona;
         }
         
         
         
                  
         public String redirecionaPesquisas() throws ClassNotFoundException, SQLException{
             String redireciona = null;
             boolean permitidoRedirecionamento = verificaPapel();
             if(permitidoRedirecionamento){
                 redireciona = "/restricted/pesquisas?faces-redirect=true";
             }
             return redireciona;
         }
         
         
           public String redirecionaLocais() throws ClassNotFoundException, SQLException{
             String redireciona = null;
             boolean permitidoRedirecionamento = verificaPapel();
             if(permitidoRedirecionamento){
                 redireciona = "/restricted/locais?faces-redirect=true";
             }
             return redireciona;
         }
           
            public String redirecionaProjetos() throws ClassNotFoundException, SQLException{
             String redireciona = null;
             boolean permitidoRedirecionamento = verificaPapel();
             if(permitidoRedirecionamento){
                 redireciona = "/restricted/projetos?faces-redirect=true";
             }
             return redireciona;
         }
         
            
            
         
          public String logout(){
            
         
             this.permiteAcesso = false;
             String redireciona = "login?faces-redirect=true";
             this.usuario = new Usuarios();
             contextoJSF.retirarUsuarioMapaSessao();
             return redireciona;
         }
          
          
          public boolean verificaPrivilegio(){
              
           boolean possuiPrivilegio = false;
         
                     
              if(this.usuario.getPapel().isPrivAdmin()){
                  possuiPrivilegio = true;
              }
              return possuiPrivilegio;
         }
          
          
            public boolean verificaUsuarioLogado() throws SQLException, ClassNotFoundException{

            Usuarios usuario = contextoJSF.verificarUsuarioLogado();   
           
             if (this.usuario.getNome() != null){
                    return true;
             }
             else return false;
            
            }
            
               public void solicitarNovaSenha() throws Exception {
       
             
             UsuariosDAO usuarioDAO = new UsuariosDAO();
             Usuarios usuarioSemSenha = usuarioDAO.findByLogin(this.usuario.getLogin());
             if(usuarioSemSenha != null){
                 
             CriptografarSenha criptoSenha = new CriptografarSenha();
             criptoSenha.gerarNovaSenha(usuarioSemSenha);
             
            String mensagem = "                                            "
                    + "                                                         "
                    + "                                       ************************"
                    + "************************************************************"
                    + "************************************************************"
                    + "                                                              "
                    + "                                                             "
                    + "NOVA SENHA ENVIADA PARA O EMAIL :"+usuarioSemSenha.getEmail()+"            "
                            + "                                                       "
                            + "                                                          "
                            + "                                                          "
                            + "                                                          "
                            + "***********************************************************"
                            + "***********************************************************"
                            + "***********************************************************";    
            contextoJSF.adicionaMensagem("alerta", mensagem);

             }
            
             
         }
      
  
  }
