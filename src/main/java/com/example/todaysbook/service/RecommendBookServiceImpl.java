package com.example.todaysbook.service;

import com.example.todaysbook.domain.dto.FavoriteBookDTO;
import com.example.todaysbook.domain.dto.RecommendBookDto;
import com.example.todaysbook.domain.dto.ReviewRequestDto;
import com.example.todaysbook.domain.dto.SimpleReview;
import com.example.todaysbook.repository.FavoriteBookRepository;
import com.example.todaysbook.repository.RecommendBookMapper;
import com.example.todaysbook.repository.ReviewMapper;
import lombok.RequiredArgsConstructor;
import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.impl.common.LongPrimitiveIterator;
import org.apache.mahout.cf.taste.impl.model.file.FileDataModel;
import org.apache.mahout.cf.taste.impl.recommender.GenericItemBasedRecommender;
import org.apache.mahout.cf.taste.impl.similarity.TanimotoCoefficientSimilarity;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.recommender.RecommendedItem;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class RecommendBookServiceImpl implements RecommendBookService {

    private final RecommendBookMapper recommendBookMapper;
    private final ReviewMapper reviewMapper;
    @Override
    public void GenerateRecommendBookList(List<SimpleReview> reviews) throws TasteException, IOException {


        String filePath ="/home/ubuntu/app/src/main/resources/data/rating.csv";

        int howMany = 5;

        fileWrite(filePath, reviews);

        recommendBookMapper.truncateRecommendBook();
        recommendBookMapper.insertRecommendBookInfo(makeRecommendList(filePath, howMany));
    }

    @Override
    public List<RecommendBookDto> getRecommendBooks(Long bookId) {

        return recommendBookMapper.getRecommendBooks(bookId);
    }

    @Override
    public List<RecommendBookDto> getRecommendBooksByFavoriteBooks(Long userId) {

        return recommendBookMapper.getRecommendBooksByFavoriteBooks(userId);
    }

    private void fileWrite(String filePath, List<SimpleReview> reviews) throws IOException {

        BufferedWriter bw = new BufferedWriter(new FileWriter(filePath, false));

        for(SimpleReview review : reviews) {

            bw.write(review.getUserId()+",");
            bw.write(review.getBookId()+",");
            bw.write(String.valueOf(review.getScore()));
            bw.write("\r\n");
        }

        bw.close();
    }

    private List<Map<String, Object>> makeRecommendList(String filePath, int howMany) throws IOException, TasteException {

        File dataFile = new File(filePath);
        DataModel model = new FileDataModel(dataFile);

        TanimotoCoefficientSimilarity similarity = new TanimotoCoefficientSimilarity(model);
        GenericItemBasedRecommender recommender = new GenericItemBasedRecommender(model, similarity);

        List<Map<String, Object>> items = new ArrayList<>();

        LongPrimitiveIterator itemIDs = model.getItemIDs();
        while (itemIDs.hasNext()) {
            long currentItemID = itemIDs.nextLong();

            List<RecommendedItem> recommendedItems = recommender.mostSimilarItems(currentItemID, howMany);

            for (RecommendedItem recommendedItem : recommendedItems) {
                Map<String, Object> item = new HashMap<>();

                item.put("bookId", currentItemID);
                item.put("recommendBookId", recommendedItem.getItemID());
                item.put("similarityScore", recommendedItem.getValue());

                items.add(item);
            }
        }

        return items;
    }

    @Scheduled(cron = "0 0 0/3 * * ?")
    private void ScheduledGenerateRecommendBookList() throws TasteException, IOException {

        String filePath ="/home/ubuntu/app/src/main/resources/data/rating.csv";

        int howMany = 5;

        List<SimpleReview> reviews = reviewMapper.getSimpleReviews();

        fileWrite(filePath, reviews);

        recommendBookMapper.truncateRecommendBook();
        recommendBookMapper.insertRecommendBookInfo(makeRecommendList(filePath, howMany));
    }
}
