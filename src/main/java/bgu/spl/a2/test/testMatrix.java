package bgu.spl.a2.test;

import bgu.spl.a2.WorkStealingThreadPool;
import bgu.spl.a2.sim.Simulator;

import java.util.Random;
import java.util.StringJoiner;


public class testMatrix {
    public static void main(String args[]) throws InterruptedException {

         String[] toppings = {"Cheese", "Pepperoni", "Black Olives"};

        Simulator.main(toppings);
        Simulator.start();
    }
}
