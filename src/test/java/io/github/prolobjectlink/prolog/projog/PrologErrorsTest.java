/*-
 * #%L
 * prolobjectlink-jpi-projog
 * %%
 * Copyright (C) 2012 - 2019 Prolobjectlink Project
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

import static org.junit.Assert.assertEquals;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import io.github.prolobjectlink.prolog.CompoundExpectedError;
import io.github.prolobjectlink.prolog.FunctorError;
import io.github.prolobjectlink.prolog.IndicatorError;
import io.github.prolobjectlink.prolog.ListExpectedError;
import io.github.prolobjectlink.prolog.PrologError;
import io.github.prolobjectlink.prolog.StructureExpectedError;
import io.github.prolobjectlink.prolog.SyntaxError;
import io.github.prolobjectlink.prolog.UnknownTermError;

/**
 * Not covered errors during test case coverage running.
 * 
 * @author Jose Zalacain
 * @since 1.0
 */
public class PrologErrorsTest {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public final void testCompoundExpected() {
		assertEquals("The expected term is not a compound term : " + getClass(),
				new CompoundExpectedError(getClass()).getMessage());
	}

	@Test
	public final void testFunctorExpected() {
		assertEquals("The term not have functor: " + getClass(), new FunctorError(getClass()).getMessage());
	}

	@Test
	public final void testIndicatorExpected() {
		assertEquals("The term not have indicator: " + getClass(), new IndicatorError(getClass()).getMessage());
	}

	@Test
	public final void testListExpected() {
		assertEquals("The expected term is not a list : " + getClass(), new ListExpectedError(getClass()).getMessage());
	}

	@Test
	public final void testRuntimeExpected() {
		assertEquals("something is wrong", new PrologError("something is wrong").getMessage());
	}

	@Test
	public final void testStructureExpected() {
		assertEquals("The expected term is not a structure : " + getClass(),
				new StructureExpectedError(getClass()).getMessage());
	}

	@Test
	public final void testSyntaxExpected() {
		assertEquals("The string parsed have prolog syntax error: " + getClass().getName(),
				new SyntaxError(getClass().getName()).getMessage());
	}

	@Test
	public final void testUnknowExpected() {
		assertEquals("The object " + getClass() + " is not a correct prolog term",
				new UnknownTermError(getClass()).getMessage());
	}

}
