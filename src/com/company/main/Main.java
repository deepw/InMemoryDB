package com.company.main;

import java.util.Scanner;
import java.util.Stack;
import java.util.StringTokenizer;

public class Main {
    private static Database mainDB;
    private static Stack<Transaction> pendingTransactions;

    /**
     * Method to print the help for the user
     */
    private static void printHelp() {
        System.out.println("Usage: [SET|GET|DELETE|BEGIN|COMMIT|ROLLBACK] [parameters..]");
    }

    static boolean handleCommand(String command) {
        StringTokenizer tokenizer = new StringTokenizer(command);
        String directive = tokenizer.nextToken().toUpperCase();
        switch (directive) {
            case "SET": {
                if (tokenizer.countTokens() != 2) {
                    printHelp();
                    return false;
                }
                String key = tokenizer.nextToken();
                String value = tokenizer.nextToken();
                mainDB.set(key, value, pendingTransactions.empty() ? null : pendingTransactions.peek());
                break;
            }
            case "GET": {
                if (tokenizer.countTokens() != 1) {
                    printHelp();
                    return false;
                }
                String key = tokenizer.nextToken();
                System.out.println(mainDB.get(key));
                break;
            }
            case "DELETE": {
                if (tokenizer.countTokens() != 1) {
                    printHelp();
                    return false;
                }
                String key = tokenizer.nextToken();
                mainDB.delete(key, pendingTransactions.empty() ? null : pendingTransactions.peek());
                break;
            }
            case "COUNT": {
                if (tokenizer.countTokens() != 1) {
                    printHelp();
                    return false;
                }
                String value = tokenizer.nextToken();
                System.out.println(mainDB.count(value));
                break;
            }
            case "END": {
                return true;
            }
            case "BEGIN": {
                pendingTransactions.push(new Transaction());
                break;
            }
            case "COMMIT": {
                int size = pendingTransactions.size();
                for (int i = 0; i < size; i++) {
                    // Pop this transaction
                    pendingTransactions.pop();
                }
                break;
            }
            case "ROLLBACK": {
                if (pendingTransactions.empty()) {
                    System.out.println("NO TRANSACTION");
                    break;
                }
                // Get the latest pending transaction
                Transaction transaction = pendingTransactions.pop();
                // Pop all the commands from this transaction and execute them.
                while (!transaction.getRollbackCommands().empty()) {
                    handleCommand(transaction.getRollbackCommands().pop());
                }
                break;
            }
            default: {
                System.out.println("Invalid input.");
                printHelp();
            }
        }
        return false;
    }

    public static void main(String[] args) {
        mainDB = new Database();
        pendingTransactions = new Stack<Transaction>();
        // Run cmd loop till user quits
        boolean quit = false;
        while (!quit) {
            Scanner scanner = new Scanner(System.in);
            String command = scanner.nextLine();
            // ignore empty input
            if (command.length() > 0) {
                quit = handleCommand(command);
            }
        }
    }
}
