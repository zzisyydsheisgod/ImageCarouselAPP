package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import androidx.viewpager2.widget.ViewPager2;

import android.widget.EditText;


public class activity_login extends AppCompatActivity {

    //定义常量，存储正确的用户名和密码
    private static final String CORRECT_USERNAME  = "admin";
    private static final String CORRECT_PASSWORD = "123456";

    //声明EditText变量，用于引用布局文件中的用户名和密码输入框
    private EditText etUsername;//用户名输入框
    private EditText etPassword;//密码输入框


    //重写onCreate()方法,这是Activity生命周期的第一个方法，在Activity创建时调用
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //调用父类的onCreate()方法，完成Activityd正确初始化
        super.onCreate(savedInstanceState);

        //启用边缘到边缘显示效果，将UI界面显示出来
        EdgeToEdge.enable(this);

        //设置Activity的布局文件，将UI界面显示出来
        setContentView(R.layout.activity_login);

        //设置窗口插入区域的监听器，用于处理状态栏和导航栏区域的显示
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            
            //获取系统栏的插入区域信息（状态栏、导航栏等）
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            
            //为视图设置内边距，确保内容不会被系统栏遮挡
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
           
            //返回插入区域的信息
            return insets;
        });


        //获取用户名和密码输入框
        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword); 

        //通过findViewById()方法获取布局文件中的登录按钮，并赋值变量给BTN_OK
        Button BTN_OK = findViewById(R.id.login_ok);

        //为登录按钮设置点击事件监听器
        BTN_OK.setOnClickListener(new View.OnClickListener() {
            //重写onClick()方法，当按钮被点击时会执行这里的代码
            @Override
            public void onClick(View v) {

                //获取用户在用户名输入框中输入的内容，并除去前后空格
                String username = etUsername.getText().toString().trim();

                //获取用户在密码输入框中输入的内容，并除去前后空格
                String password = etPassword.getText().toString().trim();

                //调用validateCredentials()方法，传入用户名和密码，并判断是否正确
                if(validateCredentials(username,password)){

                    //如果验证通过，显示“登录成功”的提示信息
                    Toast.makeText(activity_login.this,"登录成功",Toast.LENGTH_SHORT).show();

                    //创建一个Intent对象，用于从当前Activity跳转到ImageCarouselActivity
                    Intent intent = new Intent(activity_login.this, ImageCarouselActivity.class);
                
                    //启动目标Activity（图片轮播界面）                
                    startActivity(intent);

                    // 关闭登录界面
                    finish(); 

                }else{
                    //如果验证失败，显示登录失败的信息
                    Toast.makeText(activity_login.this,"用户名或密码错误",Toast.LENGTH_SHORT).show();
                }

               


              
            }
        });









        int[] images = {
            R.drawable.castorice,
            R.drawable.genies,
            R.drawable.heita,
            R.drawable.kafka,
            R.drawable.ruanmei
        };


        //通过findViewById()方法获取布局文件中的ViewPager2控件
        ViewPager2 viewPager = findViewById(R.id.viewPager);

        //创建ImagePagerAdapter适配器对象，用于为ViewPager2提供数据
        ImagePagerAdapter adapter = new ImagePagerAdapter(this, images);

        //为ViewPager2设置适配器
        viewPager.setAdapter(adapter);
    }


/**
 * 验证用户输入的用户名和密码是否正确
 * 
 * @param username 用户输入的用户名
 * @param password 用户输入的密码
 * @return 验证结果，正确返回true，否则返回false
 */

 private boolean validateCredentials(String username,String password){

    //比较用户输入的账号密码与预设的是否相同
    return CORRECT_USERNAME.equals(username) && CORRECT_PASSWORD.equals(password);

 }

}



