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

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Set;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import io.github.prolobjectlink.prolog.PrologAtom;
import io.github.prolobjectlink.prolog.PrologDouble;
import io.github.prolobjectlink.prolog.PrologFloat;
import io.github.prolobjectlink.prolog.PrologInteger;
import io.github.prolobjectlink.prolog.PrologList;
import io.github.prolobjectlink.prolog.PrologLong;
import io.github.prolobjectlink.prolog.PrologMap;
import io.github.prolobjectlink.prolog.PrologStructure;
import io.github.prolobjectlink.prolog.PrologTerm;
import io.github.prolobjectlink.prolog.PrologTermType;
import io.github.prolobjectlink.prolog.PrologVariable;

public class PrologMapTest extends PrologCollectionTest {

	PrologTerm map = provider.newMap();

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testHashCode() {
		assertEquals(provider.newMap().hashCode(), map.hashCode());
	}

	@Test
	public void testIsList() {
		assertTrue(map.isList());
	}

	@Test
	public void testIsStructure() {
		assertFalse(map.isStructure());
	}

	@Test
	public void testIsEmptyList() {
		assertTrue(map.isEmptyList());
	}

	@Test
	public void testGetFunctor() {
		assertEquals(".", map.getFunctor());
	}

	@Test
	public void testGetArguments() {

		assertArrayEquals(new PrologTerm[0], map.getArguments());

		PrologMap m = provider.cast(map);

		m.put(x, elephant);
		m.put(y, cat);
		m.put(z, bear);

		PrologTerm xelephant = provider.newEntry(x, elephant);
		PrologTerm ycat = provider.newEntry(y, cat);
		PrologTerm zbear = provider.newEntry(z, bear);

		assertArrayEquals(new PrologTerm[] { xelephant, ycat, zbear }, map.getArguments());
	}

	@Test
	public void testEqualsObject() {
		assertEquals(provider.newMap(), map);
	}

	@Test
	public void testIterator() {

		Entry<PrologTerm, PrologTerm> entry = null;
		PrologMap m = provider.cast(map);

		m.put(x, elephant);
		m.put(y, cat);
		m.put(z, bear);

		Iterator<Entry<PrologTerm, PrologTerm>> i = m.entrySet().iterator();

		entry = i.next();
		assertEquals(x, entry.getKey());
		assertEquals(elephant, entry.getValue());
		entry = i.next();
		assertEquals(y, entry.getKey());
		assertEquals(cat, entry.getValue());
		entry = i.next();
		assertEquals(z, entry.getKey());
		assertEquals(bear, entry.getValue());

	}

	@Test
	public void testGetHead() {

		PrologMap m = provider.cast(map);

		m.put(x, elephant);
		m.put(y, cat);
		m.put(z, bear);

		assertEquals(provider.newEntry(x, elephant), m.getHead());

	}

	@Test
	public void testGetTail() {

		PrologMap m = provider.cast(map);

		m.put(x, elephant);
		m.put(y, cat);
		m.put(z, bear);

		PrologTerm ycat = provider.newEntry(y, cat);
		PrologTerm zbear = provider.newEntry(z, bear);

		assertArrayEquals(new PrologTerm[] { ycat, zbear }, m.getTail().getArguments());

	}

	@Test
	public void testPut() {

		PrologMap m = provider.cast(map);

		m.put(x, elephant);
		m.put(y, cat);
		m.put(z, bear);

		assertEquals(elephant, m.get(x));
		assertEquals(cat, m.get(y));
		assertEquals(bear, m.get(z));

	}

	@Test
	public void testEntrySet() {

		Entry<PrologTerm, PrologTerm> entry = null;
		PrologMap m = provider.cast(map);

		m.put(x, elephant);
		m.put(y, cat);
		m.put(z, bear);

		Set<Entry<PrologTerm, PrologTerm>> set = m.entrySet();
		Iterator<Entry<PrologTerm, PrologTerm>> i = set.iterator();

		entry = i.next();
		assertEquals(x, entry.getKey());
		assertEquals(elephant, entry.getValue());
		entry = i.next();
		assertEquals(y, entry.getKey());
		assertEquals(cat, entry.getValue());
		entry = i.next();
		assertEquals(z, entry.getKey());
		assertEquals(bear, entry.getValue());

	}

	@Test
	public void testContainsKey() {

		PrologMap m = provider.cast(map);

		assertFalse(m.containsKey(x));
		assertFalse(m.containsKey(y));
		assertFalse(m.containsKey(z));

		m.put(x, elephant);
		m.put(y, cat);
		m.put(z, bear);

		assertTrue(m.containsKey(x));
		assertTrue(m.containsKey(y));
		assertTrue(m.containsKey(z));

	}

	@Test
	public void testContainsValue() {

		PrologMap m = provider.cast(map);

		assertFalse(m.containsValue(elephant));
		assertFalse(m.containsValue(cat));
		assertFalse(m.containsValue(bear));

		m.put(x, elephant);
		m.put(y, cat);
		m.put(z, bear);

		assertTrue(m.containsValue(elephant));
		assertTrue(m.containsValue(cat));
		assertTrue(m.containsValue(bear));

	}

	@Test
	public void testGet() {

		PrologMap m = provider.cast(map);

		m.put(x, elephant);
		m.put(y, cat);
		m.put(z, bear);

		assertEquals(elephant, m.get(x));
		assertEquals(cat, m.get(y));
		assertEquals(bear, m.get(z));

	}

	@Test
	public void testRemove() {

		PrologMap m = provider.cast(map);

		assertFalse(m.containsKey(x));
		assertFalse(m.containsKey(y));
		assertFalse(m.containsKey(z));

		m.put(x, elephant);
		m.put(y, cat);
		m.put(z, bear);

		assertTrue(m.containsKey(x));
		assertTrue(m.containsKey(y));
		assertTrue(m.containsKey(z));

		m.remove(x);
		m.remove(y);
		m.remove(z);

		assertFalse(m.containsKey(x));
		assertFalse(m.containsKey(y));
		assertFalse(m.containsKey(z));

	}

	@Test
	public void testPutAll() {

		PrologMap m = provider.cast(map);

		assertFalse(m.containsKey(x));
		assertFalse(m.containsKey(y));
		assertFalse(m.containsKey(z));

		m.put(x, elephant);
		m.put(y, cat);
		m.put(z, bear);

		assertTrue(m.containsKey(x));
		assertTrue(m.containsKey(y));
		assertTrue(m.containsKey(z));

		PrologTerm jmx = provider.newMap(3);
		PrologMap jpa = provider.cast(jmx);

		assertFalse(jpa.containsKey(x));
		assertFalse(jpa.containsKey(y));
		assertFalse(jpa.containsKey(z));

		jpa.putAll(m);

		assertTrue(jpa.containsKey(x));
		assertTrue(jpa.containsKey(y));
		assertTrue(jpa.containsKey(z));

	}

	@Test
	public void testKeySet() {

		PrologMap m = provider.cast(map);

		m.put(x, elephant);
		m.put(y, cat);
		m.put(z, bear);

		Set<PrologTerm> set = new HashSet<PrologTerm>(3);
		set.add(x);
		set.add(y);
		set.add(z);

		assertEquals(set, m.keySet());

	}

	@Test
	public void testValues() {

		PrologMap m = provider.cast(map);

		m.put(x, elephant);
		m.put(y, cat);
		m.put(z, bear);

		Collection<PrologTerm> collection = m.values();
		assertTrue(collection.contains(elephant));
		assertTrue(collection.contains(cat));
		assertTrue(collection.contains(bear));

	}

	@Test
	public void testIsEmpty() {
		PrologMap x = provider.cast(map);
		assertTrue(x.isEmpty());
	}

	@Test
	public void testClear() {
		PrologMap x = provider.cast(map);
		assertTrue(x.isEmpty());
		x.put(z, bear);
		assertFalse(x.isEmpty());
		x.clear();
		assertTrue(x.isEmpty());
	}

	@Test
	public void testSize() {
		PrologMap x = provider.cast(map);
		assertTrue(x.isEmpty());
		assertEquals(0, x.size());
		x.put(z, bear);
		assertFalse(x.isEmpty());
		assertTrue(x.size() > 0);
		assertEquals(1, x.size());
		x.clear();
		assertEquals(0, x.size());
		assertTrue(x.isEmpty());
	}

	@Test
	public void testIsAtom() {
		assertFalse(map.isAtom());
	}

	@Test
	public void testIsNumber() {
		assertFalse(map.isNumber());
	}

	@Test
	public void testIsFloat() {
		assertFalse(map.isFloat());
	}

	@Test
	public void testIsInteger() {
		assertFalse(map.isInteger());
	}

	@Test
	public void testIsDouble() {
		assertFalse(map.isDouble());
	}

	@Test
	public void testIsLong() {
		assertFalse(map.isLong());
	}

	@Test
	public void testIsVariable() {
		assertFalse(map.isVariable());
	}

	@Test
	public void testIsNil() {
		assertFalse(map.isNil());
	}

	@Test
	public void testIsAtomic() {
		assertFalse(map.isAtomic());
	}

	@Test
	public void testIsCompound() {
		assertTrue(map.isCompound());
	}

	@Test
	public void testIsEvaluable() {
		assertFalse(map.isEvaluable());
	}

	@Test
	public void testIsTrueType() {
		assertFalse(map.isTrueType());
	}

	@Test
	public void testIsFalseType() {
		assertFalse(map.isFalseType());
	}

	@Test
	public void testIsNullType() {
		assertFalse(map.isNullType());
	}

	@Test
	public void testIsVoidType() {
		assertFalse(map.isVoidType());
	}

	@Test
	public void testIsObjectType() {
		assertFalse(map.isObjectType());
	}

	@Test
	public void testIsReference() {
		assertFalse(map.isReference());
	}

	@Test
	public void testGetObject() {
		assertNull(map.getObject());
	}

	@Test
	public void testGetArity() {
		assertEquals(0, map.getArity());
	}

	@Test
	public void testGetArgument() {

		PrologMap m = provider.cast(map);

		m.put(x, elephant);
		m.put(y, cat);
		m.put(z, bear);

		assertEquals(provider.newEntry(x, elephant), m.getArgument(0));
		assertEquals(provider.newEntry(y, cat), m.getArgument(1));
		assertEquals(provider.newEntry(z, bear), m.getArgument(2));

	}

	@Test
	public void testGetIndicator() {
		assertEquals(map.getFunctor() + "/" + map.getArity(), map.getIndicator());
	}

	@Test
	public void testHasIndicator() {
		assertTrue(map.hasIndicator(map.getFunctor(), map.getArity()));
	}

	@Test
	public void testGetTerm() {
		assertEquals(map, map.getTerm());
	}

	@Test
	public void testGetType() {
		assertEquals(PrologTermType.MAP_TYPE, map.getType());
	}

	@Test
	public void testGetProvider() {
		assertEquals(provider, map.getProvider());
	}

	@Test
	public void testIsEntry() {
		assertFalse(map.isEntry());
	}

	@Test
	public void testIsMap() {
		assertTrue(map.isMap());
	}

	@Test
	public void testToString() {

		PrologMap m = provider.cast(map);
		assertTrue(m.isEmpty());
		assertEquals(0, m.size());

		m.put(x, elephant);
		m.put(y, cat);
		m.put(z, bear);

		assertFalse(m.isEmpty());
		assertEquals(3, m.size());

		assertEquals("[X-elephant, Y-cat, Z-bear]", m.toString());
	}

	@Test
	public void testUnify() {

		// with atom
		PrologAtom atom = provider.newAtom("John Doe");
		assertFalse(map.unify(atom));

		// with integer
		PrologInteger iValue = provider.newInteger(28);
		assertFalse(map.unify(iValue));

		// with long
		PrologLong lValue = provider.newLong(28);
		assertFalse(map.unify(lValue));

		// with float
		PrologFloat fValue = provider.newFloat(36.47);
		assertFalse(map.unify(fValue));

		// with double
		PrologDouble dValue = provider.newDouble(36.47);
		assertFalse(map.unify(dValue));

		// with variable
		PrologVariable variable = provider.newVariable("X", 0);
		// true. case predicate and variable
		assertTrue(map.unify(variable));

		// with predicate
		PrologStructure structure1 = provider.parseStructure("some_predicate(X)");
		PrologStructure structure2 = provider.parseStructure("some_predicate(28)");

		// true because are equals
		assertTrue(map.unify(map));
		// true because match and their arguments unify
		assertFalse(map.unify(structure1));
		// false because match but their arguments not unify
		assertFalse(map.unify(structure2));

		// with list
		PrologList flattenList = provider.parseList("['Some Literal']");
		PrologList headTailList = provider.parseList("['Some Literal'|[]]");
		PrologTerm empty = provider.prologEmpty();
		assertFalse(map.unify(flattenList));
		assertFalse(map.unify(headTailList));
		assertFalse(map.unify(empty));

		// with expression
		PrologTerm expression = provider.parseTerm("58+93*10");
		assertFalse(map.unify(expression));

	}

	@Test
	public void testCompareTo() {

		// with atom
		PrologAtom atom = provider.newAtom("John Doe");
		assertEquals(1, map.compareTo(atom));

		// with integer
		PrologInteger iValue = provider.newInteger(28);
		assertEquals(1, map.compareTo(iValue));

		// with long
		PrologLong lValue = provider.newLong(28);
		assertEquals(1, map.compareTo(lValue));

		// with float
		PrologFloat fValue = provider.newFloat(36.47);
		assertEquals(1, map.compareTo(fValue));

		// with double
		PrologDouble dValue = provider.newDouble(36.47);
		assertEquals(1, map.compareTo(dValue));

		// with variable
		PrologVariable variable = provider.newVariable("X", 0);
		assertEquals(1, map.compareTo(variable));

		// with predicate
		PrologStructure structure1 = provider.parseStructure("some_predicate(X)");
		PrologStructure structure2 = provider.parseStructure("some_predicate(28)");

		// true because are equals
		assertEquals(0, map.compareTo(map));
		// true because match and their arguments compareTo
		assertEquals(-1, map.compareTo(structure1));
		// false because match but their arguments not compareTo
		assertEquals(-1, map.compareTo(structure2));

		// with list
		PrologList flattenList = provider.parseList("['Some Literal']");
		PrologList headTailList = provider.parseList("['Some Literal'|[]]");
		PrologTerm empty = provider.prologEmpty();
		assertEquals(-1, map.compareTo(flattenList));
		assertEquals(-1, map.compareTo(headTailList));
		assertEquals(0, map.compareTo(empty));

		// with expression
		PrologTerm expression = provider.parseTerm("58+93*10");
		assertEquals(-1, map.compareTo(expression));

	}

}
