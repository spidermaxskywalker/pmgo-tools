package br.com.maxgontijo.pmgo.planilhasveiculos.util;

import br.com.maxgontijo.pmgo.planilhasveiculos.model.Usuario;

import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

public class UtilSessao {
    private static final String ATTR_SESSION_USER = "ATTR_SESSION_USER";

    public static HttpSession getSession() {
        return getSession(false);
    }

    public static HttpSession getSession(boolean createIfNotExists) {
        HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(createIfNotExists);
        return session;
    }

    public static Object getSessionAttribute(String name) {
        HttpSession session = getSession();
        return getSessionAttribute(name, session);
    }

    public static Object getSessionAttribute(String name, HttpSession session) {
        if (session != null) {
            return session.getAttribute(name);
        } else {
            return null;
        }
    }

    public static void putSessionAttribute(String name, Object value) {
        HttpSession session = getSession(true);
        putSessionAttribute(name, value, session);
    }

    public static void putSessionAttribute(String name, Object value, HttpSession session) {
        if (session != null) {
            session.setAttribute(name, value);
        }
    }

    public static void invalidateSession() {
        HttpSession session = getSession();
        if (session != null) {
            session.invalidate();
        }
    }

    public static Usuario getUsuarioLogado() {
        return (Usuario) getSessionAttribute(ATTR_SESSION_USER);
    }

    public static Usuario getUsuarioLogado(HttpSession session) {
        return (Usuario) getSessionAttribute(ATTR_SESSION_USER, session);
    }

    public static void putUsuarioLogado(Usuario usuario) {
        putUsuarioLogado(usuario, getSession(true));
    }

    public static void putUsuarioLogado(Usuario usuario, HttpSession session) {
        putSessionAttribute(ATTR_SESSION_USER, usuario, session);
    }
}
