package com.example.demo.dto;

public class TransactionDto {

	private String accountNo;
	private String date;
	private String transactionDetails;
	private String chequeNo;
	private String valueDate;
	private String withdrawalAmount;
	private String depositAmount;
	private String balance;

	public String getAccountNo() {
		return accountNo;
	}

	public void setAccountNo(String accountNo) {
		this.accountNo = accountNo;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getTransactionDetails() {
		return transactionDetails;
	}

	public void setTransactionDetails(String transactionDetails) {
		this.transactionDetails = transactionDetails;
	}

	public String getChequeNo() {
		return chequeNo;
	}

	public void setChequeNo(String chequeNo) {
		this.chequeNo = chequeNo;
	}

	public String getValueDate() {
		return valueDate;
	}

	public void setValueDate(String valueDate) {
		this.valueDate = valueDate;
	}

	public String getWithdrawalAmount() {
		return withdrawalAmount;
	}

	public void setWithdrawalAmount(String withdrawalAmount) {
		this.withdrawalAmount = withdrawalAmount;
	}

	public String getDepositAmount() {
		return depositAmount;
	}

	public void setDepositAmount(String depositAmount) {
		this.depositAmount = depositAmount;
	}

	public String getBalance() {
		return balance;
	}

	public void setBalance(String balance) {
		this.balance = balance;
	}

	@Override
	public String toString() {
		return "TransactionDto [accountNo=" + accountNo + ", date=" + date + ", transactionDetails="
				+ transactionDetails + ", ChequeNo=" + chequeNo + ", valueDate=" + valueDate + ", withdrawalAmount="
				+ withdrawalAmount + ", depositAmount=" + depositAmount + ", balance=" + balance + "]";
	}

}
