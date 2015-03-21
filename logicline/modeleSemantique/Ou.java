/**
 * 
 */
package logicline.modeleSemantique;

import java.util.Set;

/**
 * @author Honoré Nintunze
 * 
 */
public class Ou extends Formule {

	protected Formule fg;
	protected Formule fd;

	public Ou(Formule fg, Formule fd) {
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
		return "(" + fg.toString() + ") ∨ (" + fd.toString() + ")";
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
		return new Ou(fg.substitue(s), fd.substitue(s));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see logicline.modeleSemantique.Formule#valeur()
	 */
	@Override
	public boolean valeur() throws VariableLibreException {
		return fg.valeur() || fd.valeur();
	}

	@Override
	public Formule supprImplications() {
		return new Ou(fg.supprImplications(), fd.supprImplications());
	}

	@Override
	protected Formule negation() {
		return new Et(fg.negation(), fd.negation());
	}

	@Override
	protected Formule entrerNegations() {
		return new Ou(fg.entrerNegations(), fd.entrerNegations());
	}

	@Override
	protected boolean contientEt() {
		return fg.contientEt() || fd.contientEt();
	}

	@Override
	protected Formule entrerDisjonctions() {
		if (fg.contientEt()) {
			return fg.ougauche(fd);
		}
		return fd.oudroite(fg);
	}

	@Override
	public ListeClauses clauses() throws NotFNCException, TrueClauseException,
			FalseClauseException, VariableClauseException {
		ListeClauses lc = new ListeClauses();
		Clause c = new Clause();
		int nbC, i;

		// partie gauche
		try {
			ListeClauses lcg = fg.clauses();
			nbC = lcg.nbClauses();
			for (i = 0; i < nbC; i++) {
				c.addAll(lcg.get(i));
			}
		} catch (TrueClauseException e) {
			throw new TrueClauseException();
		} catch (FalseClauseException e) {
			// ne fait rien et passe à la suite
		}

		// partie droite
		try {
			ListeClauses lcd = fd.clauses();
			nbC = lcd.nbClauses();
			for (i = 0; i < nbC; i++) {
				c.addAll(lcd.get(i));
			}
		} catch (TrueClauseException e) {
			throw new TrueClauseException();
		} catch (FalseClauseException e) {
			// ne fait rien et passe à la suite
		}
		lc.add(c);
		return lc;
	}
}
