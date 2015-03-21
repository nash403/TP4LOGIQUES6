package logicline.modeleSemantique;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

public class ListeClauses {
	public ListeClauses() {
		clauses = new LinkedList<Clause>();
	}

	public String toString() {
		if (clauses.size() == 0)
			return "⊤";

		String s = "";
		ListIterator<Clause> it = clauses.listIterator();
		s += it.next().toString();
		while (it.hasNext())
			s += " ∧ " + it.next().toString();
		return s;
	}

	public void add(Clause c) {
		clauses.add(c);
	}

	public void addAll(ListeClauses l) {
		clauses.addAll(l.clauses);
	}

	public Clause get(int i) {
		return clauses.get(i);
	}

	public ListeClauses simplifieClause(String litteral, boolean valeur)
			throws NotSatisfiableException {
		ListeClauses lc = new ListeClauses();
		int nbC = nbClauses();
		for (int i = 0; i < nbC; i++) {
			Clause c = get(i);
			Boolean posOuNeg = c.get(litteral);
			if (posOuNeg == null) {
				lc.add(c);
				continue;
			}
			if (posOuNeg) {
				if (valeur == false) {
					c.remove(litteral);
					lc.add(c);
				}
			} else {
				if (valeur == true) {
					c.remove(litteral);
					lc.add(c);
				}
			}
		}
		return lc;
	}

	/**
	 * @return les clauses unitaires de cette ListeClause
	 */
	public List<Clause> clauseUnitaires() {
		List<Clause> lc = new ArrayList<Clause>();
		for (Clause c : clauses) {
			String lit = c.clauseUnitaire();
			if (lit != null)
				lc.add(c);
		}
		return lc;
	}

	/**
	 * Vérifie dans une liste de clauses (supposées unitaires) si elle contient
	 * la clause contraire à la clause donnée en paramètre.
	 * 
	 * @param lc
	 *            la liste de clause unitaire
	 * @param c
	 *            la clause dont on cherche la contraire
	 * @return true si la liste en contient, false sinon
	 */
	public boolean contientContraire(List<Clause> lc, Clause c) {
		int l = lc.size();
		Clause c1;
		String s = c.clauseUnitaire(), s1;
		boolean val = c.get(s);
		for (int i = 0; i < l; i++) {
			c1 = lc.get(i);
			s1 = c1.clauseUnitaire();
			if (s.equals(s1)) {
				if (val == !c1.get(s1))
					return true;
			}
		}
		return false;
	}

	/**
	 * @return the number of clauses that this ListeClauses contains
	 */
	public int nbClauses() {
		return clauses.size();
	}

	public Substitution dpll() throws NotSatisfiableException {
		ListeClauses lc = this;
		Substitution substitution = new Substitution();
		String litteral;

		if (clauses.isEmpty())
			return substitution;

		// TRAITEMENT DES CLAUSES UNITAIRES
		List<Clause> clausesUnitaires = clauseUnitaires();
		if (!clausesUnitaires.isEmpty()) {
			Clause unitaire = clausesUnitaires.get(0);
			if (contientContraire(clausesUnitaires, unitaire))
				throw new NotSatisfiableException();
			litteral = unitaire.clauseUnitaire();
			boolean val = unitaire.get(litteral);
			lc = lc.simplifieClause(litteral, val);
			substitution = lc.dpll();
			substitution.set(litteral, new Constante(val));
			return substitution;
		} else {

			// TRAITEMENT DES LITTERAUX PURS
			List<String> lNeg = new ArrayList<String>(), lPos = new ArrayList<String>();
			for (Clause c : lc.clauses) {
				lNeg.addAll(c.litterauxNegatifs());
				lPos.addAll(c.litterauxPositifs());
			}
			String s;
			for (int i = 0; i < lNeg.size(); i++) {
				s = lNeg.get(i);
				// si un lit negatif est aussi positif on le retire des 2 listes
				if (lPos.contains(s)) {
					lPos.remove(s);
					lNeg.remove(i--);
				}
			}
			// ici lNeg et lPos contiennent les littéraux purs resp. negatifs et
			// positifs
			if (!lPos.isEmpty()) {
				litteral = lPos.get(0);
				lc = lc.simplifieClause(litteral, true);
				substitution = lc.dpll();
				substitution.set(litteral, new Constante(true));
				return substitution;
			} else if (!lNeg.isEmpty()) {
				litteral = lNeg.get(0);
				lc = lc.simplifieClause(litteral, false);
				substitution = lc.dpll();
				substitution.set(litteral, new Constante(false));
				return substitution;
			} else {
				// TRAITEMENT DES RESULTANTES
				litteral = get(0).premierLitteral();
				try {
					ListeClauses lcRTrue = lc.simplifieClause(litteral, true);
					substitution = lcRTrue.dpll();
					substitution.set(litteral, new Constante(true));
					return substitution;
				} catch (NotSatisfiableException e) {
					ListeClauses lcRFasle = lc.simplifieClause(litteral, false);
					substitution = lcRFasle.dpll();
					substitution.set(litteral, new Constante(false));
					return substitution;
				}
			}
		}

	}

	private List<Clause> clauses;
}
