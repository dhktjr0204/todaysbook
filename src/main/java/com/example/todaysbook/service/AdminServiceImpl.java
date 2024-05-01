package com.example.todaysbook.service;

import com.example.todaysbook.domain.dto.AdminUserDto;
import com.example.todaysbook.domain.dto.BookDto;
import com.example.todaysbook.domain.entity.Book;
import com.example.todaysbook.domain.entity.User;
import com.example.todaysbook.repository.BookRepository;
import com.example.todaysbook.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {
    private final UserRepository userRepository;
    private final BookRepository bookRepository;

    @Override
    public Page<AdminUserDto> findAllUser(Pageable pageable) {
        Page<User> userList = userRepository.findAll(pageable);
        return userList.map(this::convertUserToDto);
    }

    @Override
    public Page<AdminUserDto> findUsersByKeyword(String keyword, Pageable pageable) {
        Page<User> userList = userRepository
                .findByEmailContainingOrNickNameContaining(keyword, keyword, pageable);

        if(userList.isEmpty()){
            return Page.empty();
        }

        return userList.map(this::convertUserToDto);
    }

    @Override
    public Page<BookDto> findAllBook(Pageable pageable) {
        Page<Book> bookList = bookRepository.findAll(pageable);

        return bookList.map(this::convertBookToDto);
    }

    @Override
    public Page<BookDto> findBooksByKeyword(String keyword, Pageable pageable) {
        Page<Book> bookList = bookRepository.findByAuthorContainingOrTitleContaining(keyword, keyword, pageable);

        if(bookList.isEmpty()){
            return Page.empty();
        }

        return bookList.map(this::convertBookToDto);
    }

    private AdminUserDto convertUserToDto(User user){
        return AdminUserDto.builder()
                .id(user.getId())
                .email(user.getEmail())
                .nickName(user.getNickName())
                .grade(user.getRole())
                .build();
    }

    private BookDto convertBookToDto(Book book){
        return BookDto.builder()
                .id(book.getId())
                .title(book.getTitle())
                .price(book.getPrice())
                .stock(book.getStock())
                .build();
    }
}
