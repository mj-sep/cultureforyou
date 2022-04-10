package com.example.cultureforyou;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

public class LikeListActivity extends AppCompatActivity {

    static final String[] LIST_MENU = {"LIST1", "LIST2", "LIST3"} ; // 리스트뷰 데이터 예시

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.like_list);

        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, LIST_MENU);

        ListView listview = (ListView) findViewById(R.id.like_listView);
        listview.setAdapter(adapter);
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//
//        getMenuInflater().inflate(R.menu.action_items_main, menu);
//
//        MenuItem searchItem = menu.findItem(R.id.like_listView);
//        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
//        searchView.setQueryHint("검색어를 입력하세요.");
//
//        // SearchView 입력 글자색과 힌트 색상 변경하기
//        SearchView.SearchAutoComplete searchAutoComplete = (SearchView.SearchAutoComplete)searchView.findViewById(android.support.v7.appcompat.R.id.search_src_text);
//        searchAutoComplete.setHintTextColor(Color.GRAY);
//        searchAutoComplete.setTextColor(Color.WHITE);
//
//        return super.onCreateOptionsMenu(menu);
//    }
}