package com.example.helloandroid;

import android.app.Application;
import android.app.Activity;
import android.app.Instrumentation;


import android.os.Bundle;
import android.util.Log;

import android.content.pm.PackageManager;
import android.content.pm.ApplicationInfo;
import android.content.Context;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;

public class MainApplication extends Application {
    private static final String TAG = "MultiDex";
    private Application application = this;
    private InMemoryDexClassLoaderTest inmemorydexclassloadertest = null;
    private String appClassName = null;


    @Override
        protected void attachBaseContext(Context base)
        {
            super.attachBaseContext(base);
            inmemorydexclassloadertest = new InMemoryDexClassLoaderTest();
            try
            {
                inmemorydexclassloadertest.setUp(this);
                Log.v(TAG, "setUp()");
            }
            catch(Exception e)
            {
                e.printStackTrace();
            }
        }


    @Override
        public void onCreate() {
            super.onCreate();
            try
            {
                ClassLoader cl = inmemorydexclassloadertest.createLoaderDirect();
                String packageName = this.getPackageName();
                Log.v(TAG, "packageName : "+ packageName);
                Object mLoadedApk = getFieldOjbect("android.app.Application", (Object)this, "mLoadedApk");
                
                Object currentActivityThread = invokeStaticMethod("android.app.ActivityThread", "currentActivityThread", new Class[]{}, new Object[]{});
                Object oldApplication = getFieldOjbect("android.app.ActivityThread", currentActivityThread, "mInitialApplication");
                ArrayList<Application> mAllApplications = (ArrayList<Application>)getFieldOjbect("android.app.ActivityThread", currentActivityThread, "mAllApplications");
                
                
                for (Application mApplication : mAllApplications)
                {
                    mApplication.toString();
                    if( mApplication == this )
                    {
                        Log.v(TAG, "delete app : " + mApplication.toString());
                        mAllApplications.remove(this);
                        Log.v(TAG, "old app : " + oldApplication.toString());

                    }
                    
                }

                ApplicationInfo mApplicationInfo = (ApplicationInfo)getFieldOjbect("android.app.LoadedApk", mLoadedApk, "mApplicationInfo");
                mApplicationInfo.className = null;
                
                //Log.v(TAG, "LoadedApk.mApplicationInfo.className : " + className);
                //setFieldOjbect("android.app.ActivityThread", "mInitialApplication", currentActivityThread, null);
                setFieldOjbect("android.app.LoadedApk", "mApplication", mLoadedApk, null);
                setFieldOjbect("android.app.LoadedApk", "mClassLoader", mLoadedApk, cl);


                Instrumentation instrumentation = new Instrumentation();
                //instrumentation.newApplication( cl, 
                Application app1 = (Application) invokeMethod("android.app.LoadedApk", "makeApplication", mLoadedApk, new Class[] { boolean.class, Instrumentation.class }, new Object[] { false, null });
                

                
                Object mSysLoadedApk = getFieldOjbect("android.app.Application", app1, "mLoadedApk");
                ClassLoader sys_cl = (ClassLoader)getFieldOjbect("android.app.LoadedApk", mSysLoadedApk, "mClassLoader");
                ClassLoader test_cl = (ClassLoader) invokeMethod("android.app.LoadedApk", "getClassLoader", mLoadedApk, new Class[]{}, new Object[]{});
                Log.v(TAG, "sys_cl : " + sys_cl.toString());
                Log.v(TAG, "test_cl: " + test_cl.toString());


                app1.onCreate();

            }
            catch(Exception e)
            {
                e.printStackTrace();
                Log.v(TAG, "setUp() failed");
            }
        }

    public Object getFieldOjbect(String class_name, Object obj, String filedName) {
        try {
            Class<?> obj_class = Class.forName(class_name);
            Field field = obj_class.getDeclaredField(filedName);
            field.setAccessible(true);
            return field.get(obj);
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;

    }
    public Object invokeStaticMethod(String class_name, String method_name, Class[] pareTyple, Object[] pareVaules) {

        try {
            Class<?> obj_class = Class.forName(class_name);
            Method method = obj_class.getMethod(method_name, pareTyple);
            return method.invoke(null, pareVaules);
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;

    }
    public Object invokeMethod(String class_name, String method_name,
            Object obj, Class[] pareTyple, Object[] pareVaules) {

        try {
            Class<?> obj_class = Class.forName(class_name);
            Method method = obj_class.getMethod(method_name, pareTyple);
            return method.invoke(obj, pareVaules);
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;

    }
    public void setFieldOjbect(String classname, String filedName, Object obj,
            Object filedVaule) {
        try {
            Class<?> obj_class = Class.forName(classname);
            Field field = obj_class.getDeclaredField(filedName);
            field.setAccessible(true);
            field.set(obj, filedVaule);
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

}
