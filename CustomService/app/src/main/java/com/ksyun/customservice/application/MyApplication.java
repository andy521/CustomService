package com.ksyun.customservice.application;

import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.view.WindowManager;

import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiskCache;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

import java.io.File;


public class MyApplication extends Application {

    public static String cache_image_path, photo_path;
    public static File cacheImageDir, photoDir;
    private static MyApplication instance;
    public static int screenWidth, screenHeight;
    public static String loginShare = "";
    public static Context applicationContext;
    private String myName, myPhoto;


    public static synchronized MyApplication getInstance() {
        if (instance == null) {
            instance = new MyApplication();
        }
        return instance;
    }

    public void onCreate() {
        super.onCreate();
        initImageLoader(this);
        applicationContext = this;
        getScreenDimension();
    }

    public void getScreenDimension() {
        WindowManager mWM = ((WindowManager) getApplicationContext()
                .getSystemService(Context.WINDOW_SERVICE));
        DisplayMetrics mDisplayMetrics = new DisplayMetrics();
        mWM.getDefaultDisplay().getMetrics(mDisplayMetrics);
        screenWidth = mDisplayMetrics.widthPixels;
        screenHeight = mDisplayMetrics.heightPixels;
    }


    private void initImageLoader(Context context) {
        File cacheDir = createCacheDir();
//		 listView.setOnScrollListener(new PauseOnScrollListener(imageLoader,
//		 true, true));//滚动时是否加载图片
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisk(true).imageScaleType(ImageScaleType.IN_SAMPLE_INT)
                .bitmapConfig(Bitmap.Config.RGB_565)
                        // .showStubImage(R.drawable.ic_stub)
                        // .showImageOnLoading(R.drawable.ic_launcher)
                        // .showImageForEmptyUri(R.drawable.kedou)
                        // .showImageOnFail(R.drawable.k2k2k2k)
                .displayer(new RoundedBitmapDisplayer(10))
                .build();

        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
                context)
                .threadPriority(Thread.NORM_PRIORITY - 1)
                .threadPoolSize(5)
                .memoryCacheExtraOptions(480, 800)
                .denyCacheImageMultipleSizesInMemory()
                .diskCacheFileNameGenerator(new Md5FileNameGenerator())
                .tasksProcessingOrder(QueueProcessingType.LIFO)
                .memoryCacheSize(1 * 1024 * 1024)
                .diskCacheSize(50 * 1024 * 1024)
                        // .discCacheFileCount(200)
                        // .defaultDisplayImageOptions(options)
                .diskCache(new UnlimitedDiskCache(cacheDir)).build();

        // Initialize ImageLoader with configuration.
        ImageLoader imageLoader = ImageLoader.getInstance();
        imageLoader.init(config);
    }

    private File createCacheDir() {

        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            File sdcardDir = Environment.getExternalStorageDirectory();
            cache_image_path = sdcardDir.getPath() + "/CustomService/cache/images/";
            cacheImageDir = new File(cache_image_path);
            photo_path = sdcardDir.getPath() + "/CustomService/cache/photoes/";
            photoDir = new File(photo_path);
        } else {
            cacheImageDir = new File("/CustomService/cache/images");
            photoDir = new File("/CustomService/cache/photoes");
        }
        if (!cacheImageDir.exists()) {
            cacheImageDir.mkdirs();
        }
        if (!photoDir.exists()) {
            photoDir.mkdirs();
        }
        return cacheImageDir;
    }

	/*public void addActivity(Activity activity) {
        activityList.add(activity);
	}

	public void exit() {
		for (Activity activity : activityList) {
			activity.finish();
		}
		System.exit(0);
	}*/


}
