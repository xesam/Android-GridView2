package io.github.xesam.android.gridview2.example;

import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;
import java.util.List;

import cn.net.chelaile.android.components.R;
import io.github.xesam.android.gridview2.GridView2;

public class GridMainActivity extends AppCompatActivity {

    private GridView2 adsGridRecyclerView;
    private SeekBar columnsSeekBar;
    private SeekBar rowsSeekBar;
    private SeekBar totalItemsSeekBar;
    private TextView columnsValueTextView;
    private TextView rowsValueTextView;
    private TextView totalItemsValueTextView;
    private Button moreView;
    private CheckBox moreViewCheckBox;
    private GridCardAdapter gridCardAdapter;
    private List<GridItem> items;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_example_grid_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // 初始化控件
        initViews();

        // 设置监听器
        setListeners();

        // 创建MoreView
        moreView = new Button(this);
        moreView.setText("More");

        // 创建数据并设置适配器
        items = createSampleData();
        gridCardAdapter = new GridCardAdapter(items);
        adsGridRecyclerView.setAdapter(gridCardAdapter);

        // 应用初始设置
        applySettings();
    }

    private void initViews() {
        adsGridRecyclerView = findViewById(R.id.adsGridRecyclerView);
        columnsSeekBar = findViewById(R.id.columnsSeekBar);
        rowsSeekBar = findViewById(R.id.rowsSeekBar);
        totalItemsSeekBar = findViewById(R.id.totalItemsSeekBar);
        columnsValueTextView = findViewById(R.id.columnsValueTextView);
        rowsValueTextView = findViewById(R.id.rowsValueTextView);
        totalItemsValueTextView = findViewById(R.id.totalItemsValueTextView);
        moreViewCheckBox = findViewById(R.id.moreViewCheckBox);
    }

    private void setListeners() {
        // 列数SeekBar监听器
        columnsSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                // 确保最小值为1
                if (progress < 1) {
                    progress = 1;
                    columnsSeekBar.setProgress(progress);
                }
                columnsValueTextView.setText(String.valueOf(progress));

                // 立即应用设置
                applySettings();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        // 行数SeekBar监听器
        rowsSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                // 确保最小值为1
                if (progress < 1) {
                    progress = 1;
                    rowsSeekBar.setProgress(progress);
                }
                rowsValueTextView.setText(String.valueOf(progress));

                // 立即应用设置
                applySettings();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        // MoreView CheckBox监听器
        moreViewCheckBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            applySettings();
        });

        // 总数SeekBar监听器
        totalItemsSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                // 确保最小值为1
                if (progress < 1) {
                    progress = 1;
                    totalItemsSeekBar.setProgress(progress);
                }
                totalItemsValueTextView.setText(String.valueOf(progress));

                // 重新创建数据并应用设置
                items = createSampleData(progress);
                gridCardAdapter = new GridCardAdapter(items);
                adsGridRecyclerView.setAdapter(gridCardAdapter);
                applySettings();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
    }

    private void applySettings() {
        int maxColumns = columnsSeekBar.getProgress();
        int maxRows = rowsSeekBar.getProgress();
        boolean showMoreView = moreViewCheckBox.isChecked();

        // 确保最小值为1
        if (maxColumns < 1) maxColumns = 1;
        if (maxRows < 1) maxRows = 1;

        // 应用设置
        adsGridRecyclerView.setMaxColumns(maxColumns);
        adsGridRecyclerView.setMaxRows(maxRows);

        // 设置MoreView
        if (showMoreView) {
            adsGridRecyclerView.setMoreView(moreView);
        } else {
            adsGridRecyclerView.setMoreView(null);
        }

        // 通知适配器数据变化，触发重新计算显示
        gridCardAdapter.notifyDataSetChanged();
    }

    private List<GridItem> createSampleData() {
        return createSampleData(totalItemsSeekBar.getProgress());
    }

    private List<GridItem> createSampleData(int count) {
        List<GridItem> items = new ArrayList<>();
        String[] titles = {
                "公交查询", "地铁线路", "实时到站", "路线规划",
                "站点收藏", "历史记录", "附近站点", "出行提醒",
                "天气信息", "线路公告", "乘车码", "失物招领",
                "客服中心", "意见反馈", "分享好友", "设置中心",
                "夜间模式", "离线地图", "语音播报", "个人中心"
        };
        
        String[] subtitles = {
                "快速查询公交线路", "地铁线路一目了然", "实时掌握到站时间", "智能规划出行路线",
                "收藏常用站点", "查看历史记录", "发现附近公交站点", "设置出行提醒",
                "查看当地天气", "获取最新线路公告", "刷码乘车更便捷", "发布失物招领信息",
                "联系在线客服", "提交使用反馈", "邀请好友使用", "个性化设置",
                "切换夜间模式", "下载离线地图", "开启语音提醒", "管理个人信息"
        };

        for (int i = 0; i < count; i++) {
            items.add(new GridItem(
                    R.drawable.ic_launcher_foreground,
                    titles[i % titles.length],
                    subtitles[i % subtitles.length]
            ));
        }
        return items;
    }


}