package com.techelevator.dao;

import com.techelevator.model.Card;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.BadSqlGrammarException;
import org.springframework.jdbc.CannotGetJdbcConnectionException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import java.util.ArrayList;
import java.util.List;

//Define this class as an Java Database Connection implementation of a CardDao
@Component
public class JdbcCardDao implements CardDao {
    private final JdbcTemplate jdbcTemplate;

    public JdbcCardDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Card getCardById(String cardID) {
        // query command to select card using an exact cardID using parameterized input
        String sql = "SELECT card_id,tcg_id, card_title, card_small_image_url,card_normal_image_url, card_details_url " +
                "FROM cards WHERE card_id = ?;";
        // queried card initially set to null in case no results are returned
        Card queriedCard = null;
        // setting up jdbc call inside of a try block to catch any potential errors
        try {
            // send SQL command and return the results as a SQL Row Set
            SqlRowSet results = jdbcTemplate.queryForRowSet(sql, cardID);
            // if there is a RowSet returned...
            if (results.next()) {
                // use helper method to map sql row to card object
                queriedCard = mapResultsToCard(results);
            }
        } catch (CannotGetJdbcConnectionException e) {
            // catch any database connection errors and throw a new error to be caught at next level
            throw new RuntimeException("Unable to connect to the database!", e);
        } catch (BadSqlGrammarException e) {
            // catch any SQL command errors and throw a new error to be caught at next level
            throw new RuntimeException("Bad SQL grammar: " + e.getSql() + "\n" + e.getSQLException(), e);
        } catch (DataIntegrityViolationException e) {
            // catch any database connection errors and throw a new error to be caught at next level
            throw new RuntimeException("Database Integrity Violation!", e);
        }
        //if there was a valid result, this returns a card object with data otherwise a null is returned
        return queriedCard;
    }
    //ToDO remove this method in subsequent sprint
//    @Override
//    public Card getCardByTitle(String cardTitle) {
//        // query command to select card using an exact card title using parameterized input
//        String sql = "SELECT card_id,tcg_id, card_title,card_small_image_url,card_normal_image_url, card_details_url " +
//                "FROM cards WHERE card_title = ?;";
//        // queried card initially set to null in case no results are returned
//        Card queriedCard = null;
//        // setting up jdbc call inside of a try block to catch any potential errors
//        try {
//            // send SQL command and return the results as a SQL Row Set
//            SqlRowSet results = jdbcTemplate.queryForRowSet(sql, cardTitle);
//            // if there is a RowSet returned...
//            if (results.next()) {
//                // use helper method to map sql row to card object
//                queriedCard = mapResultsToCard(results);
//            }
//        } catch (CannotGetJdbcConnectionException e) {
//            // catch any database connection errors and throw a new error to be caught at next level
//            throw new RuntimeException("Unable to connect to the database!", e);
//        } catch (BadSqlGrammarException e) {
//            // catch any SQL command errors and throw a new error to be caught at next level
//            throw new RuntimeException("Bad SQL grammar: " + e.getSql() + "\n" + e.getSQLException(), e);
//        } catch (DataIntegrityViolationException e) {
//            // catch any database connection errors and throw a new error to be caught at next level
//            throw new RuntimeException("Database Integrity Violation!", e);
//        }
//        //if there was a valid result, this returns a card object with data otherwise a null is returned
//        return queriedCard;
//    }

    @Override
    public List<Card> getCardsByTitle(String cardTitle, boolean isExactMatch) {
        // Queried Cards set to empty list
        List<Card> queriedCards = new ArrayList<>();
        String parameter = cardTitle;
        // query command to select card matches using a card title as parameterized input
        String sql = "SELECT card_id,tcg_id, card_title, card_small_image_url,card_normal_image_url, card_details_url " +
                "FROM cards WHERE card_title LIKE ?;";

        if(!isExactMatch){
            parameter = "%"+ cardTitle+"%";
            // query command to select card matches using a card title as parameterized input
            sql = "SELECT card_id,tcg_id, card_title, card_small_image_url,card_normal_image_url, card_details_url " +
                    "FROM cards WHERE card_title ILIKE ?;";
        }

        try{
            // send SQL command and return the results as a SQL Row Set
            SqlRowSet results = jdbcTemplate.queryForRowSet(sql,parameter);
            // while there are results
            while(results.next()){
                // add the mapped results to the list
                queriedCards.add(mapResultsToCard(results));
            }
        }catch (CannotGetJdbcConnectionException e) {
            // catch any database connection errors and throw a new error to be caught at next level
            throw new RuntimeException("Unable to connect to the database!", e);
        }catch (BadSqlGrammarException e) {
            // catch any SQL command errors and throw a new error to be caught at next level
            throw new RuntimeException("Bad SQL grammar: " + e.getSql() + "\n" + e.getSQLException(), e);
        }catch (DataIntegrityViolationException e) {
            // catch any database connection errors and throw a new error to be caught at next level
            throw new RuntimeException("Database Integrity Violation!", e);
        }
        //if there were valid results, this returns a list of card object with data otherwise an empty list is returned
        return queriedCards;
    }


    /**
     * Method to get cards by color
     * Search using preset option of Strings (Red, Black, White, Green, Blue)
     * Must be an exact match as options will not be user entered. ILIKE used for cases where cards have multiple colors
     * Stored in color object in API using Upper Case single letter abbreviations.
     * W - White | U - Blue | B - Black | R - Red | G - Green
     */

    @Override
    public List<Card> getCardsByColor(String cardColor) {
        List<Card> cardsOfSelectedColor = new ArrayList<>();
        String sql = "SELECT card_id,tcg_id, card_title, card_small_image_url,card_normal_image_url, " +
                "card_details_url, card_reverse_image_url, card_colors, card_color_identity, card_set_id, card_set_code, " +
                "card_set_name, card_details_url, card_collector_number, card_legalities, card_layout, card_cmc, card_edhrec_rank" +
                "FROM cards WHERE card_colors ILIKE ?;";
        String parameter = "%"+cardColor+"%";

        try{
            // send SQL command and return the results as a SQL Row Set
            SqlRowSet results = jdbcTemplate.queryForRowSet(sql,parameter);
            // while there are results
            while(results.next()){
                // add the mapped results to the list
                cardsOfSelectedColor.add(mapResultsToCard(results));
            }
        }catch (CannotGetJdbcConnectionException e) {
            // catch any database connection errors and throw a new error to be caught at next level
            throw new RuntimeException("Unable to connect to the database!", e);
        }catch (BadSqlGrammarException e) {
            // catch any SQL command errors and throw a new error to be caught at next level
            throw new RuntimeException("Bad SQL grammar: " + e.getSql() + "\n" + e.getSQLException(), e);
        }catch (DataIntegrityViolationException e) {
            // catch any database connection errors and throw a new error to be caught at next level
            throw new RuntimeException("Database Integrity Violation!", e);
        }
        //if there were valid results, this returns a list of card object with data otherwise an empty list is returned
        return cardsOfSelectedColor;
    }

    @Override
    public List<Card> getCardsByColorIdentity(String cardColorIdentity) {
        List<Card> cardsOfSelectedColor = new ArrayList<>();
        String sql = "SELECT card_id,tcg_id, card_title, card_small_image_url,card_normal_image_url, " +
                "card_details_url, card_reverse_image_url, card_colors, card_color_identity, card_set_id, card_set_code, " +
                "card_set_name, card_details_url, card_collector_number, card_legalities, card_layout, card_cmc, card_edhrec_rank" +
                "FROM cards WHERE card_colors_identity ILIKE ?;";
        String parameter = "%"+cardColorIdentity+"%";

        try{
            // send SQL command and return the results as a SQL Row Set
            SqlRowSet results = jdbcTemplate.queryForRowSet(sql,parameter);
            // while there are results
            while(results.next()){
                // add the mapped results to the list
                cardsOfSelectedColor.add(mapResultsToCard(results));
            }
        }catch (CannotGetJdbcConnectionException e) {
            // catch any database connection errors and throw a new error to be caught at next level
            throw new RuntimeException("Unable to connect to the database!", e);
        }catch (BadSqlGrammarException e) {
            // catch any SQL command errors and throw a new error to be caught at next level
            throw new RuntimeException("Bad SQL grammar: " + e.getSql() + "\n" + e.getSQLException(), e);
        }catch (DataIntegrityViolationException e) {
            // catch any database connection errors and throw a new error to be caught at next level
            throw new RuntimeException("Database Integrity Violation!", e);
        }
        //if there were valid results, this returns a list of card object with data otherwise an empty list is returned
        return cardsOfSelectedColor;
    }

    /**
     * Methods to get cards by set id and code. Set name can be displayed but does not need to be searchable.
     * Codes are three letter abbreviations of set names
     * Must be an exact match as options will be (possibly) searchable but preset.
     */

    @Override
    public List<Card> getCardsBySetCode(String setCode) {
        List<Card> cardsOfSelectedSet = new ArrayList<>();
        String sql = "SELECT card_id,tcg_id, card_title, card_small_image_url,card_normal_image_url, " +
                "card_details_url, card_reverse_image_url, card_colors, card_color_identity, card_set_id, card_set_code, " +
                "card_set_name, card_details_url, card_collector_number, card_legalities, card_layout, card_cmc, card_edhrec_rank" +
                "FROM cards WHERE card_set_code = ?;";
        String parameter = setCode;

        try{
            // send SQL command and return the results as a SQL Row Set
            SqlRowSet results = jdbcTemplate.queryForRowSet(sql,parameter);
            // while there are results
            while(results.next()){
                // add the mapped results to the list
                cardsOfSelectedSet.add(mapResultsToCard(results));
            }
        }catch (CannotGetJdbcConnectionException e) {
            // catch any database connection errors and throw a new error to be caught at next level
            throw new RuntimeException("Unable to connect to the database!", e);
        }catch (BadSqlGrammarException e) {
            // catch any SQL command errors and throw a new error to be caught at next level
            throw new RuntimeException("Bad SQL grammar: " + e.getSql() + "\n" + e.getSQLException(), e);
        }catch (DataIntegrityViolationException e) {
            // catch any database connection errors and throw a new error to be caught at next level
            throw new RuntimeException("Database Integrity Violation!", e);
        }
        //if there were valid results, this returns a list of card object with data otherwise an empty list is returned
        return cardsOfSelectedSet;
    }

    @Override
    public List<Card> getCardsBySetId(String setId) {
        List<Card> cardsOfSelectedSet = new ArrayList<>();
        String sql = "SELECT card_id,tcg_id, card_title, card_small_image_url,card_normal_image_url, " +
                "card_details_url, card_reverse_image_url, card_colors, card_color_identity, card_set_id, card_set_code, " +
                "card_set_name, card_details_url, card_collector_number, card_legalities, card_layout, card_cmc, card_edhrec_rank" +
                "FROM cards WHERE card_set_id = ?;";
        String parameter = setId;

        try{
            // send SQL command and return the results as a SQL Row Set
            SqlRowSet results = jdbcTemplate.queryForRowSet(sql,parameter);
            // while there are results
            while(results.next()){
                // add the mapped results to the list
                cardsOfSelectedSet.add(mapResultsToCard(results));
            }
        }catch (CannotGetJdbcConnectionException e) {
            // catch any database connection errors and throw a new error to be caught at next level
            throw new RuntimeException("Unable to connect to the database!", e);
        }catch (BadSqlGrammarException e) {
            // catch any SQL command errors and throw a new error to be caught at next level
            throw new RuntimeException("Bad SQL grammar: " + e.getSql() + "\n" + e.getSQLException(), e);
        }catch (DataIntegrityViolationException e) {
            // catch any database connection errors and throw a new error to be caught at next level
            throw new RuntimeException("Database Integrity Violation!", e);
        }
        //if there were valid results, this returns a list of card object with data otherwise an empty list is returned
        return cardsOfSelectedSet;
    }

    @Override
    public List<Card> getCardsBySetCode(String setCode) {
        return null;
    }

    @Override
    public Card addCard(Card cardToBeAdded) {
        // Setting initially created card
        Card createdCard = null;
        //query command to insert card into the database
        String sql = "INSERT INTO cards (card_id, tcg_id, card_title,card_small_image_url,card_normal_image_url, card_details_url) "+
                                 "VALUES(?, ?, ?, ?, ?, ?)";
    try{
        int rowsInserted = jdbcTemplate.update(sql,cardToBeAdded.getId(),
                                cardToBeAdded.getTcgId(),
                                cardToBeAdded.getName(),
                                cardToBeAdded.getSmallImgUrl(),
                                cardToBeAdded.getImageUrl(),
                                cardToBeAdded.getScryfallUrl());
        if(rowsInserted==1){
            createdCard = getCardById(cardToBeAdded.getId());
        }else{
            throw new DataIntegrityViolationException("More that 1 entry updated!");
        }
    }catch (CannotGetJdbcConnectionException e) {
        // catch any database connection errors and throw a new error to be caught at next level
        throw new RuntimeException("Unable to connect to the database!", e);
    }catch (BadSqlGrammarException e) {
        // catch any SQL command errors and throw a new error to be caught at next level
        throw new RuntimeException("Bad SQL grammar: " + e.getSql() + "\n" + e.getSQLException(), e);
    }catch (DataIntegrityViolationException e) {
        // catch any database connection errors and throw a new error to be caught at next level
        throw new RuntimeException("Database Integrity Violation!", e);
    }

        return createdCard;
    }


    @Override
    public Card mapResultsToCard(SqlRowSet results) {
        Card mappedCard = new Card();
        mappedCard.setId(results.getString("card_id"));
        mappedCard.setTcgId(results.getInt("tcg_id"));
        mappedCard.setName(results.getString("card_title"));
        mappedCard.setSmallImgUrl(results.getString("card_small_image_url"));
        mappedCard.setImageUrl(results.getString("card_normal_image_url"));
        mappedCard.setScryfallUrl(results.getString("card_details_url"));
        return mappedCard;
    }


}
