package github.tornaco.practice.honeycomb.sdk;

import android.os.Binder;
import android.os.RemoteException;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.UUID;

import androidx.test.InstrumentationRegistry;
import androidx.test.runner.AndroidJUnit4;
import github.tornaco.practice.honeycomb.sdk.data.PreferenceManager;

@RunWith(AndroidJUnit4.class)
public class HoneyCombManagerTest {

    @Test
    public void local() {
        Assert.assertNotNull(HoneyCombManager.global());
    }

    @Test
    public void addService() throws RemoteException {
        String name = UUID.randomUUID().toString();
        HoneyCombManager.global().addService(name, new Binder());
        Assert.assertNotNull(HoneyCombManager.global().getService(name));
    }

    @Test
    public void deleteService() throws RemoteException {
        String name = UUID.randomUUID().toString();
        HoneyCombManager.global().addService(name, new Binder());
        Assert.assertNotNull(HoneyCombManager.global().getService(name));
        HoneyCombManager.global().deleteService(name);
        Assert.assertNull(HoneyCombManager.global().getService(name));
    }

    @Test
    public void hasService() throws RemoteException {
        String name = UUID.randomUUID().toString();
        HoneyCombManager.global().addService(name, new Binder());
        Assert.assertTrue(HoneyCombManager.global().hasService(name));
    }

    @Test
    public void getPreferenceManager() throws RemoteException {
        PreferenceManager preferenceManager = HoneyCombManager.global()
                .getPreferenceManager(InstrumentationRegistry.getTargetContext().getPackageName());
        String name = UUID.randomUUID().toString();
        Assert.assertTrue(preferenceManager.putString(name, "Hello~!"));
        Assert.assertEquals(preferenceManager.getString(name, null), "Hello~!");
    }
}