package com.example.curewell.entities;

import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;
import java.util.Objects;

public class RespVente extends Employee{

    protected List<Transaction> transactions;

    public RespVente(int id, String firstName, String lastName, String phone, String address, String password, String email, List<Company> companies, List<Transaction> transactions) {
        super(id, firstName, lastName, phone, address, password, email, companies);
        this.transactions = transactions;
    }

    public void addTransaction(Transaction transaction) {
        transactions.add(transaction);
    }

    // only possible for admin?
    public void deleteCompany(Transaction transaction) {
        transactions.removeIf(comp -> Objects.equals(comp, transaction));
    }

    public boolean validateTransaction(@NotNull Transaction transaction) {
        boolean canValidate = true;

        for (Map.Entry<Medicine, Integer> me : transaction.getMedicineQuantityMap().entrySet()) {
            // checking each medicine in the order before changing anything
            canValidate = canValidate && me.getKey().canValidate(me.getValue());
        }

        if (!canValidate) return false;

        // modifying storage
        for (Map.Entry<Medicine, Integer> me : transaction.getMedicineQuantityMap().entrySet()) {
            me.getKey().decreaseQuantity(me.getValue());
        }

        // setting the transaction.status to validated
        for (Transaction transac : transactions) {
            if (Objects.equals(transaction, transac)) transac.setStatus(StatusTransaction.Validated);
        }

        return true;
    }

    public boolean cancelTransaction(@NotNull Transaction transaction) {
        // if the transaction is already validated it can't be cancelled anymore, so we should check first if it is not validated
        if (transaction.getStatus() != StatusTransaction.Validated){
            transaction.setStatus(StatusTransaction.Canceled);
            return true;
        }
        return false; // return false if it can't be cancelled anymore (as it has been validated already)
    }
}