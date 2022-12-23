/*
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

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import io.github.prolobjectlink.prolog.PrologEngine;
import io.github.prolobjectlink.prolog.PrologQuery;
import io.github.prolobjectlink.prolog.PrologQueryBuilder;
import io.github.prolobjectlink.prolog.PrologStructure;
import io.github.prolobjectlink.prolog.PrologTerm;
import io.github.prolobjectlink.prolog.PrologVariable;

public class PrologQueryBuilderTest extends PrologBaseTest {

	private PrologVariable x;
	private PrologStructure dark;
	private PrologStructure big;
	private PrologStructure small;

	private PrologEngine engine;
	private PrologQuery query;

	private PrologQueryBuilder builder;

	private PrologTerm[] bearExpected = { provider.newAtom("bear") };
	private PrologTerm[] catExpected = { provider.newAtom("cat") };

	@Before
	public void setUp() throws Exception {

		engine = provider.newEngine();

		engine.assertz("big(bear)");
		engine.assertz("big(elephant)");
		engine.assertz("small(cat)");
		engine.assertz("brown(bear)");
		engine.assertz("black(cat)");
		engine.assertz("gray(elephant)");
		engine.assertz("dark(Z) :- black(Z)");
		engine.assertz("dark(Z) :- brown(Z)");

	}

	@After
	public void tearDown() throws Exception {
		engine.dispose();
	}

	@Test
	public void testBeginPrologTerm() {

		x = provider.newVariable("X", 0);
		big = provider.newStructure("big", x);
		dark = provider.newStructure("dark", x);
		small = provider.newStructure("small", x);

		query = engine.newQueryBuilder().begin(dark).query();
		assertTrue(query.hasSolution());
		assertArrayEquals(catExpected, query.oneSolution());

	}

	@Test
	public void testCommaPrologTerm() {

		x = provider.newVariable("X", 0);
		big = provider.newStructure("big", x);
		dark = provider.newStructure("dark", x);
		small = provider.newStructure("small", x);

		builder = engine.newQueryBuilder();
		builder.begin(dark);
		builder.comma(big);
		builder.comma(big);
		builder.comma(big);
		builder.comma(big);
		builder.comma(big);
		builder.comma(big);
		builder.comma(big);
		builder.comma(big);

		query = builder.query();
		assertTrue(query.hasSolution());
		assertArrayEquals(bearExpected, query.oneSolution());

		x = provider.newVariable("X", 0);
		big = provider.newStructure("big", x);
		dark = provider.newStructure("dark", x);
		small = provider.newStructure("small", x);

		builder = engine.newQueryBuilder();
		builder.begin(dark);
		builder.comma(provider.newStructure("\\+", big));
		query = builder.query();
		assertTrue(query.hasSolution());
		assertArrayEquals(catExpected, query.oneSolution());

		x = provider.newVariable("X", 0);
		big = provider.newStructure("big", x);
		dark = provider.newStructure("dark", x);
		small = provider.newStructure("small", x);

		builder = engine.newQueryBuilder();
		builder.begin(dark);
		builder.comma(provider.newStructure("\\+", big));
		builder.comma(small);
		query = builder.query();
		assertTrue(query.hasSolution());
		assertArrayEquals(catExpected, query.oneSolution());

	}

	@Test
	public void testSemicolonPrologTerm() {

		x = provider.newVariable("X", 0);
		big = provider.newStructure("big", x);
		dark = provider.newStructure("dark", x);
		small = provider.newStructure("small", x);

		builder = engine.newQueryBuilder();
		builder.begin(dark);
		builder.semicolon(big);
		builder.semicolon(big);
		builder.semicolon(big);
		builder.semicolon(big);
		builder.semicolon(big);
		builder.semicolon(big);
		builder.semicolon(big);
		builder.semicolon(big);

		query = builder.query();
		assertTrue(query.hasSolution());
		assertArrayEquals(catExpected, query.oneSolution());

		x = provider.newVariable("X", 0);
		big = provider.newStructure("big", x);
		dark = provider.newStructure("dark", x);
		small = provider.newStructure("small", x);

		builder = engine.newQueryBuilder();

		// fail intentionally at the first predicate
		builder.begin(provider.prologFail());

		// solve second as alternative
		builder.semicolon(big);
		query = builder.query();
		assertTrue(query.hasSolution());
		assertArrayEquals(bearExpected, query.oneSolution());

		x = provider.newVariable("X", 0);
		big = provider.newStructure("big", x);
		dark = provider.newStructure("dark", x);
		small = provider.newStructure("small", x);

		builder = engine.newQueryBuilder();

		// fail intentionally at the first predicate
		builder.begin("dark", provider.newAtom("octupus"));

		// solve second as alternative
		builder.semicolon(big);
		query = builder.query();
		assertTrue(query.hasSolution());
		assertArrayEquals(bearExpected, query.oneSolution());

	}

	@Test
	public void testBuildQuery() {

		x = provider.newVariable("X", 0);
		big = provider.newStructure("big", x);
		dark = provider.newStructure("dark", x);
		small = provider.newStructure("small", x);

		query = engine.query(dark);
		builder = engine.newQueryBuilder().begin(dark);
		assertEquals(query, builder.query());
		query.dispose();

		x = provider.newVariable("X", 0);
		big = provider.newStructure("big", x);
		dark = provider.newStructure("dark", x);
		small = provider.newStructure("small", x);

		query = engine.query("dark(X),big(X)");
		builder = engine.newQueryBuilder().begin(dark).comma(big);
		assertEquals(query, builder.query());

	}

}
