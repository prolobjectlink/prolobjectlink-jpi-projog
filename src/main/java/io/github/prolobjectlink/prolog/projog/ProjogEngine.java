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

import static io.github.prolobjectlink.prolog.PrologLogger.DONT_WORRY;
import static io.github.prolobjectlink.prolog.PrologLogger.IO;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Reader;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import org.projog.core.parser.Operands;
import org.projog.core.predicate.PredicateKey;
import org.projog.core.predicate.udp.ClauseModel;
import org.projog.core.predicate.udp.UserDefinedPredicateFactory;
import org.projog.core.term.Atom;
import org.projog.core.term.Structure;
import org.projog.core.term.Term;
import org.projog.core.term.Variable;

import io.github.prolobjectlink.prolog.AbstractEngine;
import io.github.prolobjectlink.prolog.AbstractOperator;
import io.github.prolobjectlink.prolog.ArrayIterator;
import io.github.prolobjectlink.prolog.Licenses;
import io.github.prolobjectlink.prolog.PrologClause;
import io.github.prolobjectlink.prolog.PrologEngine;
import io.github.prolobjectlink.prolog.PrologIndicator;
import io.github.prolobjectlink.prolog.PrologOperator;
import io.github.prolobjectlink.prolog.PrologProgram;
import io.github.prolobjectlink.prolog.PrologProvider;
import io.github.prolobjectlink.prolog.PrologQuery;
import io.github.prolobjectlink.prolog.PrologTerm;

/**
 * 
 * @author Jose Zalacain
 * @since 1.0
 */
public class ProjogEngine extends AbstractEngine implements PrologEngine {

	final org.projog.api.Projog projog = new org.projog.api.Projog();
	final Set<String> userOperators = new HashSet<String>();
	private static final Term BODY = new Atom("true");

	protected ProjogEngine(PrologProvider provider) {
		super(provider);
	}

	public final List<String> verify() {
		return Arrays.asList("OK");
	}

	@Override
	public void consult(String path) {
		projog.consultFile(new File(path));
	}

	@Override
	public void consult(Reader reader) {
		projog.consultReader(reader);
	}

	@Override
	public void include(String path) {
		projog.consultFile(new File(path));
	}

	@Override
	public void include(Reader reader) {
		projog.consultReader(reader);
	}

	@Override
	public void persist(String path) {
		PrintWriter writer = null;
		try {
			writer = new PrintWriter(path);
			Map<PredicateKey, UserDefinedPredicateFactory> m = projog.getKnowledgeBase().getPredicates()
					.getUserDefinedPredicates();
			for (Map.Entry<PredicateKey, UserDefinedPredicateFactory> entry : m.entrySet()) {
				UserDefinedPredicateFactory val = entry.getValue();
				Iterator<ClauseModel> i = val.getImplications();
				while (i.hasNext()) {
					ClauseModel clause = i.next();
					if (clause.isFact()) {
						PrologTerm head = toTerm(clause.getConsequent(), PrologTerm.class);
						writer.append("" + head + ".");
						writer.append('\n');
					} else {
						PrologTerm head = toTerm(clause.getConsequent(), PrologTerm.class);
						PrologTerm body = toTerm(clause.getAntecedent(), PrologTerm.class);
						writer.append("" + head + " :- " + body + ".");
						writer.append('\n');
					}
				}
			}
		} catch (IOException e) {
			getLogger().warn(getClass(), IO + path, e);
			getLogger().info(getClass(), DONT_WORRY + path);
		} finally {
			if (writer != null) {
				writer.close();
			}
		}
	}

	@Override
	public void abolish(String functor, int arity) {
		Variable[] args = new Variable[arity];
		Arrays.fill(args, new Variable());
		Term p = Structure.createStructure(functor, args);
		projog.executeOnce("retractall(" + p + ").");
	}

	@Override
	public void asserta(String stringClause) {
		if (!clause(stringClause)) {
			projog.executeOnce("asserta((" + stringClause + ")).");
		}
	}

	@Override
	public void asserta(PrologTerm term) {
		if (!clause(term)) {
			projog.executeOnce("asserta((" + term + ")).");
		}
	}

	@Override
	public void asserta(PrologTerm head, PrologTerm... body) {
		String b = Arrays.toString(body);
		b = b.substring(1, b.length() - 1);
		if (!clause(head, body)) {
			projog.executeOnce("asserta((" + head + " :- " + b + ")).");
		}
	}

	@Override
	public void assertz(String stringClause) {
		if (!clause(stringClause)) {
			projog.executeOnce("assertz((" + stringClause + ")).");
		}
	}

	@Override
	public void assertz(PrologTerm term) {
		if (!clause(term)) {
			projog.executeOnce("assertz((" + term + ")).");
		}
	}

	@Override
	public void assertz(PrologTerm head, PrologTerm... body) {
		String b = Arrays.toString(body);
		b = b.substring(1, b.length() - 1);
		if (!clause(head, body)) {
			projog.executeOnce("assertz((" + head + " :- " + b + ")).");
		}
	}

	@Override
	public boolean clause(String t) {
		return clause(provider.parseTerm(t));
	}

	@Override
	public boolean clause(PrologTerm term) {
		Term h = null;
		Term b = BODY;
		Term c = fromTerm(term, Term.class);
		if (term.hasIndicator(":-", 2)) {
			h = c.getArgument(0);
			b = c.getArgument(1);
		} else {
			h = c;
		}
		return projog.executeQuery("clause(" + h + "," + b + ").").next();
	}

	@Override
	public boolean clause(PrologTerm head, PrologTerm... body) {
		Term clause = fromTerm(head, body, Term.class);
		Term h = clause.getArgument(0);
		Term b = clause.getArgument(1);
		return projog.executeQuery("clause(" + h + "," + b + ").").next();
	}

	@Override
	public void retract(String stringClause) {
		projog.executeOnce("retract(" + stringClause + ").");
	}

	@Override
	public void retract(PrologTerm term) {
		projog.executeOnce("retract(" + term + ").");
	}

	@Override
	public void retract(PrologTerm head, PrologTerm... body) {
		String b = Arrays.toString(body);
		b = b.substring(1, b.length() - 1);
		if (!clause(head, body)) {
			projog.executeOnce("retract((" + head + " :- " + b + ")).");
		}
	}

	@Override
	public PrologQuery query(String query) {
		return new ProjogQuery(this, query);
	}

	@Override
	public PrologQuery query(PrologTerm goal) {
		return query("" + goal + "");
	}

	public final PrologQuery query(PrologTerm[] terms) {
		Iterator<PrologTerm> i = new ArrayIterator<PrologTerm>(terms);
		StringBuilder buffer = new StringBuilder();
		while (i.hasNext()) {
			buffer.append(i.next());
			if (i.hasNext()) {
				buffer.append(',');
			}
		}
//		buffer.append(".");
		return query("" + buffer + "");
	}

	public final PrologQuery query(PrologTerm term, PrologTerm... terms) {
		Iterator<PrologTerm> i = new ArrayIterator<PrologTerm>(terms);
		StringBuilder buffer = new StringBuilder();
		buffer.append("" + term + "");
		while (i.hasNext()) {
			buffer.append(',');
			buffer.append(i.next());
		}
//		buffer.append(".");
		return query("" + buffer + "");
	}

	@Override
	public void operator(int priority, String specifier, String operator) {
		projog.executeOnce("op(" + priority + "," + specifier + ", '" + operator + "').");
		userOperators.add(operator);
	}

	@Override
	public boolean currentPredicate(String functor, int arity) {
		Set<PredicateKey> ps = projog.getKnowledgeBase().getPredicates().getAllDefinedPredicateKeys();
		for (PredicateKey predicateKey : ps) {
			int a = predicateKey.getNumArgs();
			String f = predicateKey.getName();
			if (arity == a && functor.equals(f)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean currentOperator(int priority, String specifier, String operator) {
		Operands ops = projog.getKnowledgeBase().getOperands();
		Object prefix = ops.prefix(operator) ? ops.getPrefixPriority(operator) : null;
		Object infix = ops.infix(operator) ? ops.getInfixPriority(operator) : null;
		Object postfix = ops.postfix(operator) ? ops.getPostfixPriority(operator) : null;
		int p = prefix != null ? (int) prefix
				: (infix != null ? (int) infix : (int) (postfix != null ? postfix : Integer.MIN_VALUE));
		String associativity = ops.fx(operator) ? "fx"
				: (ops.fy(operator) ? "fy"
						: (ops.xf(operator) ? "xf"
								: (ops.yf(operator) ? "yf"
										: (ops.xfx(operator) ? "xfx"
												: (ops.xfy(operator) ? "xfy"
														: (ops.yfx(operator) ? "yfx" : ("unknow")))))));
		return ops.isDefined(operator) && p == priority && associativity.equals(specifier);
	}

	@Override
	public Set<PrologOperator> currentOperators() {
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
		for (String string : userOperators) {
			if (ops.isDefined(string)) {
				Object prefix = ops.prefix(string) ? ops.getPrefixPriority(string) : null;
				Object infix = ops.infix(string) ? ops.getInfixPriority(string) : null;
				Object postfix = ops.postfix(string) ? ops.getPostfixPriority(string) : null;
				int priority = prefix != null ? (int) prefix
						: (infix != null ? (int) infix : (int) (postfix != null ? postfix : Integer.MIN_VALUE));
				String specifier = ops.fx(string) ? "fx"
						: (ops.fy(string) ? "fy"
								: (ops.xf(string) ? "xf"
										: (ops.yf(string) ? "yf"
												: (ops.xfx(string) ? "xfx"
														: (ops.xfy(string) ? "xfy"
																: (ops.yfx(string) ? "yfx" : ("unknow")))))));
				AbstractOperator operator = new ProjogOperator(priority, specifier, string);
				operators.add(operator);
			}
		}
		return operators;
	}

	@Override
	public Iterator<PrologClause> iterator() {
		Collection<PrologClause> cls = new LinkedList<PrologClause>();
		Map<PredicateKey, UserDefinedPredicateFactory> m = projog.getKnowledgeBase().getPredicates()
				.getUserDefinedPredicates();
		for (Map.Entry<PredicateKey, UserDefinedPredicateFactory> entry : m.entrySet()) {
			UserDefinedPredicateFactory val = entry.getValue();
			Iterator<ClauseModel> i = val.getImplications();
			while (i.hasNext()) {
				ClauseModel clause = i.next();
				if (clause.isFact()) {
					PrologTerm head = toTerm(clause.getConsequent(), PrologTerm.class);
					cls.add(new ProjogClause(provider, head, false, false, false));
				} else {
					PrologTerm head = toTerm(clause.getConsequent(), PrologTerm.class);
					PrologTerm body = toTerm(clause.getAntecedent(), PrologTerm.class);
					cls.add(new ProjogClause(provider, head, body, false, false, false));
				}
			}
		}
		return new PrologProgramIterator(cls);
	}

	@Override
	public int getProgramSize() {
		int counter = 0;
		Map<PredicateKey, UserDefinedPredicateFactory> m = projog.getKnowledgeBase().getPredicates()
				.getUserDefinedPredicates();
		for (Map.Entry<PredicateKey, UserDefinedPredicateFactory> entry : m.entrySet()) {
			UserDefinedPredicateFactory val = entry.getValue();
			Iterator<ClauseModel> i = val.getImplications();
			while (i.hasNext()) {
				i.next();
				counter++;
			}
		}
		return counter;
	}

	@Override
	public PrologProgram getProgram() {
		return new ProjogProgram(this);
	}

	@Override
	public Set<PrologIndicator> getPredicates() {
		Set<PrologIndicator> predicates = new HashSet<PrologIndicator>();
		Map<PredicateKey, UserDefinedPredicateFactory> m = projog.getKnowledgeBase().getPredicates()
				.getUserDefinedPredicates();
		for (Map.Entry<PredicateKey, UserDefinedPredicateFactory> entry : m.entrySet()) {
			UserDefinedPredicateFactory val = entry.getValue();
			Iterator<ClauseModel> i = val.getImplications();
			while (i.hasNext()) {
				ClauseModel clause = i.next();
				int arity = clause.getPredicateKey().getNumArgs();
				String functor = clause.getPredicateKey().getName();
				ProjogIndicator pi = new ProjogIndicator(functor, arity);
				predicates.add(pi);
			}
		}
		return predicates;
	}

	@Override
	public Set<PrologIndicator> getBuiltIns() {
		Set<PrologIndicator> builtins = new HashSet<PrologIndicator>();
		Set<PredicateKey> ps = projog.getKnowledgeBase().getPredicates().getAllDefinedPredicateKeys();
		Map<PredicateKey, UserDefinedPredicateFactory> m = projog.getKnowledgeBase().getPredicates()
				.getUserDefinedPredicates();
		for (PredicateKey predicateKey : ps) {
			if (!m.containsKey(predicateKey)) {
				int arity = predicateKey.getNumArgs();
				String functor = predicateKey.getName();
				ProjogIndicator pi = new ProjogIndicator(functor, arity);
				builtins.add(pi);
			}
		}
		return builtins;
	}

	public String getLicense() {
		return Licenses.APACHE_V2;
	}

	public String getVersion() {
		return Projog.VERSION;
	}

	public final String getVendor() {
		// return Projog.NAME
		return "projog.org";
	}

	public String getName() {
		return Projog.NAME;
	}

	@Override
	public void dispose() {
		// projog.getKnowledgeBase().getPredicates().getUserDefinedPredicates().clear()
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + Objects.hash(projog);
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
		ProjogEngine other = (ProjogEngine) obj;
		return Objects.equals(projog.getClass(), other.projog.getClass());
	}

}
