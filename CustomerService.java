package com.cg.ibs.cardmanagement.service;

import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

import com.cg.ibs.cardmanagement.dao.CustomerDao;
import com.cg.ibs.cardmanagement.bean.CaseIdBean;
import com.cg.ibs.cardmanagement.bean.CreditCardBean;
import com.cg.ibs.cardmanagement.bean.CreditCardTransaction;
import com.cg.ibs.cardmanagement.bean.DebitCardBean;
import com.cg.ibs.cardmanagement.bean.DebitCardTransaction;
import com.cg.ibs.cardmanagement.dao.CardManagementDaoImpl;

public interface CustomerService {

	public boolean verifyDebitCardNumber(BigInteger debitCardNumber);

	public List<DebitCardBean> viewAllDebitCards();

	public List<CreditCardBean> viewAllCreditCards();

	boolean applyNewDebitCard(BigInteger accountNumber);

	String verifyDebitcardType(BigInteger debitCardNumber);

	void requestDebitCardUpgrade(BigInteger debitCardNumber, int myChoice);

	String resetDebitPin(BigInteger debitCardNumber, int newPin);

	public boolean verifyDebitPin(int pin, BigInteger debitCardNumber);

	boolean verifyCreditCardNumber(BigInteger creditCardNumber);

	String resetCreditPin(BigInteger creditCardNumber, int newPin);

	public boolean verifyCreditPin(int pin, BigInteger creditCardNumber);

	void applyNewCreditCard();

	boolean requestDebitCardLost(BigInteger debitCardNumber);

	boolean requestCreditCardLost(BigInteger creditCardNumber);

	String verifyCreditcardType(BigInteger creditCardNumber);

	void requestCreditCardUpgrade(BigInteger creditCardNumber, int myChoice);

	boolean raiseDebitMismatchTicket(String transactionId);

	boolean raiseCreditMismatchTicket(String transactionId);

	public List<DebitCardTransaction> getDebitTrns(int dys, BigInteger debitCardNumber);

	public List<CreditCardTransaction> getCreditTrans(int days, BigInteger creditCardNumber);

	public int getPinLength(int pin);

}