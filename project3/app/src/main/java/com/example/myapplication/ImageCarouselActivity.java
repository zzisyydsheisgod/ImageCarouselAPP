package com.example.myapplication;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.LinearLayout;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

//定义图片轮播界面Activity类，继承自AppCompatActivity
public class ImageCarouselActivity extends AppCompatActivity {

    //声明ViewPager2组件，用于实现图片轮播效果
    private ViewPager2 viewPager;

    //声明控制按钮，用于控制自动轮播的开始和停止
    private Button btnControl;


    private Button leftBtn;

    private Button rightBtn;


    //声明拖动条布局
    private SeekBar seekBar;
    private LinearLayout seekBarLayout;

    //定义图片资源数组，存储图片资源
    private int[] images = {
        R.drawable.castorice,
        R.drawable.genies,
        R.drawable.heita,
        R.drawable.kafka,
        R.drawable.ruanmei
    };

    //显示单张图片
    //bg.setImageResource(R.drawable.图片名)
    

    //定义一个Handler对象，用于处理延时任务和线程间通信
    private Handler handler = new Handler();

    //声明布尔型变量，用于标记ViewPager是否正在滚动
    private boolean isScrolling = false;

    //声明整形变量，用于记录当前显示图片的位置 
    private int currentPosition = 0;

    //声明布尔型变量，用于标记是否正在进行轮播
    boolean isAutoPlaying = false;

    //创建Runnable对象，用于执行自动轮播任务
    private Runnable runnable = new Runnable() {

    //重写run方法，定义需要执行的任务
    @Override
        public void run() {

        //判断ViewPager是否正在滚动，如果没有滚动则执行自动切换
        if (!isScrolling && isAutoPlaying) {

            //获取当前显示的图片位置
            currentPosition = viewPager.getCurrentItem();

            //计算下一个要显示的图片位置
            int nextPosition = (currentPosition + 1);

            //设置ViewPager显示下一张图片，第二个参数为true表示启用平滑滚动效果
            viewPager.setCurrentItem(nextPosition, true);
        }

        // 延迟1秒后继续执行
        handler.postDelayed(this, 1000); // 每1秒自动切换
    }
    };

    //重写onCreate方法，设置布局文件并初始化ViewPager组件
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //调用父类的onCreate()方法，完成Activity正确创建
        super.onCreate(savedInstanceState);

        //启用边缘到边缘显示效果，使内容可以延伸到状态栏和导航栏区域
        EdgeToEdge.enable(this);

        //设置Activity的布局文件，将UI界面显示出来
        setContentView(R.layout.activity_image_carousel);

        //设置窗口
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        
        //通过findViewById()方法获取布局文件中的ViewPager2组件
        viewPager = findViewById(R.id.viewPager);

        //通过findViewById()方法获取布局文件中的按钮文件
        btnControl = findViewById(R.id.btn_control);

        leftBtn = findViewById(R.id.left_btn);

        rightBtn = findViewById(R.id.right_btn);


        //获取拖动条和布局
        seekBar = findViewById(R.id.seek_bar);
        seekBarLayout  = findViewById(R.id.seek_bar_layout);

        //创建图片轮播适配器对象
        ImageCarouselAdapter adapter = new ImageCarouselAdapter();

        //为ViewPager2设置适配器
        viewPager.setAdapter(adapter);

        //为ViewPager2注册页面改变回调监听器，用于监听页面滚动状态和选择事件
        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            //重写onPageScrollStateChanged()方法，用于监听页面滚动状态
            @Override

            public void onPageScrollStateChanged(int state) {

                //调用父类方法
                super.onPageScrollStateChanged(state);

                //更新滚动状态，当转台不为SCROLL_STATE_IDLE时，表示正在滚动
                isScrolling = (state != ViewPager2.SCROLL_STATE_IDLE);
                
                // 当滚动结束时，检查是否需要跳转到正确的位置（实现无限循环）
                if (state == ViewPager2.SCROLL_STATE_IDLE) {
                    int index = viewPager.getCurrentItem() % images.length;
                    if (index == 0 && currentPosition > 0) {
                        // 跳转到第一个实际位置
                        viewPager.setCurrentItem(images.length, false);
                    } else if (index == images.length - 1 && currentPosition < viewPager.getCurrentItem()) {
                        // 跳转到最后一个实际位置之前的循环位置
                        viewPager.setCurrentItem(images.length - 1 + images.length, false);
                    }
                }
            }

            //重写onPageSelected()方法，监听页面选择事件
            @Override

            //调用父类方法
            public void onPageSelected(int position) {

                //调用父类方法
                super.onPageSelected(position);

                //更新当前位置
                currentPosition = position;

            //更新拖动条位置
            if (seekBar != null && !isAutoPlaying) {
                seekBar.setProgress(position % images.length);
                }                
            }
        });
        
        // 设置初始位置为中间的某个位置，确保可以向前和向后滚动
        viewPager.post(() -> viewPager.setCurrentItem(images.length * 100, false));


    //设置拖动条监听器
    seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            if (fromUser) {
                //用户拖动时更新图片位置
                viewPager.setCurrentItem(images.length * 100 + progress, true);
            }
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {
            //开始拖动时不需要特殊处理
        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            //停止拖动时不需要特殊处理
        }
    });


        //为按钮设置点击监听器，用于切换自动播放状态
        btnControl.setOnClickListener(new View.OnClickListener(){
            @Override

            //为按钮设置点击监听器，用于切换自动播放状态
            public void onClick(android.view.View v){

                //判断当前是否正在自动播放
                if(isAutoPlaying){

                    //停止自动播放
                    stopAutoCarousel();

                    //更改按钮文本为"开始轮播"
                    btnControl.setText("开始轮播");

                    //更新自动播放为false
                    isAutoPlaying = false;

                    //显示左右切换按钮
                    leftBtn.setVisibility(View.VISIBLE);
                    rightBtn.setVisibility(View.VISIBLE);
                
                    //显示拖动条
                    seekBarLayout.setVisibility(View.VISIBLE);
                }else{

                    //开始自动播放
                    startAutoCarousel();

                    //更改按钮文本为"停止轮播"
                    btnControl.setText("停止轮播");

                    //更新自动播放为true
                    isAutoPlaying = true;

                    //隐藏左右切换按钮
                    leftBtn.setVisibility(View.GONE);
                    rightBtn.setVisibility(View.GONE);

                    //隐藏拖动条
                    seekBarLayout.setVisibility(View.GONE);
                }
            }
        });



        //设置左侧按钮点击事件监听
        leftBtn.setOnClickListener(new View.OnClickListener(){

            @Override

            public void onClick(View v){

                int previousPosition = viewPager.getCurrentItem() - 1;

                //设置显示上一张图片，第二个参数表示运行平滑滑动
                viewPager.setCurrentItem(previousPosition,true);
            }
        });

        //设置右侧按钮点击事件监听
        rightBtn.setOnClickListener(new View.OnClickListener(){

            //重写onClick()方法，定义按钮点击事件
            @Override

            public void onClick(View v){

                //判断ViewPager是否正在滚动
                if(!isScrolling){

                    //计算下一张图片的位置
                    int nextPosition = viewPager.getCurrentItem() + 1;

                    //设置ViewPager显示下一张图片，第二个参数true表示启用平滑滚动效果
                    viewPager.setCurrentItem(nextPosition,true);
                }
            }
        });


        //初始化时隐藏左右切花按钮
        leftBtn.setVisibility(View.GONE);
        rightBtn.setVisibility(View.GONE);
        
        //初始化时隐藏拖动条
        seekBarLayout.setVisibility(View.GONE);

        // 开始自动轮播
        startAutoCarousel();

    }
    
    //定义开始自动轮播方法
    private void startAutoCarousel() {

        //设置自动播放状态为true
        isAutoPlaying = true;

        //延时1秒后执行runnable任务，开始自动轮播
        handler.postDelayed(runnable,1000);

        //确保btnControl不为null再设置文本
        if(btnControl != null) {
            
        //更新按钮文本为"停止轮播"
        btnControl.setText("停止轮播");
        }
    }



    

    //定义停止自动轮播的方法
    private void stopAutoCarousel() {
        
        //设置自动播放状态为false
        isAutoPlaying = false;

        //确保btnControl不为null再设置文本
        if(btnControl != null) {
            //更新按钮文本为"开始轮播"
            btnControl.setText("开始轮播");
        }

        //移除runnable任务，停止自动轮播
        handler.removeCallbacks(runnable);
     
    }


    //切换自动播放状态的方法
    private void toggleAutoPlay(){

        //根据当前自动播放状态决定执行开始还是停止操作
        if(isAutoPlaying){

            //如果正在自动播放，则停止
            stopAutoCarousel();

        }else{

            //如果未自动播放，则开始
            startAutoCarousel();
            }
    }

    
    //重写onPause()方法，这是Activity生命周期方法，当Activity重新获得焦点时调用
    @Override
    protected void onPause() {

        //调用父类方法
        super.onPause();
        stopAutoCarousel(); // 页面不可见时停止轮播，节省资源
    }
    
    //重写onResume()方法，这是Activity生命周期方法，当Activity重新获得焦点时调用
    @Override
    protected void onResume() {
        super.onResume();

        //页面可见时，如果之前是自动播放状态，则回复自动播放
        if (isAutoPlaying) {
            startAutoCarousel();// 页面可见时开始轮播
        } 
    }
    
    // 图片轮播适配器
    private class ImageCarouselAdapter extends RecyclerView.Adapter<ImageCarouselAdapter.ImageViewHolder> {
        
        @Override
        public ImageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            ImageView imageView = new ImageView(ImageCarouselActivity.this);
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setLayoutParams(new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT));
            return new ImageViewHolder(imageView);
        }
        
        @Override
        public void onBindViewHolder(ImageViewHolder holder, int position) {
            // 使用模运算确保位置在有效范围内
            int actualPosition = position % images.length;
            ((ImageView) holder.itemView).setImageResource(images[actualPosition]);
        }
        
        @Override
        public int getItemCount() {
            // 返回一个大的数字以支持无限循环滚动
            return Integer.MAX_VALUE;
        }
        
        class ImageViewHolder extends RecyclerView.ViewHolder {
            public ImageViewHolder(View itemView) {
                super(itemView);
            }
        }
    }
}