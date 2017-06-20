package com.bc_manga2.Presenter;

import com.bc_manga2.View.maView;

import io.reactivex.disposables.CompositeDisposable;

public abstract class BasePresenter<V extends maView> {
	
    private CompositeDisposable disposable;

    private V view;

    public BasePresenter(V view) {
    	this.view = view;
    	setView(view);
    }

    public CompositeDisposable getDisposable() {
        return disposable;
    }

    public V getView() {
        return view;
    }

    public void setView(V view) {
    	disposable = new CompositeDisposable();
    	this.view = view;
    }

    public void destroy() {
    	try {
    		disposable.dispose();
		} catch (Exception e) { }
    	
        this.view = null;
    }
}
