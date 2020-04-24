package com.china.cibn.bangtvmobile.bangtv.entity.recommend;

import java.util.List;

/**
 * Created by gzj on 2018/3/15.
 */

public class LayoutFileInfo {


    /**
     * id : tuijian
     * spacing : 10
     * margin : {"l":10,"b":10}
     * height : 600
     * layout : [{"s":33,"w":295},{"s":21,"w":450},{"s":11,"w":450},{"s":31,"w":450},{"s":11,"w":450},{"s":32,"w":450},{"s":21,"w":450},{"s":31,"w":450}]
     */

    private String id;
    private int spacing;
    private MarginBean margin;
    private int height;
    private List<LayoutBean> layout;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getSpacing() {
        return spacing;
    }

    public void setSpacing(int spacing) {
        this.spacing = spacing;
    }

    public MarginBean getMargin() {
        return margin;
    }

    public void setMargin(MarginBean margin) {
        this.margin = margin;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public List<LayoutBean> getLayout() {
        return layout;
    }

    public void setLayout(List<LayoutBean> layout) {
        this.layout = layout;
    }

    public static class MarginBean {
        /**
         * l : 10
         * b : 10
         */

        private int l;
        private int b;

        public int getL() {
            return l;
        }

        public void setL(int l) {
            this.l = l;
        }

        public int getB() {
            return b;
        }

        public void setB(int b) {
            this.b = b;
        }
    }

    public static class LayoutBean {
        /**
         * s : 33
         * w : 295
         */

        private int s;
        private int w;

        public int getS() {
            return s;
        }

        public void setS(int s) {
            this.s = s;
        }

        public int getW() {
            return w;
        }

        public void setW(int w) {
            this.w = w;
        }
    }
}
