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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.projog.api.Projog;
import org.projog.core.parser.Operands;
import org.projog.core.predicate.PredicateKey;
import org.projog.core.predicate.udp.UserDefinedPredicateFactory;

import io.github.prolobjectlink.prolog.AbstractOperator;
import io.github.prolobjectlink.prolog.Licenses;
import io.github.prolobjectlink.prolog.PrologAtom;
import io.github.prolobjectlink.prolog.PrologEngine;
import io.github.prolobjectlink.prolog.PrologOperator;
import io.github.prolobjectlink.prolog.PrologQuery;
import io.github.prolobjectlink.prolog.PrologStructure;
import io.github.prolobjectlink.prolog.PrologTerm;
import io.github.prolobjectlink.prolog.PrologVariable;

public class PrologEngineTest extends PrologBaseTest {

	private PrologEngine engine;
	private PrologQuery query;

	@Before
	public void setUp() throws Exception {

		engine = provider.newEngine();

		solution[0][0] = mcardon;
		solution[0][1] = one;
		solution[0][2] = five;
		solution[0][3] = board;
		solution[0][4] = threeThousand;

		solution[1][0] = treeman;
		solution[1][1] = two;
		solution[1][2] = three;
		solution[1][3] = human_resources;
		solution[1][4] = twoThousand;

		solution[2][0] = chapman;
		solution[2][1] = one;
		solution[2][2] = two;
		solution[2][3] = board;
		solution[2][4] = thousandFiveHundred;

		solution[3][0] = claessen;
		solution[3][1] = four;
		solution[3][2] = one;
		solution[3][3] = technical_services;
		solution[3][4] = thousand;

		solution[4][0] = petersen;
		solution[4][1] = five;
		solution[4][2] = eight;
		solution[4][3] = administration;
		solution[4][4] = fourThousandFiveHundred;

		solution[5][0] = cohn;
		solution[5][1] = one;
		solution[5][2] = seven;
		solution[5][3] = board;
		solution[5][4] = fourThousand;

		solution[6][0] = duffy;
		solution[6][1] = one;
		solution[6][2] = nine;
		solution[6][3] = board;
		solution[6][4] = fiveThousand;

	}

	@After
	public void tearDown() throws Exception {
		engine.dispose();
	}

	@Test
	public final void testInclude() {

		engine.include("family.pl");
		engine.include("company.pl");
		engine.include("zoo.pl");
		assertFalse(engine.isProgramEmpty());
		assertEquals(50, engine.getProgramSize());

	}

	@Test
	public final void testConsult() {

		engine = provider.newEngine();
		engine.consult("family.pl");
		assertFalse(engine.isProgramEmpty());
		assertEquals(21, engine.getProgramSize());
		engine.dispose();

		engine = provider.newEngine();
		engine.consult("company.pl");
		assertFalse(engine.isProgramEmpty());
		assertEquals(21, engine.getProgramSize());
		engine.dispose();

		engine = provider.newEngine();
		engine.consult("zoo.pl");
		assertFalse(engine.isProgramEmpty());
		assertEquals(8, engine.getProgramSize());
		engine.dispose();

	}

	@Test
	public final void testSave() {
		// Family save test case
		engine = provider.newEngine();

		engine.assertz(provider.newStructure(parent, pam, bob));
		engine.assertz(provider.newStructure(parent, tom, bob));
		engine.assertz(provider.newStructure(parent, tom, liz));
		engine.assertz(provider.newStructure(parent, bob, ann));
		engine.assertz(provider.newStructure(parent, bob, pat));
		engine.assertz(provider.newStructure(parent, pat, jim));

		engine.assertz(provider.newStructure(female, pam));
		engine.assertz(provider.newStructure(male, tom));
		engine.assertz(provider.newStructure(male, bob));
		engine.assertz(provider.newStructure(female, liz));
		engine.assertz(provider.newStructure(female, ann));
		engine.assertz(provider.newStructure(female, pat));
		engine.assertz(provider.newStructure(male, jim));

		x = provider.newVariable("X", 0);
		y = provider.newVariable("Y", 1);
		z = provider.newVariable("Z", 2);
		engine.assertz(provider.newStructure(offspring, x, y), provider.newStructure(parent, x, y));

		x = provider.newVariable("X", 0);
		y = provider.newVariable("Y", 1);
		engine.assertz(provider.newStructure(mother, x, y), provider.newStructure(parent, x, y),
				provider.newStructure(female, x));

		x = provider.newVariable("X", 0);
		y = provider.newVariable("Y", 1);
		z = provider.newVariable("Z", 2);
		engine.assertz(provider.newStructure(grandparent, x, z), provider.newStructure(parent, x, y),
				provider.newStructure(parent, y, z));

		x = provider.newVariable("X", 0);
		y = provider.newVariable("Y", 1);
		z = provider.newVariable("Z", 2);

		engine.assertz(provider.newStructure(sister, x, y), provider.newStructure(parent, z, x),
				provider.newStructure(parent, z, y), provider.newStructure(female, x),
				provider.newStructure(different, x, y));

		x = provider.newVariable("X", 0);
		engine.assertz(provider.newStructure(different, x, x), provider.prologCut(), provider.prologFail());

		x = provider.newVariable("X", 0);
		y = provider.newVariable("Y", 1);
		engine.assertz(provider.newStructure(different, x, y));

		x = provider.newVariable("X", 0);
		y = provider.newVariable("Y", 1);
		z = provider.newVariable("Z", 2);
		engine.assertz(provider.newStructure(predecessor, x, z), provider.newStructure(parent, x, z));

		x = provider.newVariable("X", 0);
		y = provider.newVariable("Y", 1);
		z = provider.newVariable("Z", 2);
		engine.assertz(provider.newStructure(predecessor, x, z), provider.newStructure(parent, x, y),
				provider.newStructure(predecessor, y, z));

		engine.persist("family.pl");

		// Physic existence test
		File file = new File("family.pl");
		assertTrue(file.exists());
		assertTrue(file.length() > 0);

		// Logical program saved
		engine.consult("family.pl");
		assertFalse(engine.isProgramEmpty());
		assertEquals(21, engine.getProgramSize());
		engine.dispose();

		// Company save test case
		engine = provider.newEngine();

		// employee relationship
		engine.assertz(provider.newStructure(employee, mcardon, one, five));
		engine.assertz(provider.newStructure(employee, treeman, two, three));
		engine.assertz(provider.newStructure(employee, chapman, one, two));
		engine.assertz(provider.newStructure(employee, claessen, four, one));
		engine.assertz(provider.newStructure(employee, petersen, five, eight));
		engine.assertz(provider.newStructure(employee, cohn, one, seven));
		engine.assertz(provider.newStructure(employee, duffy, one, nine));

		// department relationship
		engine.assertz(provider.newStructure(department, one, board));
		engine.assertz(provider.newStructure(department, two, human_resources));
		engine.assertz(provider.newStructure(department, three, production));
		engine.assertz(provider.newStructure(department, four, technical_services));
		engine.assertz(provider.newStructure(department, five, administration));

		// salary relationship
		engine.assertz(provider.newStructure(salary, one, thousand));
		engine.assertz(provider.newStructure(salary, two, thousandFiveHundred));
		engine.assertz(provider.newStructure(salary, three, twoThousand));
		engine.assertz(provider.newStructure(salary, four, twoThousandFiveHundred));
		engine.assertz(provider.newStructure(salary, five, threeThousand));
		engine.assertz(provider.newStructure(salary, six, threeThousandFiveHundred));
		engine.assertz(provider.newStructure(salary, seven, fourThousand));
		engine.assertz(provider.newStructure(salary, eight, fourThousandFiveHundred));
		engine.assertz(provider.newStructure(salary, nine, fiveThousand));

		engine.persist("company.pl");

		// Physic existence test
		file = new File("company.pl");
		assertTrue(file.exists());
		assertTrue(file.length() > 0);

		// Logical program saved
		engine.consult("company.pl");
		assertFalse(engine.isProgramEmpty());
		assertEquals(21, engine.getProgramSize());
		engine.dispose();

		// Zoo save test case
		engine = provider.newEngine();

		engine.assertz(provider.newStructure("big", bear));
		engine.assertz(provider.newStructure("big", elephant));
		engine.assertz(provider.newStructure("small", cat));
		engine.assertz(provider.newStructure("brown", bear));
		engine.assertz(provider.newStructure("black", cat));
		engine.assertz(provider.newStructure("gray", elephant));

		// dark rules
		z = provider.newVariable("Z", 0);
		engine.assertz(provider.newStructure("dark", z), provider.newStructure("black", z));

		z = provider.newVariable("Z", 0);
		engine.assertz(provider.newStructure("dark", z), provider.newStructure("brown", z));

		engine.persist("zoo.pl");

		// Physic existence test
		file = new File("zoo.pl");
		assertTrue(file.exists());
		assertTrue(file.length() > 0);

		// Logical program saved
		engine.consult("zoo.pl");
		assertFalse(engine.isProgramEmpty());
		assertEquals(8, engine.getProgramSize());
		engine.dispose();

	}

	@Test
	public final void testAbolish() {

		engine.assertz("parent( pam, bob)");
		engine.assertz("parent( tom, bob)");
		engine.assertz("parent( tom, liz)");
		engine.assertz("parent( bob, ann)");
		engine.assertz("parent( bob, pat)");
		engine.assertz("parent( pat, jim)");
		assertEquals(6, engine.getProgramSize());

		engine.abolish("parent", 2);
		assertTrue(engine.isProgramEmpty());
		assertEquals(0, engine.getProgramSize());

	}

	@Test
	public final void testAssertaString() {
		engine.asserta("parent( pam, bob)");
		assertEquals(1, engine.getProgramSize());

		engine.asserta("parent( pam, bob)");
		// the engine size is one because the added clause is already defined
		assertEquals(1, engine.getProgramSize());

		engine.asserta("parent( tom, bob)");
		assertEquals(2, engine.getProgramSize());

		engine.asserta("mother( X, Y):-parent( X, Y),female( X)");
		assertEquals(3, engine.getProgramSize());

		engine.asserta("mother( X, Y):-parent( X, Y),female( X)");
		// the program size is three because the added clause is already defined
		assertEquals(3, engine.getProgramSize());

	}

	@Test
	public final void testAssertaPrologTerm() {
		engine.asserta(provider.newStructure(parent, pam, bob));
		assertEquals(1, engine.getProgramSize());

		engine.asserta(provider.newStructure(parent, pam, bob));
		// the engine size is one because the added clause is already defined
		assertEquals(1, engine.getProgramSize());

		engine.asserta(provider.newStructure(parent, tom, bob));
		assertEquals(2, engine.getProgramSize());

		x = provider.newVariable("X", 0);
		y = provider.newVariable("Y", 1);
		engine.asserta(provider.newStructure(mother, x, y), provider.newStructure(parent, x, y),
				provider.newStructure(female, x));
		assertEquals(3, engine.getProgramSize());

		x = provider.newVariable("X", 0);
		y = provider.newVariable("Y", 1);
		engine.asserta(provider.newStructure(mother, x, y), provider.newStructure(parent, x, y),
				provider.newStructure(female, x));
		// the program size is three because the added clause is already defined
		assertEquals(3, engine.getProgramSize());

	}

	@Test
	public final void testAssertaPrologTermPrologTermArray() {
		PrologVariable x = provider.newVariable("X", 0);
		PrologVariable y = provider.newVariable("Y", 1);
		engine.asserta(provider.newStructure(mother, x, y), provider.newStructure(parent, x, y),
				provider.newStructure(female, x));
		assertEquals(1, engine.getProgramSize());
	}

	@Test
	public final void testAssertzString() {
		engine.assertz("parent( pam, bob)");
		assertEquals(1, engine.getProgramSize());

		engine.assertz("parent( pam, bob)");
		// the engine size is one because the added clause is already defined
		assertEquals(1, engine.getProgramSize());

		engine.assertz("parent( tom, bob)");
		assertEquals(2, engine.getProgramSize());

		engine.assertz("mother( X, Y):-parent( X, Y),female( X)");
		assertEquals(3, engine.getProgramSize());

		engine.assertz("mother( X, Y):-parent( X, Y),female( X)");
		// the program size is three because the added clause is already defined
		assertEquals(3, engine.getProgramSize());
	}

	@Test
	public final void testAssertzPrologTerm() {
		engine.assertz(provider.newStructure(parent, pam, bob));
		assertEquals(1, engine.getProgramSize());

		engine.assertz(provider.newStructure(parent, pam, bob));
		// the engine size is one because the added clause is already defined
		assertEquals(1, engine.getProgramSize());

		engine.assertz(provider.newStructure(parent, tom, bob));
		assertEquals(2, engine.getProgramSize());

		x = provider.newVariable("X", 0);
		y = provider.newVariable("Y", 1);
		engine.assertz(provider.newStructure(mother, x, y), provider.newStructure(parent, x, y),
				provider.newStructure(female, x));
		assertEquals(3, engine.getProgramSize());

		x = provider.newVariable("X", 0);
		y = provider.newVariable("Y", 1);
		engine.assertz(provider.newStructure(mother, x, y), provider.newStructure(parent, x, y),
				provider.newStructure(female, x));
		// the program size is three because the added clause is already defined
		assertEquals(3, engine.getProgramSize());

	}

	@Test
	public final void testAssertzPrologTermPrologTermArray() {
		PrologVariable x = provider.newVariable("X", 0);
		PrologVariable y = provider.newVariable("Y", 1);
		engine.assertz(provider.newStructure(mother, x, y), provider.newStructure(parent, x, y),
				provider.newStructure(female, x));
		assertEquals(1, engine.getProgramSize());
	}

	@Test
	public final void testClauseString() {

		engine.assertz("parent( pam, bob)");
		engine.assertz("parent( tom, bob)");
		engine.assertz("parent( tom, liz)");
		engine.assertz("parent( bob, ann)");
		engine.assertz("parent( bob, pat)");
		engine.assertz("parent( pat, jim)");
		engine.assertz("predecessor( X, Z):-parent( X, Z)");
		engine.assertz("predecessor( X, Z):-parent( X, Y),predecessor( Y, Z)");

		assertTrue(engine.clause("parent( X, Y)"));
		assertTrue(engine.clause("parent( pam, bob)"));
		assertTrue(engine.clause("parent( pat, jim)"));
		assertTrue(engine.clause("predecessor( X, Z):-parent( X, Z)"));
		assertTrue(engine.clause("predecessor( X, Z):-parent( X, Y),predecessor( Y, Z)"));

	}

	@Test
	public final void testClausePrologTerm() {
		engine.assertz("parent( pam, bob)");
		engine.assertz("parent( tom, bob)");
		engine.assertz("parent( tom, liz)");
		engine.assertz("parent( bob, ann)");
		engine.assertz("parent( bob, pat)");
		engine.assertz("parent( pat, jim)");
		engine.assertz("predecessor( X, Z):-parent( X, Z)");
		engine.assertz("predecessor( X, Z):-parent( X, Y),predecessor( Y, Z)");

		PrologAtom pam = provider.newAtom("pam");
		PrologAtom bob = provider.newAtom("bob");
		assertTrue(engine.clause(provider.newStructure("parent", pam, bob)));

		PrologAtom pat = provider.newAtom("pat");
		PrologAtom jim = provider.newAtom("jim");
		assertTrue(engine.clause(provider.newStructure("parent", pat, jim)));

		x = provider.newVariable("X", 0);
		y = provider.newVariable("Y", 1);
		assertTrue(engine.clause(provider.newStructure("parent", x, y)));
	}

	@Test
	public final void testClausePrologTermPrologTermArray() {
		engine.assertz("parent( pam, bob)");
		engine.assertz("parent( tom, bob)");
		engine.assertz("parent( tom, liz)");
		engine.assertz("parent( bob, ann)");
		engine.assertz("parent( bob, pat)");
		engine.assertz("parent( pat, jim)");
		engine.assertz("predecessor( X, Z):-parent( X, Z)");
		engine.assertz("predecessor( X, Z):-parent( X, Y),predecessor( Y, Z)");

		x = provider.newVariable("X", 0);
		y = provider.newVariable("Y", 1);
		z = provider.newVariable("Z", 2);
		assertTrue(engine.clause(provider.newStructure("predecessor", x, z), provider.newStructure("parent", x, z)));

		x = provider.newVariable("X", 0);
		y = provider.newVariable("Y", 1);
		z = provider.newVariable("Z", 2);
		assertTrue(engine.clause(provider.newStructure("predecessor", x, z), provider.newStructure("parent", x, y),
				provider.newStructure("predecessor", y, z)));

	}

	@Test
	public final void testRetractString() {

		engine.assertz("parent( pam, bob)");
		engine.assertz("parent( tom, bob)");
		engine.assertz("parent( tom, liz)");
		engine.assertz("parent( bob, ann)");
		engine.assertz("parent( bob, pat)");
		engine.assertz("parent( pat, jim)");

		engine.retract("parent( tom, bob)");
		assertEquals(5, engine.getProgramSize());

		engine.retract("parent( bob, pat)");
		assertEquals(4, engine.getProgramSize());

		engine.retract("parent( pat, jim)");
		assertEquals(3, engine.getProgramSize());

		engine.retract("parent( pam, bob)");
		assertEquals(2, engine.getProgramSize());

		engine.retract("parent( tom, liz)");
		assertEquals(1, engine.getProgramSize());

		engine.retract("parent( bob, ann)");
		assertEquals(0, engine.getProgramSize());

	}

	@Test
	public final void testRetractPrologTerm() {
		engine.assertz("parent( pam, bob)");
		engine.assertz("parent( tom, bob)");
		engine.assertz("parent( tom, liz)");
		engine.assertz("parent( bob, ann)");
		engine.assertz("parent( bob, pat)");
		engine.assertz("parent( pat, jim)");

		engine.retract(provider.newStructure(parent, tom, bob));
		assertEquals(5, engine.getProgramSize());

		engine.retract(provider.newStructure(parent, bob, pat));
		assertEquals(4, engine.getProgramSize());

		engine.retract(provider.newStructure(parent, pat, jim));
		assertEquals(3, engine.getProgramSize());

		engine.retract(provider.newStructure(parent, pam, bob));
		assertEquals(2, engine.getProgramSize());

		engine.retract(provider.newStructure(parent, tom, liz));
		assertEquals(1, engine.getProgramSize());

		engine.retract(provider.newStructure(parent, bob, ann));
		assertEquals(0, engine.getProgramSize());
	}

	@Test
	public final void testRetractPrologTermPrologTermArray() {
		engine.assertz("mother( X, Y):-parent( X, Y),female( X)");
		assertEquals(1, engine.getProgramSize());

		PrologVariable x = provider.newVariable("X", 0);
		PrologVariable y = provider.newVariable("Y", 1);
		engine.retract(provider.newStructure(mother, x, y), provider.newStructure(parent, x, y),
				provider.newStructure(female, x));
		assertEquals(0, engine.getProgramSize());
	}

	@Test
	public final void testUnify() {

		// with atom
		PrologAtom atom = provider.newAtom("smith");
		PrologAtom atom1 = provider.newAtom("doe");
		// true because the atoms are equals
		assertTrue(engine.unify(atom, atom));
		// false because the atoms are different
		assertFalse(engine.unify(atom, atom1));

	}

	@Test
	public final void testMatch() {

		Map<String, PrologTerm> map = new HashMap<String, PrologTerm>(1);
		PrologTerm variable = provider.newVariable("X", 0);
		PrologTerm atom = provider.newAtom("doe");
		PrologEngine engine = provider.newEngine();
		map.put("X", atom);
		assertEquals(map, engine.match(variable, atom));
		PrologTerm variables = provider.newStructure("struct", variable);
		PrologTerm atoms = provider.newStructure("struct", atom);
		map.clear();
		map.put("X", atom);
		assertEquals(map, engine.match(variables, atoms));

	}

	@Test
	public final void testContainsString() {

		engine.assertz("parent( pam, bob)");
		engine.assertz("parent( tom, bob)");
		engine.assertz("parent( tom, liz)");
		engine.assertz("parent( bob, ann)");
		engine.assertz("parent( bob, pat)");
		engine.assertz("parent( pat, jim)");
		engine.assertz("female( pam)");
		engine.assertz("male( tom)");
		engine.assertz("male( bob)");
		engine.assertz("female( liz)");
		engine.assertz("female( ann)");
		engine.assertz("female( pat)");
		engine.assertz("male( jim)");
		engine.assertz("offspring( Y, X):-parent( X, Y)");
		engine.assertz("mother( X, Y):-parent( X, Y),female( X)");
		engine.assertz("grandparent( X, Z):-parent( X, Y),parent( Y, Z)");
		engine.assertz("sister( X, Y):-parent( Z, X),parent( Z, Y),female( X),different( X, Y)");
		engine.assertz("predecessor( X, Z):-parent( X, Z)");
		engine.assertz("predecessor( X, Z):-parent( X, Y),predecessor( Y, Z)");
		engine.assertz("different( X, X) :- !, fail");
		engine.assertz("different( X, Y)");

		assertTrue(engine.contains("mother(X,Y)"));

	}

	@Test
	public final void testContainsPrologTermPrologTermArray() {

		engine.assertz("parent( pam, bob)");
		engine.assertz("parent( tom, bob)");
		engine.assertz("parent( tom, liz)");
		engine.assertz("parent( bob, ann)");
		engine.assertz("parent( bob, pat)");
		engine.assertz("parent( pat, jim)");
		engine.assertz("female( pam)");
		engine.assertz("male( tom)");
		engine.assertz("male( bob)");
		engine.assertz("female( liz)");
		engine.assertz("female( ann)");
		engine.assertz("female( pat)");
		engine.assertz("male( jim)");
		engine.assertz("offspring( Y, X):-parent( X, Y)");
		engine.assertz("mother( X, Y):-parent( X, Y),female( X)");
		engine.assertz("grandparent( X, Z):-parent( X, Y),parent( Y, Z)");
		engine.assertz("sister( X, Y):-parent( Z, X),parent( Z, Y),female( X),different( X, Y)");
		engine.assertz("predecessor( X, Z):-parent( X, Z)");
		engine.assertz("predecessor( X, Z):-parent( X, Y),predecessor( Y, Z)");
		engine.assertz("different( X, X) :- !, fail");
		engine.assertz("different( X, Y)");

		x = provider.newVariable("X", 0);
		y = provider.newVariable("Y", 1);
		z = provider.newVariable("Z", 2);

		assertTrue(engine.contains(provider.newStructure(mother, x, y)));

		x = provider.newVariable("X", 0);
		y = provider.newVariable("Y", 1);
		z = provider.newVariable("Z", 2);

		assertTrue(engine.contains(provider.newStructure(mother, x, y), provider.newStructure(grandparent, x, z)));

	}

	@Test
	public final void testFindString() {

		engine.assertz("parent( pam, bob)");
		engine.assertz("parent( tom, bob)");
		engine.assertz("parent( tom, liz)");
		engine.assertz("parent( bob, ann)");
		engine.assertz("parent( bob, pat)");
		engine.assertz("parent( pat, jim)");
		engine.assertz("female( pam)");
		engine.assertz("male( tom)");
		engine.assertz("male( bob)");
		engine.assertz("female( liz)");
		engine.assertz("female( ann)");
		engine.assertz("female( pat)");
		engine.assertz("male( jim)");
		engine.assertz("offspring( Y, X):-parent( X, Y)");
		engine.assertz("mother( X, Y):-parent( X, Y),female( X)");
		engine.assertz("grandparent( X, Z):-parent( X, Y),parent( Y, Z)");
		engine.assertz("sister( X, Y):-parent( Z, X),parent( Z, Y),female( X),different( X, Y)");
		engine.assertz("predecessor( X, Z):-parent( X, Z)");
		engine.assertz("predecessor( X, Z):-parent( X, Y),predecessor( Y, Z)");
		engine.assertz("different( X, X) :- !, fail");
		engine.assertz("different( X, Y)");

		famillySolutionMap.put("X", pam);
		famillySolutionMap.put("Y", bob);

		solutionMap = engine.queryOne("mother(X,Y)");
		assertEquals(famillySolutionMap, solutionMap);

	}

	@Test
	public final void testFindPrologTerm() {

		engine.assertz("parent( pam, bob)");
		engine.assertz("parent( tom, bob)");
		engine.assertz("parent( tom, liz)");
		engine.assertz("parent( bob, ann)");
		engine.assertz("parent( bob, pat)");
		engine.assertz("parent( pat, jim)");
		engine.assertz("female( pam)");
		engine.assertz("male( tom)");
		engine.assertz("male( bob)");
		engine.assertz("female( liz)");
		engine.assertz("female( ann)");
		engine.assertz("female( pat)");
		engine.assertz("male( jim)");
		engine.assertz("offspring( Y, X):-parent( X, Y)");
		engine.assertz("mother( X, Y):-parent( X, Y),female( X)");
		engine.assertz("grandparent( X, Z):-parent( X, Y),parent( Y, Z)");
		engine.assertz("sister( X, Y):-parent( Z, X),parent( Z, Y),female( X),different( X, Y)");
		engine.assertz("predecessor( X, Z):-parent( X, Z)");
		engine.assertz("predecessor( X, Z):-parent( X, Y),predecessor( Y, Z)");
		engine.assertz("different( X, X) :- !, fail");
		engine.assertz("different( X, Y)");

		famillySolutionMap.put("X", pam);
		famillySolutionMap.put("Y", bob);

		PrologVariable x = provider.newVariable("X", 0);
		PrologVariable y = provider.newVariable("Y", 1);
		solutionMap = engine.queryOne(provider.newStructure(mother, x, y));
		assertEquals(famillySolutionMap, solutionMap);
	}

	@Test
	public final void testFindPrologTermArray() {

		engine.assertz("parent( pam, bob)");
		engine.assertz("parent( tom, bob)");
		engine.assertz("parent( tom, liz)");
		engine.assertz("parent( bob, ann)");
		engine.assertz("parent( bob, pat)");
		engine.assertz("parent( pat, jim)");
		engine.assertz("female( pam)");
		engine.assertz("male( tom)");
		engine.assertz("male( bob)");
		engine.assertz("female( liz)");
		engine.assertz("female( ann)");
		engine.assertz("female( pat)");
		engine.assertz("male( jim)");
		engine.assertz("offspring( Y, X):-parent( X, Y)");
		engine.assertz("mother( X, Y):-parent( X, Y),female( X)");
		engine.assertz("grandparent( X, Z):-parent( X, Y),parent( Y, Z)");
		engine.assertz("sister( X, Y):-parent( Z, X),parent( Z, Y),female( X),different( X, Y)");
		engine.assertz("predecessor( X, Z):-parent( X, Z)");
		engine.assertz("predecessor( X, Z):-parent( X, Y),predecessor( Y, Z)");
		engine.assertz("different( X, X) :- !, fail");
		engine.assertz("different( X, Y)");

		famillySolutionMap.put("X", pam);
		famillySolutionMap.put("Y", bob);
		famillySolutionMap.put("Z", ann);

		x = provider.newVariable("X", 0);
		y = provider.newVariable("Y", 1);
		z = provider.newVariable("Z", 2);

		solutionMap = engine.queryOne(provider.newStructure(mother, x, y), provider.newStructure(grandparent, x, z));

		assertEquals(famillySolutionMap, solutionMap);
	}

	@Test
	public final void testFindAllString() {

		engine.assertz("parent( pam, bob)");
		engine.assertz("parent( tom, bob)");
		engine.assertz("parent( tom, liz)");
		engine.assertz("parent( bob, ann)");
		engine.assertz("parent( bob, pat)");
		engine.assertz("parent( pat, jim)");
		engine.assertz("female( pam)");
		engine.assertz("male( tom)");
		engine.assertz("male( bob)");
		engine.assertz("female( liz)");
		engine.assertz("female( ann)");
		engine.assertz("female( pat)");
		engine.assertz("male( jim)");
		engine.assertz("offspring( Y, X):-parent( X, Y)");
		engine.assertz("mother( X, Y):-parent( X, Y),female( X)");
		engine.assertz("grandparent( X, Z):-parent( X, Y),parent( Y, Z)");
		engine.assertz("sister( X, Y):-parent( Z, X),parent( Z, Y),female( X),different( X, Y)");
		engine.assertz("predecessor( X, Z):-parent( X, Z)");
		engine.assertz("predecessor( X, Z):-parent( X, Y),predecessor( Y, Z)");
		engine.assertz("different( X, X) :- !, fail");
		engine.assertz("different( X, Y)");

		List<Map<String, PrologTerm>> famillyAll = new ArrayList<Map<String, PrologTerm>>(6);
		solutionMap = new HashMap<String, PrologTerm>();
		solutionMap.put("X", pam);
		solutionMap.put("Y", bob);
		famillyAll.add(0, solutionMap);
		solutionMap = new HashMap<String, PrologTerm>();
		solutionMap.put("X", tom);
		solutionMap.put("Y", bob);
		famillyAll.add(1, solutionMap);
		solutionMap = new HashMap<String, PrologTerm>();
		solutionMap.put("X", tom);
		solutionMap.put("Y", liz);
		famillyAll.add(2, solutionMap);
		solutionMap = new HashMap<String, PrologTerm>();
		solutionMap.put("X", bob);
		solutionMap.put("Y", ann);
		famillyAll.add(3, solutionMap);
		solutionMap = new HashMap<String, PrologTerm>();
		solutionMap.put("X", bob);
		solutionMap.put("Y", pat);
		famillyAll.add(4, solutionMap);
		solutionMap = new HashMap<String, PrologTerm>();
		solutionMap.put("X", pat);
		solutionMap.put("Y", jim);
		famillyAll.add(5, solutionMap);

		List<Map<String, PrologTerm>> allSolutionMap = engine.queryAll("parent( X, Y)");
		assertEquals(famillyAll, allSolutionMap);

	}

	@Test
	public final void testFindAllPrologTerm() {

		engine.assertz("parent( pam, bob)");
		engine.assertz("parent( tom, bob)");
		engine.assertz("parent( tom, liz)");
		engine.assertz("parent( bob, ann)");
		engine.assertz("parent( bob, pat)");
		engine.assertz("parent( pat, jim)");
		engine.assertz("female( pam)");
		engine.assertz("male( tom)");
		engine.assertz("male( bob)");
		engine.assertz("female( liz)");
		engine.assertz("female( ann)");
		engine.assertz("female( pat)");
		engine.assertz("male( jim)");
		engine.assertz("offspring( Y, X):-parent( X, Y)");
		engine.assertz("mother( X, Y):-parent( X, Y),female( X)");
		engine.assertz("grandparent( X, Z):-parent( X, Y),parent( Y, Z)");
		engine.assertz("sister( X, Y):-parent( Z, X),parent( Z, Y),female( X),different( X, Y)");
		engine.assertz("predecessor( X, Z):-parent( X, Z)");
		engine.assertz("predecessor( X, Z):-parent( X, Y),predecessor( Y, Z)");
		engine.assertz("different( X, X) :- !, fail");
		engine.assertz("different( X, Y)");

		List<Map<String, PrologTerm>> famillyAll = new ArrayList<Map<String, PrologTerm>>(6);
		solutionMap = new HashMap<String, PrologTerm>();
		solutionMap.put("X", pam);
		solutionMap.put("Y", bob);
		famillyAll.add(0, solutionMap);
		solutionMap = new HashMap<String, PrologTerm>();
		solutionMap.put("X", tom);
		solutionMap.put("Y", bob);
		famillyAll.add(1, solutionMap);
		solutionMap = new HashMap<String, PrologTerm>();
		solutionMap.put("X", tom);
		solutionMap.put("Y", liz);
		famillyAll.add(2, solutionMap);
		solutionMap = new HashMap<String, PrologTerm>();
		solutionMap.put("X", bob);
		solutionMap.put("Y", ann);
		famillyAll.add(3, solutionMap);
		solutionMap = new HashMap<String, PrologTerm>();
		solutionMap.put("X", bob);
		solutionMap.put("Y", pat);
		famillyAll.add(4, solutionMap);
		solutionMap = new HashMap<String, PrologTerm>();
		solutionMap.put("X", pat);
		solutionMap.put("Y", jim);
		famillyAll.add(5, solutionMap);

		x = provider.newVariable("X", 0);
		y = provider.newVariable("Y", 1);
		List<Map<String, PrologTerm>> allSolutionMap = engine.queryAll(provider.newStructure(parent, x, y));
		assertEquals(famillyAll, allSolutionMap);
	}

	@Test
	public final void testFindAllPrologTermArray() {

		engine.assertz("parent( pam, bob)");
		engine.assertz("parent( tom, bob)");
		engine.assertz("parent( tom, liz)");
		engine.assertz("parent( bob, ann)");
		engine.assertz("parent( bob, pat)");
		engine.assertz("parent( pat, jim)");
		engine.assertz("female( pam)");
		engine.assertz("male( tom)");
		engine.assertz("male( bob)");
		engine.assertz("female( liz)");
		engine.assertz("female( ann)");
		engine.assertz("female( pat)");
		engine.assertz("male( jim)");
		engine.assertz("offspring( Y, X):-parent( X, Y)");
		engine.assertz("mother( X, Y):-parent( X, Y),female( X)");
		engine.assertz("grandparent( X, Z):-parent( X, Y),parent( Y, Z)");
		engine.assertz("sister( X, Y):-parent( Z, X),parent( Z, Y),female( X),different( X, Y)");
		engine.assertz("predecessor( X, Z):-parent( X, Z)");
		engine.assertz("predecessor( X, Z):-parent( X, Y),predecessor( Y, Z)");
		engine.assertz("different( X, X) :- !, fail");
		engine.assertz("different( X, Y)");

		List<Map<String, PrologTerm>> famillyAll = new ArrayList<Map<String, PrologTerm>>(6);
		solutionMap = new HashMap<String, PrologTerm>();
		solutionMap.put("X", pam);
		solutionMap.put("Y", bob);
		solutionMap.put("Z", ann);
		famillyAll.add(0, solutionMap);
		solutionMap = new HashMap<String, PrologTerm>();
		solutionMap.put("X", pam);
		solutionMap.put("Y", bob);
		solutionMap.put("Z", pat);
		famillyAll.add(1, solutionMap);

		x = provider.newVariable("X", 0);
		y = provider.newVariable("Y", 1);
		z = provider.newVariable("Z", 2);
		List<Map<String, PrologTerm>> allSolutionMap = engine.queryAll(provider.newStructure(mother, x, y),
				provider.newStructure(grandparent, x, z));

		assertEquals(famillyAll, allSolutionMap);
	}

	@Test
	public final void testCreateQueryString() {

		// employee relationship
		engine.assertz("employee( mcardon, 1, 5 )");
		engine.assertz("employee( treeman, 2, 3 )");
		engine.assertz("employee( chapman, 1, 2 )");
		engine.assertz("employee( claessen, 4, 1 )");
		engine.assertz("employee( petersen, 5, 8 )");
		engine.assertz("employee( cohn, 1, 7 )");
		engine.assertz("employee( duffy, 1, 9 )");

		// department relationship
		engine.assertz("department( 1, board )");
		engine.assertz("department( 2, human_resources )");
		engine.assertz("department( 3, production )");
		engine.assertz("department( 4, technical_services )");
		engine.assertz("department( 5, administration )");

		// salary relationship
		engine.assertz("salary( 1, 1000 )");
		engine.assertz("salary( 2, 1500 )");
		engine.assertz("salary( 3, 2000 )");
		engine.assertz("salary( 4, 2500 )");
		engine.assertz("salary( 5, 3000 )");
		engine.assertz("salary( 6, 3500 )");
		engine.assertz("salary( 7, 4000 )");
		engine.assertz("salary( 8, 4500 )");
		engine.assertz("salary( 9, 5000 )");

		query = engine.query("employee(Name,Dpto,Scale),department(Dpto,DepartmentName),salary(Scale,Money)");
		Map<String, PrologTerm>[] allSolutionMap = query.allVariablesSolutions();

		Map<String, PrologTerm> solutionMap = allSolutionMap[0];
		assertEquals(mcardon, solutionMap.get("Name"));
		assertEquals(one, solutionMap.get("Dpto"));
		assertEquals(five, solutionMap.get("Scale"));
		assertEquals(board, solutionMap.get("DepartmentName"));
		assertEquals(threeThousand, solutionMap.get("Money"));

		solutionMap = allSolutionMap[1];
		assertEquals(treeman, solutionMap.get("Name"));
		assertEquals(two, solutionMap.get("Dpto"));
		assertEquals(three, solutionMap.get("Scale"));
		assertEquals(human_resources, solutionMap.get("DepartmentName"));
		assertEquals(twoThousand, solutionMap.get("Money"));

		solutionMap = allSolutionMap[2];
		assertEquals(chapman, solutionMap.get("Name"));
		assertEquals(one, solutionMap.get("Dpto"));
		assertEquals(two, solutionMap.get("Scale"));
		assertEquals(board, solutionMap.get("DepartmentName"));
		assertEquals(thousandFiveHundred, solutionMap.get("Money"));

		solutionMap = allSolutionMap[3];
		assertEquals(claessen, solutionMap.get("Name"));
		assertEquals(four, solutionMap.get("Dpto"));
		assertEquals(one, solutionMap.get("Scale"));
		assertEquals(technical_services, solutionMap.get("DepartmentName"));
		assertEquals(thousand, solutionMap.get("Money"));

		solutionMap = allSolutionMap[4];
		assertEquals(petersen, solutionMap.get("Name"));
		assertEquals(five, solutionMap.get("Dpto"));
		assertEquals(eight, solutionMap.get("Scale"));
		assertEquals(administration, solutionMap.get("DepartmentName"));
		assertEquals(fourThousandFiveHundred, solutionMap.get("Money"));

		solutionMap = allSolutionMap[5];
		assertEquals(cohn, solutionMap.get("Name"));
		assertEquals(one, solutionMap.get("Dpto"));
		assertEquals(seven, solutionMap.get("Scale"));
		assertEquals(board, solutionMap.get("DepartmentName"));
		assertEquals(fourThousand, solutionMap.get("Money"));

		solutionMap = allSolutionMap[6];
		assertEquals(duffy, solutionMap.get("Name"));
		assertEquals(one, solutionMap.get("Dpto"));
		assertEquals(nine, solutionMap.get("Scale"));
		assertEquals(board, solutionMap.get("DepartmentName"));
		assertEquals(fiveThousand, solutionMap.get("Money"));

	}

	@Test
	public final void testCreateQueryPrologTermArray() {

		// employee relationship
		engine.assertz("employee( mcardon, 1, 5 )");
		engine.assertz("employee( treeman, 2, 3 )");
		engine.assertz("employee( chapman, 1, 2 )");
		engine.assertz("employee( claessen, 4, 1 )");
		engine.assertz("employee( petersen, 5, 8 )");
		engine.assertz("employee( cohn, 1, 7 )");
		engine.assertz("employee( duffy, 1, 9 )");

		// department relationship
		engine.assertz("department( 1, board )");
		engine.assertz("department( 2, human_resources )");
		engine.assertz("department( 3, production )");
		engine.assertz("department( 4, technical_services )");
		engine.assertz("department( 5, administration )");

		// salary relationship
		engine.assertz("salary( 1, 1000 )");
		engine.assertz("salary( 2, 1500 )");
		engine.assertz("salary( 3, 2000 )");
		engine.assertz("salary( 4, 2500 )");
		engine.assertz("salary( 5, 3000 )");
		engine.assertz("salary( 6, 3500 )");
		engine.assertz("salary( 7, 4000 )");
		engine.assertz("salary( 8, 4500 )");
		engine.assertz("salary( 9, 5000 )");

		PrologStructure employee = provider.newStructure("employee", name, dpto, scale);
		PrologStructure department = provider.newStructure("department", dpto, dptoName);
		PrologStructure salary = provider.newStructure("salary", scale, money);

		query = engine.query(employee, department, salary);
		Map<String, PrologTerm>[] allSolutionMap = query.allVariablesSolutions();

		Map<String, PrologTerm> solutionMap = allSolutionMap[0];
		assertEquals(mcardon, solutionMap.get("Name"));
		assertEquals(one, solutionMap.get("Dpto"));
		assertEquals(five, solutionMap.get("Scale"));
		assertEquals(board, solutionMap.get("DepartmentName"));
		assertEquals(threeThousand, solutionMap.get("Money"));

		solutionMap = allSolutionMap[1];
		assertEquals(treeman, solutionMap.get("Name"));
		assertEquals(two, solutionMap.get("Dpto"));
		assertEquals(three, solutionMap.get("Scale"));
		assertEquals(human_resources, solutionMap.get("DepartmentName"));
		assertEquals(twoThousand, solutionMap.get("Money"));

		solutionMap = allSolutionMap[2];
		assertEquals(chapman, solutionMap.get("Name"));
		assertEquals(one, solutionMap.get("Dpto"));
		assertEquals(two, solutionMap.get("Scale"));
		assertEquals(board, solutionMap.get("DepartmentName"));
		assertEquals(thousandFiveHundred, solutionMap.get("Money"));

		solutionMap = allSolutionMap[3];
		assertEquals(claessen, solutionMap.get("Name"));
		assertEquals(four, solutionMap.get("Dpto"));
		assertEquals(one, solutionMap.get("Scale"));
		assertEquals(technical_services, solutionMap.get("DepartmentName"));
		assertEquals(thousand, solutionMap.get("Money"));

		solutionMap = allSolutionMap[4];
		assertEquals(petersen, solutionMap.get("Name"));
		assertEquals(five, solutionMap.get("Dpto"));
		assertEquals(eight, solutionMap.get("Scale"));
		assertEquals(administration, solutionMap.get("DepartmentName"));
		assertEquals(fourThousandFiveHundred, solutionMap.get("Money"));

		solutionMap = allSolutionMap[5];
		assertEquals(cohn, solutionMap.get("Name"));
		assertEquals(one, solutionMap.get("Dpto"));
		assertEquals(seven, solutionMap.get("Scale"));
		assertEquals(board, solutionMap.get("DepartmentName"));
		assertEquals(fourThousand, solutionMap.get("Money"));

		solutionMap = allSolutionMap[6];
		assertEquals(duffy, solutionMap.get("Name"));
		assertEquals(one, solutionMap.get("Dpto"));
		assertEquals(nine, solutionMap.get("Scale"));
		assertEquals(board, solutionMap.get("DepartmentName"));
		assertEquals(fiveThousand, solutionMap.get("Money"));
	}

	@Test
	public final void testOperator() {

		assertFalse(engine.currentOperator(1200, "xfx", "<=="));
		engine.operator(1200, "xfx", "<==");
		assertTrue(engine.currentOperator(1200, "xfx", "<=="));

	}

	@Test
	public final void testCurrentPredicates() {
		Set<ProjogIndicator> predicates = new HashSet<ProjogIndicator>();
		org.projog.api.Projog projog = new org.projog.api.Projog();
		Set<PredicateKey> ps = projog.getKnowledgeBase().getPredicates().getAllDefinedPredicateKeys();
		Map<PredicateKey, UserDefinedPredicateFactory> m = projog.getKnowledgeBase().getPredicates()
				.getUserDefinedPredicates();
		for (PredicateKey predicateKey : ps) {
			if (!m.containsKey(predicateKey)) {
				int arity = predicateKey.getNumArgs();
				String functor = predicateKey.getName();
				ProjogIndicator pi = new ProjogIndicator(functor, arity);
				predicates.add(pi);
			}
		}
		assertEquals(predicates, engine.currentPredicates());
	}

	@Test
	public final void testCurrentOperators() {
		Projog projog = new Projog();
		Operands ops = projog.getKnowledgeBase().getOperands();
		Set<PrologOperator> operators = new HashSet<PrologOperator>();
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
		assertEquals(operators, engine.currentOperators());
	}

	@Test
	public final void testCurrentPredicate() {

		engine.include("family.pl");
		engine.include("company.pl");
		engine.include("zoo.pl");

		// user defined
		assertTrue(engine.currentPredicate("predecessor", 2));
		assertTrue(engine.currentPredicate("grandparent", 2));
		assertTrue(engine.currentPredicate("different", 2));
		assertTrue(engine.currentPredicate("offspring", 2));
		assertTrue(engine.currentPredicate("parent", 2));
		assertTrue(engine.currentPredicate("mother", 2));
		assertTrue(engine.currentPredicate("sister", 2));
		assertTrue(engine.currentPredicate("female", 1));
		assertTrue(engine.currentPredicate("male", 1));

		assertTrue(engine.currentPredicate("big", 1));
		assertTrue(engine.currentPredicate("small", 1));
		assertTrue(engine.currentPredicate("dark", 1));
		assertTrue(engine.currentPredicate("gray", 1));
		assertTrue(engine.currentPredicate("brown", 1));
		assertTrue(engine.currentPredicate("black", 1));

		assertTrue(engine.currentPredicate("salary", 2));
		assertTrue(engine.currentPredicate("employee", 3));
		assertTrue(engine.currentPredicate("department", 2));

		// supported built-ins
		assertTrue(engine.currentPredicate("see", 1));
		assertTrue(engine.currentPredicate("nonvar", 1));
		assertTrue(engine.currentPredicate("atomic", 1));
		assertTrue(engine.currentPredicate("number", 1));
		assertTrue(engine.currentPredicate("atom_chars", 2));
//		assertTrue(engine.currentPredicate("throw", 1));
		assertTrue(engine.currentPredicate("var", 1));
//		assertTrue(engine.currentPredicate("char_code", 2));
//		assertTrue(engine.currentPredicate("telling", 1));
		assertTrue(engine.currentPredicate("seen", 0));
		assertTrue(engine.currentPredicate("notrace", 0));
		assertTrue(engine.currentPredicate("compound", 1));

	}

	@Test
	public final void testCurrentOperator() {

		assertTrue(engine.currentOperator(700, "xfx", "is"));
//		assertTrue(engine.currentOperator(900, "fy", "not"));
//		assertTrue(engine.currentOperator(200, "xfx", "**"));
		assertTrue(engine.currentOperator(700, "xfx", "=.."));
		assertTrue(engine.currentOperator(400, "yfx", "<<"));
		assertTrue(engine.currentOperator(400, "yfx", "/"));
		assertTrue(engine.currentOperator(400, "yfx", "mod"));
		assertTrue(engine.currentOperator(500, "yfx", "+"));
//		assertTrue(engine.currentOperator(200, "fx", "exp"));
//		assertTrue(engine.currentOperator(200, "fx", "cos"));
		assertTrue(engine.currentOperator(400, "yfx", "*"));
		assertTrue(engine.currentOperator(1050, "xfy", "->"));
		assertTrue(engine.currentOperator(700, "xfx", ">"));
		assertTrue(engine.currentOperator(700, "xfx", "\\="));
//		assertTrue(engine.currentOperator(200, "xfx", "as"));
//		assertTrue(engine.currentOperator(200, "xfy", "^"));
//		assertTrue(engine.currentOperator(300, "yfx", "div"));
		assertTrue(engine.currentOperator(700, "xfx", "@<"));
		assertTrue(engine.currentOperator(1100, "xfy", ";"));
		assertTrue(engine.currentOperator(700, "xfx", "=<"));
		assertTrue(engine.currentOperator(1200, "fx", ":-"));
//		assertTrue(engine.currentOperator(600, "xfx", "."));
		assertTrue(engine.currentOperator(700, "xfx", "=\\="));
		assertTrue(engine.currentOperator(200, "fy", "-"));
//		assertTrue(engine.currentOperator(200, "fx", "sin"));
		assertTrue(engine.currentOperator(700, "xfx", "="));
//		assertTrue(engine.currentOperator(800, "xfx", "<-"));
//		assertTrue(engine.currentOperator(200, "fx", "log"));
//		assertTrue(engine.currentOperator(850, "xfx", "returns"));
		assertTrue(engine.currentOperator(700, "xfx", ">="));
		assertTrue(engine.currentOperator(700, "xfx", "@=<"));
		assertTrue(engine.currentOperator(400, "yfx", ">>"));
//		assertTrue(engine.currentOperator(500, "yfx", "-"));
		assertTrue(engine.currentOperator(1200, "fx", "?-"));
		assertTrue(engine.currentOperator(700, "xfx", "=="));
		assertTrue(engine.currentOperator(700, "xfx", "\\=="));
		assertTrue(engine.currentOperator(700, "xfx", "<"));
		assertTrue(engine.currentOperator(700, "xfx", "=:="));
		assertTrue(engine.currentOperator(900, "fy", "\\+"));
//		assertTrue(engine.currentOperator(200, "fx", "sqrt"));
		assertTrue(engine.currentOperator(1000, "xfy", ","));
		assertTrue(engine.currentOperator(700, "xfx", "@>"));
//		assertTrue(engine.currentOperator(200, "fy", "\\"));
		assertTrue(engine.currentOperator(400, "yfx", "//"));
		assertTrue(engine.currentOperator(700, "xfx", "@>="));
		assertTrue(engine.currentOperator(400, "yfx", "rem"));
//		assertTrue(engine.currentOperator(200, "fx", "atan"));
//		assertTrue(engine.currentOperator(1200, "xfx", ":-"));
//		assertTrue(engine.currentOperator(500, "yfx", "\\/"));
//		assertTrue(engine.currentOperator(500, "yfx", "/\\"));

	}

	@Test
	public final void testGetLicense() {
		assertEquals(Licenses.APACHE_V2, engine.getLicense());
	}

	@Test
	public final void testGetVersion() {
		assertEquals("0.6.0", engine.getVersion());
	}

	@Test
	public final void testGetName() {
		assertEquals("Projog", engine.getName());
	}

	@Test
	public final void testDispose() {
		engine.dispose();
		assertTrue(engine.isProgramEmpty());
		assertEquals(0, engine.getProgramSize());
	}

	@Test
	public final void testIterator() {

		engine = provider.newEngine();
		engine.consult("family.pl");

		int counter = 0;
		Iterator<?> i = engine.iterator();
		int size = engine.getProgramSize();

		assertNotNull(i);
		while (i.hasNext()) {
			counter++;
			i.next();
		}

		assertEquals(size, counter);

	}

	@Test
	public final void testToString() {
		assertEquals(engine.getName() + " " + engine.getVersion(), engine.toString());
	}

	@Test
	public final void testEquals() {
		assertEquals(provider.newEngine(), engine);
	}

	@Test
	@Ignore
	public final void testHashCode() {
		// assertEquals(provider.newEngine().hashCode(), engine.hashCode())
		assertTrue(engine.hashCode() > 0);
	}

	@Test
	public void testRunOnWindows() {
		assertTrue(engine.runOnWindows() == engine.getOSName().startsWith("Windows"));
	}

	@Test
	public void testRunOnLinux() {
		assertTrue(engine.runOnLinux() == engine.getOSName().startsWith("Linux"));
	}

	@Test
	public void testRunOnOSX() {
		assertTrue(engine.runOnOSX() == (engine.getOSName().equals("Mac OS X") || engine.getOSName().equals("Darwin")));
	}

	@Test
	public void testGetOSName() {
		assertEquals(System.getProperty("os.name"), engine.getOSName());
	}

	@Test
	public void testGetOSArch() {
		assertEquals(System.getProperty("os.arch"), engine.getOSArch());
	}

	@Test
	public void testConsultReader() throws FileNotFoundException {

		engine = provider.newEngine();
		engine.consult(new FileReader("family.pl"));
		assertFalse(engine.isProgramEmpty());
		assertEquals(21, engine.getProgramSize());
		engine.dispose();

		engine = provider.newEngine();
		engine.consult(new FileReader("company.pl"));
		assertFalse(engine.isProgramEmpty());
		assertEquals(21, engine.getProgramSize());
		engine.dispose();

		engine = provider.newEngine();
		engine.consult(new FileReader("zoo.pl"));
		assertFalse(engine.isProgramEmpty());
		assertEquals(8, engine.getProgramSize());
		engine.dispose();

	}

	@Test
	public void testIncludeReader() throws FileNotFoundException {

		engine.include(new FileReader("family.pl"));
		assertFalse(engine.isProgramEmpty());
		assertEquals(21, engine.getProgramSize());
		engine.include(new FileReader("company.pl"));
		assertFalse(engine.isProgramEmpty());
		assertEquals(42, engine.getProgramSize());
		engine.include(new FileReader("zoo.pl"));
		assertFalse(engine.isProgramEmpty());
		assertEquals(50, engine.getProgramSize());

	}

	@Test
	@Ignore
	public void testPersistWriter() throws IOException {

		// Family save test case
		engine = provider.newEngine();

		engine.assertz(provider.newStructure(parent, pam, bob));
		engine.assertz(provider.newStructure(parent, tom, bob));
		engine.assertz(provider.newStructure(parent, tom, liz));
		engine.assertz(provider.newStructure(parent, bob, ann));
		engine.assertz(provider.newStructure(parent, bob, pat));
		engine.assertz(provider.newStructure(parent, pat, jim));

		engine.assertz(provider.newStructure(female, pam));
		engine.assertz(provider.newStructure(male, tom));
		engine.assertz(provider.newStructure(male, bob));
		engine.assertz(provider.newStructure(female, liz));
		engine.assertz(provider.newStructure(female, ann));
		engine.assertz(provider.newStructure(female, pat));
		engine.assertz(provider.newStructure(male, jim));

		x = provider.newVariable("X", 0);
		y = provider.newVariable("Y", 1);
		z = provider.newVariable("Z", 2);
		engine.assertz(provider.newStructure(offspring, x, y), provider.newStructure(parent, x, y));

		x = provider.newVariable("X", 0);
		y = provider.newVariable("Y", 1);
		engine.assertz(provider.newStructure(mother, x, y), provider.newStructure(parent, x, y),
				provider.newStructure(female, x));

		x = provider.newVariable("X", 0);
		y = provider.newVariable("Y", 1);
		z = provider.newVariable("Z", 2);
		engine.assertz(provider.newStructure(grandparent, x, z), provider.newStructure(parent, x, y),
				provider.newStructure(parent, y, z));

		x = provider.newVariable("X", 0);
		y = provider.newVariable("Y", 1);
		z = provider.newVariable("Z", 2);

		engine.assertz(provider.newStructure(sister, x, y), provider.newStructure(parent, z, x),
				provider.newStructure(parent, z, y), provider.newStructure(female, x),
				provider.newStructure(different, x, y));

		x = provider.newVariable("X", 0);
		engine.assertz(provider.newStructure(different, x, x), provider.prologCut(), provider.prologFail());

		x = provider.newVariable("X", 0);
		y = provider.newVariable("Y", 1);
		engine.assertz(provider.newStructure(different, x, y));

		x = provider.newVariable("X", 0);
		y = provider.newVariable("Y", 1);
		z = provider.newVariable("Z", 2);
		engine.assertz(provider.newStructure(predecessor, x, z), provider.newStructure(parent, x, z));

		x = provider.newVariable("X", 0);
		y = provider.newVariable("Y", 1);
		z = provider.newVariable("Z", 2);
		engine.assertz(provider.newStructure(predecessor, x, z), provider.newStructure(parent, x, y),
				provider.newStructure(predecessor, y, z));

		Writer writer = new FileWriter("family.pl");
		engine.persist(writer);
		writer.flush();
		writer.close();

		// Physic existence test
		File file = new File("family.pl");
		assertTrue(file.exists());
		assertTrue(file.length() > 0);

		// Logical program saved
		engine.consult("family.pl");
		assertFalse(engine.isProgramEmpty());
		assertEquals(21, engine.getProgramSize());
		engine.dispose();

		// Company save test case
		engine = provider.newEngine();

		// employee relationship
		engine.assertz(provider.newStructure(employee, mcardon, one, five));
		engine.assertz(provider.newStructure(employee, treeman, two, three));
		engine.assertz(provider.newStructure(employee, chapman, one, two));
		engine.assertz(provider.newStructure(employee, claessen, four, one));
		engine.assertz(provider.newStructure(employee, petersen, five, eight));
		engine.assertz(provider.newStructure(employee, cohn, one, seven));
		engine.assertz(provider.newStructure(employee, duffy, one, nine));

		// department relationship
		engine.assertz(provider.newStructure(department, one, board));
		engine.assertz(provider.newStructure(department, two, human_resources));
		engine.assertz(provider.newStructure(department, three, production));
		engine.assertz(provider.newStructure(department, four, technical_services));
		engine.assertz(provider.newStructure(department, five, administration));

		// salary relationship
		engine.assertz(provider.newStructure(salary, one, thousand));
		engine.assertz(provider.newStructure(salary, two, thousandFiveHundred));
		engine.assertz(provider.newStructure(salary, three, twoThousand));
		engine.assertz(provider.newStructure(salary, four, twoThousandFiveHundred));
		engine.assertz(provider.newStructure(salary, five, threeThousand));
		engine.assertz(provider.newStructure(salary, six, threeThousandFiveHundred));
		engine.assertz(provider.newStructure(salary, seven, fourThousand));
		engine.assertz(provider.newStructure(salary, eight, fourThousandFiveHundred));
		engine.assertz(provider.newStructure(salary, nine, fiveThousand));

		writer = new FileWriter("company.pl");
		engine.persist(writer);
		writer.flush();
		writer.close();

		// Physic existence test
		file = new File("company.pl");
		assertTrue(file.exists());
		assertTrue(file.length() > 0);

		// Logical program saved
		engine.consult("company.pl");
		assertFalse(engine.isProgramEmpty());
		assertEquals(21, engine.getProgramSize());
		engine.dispose();

		// Zoo save test case
		engine = provider.newEngine();

		engine.assertz(provider.newStructure("big", bear));
		engine.assertz(provider.newStructure("big", elephant));
		engine.assertz(provider.newStructure("small", cat));
		engine.assertz(provider.newStructure("brown", bear));
		engine.assertz(provider.newStructure("black", cat));
		engine.assertz(provider.newStructure("gray", elephant));

		// dark rules
		z = provider.newVariable("Z", 0);
		engine.assertz(provider.newStructure("dark", z), provider.newStructure("black", z));

		z = provider.newVariable("Z", 0);
		engine.assertz(provider.newStructure("dark", z), provider.newStructure("brown", z));

		writer = new FileWriter("zoo.pl");
		engine.persist(writer);
		writer.flush();
		writer.close();

		// Physic existence test
		file = new File("zoo.pl");
		assertTrue(file.exists());
		assertTrue(file.length() > 0);

		// Logical program saved
		engine.consult("zoo.pl");
		assertFalse(engine.isProgramEmpty());
		assertEquals(8, engine.getProgramSize());
		engine.dispose();

	}

	@Test
	public void testGetVendor() {
		assertEquals("projog.org", engine.getVendor());
	}

}
