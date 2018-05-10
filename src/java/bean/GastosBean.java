/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bean;

import DAO.GastosDAO;
import DAO.LocaisDAO;
import DAO.ProjetosDAO;
import DAO.UsuariosDAO;
import Util.ContextJSF;
import java.io.Serializable;
import java.sql.SQLException;
import java.util.List;
import java.util.Locale;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import modelo.Gastos;
import modelo.Locais;
import modelo.Projetos;
import modelo.Usuarios;

/**
 *
 * @author Emm
 */
@Named
@SessionScoped
public class GastosBean implements Serializable {

    private static final long serialVersionUID = 1137446474255371332L;
    
    @Inject
    private Gastos gasto;
    
    @Inject
    private Locais local;
    
    @Inject
    private ContextJSF contextoJSF;
    
    @Inject
    private Usuarios usuario;
    
    @Inject
    private Gastos gastoEditado;


    private int projetoID;
    private int localID;
    //este é o usuário logado

    private boolean canEdit = false;
    private int pesquisaByMes;
    private int pesquisaByAno;
    private List<Gastos> listaGastosFiltrados;

    private boolean mostrarTabelaPesquisas = false;
    private boolean houveErro = false;
    private Integer IDUsuarioPesquisado;
    
    private List<Gastos> listaGastosTotais;
    private double gastosFiltrados;
    private double gastosTotais;
    private List<Gastos> listaGastosPesquisa;
    private boolean mostraTotal = false;

    public boolean isMostraTotal() {
        return mostraTotal;
    }

    public void setMostraTotal(boolean mostraTotal) {
        this.mostraTotal = mostraTotal;
    }

    public List<Gastos> getListaGastosFiltrados() {
        return listaGastosFiltrados;
    }

    public void setListaGastosFiltrados(List<Gastos> listaGastosFiltrados) {
        this.listaGastosFiltrados = listaGastosFiltrados;
    }

    public List<Gastos> getListaGastosTotais() throws ClassNotFoundException, SQLException {
        GastosDAO gastoDAO = new GastosDAO();
        this.listaGastosTotais = gastoDAO.findGastoEntities();
        return listaGastosTotais;

    }

    public void setListaGastosTotais(List<Gastos> listaGastosTotais) {
        this.listaGastosTotais = listaGastosTotais;
    }

    public double getGastosFiltrados() {

        return this.gastosFiltrados;
    }

    public void setGastosFiltrados(double gastosFiltrados) {
        this.gastosFiltrados = gastosFiltrados;
    }


    public Integer getIDUsuarioPesquisado() {
        return IDUsuarioPesquisado;
    }

    public void setIDUsuarioPesquisado(Integer IDUsuarioPesquisado) {
        this.IDUsuarioPesquisado = IDUsuarioPesquisado;
    }

    public boolean isHouveErro() {
        return houveErro;
    }

    public void setHouveErro(boolean houveErro) {
        this.houveErro = houveErro;
    }

    public boolean isMostrarTabelaPesquisas() {
        return mostrarTabelaPesquisas;
    }

    public void setMostrarTabelaPesquisas(boolean mostrarTabelaPesquisas) {
        this.mostrarTabelaPesquisas = mostrarTabelaPesquisas;
    }

    public List<Gastos> getListaGastosPesquisa() {
        return listaGastosPesquisa;
    }

    public void setListaGastosPesquisa(List<Gastos> listaGastosPesquisa) {
        this.listaGastosPesquisa = listaGastosPesquisa;
    }

    public int getProjetoID() {
        return projetoID;
    }

    public void setProjetoID(int projetoID) {
        this.projetoID = projetoID;
    }

    public int getPesquisaByMes() {
        return pesquisaByMes;
    }

    public void setPesquisaByMes(int pesquisaByMes) {
        this.pesquisaByMes = pesquisaByMes;
    }

    public int getPesquisaByAno() {
        return pesquisaByAno;
    }

    public void setPesquisaByAno(int pesquisaByAno) {
        this.pesquisaByAno = pesquisaByAno;
    }

    public boolean isCanEdit() {
        return canEdit;
    }

    public void setCanEdit(boolean canEdit) {
        this.canEdit = canEdit;
    }

    public Usuarios getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuarios usuario) {
        this.usuario = usuario;
    }

    public double getGastosTotais() {

        return this.gastosTotais;
    }

    public void setGastosTotais(double gastosTotais) {
        this.gastosTotais = gastosTotais;
    }

    public int getLocalID() {
        return localID;
    }

    public void setLocalID(int localID) {
        this.localID = localID;
    }

    public Locais getLocal() {
        return local;
    }

    public void setLocal(Locais local) {
        this.local = local;
    }

    public Gastos getGasto() {
        return gasto;
    }

    public void setGasto(Gastos gasto) {
        this.gasto = gasto;
    }

    public Gastos getGastoEditado() {
        return gastoEditado;
    }

    public void setGastoEditado(Gastos gastoEditado) {
        this.gastoEditado = gastoEditado;
    }

    /**
     * Creates a new instance of GastoBean
     */
    public GastosBean() {
    }

    public void limparTabelaView() {
        this.mostrarTabelaPesquisas = false;
        this.listaGastosPesquisa = null;
        this.gastosTotais = 0;

    }

    public void mostraMensagemErro(String message) {

        this.houveErro = true;
        this.contextoJSF.adicionaMensagem("erro", message);

    }

    public List<Gastos> listaGastosPesquisados() {

        if (this.listaGastosPesquisa == null) {
            this.setMostrarTabelaPesquisas(false);
        }

        return this.listaGastosPesquisa;

    }

    public void verificaGastosMes() throws ClassNotFoundException, SQLException {

        LoginBean lf = new LoginBean();
        Usuarios usuarioLogado = new Usuarios();
        usuarioLogado = lf.getUsuario();
        GastosDAO gastoDAO = new GastosDAO();

        if (usuarioLogado.getPapel().isPrivAdmin()) {

            this.gastosTotais = gastoDAO.calculaGastosMensais(this.pesquisaByMes, this.pesquisaByAno);
            this.listaGastosPesquisa = gastoDAO.listaGastosByMes(this.pesquisaByMes, this.pesquisaByAno);

        } else {

            this.gastosTotais = gastoDAO.calculaGastosMensaisUsuarioLogado(this.pesquisaByMes, this.pesquisaByAno, usuarioLogado.getIdUsuario());
            this.listaGastosPesquisa = gastoDAO.listaGastosByMesUsuarioLogado(this.pesquisaByMes, this.pesquisaByAno, usuarioLogado.getIdUsuario());
        }

        if (!houveErro) {

            this.mostrarTabelaPesquisas = true;

        }

    }

    public void verificaGastosLocal() throws ClassNotFoundException, SQLException {

        LoginBean lf = new LoginBean();
        Usuarios usuarioLogado = new Usuarios();
        usuarioLogado = lf.getUsuario();
        GastosDAO gastoDAO = new GastosDAO();

        if (usuarioLogado.getPapel().isPrivAdmin()) {

            this.gastosTotais = gastoDAO.calculaGastosByLocal(this.localID);
            this.listaGastosPesquisa = gastoDAO.listaGastosByLocal(this.localID);

        } else {
            this.gastosTotais = gastoDAO.calculaGastosByLocalUsuarioLogado(localID, usuarioLogado.getIdUsuario());
            this.listaGastosPesquisa = gastoDAO.listaGastosByLocalUsuarioLogado(this.localID, usuarioLogado.getIdUsuario());
        }

        if (!houveErro) {
            this.mostrarTabelaPesquisas = true;
        }

    }

    public void verificaGastosUsuario() throws ClassNotFoundException, SQLException {

        LoginBean lf = new LoginBean();
        Usuarios usuarioLogado = new Usuarios();
        usuarioLogado = lf.getUsuario();
        GastosDAO gastoDAO = new GastosDAO();

        if (usuarioLogado.getPapel().isPrivAdmin()) {

            this.gastosTotais = gastoDAO.calculaGastosByUsuario(this.IDUsuarioPesquisado);
            this.listaGastosPesquisa = gastoDAO.listaGastosByUsuario(this.IDUsuarioPesquisado);

        } else {
            this.gastosTotais = gastoDAO.calculaGastosByUsuario(usuarioLogado.getIdUsuario());
            this.listaGastosPesquisa = gastoDAO.listaGastosByUsuarioLogado(usuarioLogado.getIdUsuario());
        }

        this.mostrarTabelaPesquisas = true;

    }

    public void verificaGastosProjeto() throws ClassNotFoundException, SQLException {

        LoginBean lf = new LoginBean();
        Usuarios usuarioLogado = new Usuarios();
        usuarioLogado = lf.getUsuario();
        GastosDAO gastoDAO = new GastosDAO();

        if (usuarioLogado.getPapel().isPrivAdmin()) {

            this.gastosTotais = gastoDAO.calculaGastosByProjeto(this.projetoID);
            this.listaGastosPesquisa = gastoDAO.listaGastosByProjeto(this.projetoID);

        } else {
            this.gastosTotais = gastoDAO.calculaGastosByProjetoUsuarioLogado(this.projetoID, usuarioLogado.getIdUsuario());
            this.listaGastosPesquisa = gastoDAO.listaGastosByProjetoUsuarioLogado(this.projetoID, usuarioLogado.getIdUsuario());
        }

        if (!houveErro) {
            this.mostrarTabelaPesquisas = true;
        }

    }

    public void adicionarGasto() throws ClassNotFoundException, SQLException {

        GastosDAO gastoDAO = new GastosDAO();
        boolean gravado = false;

        gasto.setLocal(local);

        if (gasto.getLocal() == null & gasto.getUsuario() == null) {
            String mensagem1 = null;
            mensagem1 = "HOUVE UM PROBLEMA E O GASTO NÃO FOI GRAVADO POIS "
                    + "O LOCAL E/OU USUÁRIO ESTÃO NULOS";

            contextoJSF.adicionaMensagem("erro", mensagem1);

        } else {
            gravado = gastoDAO.criarGasto(gasto);
        }
        String mensagem = null;

        if (!gravado) {
            mensagem = "HOUVE UM PROBLEMA E O GASTO NÃO FOI GRAVADO";
            contextoJSF.adicionaMensagem("erro", mensagem);
        } else {

            mensagem = "O GASTO FOI GRAVADO COM SUCESSO";
            contextoJSF.adicionaMensagem("erro", mensagem);

        }

        //this.gasto = new Gastos();
    }

    public void gravaLocal() throws SQLException, ClassNotFoundException {

        LocaisDAO localDAO = new LocaisDAO();

        this.local = localDAO.findLocal(localID);
        this.gasto.setLocal(local);

    }

    public List<Locais> selecionaLocais() throws SQLException, ClassNotFoundException {

        LocaisDAO locaisDAO = new LocaisDAO();

        List<Locais> listaLocais = locaisDAO.findLocalEntities();

        return listaLocais;
    }

    public List<Projetos> selecionaProjetos() throws ClassNotFoundException, SQLException {

        ProjetosDAO projetoDAO = new ProjetosDAO();
        List<Projetos> listaProjetos = projetoDAO.findProjetoEntities();
        return listaProjetos;
    }

    public List<Usuarios> selecionaUsuarios() throws ClassNotFoundException, SQLException {

        UsuariosDAO usuarioDAO = new UsuariosDAO();

        List<Usuarios> listaUsuarios = usuarioDAO.consultaUsuarios();

        return listaUsuarios;
    }

    public void gravaUsuario() throws ClassNotFoundException, SQLException {

        UsuariosDAO usuarioDAO = new UsuariosDAO();

        this.usuario = usuarioDAO.findUsuario(IDUsuarioPesquisado);
        gasto.setUsuario(usuario);
    }

    public void editaGasto() throws Exception {
        LoginBean lf = new LoginBean();
        boolean possuiPrivilegio = lf.verificaPrivilegio();

        if (possuiPrivilegio) {
            this.canEdit = true;
        } else {

            String mensagem = "DESCULPE, MAS VOCÊ NÃO TEM PRIVILÉGIO DE ADMINISTRADOR PARA EXECUTAR ESTA AÇÃO!";
            contextoJSF.adicionaMensagem("alerta", mensagem);
        }

    }

    /*
             Estou recebendo neste método um gasto como parâmetro que está 
             vindo do dataTable. Ou seja, este gasto que vem como parâmetro é 
             a variável item lá do dataTable.
             
             Uma outra foma de editar seria carregar os datos modificados no 
             objeto gastoEditado desta classe aqui, através de um elemento 
             
                <h:commandButton value="SALVAR GASTO EDITADO" 
                             action="#{gastoBean.salvarGastoEditado()}"
                             rendered="#{gastoBean.canEdit}">
                             
                <f:setPropertyActionListener target="#{gastoBean.gastoEditado}"
             value="#{item}"/> 
          
            </h:commandButton>
             
             Neste caso eu não passaria o item como parâmetro do método salvarGastoEditado().
             
     */
    public void salvarGastoEditado() throws Exception {

        /*Só é necessário se eu não utilizar o 
                <f:setPropertyActionListener /> no dataTable
                Só que neste caso tenho que passar a variável do dataTable (que 
                é um Gastos, ou seja, um item da lista de gastos carregada) 
                como parâmetro do método.
                
                this.gastoEditado = gasto; 
         */
        LoginBean lf = new LoginBean();
        boolean possuiPrivilegio = lf.verificaPrivilegio();

        if (possuiPrivilegio & this.gastoEditado != null) {
            GastosDAO gastoDAO = new GastosDAO();
            gastoDAO.edit(this.gastoEditado);
            this.canEdit = false;
            this.listaGastosTotais = gastoDAO.findGastoEntities();

        }

    }

    public void deletaGasto() throws ClassNotFoundException, SQLException {

        LoginBean lf = new LoginBean();
        boolean possuiPrivilegio = lf.verificaPrivilegio();

        if (possuiPrivilegio & this.gasto != null) {

            GastosDAO gastoDAO = new GastosDAO();
            gastoDAO.destroy(this.gasto.getId_gasto());
            this.mostrarTabelaPesquisas = false;
            this.listaGastosPesquisa = null;
            this.gastosTotais = 0;

        }

    }

    public String verificaGastosTotais() throws ClassNotFoundException, ClassNotFoundException, SQLException {

        GastosDAO gastoDAO = new GastosDAO();
        LoginBean lf = new LoginBean();
        boolean isAdministrador = lf.verificaPrivilegio();
        Usuarios usuarioLogado = lf.getUsuario();

        if (isAdministrador) {
            this.listaGastosPesquisa = gastoDAO.findGastoEntities();
            this.gastosTotais = gastoDAO.calculaGastosTotais();
        } else {
            this.listaGastosPesquisa = gastoDAO.listaGastosByUsuarioLogado(usuarioLogado.getIdUsuario());
            this.gastosTotais = gastoDAO.calculaGastosTotaisUsuarioLogado(usuarioLogado.getIdUsuario());
        }
        if (!houveErro) {
            this.mostrarTabelaPesquisas = true;
        }
        return "/restricted/gastos?faces-redirect=true";
    }

    // http://respostas.guj.com.br/9399-primefaces-datatable-listener-para-calculo-apos-filtro
    //        https://groups.google.com/forum/#!topic/javasf/24reJNQo-eQ  

    /*   public void filterListener(){
        
             for (Gastos gasto : this.listaGastosFiltrados){
                
                this.gastosFiltrados += gasto.getValorGasto();
                
            }
       }

/*
        public void calcularTotaisFiltrados(List<Gasto> listaGastosFiltrada){
            
            for (Gastos gasto : this.listaGastosFiltrados){
                
                this.gastosFiltrados += gasto.getValorGasto();
                
            }
            
        }
     */
    public boolean valorEhMenor(Object valorColuna, Object filtroDigitado, Locale locale) { //java.util.Locale

        //tirando espaços do filtro
        String textoDigitado = (filtroDigitado == null) ? null : filtroDigitado.toString().trim();

        System.out.println("Filtrando pelo " + textoDigitado + ", Valor do elemento: " + valorColuna);

        // o filtro é nulo ou vazio?
        if (textoDigitado == null || textoDigitado.equals("")) {
            return true;
        }

        // elemento da tabela é nulo?
        if (valorColuna == null) {
            return false;
        }

        try {
            // fazendo o parsing do filtro para converter para Double
            Double valorDigitado = Double.valueOf(textoDigitado);
            Double valorXColuna = (Double) valorColuna;

            // comparando os valores, compareTo devolve um valor negativo se o value é menor do que o filtro
            return valorXColuna.compareTo(valorDigitado) < 0;

        } catch (NumberFormatException e) {

            // usuario nao digitou um numero
            return false;
        }

    }

    public void calculaGastos() {

        this.mostraTotal = true;

        if (this.listaGastosFiltrados != null) {

            this.gastosFiltrados = 0;
            for (Gastos gasto : this.listaGastosFiltrados) {
                this.gastosFiltrados += gasto.getValorGasto();
            }
        } else {
            this.gastosFiltrados = 0;
            for (Gastos gasto : this.listaGastosTotais) {
                this.gastosFiltrados += gasto.getValorGasto();
            }
        }

    }

}
