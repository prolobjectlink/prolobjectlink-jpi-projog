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
import static org.junit.Assert.assertTrue;

import java.util.Set;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import io.github.prolobjectlink.prolog.PrologEngine;
import io.github.prolobjectlink.prolog.PrologOperator;

public class PrologOperatorTest extends PrologBaseTest {

	private PrologOperator op;

	@Before
	public void setUp() throws Exception {
		PrologEngine engine = provider.newEngine();
		engine.operator(500, "xfx", "&");
		Set<PrologOperator> ops = engine.currentOperators();
		for (PrologOperator prologOperator : ops) {
			if (prologOperator.getOperator().equals("&")) {
				op = prologOperator;
			}
		}
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public final void testHashCode() {
		PrologOperator another = null;
		PrologEngine engine = provider.newEngine();
		engine.operator(500, "xfx", "&");
		Set<PrologOperator> ops = engine.currentOperators();
		for (PrologOperator prologOperator : ops) {
			if (prologOperator.getOperator().equals("&")) {
				another = prologOperator;
			}
		}
		assertEquals(another.hashCode(), op.hashCode());
	}

	@Test
	public final void testGetPriority() {
		assertEquals(500, op.getPriority());
	}

	@Test
	public final void testGetSpecifier() {
		assertEquals("xfx", op.getSpecifier());
	}

	@Test
	public final void testGetOperator() {
		assertEquals("&", op.getOperator());
	}

	@Test
	public final void testToString() {
		assertEquals("op(500,xfx,&)", op.toString());
	}

	@Test
	public final void testEqualsObject() {
		PrologOperator another = null;
		PrologEngine engine = provider.newEngine();
		engine.operator(500, "xfx", "&");
		Set<PrologOperator> ops = engine.currentOperators();
		for (PrologOperator prologOperator : ops) {
			if (prologOperator.getOperator().equals("&")) {
				another = prologOperator;
			}
		}
		assertEquals(another, op);
	}

	@Test
	public final void testCompareTo() {
		PrologOperator another = null;
		PrologEngine engine = provider.newEngine();
		engine.operator(500, "xfx", "&");
		Set<PrologOperator> ops = engine.currentOperators();
		for (PrologOperator prologOperator : ops) {
			if (prologOperator.getOperator().equals("&")) {
				another = prologOperator;
			}
		}
		assertTrue(another.compareTo(op) == 0);
	}

}
