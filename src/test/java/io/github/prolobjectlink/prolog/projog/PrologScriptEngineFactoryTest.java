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
import static org.junit.Assert.assertNotNull;

import java.util.Arrays;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineFactory;

import org.junit.Test;

public class PrologScriptEngineFactoryTest extends PrologBaseTest {

	private ScriptEngineFactory sef = manager.getEngineByName(provider.getName()).getFactory();

	@Test
	public void testGetEngineName() {
		assertEquals("Projog", sef.getEngineName());
	}

	@Test
	public void testGetEngineVersion() {
		assertEquals(provider.newEngine().getVersion(), sef.getEngineVersion());
	}

	@Test
	public void testGetExtensions() {
		assertEquals(Arrays.asList("pro", "pl"), sef.getExtensions());
	}

	@Test
	public void testGetMimeTypes() {
		assertEquals(Arrays.asList("text/plain"), sef.getMimeTypes());
	}

	@Test
	public void testGetNames() {
		assertEquals(Arrays.asList("Projog", "Prolog", "prolog"), sef.getNames());
	}

	@Test
	public void testGetLanguageName() {
		assertEquals("Prolog", sef.getLanguageName());
	}

	@Test
	public void testGetLanguageVersion() {
		assertEquals(provider.getVersion(), sef.getLanguageVersion());
	}

	@Test
	public void testGetParameter() {
		assertEquals(provider.getName(), sef.getParameter(ScriptEngine.NAME));
		assertEquals(provider.getName(), sef.getParameter(ScriptEngine.ENGINE));
		assertEquals(provider.getVersion(), sef.getParameter(ScriptEngine.ENGINE_VERSION));
		assertEquals("Prolog", sef.getParameter(ScriptEngine.LANGUAGE));
		assertEquals(provider.getVersion(), sef.getParameter(ScriptEngine.LANGUAGE_VERSION));
	}

	@Test
	public void testGetOutputStatement() {
		assertEquals("write('Hello World')", sef.getOutputStatement("Hello World"));
	}

	@Test
	public void testGetProgram() {
		assertEquals("black(cat).\n"

				+ "gray(elephant).\n"

				+ "big(bear).\n"

				+ "big(elephant).\n"

				+ "brown(bear).\n"

				+ "dark(Z) :- black(Z).\n"

				+ "dark(Z) :- brown(Z).\n"

				+ "small(cat).",
				sef.getProgram("black(cat)", "gray(elephant)", "big(bear)", "big(elephant)", "brown(bear)",
						"dark(Z) :- black(Z)", "dark(Z) :- brown(Z)", "small(cat)"));
	}

	@Test
	public void testGetScriptEngine() {
		assertNotNull(sef.getScriptEngine());
	}

	@Test
	public void testGetMethodCallSyntax() {
		assertEquals("OBJ1 <- compareTo(OBJ2,OBJ3)", sef.getMethodCallSyntax("OBJ1", "compareTo", "OBJ2", "OBJ3"));
		assertEquals("OBJ1 <- equals(OBJ2)", sef.getMethodCallSyntax("OBJ1", "equals", "OBJ2"));
		assertEquals("OBJ1 <- hashCode()", sef.getMethodCallSyntax("OBJ1", "hashCode"));
	}

}
