/**
 * 
 */
package logicline.modeleSemantique;

import java.util.HashSet;
import java.util.Set;

/**
 * @author nintunze
 * 
 */
public class Constante extends Formule {

	protected boolean value;

	public Constante(boolean b) {
		value = b;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see logicline.modeleSemantique.Formule#toString()
	 */
	@Override
	public String toString() {
		return value ? "T" : "⊥";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see logicline.modeleSemantique.Formule#variablesLibres()
	 */
	@Override
	public Set<String> variablesLibres() {
		return new HashSet<>();
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
		return this;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see logicline.modeleSemantique.Formule#valeur()
	 */
	@Override
	public boolean valeur() throws VariableLibreException {
		return value;
	}

	@Override
	public ListeClauses clauses() throws NotFNCException, TrueClauseException,
			FalseClauseException, VariableClauseException {
		
		if (value)
			throw new TrueClauseException();
		else
			throw new FalseClauseException();
	}
}
