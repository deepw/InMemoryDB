package com.company.main;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created by deepakwadhwani on 7/7/17.
 */
public class Database {
    Map<String, String> mainDatabase;
    Map<String, Set<String>> countDB;

    public Database() {
        mainDatabase = new HashMap<String, String>();
        countDB = new HashMap<String, Set<String>>();
    }

    public void set(String key, String value, Transaction transaction) {
        saveAddToTransaction(transaction, key);
        handleAdd(key, value);
        mainDatabase.put(key, value);
    }

    public String get(String key) {
        return mainDatabase.get(key);
    }

    public void delete(String key, Transaction transaction) {
        if (!mainDatabase.containsKey(key)) {
            return;
        }
        handleDelete(key, mainDatabase.get(key));
        saveDeleteToTransaction(transaction, key);
        mainDatabase.remove(key);
    }

    public int count(String value) {
        if (!countDB.containsKey(value)) {
            return 0;
        } else {
            return countDB.get(value).size();
        }
    }

    private void handleAdd(String key, String value) {
        if (!countDB.containsKey(value)) {
            Set<String> keySet = new HashSet<String>();
            keySet.add(key);
            countDB.put(value, keySet);
        } else {
            if (countDB.containsKey(mainDatabase.get(key))) {
                countDB.get(mainDatabase.get(key)).remove(key);
            }
            countDB.get(value).add(key);
        }
    }

    private void handleDelete(String key, String value) {
        if (countDB.containsKey(value)) {
            countDB.get(value).remove(key);
        }
    }

    private void saveAddToTransaction(Transaction transaction, String key) {
        if (transaction == null) {
            return;
        }
        StringBuilder stringBuilder = new StringBuilder();
        if (!mainDatabase.containsKey(key)) {
            stringBuilder.append("DELETE ");
            stringBuilder.append(key);
        } else {
            stringBuilder.append("SET ");
            stringBuilder.append(key);
            stringBuilder.append(" ");
            stringBuilder.append(mainDatabase.get(key));
        }
        transaction.getRollbackCommands().push(stringBuilder.toString());
    }

    // Can be merged with add in the same method
    private void saveDeleteToTransaction(Transaction transaction, String key) {
        if (transaction == null) {
            return;
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("SET ");
        stringBuilder.append(key);
        stringBuilder.append(" ");
        stringBuilder.append(mainDatabase.get(key));
        transaction.getRollbackCommands().push(stringBuilder.toString());
    }
}
