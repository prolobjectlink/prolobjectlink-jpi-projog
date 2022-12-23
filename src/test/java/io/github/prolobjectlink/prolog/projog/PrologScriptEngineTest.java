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
import static org.junit.Assert.assertNotSame;

import java.io.StringReader;

import javax.script.Bindings;
import javax.script.ScriptContext;
import javax.script.ScriptEngine;
import javax.script.ScriptException;
import javax.script.SimpleBindings;
import javax.script.SimpleScriptContext;

import org.junit.Test;

public class PrologScriptEngineTest extends PrologBaseTest {

	private final Bindings bindings = new SimpleBindings();
	private final ScriptEngine engine = manager.getEngineByName(provider.getName());

	@Test
	public void testCreateBindings() throws ScriptException {
		Bindings b = engine.createBindings();
		b.put("X", 42.0);
		assertEquals(true, engine.eval("?- X == 42.0.", b));
	}

	@Test
	public void testGetFactory() {
		assertEquals(manager.getEngineByName(provider.getName()).getFactory(), engine.getFactory());
	}

	@Test
	public void testSetContextScriptContext() {
		ScriptContext old = engine.getContext();
		engine.setContext(new SimpleScriptContext());
		assertNotSame(old, engine.getContext());
	}

	@Test
	public void testGetContext() throws ScriptException {
		assertEquals(true, engine.eval("?- X is 5+3."));
		assertEquals(8, engine.get("X"));
		assertNotNull(engine.getContext());
	}

	@Test
	public void testGetBindingsInt() throws ScriptException {
		assertEquals(0, engine.getBindings(ScriptContext.ENGINE_SCOPE).size());
		assertEquals(true, engine.eval("?- X is 5+3."));
		assertEquals(8, engine.get("X"));
		assertEquals(1, engine.getBindings(ScriptContext.ENGINE_SCOPE).size());
	}

	@Test
	public void testSetBindingsBindingsInt() throws ScriptException {
		bindings.put("X", 42.0);
		engine.setBindings(bindings, ScriptContext.ENGINE_SCOPE);
		assertEquals(true, engine.eval("?- X == 42.0.", bindings));
	}

	@Test
	public void testPutStringObject() throws ScriptException {
		engine.put("X", 42.0);
		assertEquals(true, engine.eval("?- X == 42.0."));
	}

	@Test
	public void testGetString() throws ScriptException {
		assertEquals(true, engine.eval("?- X is 5+3."));
		assertEquals(8, engine.get("X"));
	}

	@Test
	public void testEvalStringScriptContext() throws ScriptException {
		assertEquals(true, engine.eval("?- X is 5+3.", engine.getContext()));
		assertEquals(8, engine.get("X"));
	}

	@Test
	public void testEvalReaderScriptContext() throws ScriptException {
		assertEquals(true, engine.eval(new StringReader("?- X is 5+3."), engine.getContext()));
		assertEquals(8, engine.get("X"));
	}

	@Test
	public void testEvalReaderBindings() throws ScriptException {
		assertEquals(true, engine.eval(new StringReader("?- X is 5+3."), bindings));
		assertEquals(8, engine.get("X"));
	}

	@Test
	public void testEvalStringBindings() throws ScriptException {
		assertEquals(0, engine.getBindings(ScriptContext.ENGINE_SCOPE).size());
		assertEquals(true, engine.eval("?- X is 5+3."));
		assertEquals(8, engine.get("X"));
		assertEquals(1, engine.getBindings(ScriptContext.ENGINE_SCOPE).size());
	}

	@Test
	public void testEvalReader() throws ScriptException {
		assertEquals(true, engine.eval(new StringReader("?- X is 5+3.")));
		assertEquals(8, engine.get("X"));
	}

	@Test
	public void testEvalString() throws ScriptException {
		assertEquals(true, engine.eval("?- X is 5+3."));
		assertEquals(8, engine.get("X"));
	}

	@Test
	public void testGetScriptContextBindings() throws ScriptException {
		assertEquals(true, engine.eval("?- X is 5+3."));
		assertEquals(8, engine.get("X"));
		assertNotNull(engine.getContext());
	}

}
