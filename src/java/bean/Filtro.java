/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bean;

import java.io.IOException;
import javax.inject.Inject;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author Emm
 */
public class Filtro implements Filter {

    @Inject
    LoginBean lf;

    /*
           
           https://stackoverflow.com/questions/1026846/how-to-redirect-to-login-page-when-session-is-expired-in-java-web-application
           PARA MELHORAR O PROCESSO DE LOGIN
           
           
     */
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        HttpSession sess = ((HttpServletRequest) request).getSession(false);

        if (sess != null) {
            if (lf.isPermiteAcesso() == false) {

                String contextPath = ((HttpServletRequest) request).getContextPath();

                ((HttpServletResponse) response).sendRedirect(contextPath + "login.xhtml?faces-redirect=true");
            } else {
                chain.doFilter(request, response);
            }
        }
    }
    @Override
    public void init(FilterConfig arg0) throws ServletException {
       
        System.out.println("Iniciando contexto JSF");
        
        
    }

    @Override
    public void destroy() {
        
        System.out.println("Destruindo contexto JSF no Filtro");
        
    }

}
