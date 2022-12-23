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

import static io.github.prolobjectlink.prolog.PrologTermType.INTEGER_TYPE;

import org.projog.core.term.IntegerNumber;

import io.github.prolobjectlink.prolog.ArityError;
import io.github.prolobjectlink.prolog.FunctorError;
import io.github.prolobjectlink.prolog.PrologDouble;
import io.github.prolobjectlink.prolog.PrologFloat;
import io.github.prolobjectlink.prolog.PrologInteger;
import io.github.prolobjectlink.prolog.PrologLong;
import io.github.prolobjectlink.prolog.PrologProvider;
import io.github.prolobjectlink.prolog.PrologTerm;

/**
 * 
 * @author Jose Zalacain
 * @since 1.0
 */
final class ProjogInteger extends ProjogTerm implements PrologInteger {

	ProjogInteger(PrologProvider provider, Number value) {
		super(INTEGER_TYPE, provider, new IntegerNumber(value.intValue()));
	}

	public PrologInteger getPrologInteger() {
		return new ProjogInteger(provider, getIntegerValue());
	}

	public PrologFloat getPrologFloat() {
		return new ProjogFloat(provider, getFloatValue());
	}

	public PrologDouble getPrologDouble() {
		return new ProjogDouble(provider, getDoubleValue());
	}

	public PrologLong getPrologLong() {
		return new ProjogLong(provider, getLongValue());
	}

	public long getLongValue() {
		return ((IntegerNumber) value).getLong();
	}

	public double getDoubleValue() {
		return ((IntegerNumber) value).getDouble();
	}

	public int getIntegerValue() {
		return (int) getLongValue();
	}

	public float getFloatValue() {
		return (float) getDoubleValue();
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

}
