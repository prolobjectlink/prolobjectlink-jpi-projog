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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.StringTokenizer;

import org.projog.api.QueryResult;

import io.github.prolobjectlink.prolog.AbstractEngine;
import io.github.prolobjectlink.prolog.AbstractQuery;
import io.github.prolobjectlink.prolog.PrologQuery;
import io.github.prolobjectlink.prolog.PrologTerm;

/**
 * 
 * @author Jose Zalacain
 * @since 1.0
 */
final class ProjogQuery extends AbstractQuery implements PrologQuery {

	private List<String> variables = new ArrayList<String>();
	private org.projog.api.Projog projog;
	private QueryResult queryResult;
	private boolean next;

	ProjogQuery(AbstractEngine engine, String query) {
		super(engine);
		String delim = ",;() +-*/%=><";
		this.projog = ((ProjogEngine) engine).projog;
		StringTokenizer tokenizer = new StringTokenizer(query, delim);
		while (tokenizer.hasMoreTokens()) {
			String token = tokenizer.nextToken();
			if (token.matches("[A-Z][A-Za-z0-9_]*") && !variables.contains(token)) {
				variables.add(token);
			}
		}
		queryResult = projog.executeQuery(query + ".");
		next = queryResult.next();
	}

	@Override
	public boolean hasSolution() {
		return next;
	}

	@Override
	public boolean hasMoreSolutions() {
		return !queryResult.isExhausted();
	}

	@Override
	public PrologTerm[] oneSolution() {
		PrologTerm[] array = new PrologTerm[variables.size()];
		for (int i = 0; i < array.length; i++) {
			array[i] = toTerm(queryResult.getTerm(variables.get(i)), PrologTerm.class);
		}
		return array;
	}

	@Override
	public Map<String, PrologTerm> oneVariablesSolution() {
		Map<String, PrologTerm> map = new HashMap<String, PrologTerm>(variables.size());
		for (String variable : variables) {
			map.put(variable, toTerm(queryResult.getTerm(variable), PrologTerm.class));
		}
		return map;
	}

	@Override
	public PrologTerm[] nextSolution() {
		PrologTerm[] array = new PrologTerm[variables.size()];
		if (next) {
			for (int i = 0; i < array.length; i++) {
				array[i] = toTerm(queryResult.getTerm(variables.get(i)), PrologTerm.class);
			}
			next = queryResult.next();
		}
		return array;
	}

	@Override
	public Map<String, PrologTerm> nextVariablesSolution() {
		Map<String, PrologTerm> map = new HashMap<String, PrologTerm>(variables.size());
		if (next) {
			for (String variable : variables) {
				map.put(variable, toTerm(queryResult.getTerm(variable), PrologTerm.class));
			}
			next = queryResult.next();
		}
		return map;
	}

	@Override
	public PrologTerm[][] nSolutions(int n) {
		if (n > 0) {
			int m = 0;
			int index = 0;
			List<PrologTerm[]> all = new ArrayList<PrologTerm[]>();
			while (next && index < n) {
				PrologTerm[] solutions = new PrologTerm[variables.size()];
				for (int i = 0; i < solutions.length; i++) {
					solutions[i] = toTerm(queryResult.getTerm(variables.get(i)), PrologTerm.class);
				}
				m = solutions.length > m ? solutions.length : m;
				next = queryResult.next();
				all.add(solutions);
				index++;
			}

			PrologTerm[][] allSolutions = new PrologTerm[n][m];
			for (int i = 0; i < n; i++) {
				PrologTerm[] solutionArray = all.get(i);
				System.arraycopy(solutionArray, 0, allSolutions[i], 0, m);
			}
			return allSolutions;
		}
		return new PrologTerm[0][0];
	}

	@Override
	@SuppressWarnings("unchecked")
	public Map<String, PrologTerm>[] nVariablesSolutions(int n) {
		if (n > 0) {
			int index = 0;
			Map<String, PrologTerm>[] solutionMaps = new HashMap[n];
			while (next && index < n) {
				Map<String, PrologTerm> solutionMap = new HashMap<String, PrologTerm>(variables.size());
				for (String variable : variables) {
					solutionMap.put(variable, toTerm(queryResult.getTerm(variable), PrologTerm.class));
				}
				solutionMaps[index++] = solutionMap;
				next = queryResult.next();
			}
			return solutionMaps;
		}
		return new HashMap[0];
	}

	@Override
	public PrologTerm[][] allSolutions() {
		// n:solutionCount, m:solutionSize
		int n = 0;
		int m = 0;
		List<PrologTerm[]> all = new ArrayList<PrologTerm[]>();
		while (next) {
			PrologTerm[] solutions = new PrologTerm[variables.size()];
			for (int i = 0; i < solutions.length; i++) {
				solutions[i] = toTerm(queryResult.getTerm(variables.get(i)), PrologTerm.class);
			}
			m = solutions.length > m ? solutions.length : m;
			next = queryResult.next();
			all.add(solutions);
			n++;
		}

		PrologTerm[][] allSolutions = new PrologTerm[n][m];
		for (int i = 0; i < n; i++) {
			PrologTerm[] solutionArray = all.get(i);
			System.arraycopy(solutionArray, 0, allSolutions[i], 0, m);
		}
		return allSolutions;
	}

	@Override
	@SuppressWarnings("unchecked")
	public Map<String, PrologTerm>[] allVariablesSolutions() {
		List<Map<String, PrologTerm>> allVariables = new ArrayList<Map<String, PrologTerm>>();
		while (next) {
			Map<String, PrologTerm> vars = new HashMap<String, PrologTerm>(variables.size());
			for (String variable : variables) {
				vars.put(variable, toTerm(queryResult.getTerm(variable), PrologTerm.class));
			}
			allVariables.add(vars);
			next = queryResult.next();
		}

		int lenght = allVariables.size();
		Map<String, PrologTerm>[] allVariablesSolution = new HashMap[lenght];
		for (int i = 0; i < lenght; i++) {
			allVariablesSolution[i] = allVariables.get(i);
		}
		return allVariablesSolution;
	}

	@Override
	public List<Map<String, PrologTerm>> all() {
		List<Map<String, PrologTerm>> allVariables = new ArrayList<Map<String, PrologTerm>>();
		while (queryResult.next()) {
			Map<String, PrologTerm> vars = oneVariablesSolution();
			allVariables.add(vars);
		}
		return allVariables;
	}

	@Override
	public void dispose() {
//		queryResult = null
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + Objects.hash(queryResult);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		ProjogQuery other = (ProjogQuery) obj;
		return Objects.equals(queryResult.getVariableIds(), other.queryResult.getVariableIds());
	}

	@Override
	public String toString() {
		return "" + queryResult + "";
	}

}
