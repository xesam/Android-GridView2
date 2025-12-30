package io.github.xesam.android.gridview2;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * 网格组件
 * 继承自RecyclerView，用于以网格形式展示动态网格元素
 */
public class GridView2 extends RecyclerView {

    private static final int VIEW_TYPE_MORE = Integer.MAX_VALUE; // MoreView类型，避免与用户类型冲突

    private int maxColumns = 1; // 每行最大元素个数
    private int maxRows = Integer.MAX_VALUE; // 最大显示行数
    private GridLayoutManager layoutManager;
    private Adapter<ViewHolder> wrappedAdapter; // 包装的适配器
    private View moreView; // MoreView

    public GridView2(Context context) {
        super(context);
        init(context, null);
    }

    public GridView2(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public GridView2(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        // 从XML属性中读取配置
        if (attrs != null) {
            TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.Xesam_GridView2);
            maxColumns = a.getInt(R.styleable.Xesam_GridView2_maxColumns, maxColumns);
            maxRows = a.getInt(R.styleable.Xesam_GridView2_maxRows, maxRows);
            a.recycle();
        }

        layoutManager = new GridLayoutManager(getContext(), maxColumns);
        setLayoutManager(layoutManager);
    }

    /**
     * 设置每行最大元素个数
     *
     * @param maxColumns 每行最大元素个数
     */
    public void setMaxColumns(int maxColumns) {
        this.maxColumns = maxColumns;
        updateSpanCount();
    }

    /**
     * 设置最大显示行数
     *
     * @param maxRows 最大显示行数
     */
    public void setMaxRows(int maxRows) {
        this.maxRows = maxRows;
        // 通知适配器数据变化，触发重新计算显示
        if (wrappedAdapter != null) {
            wrappedAdapter.notifyDataSetChanged();
            updateSpanCount();
        }
    }

    /**
     * 设置MoreView
     *
     * @param moreView MoreView
     */
    public void setMoreView(View moreView) {
        this.moreView = moreView;
        // 通知适配器数据变化，触发重新计算显示
        if (wrappedAdapter != null) {
            wrappedAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void setAdapter(Adapter adapter) {
        if (adapter != null) {
            wrappedAdapter = new RowLimitAdapter(adapter);
            super.setAdapter(wrappedAdapter);

            // 应用XML配置的maxColumns，使用智能列数计算
            updateSpanCount();
        } else {
            super.setAdapter(null);
        }
    }

    private void updateSpanCount() {
        if (layoutManager == null || wrappedAdapter == null) {
            return;
        }

        int actualItems = ((RowLimitAdapter) wrappedAdapter).wrapped.getItemCount();
        int actualColumns;

        if (maxRows == 1) {
            // 单行模式：列数与实际元素数量一致
            actualColumns = Math.min(actualItems, maxColumns);
            actualColumns = Math.max(actualColumns, 1);
        } else {
            // 多行模式：根据实际元素数量优化列数
            if (actualItems <= maxColumns) {
                actualColumns = actualItems;
            } else {
                actualColumns = maxColumns;
            }
        }

        layoutManager.setSpanCount(actualColumns);
    }

    // 包装适配器，用于限制显示行数
    private class RowLimitAdapter extends Adapter<ViewHolder> {
        private final Adapter<ViewHolder> wrapped;

        RowLimitAdapter(Adapter<ViewHolder> wrapped) {
            this.wrapped = wrapped;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            // 如果是MoreView类型，则创建MoreView的ViewHolder
            if (viewType == VIEW_TYPE_MORE) {
                return new ViewHolder(moreView) {
                };
            }
            return wrapped.onCreateViewHolder(parent, viewType);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            // 如果是MoreView位置，则不绑定数据
            if (getItemViewType(position) == VIEW_TYPE_MORE) {
                return;
            }
            wrapped.onBindViewHolder(holder, position);
        }

        @Override
        public int getItemCount() {
            DisplayInfo info = calculateDisplayInfo();
            return info.displayCount;
        }

        @Override
        public int getItemViewType(int position) {
            DisplayInfo info = calculateDisplayInfo();

            // 只有在超出限制且是最后一个位置时才显示MoreView
            if (info.hasMoreView && position == info.displayCount - 1) {
                return VIEW_TYPE_MORE;
            }
            return wrapped.getItemViewType(position);
        }

        /**
         * 计算显示信息的辅助类
         */
        private class DisplayInfo {
            final int displayCount;
            final boolean hasMoreView;

            DisplayInfo(int displayCount, boolean hasMoreView) {
                this.displayCount = displayCount;
                this.hasMoreView = hasMoreView;
            }
        }

        /**
         * 计算显示相关信息，避免重复计算
         */
        private DisplayInfo calculateDisplayInfo() {
            int actualItems = wrapped.getItemCount();

            // 如果实际元素很少，直接显示所有元素
            if (actualItems <= maxColumns) {
                return new DisplayInfo(actualItems, false);
            }

            // 计算需要的行数
            int neededRows = (int) Math.ceil((double) actualItems / maxColumns);

            // 如果需要的行数小于等于maxRows，显示所有元素
            if (neededRows <= maxRows) {
                return new DisplayInfo(actualItems, false);
            }

            // 否则按maxRows限制显示
            int maxItems = maxRows * maxColumns;
            boolean hasMoreView = moreView != null;

            return new DisplayInfo(maxItems, hasMoreView);
        }
    }
}