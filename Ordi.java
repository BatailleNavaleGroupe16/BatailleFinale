/**
 * Classe Ordi qui gère l'attaque de l'ordinateur
 * Contient les méthodes nécessaires pour le choix de la case que l'ordi attaque
 * 
 * Nous avons recherché la meilleure manière de jouer pour faire un ordinateur assez bon.
 * Pour les premiers coups joués, l'ordinateur attaque de manière totalement aléatoire
 * Si il y a une touche, il cherche à attaquer sur la case adjacente où se trouve le plus probablement un bateau
 * Après 10 coups, on considère que la grille est suffisament quadrillée pour utiliser une densité de probabilité de la présence d'un bateau sur chaque case
 *   Le faire après 10 coups assure que chaque partie soit différente : sinon l'ordinateur joue toujours selon le même schéma
 * Ce fonctionnement est en partie tiré du travail de DataGenetics : https://www.datagenetics.com/blog/december32011/?fbclid=IwAR0CrTA2PGrzMjOiq_gceVdXwtjB6vxDBuRMHdXy0FWWMR5VjnjrzpBa5AY
 * qui nous a permis de percevoir quelle stratégie devrait utiliser l'ordinateur
 * 
 * En moyenne, d'après les tests que l'on a fait, un ordinateur met une quarantaine de coups pour gagner contre un autre ordinateur.
 * Le placement des bateaux d'un humain n'étant pas aléatoire, ce résultat peut varier si l'ordinateur affronte un humain
 */

public class Ordi{
    
    
    public byte[][] coups;
    public byte[][] proba;
    
    private byte[][] direction = { {0,-1}, {0,1}, {-1,0}, {1,0} };
    private byte aleat;
    
    private boolean attaque;
    private short i;
    public byte [] casesBateaux = {2, 3, 3, 4, 5};
    
    private byte ligne;
    private byte colonne;
    public byte caseL;
    public byte caseC;
    
    private byte tour = 0;
    private Bateau bateau;
    
    
    /**
     * Constructeur de la classe
     * Partage la grille des coups joués avec la classe joueur
     * (facilite les modifications et les recherches dans la grille)
     */
    public Ordi(byte[][] grille){
        this.coups = grille;
        proba = new byte[grille.length][grille[0].length];
        bateau = new Bateau(this);
    }
    
    
    /**
     * Méthode pour l'attaque de l'ordinateur
     * Prend le joueur actif et la cible en paramètre
     * Modifie Joueur mais ne retourne rien
     */
    public void AttaqueOrdi(Joueur joueur, Joueur cible){
        
        for(byte k = 0; k<casesBateaux.length; k+=1){
            if (cible.nbreCasesBateau[k]==0){//Si ce bateau est coulé
                this.casesBateaux[k] = 0;//Si on doit utiliser une probabilité de présence, on place des bateaux entier et pas des morceaux de bateau
            }
        }
        
        i = 0;
        attaque = false;//Pour savoir si une attaque a pu être menée ou pas
        
        while ((attaque==false) && (i<coups.length*coups[0].length)){
            
            //On parcourt les cases touchées de la grille et on détermine la meilleure façon d'attaquer à proximité
            
            
            if (coups[i/12][i%12]==-2){
                
                ligne = (byte)(i/12);
                colonne = (byte)(i%12);
                
                
                //Permet de connaître les coordonnées de la dernière case touchée visitée. (utile en cas de bateaux collés)
                
                
                if ((coups[ligne+1][colonne]!=-2) && (coups[ligne-1][colonne]!=-2) && (coups[ligne][colonne+1]!=-2) && (coups[ligne][colonne-1]!=-2)){//si la case est isolée
                    //Attaque aléatoire sur une case autour avec pondération selon proba
                    this.AttaqueRandom(cible);
                    
                }else{//Si la case a une autre touche à proximité
                    
                    byte k = 0;
                    
                    while ((attaque==false) && (k<direction.length)){//On va regarder les cases directement à côté pour jouer dans la direction donnée par les cases déjà touchées
                        if ((coups[ ligne+direction[k][0] ][colonne+direction[k][1]]==-2) && (coups[ligne-direction[k][0]][colonne-direction[k][1]]==0)){
                            //Vérifie les cases avant et après pour déterminer la direction la plus probable du bateau
                            
                            caseL = (byte)(ligne-direction[k][0]);
                            caseC = (byte)(colonne-direction[k][1]);
                            attaque = true;
                            
                        }
                        
                        k+=1;
                    }
                }
                
                //Si toutes les cases autour ont déjà été attaquées, on ne fait rien
                
            }
            
            
            i +=1;
        }
        
        
        //Si aucune attaque n'a pu être menée
        if (attaque==false){
            if (this.coups[this.ligne][this.colonne]==-2){//Si on a pas pu attaquer autour de la dernière case attaquée, il faut changer de sens d'attaque
                this.AttaqueRandom(cible);
                
            }else if (tour<10){//Si on a joué moins de 10 coups, on attaque complètement aléatoirement. Si la case était touchée, on a forcément pu attaquer
                
                do{
                    //Tirage d'une case paire aléatoirement. Le plus petit bateau fait 2 cases donc on est sûr de le toucher en attaquant une case sur 2
                    this.ligne = (byte)(10*Math.random() +1);
                    this.colonne = (byte)(2*(byte)(5*Math.random()) + this.ligne%2 +1);
                    
                }while(this.coups[this.ligne][this.colonne] != 0);
                
                this.caseL = this.ligne;
                this.caseC = this.colonne;
                
            }else{//Si la case n'a pas été attaquée et qu'on a joué plus de 10 coups, on attaque la case où il y a le plus de chance d'y avoir un bateau
                bateau.Proba(this, cible);
            }
        }
        
        
        
        joueur.caseL = this.caseL;
        joueur.caseC = this.caseC;
        
        tour +=1;
        
    }
    
    
    /**
     * Méthode pour attaquer aléatoirement autour de la case
     * Prend la cible en paramètre (pour connaître les bateaux qui lui restent)
     * Ne retourne rien
     */
    private void AttaqueRandom(Joueur cible){
        
        //Génération des probas
        bateau.Proba(this, cible);
        
        byte max = 0;
        for (byte k = 0; k<direction.length ; k +=1){//Recherche du plus probable directement autour de la case
            if (proba[ ligne+direction[k][0] ][colonne+direction[k][1]] > max){
                caseL = (byte)(ligne+direction[k][0]);
                caseC = (byte)(colonne+direction[k][1]);
                max = proba[ ligne+direction[k][0] ][colonne+direction[k][1]];
            }
        }
        
        attaque = true;
        
    }
     
    
}
