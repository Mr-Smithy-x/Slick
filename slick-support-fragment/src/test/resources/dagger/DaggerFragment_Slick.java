package test;

import android.support.v4.app.Fragment;

import com.github.slick.InternalOnDestroyListener;
import com.github.slick.supportfragment.SlickDelegateFragment;

import java.lang.Override;
import java.lang.String;
import java.util.HashMap;

public class DaggerFragment_Slick implements InternalOnDestroyListener {

    private static DaggerFragment_Slick hostInstance;
    private final HashMap<String, SlickDelegateFragment<ExampleView, ExamplePresenter>> delegates = new HashMap<>();

    public static <T extends Fragment & ExampleView> void bind(T daggerFragment) {
        final String id = SlickDelegateFragment.getId(daggerFragment);
        if (hostInstance == null) hostInstance = new DaggerFragment_Slick();
        SlickDelegateFragment<ExampleView, ExamplePresenter> delegate = hostInstance.delegates.get(id)
        if (delegate == null) {
            final ExamplePresenter presenter = ((DaggerFragment) daggerFragment).provider.get();
            delegate = new SlickDelegateFragment<>(presenter, daggerFragment.getClass(), id);
            delegate.setListener(hostInstance);
            hostInstance.delegates.put(id, delegate);
        }
        daggerFragment.getFragmentManager().registerFragmentLifecycleCallbacks(delegate, false);
        ((DaggerFragment) daggerFragment).presenter = delegate.getPresenter();
    }

    @Override
    public void onDestroy(String id) {
        hostInstance.delegates.remove(id);
        if (hostInstance.delegates.size() == 0) {
            hostInstance = null;
        }
    }
}