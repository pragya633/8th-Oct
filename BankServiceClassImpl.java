package com.cg.ibs.cardmanagement.service;

import java.math.BigInteger;
import java.util.List;

import com.cg.ibs.cardmanagement.bean.CaseIdBean;
import com.cg.ibs.cardmanagement.bean.CreditCardTransaction;
import com.cg.ibs.cardmanagement.bean.DebitCardTransaction;
import com.cg.ibs.cardmanagement.dao.BankDao;
import com.cg.ibs.cardmanagement.dao.CardManagementDaoImpl;

public class BankServiceClassImpl implements BankService {

	BankDao bankDao = new CardManagementDaoImpl();
	CaseIdBean caseIdObj = new CaseIdBean();

	@Override
	public List<CaseIdBean> viewQueries() {

		List<CaseIdBean> caseBeans = bankDao.viewAllQueries();

		return bankDao.viewAllQueries();

	}

	@Override
	public List<DebitCardTransaction> getDebitTrns(int days, BigInteger debitCardNumber) {

		boolean check = bankDao.verifyDebitCardNumber(debitCardNumber);
		if (check) {
			if (days >= 1) {

				return bankDao.getDebitTrans(days, debitCardNumber);
			} else {
				return null;
			}

		} else {
			return null;
		}
	}

	@Override
	public List<CreditCardTransaction> getCreditTrans(int days, BigInteger creditCardNumber) {

		boolean check = bankDao.verifyCreditCardNumber(creditCardNumber);
		if (check) {
			if (days >= 1) {

				return bankDao.getCreditTrans(days, creditCardNumber);
			} else {
				return null;
			}

		} else {
			return null;
		}
	}

	@Override
	public boolean verifyQueryId(String queryId) {

		boolean check = bankDao.verifyQueryId(queryId);
		if (check) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public void setQueryStatus(String queryId, String newStatus) {
		// TODO Auto-generated method stub
		bankDao.setQueryStatus(queryId, newStatus);

	}

}