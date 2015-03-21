/**
 * 
 */
package logicline.modeleSemantique;

import java.util.Set;

/**
 * @author nintunze
 * 
 */
public class Variable extends Formule {

	protected String name;

	public Variable(String s) {
		name = s;
		vl.add(name);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see logicline.modeleSemantique.Formule#toString()
	 */
	@Override
	public String toString() {
		return name;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see logicline.modeleSemantique.Formule#variablesLibres()
	 */
	@Override
	public Set<String> variablesLibres() {
		return vl;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * logicline.modeleSemantique.Formule#substitue(logicline.modeleSemantique
	 * .Substitution)
	 */
	@Override
	public Formule substitue(Substitution s) {
		Formule f = s.get(toString());
		if ((f == null))
			return this;
		else
			return f;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see logicline.modeleSemantique.Formule#valeur()
	 */
	@Override
	public boolean valeur() throws VariableLibreException {
		throw new VariableLibreException(name);
	}

	@Override
	public ListeClauses clauses() throws NotFNCException, TrueClauseException,
			FalseClauseException, VariableClauseException {
		ListeClauses lc = new ListeClauses();
		Clause c = new Clause(toString(), true);
		lc.add(c);
		return lc;
	}

}
