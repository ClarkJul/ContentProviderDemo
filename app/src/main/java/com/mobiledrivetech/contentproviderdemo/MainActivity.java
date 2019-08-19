package com.mobiledrivetech.contentproviderdemo;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private BooksContentProvider mBookContentProvider;
    private TextView mTableInfo;
    private ContentResolver mContentResolver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.v("MainActivity", "onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button cpAdd = (Button) findViewById(R.id.provider_add);
        Button cpDelete = (Button) findViewById(R.id.provider_delete);
        Button cpUpdate = (Button) findViewById(R.id.provider_update);
        Button cpQuery = (Button) findViewById(R.id.provider_query);

        mTableInfo = (TextView) findViewById(R.id.table_info);

        cpAdd.setOnClickListener(this);
        cpDelete.setOnClickListener(this);
        cpQuery.setOnClickListener(this);
        cpUpdate.setOnClickListener(this);
        mContentResolver = getContentResolver();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {case R.id.provider_add:
            String[] bookNames = new String[]{"Chinese", "Math", "English", "Sports"};
            String[] bookPublishers = new String[]{"XinHua", "GongXin", "DianZi", "YouDian"};
            for (int i = 0; i < bookNames.length; i++) {
                ContentValues values = new ContentValues();
                values.put(IProviderMetaData.BookTableMetaData.BOOK_NAME, bookNames[i]);
                values.put(IProviderMetaData.BookTableMetaData.BOOK_PUBLISHER, bookPublishers[i]);
                mContentResolver.insert(IProviderMetaData.BookTableMetaData.CONTENT_URI, values);
            }
            break;
            case R.id.provider_delete:
                String bookId = "1";
                if (!"".equals(bookId)) {
                    ContentValues values1 = new ContentValues();
                    values1.put(IProviderMetaData.BookTableMetaData.BOOK_ID,
                            bookId);
                    mContentResolver.delete(
                            Uri.withAppendedPath(
                                    IProviderMetaData.BookTableMetaData.CONTENT_URI,
                                    bookId
                            ), "_id = ?",
                            new String[]{bookId}
                    );
                } else {
                    mContentResolver.delete(
                            IProviderMetaData.BookTableMetaData.CONTENT_URI,
                            null,
                            null
                    );
                }
                break;
            case R.id.provider_query:
                Cursor cursor = mContentResolver.query(IProviderMetaData.BookTableMetaData.CONTENT_URI, null, null, null, null);
                String text = "";
                if (cursor != null) {
                    while (cursor.moveToNext()) {
                        String bookIdText =
                                cursor.getString(cursor.getColumnIndex(IProviderMetaData.BookTableMetaData.BOOK_ID));
                        String bookNameText =
                                cursor.getString(cursor.getColumnIndex(IProviderMetaData.BookTableMetaData.BOOK_NAME));
                        String bookPublisherText =
                                cursor.getString(cursor.getColumnIndex(IProviderMetaData.BookTableMetaData.BOOK_PUBLISHER));
                        text += "id = " + bookIdText + ",name = " + bookNameText + ",publisher = " + bookPublisherText + "\n";
                    }
                    cursor.close();
                    mTableInfo.setText(text);
                }
                break;
            case R.id.provider_update:
                String bookId1 = "2";
                String bookName = "Art";
                String bookPublisher = "TieDao";
                ContentValues values2 = new ContentValues();
                values2.put(IProviderMetaData.BookTableMetaData.BOOK_NAME,
                        bookName);
                values2.put(IProviderMetaData.BookTableMetaData.BOOK_PUBLISHER,
                        bookPublisher);
                if ("".equals(bookId1)) {
                    mContentResolver.update(
                            IProviderMetaData.BookTableMetaData.CONTENT_URI,
                            values2, null, null);
                } else {
                    mContentResolver.update(
                            Uri.withAppendedPath(
                                    IProviderMetaData.BookTableMetaData.CONTENT_URI, bookId1),
                            values2, "_id = ? ", new String[]{bookId1});
                }
                break;default:
                Log.v("MainActivity", "default");
                break;
        }
    }
}
