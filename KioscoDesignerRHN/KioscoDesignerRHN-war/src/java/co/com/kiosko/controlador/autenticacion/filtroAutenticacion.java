package co.com.kiosko.controlador.autenticacion;

import java.io.IOException;
import java.io.Serializable;
import javax.faces.application.ResourceHandler;
import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author Felipe Triviño
 */
@WebFilter(filterName = "filtroAutenticacion", urlPatterns = {"/faces/*"})
public class filtroAutenticacion implements Filter, Serializable {

    FilterConfig filterConfig = null;

    public filtroAutenticacion() {
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        this.filterConfig = filterConfig;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        try {

            HttpServletRequest req = (HttpServletRequest) request;
            HttpServletResponse res = (HttpServletResponse) response;
//            try{
//            System.out.println("getAuthType() " + req.getAuthType());
//            System.out.println("getCharacterEncoding() " + req.getCharacterEncoding());
//            System.out.println("getContentLengthLong() " + req.getContentLengthLong());
//            System.out.println("getContentType() " + req.getContentType());
//            System.out.println("getContextPath() " + req.getContextPath());
//            for (Cookie c : req.getCookies()) {
//                System.out.println("getComment() " + c.getComment());
//                System.out.println("getDomain() " + c.getDomain());
//                System.out.println("getMaxAge() " + c.getMaxAge());
//                System.out.println("getName() " + c.getName());
//                System.out.println("getPath() " + c.getPath());
//                System.out.println("getSecure() " + c.getSecure());
//                System.out.println("getValue() " + c.getValue());
//                System.out.println("getVersion() " + c.getVersion());
//            }
//            System.out.println("getLocalAddr() " + req.getLocalAddr());
//            System.out.println("getLocalName() " + req.getLocalName());
//            System.out.println("getLocalPort() " + req.getLocalPort());
//            System.out.println("getMethod() " + req.getMethod());
//            System.out.println("getPathInfo() " + req.getPathInfo());
//            System.out.println("getPathTranslated() " + req.getPathTranslated());
//            System.out.println("getProtocol() " + req.getProtocol());
//            System.out.println("getQueryString() " + req.getQueryString());
//            System.out.println("getRemoteAddr() " + req.getRemoteAddr());
//            System.out.println("getRemoteHost() " + req.getRemoteHost());
//            System.out.println("getRemotePort() " + req.getRemotePort());
//            System.out.println("getRemoteUser() " + req.getRemoteUser());
//            System.out.println("getRequestURI() " + req.getRequestURI());
//            System.out.println("getRequestURL() " + req.getRequestURL());
//            System.out.println("getRequestedSessionId() " + req.getRequestedSessionId());
//            System.out.println("getScheme() " + req.getScheme());
//            System.out.println("getServerName() " + req.getServerName());
//            System.out.println("getServerPort() " + req.getServerPort());
//            System.out.println("getServletPath() " + req.getServletPath());
//            } catch(Exception e){
//                System.out.println("excepcion en filtro: "+e.getMessage());
//            }
            HttpSession ses = req.getSession(false);
            String reqURI = req.getRequestURI();

            /*if (reqURI.equals(req.getContextPath() + "/") || reqURI.indexOf("/faces/Ingreso/ingreso.xhtml") >= 0 || (ses != null && ses.getAttribute("idUsuario") != null)
                    || reqURI.indexOf("/public/") >= 0 || reqURI.contains("javax.faces.resource")) {*/
            if (reqURI.equals(req.getContextPath() + "/") || reqURI.contains("/faces/Ingreso/ingreso.xhtml") || (ses != null && ses.getAttribute("idUsuario") != null)
                    || reqURI.contains("/public/") || reqURI.contains("javax.faces.resource")) {
                if (!reqURI.startsWith(req.getContextPath() + ResourceHandler.RESOURCE_IDENTIFIER)) {
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
