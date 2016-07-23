// Generated code from Butter Knife. Do not modify!
package com.uber.challenge.flickr;

import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;
import butterknife.Unbinder;
import butterknife.internal.Finder;
import java.lang.IllegalStateException;
import java.lang.Object;
import java.lang.Override;

public class MainAct_ViewBinding<T extends MainAct> implements Unbinder {
  protected T target;

  private View view2131427411;

  private View view2131427409;

  public MainAct_ViewBinding(final T target, Finder finder, Object source) {
    this.target = target;

    View view;
    view = finder.findRequiredView(source, R.id.in, "field 'mDisplayingView' and method 'onGridItemClick'");
    target.mDisplayingView = finder.castView(view, R.id.in, "field 'mDisplayingView'", GridView.class);
    view2131427411 = view;
    ((AdapterView<?>) view).setOnItemClickListener(new AdapterView.OnItemClickListener() {
      @Override
      public void onItemClick(AdapterView<?> p0, View p1, int p2, long p3) {
        target.onGridItemClick(p0, p1, p2, p3);
      }
    });
    view = finder.findRequiredView(source, R.id.searchBox, "field 'm_searchEditText' and method 'onSearchEditorAcion'");
    target.m_searchEditText = finder.castView(view, R.id.searchBox, "field 'm_searchEditText'", EditText.class);
    view2131427409 = view;
    ((TextView) view).setOnEditorActionListener(new TextView.OnEditorActionListener() {
      @Override
      public boolean onEditorAction(TextView p0, int p1, KeyEvent p2) {
        return target.onSearchEditorAcion(p0, p1, p2);
      }
    });
    target.m_photoCounter = finder.findRequiredViewAsType(source, R.id.photosCounter, "field 'm_photoCounter'", TextView.class);
  }

  @Override
  public void unbind() {
    T target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");

    target.mDisplayingView = null;
    target.m_searchEditText = null;
    target.m_photoCounter = null;

    ((AdapterView<?>) view2131427411).setOnItemClickListener(null);
    view2131427411 = null;
    ((TextView) view2131427409).setOnEditorActionListener(null);
    view2131427409 = null;

    this.target = null;
  }
}
