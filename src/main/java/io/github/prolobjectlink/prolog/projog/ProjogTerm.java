/*-
 * #%L
 * prolobjectlink-jpi-projog
 * %%
 * Copyright (C) 2020 - 2022 Prolobjectlink Project
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

import static io.github.prolobjectlink.prolog.AbstractConverter.SIMPLE_ATOM_REGEX;
import static io.github.prolobjectlink.prolog.PrologTermType.FLOAT_TYPE;
import static io.github.prolobjectlink.prolog.PrologTermType.LONG_TYPE;
import static io.github.prolobjectlink.prolog.PrologTermType.OBJECT_TYPE;

import org.projog.core.parser.Operands;
import org.projog.core.term.Term;
import org.projog.core.term.TermComparator;
import org.projog.core.term.TermType;
import org.projog.core.term.Variable;

import io.github.prolobjectlink.prolog.AbstractTerm;
import io.github.prolobjectlink.prolog.PrologProvider;
import io.github.prolobjectlink.prolog.PrologTerm;

abstract class ProjogTerm extends AbstractTerm implements PrologTerm {

	protected Term value;

	protected ProjogTerm(int type, PrologProvider provider) {
		super(type, provider);
	}

	protected ProjogTerm(int type, PrologProvider provider, Term value) {
		super(type, provider);
		this.value = value;
	}

	public final boolean isAtom() {
		return value.getType() == TermType.ATOM;
	}

	public final boolean isNumber() {
		return value.getType() == TermType.FRACTION || value.getType() == TermType.INTEGER;
	}

	public final boolean isFloat() {
		return type == FLOAT_TYPE;
	}

	public final boolean isInteger() {
		return value.getType() == TermType.INTEGER;
	}

	public final boolean isDouble() {
		return value.getType() == TermType.FRACTION;
	}

	public final boolean isLong() {
		return type == LONG_TYPE;
	}

	public final boolean isVariable() {
		return value.getType() == TermType.VARIABLE;
	}

	public final boolean isList() {
		return value.getType() == TermType.LIST;
	}

	public final boolean isStructure() {
		return value.getType() == TermType.STRUCTURE;
	}

	public final boolean isNil() {
		if (!isVariable() && !isNumber()) {
			return hasIndicator("nil", 0);
		}
		return false;
	}

	public final boolean isEmptyList() {
		return value.getType() == TermType.EMPTY_LIST;
	}

	public final boolean isAtomic() {
		return !isCompound();
	}

	public final boolean isCompound() {
		return !(isEmptyList()) && (isList() || isStructure());
	}

	public final boolean isEvaluable() {
		if (!isNumber() && !isVariable()) {
			Operands operands = new Operands();
			return operands.isDefined(getFunctor());
		}
		return false;
	}

	public final boolean isTrueType() {
		return getObject().equals(true);
	}

	public final boolean isFalseType() {
		return getObject().equals(false);
	}

	public final boolean isNullType() {
		return getObject() == null;
	}

	public final boolean isVoidType() {
		return getObject() == void.class;
	}

	public final boolean isObjectType() {
		return getType() == OBJECT_TYPE;
	}

	public final boolean isReference() {
		return isObjectType();
	}

	public int getArity() {
		return value.getNumberOfArguments();
	}

	public String getFunctor() {
		return value.getName();
	}

	public final boolean unify(PrologTerm term) {
		Term otherTerm = fromTerm(term, Term.class);
		boolean unify = value.unify(otherTerm);
		// check variable type for undone instantiation
		if (otherTerm instanceof Variable) {
			otherTerm.backtrack();
		}
		if (value instanceof Variable) {
			value.backtrack();
		}
		return unify;
	}

	// NOTE: projog not compare numbers by value
	public final int compareTo(PrologTerm term) {
		Term t = fromTerm(term, Term.class);
		int r = TermComparator.TERM_COMPARATOR.compare(value, t);
		return r < 0 ? -1 : (r > 0 ? 1 : 0);
	}

	@Override
	public final int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((value == null) ? 0 : value.hashCode());
		return result;
	}

	@Override
	public final boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ProjogTerm other = (ProjogTerm) obj;
		if (value == null) {
			if (other.value != null)
				return false;
		} else if (value.toString().equals(other.value.toString())) {
			return true;
		} else if (!value.unify(other.value)) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "" + value + "";
	}

}
