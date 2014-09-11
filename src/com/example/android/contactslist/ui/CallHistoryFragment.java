package com.example.android.contactslist.ui;

import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.io.IOException;

import com.example.android.contactslist.BuildConfig;
import com.example.android.contactslist.R;
import com.example.android.contactslist.adapter.CallHistoryAdapter;
import com.example.android.contactslist.adapter.ContactsAdapter;
import com.example.android.contactslist.util.ImageLoader;
import com.example.android.contactslist.util.Utils;

import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract.Contacts;
import android.provider.ContactsContract.Contacts.Photo;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class CallHistoryFragment extends Fragment {

    private CallHistoryAdapter mAdapter; // The main query adapter
    private ImageLoader mImageLoader; // Handles loading the contact image in a background thread
    
    // Whether or not this fragment is showing in a two-pane layout
    private boolean mIsTwoPaneLayout;
    
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

        // Check if this fragment is part of a two-pane set up or a single pane by reading a
        // boolean from the application resource directories. This lets allows us to easily specify
        // which screen sizes should use a two-pane layout by setting this boolean in the
        // corresponding resource size-qualified directory.
        mIsTwoPaneLayout = getResources().getBoolean(R.bool.has_two_panes);

        // Let this fragment contribute menu items
        setHasOptionsMenu(true);

        /*
         * An ImageLoader object loads and resizes an image in the background and binds it to the
         * QuickContactBadge in each item layout of the ListView. ImageLoader implements memory
         * caching for each image, which substantially improves refreshes of the ListView as the
         * user scrolls through it.
         *
         * To learn more about downloading images asynchronously and caching the results, read the
         * Android training class Displaying Bitmaps Efficiently.
         *
         * http://developer.android.com/training/displaying-bitmaps/
         */
        mImageLoader = new ImageLoader(getActivity(), getListPreferredItemHeight()) {
            @Override
            protected Bitmap processBitmap(Object data) {
                // This gets called in a background thread and passed the data from
                // ImageLoader.loadImage().
                return loadContactPhotoThumbnail((String) data, getImageSize());
            }
        };

        // Set a placeholder loading image for the image loader
        mImageLoader.setLoadingImage(R.drawable.ic_contact_picture_holo_light);

        // Add a cache to the image loader
        mImageLoader.addImageCache(getActivity().getSupportFragmentManager(), 0.1f);
        
        // Create the main contacts adapter
        mAdapter = new CallHistoryAdapter(getActivity(),mImageLoader);
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.call_history_fragment, container, false);
		return view;
	}
	
	 private Bitmap loadContactPhotoThumbnail(String photoData, int imageSize) {

	        // Ensures the Fragment is still added to an activity. As this method is called in a
	        // background thread, there's the possibility the Fragment is no longer attached and
	        // added to an activity. If so, no need to spend resources loading the contact photo.
	        if (!isAdded() || getActivity() == null) {
	            return null;
	        }

	        // Instantiates an AssetFileDescriptor. Given a content Uri pointing to an image file, the
	        // ContentResolver can return an AssetFileDescriptor for the file.
	        AssetFileDescriptor afd = null;

	        // This "try" block catches an Exception if the file descriptor returned from the Contacts
	        // Provider doesn't point to an existing file.
	        try {
	            Uri thumbUri;
	            // If Android 3.0 or later, converts the Uri passed as a string to a Uri object.
	            if (Utils.hasHoneycomb()) {
	                thumbUri = Uri.parse(photoData);
	            } else {
	                // For versions prior to Android 3.0, appends the string argument to the content
	                // Uri for the Contacts table.
	                final Uri contactUri = Uri.withAppendedPath(Contacts.CONTENT_URI, photoData);

	                // Appends the content Uri for the Contacts.Photo table to the previously
	                // constructed contact Uri to yield a content URI for the thumbnail image
	                thumbUri = Uri.withAppendedPath(contactUri, Photo.CONTENT_DIRECTORY);
	            }
	            // Retrieves a file descriptor from the Contacts Provider. To learn more about this
	            // feature, read the reference documentation for
	            // ContentResolver#openAssetFileDescriptor.
	            afd = getActivity().getContentResolver().openAssetFileDescriptor(thumbUri, "r");

	            // Gets a FileDescriptor from the AssetFileDescriptor. A BitmapFactory object can
	            // decode the contents of a file pointed to by a FileDescriptor into a Bitmap.
	            FileDescriptor fileDescriptor = afd.getFileDescriptor();

	            if (fileDescriptor != null) {
	                // Decodes a Bitmap from the image pointed to by the FileDescriptor, and scales it
	                // to the specified width and height
	                return ImageLoader.decodeSampledBitmapFromDescriptor(
	                        fileDescriptor, imageSize, imageSize);
	            }
	        } catch (FileNotFoundException e) {
	            // If the file pointed to by the thumbnail URI doesn't exist, or the file can't be
	            // opened in "read" mode, ContentResolver.openAssetFileDescriptor throws a
	            // FileNotFoundException.
	           
	        } finally {
	            // If an AssetFileDescriptor was returned, try to close it
	            if (afd != null) {
	                try {
	                    afd.close();
	                } catch (IOException e) {
	                    // Closing a file descriptor might cause an IOException if the file is
	                    // already closed. Nothing extra is needed to handle this.
	                }
	            }
	        }

	        // If the decoding failed, returns null
	        return null;
	    }
	 
	 /**
	     * Gets the preferred height for each item in the ListView, in pixels, after accounting for
	     * screen density. ImageLoader uses this value to resize thumbnail images to match the ListView
	     * item height.
	     *
	     * @return The preferred height in pixels, based on the current theme.
	     */
	    private int getListPreferredItemHeight() {
	        final TypedValue typedValue = new TypedValue();

	        // Resolve list item preferred height theme attribute into typedValue
	        getActivity().getTheme().resolveAttribute(
	                android.R.attr.listPreferredItemHeight, typedValue, true);

	        // Create a new DisplayMetrics object
	        final DisplayMetrics metrics = new android.util.DisplayMetrics();

	        // Populate the DisplayMetrics
	        getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);

	        // Return theme value based on DisplayMetrics
	        return (int) typedValue.getDimension(metrics);
	    }
}
