package com.kms.chatters.common.utils;

import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PagingUtils {

    @Autowired
    StringUtils strUtils;

    public HashMap<String, Object> addPagingPropsToMap(HashMap<String, Object> map) {

        int targetPage = 1; // 타겟 페이지
        int pageSize = 10; // 페이지에 출력할 리스트 갯수
        int pageCount = 10; // 한 블록 당 페이지 갯수 (ex :  < 1 2 3 4 5 6 7 8 9 10 > )
        int totalCount = 0; // 총 로우 갯수 
        int startPage = 1, endPage = pageCount; // 시작, 끝 페이지
        int offset; //limit offset (시작 로우 index)
        
        if(!strUtils.isEmpty(map.get("targetPage").toString())){
            targetPage = Integer.parseInt(map.get("targetPage").toString());
        }

        if(!strUtils.isEmpty(map.get("pageSize").toString())){
            pageSize = Integer.parseInt(map.get("pageSize").toString());
        }

        if(!strUtils.isEmpty(map.get("pageCount").toString())){
            pageCount = Integer.parseInt(map.get("pageCount").toString());
        }

        if(!strUtils.isEmpty(map.get("totalCount").toString())){
            totalCount = Integer.parseInt(map.get("totalCount").toString());
        }

        int totalPageCount = (totalCount/pageSize); //총 페이지 갯수
        if(totalCount % pageSize > 0){
            totalPageCount++;
        }

        // 시작페이지 = ((요청한 페이지 - 1) / 블록 당 페이지 갯수) * 블록 당 페이지 갯수 + 1
        startPage = ((targetPage - 1) / pageCount) * pageCount + 1;
        // 끝 페이지 = 시작 페이지 + 블록 당 페이지 갯수 - 1
        endPage = startPage + pageCount - 1;
        //마지막 페이지가 총 페이지 갯수보다 클 경우 총 페이지 갯수로 고정시킴.
        if(endPage > totalPageCount){
            endPage = totalPageCount;
        }

        //mariadb limit offset
        offset = (targetPage - 1) * pageSize;

        map.put("totalPageCount", totalPageCount);
        map.put("startPage", startPage);
        map.put("endPage", endPage);
        map.put("offset", offset);

        return map;
    }
}