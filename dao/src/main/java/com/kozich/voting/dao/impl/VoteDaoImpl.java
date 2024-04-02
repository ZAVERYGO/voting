package com.kozich.voting.dao.impl;

import com.kozich.voting.Entity.VoteEntity;

import com.kozich.voting.dao.api.VoteDao;
import com.kozich.voting.dao.factory.DaoFactory;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import java.util.*;

public class VoteDaoImpl implements VoteDao {

    private final static String GET_LIST = "SELECT id, dt_create, artist, about FROM app.vote";
    private final static String GET_BY_ID = GET_LIST + " WHERE id = ";

    @Override
    public List<VoteEntity> get() {
        List<VoteEntity> data = new ArrayList<>();
        try(Connection conn = DaoFactory.getConnection();
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(GET_LIST);
        ){
            while (rs.next()) {
                VoteEntity entity = new VoteEntity();
                entity.setId(rs.getLong("id"));
                entity.setArtistId(rs.getLong("artist_id"));
                entity.setAbout(rs.getString("about"));
                data.add(entity);
            }
        } catch (SQLException e){
            throw new IllegalStateException("Ошибка выполнения запроса к БД", e);
        }

        return data;
    }
    @Override
    public Optional<VoteEntity> get(long id) {
        try(Connection conn = DaoFactory.getConnection();
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(GET_BY_ID + id);
        ){
            VoteEntity entity = null;
            while (rs.next()) {
                if(entity == null){
                    entity = new VoteEntity();
                    entity.setId(rs.getLong("id"));
                    entity.setArtistId(rs.getLong("artist_id"));
                    entity.setAbout(rs.getString("about"));
                } else {
                    throw new IllegalStateException("В запросе получилось больше одного значения");
                }
            }

            if(entity == null){
                return Optional.empty();
            } else {
                return Optional.of(entity);
            }
        } catch (SQLException e){
            throw new IllegalStateException("Ошибка выполнения запроса к БД", e);
        }
    }
}
