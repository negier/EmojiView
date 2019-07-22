package com.negier.emojiview;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class EmojiView extends FrameLayout implements EmojiRecyclerViewAdapter.OnItemClickListener {
    private Context context;
    private ViewPager mViewPager;
    private ViewPager.OnPageChangeListener onPageChangeListener;
    private IIndicatorView mRoundIndicatorView;
    private final int rows = 3;
    private final int columns = 7;
    private int pages;
    private Emoji deleteEmoji;
    private EditText editText;

    public EmojiView(@NonNull Context context) {
        this(context, null);
    }

    public EmojiView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public EmojiView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        this.context = context;
        pages = (int) Math.ceil(EmojiUtil.emojiList.size() / (rows * columns - 1));
        deleteEmoji = new Emoji(R.mipmap.face_delete, "[删除]");

        init();
    }

    public void setOutputSource(EditText editText) {
        this.editText = editText;
    }

    private void init() {
        View view = LayoutInflater.from(context).inflate(R.layout.emoji_view, this, true);

        mViewPager = view.findViewById(R.id.view_pager);
        mRoundIndicatorView = view.findViewById(R.id.round_indicator_view);

        mRoundIndicatorView.setIndicators(pages);

        onPageChangeListener = new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                mRoundIndicatorView.onPageScrolled(position, positionOffset, positionOffsetPixels);
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        };
        mViewPager.addOnPageChangeListener(onPageChangeListener);
        mViewPager.setAdapter(new PagerAdapter() {

            private List<Emoji> emojiList;
            private EmojiRecyclerViewAdapter adapter;

            @Override
            public int getCount() {
                return pages;
            }

            @Override
            public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
                return view == o;
            }

            @NonNull
            @Override
            public Object instantiateItem(@NonNull ViewGroup container, int position) {
                View emojiPager = LayoutInflater.from(container.getContext()).inflate(R.layout.layout_emoji_pager, container, false);

                RecyclerView recyclerView = emojiPager.findViewById(R.id.recycler_view);
                recyclerView.setLayoutManager(new GridLayoutManager(container.getContext(), 7));
                int fromIndex = position * (rows * columns - 1);
                int toIndex = (position + 1) * (rows * columns - 1);
                toIndex = toIndex > EmojiUtil.provideEmojis().size() ? EmojiUtil.provideEmojis().size() : toIndex;
                emojiList = new ArrayList<>(EmojiUtil.provideEmojis().subList(fromIndex, toIndex));
                emojiList.add(deleteEmoji);
                adapter = new EmojiRecyclerViewAdapter(emojiList);
                adapter.setOnItemClickListener(EmojiView.this);
                recyclerView.setAdapter(adapter);

                container.addView(emojiPager);
                return emojiPager;
            }

            @Override
            public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
                container.removeView((View) object);
            }
        });
    }

    private int keyCode = KeyEvent.KEYCODE_DEL;
    private KeyEvent keyEventDown = new KeyEvent(KeyEvent.ACTION_DOWN, keyCode);
    private KeyEvent keyEventUp = new KeyEvent(KeyEvent.ACTION_UP, keyCode);

    public void delete() {
        editText.onKeyDown(keyCode, keyEventDown);
        editText.onKeyUp(keyCode, keyEventUp);
    }

    public void insert(EditText editText, Emoji emoji) {
        int start = editText.getSelectionStart();
        editText.getText().delete(editText.getSelectionStart(), editText.getSelectionEnd());
        String content = editText.getText().toString();
        String leftPart = content.substring(0, start);
        String rightPart = content.substring(start);
        leftPart += emoji.getKey();
        content = leftPart + rightPart;
        editText.setText(EmojiUtil.obtainImageSpannableString(this.getContext(), content));
        editText.setSelection(leftPart.length());
    }

    public void clear() {
        mViewPager.removeOnPageChangeListener(onPageChangeListener);
    }

    @Override
    public void onItemClick(Emoji emoji) {
        if (emoji == deleteEmoji) {
            delete();
            return;
        }
        if (editText != null) {
            insert(editText, emoji);
        }
    }

    private boolean isLongDelete;
    private boolean isLongInsEmoji;
    private ExecutorService singleThreadExecutor = Executors.newSingleThreadExecutor();

    @Override
    public void onItemLongClick(final Emoji emoji, boolean touch) {
        if (emoji == deleteEmoji) {
            isLongDelete = touch;
            if (touch) {
                singleThreadExecutor.submit(new Runnable() {
                    @Override
                    public void run() {
                        while (isLongDelete) {
                            post(new Runnable() {
                                @Override
                                public void run() {
                                    delete();
                                }
                            });
                            try {
                                Thread.sleep(80);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });
            }
        } else {
            isLongInsEmoji = touch;
            if (touch) {
                singleThreadExecutor.submit(new Runnable() {
                    @Override
                    public void run() {
                        while (isLongInsEmoji) {
                            post(new Runnable() {
                                @Override
                                public void run() {
                                    insert(editText, emoji);
                                }
                            });
                            try {
                                Thread.sleep(80);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });
            }
        }
    }
}
