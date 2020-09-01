package ngame;

import java.util.*;

public class Main {


    public static void main(String[] args) {

        Main main = new Main();
        main.start();
    }


    public void start(){
        Board board = new Board(3);
//        board.shuffle(100);

        System.out.println("ngame.Board score: " + board.getScore());
        board.print();

        System.out.println("---------------");

        Board result;
        Queue<Board> priorityQueue = new PriorityQueue<>(Comparator.comparing(Board::getScore));
        Queue<Board> linkedList = new LinkedList<>();

        try {
            result = solve(board, priorityQueue);
            System.out.println("ngame.Board score: " + result.getScore());
            List<Move> solutionPath = result.getSolutionPath();
            System.out.println("Moves needed to solve: " + solutionPath.size());
            result.print();
//            System.out.println(solutionPath);


        } catch (Exception e) {
            System.out.println("no solution can be found");
        }

    }


    private Board solve(Board board, Queue<Board> queue) throws Exception {

        Set<Board> discoveredBoards = new HashSet<>();
        queue.add(board);
        discoveredBoards.add(board);
        int steps = 0;

        while (!queue.isEmpty()){
            steps++;
            Board poll = queue.poll();
            Set<Move> possibleMoves = poll.getPossibleMoves();
            if (poll.isCorrect()){
                System.out.println("Traversed nodes to solve: " + steps);
                return poll;
            }

            for (Move possibleMove : possibleMoves) {

                Board copy = poll.copy();
                copy.swap(possibleMove);
                if (discoveredBoards.contains(copy)) continue;
                possibleMove.setLastMove(poll.getLastMove());
                copy.setLastMove(possibleMove);
                queue.add(copy);
                discoveredBoards.add(copy);
            }
        }

        throw new Exception("No solution found");
    }



}
