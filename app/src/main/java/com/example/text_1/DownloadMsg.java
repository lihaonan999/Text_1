package com.example.text_1;

/**
 * Created by ${李昊男} on 2019/11/12.
 */

public class DownloadMsg {
        private int code;// 0 表示下载失败 1 设置最大进度 2 设置当前进度 3 表示下载完成
        private int progress;//表示当前进度
        private int max;//表示文件的总大小
        public DownloadMsg(int code, int progress, int max) {
            this.code = code;
            this.progress = progress;
            this.max = max;
        }

        public int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }

        public int getProgress() {
            return progress;
        }

        public void setProgress(int progress) {
            this.progress = progress;
        }

        public int getMax() {
            return max;
        }

        public void setMax(int max) {
            this.max = max;
        }




}
