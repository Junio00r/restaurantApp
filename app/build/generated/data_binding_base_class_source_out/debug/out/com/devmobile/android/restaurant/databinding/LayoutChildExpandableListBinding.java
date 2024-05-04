// Generated by view binder compiler. Do not edit!
package com.devmobile.android.restaurant.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import com.devmobile.android.restaurant.R;
import com.google.android.material.textview.MaterialTextView;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class LayoutChildExpandableListBinding implements ViewBinding {
  @NonNull
  private final LinearLayout rootView;

  @NonNull
  public final MaterialTextView textItemName;

  @NonNull
  public final MaterialTextView textItemPrice;

  private LayoutChildExpandableListBinding(@NonNull LinearLayout rootView,
      @NonNull MaterialTextView textItemName, @NonNull MaterialTextView textItemPrice) {
    this.rootView = rootView;
    this.textItemName = textItemName;
    this.textItemPrice = textItemPrice;
  }

  @Override
  @NonNull
  public LinearLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static LayoutChildExpandableListBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static LayoutChildExpandableListBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.layout_child_expandable_list, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static LayoutChildExpandableListBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.textItemName;
      MaterialTextView textItemName = ViewBindings.findChildViewById(rootView, id);
      if (textItemName == null) {
        break missingId;
      }

      id = R.id.textItemPrice;
      MaterialTextView textItemPrice = ViewBindings.findChildViewById(rootView, id);
      if (textItemPrice == null) {
        break missingId;
      }

      return new LayoutChildExpandableListBinding((LinearLayout) rootView, textItemName,
          textItemPrice);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}