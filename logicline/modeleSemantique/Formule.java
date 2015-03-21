package logicline.modeleSemantique;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public abstract class Formule {
	Set<String> vl = new HashSet<>();

	public Formule() {
	}

	// retourne une représentation ASCII de la formule logique
	public abstract String toString();

	// supprime toutes les implications de la formule
	protected Formule supprImplications() {
		return this;
	}

	// déplace les non à l'intérieur des formules
	protected Formule entrerNegations() {
		return this;
	}

	// Retourne la formule représentant la négation de this
	protected Formule negation() {
		return new Non(this);
	}

	// Retourne vrai si la formule contient un Et
	protected boolean contientEt() {
		return false;
	}

	// Retourne une formule équivalente à OU(this, d)
	protected Formule ougauche(Formule d) {
		return d.oudroite(this);
	}

	// Retourne une formule équivalente à OU(g, this), g ne contenant pas de ET
	protected Formule oudroite(Formule g) {
		return new Ou(g, this);
	}

	// déplace les non à l'intérieur des formules
	protected Formule entrerDisjonctions() {
		return this;
	}

	// transforme la formule en FNC
	public Formule fnc() {
		Formule f = this.supprImplications();
		f = f.entrerNegations();
		f = f.entrerDisjonctions();
		return f;
	}

	// retourne la liste des clauses d'une formule en FNC
	public ListeClauses clauses() throws NotFNCException, TrueClauseException,
			FalseClauseException, VariableClauseException {
		throw new NotFNCException(this);
	}

	// retourne la liste des noms des variables libres de la formule
	public abstract Set<String> variablesLibres();

	// effectue une substitution dans une formule
	public abstract Formule substitue(Substitution s);

	// retourne l'évaluation de la formule
	public abstract boolean valeur() throws VariableLibreException;

	/**
	 * @param valeur
	 *            la valeur dont on veut la chaine binaire
	 * @param longueur
	 *            la taille nminimum de la chaine
	 * @return la représentation binaire de valeur de taille minimum longueur
	 */
	public static String enBinaireDeTaille(int valeur, int longueur) {
		String binaire = Integer.toBinaryString(valeur);
		StringBuilder zeroDePoidsFort = new StringBuilder();
		int longBinanire = binaire.length();
		for (int i = 0; i < longueur - longBinanire; i++)
			zeroDePoidsFort.append("0");
		return zeroDePoidsFort + binaire;
	}

	// affiche la table de vérité de la formule
	public void tableVerite() {
		int i, j;
		Set<String> variablesL = variablesLibres();
		Object[] vlTab = variablesL.toArray();
		int nbLignes = (int) Math.pow(2, (double) variablesL.size());
		int tailleRepresentationBin = variablesL.size();

		// création de la liste des substitutions correspondant à chaque ligne
		// de la table
		List<Substitution> ls = new ArrayList<>();
		for (i = 0; i < nbLignes; i++) {
			Substitution s = new Substitution();
			String ligneStr = enBinaireDeTaille(i, tailleRepresentationBin);
			char[] ligneChar = ligneStr.toCharArray();
			for (j = 0; j < tailleRepresentationBin; j++) {
				// '0' correspond à FAUX, '1' VRAI
				switch (ligneChar[j]) {
				case '0':
					s.set((String) vlTab[j], new Constante(false));
					break;
				case '1':
					s.set((String) vlTab[j], new Constante(true));
					break;

				default:
					System.out.println("problème");
					return;
				}
			}
			ls.add(s);
		}

		// création de l'affichage
		StringBuilder sb = new StringBuilder("");
		for (i = 0; i < tailleRepresentationBin; i++) {
			if (i == 0)
				sb.append((String) vlTab[i]);
			else
				sb.append("\t|").append((String) vlTab[i]);
		}
		sb.append("\t|").append(this.toString()).append("\n");
		for (Substitution s : ls) {
			for (i = 0; i < tailleRepresentationBin; i++) {
				String constante = s.get((String) vlTab[i]).toString();
				if (i == 0)
					sb.append(constante);
				else
					sb.append("\t|").append(constante);
			}
			try {
				sb.append("\t|")
						.append(new Constante(this.substitue(s).valeur())
								.toString()).append("\n");
			} catch (VariableLibreException e) {
				e.printStackTrace();
			}
		}
		System.out.println(sb.toString());
	}
}
