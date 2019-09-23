package br.com.maxgontijo.pmgo.planilhasveiculos.config;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

@Service
public class IunesSpringApplicationContext implements ApplicationContextAware {
	private static ApplicationContext context;

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		context = applicationContext;
	}

	public static <T> T getBean(Class<T> beanClass) {
		return context.getBean(beanClass);
	}

	public static ApplicationContext getContext() {
		return context;
	}
}
