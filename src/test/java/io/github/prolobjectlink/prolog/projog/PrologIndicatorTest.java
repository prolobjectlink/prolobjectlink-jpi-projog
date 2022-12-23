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

import io.github.prolobjectlink.prolog.PrologIndicator;

public class PrologIndicatorTest extends PrologBaseTest {

	private final PrologIndicator pi = new ProjogIndicator("functor", 3);

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public final void testGetArity() {
		assertEquals(3, pi.getArity());
	}

	@Test
	public final void testGetFunctor() {
		assertEquals("functor", pi.getFunctor());
	}

	@Test
	public final void testGetIndicator() {
		assertEquals("functor/3", pi.getIndicator());
	}

	@Test
	public final void testToString() {
		assertEquals("functor/3", pi.toString());
	}

	@Test
	public final void testHashCode() {
		assertEquals(new ProjogIndicator("functor", 3).hashCode(), pi.hashCode());
	}

	@Test
	public final void testEquals() {
		assertEquals(new ProjogIndicator("functor", 3), pi);
	}

}
