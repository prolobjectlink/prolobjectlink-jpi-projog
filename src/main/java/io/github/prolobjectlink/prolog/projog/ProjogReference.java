/*-
 * #%L
 * prolobjectlink-jpi-projog
 * %%
 * Copyright (C) 2020 - 2021 Prolobjectlink Project
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

import static io.github.prolobjectlink.prolog.PrologTermType.OBJECT_TYPE;

import org.projog.core.term.Atom;
import org.projog.core.term.Structure;
import org.projog.core.term.Term;

import io.github.prolobjectlink.prolog.PrologProvider;
import io.github.prolobjectlink.prolog.PrologReference;
import io.github.prolobjectlink.prolog.PrologTerm;

public class ProjogReference extends ProjogTerm implements PrologReference {

	protected final Object reference;

	protected ProjogReference(PrologProvider provider, Object reference) {
		super(OBJECT_TYPE, provider, Structure.createStructure("'@'", new Term[] { new Atom("'" + reference + "'") }));
		this.reference = reference;
	}

	@Override
	public int getArity() {
		return 1;
	}

	@Override
	public String getFunctor() {
		return "@";
	}

	@Override
	public PrologTerm[] getArguments() {
		String string = reference.toString();
		PrologTerm tag = provider.newAtom(string);
		return new PrologTerm[] { tag };
	}

	@Override
	public PrologTerm getTerm() {
		String string = reference.toString();
		PrologTerm tag = provider.newAtom(string);
		return provider.newStructure(getFunctor(), tag);
	}

	@Override
	public Class<?> getReferenceType() {
		return reference.getClass();
	}

	@Override
	public Object getObject() {
		return reference;
	}

	@Override
	public String toString() {
		return "" + getTerm() + "";
	}

}
