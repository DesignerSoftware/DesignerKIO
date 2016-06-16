package co.com.kiosko.controlador.autenticacion;

import java.io.IOException;
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
public class filtroAutenticacion implements Filter {

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
            if (reqURI.equals(req.getContextPath() + "/") || reqURI.indexOf("/faces/Ingreso/ingreso.xhtml") >= 0 || (ses != null && ses.getAttribute("idUsuario") != null)
                    || reqURI.indexOf("/public/") >= 0 || reqURI.contains("javax.faces.resource")) {
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
