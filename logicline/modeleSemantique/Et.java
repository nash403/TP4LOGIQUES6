/**
 * 
 */
package logicline.modeleSemantique;

import java.util.Set;

/**
 * @author Honoré Nintunze
 * 
 */
public class Et extends Formule {

	protected Formule fg;
	protected Formule fd;

	public Et(Formule fg, Formule fd) {
		this.fg = fg;
		this.fd = fd;
		vl.addAll(fg.variablesLibres());
		vl.addAll(fd.variablesLibres());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see logicline.modeleSemantique.Formule#toString()
	 */
	@Override
	public String toString() {
		return "(" + fg.toString() + ") ∧ (" + fd.toString() + ")";
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
		return new Et(fg.substitue(s), fd.substitue(s));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see logicline.modeleSemantique.Formule#valeur()
	 */
	@Override
	public boolean valeur() throws VariableLibreException {
		return fg.valeur() && fd.valeur();
	}

	@Override
	public Formule supprImplications() {
		return new Et(fg.supprImplications(), fd.supprImplications());
	}

	@Override
	protected Formule negation() {
		return new Ou(fg.negation(), fd.negation());
	}

	@Override
	protected Formule entrerNegations() {
		return new Et(fg.entrerNegations(), fd.entrerNegations());
	}

	@Override
	protected boolean contientEt() {
		return true;
	}

	@Override
	public Formule ougauche(Formule g) {
		return new Et(fg.ougauche(g), fd.ougauche(g));
	}

	@Override
	public Formule oudroite(Formule d) {
		return new Et(fg.oudroite(d), fd.oudroite(d));
	}

	@Override
	protected Formule entrerDisjonctions() {
		return new Et(fg.entrerDisjonctions(), fd.entrerDisjonctions());
	}

	@Override
	public ListeClauses clauses() throws NotFNCException, TrueClauseException,
			FalseClauseException, VariableClauseException {
		ListeClauses lc = new ListeClauses();

		// partie gauche
		try {
			ListeClauses lcg = fg.clauses();
			lc.addAll(lcg);
		} catch (TrueClauseException e) {
			// ne fait rien et passe à la suite
		} catch (FalseClauseException e) {
			throw new FalseClauseException();
		}

		// partie droite
		try {
			ListeClauses lcd = fd.clauses();
			lc.addAll(lcd);
		} catch (TrueClauseException e) {
			// ne fait rien et passe à la suite
		} catch (FalseClauseException e) {
			throw new FalseClauseException();
		}
		return lc;
	}
}
