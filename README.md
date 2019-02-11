# android dynamic loader
## android Application Dynamic Loader Using InmemoryDexClassLoader


# How to Use
## build example
    . ./build.sh Multidex

Adjusting in Android 8.0(Oreo)

## ClassLoader Routine in Android
java
dalvik.system.Dexfile.openDexFile()
    c++
    DexFile_openDexFileNative
    runtime->GetOatFileManager().OpenDexFilesFromOat
    context = ClassLoaderContext::CreateContextForClassLoader(class_loader, dex_elements);
    std::unique_ptr<ClassLoaderContext> result(new ClassLoaderContext(/*owns_the_dex_files*/ false));
    if (result->AddInfoToContextFromClassLoader(soa, h_class_loader, h_dex_elements))

## Inheritance structure

 java.lang.Object
 ↳    java.lang.ClassLoader
    ↳    dalvik.system.BaseDexClassLoader
        ↳    dalvik.system.InMemoryDexClassLoader



java.lang.Object
    ↳    java.lang.ClassLoader
        ↳    dalvik.system.BaseDexClassLoader
            ↳    dalvik.system.PathClassLoader
                ↳    dalvik.system.DelegateLastClassLoader


java.lang.Object
    ↳ java.lang.ClassLoader
        ↳    dalvik.system.BaseDexClassLoader
            ↳    dalvik.system.DexClassLoader

java.lang.Object
    ↳ java.lang.ClassLoader
        ↳    dalvik.system.BaseDexClassLoader
            ↳    dalvik.system.PathClassLoader
                ↳    dalvik.system.DelegateLastClassLoader


기존 ClassLoader parent classloader -> child classloader
DelegateLastClassLoader child classloader -> parent classloader
            업캐스팅 후 다운캐스팅(x)
