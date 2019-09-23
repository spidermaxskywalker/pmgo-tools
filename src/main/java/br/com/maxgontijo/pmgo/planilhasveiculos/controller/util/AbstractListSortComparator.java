package br.com.maxgontijo.pmgo.planilhasveiculos.controller.util;

import java.util.Comparator;

public abstract class AbstractListSortComparator<T> implements Comparator<T> {
	protected final String[] args;

	public AbstractListSortComparator(String[] args) {
		this.args = args;
	}
}
