package ex09;

import java.util.ArrayList;

// A CLASS is a blueprint; an OBJECT is one instance built from it with 'new'.
//   BankAccount alice = new BankAccount("Alice", 10000);
//
// This file has NO main method. BankApp.java uses this class and runs the tests.
public class BankAccount {

    // ---------------------------------------------------------------
    // FIELDS: the data every account object carries.
    // 'private' = only code INSIDE this class can touch it (encapsulation).
    // 'final'   = assigned once (in the constructor), then never changed.
    // 'static'  = ONE copy shared by the whole class, not one per object.
    // ---------------------------------------------------------------

    // 1: declare two private static int fields:
    //   nextAccountNumber, starting at 1001 (the number the NEXT new account will receive)
    //   accountsCreated, starting at 0
    private static int nextAccountNumber = 1001;
    private static int accountsCreated = 0;

    // 2: declare the instance fields:
    //   private final int accountNumber;
    //   private final String ownerName;
    //   private long balancePence;                              (NOT final: it changes)
    //   private final ArrayList<String> history = new ArrayList<>();
    private final int accountNumber;
    private final String ownerName;
    private long balancePence;
    private final ArrayList<String> history = new ArrayList<>();


    // ---------------------------------------------------------------
    // CONSTRUCTORS: special methods that set up a new object.
    // Same name as the class, and NO return type (not even void).
    // ---------------------------------------------------------------

    /**
     * Opens an account with an opening balance.
     * - If openingPence < 0, throw new IllegalArgumentException("Opening balance cannot be negative")
     *   BEFORE changing anything (a rejected account must NOT use up an account number).
     * - Assign the next account number, then advance the counter; add one to accountsCreated.
     * - Record "OPEN <openingPence>" in the history, e.g. "OPEN 10000".
     * Use 'this.ownerName = ownerName;' where the parameter has the same name as the field.
     */
    public BankAccount(String ownerName, long openingPence) {
        // 3
        if (openingPence < 0){
            throw new IllegalArgumentException("Opening balance cannot be negative");
        }
        this.balancePence = openingPence;
        this.accountNumber = nextAccountNumber;
        accountsCreated++;
        nextAccountNumber++;
        this.history.add("OPEN " + openingPence);
        this.ownerName = ownerName;
    }

    /** Opens an account with a zero balance. Must be ONE line that calls the other constructor: this(ownerName, 0); */
    public BankAccount(String ownerName) {
        //  4
        this(ownerName, 0);
    }

    // ---------------------------------------------------------------
    // GETTERS: read-only access to private fields. There's deliberately
    // NO setBalance(), so the balance can only change through deposit/withdraw/transfer.
    // ---------------------------------------------------------------

    public int getAccountNumber() {
        return this.accountNumber; // 5
    }

    public String getOwnerName() {
        return this.ownerName; // 5
    }

    public long getBalancePence() {
        return this.balancePence; // 5
    }

    /**
     * Adds money. If pence <= 0, throw new IllegalArgumentException("Deposit must be positive").
     * Records "DEPOSIT <pence>".
     */
    public void deposit(long pence) {
        // 6
        if (pence <= 0){
            throw new IllegalArgumentException("Deposit must be positive");
        }
        this.balancePence += pence;
        this.history.add("DEPOSIT " + pence);
    }

    /**
     * Takes money out. If pence <= 0, throw new IllegalArgumentException("Withdrawal must be positive").
     * If there isn't enough money: record "DECLINED <pence>" and return false (the balance doesn't change).
     * Otherwise: subtract, record "WITHDRAW <pence>", return true.
     */
    public boolean withdraw(long pence) {
        // 7
        if (pence <= 0){
            throw new IllegalArgumentException("Withdrawal must be positive");
        }
        if (this.balancePence < pence){
            this.history.add("DECLINED " + pence);
            return false;
        }
        this.balancePence -= pence;
        this.history.add("WITHDRAW " + pence);
        return true;
    }

    /**
     * Moves money from THIS account into 'other'.
     * Return false (and change nothing) if: other is null, other is this same account (use ==),
     * pence <= 0, or pence > balance.
     * On success, record "TRANSFER_OUT <pence> to #<other number>" here
     * and "TRANSFER_IN <pence> from #<this number>" on the other account, then return true.
     *
     * Surprise: you CAN read and write other.balancePence directly, even though it's private.
     * In Java, 'private' means private to the CLASS, not to the object.
     */
    public boolean transferTo(BankAccount other, long pence) {
        // 8
        if (other == null || other == this || pence <= 0 || pence > this.balancePence){
            return false;
        }
        this.balancePence -= pence;
        other.balancePence += pence;
        this.history.add("TRANSFER_OUT " + pence + " to #" + other.accountNumber);
        other.history.add("TRANSFER_IN " + pence + " from #" + this.accountNumber);
        return true;
    }

    /**
     * Returns a COPY of the history, so callers can't tamper with the real one.
     * (Returning the field itself would let anyone call getHistory().add("fake entry").)
     */
    public ArrayList<String> getHistory() {
        // 9: return new ArrayList<>(history);
        return new ArrayList<>(history);
    }

    /** A static method: called on the CLASS, as in BankAccount.getAccountsCreated(), not on an object. */
    public static int getAccountsCreated() {
        return accountsCreated; // 10
    }

    /**
     * Called automatically when an object is printed or joined to a String.
     * Format: "Account #1001 [Alice] balance £86.00"
     * Without this, printing an account gives something like "ex09.BankAccount@6d06d69c".
     */
    @Override
    public String toString() {
        return String.format("Account #%d [%s] balance £%.2f", this.accountNumber, this.ownerName,
                (double) this.balancePence / 100); //  11 (the @Override annotation is explained in the next batch)
    }
}
