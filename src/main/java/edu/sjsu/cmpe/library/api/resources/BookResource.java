package edu.sjsu.cmpe.library.api.resources;

import java.util.*;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.DELETE;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.validation.Valid;

import com.yammer.dropwizard.jersey.params.LongParam;
import com.yammer.metrics.annotation.Timed;

import edu.sjsu.cmpe.library.domain.Book;
import edu.sjsu.cmpe.library.dto.BookDto;
import edu.sjsu.cmpe.library.dto.LinkDto;
//import edu.sjsu.cmpe.library.repository.BookRepositoryInterface;

import edu.sjsu.cmpe.library.dto.BookDto;
import edu.sjsu.cmpe.library.dto.BooksDto;
import edu.sjsu.cmpe.library.dto.LinkDto;
import edu.sjsu.cmpe.library.dto.LinksDto;
import edu.sjsu.cmpe.library.domain.Author;
import edu.sjsu.cmpe.library.domain.Book;
import edu.sjsu.cmpe.library.domain.Review;

@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Path("/v1/books")
public class BookResource 
{
	//constructor
	public BookResource() 
	{
	
	}


	@GET
	@Timed(name = "list-book")
	public BooksDto getBooks() 
	{
		BooksDto booksListResponse = new BooksDto();
		for(Iterator<Book> i = BooksDto.getBooks().iterator(); i.hasNext(); ) 
		{
			Book iBook = i.next();
			booksListResponse.addLink(new LinkDto("view-book", "/books/" + iBook.getIsbn(), "GET"));
			booksListResponse.addLink(new LinkDto("update-book", "/books/" + iBook.getIsbn(), "PUT"));
			booksListResponse.addLink(new LinkDto("delete-book", "/books/" + iBook.getIsbn(), "DELETE"));
			booksListResponse.addLink(new LinkDto("create-review", "/books/" + iBook.getIsbn() + "/reviews", "POST"));
		}
		return booksListResponse;
	}//public BooksDto getBooks()


    //Posts a new book.
	@POST
	@Timed(name="create-book")
	public BooksDto createBook(@Valid Book bookToCreate)
	{
		BooksDto bookCreatedResponse = new BooksDto();
		Book bookSavedResponse = BooksDto.addBookToStorage(bookToCreate);
		if(bookSavedResponse.getIsbn() >0)
		{
			bookCreatedResponse.addLink(new LinkDto("view-book", "/books/" + bookSavedResponse.getIsbn(), "GET"));
			bookCreatedResponse.addLink(new LinkDto("update-book", "/books/" + bookSavedResponse.getIsbn(), "PUT"));
			bookCreatedResponse.addLink(new LinkDto("delete-book", "/books/" + bookSavedResponse.getIsbn(), "DELETE"));
			bookCreatedResponse.addLink(new LinkDto("create-review", "/books/" + bookSavedResponse.getIsbn() + "/reviews/", "POST"));
		}
		return bookCreatedResponse;		
	}// end public BooksDto createBook	
		

	//Gets one book by isbn.
	@GET
	@Path("/{isbn}")
	@Timed(name = "view-book")
	public BookDto getBookByIsbn(@PathParam("isbn") int isbn) 
	{
		BookDto bookResponse = new BookDto(BooksDto.getBookByISBN(isbn));
		bookResponse.addLink(new LinkDto("view-book", "/books/" + bookResponse.getBook().getIsbn(), "GET"));
		bookResponse.addLink(new LinkDto("update-book", "/books/" + bookResponse.getBook().getIsbn(), "PUT"));
		bookResponse.addLink(new LinkDto("delete-book", "/books/" +  bookResponse.getBook().getIsbn(), "DELETE"));
		bookResponse.addLink(new LinkDto("create-review", "/books/" +  bookResponse.getBook().getIsbn() + "/reviews/", "POST"));
		if(bookResponse.getBook().getBookRating()!= null && !bookResponse.getBook().getBookRating().isEmpty())
		{
			bookResponse.addLink(new LinkDto("view-all-reviews", "/books/" +  bookResponse.getBook().getIsbn() + "/reviews/", "GET"));
		}
		return bookResponse;
	}// end public BookDto getBookByIsbn


	
	//deletes a book by isbn
	@DELETE
	@Path("/{isbn}")
	@Timed(name = "delete-book")
	public BooksDto deleteBookByIsbn(@PathParam("isbn") int isbn)
	{
		BooksDto bookDeleteResponse = new BooksDto();
		BooksDto.deleteBookByISBN(isbn);
		bookDeleteResponse.addLink(new LinkDto("create-book", "/books", "POST"));
		return bookDeleteResponse;
	}//end BooksDto deleteBookByIsbn
	

	//updates a book base on isbn
	@PUT
	@Path("/{isbn}")
	@Timed(name = "update-book")
	public BookDto updateookByIsbn(@PathParam("isbn") int isbn,@QueryParam("status") String status)
	{
		BookDto updatedBookResponse = new BookDto(BooksDto.updateBookByISBN(isbn, status));
		updatedBookResponse.addLink(new LinkDto("view-book", "/books/" + updatedBookResponse.getBook().getIsbn(), "GET"));
		updatedBookResponse.addLink(new LinkDto("update-book", "/books/" + updatedBookResponse.getBook().getIsbn(), "PUT"));
		updatedBookResponse.addLink(new LinkDto("delete-book", "/books/" + updatedBookResponse.getBook().getIsbn(), "DELETE"));
		updatedBookResponse.addLink(new LinkDto("create-reviews", "/books/" + updatedBookResponse.getBook().getIsbn() + "/reviews/", "POST"));
		if(updatedBookResponse.getBook().getBookRating()!= null && !updatedBookResponse.getBook().getBookRating().isEmpty())
		{
			updatedBookResponse.addLink(new LinkDto("create-reviews", "/books/" + updatedBookResponse.getBook().getIsbn() + "/reviews/", "GET"));
		}
		return updatedBookResponse;
	}// end public BookDto updateookByIsbn


	@POST
	@Path("/{isbn}/reviews")
	@Timed(name = "create-review")
	public BooksDto createBookReviews(@PathParam("isbn") int isbn,@Valid Review review) 
	{
		Review reviewResponse =  BooksDto.createReviews(isbn, review);		
		BooksDto bookCreateReviewResponse = new BooksDto();
		bookCreateReviewResponse.addLink(new LinkDto("view-review", "/books/"+isbn+"/reviews/"+reviewResponse.getId(), "GET"));
		return bookCreateReviewResponse;
	}


	
	
	// gets a review of a book based on isbn
    @GET
    @Path("/{isbn}/reviews")
    @Timed(name = "view-book")
    public List<Review> getBookReviews(@PathParam("isbn") int isbn) {
    	return BooksDto.getReviews(isbn);
    }
	
	
    @GET
    @Path("/{isbn}/reviews/{id}")
    @Timed(name = "view-book")
    public Review getBookReviewsByIsbn(@PathParam("isbn") int isbn, @PathParam("id") int reviewId) {
    	return BooksDto.getReviewsByISBN(isbn, reviewId);   	
    }
	
	
    @GET
    @Path("/{isbn}/authors")
    @Timed(name = "view-book")
    public List<Author> getBookAuthors(@PathParam("isbn") int isbn) {
    	return BooksDto.getAuthors(isbn);
    }


    @GET
    @Path("/{isbn}/authors/{id}")
    @Timed(name = "view-book")
    public Author getBookAuthorsByIsbn(@PathParam("isbn") int isbn, @PathParam("id") int authId) {
    	return BooksDto.getAuthorsByISBN(isbn, authId);   	
    }
    
} //end class BookResource
