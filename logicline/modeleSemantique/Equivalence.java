/**
 * 
 */
package logicline.modeleSemantique;

import java.util.Set;

/**
 * @author Honoré Nintunze
 * 
 */
public class Equivalence extends Formule {

	protected Formule fg;
	protected Formule fd;

	public Equivalence(Formule fg, Formule fd) {
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
		return "(" + fg.toString() + ") ⇔ (" + fd.toString() + ")";
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
		return new Equivalence(fg.substitue(s), fd.substitue(s));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see logicline.modeleSemantique.Formule#valeur()
	 */
	@Override
	public boolean valeur() throws VariableLibreException {
		return fnc().valeur();
	}

	@Override
	public Formule supprImplications() {
		return new Et(new Ou(new Non(fg.supprImplications()),
				fd.supprImplications()), new Ou(fg.supprImplications(),
				new Non(fd.supprImplications())));
	}

	@Override
	protected boolean contientEt() {
		return fg.contientEt() || fd.contientEt();
	}

}
