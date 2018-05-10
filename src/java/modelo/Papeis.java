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
public class Papeis implements Serializable {

    private static final long serialVersionUID = 1646458488277900394L;
    
    private int idPapel;

    private String descPapel;
    
    private boolean ativo = true;
    
    private boolean privAdmin = false;
    
    private boolean privSuperAdmin = false;

    public boolean isPrivSuperAdmin() {
        return privSuperAdmin;
    }

    public void setPrivSuperAdmin(boolean privSuperAdmin) {
        this.privSuperAdmin = privSuperAdmin;
    }
    
    private List<Usuarios> usuario;

    public List<Usuarios> getUsuario() {
        return usuario;
    }

    public void setUsuario(List<Usuarios> usuario) {
        this.usuario = usuario;
    }

    public int getIdPapel() {
        return idPapel;
    }

    public void setIdPapel(int idPapel) {
        this.idPapel = idPapel;
    }

    public String getDescPapel() {
        return descPapel;
    }

    public void setDescPapel(String descPapel) {
        this.descPapel = descPapel;
    }

    public boolean isAtivo() {
        return ativo;
    }

    public void setAtivo(boolean ativo) {
        this.ativo = ativo;
    }

    public boolean isPrivAdmin() {
        return privAdmin;
    }

    public void setPrivAdmin(boolean privAdmin) {
        this.privAdmin = privAdmin;
    }
    
    
}
