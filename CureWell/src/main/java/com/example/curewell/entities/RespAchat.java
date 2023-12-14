package com.example.curewell.entities;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class RespAchat extends Employee{

    protected List<Transaction> transactions;

    public RespAchat(int id, String firstName, String lastName, String phone, String address, String password, String email, List<Company> companies) {
        super(id, firstName, lastName, phone, address, password, email, companies);
        transactions = new ArrayList<>();
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }

    public void addTransaction(Transaction transaction) {
        transactions.add(transaction);
    }

    public void deleteCompany(Transaction transaction) {
        transactions.removeIf(comp -> Objects.equals(comp, transaction));
    }

    public boolean validateTransaction(@NotNull Transaction transaction) {
        // modifying storage
        for (Map.Entry<Medicine, Integer> mapEntry : transaction.getMedicineQuantityMap().entrySet()) {
            // before this line, we should check if the medicine exists already in the database or not (not done for now)
            mapEntry.getKey().increaseQuantity(mapEntry.getValue()); // supposing that we always have enough storage
                                                                    // which means i don't consider that there is a maximum storage capacity for each medicine
        }

        // setting the transaction.status to validated
        for (Transaction transac : transactions) {
            if (Objects.equals(transaction, transac)) transac.setStatus(StatusTransaction.Validated);
        }

        return true;
    }
}
