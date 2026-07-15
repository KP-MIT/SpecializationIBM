package collectionsExample;


import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

// LibraryManagement class to manage the book collection
public class LibraryManagement {
    public static void main(String[] args) {
        // Step 1: Create a Scanner object for user input
        Scanner scanner = new Scanner(System.in);

        // Step 2: Create an ArrayList to store Book objects
        // Hint: Use ArrayList<Book>
        ArrayList<Book> books = new ArrayList<>();
        books.add(new Book("The Great Gatsby", "F. Scott Fitzgerald", 1925));
        books.add(new Book("To Kill a Mockingbird", "Harper Lee", 1960));
        books.add(new Book("1984", "George Orwell", 1949));
        // Step 3: Implement a menu-driven program with the following options:
        // 1. Add a book
        // 2. View all books
        // 3. Search for a book by title
        // 4. Check out a book
        // 5. Return a book
        // 6. Sort books (by title, author, or publication year)
        // 7. View available books only
        // 8. Exit
        // Step 4: Create the main menu loop
        // Hint: Use a while loop with a boolean flag or a while(true) with a break
        boolean running = true;
        while(running){
            System.out.println("\n ----------- Library Management System -----------");
            System.out.println("1. Add a book");
            System.out.println("2. View all books");
            System.out.println("3. Search for a book by title");
            System.out.println("4. Check out a book");
            System.out.println("5. Return a book");
            System.out.println("6. Sort books (by title, author, or publication year)");
            System.out.println("7. View available books only");
            System.out.println("8. Exit");
            System.out.println("Enter your choice: ");

            // Step 5: Implement case handling for each menu option
            // Hint: Use switch-case or if-else if statements
            int choice;
            try{
                choice = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e){
                System.out.println("Please try again with a number");
                continue;
            }

            switch (choice) {
                case 1 -> addBook(scanner, books);
                case 2 -> viewAllBooks(books);
                case 3 -> searchBookByTitle(scanner, books);
                case 4 -> checkOutBook(scanner, books);
                case 5 -> returnBook(scanner, books);
                case 6 -> sortBooks(scanner, books);
                case 7 -> viewAvailableBooks(books);
                case 8 -> {
                    System.out.println("Thank you for using digital library");
                    running = false;
                }
                default -> System.out.println("Invalid choice, try again");
            }
        }
        scanner.close();
    }

    // Step 6: Implement the addBook functionality
    // Hint: Prompt the user for title, author, and publication year
    private static void addBook(Scanner scanner, ArrayList<Book> books){
        System.out.println("\n ----------- Add a new Book -------------");
        System.out.println("Enter Book Title: ");
        String title = scanner.nextLine();
        System.out.println("Enter author's name: ");
        String author = scanner.nextLine();
        int year=0;
        boolean validYear=false;
        while(!validYear) {
            System.out.println("Enter publication year: ");
            try{
                year=Integer.parseInt(scanner.nextLine());
                validYear=true;
            } catch(NumberFormatException e){
                System.out.println("Enter a valid year");
            }
        }
        books.add(new Book(title,author,year));
        System.out.println("New book - "+title+" - added");
    }
    // Step 7: Implement the viewAllBooks functionality
    // Hint: Use a loop or forEach to display all books
    private static void viewAllBooks(ArrayList<Book> books){
        System.out.println("\n ------------ View All Books -----------");
        if (!books.isEmpty()){
            for (Book book:books){
                System.out.println(book.toString());
            }
        } else {
            System.out.println("library is empty");
        }
    }

    // Step 8: Implement the search functionality
    // Hint: Take user input for search term and check each book
    private static void searchBookByTitle(Scanner scanner, ArrayList<Book> books){
        System.out.println("\n ----------- Search Book by Title -----------");
        System.out.println("Enter Title: ");
        String searchTitle = scanner.nextLine().toLowerCase();
        boolean found = false;
        for (Book book:books){
            if (book.getBookTitle().toLowerCase().contains(searchTitle)){
                found = true;
                System.out.println("Book found: "+ book);
            }
        }
        if (!found) {
            System.out.println("No book found with the given title");
        }
    }

    // Step 9: Implement the checkOut functionality
    // Hint: Find the book by index and use the checkOut() method
    public static void checkOutBook(Scanner scanner, ArrayList<Book> books){
        System.out.println("\n --------------- Checkout book -------------");
        if(books.isEmpty()){
            System.out.println("Library is empty");
            return;
        }
        boolean hasAvailable=false;
        for (Book book:books){
            if(book.getAvailable()){
                hasAvailable=true;
                System.out.println("Book number: "+books.indexOf(book)+" : "+ book);
            }
        }
        if(!hasAvailable){
            System.out.println("No book available");
            return;
        }
        System.out.println("Enter the book number for check out: ");
        try {
            int bookIndex = Integer.parseInt(scanner.nextLine());
            if(bookIndex<0 || bookIndex>books.size()-1){
                System.out.println("Invalid book number");
                return;
            }
            Book selectedBook = books.get(bookIndex);

            if (selectedBook.checkOut()){
                System.out.println("Checkout successful for: "+selectedBook);
            } else {
                System.out.println("Book already checked out");
            }
        } catch (NumberFormatException e) {
            System.out.println("Enter a number only");
        }
    }

    // Step 10: Implement the returnBook functionality
    // Hint: Find the book by index and use the returnBook() method
    private static void returnBook(Scanner scanner, ArrayList<Book> books){
        System.out.println("\n ------------- Return Book -----------");
        boolean hasCheckOut = false;
        System.out.println("Checked out books are : ");
        for(Book book: books){
            if(!book.getAvailable()){
                System.out.println("Book Number: "+books.indexOf(book)+" : "+book);
                hasCheckOut=true;
            }
        }
        if (!hasCheckOut) {
            System.out.println("No books are checked out");
            return;
        }
        System.out.println("Enter the book number to return: ");
        try{
            int bookIndex = Integer.parseInt(scanner.nextLine());
            if(bookIndex<0 || bookIndex>books.size()-1){
                System.out.println("invalid book index number");
                return;
            }
            if (books.get(bookIndex).returnBook()){
                System.out.println("Book returned successfully");
            } else {
                System.out.println("Book was not checked out");
            }
        } catch (NumberFormatException e){
            System.out.println("Enter a valid number only");
        }
    }

    // Step 11: Implement the sortBooks functionality
    // Hint: Use Collections.sort() with a Comparator
    public static void sortBooks(Scanner scanner, ArrayList<Book> books){
        System.out.println("\n --------------- Sort Books -------------");
        System.out.println("Choose number to sort books by :");
        System.out.println("1. Title");
        System.out.println("2. Author");
        System.out.println("3. Publication year");
        try{
            int sortIndex = Integer.parseInt(scanner.nextLine());
            if (sortIndex<1 || sortIndex>3){
                System.out.println("Enter a valid index");
                return;
            }
            switch (sortIndex) {
                case 1 -> {
                    books.sort(new Comparator<Book>() {
                        @Override
                        public int compare(Book b1, Book b2) {
                            return b1.getBookTitle().compareToIgnoreCase(b2.getBookTitle());
                        }
                    });
                    System.out.println("Books sorted by Title");
                }
                case 2 -> {
                    books.sort((b1, b2) -> b1.getAuthor().compareToIgnoreCase(b2.getAuthor()));
                    System.out.println("Books sorted by Author");
                }
                case 3 -> {
                    books.sort(new Comparator<Book>() {
                        @Override
                        public int compare(Book b1, Book b2) {
                            return Integer.compare(b1.getPublicationYear(), b2.getPublicationYear());
                        }
                    });
                    System.out.println("Books sorted by Publication year");
                }
                default -> System.out.println("Invalid choice");
            }
            viewAllBooks(books);
        } catch (NumberFormatException e){
            System.out.println("Enter a valid number only.");
        }
    }

    // Step 12: Implement the viewAvailableBooks functionality
    // Hint: Use ArrayList's stream() or loop through to filter
    public static void viewAvailableBooks(ArrayList<Book> books){
        System.out.println("\n --------------- View All Available Books --------------");
        List<Book> availableBooks = books.stream().filter(Book::getAvailable).toList();
        for (Book book : availableBooks){
            System.out.println(book);
        }
    }

    // Step 13: Create additional helper methods as needed
    // Examples: findBookByTitle(), findBookByIndex(), etc.
}
