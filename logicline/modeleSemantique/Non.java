/**
 * 
 */
package logicline.modeleSemantique;

import java.util.Set;

/**
 * @author nintunze
 * 
 */
public class Non extends Formule {

	protected Formule formule;

	public Non(Formule f) {
		formule = f;
		vl.addAll(formule.variablesLibres());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see logicline.modeleSemantique.Formule#toString()
	 */
	@Override
	public String toString() {
		return "¬(" + formule.toString() + ")";
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
		return new Non(formule.substitue(s));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see logicline.modeleSemantique.Formule#valeur()
	 */
	@Override
	public boolean valeur() throws VariableLibreException {
		return !formule.valeur();
	}

	@Override
	public Formule supprImplications() {
		return new Non(formule.supprImplications());
	}

	@Override
	public Formule negation() {
		return formule.entrerNegations();
	}

	@Override
	public Formule entrerNegations() {
		return formule.negation();

	}

	@Override
	protected boolean contientEt() {
		return formule.contientEt();
	}

	@Override
	public ListeClauses clauses() throws NotFNCException, TrueClauseException,
			FalseClauseException, VariableClauseException {
		ListeClauses lc = new ListeClauses();
		Clause c = new Clause(formule.toString(), false);
		lc.add(c);
		return lc;
	}
}
