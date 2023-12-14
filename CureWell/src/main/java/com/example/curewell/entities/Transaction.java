package com.example.curewell.entities;


import java.util.Date;
import java.util.Map;

public class Transaction {
    protected final int id;
    protected double total;
    protected double discount;
    protected final Date dateCreation;
    protected StatusTransaction status;
    protected final Employee employee;
    protected final Company company;
    protected Date dateValidation;
    protected Date dateCancellation;
    protected Map<Medicine, Integer> medicineQuantityMap;

    public Transaction(int id, double discount, Date dateCreation, Employee employee, Company company, Map<Medicine, Integer> medicineQuantityMap) {
        this.id = id;
        this.discount = discount;
        this.dateCreation = dateCreation;
        this.status = StatusTransaction.Normal;
        this.employee = employee;
        this.company = company;
        this.medicineQuantityMap = medicineQuantityMap;

        double total = 0;
        for (Map.Entry<Medicine, Integer> me : this.medicineQuantityMap.entrySet()) {
            total +=  me.getKey().getPrice() * me.getValue();

        }

        this.discount = discount;
        this.total = total - total * discount;

        this.dateValidation = null;
        this.dateCancellation =null;
    }

    public int getId() {
        return id;
    }
    public double getTotal() {
        return total;
    }
    public double getDiscount() {
        return discount;
    }
    public Date getDateCreation() {
        return dateCreation;
    }
    public StatusTransaction getStatus() {
        return status;
    }
    public Employee getEmployee() {
        return employee;
    }
    public Company getCompany() {
        return company;
    }
    public Date getDateValidation() {
        return dateValidation;
    }
    public Date getDateCancellation() {
        return dateCancellation;
    }
    public Map<Medicine, Integer> getMedicineQuantityMap() {
        return medicineQuantityMap;
    }

    public void setTotal(double total) {
        this.total = total;
    }
    public void setDiscount(double discount) {
        this.discount = discount;
    }
    public void setStatus(StatusTransaction status) {
        this.status = status;
    }
    public void setDateValidation(Date dateValidation) {
        this.dateValidation = dateValidation;
    }
    public void setDateCancellation(Date dateCancellation) {
        this.dateCancellation = dateCancellation;
    }
    public void setMedicineQuantityMap(Map<Medicine, Integer> medicineQuantityMap) {
        this.medicineQuantityMap = medicineQuantityMap;
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "id=" + id +
                ", total=" + total +
                ", discount=" + discount +
                ", dateCreation=" + dateCreation +
                ", status=" + status +
                ", employee=" + employee +
                ", medicineList=" + medicineQuantityMap +
                '}';
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Transaction transaction = (Transaction) obj;
        return id == transaction.id;
    }

    // checkPriority function : should be added in the TransactionController to sort the transactions depending on their level of priority
    // alert function : same
}
