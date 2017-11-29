package com.m2team.library.utils;

import android.content.Context;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.net.wifi.WifiManager.WifiLock;

import java.util.List;

public class WifiHelper {
    private WifiManager wifiManager;// ??Wifi????
    private WifiInfo wifiInfo;// Wifi??
    private List<ScanResult> scanResultList; // ???????????
    private List<WifiConfiguration> wifiConfigList;// ??????
    private WifiLock wifiLock;// Wifi?

    public WifiHelper(Context context){
        this.wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);// ??Wifi??
        // ??Wifi??
        this.wifiInfo = wifiManager.getConnectionInfo();// ??????
    }

    public boolean isWifiEnabled(){
        return wifiManager.isWifiEnabled();
    }

    public boolean openWifi(){
        if (!isWifiEnabled()) {
            return wifiManager.setWifiEnabled(true);
        } else {
            return false;
        }
    }

    public boolean closeWifi(){
        if (!isWifiEnabled()) {
            return true;
        } else {
            return wifiManager.setWifiEnabled(false);
        }
    }

    public void lockWifi() {
        wifiLock.acquire();
    }


    public void unLockWifi() {
        if (!wifiLock.isHeld()) {
            wifiLock.release(); // ????
        }
    }

    public void createWifiLock() {
        wifiLock = wifiManager.createWifiLock("flyfly"); // ????????
    }

    public void startScan() {
        wifiManager.startScan();
        scanResultList = wifiManager.getScanResults(); // ????????
        wifiConfigList = wifiManager.getConfiguredNetworks(); // ??????
    }

    public List<ScanResult> getWifiList() {
        return scanResultList;
    }

    public List<WifiConfiguration> getWifiConfigList() {
        return wifiConfigList;
    }

    public String lookupScanInfo() {
        StringBuilder scanBuilder = new StringBuilder();
        if(scanResultList == null){
            return "";
        }
        for (int i = 0; i < scanResultList.size(); i++) {
            ScanResult sResult = scanResultList.get(i);
            scanBuilder.append("???" + (i + 1));
            scanBuilder.append(" ");
            scanBuilder.append(sResult.toString());  //????
            scanBuilder.append("\n");
        }
        scanBuilder.append("--------------?????--------------------");
        for(int i=0;i<wifiConfigList.size();i++){
            scanBuilder.append(wifiConfigList.get(i).toString());
            scanBuilder.append("\n");
        }
        return scanBuilder.toString();
    }

    public String getSSID(int NetId){
        return scanResultList.get(NetId).SSID;
    }

    public String getBSSID(int NetId){
        return scanResultList.get(NetId).BSSID;
    }

    public int getFrequency(int NetId){
        return scanResultList.get(NetId).frequency;
    }

    public String getCapabilities(int NetId){
        return scanResultList.get(NetId).capabilities;
    }
    public int getLevel(int NetId){
        return scanResultList.get(NetId).level;
    }





    public String getMac() {
        return (wifiInfo == null) ? "" : wifiInfo.getMacAddress();
    }

    public String getBSSID() {
        return (wifiInfo == null) ? null : wifiInfo.getBSSID();
    }

    public String getSSID() {
        return (wifiInfo == null) ? null : wifiInfo.getSSID();
    }

    public int getCurrentNetId() {
        return (wifiInfo == null) ? null : wifiInfo.getNetworkId();
    }

    public String getWifiInfo() {
        return (wifiInfo == null) ? null : wifiInfo.toString();
    }

    public int getIP() {
        return (wifiInfo == null) ? null : wifiInfo.getIpAddress();
    }

    public boolean addNetWordLink(WifiConfiguration config) {
        int NetId = wifiManager.addNetwork(config);
        return wifiManager.enableNetwork(NetId, true);
    }

    public boolean disableNetWordLink(int NetId) {
        wifiManager.disableNetwork(NetId);
        return wifiManager.disconnect();
    }

    public boolean removeNetworkLink(int NetId) {
        return wifiManager.removeNetwork(NetId);
    }

    public void hiddenSSID(int NetId){
        wifiConfigList.get(NetId).hiddenSSID=true;
    }

    public void displaySSID(int NetId){
        wifiConfigList.get(NetId).hiddenSSID=false;
    }
}
