package com.migeleiei.imagesresizer.util;

import java.util.Arrays;
import java.util.List;

public class Constants {

    public static final int DEFAULT_TEXT_SIZE = 30;
    public static final String DEFAULT_TEXT_COLOR = "#000000";
    public static final String DEFAULT_TEXT_FONT = "Arial";
    public static final double DEFAULT_TEXT_OPACITY = 1.0;

    // Image Width Default
    public static final int DEFAULT_IMAGE_MIN_WIDTH = 250;

    // Title in Dialog
    public static final String TITLE_SAVE_DIALOG = "Save Image MigelEiEi";
    public static final String CONTENT_TYPE_ERROR = "Not support type";
    public static final String TITLE_TYPE_ERROR = "File not Support";
    public static final String CONTENT_ZIP_DONT_HAVE_IMAGE = "Zip File don't have any file Image";

    public static final List<String> LIST_FILE_IMAGE = Arrays.asList("png", "jpg", "jpeg");
    public static final List<Character> LIST_HIDDEN_FILE = Arrays.asList('.', '_');

}
