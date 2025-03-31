import pygame
import random
import time
import sys

# Initialisation de pygame
pygame.init()

# Constantes
LARGEUR, HAUTEUR = 800, 600
TAILLE_CASE = 20
VITESSE_JEU = 10

# Couleurs
NOIR = (0, 0, 0)
BLANC = (255, 255, 255)
VERT = (0, 175, 0)
VERT_CLAIR = (0, 255, 0)
ROUGE = (255, 0, 0)
BLEU = (0, 0, 255)
GRIS = (100, 100, 100)

# Création de la fenêtre
fenetre = pygame.display.set_mode((LARGEUR, HAUTEUR))
pygame.display.set_caption("Snake - Le Jeu")
horloge = pygame.time.Clock()

# Chargement des polices
try:
    police_score = pygame.font.Font(None, 36)
    police_game_over = pygame.font.Font(None, 72)
    police_titre = pygame.font.Font(None, 100)
    police_menu = pygame.font.Font(None, 50)
except:
    police_score = pygame.font.SysFont('arial', 36)
    police_game_over = pygame.font.SysFont('arial', 72)
    police_titre = pygame.font.SysFont('arial', 100)
    police_menu = pygame.font.SysFont('arial', 50)

class Snake:
    def __init__(self):
        self.reset()
    
    def reset(self):
        self.x = LARGEUR // 2
        self.y = HAUTEUR // 2
        self.direction = 'DROITE'
        self.corps = [
            {'x': self.x, 'y': self.y},
            {'x': self.x - TAILLE_CASE, 'y': self.y},
            {'x': self.x - (2 * TAILLE_CASE), 'y': self.y}
        ]
        self.score = 0
        self.vitesse = VITESSE_JEU
        
    def bouger(self, direction=None):
        if direction:
            self.direction = direction
            
        # Ajouter un nouveau segment à la tête dans la direction actuelle
        if self.direction == 'DROITE':
            nouvelle_tete = {'x': self.corps[0]['x'] + TAILLE_CASE, 'y': self.corps[0]['y']}
        elif self.direction == 'GAUCHE':
            nouvelle_tete = {'x': self.corps[0]['x'] - TAILLE_CASE, 'y': self.corps[0]['y']}
        elif self.direction == 'HAUT':
            nouvelle_tete = {'x': self.corps[0]['x'], 'y': self.corps[0]['y'] - TAILLE_CASE}
        elif self.direction == 'BAS':
            nouvelle_tete = {'x': self.corps[0]['x'], 'y': self.corps[0]['y'] + TAILLE_CASE}
            
        self.corps.insert(0, nouvelle_tete)
        
    def manger(self, nourriture):
        # Vérifie si la tête du serpent est sur la nourriture
        if (self.corps[0]['x'] == nourriture.position['x'] and 
            self.corps[0]['y'] == nourriture.position['y']):
            self.score += 1
            # Augmente légèrement la vitesse
            if self.score % 5 == 0:
                self.vitesse += 1
            return True
        else:
            # Si on ne mange pas, on retire la queue
            self.corps.pop()
            return False
            
    def collision(self):
        # Vérifier les collisions avec les murs
        tete = self.corps[0]
        if (tete['x'] < 0 or tete['x'] >= LARGEUR or 
            tete['y'] < 0 or tete['y'] >= HAUTEUR):
            return True
            
        # Vérifier les collisions avec son propre corps
        for segment in self.corps[1:]:
            if tete['x'] == segment['x'] and tete['y'] == segment['y']:
                return True
                
        return False
        
    def dessiner(self):
        # Dessiner le corps du serpent
        for i, segment in enumerate(self.corps):
            couleur = VERT if i > 0 else VERT_CLAIR
            pygame.draw.rect(fenetre, couleur, 
                pygame.Rect(segment['x'], segment['y'], TAILLE_CASE, TAILLE_CASE))
            pygame.draw.rect(fenetre, NOIR, 
                pygame.Rect(segment['x'], segment['y'], TAILLE_CASE, TAILLE_CASE), 1)
                
        # Dessiner les yeux sur la tête
        tete = self.corps[0]
        oeil_rayon = 3
        if self.direction == 'DROITE':
            oeil1_pos = (tete['x'] + TAILLE_CASE - 5, tete['y'] + 5)
            oeil2_pos = (tete['x'] + TAILLE_CASE - 5, tete['y'] + TAILLE_CASE - 5)
        elif self.direction == 'GAUCHE':
            oeil1_pos = (tete['x'] + 5, tete['y'] + 5)
            oeil2_pos = (tete['x'] + 5, tete['y'] + TAILLE_CASE - 5)
        elif self.direction == 'HAUT':
            oeil1_pos = (tete['x'] + 5, tete['y'] + 5)
            oeil2_pos = (tete['x'] + TAILLE_CASE - 5, tete['y'] + 5)
        elif self.direction == 'BAS':
            oeil1_pos = (tete['x'] + 5, tete['y'] + TAILLE_CASE - 5)
            oeil2_pos = (tete['x'] + TAILLE_CASE - 5, tete['y'] + TAILLE_CASE - 5)
            
        pygame.draw.circle(fenetre, NOIR, oeil1_pos, oeil_rayon)
        pygame.draw.circle(fenetre, NOIR, oeil2_pos, oeil_rayon)
        
class Nourriture:
    def __init__(self):
        self.position = self.generer_position()
        
    def generer_position(self):
        x = random.randrange(0, LARGEUR - TAILLE_CASE, TAILLE_CASE)
        y = random.randrange(0, HAUTEUR - TAILLE_CASE, TAILLE_CASE)
        return {'x': x, 'y': y}
        
    def reposition(self, snake):
        self.position = self.generer_position()
        # S'assurer que la nourriture ne soit pas générée sur le serpent
        while any(segment['x'] == self.position['x'] and segment['y'] == self.position['y'] 
                  for segment in snake.corps):
            self.position = self.generer_position()
        
    def dessiner(self):
        pomme_rect = pygame.Rect(self.position['x'], self.position['y'], TAILLE_CASE, TAILLE_CASE)
        pygame.draw.rect(fenetre, ROUGE, pomme_rect)
        # Dessin de la queue de la pomme
        queue_rect = pygame.Rect(self.position['x'] + TAILLE_CASE//2 - 2, 
                                self.position['y'] - 5, 4, 5)
        pygame.draw.rect(fenetre, VERT, queue_rect)

def afficher_score(score):
    texte = police_score.render(f"Score: {score}", True, BLANC)
    fenetre.blit(texte, (10, 10))

def dessiner_grille():
    for x in range(0, LARGEUR, TAILLE_CASE):
        pygame.draw.line(fenetre, GRIS, (x, 0), (x, HAUTEUR))
    for y in range(0, HAUTEUR, TAILLE_CASE):
        pygame.draw.line(fenetre, GRIS, (0, y), (LARGEUR, y))

def game_over(score):
    fenetre.fill(NOIR)
    
    texte_game_over = police_game_over.render("GAME OVER", True, ROUGE)
    texte_score = police_score.render(f"Score final: {score}", True, BLANC)
    texte_rejouer = police_menu.render("Appuyez sur R pour rejouer", True, VERT)
    texte_quitter = police_menu.render("Appuyez sur Q pour quitter", True, ROUGE)
    
    fenetre.blit(texte_game_over, 
                (LARGEUR//2 - texte_game_over.get_width()//2, HAUTEUR//4))
    fenetre.blit(texte_score, 
                (LARGEUR//2 - texte_score.get_width()//2, HAUTEUR//2))
    fenetre.blit(texte_rejouer, 
                (LARGEUR//2 - texte_rejouer.get_width()//2, HAUTEUR//2 + 100))
    fenetre.blit(texte_quitter, 
                (LARGEUR//2 - texte_quitter.get_width()//2, HAUTEUR//2 + 150))
    
    pygame.display.update()
    
    attente = True
    while attente:
        for event in pygame.event.get():
            if event.type == pygame.QUIT:
                pygame.quit()
                sys.exit()
            if event.type == pygame.KEYDOWN:
                if event.key == pygame.K_q:
                    pygame.quit()
                    sys.exit()
                if event.key == pygame.K_r:
                    attente = False
                    
def menu_principal():
    fenetre.fill(NOIR)
    
    texte_titre = police_titre.render("SNAKE", True, VERT)
    texte_jouer = police_menu.render("Appuyez sur ESPACE pour jouer", True, BLANC)
    texte_quitter = police_menu.render("Appuyez sur Q pour quitter", True, ROUGE)
    
    fenetre.blit(texte_titre, 
                (LARGEUR//2 - texte_titre.get_width()//2, HAUTEUR//4))
    fenetre.blit(texte_jouer, 
                (LARGEUR//2 - texte_jouer.get_width()//2, HAUTEUR//2))
    fenetre.blit(texte_quitter, 
                (LARGEUR//2 - texte_quitter.get_width()//2, HAUTEUR//2 + 70))
    
    # Animation du serpent dans le menu
    temps = pygame.time.get_ticks() // 100
    for i in range(5):
        x = (temps + i * 3) % (LARGEUR // TAILLE_CASE)
        y = HAUTEUR//2 - 100
        pygame.draw.rect(fenetre, VERT, 
            pygame.Rect(x * TAILLE_CASE, y, TAILLE_CASE, TAILLE_CASE))
    
    pygame.display.update()
    
    while True:
        for event in pygame.event.get():
            if event.type == pygame.QUIT:
                pygame.quit()
                sys.exit()
            if event.type == pygame.KEYDOWN:
                if event.key == pygame.K_q:
                    pygame.quit()
                    sys.exit()
                if event.key == pygame.K_SPACE:
                    return
        
        # Animation continue du serpent
        temps = pygame.time.get_ticks() // 100
        fenetre.fill(NOIR, (0, HAUTEUR//2 - 100, LARGEUR, TAILLE_CASE))
        for i in range(5):
            x = (temps + i * 3) % (LARGEUR // TAILLE_CASE)
            y = HAUTEUR//2 - 100
            pygame.draw.rect(fenetre, VERT, 
                pygame.Rect(x * TAILLE_CASE, y, TAILLE_CASE, TAILLE_CASE))
        
        fenetre.blit(texte_titre, 
                    (LARGEUR//2 - texte_titre.get_width()//2, HAUTEUR//4))
        pygame.display.update()
        horloge.tick(60)

def pause():
    paused = True
    
    texte_pause = police_game_over.render("PAUSE", True, BLANC)
    texte_continuer = police_menu.render("Appuyez sur P pour continuer", True, VERT)
    
    fenetre.blit(texte_pause, 
                (LARGEUR//2 - texte_pause.get_width()//2, HAUTEUR//4))
    fenetre.blit(texte_continuer, 
                (LARGEUR//2 - texte_continuer.get_width()//2, HAUTEUR//2))
    
    pygame.display.update()
    
    while paused:
        for event in pygame.event.get():
            if event.type == pygame.QUIT:
                pygame.quit()
                sys.exit()
            if event.type == pygame.KEYDOWN:
                if event.key == pygame.K_p:
                    paused = False

def jouer():
    serpent = Snake()
    nourriture = Nourriture()
    
    jeu_actif = True
    derniere_direction = serpent.direction
    
    while jeu_actif:
        for event in pygame.event.get():
            if event.type == pygame.QUIT:
                pygame.quit()
                sys.exit()
            if event.type == pygame.KEYDOWN:
                if event.key == pygame.K_RIGHT and derniere_direction != 'GAUCHE':
                    serpent.direction = 'DROITE'
                elif event.key == pygame.K_LEFT and derniere_direction != 'DROITE':
                    serpent.direction = 'GAUCHE'
                elif event.key == pygame.K_UP and derniere_direction != 'BAS':
                    serpent.direction = 'HAUT'
                elif event.key == pygame.K_DOWN and derniere_direction != 'HAUT':
                    serpent.direction = 'BAS'
                elif event.key == pygame.K_p:
                    pause()
        
        derniere_direction = serpent.direction
        serpent.bouger()
        
        if serpent.manger(nourriture):
            nourriture.reposition(serpent)
            
        if serpent.collision():
            jeu_actif = False
            
        fenetre.fill(NOIR)
        dessiner_grille()
        nourriture.dessiner()
        serpent.dessiner()
        afficher_score(serpent.score)
        
        pygame.display.update()
        horloge.tick(serpent.vitesse)
        
    game_over(serpent.score)

# Boucle principale du jeu
def main():
    while True:
        menu_principal()
        jouer()

if __name__ == "__main__":
    main() 