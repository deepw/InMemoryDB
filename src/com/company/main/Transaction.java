package com.company.main;

import javafx.util.Pair;

import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

/**
 * Created by deepakwadhwani on 7/7/17.
 */
public class Transaction {
    // Should not be public and provide all operations methods instead
    public Stack<String> rollbackCommands;

    public Transaction() {
        this.rollbackCommands = new Stack<>();
    }

    public Stack<String> getRollbackCommands() {
        return rollbackCommands;
    }
}
