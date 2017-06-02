package com.fgr.aabao.fragment;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.TextureMapView;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.poi.OnGetPoiSearchResultListener;
import com.baidu.mapapi.search.poi.PoiDetailResult;
import com.baidu.mapapi.search.poi.PoiIndoorResult;
import com.baidu.mapapi.search.poi.PoiNearbySearchOption;
import com.baidu.mapapi.search.poi.PoiResult;
import com.baidu.mapapi.search.poi.PoiSearch;
import com.baidu.mapapi.search.poi.PoiSortType;
import com.baidu.mapapi.search.route.BikingRouteResult;
import com.baidu.mapapi.search.route.DrivingRouteLine;
import com.baidu.mapapi.search.route.DrivingRoutePlanOption;
import com.baidu.mapapi.search.route.DrivingRouteResult;
import com.baidu.mapapi.search.route.IndoorRouteResult;
import com.baidu.mapapi.search.route.MassTransitRouteResult;
import com.baidu.mapapi.search.route.OnGetRoutePlanResultListener;
import com.baidu.mapapi.search.route.PlanNode;
import com.baidu.mapapi.search.route.RoutePlanSearch;
import com.baidu.mapapi.search.route.TransitRouteLine;
import com.baidu.mapapi.search.route.TransitRoutePlanOption;
import com.baidu.mapapi.search.route.TransitRouteResult;
import com.baidu.mapapi.search.route.WalkingRouteLine;
import com.baidu.mapapi.search.route.WalkingRoutePlanOption;
import com.baidu.mapapi.search.route.WalkingRouteResult;
import com.baidu.mapapi.search.share.LocationShareURLOption;
import com.baidu.mapapi.search.share.OnGetShareUrlResultListener;
import com.baidu.mapapi.search.share.ShareUrlResult;
import com.baidu.mapapi.search.share.ShareUrlSearch;
import com.baidu.mapapi.utils.route.BaiduMapRoutePlan;
import com.baidu.mapapi.utils.route.RouteParaOption;
import com.fgr.aabao.R;
import com.fgr.aabao.application.MyApp;
import com.fgr.aabao.baidu.mapapi.overlayutil.DrivingRouteOverlay;
import com.fgr.aabao.baidu.mapapi.overlayutil.PoiOverlay;
import com.fgr.aabao.baidu.mapapi.overlayutil.TransitRouteOverlay;
import com.fgr.aabao.baidu.mapapi.overlayutil.WalkingRouteOverlay;
import com.fgr.aabao.utils.UIUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 作者：Fgr on 2017/4/30 09:30
 * 邮箱：594878760@qq.com
 * 最新修改日期/修改人员：
 * 说明：地图
 */

public class MapFragment extends BaseFragment implements View.OnClickListener, AdapterView.OnItemSelectedListener, BaiduMap.OnMarkerClickListener {
    private TextureMapView mMapView = null;// Android Studio由于模拟器的缘故，MapView可能不法运行，可以改为TextureMapView，或者使用天天模拟器，或者真机
    private BaiduMap baiduMap = null;
    private Spinner spinner = null;
    public LocationClient mLocationClient = null;
    public BDLocationListener myListener = new MyLocationListener();
    private TextView tv_info = null;
    public PoiSearch mPoiSearch = null;
    private EditText et_key = null;
    private RoutePlanSearch mRoutePlanSearch = null;
    private WalkingRouteOverlay walkingRouteOverlay = null;
    private TransitRouteOverlay transitRouteOverlay = null;
    private DrivingRouteOverlay drivingRouteOverlay = null;
    private ShareUrlSearch mShareUrlSearch = null;


    @Override
    public void init() {
        mMapView = (TextureMapView) contentView.findViewById(R.id.mapview_fragment_map);
        contentView.findViewById(R.id.imageview_fragment_map_location).setOnClickListener(this);
        contentView.findViewById(R.id.imageview_fragment_map_search).setOnClickListener(this);
        contentView.findViewById(R.id.button_fragment_map_normal).setOnClickListener(this);
        contentView.findViewById(R.id.button_fragment_map_sate).setOnClickListener(this);
        contentView.findViewById(R.id.b1).setOnClickListener(this);
        contentView.findViewById(R.id.b2).setOnClickListener(this);
        contentView.findViewById(R.id.b3).setOnClickListener(this);
        contentView.findViewById(R.id.b4).setOnClickListener(this);
        contentView.findViewById(R.id.b5).setOnClickListener(this);
        contentView.findViewById(R.id.b6).setOnClickListener(this);
        contentView.findViewById(R.id.b7).setOnClickListener(this);
        contentView.findViewById(R.id.b8).setOnClickListener(this);
        spinner = (Spinner) contentView.findViewById(R.id.spinner_fragment_map);
        tv_info = (TextView) contentView.findViewById(R.id.textview_fragment_map_info);
        et_key = (EditText) contentView.findViewById(R.id.edittext_fragment_map_key);
        List list = Arrays.asList(UIUtils.getStringArray(R.array.map_function));
        ArrayAdapter adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, list);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);
        baiduMap = mMapView.getMap();
        if (mCurrentLocation != null) {
            LatLng latLng = new LatLng(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude());
            MapStatusUpdate mapStatusUpdate = MapStatusUpdateFactory.newLatLngZoom(latLng, 12);
            baiduMap.animateMapStatus(mapStatusUpdate, 1000);
        }
        // 初始化百度定位
        mLocationClient = new LocationClient(getActivity()); // 声明LocationClient类
        mLocationClient.registerLocationListener(myListener); // 注册监听函数
        initLocation();
        List<String> permissionList = new ArrayList<>();
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.READ_PHONE_STATE);
        }
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (!permissionList.isEmpty()) {
            String[] permissions = permissionList.toArray(new String[permissionList.size()]);
            ActivityCompat.requestPermissions(getActivity(), permissions, 1);
        } else {
            requestLocation();
        }
        mPoiSearch = PoiSearch.newInstance();
        mPoiSearch.setOnGetPoiSearchResultListener(poiListener);
        mRoutePlanSearch = RoutePlanSearch.newInstance();
        mRoutePlanSearch.setOnGetRoutePlanResultListener(listener);
        mShareUrlSearch = ShareUrlSearch.newInstance();
        mShareUrlSearch.setOnGetShareUrlResultListener(shareUrlResultListener);
        baiduMap.setOnMarkerClickListener(this);
    }

    private void requestLocation() {
        mLocationClient.start();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0) {
                    for (int result : grantResults) {
                        if (result != PackageManager.PERMISSION_GRANTED) {
                            Toast.makeText(getActivity(), R.string.string_map_must, Toast.LENGTH_SHORT).show();
                            getActivity().finish();
                            return;
                        }
                    }
                    requestLocation();
                } else {
                    Toast.makeText(getActivity(), R.string.string_map_unknown, Toast.LENGTH_SHORT).show();
                    getActivity().finish();
                }
                break;
            default:
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imageview_fragment_map_location:// 移动到定位
                if (mCurrentLocation != null) {
                    LatLng latLng = new LatLng(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude());
                    MapStatusUpdate mapStatusUpdate = MapStatusUpdateFactory.newLatLng(latLng);
                    baiduMap.animateMapStatus(mapStatusUpdate, 1000);
                }
                break;
            case R.id.imageview_fragment_map_search:// 搜索附近
                if (mCurrentLocation == null) {
                    Toast.makeText(getActivity(), R.string.string_map_failure, Toast.LENGTH_SHORT).show();
                    return;
                }
                String key = et_key.getText().toString().trim();
                if (TextUtils.isEmpty(key)) {
                    return;
                }
                PoiNearbySearchOption poiNearbySearchOption = new PoiNearbySearchOption();
                LatLng latLng = new LatLng(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude());
                poiNearbySearchOption.location(latLng);
                poiNearbySearchOption.keyword(key);
                poiNearbySearchOption.pageCapacity(100).pageNum(0).radius(10 * 1000).sortType(PoiSortType.distance_from_near_to_far);
                mPoiSearch.searchNearby(poiNearbySearchOption);
                break;
            case R.id.button_fragment_map_normal:// 普通地图
                baiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
                break;
            case R.id.button_fragment_map_sate:// 卫星地图
                baiduMap.setMapType(BaiduMap.MAP_TYPE_SATELLITE);
                break;
            case R.id.iv_baidu:
                BaiduMapRoutePlan.setSupportWebRoute(true);
                RouteParaOption routeParaOption = new RouteParaOption();
                routeParaOption.cityName(mCurrentLocation.getCity());
                routeParaOption.startPoint(new LatLng(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude()));
                routeParaOption.endPoint(mCurrentMarker.getPosition());
                BaiduMapRoutePlan.openBaiduMapTransitRoute(routeParaOption, getActivity());
                break;
            case R.id.iv_walk:
                WalkingRoutePlanOption walkingRoutePlanOption = new WalkingRoutePlanOption();
                PlanNode planNode = PlanNode.withLocation(new LatLng(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude()));
                PlanNode toPlanNode = PlanNode.withLocation(mCurrentMarker.getPosition());
                walkingRoutePlanOption.from(planNode).to(toPlanNode);
                mRoutePlanSearch.walkingSearch(walkingRoutePlanOption);
                break;
            case R.id.iv_bus:
                TransitRoutePlanOption transitRoutePlanOption = new TransitRoutePlanOption();
                transitRoutePlanOption.city(mCurrentLocation.getCity());
                PlanNode planNode2 = PlanNode.withLocation(new LatLng(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude()));
                PlanNode toPlanNode2 = PlanNode.withLocation(mCurrentMarker.getPosition());
                transitRoutePlanOption.from(planNode2).to(toPlanNode2);
                mRoutePlanSearch.transitSearch(transitRoutePlanOption);
                break;
            case R.id.iv_car:
                DrivingRoutePlanOption drivingRoutePlanOption = new DrivingRoutePlanOption();
                PlanNode planNode3 = PlanNode.withLocation(new LatLng(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude()));
                PlanNode toPlanNode3 = PlanNode.withLocation(mCurrentMarker.getPosition());
                drivingRoutePlanOption.from(planNode3).to(toPlanNode3);
                mRoutePlanSearch.drivingSearch(drivingRoutePlanOption);
                break;
            case R.id.iv_share:
                LocationShareURLOption locationShareURLOption = new LocationShareURLOption();
                locationShareURLOption.location(mCurrentMarker.getPosition());
                locationShareURLOption.name(UIUtils.getString(R.string.string_map_share));
                locationShareURLOption.snippet(UIUtils.getString(R.string.string_map_from));
                mShareUrlSearch.requestLocationShareUrl(locationShareURLOption);
                break;
            default:
                et_key.setText(((Button) view).getText().toString());
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // 在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
        mMapView.onDestroy();
        mLocationClient.stop();
        mPoiSearch.destroy();
    }

    @Override
    public void onResume() {
        super.onResume();
        // 在activity执行onResume时执行mMapView. onResume ()，实现地图生命周期管理
        mMapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        // 在activity执行onPause时执行mMapView. onPause ()，实现地图生命周期管理
        mMapView.onPause();
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
        switch (position) {
            case 0:
                break;
            case 1:
                baiduMap.setTrafficEnabled(true);
                break;
            case 2:
                baiduMap.setTrafficEnabled(false);
                break;
            case 3:
                baiduMap.setBaiduHeatMapEnabled(true);
                break;
            case 4:
                baiduMap.setBaiduHeatMapEnabled(false);
                break;
            default:
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
    }

    private Marker mCurrentMarker;

    @Override
    // 导航到附近
    public boolean onMarkerClick(Marker marker) {
        mCurrentMarker = marker;
        LatLng position = marker.getPosition();
        View view = View.inflate(getActivity(), R.layout.info_window, null);
        ImageView iv_baidu = (ImageView) view.findViewById(R.id.iv_baidu);
        ImageView iv_walk = (ImageView) view.findViewById(R.id.iv_walk);
        ImageView iv_bus = (ImageView) view.findViewById(R.id.iv_bus);
        ImageView iv_car = (ImageView) view.findViewById(R.id.iv_car);
        ImageView iv_share = (ImageView) view.findViewById(R.id.iv_share);
        iv_baidu.setOnClickListener(this);
        iv_walk.setOnClickListener(this);
        iv_bus.setOnClickListener(this);
        iv_car.setOnClickListener(this);
        iv_share.setOnClickListener(this);
        InfoWindow infoWindow = new InfoWindow(view, position, -20);
        baiduMap.showInfoWindow(infoWindow);
        return true;
    }

    private BDLocation mCurrentLocation;
    private boolean isFirst = true;

    // 定位
    private class MyLocationListener implements BDLocationListener {

        @Override
        public void onReceiveLocation(final BDLocation location) {
            //获取定位结果
            if (isFirst) {
                // 让地图中心跑到我的真实位置处
                LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                MapStatusUpdate mapStatusUpdate = MapStatusUpdateFactory.newLatLngZoom(latLng, 12);
                // 没有动画效果
                // mBaiduMap.setMapStatus(mapStatusUpdate);
                baiduMap.animateMapStatus(mapStatusUpdate, 2000);
                isFirst = false;
            }
            mCurrentLocation = location;
            if (mCurrentLocation != null) {
                MyApp.getMainThreadHandler().post(new Runnable() {
                    @Override
                    public void run() {
                        if (!TextUtils.isEmpty(location.getAddrStr())) {
                            tv_info.setText(getString(R.string.string_map_info) + "：" + location.getAddrStr());
                        }
                    }
                });
                MyLocationData.Builder myLocationDataBuilder = new MyLocationData.Builder();
                myLocationDataBuilder.accuracy(mCurrentLocation.getRadius());
                myLocationDataBuilder.latitude(mCurrentLocation.getLatitude());
                myLocationDataBuilder.longitude(mCurrentLocation.getLongitude());
                MyLocationData myLocationData = myLocationDataBuilder.build();
                baiduMap.setMyLocationEnabled(true);
                baiduMap.setMyLocationData(myLocationData);
            }
        }

        @Override
        public void onConnectHotSpotMessage(String s, int i) {

        }
    }

    // 简索
    private OnGetPoiSearchResultListener poiListener = new OnGetPoiSearchResultListener() {
        @Override
        public void onGetPoiResult(PoiResult result) {
            // 获取POI检索结果
            baiduMap.clear();
            PoiOverlay poiOverlay = new PoiOverlay(baiduMap);
            poiOverlay.setData(result);
            poiOverlay.addToMap();
            baiduMap.setMapStatus(MapStatusUpdateFactory.zoomTo(16));
        }

        @Override
        public void onGetPoiDetailResult(PoiDetailResult result) {
        }

        @Override
        public void onGetPoiIndoorResult(PoiIndoorResult poiIndoorResult) {
        }
    };

    // 分享位置
    private OnGetShareUrlResultListener shareUrlResultListener = new OnGetShareUrlResultListener() {

        public void onGetPoiDetailShareUrlResult(ShareUrlResult result) {
            //分享POI详情
        }

        public void onGetLocationShareUrlResult(ShareUrlResult result) {
            //分享位置信息
            String url = result.getUrl();
            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT, url);
            sendIntent.setType("text/plain");//
            startActivity(Intent.createChooser(sendIntent, UIUtils.getString(R.string.string_map_hint)));
        }

        @Override
        public void onGetRouteShareUrlResult(ShareUrlResult result) {
        }
    };

    // 定位配置
    private void initLocation() {
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
        //可选，默认高精度，设置定位模式，高精度，低功耗，仅设备

        option.setCoorType("bd09ll");
        //可选，默认gcj02，设置返回的定位结果坐标系

        int span = 10 * 1000;
        option.setScanSpan(span);
        //可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的

        option.setIsNeedAddress(true);
        //可选，设置是否需要地址信息，默认不需要

        option.setOpenGps(true);
        //可选，默认false,设置是否使用gps

        option.setLocationNotify(true);
        //可选，默认false，设置是否当GPS有效时按照1S/1次频率输出GPS结果

        option.setIsNeedLocationDescribe(true);
        //可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”

        option.setIsNeedLocationPoiList(true);
        //可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到

        option.setIgnoreKillProcess(false);
        //可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死

        option.SetIgnoreCacheException(false);
        //可选，默认false，设置是否收集CRASH信息，默认收集

        option.setEnableSimulateGps(false);
        //可选，默认false，设置是否需要过滤GPS仿真结果，默认需要

        mLocationClient.setLocOption(option);
    }

    // 导航
    private OnGetRoutePlanResultListener listener = new OnGetRoutePlanResultListener() {
        @Override
        public void onGetWalkingRouteResult(WalkingRouteResult result) {// 步行
            init();
            if (result.getRouteLines() == null) {
                Toast.makeText(getActivity(), R.string.string_map_nothing, Toast.LENGTH_SHORT).show();
                return;
            }
            walkingRouteOverlay = new WalkingRouteOverlay(baiduMap);
            WalkingRouteLine walkingRouteLine = result.getRouteLines().get(0);
            walkingRouteOverlay.setData(walkingRouteLine);
            walkingRouteOverlay.addToMap();
            walkingRouteOverlay.zoomToSpan();
        }

        @Override
        public void onGetTransitRouteResult(TransitRouteResult result) {// 交通
            init();
            if (result.getRouteLines() == null) {
                Toast.makeText(getActivity(), R.string.string_map_nothing, Toast.LENGTH_SHORT).show();
                return;
            }
            transitRouteOverlay = new TransitRouteOverlay(baiduMap);
            TransitRouteLine transitRouteLine = result.getRouteLines().get(0);
            transitRouteOverlay.setData(transitRouteLine);
            transitRouteOverlay.addToMap();
            transitRouteOverlay.zoomToSpan();
        }

        @Override
        public void onGetDrivingRouteResult(DrivingRouteResult result) {// 开车
            init();
            if (result == null && result.getRouteLines() == null && result.getRouteLines().size() == 0) {
                Toast.makeText(getActivity(), R.string.string_map_nothing, Toast.LENGTH_SHORT).show();
                return;
            }
            drivingRouteOverlay = new DrivingRouteOverlay(baiduMap);
            DrivingRouteLine drivingRouteLine = result.getRouteLines().get(0);
            drivingRouteOverlay.setData(result.getRouteLines().get(0));
            drivingRouteOverlay.addToMap();
            drivingRouteOverlay.zoomToSpan();
        }

        @Override
        public void onGetIndoorRouteResult(IndoorRouteResult indoorRouteResult) {// 室内
        }

        @Override
        public void onGetMassTransitRouteResult(MassTransitRouteResult massTransitRouteResult) {// 公交车
        }

        @Override
        public void onGetBikingRouteResult(BikingRouteResult bikingRouteResult) {// 自行车
        }

        public void init() {
            if (walkingRouteOverlay != null) {
                walkingRouteOverlay.removeFromMap();
                walkingRouteOverlay = null;
            }
            if (transitRouteOverlay != null) {
                transitRouteOverlay.removeFromMap();
                transitRouteOverlay = null;
            }
            if (drivingRouteOverlay != null) {
                drivingRouteOverlay.removeFromMap();
                drivingRouteOverlay = null;
            }
        }
    };

}
