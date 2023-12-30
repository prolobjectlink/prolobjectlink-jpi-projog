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

import static io.github.prolobjectlink.prolog.PrologTermType.ATOM_TYPE;
import static io.github.prolobjectlink.prolog.PrologTermType.CLASS_TYPE;
import static io.github.prolobjectlink.prolog.PrologTermType.CUT_TYPE;
import static io.github.prolobjectlink.prolog.PrologTermType.DOUBLE_TYPE;
import static io.github.prolobjectlink.prolog.PrologTermType.FAIL_TYPE;
import static io.github.prolobjectlink.prolog.PrologTermType.FALSE_TYPE;
import static io.github.prolobjectlink.prolog.PrologTermType.FIELD_TYPE;
import static io.github.prolobjectlink.prolog.PrologTermType.FLOAT_TYPE;
import static io.github.prolobjectlink.prolog.PrologTermType.INTEGER_TYPE;
import static io.github.prolobjectlink.prolog.PrologTermType.LIST_TYPE;
import static io.github.prolobjectlink.prolog.PrologTermType.LONG_TYPE;
import static io.github.prolobjectlink.prolog.PrologTermType.MAP_ENTRY_TYPE;
import static io.github.prolobjectlink.prolog.PrologTermType.MAP_TYPE;
import static io.github.prolobjectlink.prolog.PrologTermType.MIXIN_TYPE;
import static io.github.prolobjectlink.prolog.PrologTermType.NIL_TYPE;
import static io.github.prolobjectlink.prolog.PrologTermType.OBJECT_TYPE;
import static io.github.prolobjectlink.prolog.PrologTermType.PARAMETER_TYPE;
import static io.github.prolobjectlink.prolog.PrologTermType.RESULT_TYPE;
import static io.github.prolobjectlink.prolog.PrologTermType.STRUCTURE_TYPE;
import static io.github.prolobjectlink.prolog.PrologTermType.TRUE_TYPE;
import static io.github.prolobjectlink.prolog.PrologTermType.VARIABLE_TYPE;

import java.util.Iterator;
import java.util.List;
import java.util.Objects;

import org.projog.core.ProjogException;
import org.projog.core.kb.KnowledgeBase;
import org.projog.core.kb.KnowledgeBaseUtils;
import org.projog.core.math.ArithmeticOperators;
import org.projog.core.predicate.PredicateKey;
import org.projog.core.term.Atom;
import org.projog.core.term.DecimalFraction;
import org.projog.core.term.IntegerNumber;
import org.projog.core.term.ListFactory;
import org.projog.core.term.ListUtils;
import org.projog.core.term.Structure;
import org.projog.core.term.Term;
import org.projog.core.term.TermType;
import org.projog.core.term.Variable;

import io.github.prolobjectlink.prolog.AbstractConverter;
import io.github.prolobjectlink.prolog.PrologAtom;
import io.github.prolobjectlink.prolog.PrologConverter;
import io.github.prolobjectlink.prolog.PrologDouble;
import io.github.prolobjectlink.prolog.PrologFloat;
import io.github.prolobjectlink.prolog.PrologInteger;
import io.github.prolobjectlink.prolog.PrologLong;
import io.github.prolobjectlink.prolog.PrologProvider;
import io.github.prolobjectlink.prolog.PrologTerm;
import io.github.prolobjectlink.prolog.PrologVariable;
import io.github.prolobjectlink.prolog.UnknownTermError;

/**
 * 
 * @author Jose Zalacain
 * @since 1.0
 */
public class ProjogConverter extends AbstractConverter<Term> implements PrologConverter<Term> {

	KnowledgeBase k = KnowledgeBaseUtils.createKnowledgeBase();
	ArithmeticOperators ops = k.getArithmeticOperators();

	private boolean checkOperator(String functor, int arity) {
		try {
			ops.getArithmeticOperator(new PredicateKey(functor, arity));
			return true;
		} catch (ProjogException e) {
			return false;
		}
	}

	@Override
	public PrologTerm toTerm(Term prologTerm) {
		if (prologTerm.getType() == TermType.EMPTY_LIST) {
			return new ProjogEmpty(provider);
		} else if (prologTerm.getType() == TermType.ATOM) {
			if (prologTerm.getName().equals("nil")) {
				return new ProjogNil(provider);
			} else if (prologTerm.getName().equals("!")) {
				return new ProjogCut(provider);
			} else if (prologTerm.getName().equals("fail")) {
				return new ProjogFail(provider);
			} else if (prologTerm.getName().equals("true")) {
				return new ProjogTrue(provider);
			} else if (prologTerm.getName().equals("false")) {
				return new ProjogFalse(provider);
			} else {
				return new ProjogAtom(provider, prologTerm.getName());
			}
		} else if (prologTerm instanceof DecimalFraction) {
			return new ProjogFloat(provider, ((DecimalFraction) prologTerm).getDouble());
		} else if (prologTerm instanceof IntegerNumber) {
			return new ProjogInteger(provider, ((IntegerNumber) prologTerm).getLong());
		} else if (prologTerm instanceof Variable) {
			String name = ((Variable) prologTerm).getId();
			PrologVariable variable = sharedVariables.get(name);
			if (variable == null) {
				variable = new ProjogVariable(provider, name);
				sharedVariables.put(variable.getName(), variable);
			}
			return variable;
		}

		else if (prologTerm.getType() == TermType.LIST) {
			int index = 0;
			List<Term> list = ListUtils.toJavaUtilList(prologTerm);
			if (list != null) {
				Term[] arguments = new Term[list.size()];
				Iterator<? extends Term> i = list.iterator();
				while (i.hasNext()) {
					Term term = i.next();
					arguments[index++] = term;
				}
				return new ProjogList(provider, arguments);
			}
			return new ProjogList(provider);
		}

		else if (prologTerm instanceof Structure) {

			Structure struct = (Structure) prologTerm;
			int arity = struct.getNumberOfArguments();
			String functor = struct.getName();
			Term[] arguments = new Term[arity];

			// expression

			if (arity == 2 && checkOperator(functor, arity)) {
				Term left = struct.getArgument(0);
				Term right = struct.getArgument(1);
				return new ProjogStructure(provider, left, functor, right);
			}

			// structure
			else {
				for (int i = 0; i < arity; i++) {
					arguments[i] = struct.getArgument(i);
				}
				return new ProjogStructure(provider, functor, arguments);
			}

		} else {
			throw new UnknownTermError(prologTerm);
		}
	}

	@Override
	public Term fromTerm(PrologTerm term) {
		switch (term.getType()) {
		case NIL_TYPE:
			return new Atom("nil");
		case CUT_TYPE:
			return new Atom("!");
		case FAIL_TYPE:
			return new Atom("fail");
		case TRUE_TYPE:
			return new Atom("true");
		case FALSE_TYPE:
			return new Atom("false");
		case ATOM_TYPE:
			return new Atom(removeQuoted(((PrologAtom) term).getStringValue()));
		case FLOAT_TYPE:
			return new DecimalFraction(((PrologFloat) term).getFloatValue());
		case INTEGER_TYPE:
			return new IntegerNumber(((PrologInteger) term).getIntegerValue());
		case DOUBLE_TYPE:
			return new DecimalFraction(((PrologDouble) term).getDoubleValue());
		case LONG_TYPE:
			return new IntegerNumber(((PrologLong) term).getLongValue());
		case VARIABLE_TYPE:
			String name = ((PrologVariable) term).getName();
			Term variable = sharedPrologVariables.get(name);
			if (variable == null) {
				variable = new Variable(name);
				sharedPrologVariables.put(name, variable);
			}
			return variable;
		case MAP_TYPE:
		case LIST_TYPE:
			return ListFactory.createList(fromTermArray(term.getArguments()));
		case STRUCTURE_TYPE:
		case MAP_ENTRY_TYPE:
			String functor = term.getFunctor();
			Term[] arguments = fromTermArray(term.getArguments());
			return Structure.createStructure(functor, arguments);
		case OBJECT_TYPE:
			return Structure.createStructure("'@'", new Term[] { new Atom("'" + term.getObject() + "'") });
		case PARAMETER_TYPE:
		case RESULT_TYPE:
		case FIELD_TYPE:
			name = ((PrologVariable) term).getName();
			return new Variable(name);
		case MIXIN_TYPE:
		case CLASS_TYPE:
			functor = removeQuoted(term.getFunctor());
			arguments = fromTermArray(term.getArguments());
			return Structure.createStructure(functor, arguments);
		default:
			throw new UnknownTermError(term);
		}
	}

	@Override
	public Term[] fromTermArray(PrologTerm[] terms) {
		Term[] prologTerms = new Term[terms.length];
		for (int i = 0; i < terms.length; i++) {
			prologTerms[i] = fromTerm(terms[i]);
		}
		return prologTerms;
	}

	@Override
	public Term fromTerm(PrologTerm head, PrologTerm[] body) {
		Term clauseHead = fromTerm(head);
		if (body != null && body.length > 0) {
			Term clauseBody = fromTerm(body[body.length - 1]);
			for (int i = body.length - 2; i >= 0; --i) {
				clauseBody = Structure.createStructure(",", new Term[] { fromTerm(body[i]), clauseBody });
			}
			return Structure.createStructure(":-", new Term[] { clauseHead, clauseBody });
		}
		return clauseHead;
	}

	@Override
	public PrologProvider createProvider() {
		return new Projog(this);
	}

	@Override
	public String toString() {
		return "ProjogConverter";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + Objects.hash(k, ops);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		ProjogConverter other = (ProjogConverter) obj;
		return Objects.equals(k, other.k) && Objects.equals(ops, other.ops);
	}

}
