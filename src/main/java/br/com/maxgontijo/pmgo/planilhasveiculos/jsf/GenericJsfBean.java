package br.com.maxgontijo.pmgo.planilhasveiculos.jsf;

import br.com.maxgontijo.pmgo.planilhasveiculos.config.IunesSpringApplicationContext;

abstract class GenericJsfBean {
	public GenericJsfBean() {
		IunesSpringApplicationContext.getContext().getAutowireCapableBeanFactory().autowireBean(this);
	}
}
