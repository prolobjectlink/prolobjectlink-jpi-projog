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

import static io.github.prolobjectlink.prolog.PrologTermType.DOUBLE_TYPE;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import io.github.prolobjectlink.prolog.ArityError;
import io.github.prolobjectlink.prolog.FunctorError;
import io.github.prolobjectlink.prolog.PrologAtom;
import io.github.prolobjectlink.prolog.PrologDouble;
import io.github.prolobjectlink.prolog.PrologFloat;
import io.github.prolobjectlink.prolog.PrologInteger;
import io.github.prolobjectlink.prolog.PrologList;
import io.github.prolobjectlink.prolog.PrologLong;
import io.github.prolobjectlink.prolog.PrologStructure;
import io.github.prolobjectlink.prolog.PrologTerm;
import io.github.prolobjectlink.prolog.PrologVariable;

public class PrologDoubleTest extends PrologBaseTest {

	private PrologDouble double1 = provider.newDouble(1.6180339887);

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public final void testGetPrologInteger() {
		assertEquals(provider.newInteger(1), double1.getPrologInteger());
	}

	@Test
	public final void testGetPrologFloat() {
		assertEquals(provider.newFloat(1.618034), double1.getPrologFloat());
	}

	@Test
	public final void testGetPrologLong() {
		assertEquals(provider.newLong(1), double1.getPrologLong());
	}

	@Test
	public final void testGetPrologDouble() {
		assertEquals(provider.newDouble(1.6180339887), double1.getPrologDouble());
	}

	@Test
	public final void testGetLongValue() {
		assertEquals(1, double1.getLongValue());
	}

	@Test
	public final void testGetDoubleValue() {
		assertEquals(1.6180339887, double1.getDoubleValue(), 0);
	}

	@Test
	public final void testGetIntValue() {
		assertEquals(1, double1.getIntegerValue());
	}

	@Test
	public final void testGetFloatValue() {
		assertEquals(1.618034F, double1.getFloatValue(), 0);
	}

	@Test
	public void testEqualsObject() {
		assertTrue(double1.equals(provider.newDouble(1.6180339887)));
	}

	@Test
	public void testToString() {
		assertEquals("1.6180339887", double1.toString());
	}

	@Test(expected = ArityError.class)
	public final void testGetArity() {
		double1.getArity();
	}

	@Test(expected = FunctorError.class)
	public final void testGetFunctor() {
		double1.getFunctor();
	}

	@Test
	public void testGetArguments() {
		assertArrayEquals(new PrologTerm[0], double1.getArguments());
	}

	@Test
	public void testGetType() {
		assertEquals(DOUBLE_TYPE, double1.getType());
	}

	@Test
	public void testIsAtom() {
		assertFalse(double1.isAtom());
	}

	@Test
	public void testIsNumber() {
		assertTrue(double1.isNumber());
	}

	@Test
	public void testIsFloat() {
		assertFalse(double1.isFloat());
	}

	@Test
	public void testIsInteger() {
		assertFalse(double1.isInteger());
	}

	@Test
	public void testIsDouble() {
		assertTrue(double1.isDouble());
	}

	@Test
	public void testIsLong() {
		assertFalse(double1.isLong());
	}

	@Test
	public void testIsVariable() {
		assertFalse(double1.isVariable());
	}

	@Test
	public void testIsList() {
		assertFalse(double1.isList());
	}

	@Test
	public void testIsStructure() {
		assertFalse(double1.isStructure());
	}

	@Test
	public void testIsNil() {
		assertFalse(double1.isNil());
	}

	@Test
	public void testIsEmptyList() {
		assertFalse(double1.isEmptyList());
	}

	@Test
	public void testIsExpression() {
		assertFalse(double1.isEvaluable());
	}

	@Test(expected = FunctorError.class)
	public void testGetKey() {
		double1.getIndicator();
	}

	@Test(expected = FunctorError.class)
	public void testHasIndicator() {
		assertFalse(double1.hasIndicator("1.6180339887", 0));
	}

	@Test
	public void testUntify() {

		// with atom
		PrologDouble dValue = provider.newDouble(36.47);
		PrologAtom atom = provider.newAtom("doe");
		assertFalse(dValue.unify(atom));

		// with integer
		PrologInteger iValue = provider.newInteger(28);
		assertFalse(dValue.unify(iValue));

		// with long
		PrologLong lValue = provider.newLong(28);
		assertFalse(dValue.unify(lValue));

		// with float
		PrologFloat fValue1 = provider.newFloat(100.98);
		// false because are different
		assertFalse(dValue.unify(fValue1));

		// with float
		PrologDouble dValue1 = provider.newDouble(100.98);
		// true because are equals
		assertTrue(dValue.unify(dValue));
		// false because are different
		assertFalse(dValue.unify(dValue1));

		// with variable
		PrologVariable variable = provider.newVariable("X", 0);
		// true. case float and variable
		assertTrue(dValue.unify(variable));

		// with predicate
		PrologStructure structure = provider.parseStructure("some_predicate(a,b,c)");
		assertFalse(dValue.unify(structure));

		// with list
		PrologList flattenedList = provider.parseList("[a,b,c]");
		assertFalse(dValue.unify(flattenedList));

		// with expression
		PrologTerm expression = provider.parseTerm("58+93*10");
		assertFalse(dValue.unify(expression));

	}

	@Test
	public void testCompareTo() {

		// with atom
		PrologDouble dValue = provider.newDouble(36.47);
		PrologAtom atom = provider.newAtom("doe");
		assertEquals(-1, dValue.compareTo(atom));

		// with integer
		PrologInteger iValue = provider.newInteger(28);
//		assertEquals(1, dValue.compareTo(iValue));
		// NOTE: projog not compare numbers by value
		assertEquals(-1, dValue.compareTo(iValue));

		// with long
		PrologLong lValue = provider.newLong(28);
//		assertEquals(1, dValue.compareTo(lValue));
		// NOTE: projog not compare numbers by value
		assertEquals(-1, dValue.compareTo(lValue));

		// with float
		PrologFloat fValue1 = provider.newFloat(100.98);
		// false because are different
		assertEquals(-1, dValue.compareTo(fValue1));

		// with float
		PrologDouble dValue1 = provider.newDouble(100.98);
		// true because are equals
		assertEquals(0, dValue.compareTo(dValue));
		// false because are different
		assertEquals(-1, dValue.compareTo(dValue1));

		// with variable
		PrologVariable variable = provider.newVariable("X", 0);
		// true. case float and variable
		assertEquals(1, dValue.compareTo(variable));

		// with predicate
		PrologStructure structure = provider.parseStructure("some_predicate(a,b,c)");
		assertEquals(-1, dValue.compareTo(structure));

		// with list
		PrologList flattenedList = provider.parseList("[a,b,c]");
		assertEquals(-1, dValue.compareTo(flattenedList));

		// with expression
		PrologTerm expression = provider.parseTerm("58+93*10");
		assertEquals(-1, dValue.compareTo(expression));

	}

}
