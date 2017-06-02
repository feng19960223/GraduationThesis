package com.fgr.aabao.fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.fgr.aabao.R;
import com.fgr.aabao.gson.Forecast;
import com.fgr.aabao.gson.Weather;
import com.fgr.aabao.service.AutoUpdateService;
import com.fgr.aabao.ui.CityActivity;
import com.fgr.aabao.utils.HttpUtils;
import com.fgr.aabao.utils.Utility;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * 作者：Fgr on 2017/4/30 09:30
 * 邮箱：594878760@qq.com
 * 最新修改日期/修改人员：
 * 说明：天气
 */

public class WeatherFragment extends BaseFragment implements View.OnClickListener, SwipeRefreshLayout.OnRefreshListener {

    private ScrollView weatherLayout;
    private TextView titleCity;
    private TextView titleUpdateTime;
    private TextView degreeText;
    private TextView weatherInfoText;
    private LinearLayout forecastLayout;
    private TextView aqiText;
    private TextView pm25Text;
    private TextView comfortText;
    private TextView carWashText;
    private TextView sportText;
    private ImageView bingPicImg;
    private SwipeRefreshLayout swipeRefresh;
    private String weatherId;
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;


    @Override
    public void init() {
        weatherLayout = (ScrollView) contentView.findViewById(R.id.weather_layout);
        titleCity = (TextView) contentView.findViewById(R.id.title_city);
        titleUpdateTime = (TextView) contentView.findViewById(R.id.title_update_time);
        degreeText = (TextView) contentView.findViewById(R.id.degree_text);
        weatherInfoText = (TextView) contentView.findViewById(R.id.weather_info_text);
        forecastLayout = (LinearLayout) contentView.findViewById(R.id.forecast_layout);
        aqiText = (TextView) contentView.findViewById(R.id.aqi_text);
        pm25Text = (TextView) contentView.findViewById(R.id.pm25_text);
        comfortText = (TextView) contentView.findViewById(R.id.comfort_text);
        carWashText = (TextView) contentView.findViewById(R.id.car_wash_text);
        sportText = (TextView) contentView.findViewById(R.id.sport_text);
        bingPicImg = (ImageView) contentView.findViewById(R.id.bing_pic_img);
        swipeRefresh = (SwipeRefreshLayout) contentView.findViewById(R.id.sf_weather);
        swipeRefresh.setColorSchemeResources(R.color.colorPrimary, R.color.colorAccent, R.color.colorPrimaryDark, R.color.colorAccent);
        titleCity.setOnClickListener(this);// 点击城市，切换地名
        swipeRefresh.setOnRefreshListener(this);
        pref = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String weatherString = pref.getString("weather", null);
        if (weatherString != null) {
            // 有缓存时直接解析天气数据
            Weather weather = Utility.handleWeatherResponse(weatherString);
            weatherId = weather.basic.weatherId;
            showWeatherInfo(weather);
        } else {
            // 无缓存时去服务器查询天气
            if (isStart) {
                isStart = false;
                weatherLayout.setVisibility(View.INVISIBLE);
                Intent intent = new Intent(getActivity(), CityActivity.class);
                startActivityForResult(intent, 1);
            }
        }
        String bingPic = pref.getString("bing_pic", null);
        if (bingPic != null) {
            Glide.with(getActivity()).load(bingPic).into(bingPicImg);
        } else {
            loadBingPic();
        }
    }

    private boolean isStart = true;

    /**
     * 处理并显示Weather实体类中是数据
     */
    public void showWeatherInfo(Weather weather) {
        String cityName = weather.basic.cityName;
        String updateTime = weather.basic.update.updateTime.split(" ")[1];
        String degree = weather.now.temperature + "℃";
        String weatherInfo = weather.now.more.info;
        titleCity.setText(cityName);
        titleUpdateTime.setText(updateTime);
        degreeText.setText(degree);
        weatherInfoText.setText(weatherInfo);
        forecastLayout.removeAllViews();
        for (Forecast forecast : weather.forecastList) {
            View view = LayoutInflater.from(getActivity()).inflate(R.layout.item_forecast, forecastLayout, false);
            TextView dateText = (TextView) view.findViewById(R.id.date_text);
            TextView infoText = (TextView) view.findViewById(R.id.info_text);
            TextView maxText = (TextView) view.findViewById(R.id.max_text);
            TextView minText = (TextView) view.findViewById(R.id.min_text);
            dateText.setText(forecast.date);
            infoText.setText(forecast.more.info);
            maxText.setText(forecast.temperature.max);
            minText.setText(forecast.temperature.min);
            forecastLayout.addView(view);
        }
        if (weather.aqi != null) {
            aqiText.setText(weather.aqi.city.aqi);
            pm25Text.setText(weather.aqi.city.pm25);
        }
        String comfort = "舒适度:" + weather.suggestion.comfort.info;
        String carWash = "洗车指数:" + weather.suggestion.carWash.info;
        String sport = "运动建议:" + weather.suggestion.sport.info;
        comfortText.setText(comfort);
        carWashText.setText(carWash);
        sportText.setText(sport);
        weatherLayout.setVisibility(View.VISIBLE);
        Intent intent = new Intent(getActivity(), AutoUpdateService.class);// 开启后台服务，每8小时更新一次数据
        getActivity().startService(intent);
    }

    /**
     * 根据天气id请求城市天气信息
     */
    public void requestWeather(final String weatherId) {
        String weatherUrl = "http://guolin.tech/api/weather?cityid=" + weatherId + "&key=cb7f48050b854b5b97d5c5903bd51175";
        HttpUtils.sendOkHttpRequest(weatherUrl, new Callback() {
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String responseText = response.body().string();
                final Weather weather = Utility.handleWeatherResponse(responseText);
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (weather != null && "ok".equals(weather.status)) {
                            editor = pref.edit();
                            editor.putString("weather", responseText);
                            editor.apply();
                            showWeatherInfo(weather);
                        } else {
                            Toast.makeText(getActivity(), R.string.string_weather_no, Toast.LENGTH_SHORT).show();
                        }
                        swipeRefresh.setRefreshing(false);
                    }
                });
            }

            @Override
            public void onFailure(Call call, IOException e) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getActivity(), R.string.string_weather_no, Toast.LENGTH_SHORT).show();
                        swipeRefresh.setRefreshing(false);// 刷新事件结束，并隐藏刷新进度条
                    }
                });
            }
        });
        loadBingPic();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (resultCode) {
            case 1:
                weatherId = data.getStringExtra("Weather_id");
                requestWeather(weatherId);
                break;
            default:
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.title_city:
                Intent intent = new Intent(getActivity(), CityActivity.class);
                startActivityForResult(intent, 1);
                break;
            default:
        }
    }

    /**
     * 加载必应每日一图
     */
    private void loadBingPic() {
        String requestBingPic = "http://guolin.tech/api/bing_pic";
        HttpUtils.sendOkHttpRequest(requestBingPic, new Callback() {
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String bingPic = response.body().string();
                editor = pref.edit();
                editor.putString("bing_pic", bingPic);
                editor.apply();
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Glide.with(getActivity()).load(bingPic).into(bingPicImg);
                    }
                });
            }

            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    public void onRefresh() {
        requestWeather(weatherId);
    }
}