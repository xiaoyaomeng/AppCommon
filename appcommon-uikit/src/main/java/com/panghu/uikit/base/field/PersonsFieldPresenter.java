package com.panghu.uikit.base.field;

import android.text.TextUtils;
import android.view.View;

import com.panghu.uikit.R;
import com.glip.widgets.tokenautocomplete.Contact;
import com.glip.widgets.tokenautocomplete.ContactsAutoCompleteView;
import com.glip.widgets.tokenautocomplete.TokenCompleteTextView;

/**
 * Created by panghu on 8/30/16.
 */
public class PersonsFieldPresenter extends AbstractFieldPresenter {

    @Override
    protected void onCreateCustomView(FieldViewHolder viewHolder) {
        ContactsAutoCompleteView personsView = (ContactsAutoCompleteView) viewHolder.mCustomView;
        personsView.allowCollapse(true);
        personsView.setTokenClickStyle(TokenCompleteTextView.TokenClickStyle.None);
        personsView.setClickable(false);
        personsView.setLongClickable(false);
    }

    @Override
    protected void onBindCustomView(View customView, AbstractField field) {
        PersonsField personsField = (PersonsField) field;
        ContactsAutoCompleteView personsView = (ContactsAutoCompleteView) customView;
        personsView.clear();
        String hint = personsView.getContext().getString(personsField.getHintResource());
        if (!TextUtils.equals(hint, personsView.getHint())) {
            personsView.setHint(hint);
        }
        if (personsField.getPersons() != null) {
            for (Contact person : personsField.getPersons()) {
                personsView.addObject(person);
            }
        }
    }

    @Override
    protected int onInflateCustomView() {
        return R.layout.common_field_custom_persons_view;
    }
}
