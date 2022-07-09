package com.nowcoder.community.entity;

/**
 * 封装 分页 相关的信息
 *
 * @author Administrator
 * @date 2022/7/9 19:34
 */
public class Page {

    // 当前页码
    private int current = 1;

    // 每页条目数
    private int limit = 10;

    // 数据总数
    private int rows;

    // 查询路径（用来复用 分页的链接）
    private String path;

    /**
     * @return 条目偏移量
     */
    public int getOffset() {
        return (current - 1) * limit;
    }

    /**
     * @return 总页数
     */
    public int getTotal() {
        int total = rows / limit;
        return rows % limit == 0 ? total : total + 1;
    }

    public int getFrom() {
        int from = current - 2;
        return Math.max(from, 1);
    }

    public int getTo() {
        int to = current + 2;
        return Math.min(to, getTotal());
    }

    public int getCurrent() {
        return current;
    }

    public void setCurrent(int current) {
        if (current >= 1) {
            this.current = current;
        }
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        if (limit >= 1 && limit <= 100) {
            this.limit = limit;
        }
    }

    public int getRows() {
        return rows;
    }

    public void setRows(int rows) {
        if (rows >= 0) {
            this.rows = rows;
        }
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }


}
