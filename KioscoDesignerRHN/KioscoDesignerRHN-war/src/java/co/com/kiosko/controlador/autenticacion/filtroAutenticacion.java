package co.com.kiosko.controlador.autenticacion;

import java.io.IOException;
import java.io.Serializable;
import javax.faces.application.ResourceHandler;
import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author Felipe Triviño
 */
@WebFilter(filterName = "filtroAutenticacion", urlPatterns = {"/faces/*"})
public class filtroAutenticacion implements Filter, Serializable {

    public filtroAutenticacion() {
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        try {

            HttpServletRequest req = (HttpServletRequest) request;
            HttpServletResponse res = (HttpServletResponse) response;

            HttpSession ses = req.getSession(false);
            String reqURI = req.getRequestURI();

            /*if (reqURI.equals(req.getContextPath() + "/") || reqURI.indexOf("/faces/Ingreso/ingreso.xhtml") >= 0 || (ses != null && ses.getAttribute("idUsuario") != null)
                    || reqURI.indexOf("/public/") >= 0 || reqURI.contains("javax.faces.resource")) {*/
            if (reqURI.equals(req.getContextPath() + "/") || reqURI.contains("/faces/Ingreso/ingreso.xhtml") || (ses != null && ses.getAttribute("idUsuario") != null)
                    || reqURI.contains("/public/") || reqURI.contains("javax.faces.resource")) {
                if (!reqURI.startsWith(req.getContextPath()+ResourceHandler.RESOURCE_IDENTIFIER)) { 
                    //Skip JSF resources (CSS/JS/Images/etc)
                    res.setHeader("Cache-Control", "no-cache, no-store, must-revalidate"); // HTTP 1.1.
                    res.setHeader("Pragma", "no-cache"); //HTTP 1.0.
                    res.setDateHeader("Expires", 0); //Proxies
                }
                chain.doFilter(request, response);
            } else {
                res.sendRedirect(req.getContextPath());
            }
        } catch (IOException t) {
            System.out.println(t.getMessage());
        } catch (ServletException t) {
            System.out.println(t.getMessage());
        }
    }

    @Override
    public void destroy() {
    }
}
