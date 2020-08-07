package br.com.maxgontijo.pmgo.planilhasveiculos.config;

import br.com.maxgontijo.pmgo.planilhasveiculos.util.UtilSessao;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class AutenticacaoFilter implements Filter {

    public AutenticacaoFilter() {
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
        HttpServletResponse response = (HttpServletResponse) res;
        HttpServletRequest request = (HttpServletRequest) req;

        String uri = request.getRequestURI();

        if (UtilSessao.getUsuarioLogado(request.getSession()) != null) {
            if (uri.equals("/login.jsf")) {
                response.sendRedirect("/index.jsf");
            } else {
                chain.doFilter(req, res);
            }
        } else {
            if (!uri.equals("/login.jsf")) {
                response.sendRedirect("/login.jsf");
            } else {
                chain.doFilter(req, res);
            }
        }
    }

    @Override
    public void init(FilterConfig filterConfig) {
    }

    @Override
    public void destroy() {
    }
}
