/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;

/**
 *
 * @author Emm
 */
@RequestScoped
public class BancoDados {
    
    
    private String URL;

    @Inject
    private ContextJSF contextoJSF;

    @Inject
    private CriptografarSenha cripto;

    public void criaInfraestrutura() throws SQLException, ClassNotFoundException {

        String senhaCriptograda = cripto.convertStringToMd5("SUPERADMIN");

        ArrayList<String> listaSQLs = new ArrayList();

        String sql1 = "CREATE TABLE IF NOT EXISTS tb_gasto (ID_GASTO INTEGER AUTO_INCREMENT NOT NULL, "
                + "DATAGASTO DATE NOT NULL, MODALIDADEPAGAMENTO VARCHAR(255) NOT NULL, TIPOGASTO VARCHAR(255) NOT NULL, VALORGASTO DOUBLE NOT NULL, USUARIO_IDUSUARIO INTEGER, LOCAL_ID_LOCAL INTEGER, PRIMARY KEY (ID_GASTO));";
        listaSQLs.add(sql1);

        String sql2 = "CREATE TABLE IF NOT EXISTS tb_local (ID_LOCAL INTEGER AUTO_INCREMENT NOT NULL, NOME VARCHAR(255) NOT NULL UNIQUE, PROJETO_ID_PROJETO INTEGER, PRIMARY KEY (ID_LOCAL));";
        listaSQLs.add(sql2);

        String sql3 = "CREATE TABLE IF NOT EXISTS tb_projeto (ID_PROJETO INTEGER AUTO_INCREMENT NOT NULL, ATIVO TINYINT(1) default 0, NOME VARCHAR(255) NOT NULL UNIQUE, PRIORIDADE VARCHAR(255) NOT NULL, STATUS_PROJETO VARCHAR(255) NOT NULL, PRIMARY KEY (ID_PROJETO));";
        listaSQLs.add(sql3);

        String sql4 = "CREATE TABLE IF NOT EXISTS tb_papel (IDPAPEL INTEGER AUTO_INCREMENT NOT NULL, ATIVO TINYINT(1) default 0, DESCPAPEL VARCHAR(255) NOT NULL, PRIVADMIN TINYINT(1) default 0, PRIV_SUPERADMIN TINYINT(1) default 0, PRIMARY KEY (IDPAPEL));";
        listaSQLs.add(sql4);

        String sql5 = "CREATE TABLE IF NOT EXISTS tb_usuario (IDUSUARIO INTEGER AUTO_INCREMENT NOT NULL, email VARCHAR(255) NOT NULL, LOGIN VARCHAR(255) NOT NULL UNIQUE, NOME VARCHAR(255) NOT NULL, PASSWORD VARCHAR(255) NOT NULL UNIQUE, PAPEL_IDPAPEL INTEGER, PRIMARY KEY (IDUSUARIO));";
        listaSQLs.add(sql5);

        String sql6 = "CREATE TABLE IF NOT EXISTS tb_local_tb_gasto (Local_ID_LOCAL INTEGER NOT NULL, gastos_ID_GASTO INTEGER NOT NULL, PRIMARY KEY (Local_ID_LOCAL, gastos_ID_GASTO));";
        listaSQLs.add(sql6);

        String sql7 = "CREATE TABLE IF NOT EXISTS tb_projeto_tb_local (Projeto_ID_PROJETO INTEGER NOT NULL, locais_ID_LOCAL INTEGER NOT NULL, PRIMARY KEY (Projeto_ID_PROJETO, locais_ID_LOCAL));";
        listaSQLs.add(sql7);

        String sql8 = "CREATE TABLE IF NOT EXISTS tb_papel_tb_usuario (Papel_IDPAPEL INTEGER NOT NULL, usuario_IDUSUARIO INTEGER NOT NULL, PRIMARY KEY (Papel_IDPAPEL, usuario_IDUSUARIO));";
        listaSQLs.add(sql8);

        String sql9 = "CREATE TABLE IF NOT EXISTS tb_usuario_tb_gasto (Usuario_IDUSUARIO INTEGER NOT NULL, gastos_ID_GASTO INTEGER NOT NULL, PRIMARY KEY (Usuario_IDUSUARIO, gastos_ID_GASTO));";
        listaSQLs.add(sql9);

        String sql10 = "ALTER TABLE tb_gasto ADD CONSTRAINT FK_tb_gasto_LOCAL_ID_LOCAL FOREIGN KEY (LOCAL_ID_LOCAL) REFERENCES tb_local (ID_LOCAL);";
        listaSQLs.add(sql10);

        String sql11 = "ALTER TABLE tb_gasto ADD CONSTRAINT FK_tb_gasto_USUARIO_IDUSUARIO FOREIGN KEY (USUARIO_IDUSUARIO) REFERENCES tb_usuario (IDUSUARIO);";
        listaSQLs.add(sql11);

        String sql12 = "ALTER TABLE tb_local ADD CONSTRAINT FK_tb_local_PROJETO_ID_PROJETO FOREIGN KEY (PROJETO_ID_PROJETO) REFERENCES tb_projeto (ID_PROJETO);";
        listaSQLs.add(sql12);

        String sql13 = "ALTER TABLE tb_usuario ADD CONSTRAINT FK_tb_usuario_PAPEL_IDPAPEL FOREIGN KEY (PAPEL_IDPAPEL) REFERENCES tb_papel (IDPAPEL);";
        listaSQLs.add(sql13);

        String sql14 = "ALTER TABLE tb_local_tb_gasto ADD CONSTRAINT FK_tb_local_tb_gasto_gastos_ID_GASTO FOREIGN KEY (gastos_ID_GASTO) REFERENCES tb_gasto (ID_GASTO);";
        listaSQLs.add(sql14);

        String sql15 = "ALTER TABLE tb_local_tb_gasto ADD CONSTRAINT FK_tb_local_tb_gasto_Local_ID_LOCAL FOREIGN KEY (Local_ID_LOCAL) REFERENCES tb_local (ID_LOCAL);";
        listaSQLs.add(sql15);

        String sql16 = "ALTER TABLE tb_projeto_tb_local ADD CONSTRAINT FK_tb_projeto_tb_local_Projeto_ID_PROJETO FOREIGN KEY (Projeto_ID_PROJETO) REFERENCES tb_projeto (ID_PROJETO);";
        listaSQLs.add(sql16);

        String sql17 = "ALTER TABLE tb_projeto_tb_local ADD CONSTRAINT FK_tb_projeto_tb_local_locais_ID_LOCAL FOREIGN KEY (locais_ID_LOCAL) REFERENCES tb_local (ID_LOCAL);";
        listaSQLs.add(sql17);

        String sql18 = "ALTER TABLE tb_papel_tb_usuario ADD CONSTRAINT FK_tb_papel_tb_usuario_Papel_IDPAPEL FOREIGN KEY (Papel_IDPAPEL) REFERENCES tb_papel (IDPAPEL);";
        listaSQLs.add(sql18);

        String sql19 = "ALTER TABLE tb_papel_tb_usuario ADD CONSTRAINT FK_tb_papel_tb_usuario_usuario_IDUSUARIO FOREIGN KEY (usuario_IDUSUARIO) REFERENCES tb_usuario (IDUSUARIO);";
        listaSQLs.add(sql19);

        String sql20 = "ALTER TABLE tb_usuario_tb_gasto ADD CONSTRAINT FK_tb_usuario_tb_gasto_Usuario_IDUSUARIO FOREIGN KEY (Usuario_IDUSUARIO) REFERENCES tb_usuario (IDUSUARIO);";
        listaSQLs.add(sql20);

        String sql21 = "ALTER TABLE tb_usuario_tb_gasto ADD CONSTRAINT FK_tb_usuario_tb_gasto_gastos_ID_GASTO FOREIGN KEY (gastos_ID_GASTO) REFERENCES tb_gasto (ID_GASTO);";
        listaSQLs.add(sql21);

        String sql22 = "INSERT INTO tb_papel (ATIVO, DESCPAPEL, PRIVADMIN, PRIV_SUPERADMIN) VALUES (true, 'SUPER_ADMINISTRADOR', true, true);";
        listaSQLs.add(sql22);

        String sql23 = "INSERT INTO tb_papel (ATIVO, DESCPAPEL, PRIVADMIN, PRIV_SUPERADMIN) VALUES (true, 'ADMINISTRADOR', true, false);";
        listaSQLs.add(sql23);

        String sql24 = "INSERT INTO tb_papel (ATIVO, DESCPAPEL, PRIVADMIN, PRIV_SUPERADMIN) VALUES (true, 'USUÁRIO', false, false);";
        listaSQLs.add(sql24);

        String sql25 = "INSERT INTO tb_usuario (email, LOGIN, NOME, PASSWORD, PAPEL_IDPAPEL) "
                + "VALUES ('xxx@gmail.com', 'SUPERADMIN', 'Administrador', '" + senhaCriptograda + "' , 1)";
        listaSQLs.add(sql25);

        String sql26 = "INSERT INTO tb_papel_tb_usuario (usuario_IDUSUARIO, Papel_IDPAPEL) VALUES (1, 1)";
        listaSQLs.add(sql26);

        executaBatchUpdate(listaSQLs);

    }

    public void criaBaseDados() throws ClassNotFoundException, SQLException {

        Class.forName("com.mysql.jdbc.Driver");
        String USER = "root";
        String PASSWORD = "root";
        this.setURL("jdbc:mysql://localhost:3306/");

        Connection conn = null;
        Statement stmt = null;
        //STEP 3: Open a connection
        System.out.println("Conectando ao servidor com a seguinte URL : " + this.URL);

        try {
            conn = DriverManager.getConnection(URL, USER, PASSWORD);
            stmt = conn.createStatement();
            conn.setAutoCommit(false);
            stmt.execute("CREATE DATABASE IF NOT EXISTS controleFinanceiroUNINOVE");
            conn.commit();
        } catch (SQLException ex) {
            conn.rollback();
            System.out.println(stmt.getWarnings().getMessage());
            Logger.getLogger(BancoDados.class.getName()).log(Level.SEVERE, null, ex);
        } finally {

            fecharConexao(conn);
            stmt.close();

        }

    }

    public Connection criaConexao() throws ClassNotFoundException {

        Class.forName("com.mysql.jdbc.Driver");
        String USER = "root";
        String PASSWORD = "root";
        this.setURL("jdbc:mysql://localhost:3306/controleFinanceiroUNINOVE");

        Connection conn = null;
        //STEP 3: Open a connection
        System.out.println("Conectando ao servidor com a seguinte URL : " + URL);

        try {
            conn = DriverManager.getConnection(URL, USER, PASSWORD);
            contextoJSF.adicionaMensagem("sucesso", "CONEXÃO CRIADA COM SUCESSO!");
        } catch (SQLException ex) {

            contextoJSF.adicionaMensagem("fatal", "CONEXÃO NÃO ESTABELECIDA");

            Logger.getLogger(BancoDados.class.getName()).log(Level.SEVERE, null, ex);
        }

        return conn;

    }

    public void fecharConexao(Connection conn) {

        System.out.println("Fechando a conexão com o banco de dados");
        try {
            if (conn != null) {
                conn.close();

                System.out.println("Conexão com o banco de dados fechada com sucesso");
            }

        } catch (SQLException ex) {
            Logger.getLogger(BancoDados.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("********ATENÇÃO ! Conexão com o banco de dados não foi fechada !");

        }
    }

    public ResultSet executaQueryResultSet(String sql) throws ClassNotFoundException, SQLException {
        
      
        Connection conn = criaConexao();
       Statement stmt = conn.createStatement();

        ResultSet rs = null;
        try {

        
            contextoJSF.adicionaMensagem("alerta", "Executando a query .....");
            System.out.println(sql);
            rs = stmt.executeQuery(sql);
            contextoJSF.adicionaMensagem("sucesso", "Executada com sucesso!");

        } catch (SQLException ex) {
            System.out.println(stmt.getWarnings().getMessage());
            contextoJSF.adicionaMensagem("erro", "Query não executada!");
            Logger.getLogger(BancoDados.class.getName()).log(Level.SEVERE, null, ex);
        } finally {

            rs.close();
            stmt.close();
            fecharConexao(conn);

        }

        return rs;

    }

    public void executaQuerieSemResultado(String sql) throws ClassNotFoundException, SQLException {

        Connection conn = criaConexao();
        Statement stmt = conn.createStatement();

        try {
            contextoJSF.adicionaMensagem("alerta", "Executando a seguinte query .....");
            System.out.println(sql);
            stmt.execute(sql);
            contextoJSF.adicionaMensagem("sucesso", "Executada com sucesso!");

        } catch (SQLException ex) {
            System.out.println(stmt.getWarnings().getMessage());
            contextoJSF.adicionaMensagem("erro", "Query não executada!");
            Logger.getLogger(BancoDados.class.getName()).log(Level.SEVERE, null, ex);
        } finally {

            stmt.close();
            fecharConexao(conn);

        }

    }

    public void executaQuerieUpdate(String sql) throws ClassNotFoundException, SQLException {

        Connection conn = criaConexao();
        Statement stmt = conn.createStatement();
        conn.setAutoCommit(false);

        try {
            contextoJSF.adicionaMensagem("alerta", "Executando commit da seguinte query .....");
            System.out.println(sql);
            stmt.executeUpdate(sql);
            conn.commit();
            contextoJSF.adicionaMensagem("sucesso", "Executada com sucesso!");

        } catch (SQLException ex) {
            System.out.println(stmt.getWarnings().getMessage());
            contextoJSF.adicionaMensagem("erro", "Query não executada! Efetuando rollback");
            conn.rollback();
            Logger.getLogger(BancoDados.class.getName()).log(Level.SEVERE, null, ex);

        } finally {

            stmt.close();
            fecharConexao(conn);

        }

    }

    public String getURL() {
        return URL;
    }

    public void setURL(String URL) {
        this.URL = URL;
    }

    public void executaBatchUpdate(ArrayList listaSQLs) throws SQLException, ClassNotFoundException {

        Connection conn = criaConexao();
        Statement stmt = conn.createStatement();
        conn.setAutoCommit(false);
        for (int i = 0; i < listaSQLs.size(); i++) {
            System.out.println(listaSQLs.get(i));
            stmt.addBatch((String) listaSQLs.get(i));

        }

        try {
            contextoJSF.adicionaMensagem("alerta", "Executando commit da seguinte query .....");
            stmt.executeBatch();
            conn.commit();
            contextoJSF.adicionaMensagem("sucesso", "Executada com sucesso!");

        } catch (SQLException ex) {

            contextoJSF.adicionaMensagem("erro", "Query não executada! Efetuando rollback");
            conn.rollback();
            Logger.getLogger(BancoDados.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println(stmt.getWarnings().getMessage());

        } finally {

            stmt.close();
            fecharConexao(conn);
        }
    }

}
