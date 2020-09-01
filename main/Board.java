package ngame;

import java.util.*;

public class Board {

    private int[][] tiles;
    private Move emptySpace;
    private Move lastMove;

    private Board(){
    }

    public Board(int size){

        tiles = new int[size][size];
        init();
    }


    public Board(int[][] tiles){
        this.tiles = tiles;
        findEmptySpace();
    }


    private void init(){
        int count = 1;
        for (int i = 0; i < tiles.length; i++) {
            for (int j = 0; j < tiles.length; j++) {
                tiles[i][j] = count++;
            }
        }
        emptySpace = new Move(tiles.length - 1, tiles.length - 1, 0, 0);
        tiles[emptySpace.getY()][emptySpace.getX()] = 0;
    }

    public void shuffle(int iterations){
        for (int i = 0; i < iterations; i++) {
            Set<Move> possibleMoves = getPossibleMoves();
            Move[] moves = possibleMoves.toArray(new Move[0]);
            swap(moves[(int) (Math.random() * moves.length)]);
        }
    }


    public void findEmptySpace(){
        for (int i = 0; i < tiles.length; i++) {
            for (int j = 0; j < tiles.length; j++) {
                if (tiles[i][j] == 0){
                    emptySpace = new Move(j, i, 0, 0);
                    return;
                }
            }
        }
    }


    public boolean isCorrect(){
        int count = 1;
        for (int[] row : tiles) {
            for (int j = 0; j < tiles.length; j++) {
                if (row[j] != count++ % (tiles.length * tiles.length)) {
                    return false;
                }
            }
        }

        return true;
    }


    public float getScore(){
        float score = 0;

        for (int i = 0; i < tiles.length; i++) {
            for (int j = 0; j < tiles.length; j++) {
                int lastNumber = (int) (Math.pow(tiles.length, 2) - 1);
                int number = tiles[i][j] == 0 ? lastNumber : tiles[i][j] - 1;
                int correctX = number % tiles.length;
                int correctY = number / tiles.length;

                score += Math.pow(correctX - j, 2) + Math.pow(correctY - i, 2);
            }
        }

        return score + getSolutionPath().size();
    }




    public void swap(Move move){
        tiles[emptySpace.getY()][emptySpace.getX()] = tiles[move.getY()][move.getX()];
        tiles[move.getY()][move.getX()] = 0;
        emptySpace = move;
    }


    public Board copy(){
        int[][] nboard = new int[tiles.length][tiles.length];

        for (int i = 0; i < tiles.length; i++) {
            System.arraycopy(tiles[i], 0, nboard[i], 0, tiles.length);
        }
        Board board = new Board();
        board.tiles = nboard;
        board.emptySpace = new Move(emptySpace.getX(), emptySpace.getY(), 0, 0);
        return board;
    }


    public Set<Move> getPossibleMoves(){

        Set<Move> moves = new HashSet<>();

        int i = emptySpace.getY(), j = emptySpace.getX();

        if (i - 1 >= 0) moves.add(new Move(j, i-1, 0, -1));
        if (i + 1 <= tiles.length-1) moves.add(new Move(j, i+1, 0, 1));
        if (j - 1 >= 0) moves.add(new Move(j - 1, i, -1, 0));
        if (j + 1 <= tiles.length-1) moves.add(new Move(j + 1, i, -1, 0));

        return moves;
    }



    public void print(){

        for (int[] row : tiles) {
            for (int j = 0; j < tiles.length; j++) {
                System.out.print(row[j]);
                System.out.print(" ");
            }
            System.out.println();
        }

    }

    public Move getLastMove() {
        return lastMove;
    }

    public void setLastMove(Move lastMove) {
        this.lastMove = lastMove;
    }


    public List<Move> getSolutionPath(){
        LinkedList<Move> moveList = new LinkedList<>();
        Move move = getLastMove();

        while (move != null){
            moveList.addFirst(move);
            move = move.getLastMove();
        }

        return moveList;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Board board = (Board) o;
        return Arrays.deepEquals(tiles, board.tiles);
    }

    @Override
    public int hashCode() {
        return Arrays.deepHashCode(tiles);
    }
}
