/*
 * #%L
 * prolobjectlink-jpi-projog
 * %%
 * Copyright (C) 2019 Prolobjectlink Project
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */
package io.github.prolobjectlink.prolog.projog;

import static io.github.prolobjectlink.prolog.PrologTermType.LIST_TYPE;

import java.util.Collection;
import java.util.Iterator;

import org.projog.core.term.EmptyList;
import org.projog.core.term.List;
import org.projog.core.term.ListFactory;
import org.projog.core.term.ListUtils;
import org.projog.core.term.Term;

import io.github.prolobjectlink.prolog.AbstractIterator;
import io.github.prolobjectlink.prolog.PrologList;
import io.github.prolobjectlink.prolog.PrologProvider;
import io.github.prolobjectlink.prolog.PrologTerm;

/**
 * 
 * @author Jose Zalacain
 * @since 1.0
 */
class ProjogList extends ProjogTerm implements PrologList {

	protected ProjogList(PrologProvider provider) {
		super(LIST_TYPE, provider, EmptyList.EMPTY_LIST);
	}

	protected ProjogList(PrologProvider provider, Term[] arguments) {
		super(LIST_TYPE, provider, ListFactory.createList(arguments));
	}

	protected ProjogList(PrologProvider provider, PrologTerm[] arguments) {
		super(LIST_TYPE, provider);
		Term[] terms = new Term[arguments.length];
		for (int i = 0; i < arguments.length; i++) {
			terms[i] = ((ProjogTerm) arguments[i]).value;
		}
		value = ListFactory.createList(terms);
	}

	protected ProjogList(PrologProvider provider, PrologTerm head, PrologTerm tail) {
		super(LIST_TYPE, provider);
		Term h = ((ProjogTerm) head).value;
		Term t = ((ProjogTerm) tail).value;
		value = ListFactory.createList(h, t);
	}

	protected ProjogList(PrologProvider provider, PrologTerm[] arguments, PrologTerm tail) {
		super(LIST_TYPE, provider);
		value = ((ProjogTerm) tail).value;
		for (int i = arguments.length - 1; i >= 0; --i) {
			value = ListFactory.createList(((ProjogTerm) arguments[i]).value, value);
		}
	}

	public int size() {
		return ListUtils.toJavaUtilList(value).size();
	}

	public void clear() {
		value = EmptyList.EMPTY_LIST;
	}

	public boolean isEmpty() {
		return size() == 0;
	}

	public Iterator<PrologTerm> iterator() {
		List list = (List) value;
		return new TuPrologListIter(list);
	}

	public PrologTerm getHead() {
		List list = (List) value;
		return toTerm(list.getArgument(0), PrologTerm.class);
	}

	public PrologTerm getTail() {
		List list = (List) value;
		return toTerm(list.getArgument(1), PrologTerm.class);
	}

	@Override
	public int getArity() {
		List list = (List) value;
		return list.getNumberOfArguments();
	}

	@Override
	public String getFunctor() {
		List list = (List) value;
		return list.getName();
	}

	public PrologTerm[] getArguments() {
		int index = 0;
		List list = (List) value;
		Collection<Term>c=ListUtils.toJavaUtilList(list);
		PrologTerm[] arguments = new PrologTerm[c.size()];
		Iterator<? extends Term> i = c.iterator();
		while (i.hasNext()) {
			Term term = i.next();
			arguments[index++] = toTerm(term, PrologTerm.class);
		}

		return arguments;
	}

	private class TuPrologListIter extends AbstractIterator<PrologTerm> implements Iterator<PrologTerm> {

		private Iterator<? extends Term> i;

		private TuPrologListIter(List list) {
			i = ListUtils.toJavaUtilList(list).iterator();
		}

		public boolean hasNext() {
			return i.hasNext();
		}

		public PrologTerm next() {
			return toTerm(i.next(), PrologTerm.class);
		}

	}

}
