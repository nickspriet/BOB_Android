package com.howest.nmct.bob.interfaces;

import com.squareup.picasso.Callback;

/**
 * Methods for controlling the toolbar
 */
public interface ToolbarController {

    /**
     * Sets the image and title of the collapsing toolbar
     * @param url URL of the image to set
     */
    void setToolbarImage(String url);

    void setToolbarImage(String url, Callback callback);

    /**
     * Sets the title of the toolbar
     * @param title Title of the toolbar to set
     */
    void setToolbarTitle(String title);
}
