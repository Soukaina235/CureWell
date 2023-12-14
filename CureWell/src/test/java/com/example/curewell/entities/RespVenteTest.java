package com.example.curewell.entities;

import org.junit.Test;
import org.junit.Before;

import java.util.*;

import static org.junit.Assert.*;

public class RespVenteTest {
    private RespVente respVente;
    private Map<Medicine, Integer> medicineQuantityMap;
    private static List<Medicine> medicines;


    @Before
    public void before() {
        List<Company> companies = new ArrayList<>();
        companies.add(new Company(1, "comp1", "0609080706", "adr1"));
        companies.add(new Company(2, "comp2", "06123456", "adr2"));

        medicines = new ArrayList<>();
        medicines.add(new Medicine(1, "med1", Category.Cat1, 89.5, 12));
        medicines.add(new Medicine(2, "med2", Category.Cat2, 163.89, 50));
        medicines.add(new Medicine(3, "med3", Category.Cat2, 150, 20));

        medicineQuantityMap = new HashMap<>();
        medicineQuantityMap.put(medicines.get(0), 5);
        medicineQuantityMap.put(medicines.get(1), 10);

        List<Transaction> transactions = new ArrayList<>();
        transactions.add(new Transaction(1, 0.05, new Date(), respVente, null, medicineQuantityMap));
//        transactions.add(new Transaction(1, 0.05, new Date(), respVente, medicineQuantityMap));


        respVente = new RespVente(987, "respV1", "respVENTE1",
                "0601020304", "adr1", "pass1",
                "respVente1@gmail.com", companies, transactions
        );
    }

    @Test
    public void testGettersConstructor() {
        assertEquals(987, respVente.getId());
        assertEquals("respV1", respVente.getFirstName());
        assertEquals("respVENTE1", respVente.getLastName());
        assertEquals("0601020304", respVente.getPhone());
        assertEquals("adr1", respVente.getAddress());
        assertEquals("pass1", respVente.getPassword());
        assertEquals("respVente1@gmail.com", respVente.getEmail());
    }

    @Test
    public void testGetCompaniesConstructor() {
        assertEquals(new Company(1, "comp1", "0609080706", "adr1"), respVente.getCompanies().get(0));
        assertEquals(new Company(2, "comp2", "06123456", "adr2"), respVente.getCompanies().get(1));
    }

    @Test
    public void testSetters() {
        respVente.setPhone("0609080706");
        assertEquals("0609080706", respVente.getPhone());

        respVente.setAddress("adr1 New");
        assertEquals("adr1 New", respVente.getAddress());

        respVente.setPassword("pass1 New");
        assertEquals("pass1 New", respVente.getPassword());

        respVente.setEmail("respVente1NEW@gmail.com");
        assertEquals("respVente1NEW@gmail.com", respVente.getEmail());
    }

    @Test
    public void testValidateTransactionTrue() { //! The transaction is validated
        Transaction transaction = new Transaction(1, 0.05, new Date(), respVente, null, medicineQuantityMap);

        assertTrue(respVente.validateTransaction(transaction)); // to check if the validateTransaction function has worked fine and returned true => the transaction was done

        for (Transaction transac : respVente.transactions) {
            if (Objects.equals(transaction, transac)) {
                assertEquals(StatusTransaction.Validated, transac.getStatus()); // to check if the status has changed to Validated
                break;
            }
        }

        // to check if the quantity of each medicine in the transaction have changed
        assertEquals(12 - 5, medicines.get(0).getQuantity());
        assertEquals(50 - 10, medicines.get(1).getQuantity());

        // to check if the quantity of other medicines that don't exist in transaction haven't changed
        assertEquals(20, medicines.get(2).getQuantity());

    }

    @Test
    public void testValidateTransactionFalse() { //! The transaction cannot be validated
        for (Map.Entry<Medicine, Integer> me : this.medicineQuantityMap.entrySet()) {
            if (Objects.equals(me.getKey(), medicines.get(0))) {
                me.setValue(60);  // change the value to 60 : 60 > 50(amount of medicines.get(1) in stock) => the transaction will not be validated
            }
        }

        Transaction transaction = new Transaction(1, 0.05, new Date(), respVente, null, medicineQuantityMap);
        assertFalse(respVente.validateTransaction(transaction));

        for (Transaction transac : respVente.transactions) {
            if (Objects.equals(transaction, transac)) {
                assertEquals(StatusTransaction.Normal, transac.getStatus());
                break;
            }
        }

        assertEquals(12, medicines.get(0).getQuantity());
        assertEquals(50, medicines.get(1).getQuantity());
        assertEquals(20, medicines.get(2).getQuantity());
    }

}