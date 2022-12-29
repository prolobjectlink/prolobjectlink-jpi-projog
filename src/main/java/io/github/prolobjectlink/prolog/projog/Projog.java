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

import java.util.Map;

import org.projog.core.parser.SentenceParser;
import org.projog.core.term.Term;

import io.github.prolobjectlink.prolog.AbstractProvider;
import io.github.prolobjectlink.prolog.PrologAtom;
import io.github.prolobjectlink.prolog.PrologConverter;
import io.github.prolobjectlink.prolog.PrologDouble;
import io.github.prolobjectlink.prolog.PrologEngine;
import io.github.prolobjectlink.prolog.PrologFloat;
import io.github.prolobjectlink.prolog.PrologInteger;
import io.github.prolobjectlink.prolog.PrologJavaConverter;
import io.github.prolobjectlink.prolog.PrologList;
import io.github.prolobjectlink.prolog.PrologLogger;
import io.github.prolobjectlink.prolog.PrologLong;
import io.github.prolobjectlink.prolog.PrologProvider;
import io.github.prolobjectlink.prolog.PrologStructure;
import io.github.prolobjectlink.prolog.PrologTerm;
import io.github.prolobjectlink.prolog.PrologVariable;

/**
 * 
 * @author Jose Zalacain
 * @since 1.0
 */
public class Projog extends AbstractProvider implements PrologProvider {

	private static final PrologLogger logger = new ProjogLogger();
	static final String VERSION = "0.6.0";
	static final String NAME = "Projog";

	public Projog() {
		super(new ProjogConverter());
	}

	public Projog(PrologConverter<Term> converter) {
		super(converter);
	}

	public PrologTerm parseTerm(String term) {
		return term.endsWith(".")
				? toTerm(SentenceParser.getInstance(term, new org.projog.api.Projog().getKnowledgeBase().getOperands())
						.parseTerm(), PrologTerm.class)
				: toTerm(SentenceParser
						.getInstance(term + ".", new org.projog.api.Projog().getKnowledgeBase().getOperands())
						.parseTerm(), PrologTerm.class);
	}

	public PrologTerm[] parseTerms(String stringTerms) {
		// TODO Auto-generated method stub
		return null;
	}

	public PrologTerm prologNil() {
		return new ProjogNil(this);
	}

	public PrologTerm prologCut() {
		return new ProjogCut(this);
	}

	public PrologTerm prologFail() {
		return new ProjogFail(this);
	}

	public PrologTerm prologTrue() {
		return new ProjogTrue(this);
	}

	public PrologTerm prologFalse() {
		return new ProjogFalse(this);
	}

	public PrologTerm prologEmpty() {
		return new ProjogEmpty(this);
	}

	public PrologTerm prologInclude(String file) {
		return newStructure("consult", newAtom(file));
	}

	public PrologEngine newEngine() {
		return new ProjogEngine(this);
	}

	public PrologEngine newEngine(String file) {
		PrologEngine engine = newEngine();
		engine.consult(file);
		return engine;
	}

	public PrologAtom newAtom(String functor) {
		if (!functor.matches(SIMPLE_ATOM_REGEX)) {
			return new ProjogAtom(this, "''" + functor + "''");
		}
		return new ProjogAtom(this, functor);
	}

	public PrologFloat newFloat(Number value) {
		return new ProjogFloat(this, value);
	}

	public PrologDouble newDouble(Number value) {
		return new ProjogDouble(this, value);
	}

	public PrologInteger newInteger(Number value) {
		return new ProjogInteger(this, value);
	}

	public PrologLong newLong(Number value) {
		return new ProjogLong(this, value);
	}

	public PrologVariable newVariable(int position) {
		return new ProjogVariable(this);
	}

	public PrologVariable newVariable(String name, int position) {
		return new ProjogVariable(this, name);
	}

	public PrologList newList() {
		return new ProjogEmpty(this);
	}

	public PrologList newList(PrologTerm[] arguments) {
		if (arguments != null && arguments.length > 0) {
			return new ProjogList(this, arguments);
		}
		return new ProjogEmpty(this);
	}

	public PrologList newList(PrologTerm head, PrologTerm tail) {
		return new ProjogList(this, head, tail);
	}

	public PrologList newList(PrologTerm[] arguments, PrologTerm tail) {
		return new ProjogList(this, arguments, tail);
	}

	public PrologStructure newStructure(String functor, PrologTerm... arguments) {
		return new ProjogStructure(this, functor, arguments);
	}

	public PrologTerm newStructure(PrologTerm left, String operator, PrologTerm right) {
		return new ProjogStructure(this, left, operator, right);
	}

	public PrologTerm newEntry(PrologTerm key, PrologTerm value) {
		return new ProjogEntry(this, key, value);
	}

	public PrologTerm newEntry(Object key, Object value) {
		PrologJavaConverter transformer = getJavaConverter();
		PrologTerm keyTerm = transformer.toTerm(key);
		PrologTerm valueTerm = transformer.toTerm(value);
		return newEntry(keyTerm, valueTerm);
	}

	public PrologTerm newMap(Map<PrologTerm, PrologTerm> map) {
		return new ProjogMap(this, map);
	}

	public PrologTerm newMap(int initialCapacity) {
		return new ProjogMap(this, initialCapacity);
	}

	public PrologTerm newMap() {
		return new ProjogMap(this);
	}

	public PrologTerm newReference(Object object) {
		return new ProjogReference(this, object);
	}

	public PrologTerm falseReference() {
		return newReference(false);
	}

	public PrologTerm trueReference() {
		return newReference(true);
	}

	public PrologTerm nullReference() {
		return newReference(null);
	}

	public PrologTerm voidReference() {
		return newReference(void.class);
	}

	public PrologJavaConverter getJavaConverter() {
		return new ProjogJavaConverter(this);
	}

	public PrologLogger getLogger() {
		return logger;
	}

	@Override
	public String toString() {
		return "Projog [converter=" + converter + "]";
	}

}
