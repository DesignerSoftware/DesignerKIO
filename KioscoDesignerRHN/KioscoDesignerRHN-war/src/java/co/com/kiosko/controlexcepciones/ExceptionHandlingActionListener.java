package co.com.kiosko.controlexcepciones;

import javax.faces.event.AbortProcessingException;
import javax.faces.event.ActionEvent;
import javax.faces.event.ActionListener;

import com.sun.faces.application.ActionListenerImpl; 
import javax.faces.application.Application;
import javax.faces.application.NavigationHandler;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

/**
 * @author Dawid Furman www.jeeaps.pl/autor Base on:
 * http://www.jroller.com/mert/entry/jsf_handling_exceptions_by_extending
 */
public class ExceptionHandlingActionListener extends ActionListenerImpl implements ActionListener {

    public static final String ERROR_NAV_PAGE = "errorPage";

    @Override
    public void processAction(ActionEvent event) throws AbortProcessingException {

        try {
            super.processAction(event);
        } catch (Exception ex) {
            ((HttpSession)  FacesContext.getCurrentInstance().getExternalContext().getSession(true)).invalidate();
            this.gotoErrorPage();
        }
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    public void gotoErrorPage(){
        FacesContext contex = FacesContext.getCurrentInstance();
        Application application = contex.getApplication();
//        contex.setViewRoot(application.getViewHandler().createView(contex, ERROR_NAV_PAGE));
//        contex.getPartialViewContext().setRenderAll(true);
        NavigationHandler navHandler = application.getNavigationHandler();
        navHandler.handleNavigation(contex, null, ERROR_NAV_PAGE);
        contex.renderResponse();
    }
    

}