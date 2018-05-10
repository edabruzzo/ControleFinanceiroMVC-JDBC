/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo;

import java.io.Serializable;
import java.util.List;



/**
 *
 * @author Emm
 */
public class Projetos implements Serializable {

    private static final long serialVersionUID = -7691438838266599757L;
    
    private int id_projeto;
    
    private String nome;
    
    private String status;
    
     
    private boolean ativo;
    
    private String prioridade;
    
    private List<Locais> locais;
    
  

    public int getId_projeto() {
        return id_projeto;
    }

    public void setId_projeto(int id_projeto) {
        this.id_projeto = id_projeto;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public boolean isAtivo() {
        return ativo;
    }

    public void setAtivo(boolean ativo) {
        this.ativo = ativo;
    }

    public List<Locais> getLocais() {
        return locais;
    }

    public void setLocais(List<Locais> locais) {
        this.locais = locais;
    }

    public String getPrioridade() {
        return prioridade;
    }

    public void setPrioridade(String prioridade) {
        this.prioridade = prioridade;
    }
    
    
    
}
