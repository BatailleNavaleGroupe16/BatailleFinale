/**
 * Classe Bateau pour tout ce qui concerne les bateaux
 * Gère les méthodes de placement des bateaux et les vérifications nécessaires
 * 
 */


import java.util.Scanner;

public class Bateau {
	
    Scanner sc = new Scanner(System.in);
    
    public byte[][] grille;
    
    public byte numero;
    public byte taille;
    public byte debutL;
    public byte debutC;
    public byte estVertical; //1 pour vertical, 2 pour horizontal
    
    
    //Constructeur pour le placement des bateaux
    public Bateau (Joueur joueur) {
        this.grille = joueur.bateaux;
    }
    
    
    //Constructeur pour la probabilité de présence de l'ordinateur
    public Bateau (Ordi ordi){
        this.grille = ordi.coups;
    }
    
    
    /**
     * Méthode pour le placement des 5 bateaux
     * Prend le joueur en paramètre
     * Ne retourne rien
     */
    public void placement (Joueur joueur){
        for (numero = 1; numero <= joueur.nbreCasesBateau.length; numero+=1){//On parcourt le tableau des tailles des bateaux
            
            
            this.taille = joueur.nbreCasesBateau[numero-1];
            do{
                
                if (joueur.type == 1){
                    joueur.afficheGrille(this.grille);
                    this.placementJoueur();
                    
                }else{
                    this.placementOrdi();
                }
                
            }while(!this.verifPlace());
            
            this.placer();
            
            
            
        }
    }
    
    
    /**
     * Méthode pour le placement des bateaux pour le joueur
     * Modifie directement la grille du joueur
     * 
     */
    public void placementJoueur () {
        
        //Le bateau est repéré par sa position de début dans le sens de la lecture (en haut à gauche) et son orientation (verticale ou horizontale)
        
        do{
            System.out.println("Veuillez saisir la ligne de la position la plus à gauche et la plus en haut de votre bateau n° : "+ numero + " de taille " + this.taille);
            this.debutL = (byte)sc.nextInt();
        }while(debutL < 1 || debutL > 10);//Doit être dans la grille
        
        do{
            System.out.println("Veuillez saisir la colonne de la position la plus à gauche et la plus en haut de votre bateau n° : "+ numero + " de taille " + this.taille);
            this.debutC = (byte)sc.nextInt();
        }while(debutC < 1 || debutC > 10);//Doit être dans la grille
        
        do{
            System.out.println("Veuillez saisir l'orientation de votre bateau : VERTICAL : 1 / HORIZONTAL : 2 ");
            this.estVertical = (byte)sc.nextInt();
        }while(this.estVertical != 1 && this.estVertical != 2);
        
        System.out.println("\nPlacé\n\n\n");
    }
    
    
    /**
     * Méthode pour le placement d'un bateau par l'ordi
     * Ne prend pas de paramètre
     * Ne retourne rien
     */
    public void placementOrdi () {
        
        //On tire une case de début et une orientation aléatoires
        
        this.debutL = (byte)((this.grille.length-2)*Math.random()+1);
        this.debutC = (byte)((this.grille[0].length-2)*Math.random()+1);
        this.estVertical = (byte)(2*Math.random()+1);
        
    }
    
    
    /**
     * Méthode pour vérifier que la place est libre
     * Ne prend pas de paramètre
     * Ne retourne rien
     */
	public boolean verifPlace(){
        byte i = 0;
        boolean bool = true;//Tant que le placement est bon on continue
        
        //On parcourt les cases que le bateau utiliserait pour vérifier qu'elles sont libres
        
        if(estVertical == 1){
            while(i < this.taille && bool){
                bool = ((this.grille[this.debutL+i][this.debutC] == 0) || (this.grille[this.debutL+i][this.debutC] == -2));//Si la case est libre ou qu'elle a été touchée (n'a pas d'influence sur le placement des bateaux)
                i++;
            }
            
        }else{
            while(i < this.taille && bool){
                bool = ((this.grille[this.debutL][this.debutC+i] == 0) || (this.grille[this.debutL][this.debutC+i] == -2));//Idem
                i++;
            }
        }
        return bool;
    }
    
    
    /**
     * Méthode pour placer un bateau sur la grille
     * Ne prend pas de paramètre
     * Ne retourne rien
     */
    public void placer () {
        
        //On parcourt les cases utilisées par le bateau pour leur donner la bonne valeur
        
        if (this.estVertical == 1) {//Si le bateau est vertical, on parcourt la colonne, sinon on parcourt la ligne
            for (byte i = 0; i < this.taille; i++) {
                this.grille[this.debutL+i][this.debutC] = this.numero;
            }
        } else {
            for (byte i = 0; i < this.taille; i++) {
                this.grille[this.debutL][this.debutC+i] = this.numero;
            }
        }
    }
    
    
    
    /**
     * Méthode pour le calcul des probabilités de présence des bateaux adverses
     * Recherche la case optimale
     * Prend la cible de l'attaque et l'ordi qui joue en paramètre
     * Modifie l'ordi mais ne retourne rien
     */
    public void Proba(Ordi ordi, Joueur cible){
        
        //Réinitialisation du tableau des probas
        for(byte i = 1; i<ordi.coups.length-1; i+=1){
            for(byte j = 1; j<ordi.coups[0].length-1; j+=1){
                ordi.proba[i][j] = 0;
            }
        }
        
        byte max = 0;
        
        //Parcours du tableau des probas pour déterminer où sont les bateaux
        for(byte i = 1; i<this.grille.length-1; i+=1){
            for(byte j = 1; j<this.grille[0].length-1; j+=1){
                
                if ((ordi.coups[i][j] == 0) || (ordi.coups[i][j] == -2)){//Si on peut potentiellemnt mettre un bateau
                    for (byte k = 0; k<ordi.casesBateaux.length; k+=1){//On essaie de placer les bateaux qui n'ont pas été coulés
                        if (ordi.casesBateaux[k] != 0){
                            taille = ordi.casesBateaux[k];
                            debutL = i;
                            debutC = j;
                            estVertical = 1;
                            if (verifPlace()){
                                //Ajoute 1 à chaque place
                                ajouterProba(ordi);
                            }
                            estVertical = 2;
                            if (verifPlace()){
                                //Ajoute 1 à chaque place
                                ajouterProba(ordi);
                            }
                            
                            
                        }
                    }
                }
                if ((ordi.proba[i][j]>max) && (ordi.coups[i][j]==0)){//Les case d'après ne modifient pas le score de cette case à cause de l'orientation des bateaux
                    max = ordi.proba[i][j];
                    ordi.caseL = i;
                    ordi.caseC = j;
                }
                
            
            }
            
        }
    
    }
    
    
    
    /**
     * Méthode pour modifier le tableau de probabilité de présence de l'ordi
     * 
     * 
     */
    public void ajouterProba (Ordi ordi) {
        
        //On parcourt les cases utilisées par le bateau pour augmenter leur score
        
        if (this.estVertical == 1) {
            for (byte i = 0; i < this.taille; i++) {
                ordi.proba[this.debutL+i][this.debutC] +=1;
            }
        } else {
            for (byte i = 0; i < this.taille; i++) {
                ordi.proba[this.debutL][this.debutC+i] +=1;
            }
        }
    }
    
    
    
    
    
}
