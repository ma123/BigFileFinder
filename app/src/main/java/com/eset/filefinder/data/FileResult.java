package com.eset.filefinder.data;

public class FileResult {
        private String path;
        private Long size;

        public FileResult(String path, Long size) {
            this.path = path;
            this.size = size;
        }

        public String getPath() {
            return path;
        }

        public void setPath(String path) {
            this.path = path;
        }

        public Long getSize() {
            return size;
        }

        public void setSize(Long size) {
            this.size = size;
        }
}
