package com.cg.ibs.cardmanagement.ui;

import java.util.*;
import java.util.regex.Pattern;

import com.cg.ibs.cardmanagement.bean.CaseIdBean;
import com.cg.ibs.cardmanagement.bean.CreditCardBean;
import com.cg.ibs.cardmanagement.bean.CreditCardTransaction;
import com.cg.ibs.cardmanagement.bean.DebitCardBean;
import com.cg.ibs.cardmanagement.bean.DebitCardTransaction;
import com.cg.ibs.cardmanagement.exceptionhandling.IBSException;
import com.cg.ibs.cardmanagement.service.BankService;
import com.cg.ibs.cardmanagement.service.BankServiceClassImpl;
import com.cg.ibs.cardmanagement.service.CustomerService;
import com.cg.ibs.cardmanagement.service.CustomerServiceImpl;

import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class CardManagementUI {

	static BigInteger accountNumber = null;
	static Scanner scan;
	static BigInteger debitCardNumber = null;
	static BigInteger creditCardNumber = null;
	
	static String transactionId;
	static boolean success = false;
	static int myChoice = 0;
	static int pin;
	CustomerService customService = new CustomerServiceImpl();
	BankService bankService = new BankServiceClassImpl();

	public void doIt() {
		while (true) {

			System.out.println("Welcome to card management System");
			System.out.println("Enter 1 to login as a customer");
			System.out.println("Enter 2 to login as a bank admin");

			int userInput = scan.nextInt();

			if (userInput == 1) {
				System.out.println("You are logged in as a customer");
				CustomerMenu choice = null;
				while (choice != CustomerMenu.CUSTOMER_LOG_OUT) {
					System.out.println("Menu");
					System.out.println("--------------------");
					System.out.println("Choice");
					System.out.println("--------------------");
					for (CustomerMenu mmenu : CustomerMenu.values()) {
						System.out.println(mmenu.ordinal() + "\t" + mmenu);
					}
					System.out.println("Choice");

					int ordinal = scan.nextInt();
					if (ordinal >= 0 && ordinal < 15) {
						choice = CustomerMenu.values()[ordinal];

						BigInteger creditCardNumber = null;
						switch (choice) {

						case LIST_EXISTING_DEBIT_CARDS:

							List<DebitCardBean> debitCardBeans = customService.viewAllDebitCards();
							if (debitCardBeans.isEmpty()) {
								System.out.println("No Existing Debit Cards");
							} else {
								for (DebitCardBean debitCardBean : debitCardBeans) {
									System.out.println(debitCardBeans.toString());
								}
							}
							break;

						case LIST_EXISTING_CREDIT_CARDS:

							List<CreditCardBean> creditCardBeans = customService.viewAllCreditCards();
							if (creditCardBeans.isEmpty()) {
								System.out.println("No Existing Credit Cards");
							} else {
								for (CreditCardBean creditCardBean : creditCardBeans) {
									System.out.println(creditCardBeans.toString());
								}
							}
							break;

						case APPLY_NEW_DEBIT_CARD:
							success = false;
							System.out.println("You are applying for a new Debit Card");
							while (!success) {

								try {
									System.out.println("Enter Account Number you want to apply debit card for :");

									accountNumber = scan.nextBigInteger();

									success = true;
								} catch (InputMismatchException wrongFormat) {

									scan.next();
									System.out.println("Renter 10 digit account number");
								}
							}

							boolean result = customService.applyNewDebitCard(accountNumber);
							System.out.println(result);
							if (result) {
								System.out.println(" Debit Card applied successfully");
							} else {
								System.out.println("Account Number Entered wrong");
							}

							break;
						case APPLY_NEW_CREDIT_CARD:

							System.out.println("You are applying for a new Credit Card");

							customService.applyNewCreditCard();
							System.out.println("New Credit Card applied successfully");
							break;

						case UPGRADE_EXISTING_DEBIT_CARD:

							success = false;

							System.out.println("Enter your Debit Card Number: ");
							while (!success) {

								try {

									debitCardNumber = scan.nextBigInteger();
									System.out.println(debitCardNumber);
									success = true;
								} catch (InputMismatchException wrongFormat) {
									scan.next();
									System.out.println("Enter a valid Debit card number");
								}
							}
							String type = customService.verifyDebitcardType(debitCardNumber);
							System.out.println("Your debit card is:" + type);

							if (type.equals("Silver")) {
								System.out.println("Choose 1 to upgrade to Gold");
								System.out.println("Choose 2 to upgrade to Platinum");

								success = false;
								while (!success) {
									try {
										myChoice = scan.nextInt();
										success = true;
									} catch (InputMismatchException wrongFormat) {
										scan.next();
										System.out.println("Choose between 1 or 2");

									}
								}
								customService.requestDebitCardUpgrade(debitCardNumber, myChoice);

							} else if (type.equals("Gold")) {
								System.out.println("Choose 2 to upgrade to Platinum");
								success = false;
								while (!success) {
									try {
										myChoice = scan.nextInt();
										success = true;
									} catch (InputMismatchException wrongFormat) {
										scan.next();
										System.out.println("Enter 2 to upgrade");
									}
								}
								customService.requestDebitCardUpgrade(debitCardNumber, myChoice);

							} else {
								System.out.println("You already have a Platinum Card");
							}

							break;
						case UPGRADE_EXISTING_CREDIT_CARD:
							System.out.println("Enter your Credit Card Number: ");
							while (!success) {

								int myChoice1;
								try {
									creditCardNumber = scan.nextBigInteger();
									success = true;
								} catch (InputMismatchException wrongFormat) {

									scan.next();
									System.out.println("Renter  credit  number");

								}
								String type1 = customService.verifyCreditcardType(creditCardNumber);
								System.out.println("Your credit card is:" + type1);

								if (type1.equals("Silver")) {
									System.out.println("Choose 1 to upgrade to Gold");
									System.out.println("Choose 2 to upgrade to Platinum");
									myChoice1 = scan.nextInt();
									customService.requestCreditCardUpgrade(creditCardNumber, myChoice1);

								} else if (type1.equals("Gold")) {
									System.out.println("Choose 2 to upgrade to Platinum");
									myChoice1 = scan.nextInt();
									customService.requestCreditCardUpgrade(creditCardNumber, myChoice1);

								} else {
									System.out.println("You already have a Platinum Card");
								}
							}
							break;
						case RESET_DEBIT_CARD_PIN:
							success = false;
							System.out.println("Enter your Debit Card Number: ");

							while (!success) {

								try {
									debitCardNumber = scan.nextBigInteger();
									success = true;
								} catch (InputMismatchException wrongFormat) {
									scan.next();
									System.out.println("Enter a valid debit card number");
								}

							}

							boolean check = customService.verifyDebitCardNumber(debitCardNumber);
							if (check) {
								System.out.println("Enter your existing pin:");

								success = false;
								while (!success) {
									try {

										pin = scan.nextInt();

										if (customService.getPinLength(pin) != 4)
											throw new IBSException("Incorrect Length of pin ");
										getNewPin(pin, debitCardNumber);
		
										
										// cll function below to set new pin
										success = true;
									} catch (InputMismatchException wrongFormat) {
										System.out.println("Enter a valid 4 digit pin");
										scan.next();

									} catch (IBSException ExceptionObj) {
										System.out.println(ExceptionObj.getMessage());
										scan.next();

									}
								}

							}

							break;
						case RESET_CREDIT_CARD_PIN:
							success = false;
							System.out.println("Enter your Credit Card Number: ");
							while(!success) {
								try {
							creditCardNumber = scan.nextBigInteger();
							success=true;
								}catch (InputMismatchException wrongFormat) {
									scan.next();
									System.out.println("Enter a valid credit card number");
								}
							}
							boolean check1 = customService.verifyCreditCardNumber(creditCardNumber);
							if (check1) {
								System.out.println("Enter your existing pin:");
								success=false;
								while (!success) {
									try {

										int pin = scan.nextInt();

										if (customService.getPinLength(pin) != 4)
											throw new IBSException("Incorrect Length of pin ");
										getNewPin2(pin, creditCardNumber);
										// cll function below to set new pin
										success = true;
									} catch (InputMismatchException wrongFormat) {
										System.out.println("Enter a valid 4 digit pin");
										scan.next();

									} catch (IBSException ExceptionObj) {
										System.out.println(ExceptionObj.getMessage());
										scan.next();

									}
								}
								
							}
							break;
							
							
						case REPORT_DEBIT_CARD_LOST:
							
							success = false;
							while (!success) {

								try {
									System.out.println("Enter your Debit Card Number: ");
									debitCardNumber = scan.nextBigInteger();
									success = true;
								} catch (InputMismatchException wrongFormat) {
									
									System.out.println("Not a valid format");
									scan.next();
								}
							}
								boolean result1=customService.requestDebitCardLost(debitCardNumber);
								if(result1) {
									System.out.println("Ticket raised successfully");
								}
								else
								{
									System.out.println("Invalid debit card number");
								}
							
							break;
						case REPORT_CREDIT_CARD_LOST:
							success = false;
							while (!success) {

								try {
									System.out.println("Enter your Credit Card Number: ");
									creditCardNumber = scan.nextBigInteger();
									success = true;
								} catch (InputMismatchException wrongFormat) {
									
									System.out.println("Not a valid format");
									scan.next();
								}
							}
								boolean result2=customService.requestCreditCardLost(creditCardNumber);
								if(result2) {
									System.out.println("Ticket raised successfully");
								}
								else
								{
									System.out.println("Invalid Credit card number");
								}
							
						case REQUEST_DEBIT_CARD_STATEMENT:
							success = false;
							int days = 0;
							while (!success) {
								

								try {
									System.out.println("Enter your Debit Card Number: ");
									debitCardNumber = scan.nextBigInteger();
									success = true;
								} catch (InputMismatchException wrongFormat) {
									scan.next();
									System.out.println("Not a valid format");
								}
							}
                                 success=false;
                                
                                 while(!success) {
                                	 try {
								System.out.println("enter days : ");
								 days = scan.nextInt();
								success=true;
                                	 }catch( InputMismatchException wrongFormat) {
     									scan.next();
    									System.out.println("Not a valid format");
									}
                                 }

								try {
									List<DebitCardTransaction> debitCardBeanTrns = customService.getDebitTrns(days,
											debitCardNumber);
									for (DebitCardTransaction debitCardTrns : debitCardBeanTrns)
										System.out.println(debitCardTrns.toString());
								}

								catch (Exception e) {
									System.out.println("invalid date format or debit card does not exist");
								}
							
							break;
						case REQUEST_CREDIT_CARD_STATEMENT:
							success = false;
							int days1 = 0;
							while (!success) {
								

								try {
									System.out.println("Enter your Credit Card Number: ");
									creditCardNumber = scan.nextBigInteger();
									success = true;
								} catch (InputMismatchException wrongFormat) {
									scan.next();
									System.out.println("Not a valid format");
								}
							}
                                 success=false;
                                
                                 while(!success) {
                                	 try {
								System.out.println("enter days : ");
								 days1 = scan.nextInt();
								success=true;
                                	 }catch( InputMismatchException wrongFormat) {
     									scan.next();
    									System.out.println("Not a valid format");
									}
                                 }

								try {
									List<CreditCardTransaction> creditCardBeanTrns = customService.getCreditTrans(days1,
											creditCardNumber);
									for (CreditCardTransaction creditCardTrns : creditCardBeanTrns)
										System.out.println(creditCardTrns.toString());
								}

								catch (Exception e) {
									System.out.println("invalid date format or debit card does not exist");
								}
							
							break;
						case REPORT_DEBITCARD_STATEMENT_MISMATCH:

							
							success=false;
							while(!success) {
								
								try {
									
							System.out.println("Enter your transaction id");
							transactionId = scan.next();
							success=true;
								}catch (InputMismatchException wrongFormat) {
     									scan.next();
    									System.out.println("Not a valid format");
								}
							}
							boolean result3=customService.raiseDebitMismatchTicket(transactionId);
							if (result3) {
								System.out.println(" Ticket Raised successfully");
							} else {
								System.out.println("Invalid transaction id");
							}

							break;
						case REPORT_CREDITCARD_STATEMENT_MISMATCH:
							success=false;
							while(!success) {
								
								try {
									
							System.out.println("Enter your transaction id");
							transactionId = scan.next();
							success=true;
								}catch (InputMismatchException wrongFormat) {
     									scan.next();
    									System.out.println("Not a valid format");
								}
							}
							boolean result4=customService.raiseCreditMismatchTicket(transactionId);
							if (result4) {
								System.out.println(" Ticket Raised successfully");
							} else {
								System.out.println("Invalid transaction id");
							}

							break;
						case CUSTOMER_LOG_OUT:
							System.out.println("LOGGED OUT");
							break;
						}
					}

				}
			} else {
				if (userInput == 2) {

					System.out.println("You are logged in as a Bank Admin");
					BankMenu cchoice = null;
					while (cchoice != BankMenu.BANK_LOG_OUT) {
						System.out.println("Menu");
						System.out.println("--------------------");
						System.out.println("Choice");
						System.out.println("--------------------");
						for (BankMenu mmenu : BankMenu.values()) {
							System.out.println(mmenu.ordinal() + "\t" + mmenu);
						}
						System.out.println("Choice");
						int ordinal = scan.nextInt();
						if (ordinal >= 0 && ordinal < BankMenu.values().length) {
							cchoice = BankMenu.values()[ordinal];

							switch (cchoice) {

							case LIST_QUERIES:
								
								
								List<CaseIdBean> caseBeans = bankService.viewQueries();
								if (caseBeans.isEmpty()) {
									System.out.println("No Existing Queries");
								} else {

								for (CaseIdBean caseId : caseBeans) {

									System.out.println(caseId.toString());
								}
								}
								
								break;

							case REPLY_QUERIES:
								String queryId = null;
								String newStatus=null;
								success=false;
								while(!success) {
						         try {
								System.out.println("Enter query ID ");
								queryId = scan.next();
								success=true;
						         }catch (InputMismatchException wrongFormat) {
										scan.next();
										System.out.println("Not a valid format");
									}
								}
								if (bankService.verifyQueryId(queryId)) {
									success=false;
									while(!success) {
							         try {
									System.out.println("Enter new Status");
									newStatus = scan.next();
							         }catch (InputMismatchException wrongFormat) {
										scan.next();
										System.out.println("Not a valid format");
									}
									}
									bankService.setQueryStatus(queryId, newStatus);

								}
								else
								{
									System.out.println("Invalid query id");
								}

								break;
							case VIEW_DEBIT_CARD_STATEMENT:
								success = false;
								int days = 0;
								while (!success) {
									

									try {
										System.out.println("Enter your Debit Card Number: ");
										debitCardNumber = scan.nextBigInteger();
										success = true;
									} catch (InputMismatchException wrongFormat) {
										scan.next();
										System.out.println("Not a valid format");
									}
								}
	                                 success=false;
	                                
	                                 while(!success) {
	                                	 try {
									System.out.println("enter days : ");
									 days = scan.nextInt();
									success=true;
	                                	 }catch( InputMismatchException wrongFormat) {
	     									scan.next();
	    									System.out.println("Not a valid format");
										}
	                                 }

									try {
										List<DebitCardTransaction> debitCardBeanTrns = bankService.getDebitTrns(days,
												debitCardNumber);
										for (DebitCardTransaction debitCardTrns : debitCardBeanTrns)
											System.out.println(debitCardTrns.toString());
									}

									catch (Exception e) {
										System.out.println("invalid date format or debit card does not exist");
									}
								break;
							case VIEW_CREDIT_CARD_STATEMENT:
								success = false;
								int days1 = 0;
								while (!success) {
									

									try {
										System.out.println("Enter your Credit Card Number: ");
										creditCardNumber = scan.nextBigInteger();
										success = true;
									} catch (InputMismatchException wrongFormat) {
										scan.next();
										System.out.println("Not a valid format");
									}
								}
	                                 success=false;
	                                
	                                 while(!success) {
	                                	 try {
									System.out.println("enter days : ");
									 days1 = scan.nextInt();
									success=true;
	                                	 }catch( InputMismatchException wrongFormat) {
	     									scan.next();
	    									System.out.println("Not a valid format");
										}
	                                 }

									try {
										List<CreditCardTransaction> creditCardBeanTrns = bankService.getCreditTrans(days1,
												creditCardNumber);
										for (CreditCardTransaction creditCardTrns : creditCardBeanTrns)
											System.out.println(creditCardTrns.toString());
									}

									catch (Exception e) {
										System.out.println("invalid date format or debit card does not exist");
									}
								

								break;
							case BANK_LOG_OUT:
								System.out.println("LOGGED OUT");
								break;

							}
						}
					}
				} else {
					System.out.println("Invalid Option!!");

				}

			}

		}
	}

	private void getNewPin(int pin2, BigInteger debitCardNumber) {
		if (customService.verifyDebitPin(pin, debitCardNumber)) {
			System.out.println("Enter new pin");
			success = false;
			while (!success) {
				try {

					pin = scan.nextInt();

					if (customService.getPinLength(pin) != 4)
						throw new IBSException("Incorrect Length of pin ");
					System.out.println(customService.resetDebitPin(debitCardNumber, pin));
					success = true;
				} catch (InputMismatchException wrongFormat) {
					System.out.println("Enter a valid 4 digit pin");
					scan.next();

				} catch (IBSException ExceptionObj) {
					System.out.println(ExceptionObj.getMessage());
					scan.next();

				}
			}

		} else {

			System.out.println("You have entered wrong pin ");
			System.out.println("Try again");
		}

	}
	
	private void getNewPin2(int pin2, BigInteger creditCardNumber) {
		if (customService.verifyCreditPin(pin, creditCardNumber) ){
			System.out.println("Enter new pin");
			success = false;
			while (!success) {
				try {

					int pin = scan.nextInt();

					if (customService.getPinLength(pin) != 4)
						throw new IBSException("Incorrect Length of pin ");
					System.out.println(customService.resetCreditPin(creditCardNumber, pin));
					success = true;
				} catch (InputMismatchException wrongFormat) {
					System.out.println("Enter a valid 4 digit pin");
					scan.next();

				} catch (IBSException ExceptionObj) {
					System.out.println(ExceptionObj.getMessage());
					scan.next();

				}
			}

		} else {

			System.out.println("You have entered wrong pin ");
			System.out.println("Try again");
		}

	}

	public static void main(String args[]) throws Exception {
		scan = new Scanner(System.in);
		CardManagementUI obj = new CardManagementUI();
		obj.doIt();
		System.out.println("Program End");
		obj.scan.close();
	}
}