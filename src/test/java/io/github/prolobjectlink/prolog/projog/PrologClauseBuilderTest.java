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
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import io.github.prolobjectlink.prolog.PrologAtom;
import io.github.prolobjectlink.prolog.PrologClauseBuilder;
import io.github.prolobjectlink.prolog.PrologEngine;
import io.github.prolobjectlink.prolog.PrologInteger;
import io.github.prolobjectlink.prolog.PrologQuery;
import io.github.prolobjectlink.prolog.PrologStructure;
import io.github.prolobjectlink.prolog.PrologTerm;
import io.github.prolobjectlink.prolog.PrologVariable;

public class PrologClauseBuilderTest extends PrologBaseTest {

	private PrologVariable x = provider.newVariable("X", 0);
	private PrologVariable z = provider.newVariable("Z", 1);

	private PrologAtom cat = provider.newAtom("cat");
	private PrologAtom bear = provider.newAtom("bear");
	private PrologAtom elephant = provider.newAtom("elephant");

	private PrologStructure darkX = provider.newStructure("dark", x);
	private PrologStructure darkZ = provider.newStructure("dark", z);

	private PrologStructure bigX = provider.newStructure("big", x);
	private PrologStructure bigBear = provider.newStructure("big", bear);
	private PrologStructure bigElephant = provider.newStructure("big", elephant);

	// private PrologStructure smallX = provider.newStructure("small", x);
	private PrologStructure smallCat = provider.newStructure("small", cat);

	private PrologStructure blackZ = provider.newStructure("black", z);
	private PrologStructure brownZ = provider.newStructure("brown", z);

	private PrologStructure blackCat = provider.newStructure("black", cat);
	private PrologStructure brownBear = provider.newStructure("brown", bear);
	private PrologStructure grayElephant = provider.newStructure("gray", elephant);

	private PrologEngine engine = provider.newEngine();
	private PrologClauseBuilder builder = engine.newClauseBuilder();

	// private PrologTerm[] catExpected = { cat };
	private PrologTerm[] bearExpected = { bear };

	@Before
	public void setUp() throws Exception {

	}

	@After
	public void tearDown() throws Exception {
		engine.dispose();
	}

	@Test
	public final void testBeginPrologTerm() {

		assertFalse(builder.begin(bigBear).clause());
		assertFalse(builder.begin(bigElephant).clause());
		assertFalse(builder.begin(smallCat).clause());
		assertFalse(builder.begin(brownBear).clause());
		assertFalse(builder.begin(blackCat).clause());
		assertFalse(builder.begin(grayElephant).clause());
		assertFalse(builder.begin(darkZ).neck(blackZ).clause());
		assertFalse(builder.begin(darkZ).neck(brownZ).clause());

		builder.begin(bigBear).assertz();
		builder.begin(bigElephant).assertz();
		builder.begin(smallCat).assertz();
		builder.begin(brownBear).assertz();
		builder.begin(blackCat).assertz();
		builder.begin(grayElephant).assertz();
		builder.begin(darkZ).neck(blackZ).assertz();
		builder.begin(darkZ).neck(brownZ).assertz();

		assertTrue(builder.begin(bigBear).clause());
		assertTrue(builder.begin(bigElephant).clause());
		assertTrue(builder.begin(smallCat).clause());
		assertTrue(builder.begin(brownBear).clause());
		assertTrue(builder.begin(blackCat).clause());
		assertTrue(builder.begin(grayElephant).clause());
		assertTrue(builder.begin(darkZ).neck(blackZ).clause());
		assertTrue(builder.begin(darkZ).neck(brownZ).clause());

		PrologQuery query = engine.query(darkX, bigX);
		assertArrayEquals(bearExpected, query.oneSolution());

	}

	@Test
	public final void testBeginStringPrologTermArray() {

		assertFalse(builder.begin("big", bear).clause());
		assertFalse(builder.begin("big", elephant).clause());
		assertFalse(builder.begin("small", cat).clause());
		assertFalse(builder.begin("brown", bear).clause());
		assertFalse(builder.begin("black", cat).clause());
		assertFalse(builder.begin("gray", elephant).clause());
		assertFalse(builder.begin("dark", z).neck("black", z).clause());
		assertFalse(builder.begin("dark", z).neck("brown", z).clause());

		builder.begin("big", bear).assertz();
		builder.begin("big", elephant).assertz();
		builder.begin("small", cat).assertz();
		builder.begin("brown", bear).assertz();
		builder.begin("black", cat).assertz();
		builder.begin("gray", elephant).assertz();
		builder.begin("dark", z).neck("black", z).assertz();
		builder.begin("dark", z).neck("brown", z).assertz();

		assertTrue(builder.begin("big", bear).clause());
		assertTrue(builder.begin("big", elephant).clause());
		assertTrue(builder.begin("small", cat).clause());
		assertTrue(builder.begin("brown", bear).clause());
		assertTrue(builder.begin("black", cat).clause());
		assertTrue(builder.begin("gray", elephant).clause());
		assertTrue(builder.begin("dark", z).neck("black", z).clause());
		assertTrue(builder.begin("dark", z).neck("brown", z).clause());

		PrologQuery query = engine.query(darkX, bigX);
		assertArrayEquals(bearExpected, query.oneSolution());

		builder.begin("big", bear).retract();
		builder.begin("big", elephant).retract();
		builder.begin("small", cat).retract();
		builder.begin("brown", bear).retract();
		builder.begin("black", cat).retract();
		builder.begin("gray", elephant).retract();

		// do not retract rules see org.projog.core.predicate.builtin.kb.Inspect.java
		// builder.begin("dark", z).neck("black", z).retract();
		// builder.begin("dark", z).neck("brown", z).retract();

		assertFalse(builder.begin("big", bear).clause());
		assertFalse(builder.begin("big", elephant).clause());
		assertFalse(builder.begin("small", cat).clause());
		assertFalse(builder.begin("brown", bear).clause());
		assertFalse(builder.begin("black", cat).clause());
		assertFalse(builder.begin("gray", elephant).clause());

		// do not clause for rules see org.projog.core.predicate.builtin.kb.Inspect.java
		// assertFalse(builder.begin("dark", z).neck("black", z).clause());
		// assertFalse(builder.begin("dark", z).neck("brown", z).clause());

	}

	@Test
	public final void testNeckPrologTerm() {

		builder.begin(darkZ).neck(blackZ).assertz();
		builder.begin(darkZ).neck(brownZ).assertz();

		assertTrue(builder.begin(darkZ).neck(blackZ).clause());
		assertTrue(builder.begin(darkZ).neck(brownZ).clause());

	}

	@Test
	public final void testNeckStringPrologTermArray() {

		builder.begin("dark", z).neck("black", z).assertz();
		builder.begin("dark", z).neck("brown", z).assertz();

		assertTrue(builder.begin("dark", z).neck("black", z).clause());
		assertTrue(builder.begin("dark", z).neck("brown", z).clause());

	}

	@Test
	public final void testCommaPrologTerm() {

		// factorial
		PrologVariable n = provider.newVariable("N", 0);
		PrologVariable nfact = provider.newVariable("Nfact", 1);
		PrologVariable n1 = provider.newVariable("N1", 0);
		PrologVariable nfact1 = provider.newVariable("Nfact1", 0);

		PrologStructure factorialBase = provider.newStructure("factorial", provider.newInteger(1),
				provider.newInteger(1));

		PrologTerm right = provider.newStructure(n, "-", provider.newInteger(1));
		PrologTerm exp = provider.newStructure(n1, "is", right);
		PrologStructure factorialRecursiveCall = provider.newStructure("factorial", n1, nfact1);
		PrologTerm right1 = provider.newStructure(n, "*", nfact1);
		PrologTerm exp1 = provider.newStructure(nfact, "is", right1);
		PrologStructure factorialProcedure = provider.newStructure("factorial", n, nfact);

		assertFalse(builder.begin(factorialBase).clause());
		assertFalse(builder.begin(factorialProcedure).neck(exp).comma(factorialRecursiveCall).comma(exp1).clause());

		builder.begin(factorialBase).assertz();
		builder.begin(factorialProcedure).neck(exp).comma(factorialRecursiveCall).comma(exp1).assertz();

		assertTrue(builder.begin(factorialBase).clause());
		assertTrue(builder.begin(factorialProcedure).neck(exp).comma(factorialRecursiveCall).comma(exp1).clause());

		PrologInteger five = provider.newInteger(5);
		PrologInteger hundredTwenty = provider.newInteger(120);
		PrologStructure factorialQuery = provider.newStructure("factorial", five, hundredTwenty);

		PrologQuery query = engine.query(factorialQuery);
		assertTrue(query.hasSolution());
		query.dispose();

		// fibonacci
		PrologStructure fibonacciBase0 = provider.newStructure("fibonacci", one, one);
		PrologStructure fibonacciBase1 = provider.newStructure("fibonacci", two, one);

		n = provider.newVariable("N", 0);
		n1 = provider.newVariable("N1", 0);
		PrologVariable n2 = provider.newVariable("N2", 0);
		PrologVariable f = provider.newVariable("F", 1);
		PrologVariable f1 = provider.newVariable("F1", 0);
		PrologVariable f2 = provider.newVariable("F2", 2);

		PrologTerm e1 = provider.newStructure(n, ">", provider.newInteger(2));
		PrologTerm e2 = provider.newStructure(n, "-", provider.newInteger(1));
		PrologTerm e3 = provider.newStructure(n1, "is", e2);
		PrologStructure fibonacciRecursiveCall1 = provider.newStructure("fibonacci", n1, f1);
		PrologTerm e4 = provider.newStructure(n, "-", provider.newInteger(2));
		PrologTerm e5 = provider.newStructure(n2, "is", e4);
		PrologStructure fibonacciRecursiveCall2 = provider.newStructure("fibonacci", n2, f2);
		PrologTerm e6 = provider.newStructure(f1, "+", f2);
		PrologTerm e7 = provider.newStructure(f, "is", e6);
		PrologStructure fibanacciProcedure = provider.newStructure("fibonacci", n, f);

		assertFalse(builder.begin(fibonacciBase0).clause());
		assertFalse(builder.begin(fibonacciBase1).clause());
		assertFalse(builder.begin(fibanacciProcedure).neck(e1).comma(e3).comma(fibonacciRecursiveCall1).comma(e5)
				.comma(fibonacciRecursiveCall2).comma(e7).clause());

		builder.begin(fibonacciBase0).assertz();
		builder.begin(fibonacciBase1).assertz();
		builder.begin(fibanacciProcedure).neck(e1).comma(e3).comma(fibonacciRecursiveCall1).comma(e5)
				.comma(fibonacciRecursiveCall2).comma(e7).assertz();

		assertTrue(builder.begin(fibonacciBase0).clause());
		assertTrue(builder.begin(fibonacciBase1).clause());
		assertTrue(builder.begin(fibanacciProcedure).neck(e1).comma(e3).comma(fibonacciRecursiveCall1).comma(e5)
				.comma(fibonacciRecursiveCall2).comma(e7).clause());

		PrologInteger seven = provider.newInteger(7);
		PrologInteger thirteen = provider.newInteger(13);
		PrologStructure fibonacciQuery = provider.newStructure("fibonacci", seven, thirteen);

		query = engine.query(fibonacciQuery);
		assertTrue(query.hasSolution());
		query.dispose();

	}

	@Test
	public final void testCommaStringPrologTermArray() {

		// factorial
		PrologVariable n = provider.newVariable("N", 0);
		PrologVariable nfact = provider.newVariable("Nfact", 1);
		PrologVariable n1 = provider.newVariable("N1", 0);
		PrologVariable nfact1 = provider.newVariable("Nfact1", 0);

		PrologTerm right = provider.newStructure(n, "-", provider.newInteger(1));
		PrologStructure factorialRecursiveCall = provider.newStructure("factorial", n1, nfact1);
		PrologTerm right1 = provider.newStructure(n, "*", nfact1);

		assertFalse(builder.begin("factorial", provider.newInteger(1), provider.newInteger(1)).clause());
		assertFalse(builder.begin("factorial", n, nfact).neck(n1, "is", right).comma(factorialRecursiveCall)
				.comma(nfact, "is", right1).clause());

		builder.begin("factorial", provider.newInteger(1), provider.newInteger(1)).assertz();
		builder.begin("factorial", n, nfact).neck(n1, "is", right).comma(factorialRecursiveCall)
				.comma(nfact, "is", right1).assertz();

		assertTrue(builder.begin("factorial", provider.newInteger(1), provider.newInteger(1)).clause());
		assertTrue(builder.begin("factorial", n, nfact).neck(n1, "is", right).comma(factorialRecursiveCall)
				.comma(nfact, "is", right1).clause());

		PrologInteger five = provider.newInteger(5);
		PrologInteger hundredTwenty = provider.newInteger(120);
		PrologStructure factorialQuery = provider.newStructure("factorial", five, hundredTwenty);

		PrologQuery query = engine.query(factorialQuery);
		assertTrue(query.hasSolution());
		query.dispose();

		n = provider.newVariable("N", 0);
		n1 = provider.newVariable("N1", 0);
		PrologVariable n2 = provider.newVariable("N2", 0);
		PrologVariable f = provider.newVariable("F", 1);
		PrologVariable f1 = provider.newVariable("F1", 0);
		PrologVariable f2 = provider.newVariable("F2", 2);

		PrologTerm e2 = provider.newStructure(n, "-", provider.newInteger(1));
		PrologTerm e4 = provider.newStructure(n, "-", provider.newInteger(2));
		PrologTerm e5 = provider.newStructure(n2, "is", e4);
		PrologTerm e6 = provider.newStructure(f1, "+", f2);
		PrologTerm e7 = provider.newStructure(f, "is", e6);

		assertFalse(builder.begin("fibonacci", provider.newInteger(1), provider.newInteger(1)).clause());
		assertFalse(builder.begin("fibonacci", provider.newInteger(2), provider.newInteger(1)).clause());
		assertFalse(builder.begin("fibonacci", n, f).neck(n, ">", provider.newInteger(2)).comma(n1, "is", e2)
				.comma("fibonacci", n1, f1).comma(e5).comma("fibonacci", n2, f2).comma(e7).clause());

		builder.begin("fibonacci", provider.newInteger(1), provider.newInteger(1)).assertz();
		builder.begin("fibonacci", provider.newInteger(2), provider.newInteger(1)).assertz();
		builder.begin("fibonacci", n, f).neck(n, ">", provider.newInteger(2)).comma(n1, "is", e2)
				.comma("fibonacci", n1, f1).comma(e5).comma("fibonacci", n2, f2).comma(e7).assertz();

		assertTrue(builder.begin("fibonacci", provider.newInteger(1), provider.newInteger(1)).clause());
		assertTrue(builder.begin("fibonacci", provider.newInteger(2), provider.newInteger(1)).clause());
		assertTrue(builder.begin("fibonacci", n, f).neck(n, ">", provider.newInteger(2)).comma(n1, "is", e2)
				.comma("fibonacci", n1, f1).comma(e5).comma("fibonacci", n2, f2).comma(e7).clause());

		PrologInteger seven = provider.newInteger(7);
		PrologInteger thirteen = provider.newInteger(13);
		PrologStructure fibonacciQuery = provider.newStructure("fibonacci", seven, thirteen);

		query = engine.query(fibonacciQuery);
		assertTrue(query.hasSolution());
		query.dispose();

	}

	@Test
	public final void testAsserta() {

		assertFalse(builder.begin(bigBear).clause());
		assertFalse(builder.begin(bigElephant).clause());
		assertFalse(builder.begin(smallCat).clause());
		assertFalse(builder.begin(brownBear).clause());
		assertFalse(builder.begin(blackCat).clause());
		assertFalse(builder.begin(grayElephant).clause());
		assertFalse(builder.begin(darkZ).neck(blackZ).clause());
		assertFalse(builder.begin(darkZ).neck(brownZ).clause());

		builder.begin(bigElephant).asserta();
		builder.begin(bigBear).asserta();
		builder.begin(smallCat).asserta();
		builder.begin(brownBear).asserta();
		builder.begin(blackCat).asserta();
		builder.begin(grayElephant).asserta();
		builder.begin(darkZ).neck(brownZ).asserta();
		builder.begin(darkZ).neck(blackZ).asserta();

		assertTrue(builder.begin(bigBear).clause());
		assertTrue(builder.begin(bigElephant).clause());
		assertTrue(builder.begin(smallCat).clause());
		assertTrue(builder.begin(brownBear).clause());
		assertTrue(builder.begin(blackCat).clause());
		assertTrue(builder.begin(grayElephant).clause());
		assertTrue(builder.begin(darkZ).neck(blackZ).clause());
		assertTrue(builder.begin(darkZ).neck(brownZ).clause());

		PrologQuery query = engine.query(darkX, bigX);
		assertArrayEquals(bearExpected, query.oneSolution());

	}

	@Test
	public final void testAssertz() {
		testBeginPrologTerm();
	}

	@Test
	public final void testClause() {
		testBeginPrologTerm();
	}

	@Test
	public final void testRetract() {

		testBeginPrologTerm();

		assertTrue(builder.begin("big", bear).clause());
		assertTrue(builder.begin("big", elephant).clause());
		assertTrue(builder.begin("small", cat).clause());
		assertTrue(builder.begin("brown", bear).clause());
		assertTrue(builder.begin("black", cat).clause());
		assertTrue(builder.begin("gray", elephant).clause());
		assertTrue(builder.begin("dark", z).neck("black", z).clause());
		assertTrue(builder.begin("dark", z).neck("brown", z).clause());

		builder.begin("big", bear).retract();
		builder.begin("big", elephant).retract();
		builder.begin("small", cat).retract();
		builder.begin("brown", bear).retract();
		builder.begin("black", cat).retract();
		builder.begin("gray", elephant).retract();

		// do not retract rules see org.projog.core.predicate.builtin.kb.Inspect.java
		// builder.begin("dark", z).neck("black", z).retract();
		// builder.begin("dark", z).neck("brown", z).retract();

		assertFalse(builder.begin("big", bear).clause());
		assertFalse(builder.begin("big", elephant).clause());
		assertFalse(builder.begin("small", cat).clause());
		assertFalse(builder.begin("brown", bear).clause());
		assertFalse(builder.begin("black", cat).clause());
		assertFalse(builder.begin("gray", elephant).clause());

		// do not clause for rules see org.projog.core.predicate.builtin.kb.Inspect.java
		// assertFalse(builder.begin("dark", z).neck("black", z).clause());
		// assertFalse(builder.begin("dark", z).neck("brown", z).clause());

	}

}
