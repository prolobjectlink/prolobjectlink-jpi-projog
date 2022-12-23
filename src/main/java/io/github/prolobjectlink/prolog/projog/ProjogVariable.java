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

import static io.github.prolobjectlink.prolog.PrologTermType.VARIABLE_TYPE;

import org.projog.core.term.Term;
import org.projog.core.term.Variable;

import io.github.prolobjectlink.prolog.ArityError;
import io.github.prolobjectlink.prolog.FunctorError;
import io.github.prolobjectlink.prolog.PrologProvider;
import io.github.prolobjectlink.prolog.PrologTerm;
import io.github.prolobjectlink.prolog.PrologVariable;

/**
 * 
 * @author Jose Zalacain
 * @since 1.0
 */
class ProjogVariable extends ProjogTerm implements PrologVariable {

	ProjogVariable(PrologProvider provider) {
		super(VARIABLE_TYPE, provider, new Variable());
	}

	ProjogVariable(PrologProvider provider, String name) {
		super(VARIABLE_TYPE, provider, new Variable(name));
	}

	ProjogVariable(int type, PrologProvider provider) {
		super(type, provider);
	}

	ProjogVariable(int type, PrologProvider provider, String name) {
		super(type, provider, new Variable(name));
	}

	ProjogVariable(int type, PrologProvider provider, Term var) {
		super(type, provider, var);
	}

	public boolean isAnonymous() {
		return ((Variable) value).isAnonymous();
	}

	public String getName() {
		return ((Variable) value).getId();
	}

	public void setName(String name) {
		this.value = new Variable(name);
	}

	public PrologTerm[] getArguments() {
		return new ProjogTerm[0];
	}

	public int getArity() {
		throw new ArityError(this);
	}

	public String getFunctor() {
		throw new FunctorError(this);
	}

	public int getPosition() {
		throw new UnsupportedOperationException("getPosition()");
	}

}
