package com.ccclubs.common.base.lce;

import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.view.View;
import android.widget.TextView;
import com.ccclubs.common.R;
import com.ccclubs.common.base.RxBaseActivity;
import com.ccclubs.common.base.RxBasePresenter;
import com.ccclubs.common.base.RxBaseView;
import com.ccclubs.common.support.LceAnimatorHelper;

/**
 * Activity基类, 继承自此类的Activity需要实现{@link #getLayoutId},{@link #init}
 * 以及{@link #createPresenter()}, 不需要覆写onCreate方法.
 * <br/>
 * 实现此类需遵循MVP设计, 第一个泛型CV需传入一个继承自{@link View}的ContentView,
 * <br/>
 * 第二个泛型M需传入一个实体Bean或者默认Object,
 * <br/>
 * 第三个泛型V需传入一个继承自{@link RxBaseView}的MVPView,
 * <br/>
 * 第四个泛型P需传入继承自{@link RxBasePresenter}的MVPPresenter.
 * <br/>
 * Presenter的生命周期已交由此类管理, 子类无需管理. 如果子类要使用多个Presenter, 则需要自行管理生命周期.
 * 此类已经实现了BaseView中的抽象方法, 子类无需再实现, 如需自定可覆写对应的方法.
 * <br/>
 */
public abstract class RxLceActivity<CV extends View, M, V extends RxLceView<M>, P extends RxBasePresenter<V>>
    extends RxBaseActivity<V, P> implements RxLceView<M> {

  protected View loadingView;
  protected CV contentView;
  protected TextView errorView;

  @CallSuper @Override protected void init(Bundle savedInstanceState) {
    super.init(savedInstanceState);
    loadingView = $(R.id.loadingView);
    contentView = $(R.id.contentView);
    errorView = $(R.id.errorView);

    if (loadingView == null) {
      throw new NullPointerException(
          "Loading view is null! Have you specified a loading view in your layout xml file?"
              + " You have to give your loading View the id R.id.loadingView");
    }

    if (contentView == null) {
      throw new NullPointerException(
          "Content view is null! Have you specified a content view in your layout xml file?"
              + " You have to give your content View the id R.id.contentView");
    }

    if (errorView == null) {
      throw new NullPointerException(
          "Error view is null! Have you specified a content view in your layout xml file?"
              + " You have to give your error View the id R.id.contentView");
    }

    // 设置error点击重新获取数据
    errorView.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        onErrorViewClicked();
      }
    });
  }

  /**
   * Called if the error view has been clicked. To disable clicking on the errorView use
   * <code>errorView.setClickable(false)</code>
   */
  public abstract void onErrorViewClicked();

  @Override public void showLoading(boolean pullToRefresh) {
    if (!pullToRefresh) {
      animateLoadingViewIn();
    }
  }

  /**
   * Override this method if you want to provide your own animation for showing the loading view
   */
  protected void animateLoadingViewIn() {
    LceAnimatorHelper.showLoading(loadingView, contentView, errorView);
  }

  @Override public void showContent() {
    animateContentViewIn();
  }

  /**
   * Called to animate from loading view to content view
   */
  protected void animateContentViewIn() {
    LceAnimatorHelper.showContent(loadingView, contentView, errorView);
  }

  @Override public void showError(Throwable e, boolean pullToRefresh) {
    if (!pullToRefresh) {
      animateErrorViewIn();
    }
  }

  /**
   * Animates the error view in (instead of displaying content view / loading view)
   */
  protected void animateErrorViewIn() {
    LceAnimatorHelper.showErrorView(loadingView, contentView, errorView);
  }
}
