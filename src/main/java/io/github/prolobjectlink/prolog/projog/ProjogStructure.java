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

import static io.github.prolobjectlink.prolog.PrologTermType.STRUCTURE_TYPE;

import org.projog.core.term.Structure;
import org.projog.core.term.Term;

import io.github.prolobjectlink.prolog.PrologProvider;
import io.github.prolobjectlink.prolog.PrologStructure;
import io.github.prolobjectlink.prolog.PrologTerm;

/**
 * 
 * @author Jose Zalacain
 * @since 1.0
 */
final class ProjogStructure extends ProjogTerm implements PrologStructure {

	ProjogStructure(PrologProvider provider, String functor, PrologTerm... arguments) {
		super(STRUCTURE_TYPE, provider);
		Term[] terms = new Term[arguments.length];
		for (int i = 0; i < arguments.length; i++) {
			terms[i] = ((ProjogTerm) arguments[i]).value;
		}
		value = Structure.createStructure(removeQuoted(functor), terms);
	}

	ProjogStructure(PrologProvider provider, String functor, Term... arguments) {
		super(STRUCTURE_TYPE, provider);
		value = Structure.createStructure(removeQuoted(functor), arguments);
	}

	ProjogStructure(PrologProvider provider, PrologTerm left, String operator, PrologTerm right) {
		super(STRUCTURE_TYPE, provider);
		Term leftOperand = ((ProjogTerm) left).value;
		Term rightOperand = ((ProjogTerm) right).value;
		value = Structure.createStructure(operator, new Term[] { leftOperand, rightOperand });
	}

	ProjogStructure(PrologProvider provider, Term left, String functor, Term right) {
		super(STRUCTURE_TYPE, provider, Structure.createStructure(functor, new Term[] { left, right }));
	}

	@Override
	public PrologTerm getArgument(int index) {
		checkIndex(index, getArity());
		Term term = value.getArgument(index);
		return toTerm(term, PrologTerm.class);
	}

	public PrologTerm[] getArguments() {
		int arity = value.getNumberOfArguments();
		PrologTerm[] arguments = new PrologTerm[arity];
		for (int i = 0; i < arity; i++) {
			arguments[i] = toTerm(value.getArgument(i), PrologTerm.class);
		}
		return arguments;
	}

	@Override
	public int getArity() {
		return value.getNumberOfArguments();
	}

	@Override
	public String getFunctor() {
		return value.getName();
	}

	public final PrologTerm getRight() {
		return getArgument(1);
	}

	public final PrologTerm getLeft() {
		return getArgument(0);
	}

	public final String getOperator() {
		return getFunctor();
	}

}
