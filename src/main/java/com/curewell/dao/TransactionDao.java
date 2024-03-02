package com.curewell.dao;

import com.curewell.model.Transaction;

import java.util.List;

public interface TransactionDao {
    int save(Transaction transaction);
    int update(Transaction transaction);
    List<Transaction> findAll();
    List<Transaction> findAllDemande();
    List<Transaction> findAllAchat();
    Transaction findTransactionByID(int id);
    String getStatusTransaction(Transaction transaction);
    public int getCountTotal();
    public int getCountValidated();
    public int getCountCancelled();
    public int getCountNormal();

}
