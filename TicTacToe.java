//-------------8422 - Batch C - Kenneth Rebello - Experiment 5 --------------//
//-------------TIC TAC TOE game using Minimax Algorithm --------------//


import java.util.*;

class AIPlayer{

    static int computeBestMove(Game game){
        List<Integer> availableMoves = game.availableMoves();
        if(availableMoves.size()>0){
            int bestMove = availableMoves.get(0);
            int bestScore = 0;
            for(Integer space: availableMoves){
                Game nextGameState = new Game(game);
                nextGameState.aiMarks(space);
                int eval = minimax(nextGameState, Player.Human);
                if(eval>bestScore){
                    bestScore = eval;
                    bestMove = space;
                }
            }
            return bestMove;
        }
        return -1;
    }

    static int minimax(Game state, Player turn){
        List<Integer> availableMoves = state.availableMoves();
        
        int res = 0;
        Player winner = state.getWinner();
        if(availableMoves.size()<=0 || winner!=Player.None){
            
            if(winner == Player.Human) {
                return -10;
            }
            if(winner == Player.AI) {
                return 10;
            }
            return 0;
        }
        else if(turn == Player.AI){
            res = 0;
            for(Integer space: availableMoves){
                Game nextGameState = new Game(state);
                nextGameState.aiMarks(space);
                int eval = minimax(nextGameState, Player.Human)-state.level;
                if(eval>res){
                    res = eval;
                }
            }
        }
        else if(turn == Player.Human){
            res = 100;
            for(Integer space: availableMoves){
                Game nextGameState = new Game(state);
                nextGameState.humanMarks(space);
                int eval = minimax(nextGameState, Player.AI)+state.level;
                if(eval<res){
                    res = eval;
                }
            }
        }
        
        return res;
    }
}

class PlayGame{
    Game game;
    char[] initBoard = new char[]{'1','2','3','4','5','6','7','8','9'};
    Game init = new Game(initBoard);
    Player winner = Player.None;

    PlayGame(Game i){
        this.game = i;
    }

    void startGame(){
        Scanner sc = new Scanner(System.in);
        System.out.println(this.init);
        System.out.println("You know the rules! Enter a number between 1 to 9 to play your move");
        Player winner = game.getWinner();
        while(winner == Player.None && !game.isComplete()){
            System.out.println(game);
            System.out.println("\n\tYour move");
            int move = sc.nextInt();
            while(!game.humanMarks(move-1)){
                move = sc.nextInt();
            };
            winner = game.getWinner();
            if(winner != Player.None) break;
            int aiMove = AIPlayer.computeBestMove(game);
            game.aiMarks(aiMove);
            winner = game.getWinner();
            if(winner != Player.None) break;
            System.out.println("AI plays "+(aiMove+1)+"\n");
        }
        switch (winner) {
            case Human:
                System.out.println(game);
                System.out.println("\n\n*******Human wins*******");
                break;
            case AI:
                System.out.println(game);
                System.out.println("\n\n*******AI wins*******");
                break;
            case Draw:
                System.out.println(game);
                System.out.println("\n\n*******Draw*******");
                break;
            default:
                break;
        }
    }

}


enum Player{
    Human,
    AI,
    Draw,
    None,
}

class Game implements Comparable{

    char[] board = {'-','-','-','-','-','-','-','-','-'};
    Player playerX;
    Player playerO;
    int level;

    Game(){
        this.playerX = Player.Human;
        this.playerO = Player.AI;
        this.level = 0;
    };

    Game(char newBoard[]){
        this.board = newBoard;
        this.playerX = Player.Human;
        this.playerO = Player.AI;
        this.level = 0;
    }

    Game(Game g){
        char[] temp = g.board;
        this.board = new char[] {
            temp[0], temp[1], temp[2], temp[3], temp[4], temp[5], temp[6], temp[7], temp[8]
        };
        this.playerX = g.playerX;
        this.playerO = g.playerO;
        this.level = g.level + 1;
    }

    List<Integer> availableMoves(){
        List<Integer> res = new ArrayList<Integer>();
        for(int i=0; i<board.length; i++){
            if(board[i]=='-'){
                res.add(i);
            }
        }
        return res;
    }

    boolean humanMarks(int pos){
        if(pos>8||pos<0){
            return true;
        }
        if(this.board[pos]!='-'){
            System.out.println("Invalid move! Try again!");
            return false;
        }
        if(this.playerX==Player.Human){
            this.board[pos] ='X';
        }
        else if(this.playerO==Player.Human){
            this.board[pos] = 'O';
        }
        return true;
    }

    void aiMarks(int pos){
        if(pos>8||pos<0){
            return;
        }
        if(this.playerX==Player.AI){
            this.board[pos] ='X';
        }
        else if(this.playerO==Player.AI){
            this.board[pos] = 'O';
        }
    }

    Player getWinner(){
        if(winCondition('X')){
            return playerX;
        }
        if(winCondition('O')){
            return playerO;
        }
        if(this.availableMoves().size()<=0){
            return Player.Draw;
        }
        return Player.None;
    }

    boolean isComplete(){
        boolean res = true;
        for(int i=0; i<board.length; i++){
            if(board[i]=='-') res = false;
        }
        return res;
    }

    boolean winCondition(char symbol){
        if(board[0]==symbol && board[1]==symbol && board[2]==symbol){
            return true;
        }
        if(board[3]==symbol && board[4]==symbol && board[5]==symbol){
            return true;
        }
        if(board[6]==symbol && board[7]==symbol && board[8]==symbol){
            return true;
        }
        if(board[0]==symbol && board[3]==symbol && board[6]==symbol){
            return true;
        }
        if(board[1]==symbol && board[4]==symbol && board[7]==symbol){
            return true;
        }
        if(board[2]==symbol && board[5]==symbol && board[8]==symbol){
            return true;
        }
        if(board[0]==symbol && board[4]==symbol && board[8]==symbol){
            return true;
        }
        if(board[2]==symbol && board[4]==symbol && board[6]==symbol){
            return true;
        }
        return false;
    }

    public String toString(){
        String res = "";
        for (int i=0; i<9; i++){
            res += "\t"+this.board[i];
            if((i+1)%3==0){
                res += "\n";
            }
        }
        return res;
    }

    public int compareTo(Object O){
        return 0;
    }
}

public class TicTacToe{
    public static void main(String[]args){
        Game newGame = new Game();
        PlayGame m = new PlayGame(newGame);
        m.startGame();
    }
}


// -------OUTPUT-------//

// 1       2       3
// 4       5       6
// 7       8       9

// You know the rules! Enter a number between 1 to 9 to play your move
// -       -       -
// -       -       -
// -       -       -


// Your move
// 1
// AI plays 5

// X       -       -
// -       O       -
// -       -       -


// Your move
// 3
// AI plays 2

// X       O       X
// -       O       -
// -       -       -


// Your move
// 8
// AI plays 4

// X       O       X
// O       O       -
// -       X       -


// Your move
// 6
// AI plays 9

// X       O       X
// O       O       X
// -       X       O


// Your move
// 7
// X       O       X
// O       O       X
// X       X       O



// *******Draw*******