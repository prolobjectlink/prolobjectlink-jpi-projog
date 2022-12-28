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

import java.util.AbstractSet;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Objects;
import java.util.Set;

import org.projog.api.Projog;
import org.projog.core.parser.Operands;
import org.projog.core.predicate.PredicateKey;

import io.github.prolobjectlink.prolog.AbstractOperator;
import io.github.prolobjectlink.prolog.PrologOperator;
import io.github.prolobjectlink.prolog.PrologOperatorSet;

/**
 * 
 * @author Jose Zalacain
 * @since 1.0
 */
final class ProjogOperatorSet extends AbstractSet<PrologOperator> implements PrologOperatorSet {

	private Set<PrologOperator> operators = new HashSet<PrologOperator>();
	private Operands ops = new Operands();

	public ProjogOperatorSet() {
		org.projog.api.Projog projog = new Projog();
		ops = projog.getKnowledgeBase().getOperands();
		Set<PredicateKey> ps = projog.getKnowledgeBase().getPredicates().getAllDefinedPredicateKeys();
		for (PredicateKey predicateKey : ps) {
			String name = predicateKey.getName();
			if (ops.isDefined(name)) {
				Object prefix = ops.prefix(name) ? ops.getPrefixPriority(name) : null;
				Object infix = ops.infix(name) ? ops.getInfixPriority(name) : null;
				Object postfix = ops.postfix(name) ? ops.getPostfixPriority(name) : null;
				int priority = prefix != null ? (int) prefix
						: (infix != null ? (int) infix : (int) (postfix != null ? postfix : Integer.MIN_VALUE));
				String specifier = ops.fx(name) ? "fx"
						: (ops.fy(name) ? "fy"
								: (ops.xf(name) ? "xf"
										: (ops.yf(name) ? "yf"
												: (ops.xfx(name) ? "xfx"
														: (ops.xfy(name) ? "xfy"
																: (ops.yfx(name) ? "yfx" : ("unknow")))))));
				AbstractOperator operator = new ProjogOperator(priority, specifier, name);
				operators.add(operator);
			}
		}
	}

	public boolean currentOp(String opreator) {
		return ops.isDefined(opreator);
	}

	@Override
	public Iterator<PrologOperator> iterator() {
		return operators.iterator();
	}

	@Override
	public int size() {
		return operators.size();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + Objects.hash(operators);
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
		ProjogOperatorSet other = (ProjogOperatorSet) obj;
		return Objects.equals(operators, other.operators);
	}

}
