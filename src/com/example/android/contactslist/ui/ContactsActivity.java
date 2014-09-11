/*
 * Copyright (C) 2013 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.android.contactslist.ui;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.SearchManager;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.example.android.contactslist.BuildConfig;
import com.example.android.contactslist.R;
import com.example.android.contactslist.util.ContactsConstants;
import com.example.android.contactslist.util.Utils;

/**
 * FragmentActivity to hold the main {@link ContactsListFragment}. On larger screen devices which
 * can fit two panes also load {@link ContactDetailFragment}.
 */
public class ContactsActivity extends FragmentActivity implements
ContactsListFragment.OnContactsInteractionListener, ActionBar.TabListener{
	// Defines a tag for identifying log entries
	private static final String TAG = "ContactsListActivity";


	private ContactDetailFragment mContactDetailFragment;

	// If true, this is a larger screen device which fits two panes
	private boolean isTwoPaneLayout;

	// True if this activity instance is a search result view (used on pre-HC devices that load
	// search results in a separate instance of the activity rather than loading results in-line
	// as the query is typed.
	private boolean isSearchResultView = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		if (BuildConfig.DEBUG) {
			Utils.enableStrictMode();
		}
		super.onCreate(savedInstanceState);

		// Set main content view. On smaller screen devices this is a single pane view with one
		// fragment. One larger screen devices this is a two pane view with two fragments.
		setContentView(R.layout.activity_main);

		final ActionBar actionBar = getActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		actionBar.addTab(actionBar.newTab().setText(R.string.people)
				.setTabListener(this));
		actionBar.addTab(actionBar.newTab().setText(R.string.call_history)
				.setTabListener(this));


		// Check if two pane bool is set based on resource directories
		isTwoPaneLayout = getResources().getBoolean(R.bool.has_two_panes);


		if (isTwoPaneLayout) {
			// If two pane layout, locate the contact detail fragment
			mContactDetailFragment = (ContactDetailFragment)
					getSupportFragmentManager().findFragmentById(R.id.contact_detail);
		}
	}

	/**
	 * This interface callback lets the main contacts list fragment notify
	 * this activity that a contact has been selected.
	 *
	 * @param contactUri The contact Uri to the selected contact.
	 */
	@Override
	public void onContactSelected(Uri contactUri) {
		if (isTwoPaneLayout && mContactDetailFragment != null) {
			// If two pane layout then update the detail fragment to show the selected contact
			mContactDetailFragment.setContact(contactUri);
		} else {
			// Otherwise single pane layout, start a new ContactDetailActivity with
			// the contact Uri
			Intent intent = new Intent(this, ContactDetailActivity.class);
			intent.setData(contactUri);
			startActivity(intent);
		}
	}

	/**
	 * This interface callback lets the main contacts list fragment notify
	 * this activity that a contact is no longer selected.
	 */
	@Override
	public void onSelectionCleared() {
		if (isTwoPaneLayout && mContactDetailFragment != null) {
			mContactDetailFragment.setContact(null);
		}
	}

	@Override
	public boolean onSearchRequested() {
		// Don't allow another search if this activity instance is already showing
		// search results. Only used pre-HC.
		return !isSearchResultView && super.onSearchRequested();
	}

	@Override
	public void onTabSelected(Tab tab, FragmentTransaction ft) {
		switch(tab.getPosition()){
		case 0:
			if (Intent.ACTION_SEARCH.equals(getIntent().getAction())) {
				
				String searchQuery = getIntent().getStringExtra(SearchManager.QUERY);
				ContactsListFragment mContactsListFragment = (ContactsListFragment)
						getSupportFragmentManager().findFragmentByTag(ContactsConstants.CONTACT_LIST);

				// This flag notes that the Activity is doing a search, and so the result will be
				// search results rather than all contacts. This prevents the Activity and Fragment
				// from trying to a search on search results.
				isSearchResultView = true;
				mContactsListFragment.setSearchQuery(searchQuery);

				// Set special title for search results
				String title = getString(R.string.contacts_list_search_results_title, searchQuery);
				setTitle(title);
			}else{
				ContactsListFragment mContactsListFragment = new ContactsListFragment();
				getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, mContactsListFragment,ContactsConstants.CONTACT_LIST).commit();
			}

			break;

		case 1:
			CallHistoryFragment mCallHistoryFragment = new CallHistoryFragment();
			getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, mCallHistoryFragment,ContactsConstants.CALL_HISTORY).commit();
			break;
		}
	}

	@Override
	public void onTabUnselected(Tab tab, FragmentTransaction ft) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onTabReselected(Tab tab, FragmentTransaction ft) {
		// TODO Auto-generated method stub

	}
}
