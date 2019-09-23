package br.com.maxgontijo.pmgo.planilhasveiculos.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import br.com.maxgontijo.pmgo.planilhasveiculos.dto.DateRestConverter;
import br.com.maxgontijo.pmgo.planilhasveiculos.dto.FiltroConverter;

@Configuration
public class IunesWebMVC extends WebMvcConfigurerAdapter {
	@Override
	public void addFormatters(FormatterRegistry registry) {
		registry.addConverter(new FiltroConverter());
		registry.addConverter(new DateRestConverter());
	}
}
