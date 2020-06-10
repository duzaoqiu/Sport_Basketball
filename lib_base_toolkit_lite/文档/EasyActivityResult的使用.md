# EasyActivityResult
一个简化在Activity和Fragment中startActivityForResult()的工具类

### step1

``` java
public class MainActivity extends AppCompatActivity {
    
    ...
        @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        EasyActivityResult.dispatch(this, requestCode, resultCode, data);
    }
    
        @Override
    protected void onDestroy() {
        EasyActivityResult.releaseActivity(this);
        super.onDestroy();
    }
    
    ...
}
```

### step2

``` java
        EasyActivityResult.startActivity(this, intent, new OnActivityResultListener() {
            @Override
            public void onActivityResult(int resultCode, Intent data) {
                if (resultCode == RESULT_OK) {
                    ...
                } else {
                    ...
                }
            }
        });
```

### other
在Fragment中的使用同理类似

