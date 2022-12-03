package com.perfect.bank.classes;

import java.sql.*;

import com.perfect.bank.exceptions.NoAccountException;
import com.perfect.bank.exceptions.NotEnoughMoneyException;

public class PLSQLInterface extends PLSQLSecret {

    Connection connexion;

    public PLSQLInterface() throws Exception {
        setupDBConnection();
    }

    public boolean isClientInDB(int clientUID) throws SQLException {
        Statement stmtExecuteQuery = connexion.createStatement();
        ResultSet rsExecuteQuery = stmtExecuteQuery.executeQuery("SELECT * FROM client where id = " + clientUID);

        int size = 0;
        if (rsExecuteQuery != null) {
            rsExecuteQuery.last();
            size = rsExecuteQuery.getRow();
        }

        rsExecuteQuery.close();
        stmtExecuteQuery.close();
        return size > 0;
    }

    public int createClient() throws SQLException {
        Statement stmtExecuteQuery = connexion.createStatement();
        ResultSet rsExecuteQuery = stmtExecuteQuery.executeQuery(
                "insert into client values (nextval('client_id_seq'),0);select currval('client_id_seq');");

        int clientUID = -1;
        if (rsExecuteQuery.next()) {
            clientUID = rsExecuteQuery.getInt(0);
        }

        rsExecuteQuery.close();
        stmtExecuteQuery.close();
        return clientUID;
    }

    public void deposit(int clientUID, double amount) throws SQLException, NoAccountException {
        Statement stmtExecuteUpdate = connexion.createStatement();
        int resultExecuteUpdate = stmtExecuteUpdate
                .executeUpdate("update client set balance = (select balance+" + amount + " where id = " + clientUID
                        + ") where id = " + clientUID + ";");
        if (resultExecuteUpdate == 0) {
            throw new NoAccountException();
        }
        stmtExecuteUpdate.close();
    }

    public void withdraw(int clientUID, double amount)
            throws SQLException, NotEnoughMoneyException, NoAccountException {

        boolean needChecks = false;

        if (getBalance(clientUID) >= amount) {
            Statement stmtExecuteUpdate = connexion.createStatement();
            int resultExecuteUpdate = stmtExecuteUpdate
                    .executeUpdate("update client set balance = (select balance-" + amount + " where id = " + clientUID
                            + ") where id = " + clientUID + ";");
            needChecks = resultExecuteUpdate == 0;
            stmtExecuteUpdate.close();
        }

        if (needChecks) {
            if (isClientInDB(clientUID)) {
                throw new NotEnoughMoneyException();
            } else {
                throw new NoAccountException();
            }
        }

    }

    public double getBalance(int clientUID) throws SQLException, NoAccountException {
        boolean noAccount = true;
        double balance = 0.0;

        Statement stmtExecuteQuery = connexion.createStatement();
        ResultSet rsExecuteQuery = stmtExecuteQuery.executeQuery("select balance from client where id = " + clientUID);
        while (rsExecuteQuery.next()) {
            balance = rsExecuteQuery.getLong(1);
            noAccount = false;
        }
        rsExecuteQuery.close();
        stmtExecuteQuery.close();

        if (noAccount) {
            throw new NoAccountException();
        }

        return balance;
    }

    private void setupDBConnection() throws SQLException, ClassNotFoundException {
        Class.forName("org.postgresql.Driver");
        connexion = DriverManager.getConnection(URL, LOGIN, MDP);
    }

    public void closeDBConnection() throws Exception {
        connexion.close();
    }

}
