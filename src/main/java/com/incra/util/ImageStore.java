package com.incra.util;

/**
 * @author Jeff Risberg
 * @since 03/12/15
 */
public class ImageStore {
    protected String dirPath;

    public ImageStore(String dirPath) {
        this.dirPath = dirPath;
    }

    public String getPath() {
        return dirPath;
    }
}
