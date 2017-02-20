/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bari.jersey.service;

import com.bari.jersey.bean.Country;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author User
 */
public class CountryService {
    DBCon data = new DBCon();

    static HashMap<Integer, Country> countryIdMap = getCountryIdMap();

   

    public List<Map<String, String>> getAllCountries() {
        List<Map<String, String>> list = new ArrayList<Map<String, String>>();
        try {
          Connection con = data.getConnect();
          String sql = "select * from countries";
            
          PreparedStatement pst = con.prepareStatement(sql);
          
            ResultSet rs = pst.executeQuery();
 
            ResultSetMetaData meta = rs.getMetaData();
            while(rs.next()){
            Map map = new HashMap();
            for(int i=0; i<=meta.getColumnCount(); i++){
            String key = meta.getColumnName(i);
            String value = rs.getString(key);
            map.put(key, value);
            } 
            list.add(map);
            }
            System.out.println(list);  
        } catch (Exception e) {
            e.printStackTrace();
           
        }
        return list;
    }

    public Country getCountry(int id)  {
        Country country = countryIdMap.get(id);

        if (country == null) {
            throw new CountryNotFoundException("Country with id " + id + " not found");
        }
        return country;
    }

    public Country addCountry(Country country) {
        try {
            Connection con = data.getConnect();
            String sql = "insert into countries(countryName, population) values(?,?)";
             
            PreparedStatement pst = con.prepareStatement(sql);
             pst.setString(1, country.getCountryName());
             pst.setLong(2, country.getPopulation());
            pst.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return country;
    }

    public Country updateCountry(Country country) {
        if (country.getId() <= 0) {
            return null;
        }
        countryIdMap.put(country.getId(), country);
        return country;

    }

    public void deleteCountry(int id) {
        countryIdMap.remove(id);
    }

    public static HashMap<Integer, Country> getCountryIdMap() {
        return countryIdMap;
    }

    // Utility method to get max id
    public static int getMaxId() {
        int max = 0;
        for (int id : countryIdMap.keySet()) {
            if (max <= id) {
                max = id;
            }

        }
        return max;
    }
  
}
