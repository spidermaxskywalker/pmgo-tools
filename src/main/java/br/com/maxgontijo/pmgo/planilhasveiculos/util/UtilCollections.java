package br.com.maxgontijo.pmgo.planilhasveiculos.util;

import java.util.List;
import java.util.ListIterator;
import java.util.function.Predicate;

public class UtilCollections {
	public static <T> int findFirstIndex(List<T> list, Predicate<T> predicate) {
		return findFirstIndex(list, predicate, 0);
	}

	public static <T> int findFirstIndex(List<T> list, Predicate<T> predicate, int indexFrom) {
		for (ListIterator<T> it = list.listIterator(indexFrom); it.hasNext();) {
			int index = it.nextIndex();
			if (predicate.test(it.next())) {
				return index;
			}
		}
		return -1;
	}

	public static <T> T findFirstElement(List<T> list, Predicate<T> predicate) {
		return findFirstElement(list, predicate, 0);
	}

	public static <T> T findFirstElement(List<T> list, Predicate<T> predicate, int indexFrom) {
		for (ListIterator<T> it = list.listIterator(indexFrom); it.hasNext();) {
			T t = it.next();
			if (predicate.test(t)) {
				return t;
			}
		}
		return null;
	}
}
