package br.com.maxgontijo.pmgo.planilhasveiculos;

import org.primefaces.webapp.filter.FileUploadFilter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletContextInitializer;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import javax.faces.webapp.FacesServlet;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import java.util.Locale;

@Configuration
@SpringBootApplication
@EntityScan({"br.com.maxgontijo.pmgo.planilhasveiculos.model"})
@EnableWebMvc
public class Application extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(Application.class);
    }

    @Bean
    public ServletRegistrationBean<FacesServlet> servletRegistrationBean() {
        FacesServlet servlet = new FacesServlet();
        servlet.getServletInfo();
        ServletRegistrationBean<FacesServlet> servletRegistrationBean = new ServletRegistrationBean<>(servlet, "*.jsf");
        return servletRegistrationBean;
    }

    @Bean
    public MessageSource messageSource() {
        Locale.setDefault(new Locale("pt", "BR"));
        ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
        messageSource.addBasenames("classpath:org/springframework/security/messages");
        return messageSource;
    }

    @Bean
    public FilterRegistrationBean<FileUploadFilter> fileUploadFilter() {
        FilterRegistrationBean<FileUploadFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new FileUploadFilter());
        registrationBean.addUrlPatterns("*.jsf");
        return registrationBean;
    }

    @Bean
    public ServletContextInitializer initializer() {
        return new ServletContextInitializer() {
            @Override
            public void onStartup(ServletContext servletContext) throws ServletException {
                servletContext.setInitParameter("primefaces.UPLOADER", "commons");
                servletContext.setInitParameter("primefaces.FONT_AWESOME", "true");
            }
        };
    }
}