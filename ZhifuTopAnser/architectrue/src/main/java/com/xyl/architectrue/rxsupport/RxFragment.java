package com.xyl.architectrue.rxsupport;

import android.os.Bundle;
import android.support.annotation.CheckResult;
import android.support.v4.app.Fragment;
import android.view.View;

import com.trello.rxlifecycle.LifecycleProvider;
import com.trello.rxlifecycle.LifecycleTransformer;
import com.trello.rxlifecycle.RxLifecycle;
import com.trello.rxlifecycle.android.FragmentEvent;
import com.trello.rxlifecycle.android.RxLifecycleAndroid;

import javax.annotation.Nonnull;

import rx.Observable;
import rx.subjects.BehaviorSubject;

/**
 * User: ShaudXiao
 * Date: 2017-01-03
 * Time: 17:14
 * Company: zx
 * Description:
 * FIXME
 */


public class RxFragment extends Fragment implements LifecycleProvider<FragmentEvent> {

    private final BehaviorSubject<FragmentEvent> lifeCycleSubject = BehaviorSubject.create();

    @Nonnull
    @Override
    @CheckResult
    public Observable<FragmentEvent> lifecycle() {
        return lifeCycleSubject.asObservable();
    }

    @Nonnull
    @Override
    @CheckResult
    public <T> LifecycleTransformer<T> bindUntilEvent(@Nonnull FragmentEvent event) {
        return RxLifecycle.bindUntilEvent(lifeCycleSubject, event);
    }

    @Nonnull
    @Override
    @CheckResult
    public <T> LifecycleTransformer<T> bindToLifecycle() {
        return RxLifecycleAndroid.bindFragment(lifeCycleSubject);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        lifeCycleSubject.onNext(FragmentEvent.CREATE);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        lifeCycleSubject.onNext(FragmentEvent.CREATE_VIEW);
    }

    @Override
    public void onStart() {
        super.onStart();
        lifeCycleSubject.onNext(FragmentEvent.START);
    }

    @Override
    public void onResume() {
        super.onResume();
        lifeCycleSubject.onNext(FragmentEvent.RESUME);
    }

    @Override
    public void onPause() {
        super.onPause();
        lifeCycleSubject.onNext(FragmentEvent.PAUSE);
    }

    @Override
    public void onStop() {
        super.onStop();
        lifeCycleSubject.onNext(FragmentEvent.STOP);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        lifeCycleSubject.onNext(FragmentEvent.DESTROY_VIEW);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        lifeCycleSubject.onNext(FragmentEvent.DESTROY);
    }
}
