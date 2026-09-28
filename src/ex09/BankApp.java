package ex09;

import java.util.List;

// Test runner for BankAccount: no need to edit. Run THIS file (it has the main method).
// Read through it too: it shows how objects are created with "new" and used with the dot operator.
public class BankApp {
    public static void main(String[] args) {
        BankAccount alice = new BankAccount("Alice", 10000);
        BankAccount bob = new BankAccount("Bob");
        check("alice number", alice.getAccountNumber(), 1001);
        check("bob number", bob.getAccountNumber(), 1002);
        check("bob opening balance", bob.getBalancePence(), 0);

        alice.deposit(2550);
        check("alice after deposit", alice.getBalancePence(), 12550);
        check("alice withdraw too much", alice.withdraw(20000), false);
        check("alice balance unchanged", alice.getBalancePence(), 12550);
        check("transfer alice -> bob", alice.transferTo(bob, 4000), true);
        check("alice after transfer", alice.getBalancePence(), 8550);
        check("bob after transfer", bob.getBalancePence(), 4000);
        check("bob withdraw 1500", bob.withdraw(1500), true);
        check("bob after withdraw", bob.getBalancePence(), 2500);
        check("transfer to self", alice.transferTo(alice, 100), false);
        check("transfer more than balance", bob.transferTo(alice, 999999), false);

        try {
            alice.deposit(-5);
            System.out.println("FAIL negative deposit should throw");
        } catch (IllegalArgumentException e) {
            System.out.println("PASS negative deposit rejected: " + e.getMessage());
        }
        try {
            new BankAccount("Mallory", -100);
            System.out.println("FAIL negative opening balance should throw");
        } catch (IllegalArgumentException e) {
            System.out.println("PASS negative opening balance rejected: " + e.getMessage());
        }

        BankAccount alias = alice;
        alias.deposit(50);
        check("alias deposit changes alice", alice.getBalancePence(), 8600);

        List<String> history = alice.getHistory();
        history.add("HACKED 1000000");
        check("alice history", alice.getHistory(), List.of("OPEN 10000", "DEPOSIT 2550", "DECLINED 20000", "TRANSFER_OUT 4000 to #1002", "DEPOSIT 50"));
        check("bob history", bob.getHistory(), List.of("OPEN 0", "TRANSFER_IN 4000 from #1001", "WITHDRAW 1500"));
        check("accounts created", BankAccount.getAccountsCreated(), 2);
        check("alice toString", alice.toString(), "Account #1001 [Alice] balance £86.00");
        check("bob toString", bob.toString(), "Account #1002 [Bob] balance £25.00");
        System.out.println("Printing an object calls toString(): " + bob);
    }

    static void check(String label, long actual, long expected) {
        System.out.println((actual == expected ? "PASS " : "FAIL ") + label + " -> got " + actual + ", expected " + expected);
    }

    static void check(String label, boolean actual, boolean expected) {
        System.out.println((actual == expected ? "PASS " : "FAIL ") + label + " -> got " + actual + ", expected " + expected);
    }

    static void check(String label, Object actual, Object expected) {
        System.out.println((expected.equals(actual) ? "PASS " : "FAIL ") + label + " -> got " + actual + ", expected " + expected);
    }
}
