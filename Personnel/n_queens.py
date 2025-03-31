def est_safe(plateau, ligne, colonne, n):
    # Vérifier la ligne sur la gauche
    for i in range(colonne):
        if plateau[ligne][i] == 1:
            return False
    
    # Vérifier la diagonale supérieure gauche
    for i, j in zip(range(ligne, -1, -1), range(colonne, -1, -1)):
        if plateau[i][j] == 1:
            return False
    
    # Vérifier la diagonale inférieure gauche
    for i, j in zip(range(ligne, n), range(colonne, -1, -1)):
        if plateau[i][j] == 1:
            return False
    
    return True

def resoudre_n_reines_util(plateau, colonne, n, solutions):
    # Si toutes les reines sont placées
    if colonne >= n:
        # Ajouter la solution trouvée
        solution = []
        for i in range(n):
            ligne = []
            for j in range(n):
                ligne.append(plateau[i][j])
            solution.append(ligne)
        solutions.append(solution)
        return True
    
    resultat = False
    # Essayer de placer une reine dans chaque ligne de cette colonne
    for i in range(n):
        if est_safe(plateau, i, colonne, n):
            # Placer la reine
            plateau[i][colonne] = 1
            
            # Récursivement placer le reste des reines
            # Pour trouver toutes les solutions, on continue même après avoir trouvé une solution
            temp = resoudre_n_reines_util(plateau, colonne + 1, n, solutions)
            resultat = resultat or temp
            
            # Retirer la reine pour essayer d'autres configurations
            plateau[i][colonne] = 0
    
    return resultat

def resoudre_n_reines(n, toutes_solutions=False):
    plateau = [[0 for _ in range(n)] for _ in range(n)]
    solutions = []
    
    if not resoudre_n_reines_util(plateau, 0, n, solutions):
        print("Aucune solution n'existe")
        return False
    
    if toutes_solutions:
        print(f"{len(solutions)} solutions trouvées pour n={n}")
        choix = input("Voulez-vous afficher toutes les solutions? (o/n): ")
        if choix.lower() == 'o':
            for i, solution in enumerate(solutions):
                print(f"\nSolution {i+1}:")
                afficher_solution(solution, n)
        else:
            i = 0
            while True:
                if i >= len(solutions):
                    break
                print(f"\nSolution {i+1}/{len(solutions)}:")
                afficher_solution(solutions[i], n)
                if i < len(solutions) - 1:
                    choix = input("Voir la solution suivante? (o/n): ")
                    if choix.lower() != 'o':
                        break
                else:
                    print("C'était la dernière solution.")
                    break
                i += 1
    else:
        print("\nUne solution:")
        afficher_solution(solutions[0], n)
    
    return True

def afficher_solution(plateau, n):
    for i in range(n):
        ligne = ""
        for j in range(n):
            if plateau[i][j] == 1:
                ligne += "♛ "  # Symbole de la reine
            else:
                # Cases alternées pour représenter l'échiquier
                if (i + j) % 2 == 0:
                    ligne += "□ "  # Case blanche
                else:
                    ligne += "■ "  # Case noire
        print(ligne)
    print()

def afficher_solution_coordonnees(plateau, n):
    positions = []
    for i in range(n):
        for j in range(n):
            if plateau[i][j] == 1:
                positions.append((i, j))
    
    print("Positions des reines (ligne, colonne):")
    for pos in positions:
        print(f"({pos[0]}, {pos[1]})")

def main():
    try:
        n = int(input("Entrez le nombre de reines (n): "))
        if n <= 0:
            print("Le nombre de reines doit être positif.")
            return
        
        if n == 2 or n == 3:
            print(f"Il n'existe pas de solution pour n={n}")
            return
            
        toutes_solutions = input("Voulez-vous trouver toutes les solutions? (o/n): ").lower() == 'o'
        
        import time
        debut = time.time()
        resoudre_n_reines(n, toutes_solutions)
        fin = time.time()
        
        print(f"\nTemps d'exécution: {fin - debut:.4f} secondes")
        
    except ValueError:
        print("Veuillez entrer un nombre entier valide.")

if __name__ == "__main__":
    main() 