/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;


/**
 *
 * @author Emm
 */

public class Usuarios implements Serializable{

    private static final long serialVersionUID = 5788345956870889976L;
    
   
    private int idUsuario;
    
    private String login;
    
    private String nome;
    
    private String password;
    
     
    private String email;
    
    private Papeis papel;
    
    private List<Gastos> gastos;
   

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
    
  

    public List<Gastos> getGastos() {
        return gastos;
    }

    public void setGastos(List<Gastos> gastos) {
        this.gastos = gastos;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Papeis getPapel() {
        return papel;
    }

    public void setPapel(Papeis papel) {
        this.papel = papel;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 61 * hash + this.idUsuario;
        hash = 61 * hash + Objects.hashCode(this.login);
        hash = 61 * hash + Objects.hashCode(this.password);
        hash = 61 * hash + Objects.hashCode(this.nome);
        hash = 61 * hash + Objects.hashCode(this.papel);
        hash = 61 * hash + Objects.hashCode(this.gastos);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Usuarios other = (Usuarios) obj;
        if (this.idUsuario != other.idUsuario) {
            return false;
        }
        if (!Objects.equals(this.login, other.login)) {
            return false;
        }
        if (!Objects.equals(this.password, other.password)) {
            return false;
        }
        if (!Objects.equals(this.nome, other.nome)) {
            return false;
        }
        if (!Objects.equals(this.papel, other.papel)) {
            return false;
        }
        if (!Objects.equals(this.gastos, other.gastos)) {
            return false;
        }
        return true;
    }
    
    
    
}
