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

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import io.github.prolobjectlink.prolog.PrologOperatorSet;

public class PrologOperatorSetTest {

	private PrologOperatorSet s = new ProjogOperatorSet();

	@Test
	public void testCurrentOp() {

		assertTrue(s.currentOp("is"));
		assertTrue(s.currentOp("not"));
		assertTrue(s.currentOp("**"));
		assertTrue(s.currentOp("=.."));
		assertTrue(s.currentOp("<<"));
		assertTrue(s.currentOp("/"));
		assertTrue(s.currentOp("mod"));
		assertTrue(s.currentOp("+"));
		assertTrue(s.currentOp("exp"));
		assertTrue(s.currentOp("cos"));
		assertTrue(s.currentOp("*"));
		assertTrue(s.currentOp("->"));
		assertTrue(s.currentOp(">"));
		assertTrue(s.currentOp("\\="));
		assertTrue(s.currentOp("as"));
		assertTrue(s.currentOp("^"));
		assertTrue(s.currentOp("div"));
		assertTrue(s.currentOp("@<"));
		assertTrue(s.currentOp(";"));
		assertTrue(s.currentOp("=<"));
		assertTrue(s.currentOp(":-"));
		assertTrue(s.currentOp("."));
		assertTrue(s.currentOp("=\\="));
		assertTrue(s.currentOp("-"));
		assertTrue(s.currentOp("sin"));
		assertTrue(s.currentOp("="));
		assertTrue(s.currentOp("<-"));
		assertTrue(s.currentOp("log"));
		assertTrue(s.currentOp("returns"));
		assertTrue(s.currentOp(">="));
		assertTrue(s.currentOp("@=<"));
		assertTrue(s.currentOp(">>"));
		assertTrue(s.currentOp("-"));
		assertTrue(s.currentOp("?-"));
		assertTrue(s.currentOp("=="));
		assertTrue(s.currentOp("\\=="));
		assertTrue(s.currentOp("<"));
		assertTrue(s.currentOp("=:="));
		assertTrue(s.currentOp("\\+"));
		assertTrue(s.currentOp("sqrt"));
		assertTrue(s.currentOp(","));
		assertTrue(s.currentOp("@>"));
		assertTrue(s.currentOp("\\"));
		assertTrue(s.currentOp("//"));
		assertTrue(s.currentOp("@>="));
		assertTrue(s.currentOp("rem"));
		assertTrue(s.currentOp("atan"));
		assertTrue(s.currentOp(":-"));
		assertTrue(s.currentOp("\\/"));
		assertTrue(s.currentOp("/\\"));

	}

	@Test
	public void testIterator() {
		assertNotNull(s.iterator());
	}

	@Test
	public void testSize() {
		assertTrue(s.size() > 0);
	}

	@Test
	public void testEquals() {
		assertTrue(s.equals(new ProjogOperatorSet()));
		assertFalse(s.equals(new Object()));
		assertFalse(s.equals(null));
		assertTrue(s.equals(s));
	}

	@Test
	public void testHashCode() {
		assertTrue(s.hashCode() == new ProjogOperatorSet().hashCode());
		// assertTrue(s.hashCode() == new String().hashCode());
		// assertTrue(s.hashCode() == ((String) null).hashCode());
	}

}
