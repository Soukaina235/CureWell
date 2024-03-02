package com.curewell.dao.impl;

import com.curewell.controller.Application;
import com.curewell.model.Medicine;
import com.curewell.model.StatusTransaction;
import com.curewell.model.Transaction;
import com.curewell.model.TypeTransaction;
import com.curewell.dao.TransactionDao;
import com.curewell.sql.ConnectDB;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

public class TransactionDaoImpl implements TransactionDao {
    @Override
    public int save(Transaction transaction) {
        ConnectDB connectDB = null;
        int result = 0;

        DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("EEE MMM dd HH:mm:ss z yyyy");
        LocalDateTime dateTime = LocalDateTime.parse(transaction.getDateCreation().toString(), inputFormatter);

        DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedDate = dateTime.format(outputFormatter);


        String login = Application.currentUser.getLogin();
        String pwd =  Application.currentUser.getPassword();
        String role = new EmployeeDaoImpl().getRoleByLoginAndPassword(login, pwd);

        if (Objects.equals(role, "RESP_ACHAT")) {
            transaction.setType(TypeTransaction.Achat);
        } else {
            transaction.setType(TypeTransaction.Demande);
        }



        String req1 = "INSERT INTO transaction(total,discount,datecreation,status,employee,company,type) VALUES ('" +
                transaction.getTotalString() + "','" +
                transaction.getDiscount() + "','" +
                formattedDate + "','" +
                transaction.getStatusString().toString() + "','" +
                transaction.getEmployee().getId() + "','" +
                transaction.getCompany().getId() + "','" +
                transaction.getType().toString() + "')";

        try {
            connectDB = new ConnectDB();
            PreparedStatement preparedStatement = connectDB.conn.prepareStatement(req1, Statement.RETURN_GENERATED_KEYS);

            result = preparedStatement.executeUpdate();

            ResultSet generatedKeys = preparedStatement.getGeneratedKeys();

            if (generatedKeys.next()) {
                int generatedId = generatedKeys.getInt(1);
                transaction.setId(generatedId);
            }

            savemedicineQuantityMap(transaction);
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            return result;
        }
        return result;
    }

    public void savemedicineQuantityMap(Transaction transaction) {
        for (Map.Entry<Medicine, Integer> entry : transaction.getMedicineQuantityMap().entrySet()) {
            savemedicineQuantity(entry.getKey().getId(), transaction.getId(), entry.getValue());
        }
    }


    public int savemedicineQuantity(int medicine, int transaction, int quantity) {
        ConnectDB connectDB = null;
        int result = 0;

        String req1 = "INSERT INTO transaction_medicine(id_medicine, id_transaction, quantity) VALUES (?, ?, ?)";

        try {
            connectDB = new ConnectDB();
            PreparedStatement preparedStatement = connectDB.conn.prepareStatement(req1);
            preparedStatement.setInt(1, medicine);
            preparedStatement.setInt(2, transaction);
            preparedStatement.setInt(3, quantity);

            result = preparedStatement.executeUpdate();
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            return result;
        }
        return result;
    }


    @Override
    public int update(Transaction transaction) {
        ConnectDB connectDB = null;
        int result = 0;
        String req = "UPDATE transaction " +
                "SET total = " + transaction.getTotalString() + "," +
                "discount = " + transaction.getDiscount() + "," +
                "dateCreation = '" + transaction.getDateCreation() + "'," +
                "status = '" + transaction.getStatusString().toString() + "'," +
                "employee_id = " + transaction.getEmployee().getId() + "," +
                "company_id = " + transaction.getCompany().getId() + "," +
                "dateValidation = '" + transaction.getDateValidation() + "'," +
                "dateCancellation = '" + transaction.getDateCancellation() + "'," +
                "type = '" + transaction.getType().toString() + "' " +
                "WHERE id = " + transaction.getId();

        try {
            connectDB = new ConnectDB();
            result = connectDB.getStatement().executeUpdate(req);
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return result;
    }

    public List<Transaction> findAll() {
        ConnectDB connectDB = null;
        List<Transaction> transactions = new ArrayList<>();
        String req = "SELECT * FROM transaction";
        try {
            connectDB = new ConnectDB();
            ResultSet rs = connectDB.getStatement().executeQuery(req);
            while(rs.next()) {
//                Map<Medicine, Integer> medicineQuantityMap = getMedicineQuantityMap(rs.getInt("id_transaction"));
                transactions.add(new Transaction(rs.getInt("id"),
                        rs.getDouble("total"),
                        rs.getDouble("discount"),
                        rs.getDate("dateCreation"),
                        StatusTransaction.valueOf(rs.getString("status")),
                        new EmployeeDaoImpl().findEmployeeById(rs.getInt("employee")),
                        new CompanyDaoImpl().findCompanyById(rs.getInt("company")),
                        rs.getDate("dateValidation"),
                        rs.getDate("dateCancellation"),
                        TypeTransaction.valueOf(rs.getString("type"))
//                        medicineQuantityMap
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        return transactions;
    }
    @Override
    public List<Transaction> findAllDemande() {
        return findAll().stream().filter((t) -> t.getType() == TypeTransaction.Demande).collect(Collectors.toList());
    }

    @Override
    public List<Transaction> findAllAchat() {
        return findAll().stream().filter((t) -> t.getType() == TypeTransaction.Achat).collect(Collectors.toList());
    }


    private Map<Medicine, Integer> getMedicineQuantityMap(int transactionId) {
        ConnectDB connectDB = null;
        Map<Medicine, Integer> medicineQuantityMap = new HashMap<>();
        String req = "SELECT * FROM transaction_medicine WHERE transaction_id = " + transactionId;


        try {
            connectDB = new ConnectDB();
            ResultSet rs = connectDB.getStatement().executeQuery(req);
            while (rs.next()) {
                int medicineId = rs.getInt("medicine_id");
                int quantity = rs.getInt("quantity");
                Medicine medicine = new MedicineDaoImpl().findMedicineById(medicineId);
                medicineQuantityMap.put(medicine, quantity);
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return medicineQuantityMap;
    }

    public Map<Medicine, Integer> findMedicineMapByTransactionID(int id_transaction) {
        ConnectDB connectDB = null;
        Map<Medicine, Integer> map = new HashMap<>();
        String req = "SELECT * FROM transaction_medicine WHERE id_transaction=" + id_transaction;

        try {
            connectDB = new ConnectDB();
            ResultSet rs = connectDB.getStatement().executeQuery(req);
            while (rs.next()) {
                map.put(new MedicineDaoImpl().findMedicineById(rs.getInt("id_medicine")), rs.getInt("quantity"));

            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        return map;
    }

    public Transaction findTransactionByID(int id) {
        ConnectDB connectDB = null;
        Transaction transaction = null;
        String req = "SELECT * FROM transaction WHERE id=" + id;
        try {
            connectDB = new ConnectDB();
            ResultSet rs = connectDB.getStatement().executeQuery(req);
            if (rs.next()) {
                transaction = new Transaction(rs.getInt("id"),
                        rs.getDouble("total"),
                        rs.getDouble("discount"),
                        rs.getDate("dateCreation"),
                        StatusTransaction.valueOf(rs.getString("status")),
                        new EmployeeDaoImpl().findEmployeeById(rs.getInt("employee")),
                        new CompanyDaoImpl().findCompanyById(rs.getInt("company")),
                        rs.getDate("dateValidation"),
                        rs.getDate("dateCancellation"),
                        TypeTransaction.valueOf(rs.getString("type"))
                );

                transaction.setMedicineQuantityMap(findMedicineMapByTransactionID(id));

            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        return transaction;
    }
    public int validerTypeTransaction(int typeTransactionId) {
        ConnectDB connectDB = null;
        String updateQuery = "UPDATE transaction SET status = 'Validated', datevalidation = ? WHERE id = ?";
        int rowsAffected = 0;

        try {
            connectDB = new ConnectDB();
            PreparedStatement preparedStatement = connectDB.conn.prepareStatement(updateQuery);

            java.util.Date currentDate = new java.util.Date();
            java.sql.Timestamp timestamp = new java.sql.Timestamp(currentDate.getTime());

            preparedStatement.setTimestamp(1, timestamp);
            preparedStatement.setInt(2, typeTransactionId);

            rowsAffected = preparedStatement.executeUpdate();

            new MedicineDaoImpl().updateStockMedicines(findTransactionByID(typeTransactionId));

        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        return rowsAffected;
    }

    public int cancelTransaction(int transactionId) {
        ConnectDB connectDB = null;
        String updateQuery = "UPDATE transaction SET status = 'Canceled', datecancellation = ? WHERE id = ?";
        int rowsAffected = 0;

        try {
            connectDB = new ConnectDB();
            PreparedStatement preparedStatement = connectDB.conn.prepareStatement(updateQuery);

            java.util.Date currentDate = new java.util.Date();
            java.sql.Timestamp timestamp = new java.sql.Timestamp(currentDate.getTime());

            preparedStatement.setTimestamp(1, timestamp);
            preparedStatement.setInt(2, transactionId);

            rowsAffected = preparedStatement.executeUpdate();

        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        return rowsAffected;
    }

    @Override
    public String getStatusTransaction(Transaction transaction){
        ConnectDB connectDB = null;
        String status= null;
        String req = "SELECT status FROM transaction WHERE id=?";
        try {
            connectDB = new ConnectDB();
            PreparedStatement preparedStatement = connectDB.conn.prepareStatement(req);
            preparedStatement.setInt(1,transaction.getId());
            ResultSet rs = preparedStatement.executeQuery();
            if(rs.next()){
                status = rs.getString("status");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        return status;
    }
}