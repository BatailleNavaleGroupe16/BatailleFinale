/**
 * Le programme principal qui gère la boucle du jeu
 * Choix du mode de jeu
 * Organisation d'un tour de jeu
 * 
 */
 
import java.util.Scanner;

public class BatailleNavale {
    
    public static void main(String[] args){
        
        Scanner sc = new Scanner(System.in);
        
        System.out.println("Bienvenue, vous allez jouer au jeu de la bataille navale\nVotre but est de trouver les bateaux de l'adversaire avant qu'il ne coule votre flotte\n\nC'est parti !\n\n\n\n");
        
        //Initialisation : choix du mode jeu : JVJ, JVO
        
        byte typeJeu = 0;
        byte typeMax = 4;
        
        
        do{
            System.out.println("Veuillez choisir le mode de jeu");
            System.out.println("Recherche des bateaux de l'ordinateur : 1\nJoueur contre Joueur : 2\nJoueur contre Ordinateur : 3" + "\nAffrontement de deux ordinateurs : 4");
            System.out.print("Votre choix : ");
            typeJeu = (byte)sc.nextInt();
            
        }while ((typeJeu <=0) || (typeJeu > typeMax));//Tant que le mode de jeu choisi n'existe pas on redemande
        
        System.out.println("\n\n\n\n\n");
        
        //Création des joueurs
        
        Joueur j1;
        Joueur j2;
        
        Joueur[] tabJ = new Joueur[2];
        
        
        if (typeJeu == 1){
            j1 = new Joueur(2);
            j2 = new Joueur(3);
            
        }else if(typeJeu == 2){
            j1 = new Joueur(1);
            j2 = new Joueur(1);
            
        }else if(typeJeu == 4){
            j1 = new Joueur(4);
            j2 = new Joueur(4);
            
        }else{
            j1 = new Joueur(1);
            j2 = new Joueur(4);
        }
        
        tabJ[0] = j1;
        tabJ[1] = j2;
        
        
        
        
        /*Boucle du jeu :
        Changement de joueur
        Affichage des coups joués
        Choix de la case à attaquer
        Teste si le jeu est fini
        */
        
        byte actuel = 1; // pour commencer par joueur 1 dans boucle
        byte autre = (byte)((actuel + 1)%2);
        boolean enCours = true;
        byte tour = 0;
        
        while (enCours){
            
            actuel = (byte)((actuel + 1)%2);
            autre = (byte)((actuel + 1)%2);
            
            tour +=1;
            
            if (tabJ[actuel].type != 3){
                System.out.println("Joueur " + (actuel+1));
                
                //Affichage coups joués
                tabJ[actuel].afficheGrille(tabJ[actuel].coupsPrecedents);
                
                //Affichage nb bateaux restants
                System.out.println("Il reste " + tabJ[autre].nbBateaux + " bateaux à trouver");
                tabJ[actuel].Attaque(tabJ[autre]);
                
                
                System.out.println("Tour joué\n\n\n");
            }
            
            
            enCours = tabJ[autre].fini();
            
            
        }
        
        System.out.println("\n\n\n\n\n\n\n\n\n" + "Le joueur " + (actuel+1) + " a gagné cette partie en " + (tour/2) + " coups\n\n");
        
        
        tabJ[actuel].afficheGrille(tabJ[actuel].coupsPrecedents);
        
        
    }
    
      
}
