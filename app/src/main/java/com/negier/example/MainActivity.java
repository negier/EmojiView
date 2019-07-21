package com.negier.example;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;

import com.negier.emojiview.EmojiView;

public class MainActivity extends AppCompatActivity {

    private EditText mEditText;
    private EmojiView mEmojiView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
        mEditText = (EditText) findViewById(R.id.edit_text);
        mEmojiView = (EmojiView) findViewById(R.id.emoji_view);
        mEmojiView.setOutputSource(mEditText);
    }

}
