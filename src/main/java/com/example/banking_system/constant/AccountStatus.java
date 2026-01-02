package com.example.banking_system.constant;


public enum AccountStatus {

    ACTIVE{
        @Override
        public boolean canLogin() {
            return true;
        }

        @Override
        public boolean canTakeOutLoan() {
            return true;
        }

        @Override
        public boolean canPerformTransactions() {
            return true;
        }

        @Override
        public boolean canDeposit() {
            return true;
        }

        @Override
        public boolean canWithdraw() {
            return true;
        }
    },

    // when have a bad loan or overdue payment
    DISABLED{
        @Override
        public boolean canLogin() {
            return true;
        }

        @Override
        public boolean canTakeOutLoan() {
            return false;
        }

        @Override
        public boolean canPerformTransactions() {
            return false;
        }

        //can deposit to pay off loan
        @Override
        public boolean canDeposit() {
            return true;
        }

        @Override
        public boolean canWithdraw() {
            return false;
        }
    },

    // when suspicious activity detected
    BLOCKED{
        @Override
        public boolean canLogin() {
            return false;
        }

        @Override
        public boolean canTakeOutLoan() {
            return false;
        }

        @Override
        public boolean canPerformTransactions() {
            return false;
        }

        @Override
        public boolean canDeposit() {
            return false;
        }

        @Override
        public boolean canWithdraw() {
            return false;
        }
    };

    public abstract boolean canLogin();
    public abstract boolean canTakeOutLoan();
    public abstract boolean canPerformTransactions();
    public abstract boolean canDeposit();
    public abstract boolean canWithdraw();
}
