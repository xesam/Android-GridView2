# Android-GridView2

一个灵活的网格布局组件，基于RecyclerView实现，支持动态列数和行数控制。

## 安装

```groovy
implementation 'io.github.xesam:android-gridview2:0.0.1'
```

## 特性说明

### 核心功能
- **动态列数控制**：支持设置每行最大元素个数，自动根据内容调整实际列数
- **行数限制**：可设置最大显示行数，超出部分可选择显示MoreView
- **智能布局**：单行模式下根据实际元素数量动态调整列数
- **MoreView支持**：可自定义"更多"视图，在内容超出时显示
- **响应式布局**：支持运行时动态调整maxColumns和maxRows参数
- **智能列数计算**：根据实际元素数量动态优化列数布局，小数据量时自动使用紧凑布局避免浪费空间

### 单行模式特性
当 `maxRows = 1` 时：
- **完全忽略maxRows限制**：显示所有元素，不受数量限制
- **动态列数**：列数 = min(实际元素数量, maxColumns)
- **无MoreView**：不显示"更多"按钮

### 多行模式特性
当 `maxRows > 1` 时：
- **严格行数限制**：最多显示 `maxRows * maxColumns` 个元素
- **MoreView支持**：超出限制时显示"更多"按钮
- **固定列数**：使用设置的maxColumns值

## 测试用例

### 测试场景1：单行模式验证
```xml
<io.github.xesam.android.gridview2.GridView2
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    app:maxColumns="5"
    app:maxRows="1" />
```

**测试数据：**
- 1个元素 → 显示1列
- 3个元素 → 显示3列
- 7个元素 → 显示5列（不超过maxColumns）

### 测试场景2：多行模式验证
```xml
<io.github.xesam.android.gridview2.GridView2
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    app:maxColumns="3"
    app:maxRows="2" />
```

**测试数据：**
- 4个元素 → 显示2行3列布局，实际显示4个元素
- 7个元素 → 显示2行3列布局，显示6个元素+MoreView
- 10个元素 → 同上，显示6个元素+MoreView

### 测试场景3：边界条件
```xml
<io.github.xesam.android.gridview2.GridView2
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    app:maxColumns="1"
    app:maxRows="1" />
```

**测试数据：**
- 0个元素 → 显示0列（空视图）
- 1个元素 → 显示1列
- 2个元素 → 显示1列（滚动显示）

### 测试场景4：动态参数调整
```java
GridView gridView = findViewById(R.id.gridView);

// 从单行切换到多行
gridView.setMaxRows(3);
gridView.setMaxColumns(4);

// 从多行切换到单行
gridView.setMaxRows(1);
gridView.setMaxColumns(6);
```

### 测试场景5：MoreView集成
```java
GridView gridView = findViewById(R.id.gridView);
View moreView = LayoutInflater.from(context).inflate(R.layout.item_more, null);
gridView.setMoreView(moreView);
```

**验证点：**
- 内容超出限制时正确显示MoreView
- MoreView点击事件正常响应
- 单行模式下不显示MoreView

### 测试场景6：智能列数计算验证
**XML配置**：
```xml
<io.github.xesam.android.gridview2.GridView2
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    app:maxColumns="10"
    app:maxRows="10" />
```

**测试场景**：
- **场景1**：元素总数=3个
  - 预期显示：1行3列，显示3个元素，不显示MoreView
- **场景2**：元素总数=8个
  - 预期显示：1行8列，显示8个元素，不显示MoreView
- **场景3**：元素总数=15个
  - 预期显示：2行10列，显示15个元素，不显示MoreView（2×10=20≥15）
- **场景4**：元素总数=25个
  - 预期显示：3行10列，显示20个元素，显示MoreView（3×10=20<25）

**验证点**：
- 1. 小数据量时使用紧凑布局，列数=实际元素数
- 2. 大数据量时使用最大列数
- 3. 空间利用率最大化，避免浪费

### 测试场景7：性能测试
**XML配置**：
```xml
<io.github.xesam.android.gridview2.GridView2
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    app:maxColumns="50"
    app:maxRows="50" />
```

**测试数据**：
- 元素总数：1000个

**验证点**：
- 1. 快速滚动无卡顿
- 2. 内存使用正常
- 3. 回收复用正常

## 使用示例

### XML配置
```xml
<io.github.xesam.android.gridview2.GridView2
    android:id="@+id/gridView"
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    app:maxColumns="3"
    app:maxRows="2" />
```

### Java代码
```java
GridView gridView = findViewById(R.id.gridView);
gridView.setAdapter(new MyAdapter(data));

// 动态调整参数
gridView.setMaxColumns(4);
gridView.setMaxRows(1);

// 设置MoreView
gridView.setMoreView(moreView);
```