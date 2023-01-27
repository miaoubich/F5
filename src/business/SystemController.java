package business;

import java.awt.print.Book;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import dataaccess.Auth;
import dataaccess.DataAccess;
import dataaccess.DataAccessFacade;
import dataaccess.User;

public class SystemController implements ControllerInterface {
	public static Auth currentAuth = null;
	
	public void login(String id, String password) throws LoginException {
		DataAccess da = new DataAccessFacade();
		HashMap<String, User> map = da.readUserMap();
		if(!map.containsKey(id)) {
			throw new LoginException("ID " + id + " not found");
		}
		String passwordFound = map.get(id).getPassword();
		if(!passwordFound.equals(password)) {
			throw new LoginException("Password incorrect");
		}
		currentAuth = map.get(id).getAuthorization();
		
	}
	
	
	@Override
	public List<String> allMemberIds() {
		DataAccess da = new DataAccessFacade();
		List<String> retval = new ArrayList<>();
		retval.addAll(da.readMemberMap().keySet());
		return retval;
	}
	
	@Override
	public List<String> allBookIds() {
		DataAccess da = new DataAccessFacade();
		List<String> retval = new ArrayList<>();
		retval.addAll(da.readBooksMap().keySet());
		return retval;
	}
	
	@Override
	//sushil code test
	public Book addBookCopy(String isbn) throws Exception {
		DataAccess da = new DataAccessFacade();
		HashMap<String, Book> bookMap = da.readBooksMap();
		if (!bookMap.containsKey(isbn))
			throw new Exception("No book found, couldn't add new copy");
		Book b = bookMap.get(isbn);
		b.addCopy();
		da.updateBook(b);
		
		return b;
	}
	
	@Override
	public BookCopy checkoutBook(String LibraryMemberId, String isbn) throws Exception{
		DataAccess da = new DataAccessFacade();
		HashMap<String, Book> bookMap = da.readBooksMap();
		if(!bookMap.containsKey(isbn))
			throw new Exception("This Book doesn not exist!!");
		
		Book book = bookMap.get(isbn);
		if(!book.isAvailable())
			throw new Exception("Requested book of ISBN number "+isbn+" is currently unavailable");
		return addToCheckout(book.getNextAvailableCopy(),libraryMemberId);
	}
	
	private BookCopy addToCheckout(BookCopy bCopy, String libraryMemberId) throws Exception {
		CheckoutRecordEntry checkOutRE = new CheckoutRecordEntry(bCopy, libraryMemberId);
		DataAccess da = new DataAccessFacade();
		try {
			da.saveToCheckoutRecord(libraryMemberId, checkoutRE);
			HashMap<String, Book> bookMap = a.readBookMap();
			Book book = bCopy.getBook();
			bCopy.changeAvailability();
			book.updateCopies(bCopy);
			bookMap.put(book.getIsbn(), book);
		} catch (Exception e) {
			throw new Exception ("Checkout Error!!!");
		}
		return bCopy;
		
	}
	
	@Override
	public List<CheckoutRecordEntry> getCheckoutRecordByMemberId(String LibraryMemberId) throws Exception{
		DataAccess da = new DataAccessFacade();
		HashMap<String LibraryMember> libMemberMap = da.readMemberMap();
		if(!libMemberMap.containsKey(libraryMemberId))
			throw new Exception("Member Id does not exist!!");
		List<CheckoutRecordEntry> checkoutList = da.getCheckoutRecordByMemberId(libraryMemberId);
		return checkoutList;
	}
	
	public void confirmBookCheckout(List<BookCopy> bookCopyCheckoutList, LibraryMember libMember) {
		DataAccess da = new DataAccessFacade();
		List<CheckoutRecordEntry> checkoutBookList = new ArrayList<>();
		HashMap<String, HashMap<Integer, BookCopy>> bookAndBookCopyMap = da.readBookCopies();
		for(BookCopy bc:bookCopyCheckoutList) {
			String isbn = bc.getBook().getIsbn();
			int bookCopyUID = bc.getCopyNum();
			if(bookAndBookCopyMap.containsKey(isbn)) {
				HashMap<Integer, BookCopy> bookCopyMap = bookAndBookCopyMap.get(isbn);
				BookCopy updateBookCopyObj = bookCopyMap.get(bookCopyUID);
				updateBookCopyObj.setAvailable(false);
				da.updateBookCopy(bookCopyUID, updateBookCopyObj);
				
				CheckoutRecordEntry checkOutRE = new CheckoutRecordEntry(updateBookCopyObj, libMember.getMember());
				da.saveCheckoutRecordEntry(checkOutRE);
				checkoutBookList.add(checkOutRE);
		}
	}
		if(checkoutBookList.size()>0) {
			da.saveCheckoutRecord(libMember.getMemberId(),checkoutBookList);
		}
	
	}
}
