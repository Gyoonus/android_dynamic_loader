if [ x$1 == x ]; then
    echo "need one arg"
    echo "example : . ./test.sh Multidex"
    return 1
fi

PWD=$(pwd)
echo $PWD

export PROJ=$PWD/$1
export ANDROID_JAR=$PWD/prebuilt/sdk/26/android.jar
export SRC=$PROJ/src
export OBJ=$PROJ/obj
export BIN=$PROJ/bin
export PATH=$PWD/prebuilt:$PATH
export LD_LIBRARY_PATH=$PWD/lib

aapt package -f -m -J $PROJ/src -M $PROJ/AndroidManifest.xml -S $PROJ/res -I $ANDROID_JAR

javac -d $OBJ -classpath $SRC -bootclasspath $ANDROID_JAR $SRC/com/example/helloandroid/*.java
dx --dex --output=$PROJ/bin/classes.dex $PROJ/obj

aapt package -f -m -F $PROJ/bin/hello.unaligned.apk -M $PROJ/AndroidManifest.xml -S $PROJ/res -I $ANDROID_JAR
cp $BIN/classes.dex .
aapt add $BIN/hello.unaligned.apk classes.dex
zipalign -f 4 $PROJ/bin/hello.unaligned.apk $PROJ/bin/hello.apk

if [ ! -f "mykey.keystore" ]
    then
    keytool -genkeypair -validity 365 -keystore mykey.keystore -keyalg RSA -keysize 2048
fi

apksigner sign --ks mykey.keystore $BIN/hello.apk

if [ -f "classes.dex" ]
    then
    rm -rf classes.dex
fi


