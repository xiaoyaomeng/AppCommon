package com.panghu.uikit.base.dialogfragment;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.panghu.uikit.base.field.DateField;
import com.panghu.uikit.base.field.InputField;
import com.panghu.uikit.base.field.ListField;
import com.panghu.uikit.base.field.MultiChoiceListField;
import com.panghu.uikit.base.field.TimeField;

/**
 * Created by dennis.jiang on 9/30/16.
 */
public class DialogFragmentManager {
    private static final int REQUEST_SETTINGSPREFERENCEFRAGMENT_CODE = 1;
    private static final String TAG_LISTDIALOGFRAGMENT = "ListDialogFragment";

    public static void showDatePickerDialogFragment(FragmentManager fragmentManager,
                                                    DateField field) {
        DatePickerDialogFragment fragment = DatePickerDialogFragment.newInstance(field);
        fragment.show(fragmentManager, "DatePickerDialogFragment");
    }

    public static void showDatePickerDialogFragment(FragmentManager fragmentManager,
                                                    DateField field,
                                                    @Nullable Fragment fromFragment) {
        DatePickerDialogFragment fragment = DatePickerDialogFragment.newInstance(field);
        if (fromFragment != null) {
            fragment.setTargetFragment(fromFragment, REQUEST_SETTINGSPREFERENCEFRAGMENT_CODE);
        }
        FragmentTransaction ft = fragmentManager.beginTransaction();
        ft.add(fragment, "DatePickerDialogFragment");
        ft.commitAllowingStateLoss();
    }

    public static void showTimePickerDialogFragment(@NonNull FragmentManager fragmentManager,
                                                    @NonNull TimeField field) {
        TimePickerDialogFragment fragment = TimePickerDialogFragment.newInstance(field);
        fragment.show(fragmentManager, "TimePickerDialogFragment");
    }

    public static void showTimePickerDialogFragment(@NonNull FragmentManager fragmentManager,
                                                    @NonNull TimeField field,
                                                    @NonNull Fragment fromFragment) {
        TimePickerDialogFragment fragment = TimePickerDialogFragment.newInstance(field);
        fragment.setTargetFragment(fromFragment, REQUEST_SETTINGSPREFERENCEFRAGMENT_CODE);
        fragment.show(fragmentManager, "TimePickerDialogFragment");
    }

    public static void showListDialogFragment(FragmentManager fragmentManager,
                                              ListField field,
                                              @Nullable Fragment fromFragment) {
        ListDialogFragment fragment = ListDialogFragment.newInstance(field);
        if (fromFragment != null) {
            fragment.setTargetFragment(fromFragment, REQUEST_SETTINGSPREFERENCEFRAGMENT_CODE);
        }
        FragmentTransaction ft = fragmentManager.beginTransaction();
        ft.add(fragment, TAG_LISTDIALOGFRAGMENT);
        ft.commitAllowingStateLoss();
    }

    public static void showListDialogWithActionButtonFragment(FragmentManager fragmentManager,
                                                              ListField field,
                                                              @StringRes int positiveButtonTextId,
                                                        @StringRes int negativeButtonTextId,
                                                        @Nullable Fragment fromFragment) {
        ListDialogFragment fragment = ListDialogFragment.newInstance(field, positiveButtonTextId, negativeButtonTextId);
        if (fromFragment != null) {
            fragment.setTargetFragment(fromFragment, REQUEST_SETTINGSPREFERENCEFRAGMENT_CODE);
        }
        FragmentTransaction ft = fragmentManager.beginTransaction();
        ft.add(fragment, TAG_LISTDIALOGFRAGMENT);
        ft.commitAllowingStateLoss();
    }

    public static void showSingleChoiceListDialogFragment(FragmentManager fragmentManager,
                                                          ListField field) {
        SingleChoiceListDialogFragment fragment = SingleChoiceListDialogFragment.newInstance(field);
        fragment.show(fragmentManager, "SingleChoiceListDialogFragment");
    }

    public static void showTextPreviewDialogFragment(FragmentManager fragmentManager, String text) {
        TextPreviewDialogFragment fragment = TextPreviewDialogFragment.newInstance(text);
        fragment.show(fragmentManager, "TextPreviewDialogFragment");
    }

    public static void showMultiChoiceListDialogFragment(FragmentManager fragmentManager,
                                                         MultiChoiceListField field,
                                                         @Nullable Fragment fromFragment) {
        MultiChoiceListDialogFragment fragment = MultiChoiceListDialogFragment.newInstance(field);
        if (fromFragment != null) {
            fragment.setTargetFragment(fromFragment, REQUEST_SETTINGSPREFERENCEFRAGMENT_CODE);
        }
        FragmentTransaction ft = fragmentManager.beginTransaction();
        ft.add(fragment, TAG_LISTDIALOGFRAGMENT);
        ft.commitAllowingStateLoss();
    }

    public static void showInputDialogFragment(@NonNull FragmentManager fragmentManager,
                                               @NonNull InputField field,
                                               @NonNull Fragment fromFragment) {
        InputDialogFragment fragment = InputDialogFragment.newInstance(field);
        fragment.setTargetFragment(fromFragment, REQUEST_SETTINGSPREFERENCEFRAGMENT_CODE);
        fragment.show(fragmentManager, "InputDialogFragment");
    }

    public static void showCustomInputDialogFragment(@NonNull FragmentManager fragmentManager,
                                                     @NonNull InputField field,
                                                     @NonNull Fragment fromFragment,
                                                     @LayoutRes int layoutRes) {
        InputDialogFragment fragment = InputDialogFragment.newInstance(field, layoutRes);
        fragment.setTargetFragment(fromFragment, REQUEST_SETTINGSPREFERENCEFRAGMENT_CODE);
        fragment.show(fragmentManager, "InputDialogFragment");
    }

    public static AsyncInputDialogFragment showAsyncInputDialogFragment(
            @NonNull FragmentManager fragmentManager,
            @NonNull InputField field) {
        AsyncInputDialogFragment fragment = AsyncInputDialogFragment.newInstance(field);
        fragment.show(fragmentManager, "InputDialogFragment");
        return fragment;
    }
}
