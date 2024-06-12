package com.example.todaysbook.service;

import com.example.todaysbook.domain.dto.BookDto;
import com.example.todaysbook.domain.dto.RecommendListDetailWithBookMarkDto;
import com.example.todaysbook.domain.dto.RecommendListWithBookMarkDto;
import com.example.todaysbook.domain.dto.RecommendListCreateRequestDto;
import com.example.todaysbook.domain.dto.RecommendListUpdateRequestDto;
import com.example.todaysbook.domain.dto.RecommendListDetailDto;
import com.example.todaysbook.domain.dto.RecommendListDto;
import com.example.todaysbook.domain.entity.UserRecommendBook;
import com.example.todaysbook.domain.entity.UserRecommendList;
import com.example.todaysbook.exception.recommendList.BookNotFoundException;
import com.example.todaysbook.exception.recommendList.RecommendListNotFoundException;
import com.example.todaysbook.exception.user.UserValidateException;
import com.example.todaysbook.repository.RecommendListMapper;
import com.example.todaysbook.repository.UserRecommendBookRepository;
import com.example.todaysbook.repository.UserRecommendListRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class RecommendListServiceImpl implements RecommendListService {
    private final UserRecommendListRepository userRecommendListRepository;
    private final UserRecommendBookRepository userRecommendBookRepository;
    private final RecommendListMapper recommendListMapper;

    @Override
    public RecommendListDetailDto getRecommendListDetail(Long listId) {
        RecommendListDto recommendList = recommendListMapper.getRecommendListByListId(listId);
        List<BookDto> bookList = recommendListMapper.getBookDetailByListId(listId);

        return RecommendListDetailDto.builder()
                .listId(recommendList.getListId())
                .listTitle(recommendList.getListTitle())
                .userId(recommendList.getUserId())
                .nickname(recommendList.getNickname())
                .date(recommendList.getDate())
                .bookList(bookList).build();
    }

    @Override
    public List<RecommendListDetailDto> getMyRecommendListAll(Long userId) {

        List<RecommendListDto> recommendLists = recommendListMapper.getMyRecommendListAllByUserId(userId);

        return convertListToDetailDto(recommendLists);
    }

    @Override
    public List<RecommendListDetailWithBookMarkDto> getRandomRecommendList(Long userId) {
        List<RecommendListWithBookMarkDto> recommendLists = recommendListMapper.getRandomRecommendList(userId);

        return convertListToDetailWithBookMarkDto(recommendLists);
    }

    @Override
    public List<RecommendListDetailDto> getMyBookMarkListAll(Long userId) {
        List<RecommendListDto> recommendLists = recommendListMapper.getMyBookMarkListByUserId(userId);

        return convertListToDetailDto(recommendLists);
    }

    @Transactional
    @Override
    public UserRecommendList save(Long userId, RecommendListCreateRequestDto request) {

        UserRecommendList userRecommendList = UserRecommendList.builder()
                .title(request.getTitle())
                .userId(userId)
                .build();

        UserRecommendList saveUserRecommendList = userRecommendListRepository.save(userRecommendList);

        for(int i=0; i<request.getBookIdList().size();i++){
            UserRecommendBook userRecommendBook = UserRecommendBook.builder()
                    .userRecommendListId(saveUserRecommendList.getId())
                    .bookId(request.getBookIdList().get(i))
                    .order(i+1)
                    .build();

            userRecommendBookRepository.save(userRecommendBook);
        }

        return saveUserRecommendList;
    }

    @Transactional
    @Override
    public void update(Long listId, RecommendListUpdateRequestDto request) {
        UserRecommendList userRecommendList = userRecommendListRepository.findById(listId)
                .orElseThrow(RecommendListNotFoundException::new);
        List<UserRecommendBook> userRecommendBooks = userRecommendBookRepository.findByUserRecommendListIdOrderByOrder(listId)
                .orElseThrow(BookNotFoundException::new);

        List<Long> existingBookIds= userRecommendBooks.stream().map(UserRecommendBook::getBookId).collect(Collectors.toList());

        //list 만든 사용자 id와 응답으로 받은 사용자 id(현재 로그인된 유저)가 다를 때
        if(request.getUserId()==null || userRecommendList.getUserId()!=request.getUserId()){
            throw new UserValidateException();
        }

        int lastOrder = userRecommendBooks.get(userRecommendBooks.size()-1).getOrder();

        //리스트에 새로 추가할거 추가하고 수정할 거 수정하기
        if(request.getNewBookList()!=null){
            for (int i = 0; i < request.getNewBookList().size(); i++) {
                long bookId = request.getNewBookList().get(i);

                //기존 책인데 순서만 바뀌었다면 순서만 업데이트
                if (existingBookIds.contains(bookId)) {
                    int bookIndex = existingBookIds.indexOf(bookId);
                    UserRecommendBook userRecommendBook = userRecommendBooks.get(bookIndex);
                    userRecommendBook.updateOrder(lastOrder + i + 1);
                } else {
                    UserRecommendBook book = UserRecommendBook.builder()
                            .userRecommendListId(listId)
                            .bookId(bookId)
                            .order(lastOrder + i + 1)
                            .build();

                    userRecommendBookRepository.save(book);
                }
            }
        }

        //삭제될 책들 삭제
        if(request.getNewBookList()!=null){
            for (Long deleteBookId : request.getDeleteBookList()) {
                userRecommendBookRepository.deleteByBookIdAndUserRecommendListId(deleteBookId, listId);
            }
        }

        //제목 수정
        userRecommendList.updateTitle(request.getTitle());
    }

    @Transactional
    @Override
    public void delete(Long listId) {
        UserRecommendList userRecommendList = userRecommendListRepository.findById(listId)
                .orElseThrow(RecommendListNotFoundException::new);

        userRecommendListRepository.delete(userRecommendList);
    }


    private List<RecommendListDetailDto> convertListToDetailDto(List<RecommendListDto> recommendLists){
        List<RecommendListDetailDto> result = new ArrayList<>();

        for(RecommendListDto recommendList:recommendLists){
            List<BookDto> bookList = recommendListMapper.getBookDetailByListId(recommendList.getListId());
            RecommendListDetailDto build = RecommendListDetailDto.builder()
                    .listId(recommendList.getListId())
                    .listTitle(recommendList.getListTitle())
                    .userId(recommendList.getUserId())
                    .nickname(recommendList.getNickname())
                    .date(recommendList.getDate())
                    .bookList(bookList).build();

            result.add(build);
        }
        return result;
    }

    private List<RecommendListDetailWithBookMarkDto> convertListToDetailWithBookMarkDto(List<RecommendListWithBookMarkDto> recommendLists){
        List<RecommendListDetailWithBookMarkDto> result=new ArrayList<>();

        for(RecommendListWithBookMarkDto list:recommendLists){
            List<BookDto> bookList = recommendListMapper.getBookDetailByListId(list.getListId());

            RecommendListDetailWithBookMarkDto build = RecommendListDetailWithBookMarkDto.builder()
                    .listId(list.getListId())
                    .listTitle(list.getListTitle())
                    .userId(list.getUserId())
                    .nickname(list.getNickname())
                    .date(list.getDate())
                    .isBookmarked(list.getIsBookMarked())
                    .bookList(bookList).build();

            result.add(build);
        }
        return result;
    }
}
