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

import java.io.FileNotFoundException;
import java.io.FileReader;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineFactory;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import org.junit.Test;

public class PrologScriptEngineManagerTest extends PrologBaseTest {

	@Test
	public void testService() throws ScriptException, FileNotFoundException {

		ScriptEngineManager manager = new ScriptEngineManager();
		ScriptEngine engine = manager.getEngineByName(provider.getName());
		assertEquals(true, engine.eval("?- X is 5+3."));
		assertEquals(8, engine.get("X"));

		engine = manager.getEngineByName("Prolog");
		assertEquals(true, engine.eval("?- X is 5+3."));
		assertEquals(8, engine.get("X"));

		engine = manager.getEngineByName("prolog");
		assertEquals(true, engine.eval("?- X is 5+3."));
		assertEquals(8, engine.get("X"));

		assertEquals(true, engine.eval(new FileReader("family.pl")));
		assertEquals(true, engine.eval(new FileReader("company.pl")));
		assertEquals(true, engine.eval(new FileReader("zoo.pl")));

		assertEquals(true, engine.eval("?- parent( Parent, Child)"));
		assertEquals("pam", engine.get("Parent"));
		assertEquals("bob", engine.get("Child"));

		assertEquals(true,
				engine.eval("?- employee(Name,Dpto,Scale),department(Dpto,DepartmentName),salary(Scale,Money)"));
		assertEquals("mcardon", engine.get("Name"));
		assertEquals(1, engine.get("Dpto"));
		assertEquals(5, engine.get("Scale"));
		assertEquals("board", engine.get("DepartmentName"));
		assertEquals(3000, engine.get("Money"));

		assertEquals(true, engine.eval("?- dark(Animal),big(Animal)"));
		assertEquals("bear", engine.get("Animal"));

	}

	@Test
	public void testRegistration() throws ScriptException, FileNotFoundException {

		ScriptEngineManager manager = new ScriptEngineManager();
		ScriptEngineFactory factory = new ProjogScriptFactory();
		manager.registerEngineName(provider.getName(), factory);
		ScriptEngine engine = manager.getEngineByName(provider.getName());
		assertEquals(true, engine.eval("?- X is 5+3."));
		assertEquals(8, engine.get("X"));

		assertEquals(true, engine.eval(new FileReader("family.pl")));
		assertEquals(true, engine.eval(new FileReader("company.pl")));
		assertEquals(true, engine.eval(new FileReader("zoo.pl")));

		assertEquals(true, engine.eval("?- parent( Parent, Child)"));
		assertEquals("pam", engine.get("Parent"));
		assertEquals("bob", engine.get("Child"));

		assertEquals(true,
				engine.eval("?- employee(Name,Dpto,Scale),department(Dpto,DepartmentName),salary(Scale,Money)"));
		assertEquals("mcardon", engine.get("Name"));
		assertEquals(1, engine.get("Dpto"));
		assertEquals(5, engine.get("Scale"));
		assertEquals("board", engine.get("DepartmentName"));
		assertEquals(3000, engine.get("Money"));

		assertEquals(true, engine.eval("?- dark(Animal),big(Animal)"));
		assertEquals("bear", engine.get("Animal"));

	}

}
