package com.panghu.uikit.base.field;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by nathan.wei on 5/21/18.
 */

public class MultiChoiceListField extends ListField {
    private Set<Integer> selections = new HashSet<>();

    public MultiChoiceListField(FieldId id, int iconResId, boolean hasDivider, boolean isVisible, int titleResId, boolean isSelectable) {
        super(id, iconResId, hasDivider, isVisible, titleResId, isSelectable);
    }

    public Set<Integer> getSelections() {
        return selections;
    }

    public void toggleSelection(int position) {
        if (selections.contains(position)) {
            selections.remove(position);
        } else {
            selections.add(position);
        }
    }
}
