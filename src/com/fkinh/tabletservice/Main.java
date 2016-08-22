package com.fkinh.tabletservice;

import com.fkinh.tabletservice.client.TabletClient;

public class Main {

    public static void main(String[] args) {
        TabletClient client = TabletClient.getInstance();
        client.send("hehe");
    }
}
