package com.mycompany;

import java.util.ArrayList;
import java.util.List;

public class Library {

    private ArrayList<Book> books = new ArrayList<>();

    // return a reference to the book
    // public Book getBook(int index) {
    //     return books.get(index);
    // }
    // if modification is not allowed, then a copy of the book
    // can be returned instead
    public Book getBook(int index) {
        return new Book(books.get(index));
    }

    public void addBook(Book book) {
        books.add(book);
    }

    // returns a reference to the private books reference variable of the Library class
    //public List getBooks() {
    //    return books;
    //}
    public List getBooks() {
        ArrayList list = new ArrayList(books.size());
        for (Book book : books) {
            list.add(new Book(book));
        }
        return list;
    }
}
