package dataaccess;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;

import business.Book;
import business.BookCopy;
import business.LibraryMember;
import dataaccess.DataAccessFacade.StorageType;


public class DataAccessFacade implements DataAccess {
	
	enum StorageType {
		BOOKS, MEMBERS, USERS, BOOKCOPIES, CHECKOUTRECORDENTRY,CHECKOUTRECORD;
	}
	
	public static final String OUTPUT_DIR = System.getProperty("user.dir") 
			+ "\\src\\dataaccess\\storage";
	public static final String DATE_PATTERN = "MM/dd/yyyy";
	
	//implement: other save operations
	public void saveNewMember(LibraryMember member) {
		HashMap<String, LibraryMember> mems = readMemberMap();
		String memberId = member.getMemberId();
		mems.put(memberId, member);
		saveToStorage(StorageType.MEMBERS, mems);	
	}
	
	@Override
	public void saveCheckoutRecordEntry(CheckoutRecordEntry checkOutRE) {
		HashMap<Integer, CheckoutRecordEntry> checkoutRecordEntryMap = readCheckoutRecordEntry();
		Integer uCheckoutId = 1;
		checkoutRecordEntryMap.put(uCheckoutId, checkOutRE);
		saveToStorage(StorageType.CHECKOUTRECORDENTRY, checkOutRE);
	}

	@Override
	public void saveToCheckoutRecord(String memberId, CheckoutRecordEntry checkOutRE) {
		HashMap<String, List<CheckoutRecordEntry>> checkoutRecordList = readCheckoutRecord();
		if (checkoutRecordList.containsKey(memberId)) {
			List<CheckoutRecordEntry> updateCheckoutList = checkoutRecordList.get(memberId);
			updateCheckoutList.add(checkOutRE);
			checkoutRecordList.put(memberId, updateCheckoutList);
		} else {
			List<CheckoutRecordEntry> newList = new ArrayList<>();
			newList.add(checkOutRE);
			checkoutRecordList.put(memberId, newList);
		}
		saveToStorage(StorageType.CHECKOUTRECORD, checkoutRecordList);
	}

	@Override
	public void saveCheckoutRecord(String memberId, List<CheckoutRecordEntry> checkOutREList) {
		HashMap<String, List<CheckoutRecordEntry>> checkoutRecordList = readCheckoutRecord();
		if (checkoutRecordList.containsKey(memberId)) {
			List<CheckoutRecordEntry> updateCheckoutList = checkoutRecordList.get(memberId);
			updateCheckoutList.addAll(checkOutREList);
			checkoutRecordList.put(memberId, updateCheckoutList);
		} else {
			checkoutRecordList.put(memberId, checkOutREList);
		}
		saveToStorage(StorageType.CHECKOUTRECORD, checkoutRecordList);
	}
	
	public List<CheckoutRecordEntry> getCheckoutRecordByMemberId(String libraryMemberId) {
		List<CheckoutRecordEntry> checkoutList = null;
		HashMap<String, List<CheckoutRecordEntry>> checkoutMap = readCheckoutRecord();
		if (checkoutMap.containsKey(libraryMemberId)) {
			checkoutList = checkoutMap.get(libraryMemberId);
		}
		return checkoutList;
	}
	
	@Override
	public HashMap<Integer, BookCopy> readBookCopy() {
		return null;
	}
	
	public HashMap<String, HashMap<Integer, BookCopy>> readBookCopies() {
		return (HashMap<String, HashMap<Integer, BookCopy>>) readFromStorage(StorageType.BOOKCOPIES);
	}

	private HashMap<Integer, CheckoutRecordEntry> readCheckoutRecordEntry() {
		return (HashMap<Integer, CheckoutRecordEntry>) readFromStorage(StorageType.CHECKOUTRECORDENTRY);
	}

	public HashMap<String, List<CheckoutRecordEntry>> readCheckoutRecord() {
		return (HashMap<String, List<CheckoutRecordEntry>>) readFromStorage(StorageType.CHECKOUTRECORD);
	}

	
	@SuppressWarnings("unchecked")
	public  HashMap<String,Book> readBooksMap() {
		//Returns a Map with name/value pairs being
		//   isbn -> Book
		return (HashMap<String,Book>) readFromStorage(StorageType.BOOKS);
	}
	
	@SuppressWarnings("unchecked")
	public HashMap<String, LibraryMember> readMemberMap() {
		//Returns a Map with name/value pairs being
		//   memberId -> LibraryMember
		return (HashMap<String, LibraryMember>) readFromStorage(
				StorageType.MEMBERS);
	}
	
	
	@SuppressWarnings("unchecked")
	public HashMap<String, User> readUserMap() {
		//Returns a Map with name/value pairs being
		//   userId -> User
		return (HashMap<String, User>)readFromStorage(StorageType.USERS);
	}
	
	
	/////load methods - these place test data into the storage area
	///// - used just once at startup  
	
		
	static void loadBookMap(List<Book> bookList) {
		HashMap<String, Book> books = new HashMap<String, Book>();
		bookList.forEach(book -> books.put(book.getIsbn(), book));
		saveToStorage(StorageType.BOOKS, books);
	}
	static void loadUserMap(List<User> userList) {
		HashMap<String, User> users = new HashMap<String, User>();
		userList.forEach(user -> users.put(user.getId(), user));
		saveToStorage(StorageType.USERS, users);
	}
 
	static void loadMemberMap(List<LibraryMember> memberList) {
		HashMap<String, LibraryMember> members = new HashMap<String, LibraryMember>();
		memberList.forEach(member -> members.put(member.getMemberId(), member));
		saveToStorage(StorageType.MEMBERS, members);
	}
	
	public static void loadCheckout() {
		HashMap<String, List<CheckoutRecordEntry>> chechoutListMap = new HashMap<>();
		saveToStorage(StorageType.CHECKOUTRECORD, chechoutListMap);
	}
	
	static void saveToStorage(StorageType type, Object ob) {
		ObjectOutputStream out = null;
		try {
			Path path = FileSystems.getDefault().getPath(OUTPUT_DIR, type.toString());
			out = new ObjectOutputStream(Files.newOutputStream(path));
			out.writeObject(ob);
		} catch(IOException e) {
			e.printStackTrace();
		} finally {
			if(out != null) {
				try {
					out.close();
				} catch(Exception e) {}
			}
		}
	}
	
	static Object readFromStorage(StorageType type) {
		ObjectInputStream in = null;
		Object retVal = null;
		try {
			Path path = FileSystems.getDefault().getPath(OUTPUT_DIR, type.toString());
			in = new ObjectInputStream(Files.newInputStream(path));
			retVal = in.readObject();
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			if(in != null) {
				try {
					in.close();
				} catch(Exception e) {}
			}
		}
		return retVal;
	}
	
	
	
	final static class Pair<S,T> implements Serializable{
		
		S first;
		T second;
		Pair(S s, T t) {
			first = s;
			second = t;
		}
		@Override 
		public boolean equals(Object ob) {
			if(ob == null) return false;
			if(this == ob) return true;
			if(ob.getClass() != getClass()) return false;
			@SuppressWarnings("unchecked")
			Pair<S,T> p = (Pair<S,T>)ob;
			return p.first.equals(first) && p.second.equals(second);
		}
		
		@Override 
		public int hashCode() {
			return first.hashCode() + 5 * second.hashCode();
		}
		@Override
		public String toString() {
			return "(" + first.toString() + ", " + second.toString() + ")";
		}
		private static final long serialVersionUID = 5399827794066637059L;
	}
	
	@Override
	public void updateBook(Book b) {
		HashMap<String, Book> bookListMap = readBooksMap();
		String isbn = b.getIsbn();
		if (bookListMap.containsKey(isbn)) {
			bookListMap.put(isbn, b);
		}
		saveToStorage(StorageType.BOOKS, bookListMap);
	}
}
